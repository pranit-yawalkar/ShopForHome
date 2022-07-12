package com.wipro.shopforhome.service;

import java.util.List;

import com.wipro.shopforhome.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wipro.shopforhome.model.Category;
import com.wipro.shopforhome.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;


	public List<Category> getAllCategories() {
		return this.categoryRepository.findAll();
	}

	public Category getCategoryById(Long id) {
		return this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category does not exist!"));
	}

	public Category createCategory(Category category) {
		return this.categoryRepository.save(category);
	}

	public Category updateCategory(Long id, Category category) {
		Category newCategory = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category does not exist!"));
		newCategory.setCategoryName(category.getCategoryName());
		newCategory.setDescription(category.getDescription());
		newCategory.setImageUrl(category.getImageUrl());
		return this.categoryRepository.save(newCategory);
	}

}
