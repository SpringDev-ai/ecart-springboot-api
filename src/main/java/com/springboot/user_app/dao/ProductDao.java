package com.springboot.user_app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.customException.ProductNotFoundException;
import com.springboot.user_app.entity.Product;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.repository.ProductRepository;


@Repository
public class ProductDao {
	@Autowired
	ProductRepository proRepo;
	
	public Product createProduct(Product p) {
		Product p1=proRepo.save(p);
		return p1;
		
	}
	
	public List<Product> getAllProduct(){
		List<Product> productList=proRepo.findAll();
			return productList;
	}
	
	public Optional<Product> getProductById(Long id) {
		return proRepo.findById(id);
	}
	public List<Product> getProductByType(String type){
		List<Product> productList=proRepo.findByType(type);
		return productList;
		
	}
	public Product getProductByName(String name){
		Optional<Product> op=proRepo.findByName(name);
		return	op.orElseThrow(()-> new IdNotFoundException());
		
	}
	public Product updateProduct(Product product) {
		Optional<Product> op = proRepo.findById(product.getProductId());
		if(op.isPresent()) {
			Product updateProduct = proRepo.save(product);
		}
			return op.orElseThrow(()-> new ProductNotFoundException());
		
	}
	public String deleteProduct(Long productId) {
		Optional<Product> op = proRepo.findById(productId);
			proRepo.deleteById(productId);
			//proRepo.delete(op.get());
			return "Product Deleted";
	}

}
