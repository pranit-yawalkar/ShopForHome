package com.wipro.shopforhome.dto.wishlist.order;

import com.sun.istack.NotNull;
import com.wipro.shopforhome.dto.cart.CartItemDTO;
import com.wipro.shopforhome.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private @NotNull Long id;
    private @NotNull Date createdDate;
    private @NotNull Double totalPrice;
    private @NotNull String sessionId;
    private @NotNull List<CartItemDTO> orderItems;
    private @NotNull Long userId;

}
