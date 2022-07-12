package com.wipro.shopforhome.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.wipro.shopforhome.dto.cart.CartDTO;
import com.wipro.shopforhome.dto.cart.CartItemDTO;
import com.wipro.shopforhome.dto.checkout.CheckoutItemDTO;
import com.wipro.shopforhome.dto.wishlist.order.OrderDTO;
import com.wipro.shopforhome.exception.ResourceNotFoundException;
import com.wipro.shopforhome.model.Order;
import com.wipro.shopforhome.model.OrderItem;
import com.wipro.shopforhome.model.Product;
import com.wipro.shopforhome.model.User;
import com.wipro.shopforhome.repository.OrderItemRepository;
import com.wipro.shopforhome.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Value("${BASE_URL}")
    private String baseURL;

    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    public Session createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException {
        // success and failure urls that will be created using frontend
        String successURL = baseURL + "payment/success";
        String failureURL = baseURL + "payment/failure";

        Stripe.apiKey=apiKey;

        List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();

        checkoutItemDTOList.forEach(checkoutItemDTO -> {
            sessionItemList.add(createSessionLineItem(checkoutItemDTO));
        });

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failureURL)
                .setSuccessUrl(successURL)
                .addAllLineItem(sessionItemList)
                .build();
        return Session.create(params);
    }

    private SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDTO checkoutItemDTO) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDTO))
                .setQuantity((long) checkoutItemDTO.getQuantity())
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDTO checkoutItemDTO) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long) (checkoutItemDTO.getPrice()*100))
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(checkoutItemDTO.getProductName())
                        .build()
                ).build();
    }

    public OrderDTO getOrderDTO(Order order) {
        List<CartItemDTO> cartItemDTOList = this.cartService.getCartItemDTO(order.getUser());
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setOrderItems(cartItemDTOList);
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setSessionId(order.getSessionId());
        orderDTO.setCreatedDate(new Date());
        return orderDTO;
    }

    public OrderDTO placeOrder(User user, String sessionId) {
        CartDTO cartDTO = this.cartService.getCartItems(user);

        List<CartItemDTO> cartItemsDtoList = cartDTO.getCartItems();

        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDTO.getTotalCost());

        orderRepository.save(newOrder);

        cartItemsDtoList.forEach(cartItemDTO -> {
            Product product = this.productService.getProductById(cartItemDTO.getProductDTO().getProductId());
            OrderItem orderItem = new OrderItem(cartItemDTO.getQuantity(),
                    cartItemDTO.getProductDTO().getPrice(), newOrder, product);
            orderItemRepository.save(orderItem);
        });

        this.cartService.deleteUserCartItems(user);

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(newOrder.getId());
        orderDTO.setCreatedDate(new Date());
        orderDTO.setOrderItems(cartItemsDtoList);
        orderDTO.setSessionId(newOrder.getSessionId());
        orderDTO.setTotalPrice(newOrder.getTotalPrice());
        orderDTO.setUserId(newOrder.getUser().getId());

        return orderDTO;
    }

    public List<OrderDTO> getAllOrders(String token) {

        this.authenticationService.authenticate(token);
        User user = this.authenticationService.getUser(token);

        List<Order> orderList = this.orderRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<OrderDTO> orderDTOList = new ArrayList<>();

        orderList.forEach(order -> {
            orderDTOList.add(getOrderDTO(order));
        });

        return orderDTOList;
    }

    public OrderDTO getOrderById(Long orderId) {
        Order order = this.orderRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Order does not exist"));
        return getOrderDTO(order);
    }

}
