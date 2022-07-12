package com.wipro.shopforhome.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wipro.shopforhome.model.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
