package com.wipro.shopforhome.service;

import com.wipro.shopforhome.dto.ProductDTO;
import com.wipro.shopforhome.dto.wishlist.AddToWishListDTO;
import com.wipro.shopforhome.dto.wishlist.WishListDTO;
import com.wipro.shopforhome.dto.wishlist.WishListItemDTO;
import com.wipro.shopforhome.exception.CustomException;
import com.wipro.shopforhome.exception.ResourceNotFoundException;
import com.wipro.shopforhome.model.Product;
import com.wipro.shopforhome.model.User;
import com.wipro.shopforhome.model.WishList;
import com.wipro.shopforhome.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthenticationService authenticationService;


    public WishList createWishList(ProductDTO productDTO, String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        // check if product is already present in the wishlist of the same user
        List<WishList> wishLists = getWishListByUser(user);

        wishLists.forEach(wishList -> {
            if(wishList.getProduct().getId().equals(productDTO.getProductId())){
                throw new CustomException("Product already added to the wishlist");
            }
        });


        Product product = new Product();
        product.setId(productDTO.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setDescription(productDTO.getDescription());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        product.setCategory(this.categoryService.getCategoryById(productDTO.getCategoryId()));

        // save the item in wishlist
        WishList wishList = new WishList(user, product);


        return wishListRepository.save(wishList);
    }

    public WishListItemDTO createWishList(AddToWishListDTO addToWishListDTO, String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);

        // check if product is already present in the wishlist of the same user
        List<WishList> wishLists = getWishListByUser(user);

        wishLists.forEach(wishList -> {
            if(wishList.getProduct().getId().equals(addToWishListDTO.getId())){
                throw new CustomException("Product already added to the wishlist");
            }
        });

        Product product = this.productService.getProductById(addToWishListDTO.getProductId());

        // save the item in wishlist
        WishList wishList = new WishList(user, product);
        wishList = this.wishListRepository.save(wishList);
        ProductDTO productDTO = this.productService.getProductDTO(wishList.getProduct());
        return new WishListItemDTO(wishList.getId(), productDTO);
    }

    public List<WishList> getAllWishList() {
        return this.wishListRepository.findAll();
    }

    public List<WishList> getWishListByUser(User user) {
        return this.wishListRepository.getWishListByUser(user);
    }


    public List<WishListDTO> getWishListByUserOrderByCreatedDateDesc(String token) {
        // authenticate the token
        authenticationService.authenticate(token);

        // find the user
        User user = authenticationService.getUser(token);
        List<WishList> wishLists = this.wishListRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<WishListDTO> wishListDTOS = new ArrayList<>();

        wishLists.forEach(wishList -> {
            ProductDTO productDTO = this.productService.getProductDTO(wishList.getProduct());
            WishListDTO wishListDTO = new WishListDTO(wishList.getId(), productDTO);
            wishListDTOS.add(wishListDTO);
        });

        return wishListDTOS;
    }

    public void deleteWishListItem(Long itemId, String token) {
        this.authenticationService.authenticate(token);

        User user = this.authenticationService.getUser(token);

        WishList wishList = this.wishListRepository.findById(itemId)
                .orElseThrow(()->new ResourceNotFoundException("Item id is not valid"));

        if(wishList.getUser()!=user) {
            throw new CustomException("WishList does not belong to the user");
        }

        this.wishListRepository.delete(wishList);
    }
}
