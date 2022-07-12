package com.wipro.shopforhome.controller;

import com.wipro.shopforhome.dto.ProductDTO;
import com.wipro.shopforhome.model.Category;
import com.wipro.shopforhome.model.Product;
import com.wipro.shopforhome.repository.CategoryRepository;
import com.wipro.shopforhome.service.CategoryService;
import com.wipro.shopforhome.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
//        Category category = categoryService.getCategoryById(productDTO.getCategoryId());
        Product response = productService.createProduct(productDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> productDTOS = this.productService.getAllProducts();
        return new ResponseEntity<>(productDTOS, HttpStatus.OK);
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        Product product = this.productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
