package com.wipro.shopforhome.controller;

import com.wipro.shopforhome.common.ApiResponse;
import com.wipro.shopforhome.dto.wishlist.AddToWishListDTO;
import com.wipro.shopforhome.dto.wishlist.WishListDTO;
import com.wipro.shopforhome.dto.wishlist.WishListItemDTO;
import com.wipro.shopforhome.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist/")
public class WishListController {

    @Autowired
    private WishListService wishListService;


    // save product in wishlist
    @PostMapping("/add")
    public ResponseEntity<WishListItemDTO> addToWishList(@RequestBody AddToWishListDTO addToWishListDTO,
                                                             @RequestParam("token") String token) {

        WishListItemDTO wishListItemDTO = wishListService.createWishList(addToWishListDTO, token);
        return new ResponseEntity<>(wishListItemDTO, HttpStatus.CREATED);
    }

    // get all products from wishlist
    @GetMapping("/getAll/{token}")
    public ResponseEntity<List<WishListDTO>> getWishListByUserOrderByCreatedDateDesc(@PathVariable String token) {

        List<WishListDTO> wishList = this.wishListService.getWishListByUserOrderByCreatedDateDesc(token);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }


    // delete product from wishlist
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<ApiResponse> deleteWishListItem(@PathVariable Long itemId,
                                                          @RequestParam("token") String token) {
        this.wishListService.deleteWishListItem(itemId, token);
        ApiResponse apiResponse = new ApiResponse(true, "Item deleted successfully");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
