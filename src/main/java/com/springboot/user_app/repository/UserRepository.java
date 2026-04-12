package com.springboot.user_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.user_app.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	

	 public Optional<User> findByEmail(String email);

}
