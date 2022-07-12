package com.wipro.shopforhome.dto.wishlist;

import com.wipro.shopforhome.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishListItemDTO {
    private Long id;
    private ProductDTO productDTO;
    private Date createdDate;

    public WishListItemDTO(Long id, ProductDTO productDTO) {
        this.id = id;
        this.productDTO = productDTO;
        this.createdDate = new Date();
    }
}
