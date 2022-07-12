package com.wipro.shopforhome.controller;

import com.wipro.shopforhome.common.ApiResponse;
import com.wipro.shopforhome.dto.cart.AddToCartDTO;
import com.wipro.shopforhome.dto.cart.CartDTO;
import com.wipro.shopforhome.model.Cart;
import com.wipro.shopforhome.service.CartService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart/")
public class CartController {

    @Autowired
    private CartService cartService;


    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody AddToCartDTO addToCartDTO,
                                          @RequestParam("token") String token) {

        Cart cart = this.cartService.addToCart(addToCartDTO, token);
        return new ResponseEntity<>(cart, HttpStatus.CREATED);
    }


    @GetMapping("/getAll/{token}")
    public ResponseEntity<CartDTO> getCartItems(@RequestParam("token") String token) {
        CartDTO cartDTO = this.cartService.getCartItems(token);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId,
                                                      @RequestParam("token") String token) {
        this.cartService.deleteCartItem(cartItemId, token);
        ApiResponse apiResponse = new ApiResponse(true, "Item deleted successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
