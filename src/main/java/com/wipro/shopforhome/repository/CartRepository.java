package com.wipro.shopforhome.repository;

import com.wipro.shopforhome.model.Cart;
import com.wipro.shopforhome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByUserOrderByCreatedDateDesc(User user);
    void deleteByUser(User user);
}
