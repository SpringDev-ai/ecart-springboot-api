package com.springboot.user_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.user_app.entity.Product;



public interface ProductRepository extends JpaRepository<Product, Long>{

	public Optional<Product> findByName(String name);
	
	public List<Product> findByType(String type);
}
