package com.wipro.shopforhome.controller;


import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.wipro.shopforhome.dto.checkout.CheckoutItemDTO;
import com.wipro.shopforhome.dto.checkout.StripeResponse;
import com.wipro.shopforhome.dto.wishlist.order.OrderDTO;
import com.wipro.shopforhome.model.User;
import com.wipro.shopforhome.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> getCheckoutList(
            @RequestBody List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException {
        Session session = this.orderService.createSession(checkoutItemDTOList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    @PostMapping("/place-order")
    public ResponseEntity<OrderDTO> placeOrder(User user, String sessionId) {
        OrderDTO orderDTO = this.orderService.placeOrder(user, sessionId);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@RequestParam("token") String token) {
        List<OrderDTO> orderDTOList = this.orderService.getAllOrders(token);
        return new ResponseEntity<>(orderDTOList, HttpStatus.OK);
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        OrderDTO orderDTO = this.orderService.getOrderById(orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }
}
