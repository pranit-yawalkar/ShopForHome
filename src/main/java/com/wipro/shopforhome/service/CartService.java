package com.wipro.shopforhome.service;

import com.wipro.shopforhome.common.ApiResponse;
import com.wipro.shopforhome.dto.ProductDTO;
import com.wipro.shopforhome.dto.cart.AddToCartDTO;
import com.wipro.shopforhome.dto.cart.CartDTO;
import com.wipro.shopforhome.dto.cart.CartItemDTO;
import com.wipro.shopforhome.exception.CustomException;
import com.wipro.shopforhome.exception.ResourceNotFoundException;
import com.wipro.shopforhome.model.Cart;
import com.wipro.shopforhome.model.Product;
import com.wipro.shopforhome.model.User;
import com.wipro.shopforhome.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthenticationService authenticationService;

    public Cart addToCart(AddToCartDTO addToCartDTO, String token) {

        authenticationService.authenticate(token);

        User user = authenticationService.getUser(token);

        // validate if the product id is valid
        Product product = this.productService.getProductById(addToCartDTO.getProductId());
        ProductDTO productDTO = this.productService.getProductDTO(product);
        Cart cart = new Cart(addToCartDTO.getQuantity(), product, user);

        // save the cart
        return this.cartRepository.save(cart);
    }

    public CartDTO getCartItems(String token) {

        this.authenticationService.authenticate(token);

        User user = this.authenticationService.getUser(token);

        List<Cart> cartList = this.cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemDTO> cartItems = new ArrayList<>();

        double totalCost = 0;

        for(Cart cart: cartList) {
            ProductDTO productDTO = this.productService.getProductDTO(cart.getProduct());
            CartItemDTO cartItemDTO = new CartItemDTO(cart, productDTO);
            totalCost += (cartItemDTO.getQuantity() *cartItemDTO.getProductDTO().getPrice());
            cartItems.add(cartItemDTO);
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartItems(cartItems);
        cartDTO.setTotalCost(totalCost);

        return cartDTO;
    }


    public CartDTO getCartItems(User user) {
        List<Cart> cartList = this.cartRepository.findAllByUserOrderByCreatedDateDesc(user);

        List<CartItemDTO> cartItems = new ArrayList<>();

        double totalCost = 0;

        for(Cart cart: cartList) {
            ProductDTO productDTO = this.productService.getProductDTO(cart.getProduct());
            CartItemDTO cartItemDTO = new CartItemDTO(cart, productDTO);
            totalCost += (cartItemDTO.getQuantity() *cartItemDTO.getProductDTO().getPrice());
            cartItems.add(cartItemDTO);
        }

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartItems(cartItems);
        cartDTO.setTotalCost(totalCost);

        return cartDTO;
    }

    public List<CartItemDTO> getCartItemDTO(User user) {
        List<Cart> cartList = this.cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDTO> cartItems = new ArrayList<>();
        for(Cart cart: cartList) {
            ProductDTO productDTO = this.productService.getProductDTO(cart.getProduct());
            CartItemDTO cartItemDTO = new CartItemDTO(cart, productDTO);
            cartItems.add(cartItemDTO);
        }
        return cartItems;
    }

    public void deleteCartItem(Long cartItemId, String token) {
        this.authenticationService.authenticate(token);

        User user = this.authenticationService.getUser(token);

        Cart cart = this.cartRepository.findById(cartItemId)
                .orElseThrow(()->new ResourceNotFoundException("Cart item id is invalid"));

        if(cart.getUser()!=user) {
            throw new CustomException("Cart item does not belong to user");
        }

        this.cartRepository.delete(cart);
    }

    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }
}
