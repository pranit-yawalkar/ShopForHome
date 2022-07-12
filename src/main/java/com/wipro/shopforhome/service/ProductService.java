package com.wipro.shopforhome.service;

import com.wipro.shopforhome.dto.ProductDTO;
import com.wipro.shopforhome.exception.ResourceNotFoundException;
import com.wipro.shopforhome.model.Category;
import com.wipro.shopforhome.model.Product;
import com.wipro.shopforhome.repository.CategoryRepository;
import com.wipro.shopforhome.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setImageUrl(productDTO.getImageUrl());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(this.categoryService.getCategoryById(productDTO.getCategoryId()));
        return this.productRepository.save(product);
    }

    public ProductDTO getProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getId());
        productDTO.setProductName(product.getProductName());
        productDTO.setImageUrl(product.getImageUrl());
        productDTO.setPrice(product.getPrice());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategoryId(product.getCategory().getId());
        return productDTO;
    }

    public Product getProductById(Long id) {
       return this.productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product does not exist"));
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = this.productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();
        products.forEach(product -> {
            productDTOS.add(getProductDTO(product));
        });
        return productDTOS;
    }

    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product does not exist"));
        product.setProductName(productDTO.getProductName());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setDescription(productDTO.getDescription());
        product.setCategory(this.categoryService.getCategoryById(productDTO.getCategoryId()));
        return this.productRepository.save(product);
    }
}
