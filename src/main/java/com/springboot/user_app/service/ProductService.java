package com.springboot.user_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.user_app.customException.ProductNotFoundException;
import com.springboot.user_app.dao.ProductDao;
import com.springboot.user_app.dto.UserResponseDto;
import com.springboot.user_app.entity.Product;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.response.ResponseStructure;


@Service
public class ProductService {
	@Autowired
	ProductDao proDao;
	
	@Autowired
	ModelMapper modelMapper;
	
	public ResponseStructure<Product> createProduct(Product p) {
		Product p1=proDao.createProduct(p);
		ResponseStructure<Product> structure = new ResponseStructure<Product>();
		if(p1!=null) {
			 structure.setData(p);
			 structure.setTime(LocalDateTime.now());
			 structure.setMessage("Product created");
			 structure.setHttpStatusCode(201);
		}
		return structure;
		
	}
	
	public ResponseStructure<List<Product>> getAllProduct(){
		List<Product> list=proDao.getAllProduct();
		ResponseStructure<List<Product>> structure=new ResponseStructure<>();
		structure.setData(list);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Products Details");
		structure.setHttpStatusCode(200);
		return structure;
		
	}
	
	public ResponseStructure<Product> getProductById(Long id){
		Optional<Product> getPro=proDao.getProductById(id);
		if(getPro.isPresent()) {
			Product p = getPro.get();
			ResponseStructure<Product> structure = new ResponseStructure<Product>();
				 structure.setData(p);
				 structure.setTime(LocalDateTime.now());
				 structure.setMessage("Product Found");
				 structure.setHttpStatusCode(200);
			
			return structure;
		}
		else {
			throw new ProductNotFoundException("ProductNotFound");
		}
	}
	public ResponseStructure<Product> getProductByName(String name){
		Product getPro=proDao.getProductByName(name);
		if(getPro!=null) {
			ResponseStructure<Product> structure = new ResponseStructure<Product>();
				 structure.setData(getPro);
				 structure.setTime(LocalDateTime.now());
				 structure.setMessage("Product Found");
				 structure.setHttpStatusCode(200);
			
			return structure;
		}
		else {
			throw new ProductNotFoundException("ProductNotFound");
		}
	}
	public ResponseStructure<List<Product>> getProductByType(String type){
		List<Product> getProList=proDao.getProductByType(type);
		if(getProList!=null) {
			ResponseStructure<List<Product>> structure = new ResponseStructure<List<Product>>();
				 structure.setData(getProList);
				 structure.setTime(LocalDateTime.now());
				 structure.setMessage("Product Found");
				 structure.setHttpStatusCode(200);
			
			return structure;
		}
		else {
			throw new ProductNotFoundException("ProductNotFound");
		}
	}
	public ResponseStructure<Product> updateProduct(Product p) {
		Product p1=proDao.updateProduct(p);
		ResponseStructure<Product> structure = new ResponseStructure<Product>();
		if(p1!=null) {
			 structure.setData(p);
			 structure.setTime(LocalDateTime.now());
			 structure.setMessage("Product Updated");
			 structure.setHttpStatusCode(201);
		}
		return structure;
	}
	public ResponseStructure<String> deleteProduct(Long proId) {
		Optional<Product> getProduct=proDao.getProductById(proId);
		if(getProduct.isPresent()) {
	    String deleteProduct = proDao.deleteProduct(proId);
		ResponseStructure<String> structure = new ResponseStructure<>();
			 structure.setData(deleteProduct);
			 structure.setTime(LocalDateTime.now());
			 structure.setMessage("User Deleted");
			 structure.setHttpStatusCode(201);
			 return structure;
		}
		else {
			throw new ProductNotFoundException("ProductNotFound");
		}
		
	}
}
