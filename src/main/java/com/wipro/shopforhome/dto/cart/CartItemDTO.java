package com.wipro.shopforhome.dto.cart;

import com.sun.istack.NotNull;
import com.wipro.shopforhome.dto.ProductDTO;
import com.wipro.shopforhome.model.Cart;
import com.wipro.shopforhome.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long id;
    private @NotNull int quantity;
    private ProductDTO productDTO;

    public CartItemDTO(Cart cart, ProductDTO productDTO) {
        this.id = cart.getId();
        this.quantity = cart.getQuantity();
        this.productDTO = productDTO;
    }

}
