package com.wipro.shopforhome.dto.cart;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartDTO {
    private Long id;
    private @NotNull Long productId;
    private @NotNull int quantity;
}

