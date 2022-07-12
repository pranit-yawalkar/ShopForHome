package com.wipro.shopforhome.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemDTO {

    private String productName;
    private int quantity;
    private Double price;
    private Long productId;
    private Long userId;
}
