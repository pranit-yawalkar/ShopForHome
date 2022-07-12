package com.wipro.shopforhome.repository;

import com.wipro.shopforhome.model.AuthenticationToken;
import com.wipro.shopforhome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<AuthenticationToken, Long> {

    AuthenticationToken findByUser(User user);

    AuthenticationToken findByToken(String token);
}
