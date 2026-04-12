package com.springboot.user_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.user_app.dto.UserResponseDto;
import com.springboot.user_app.entity.Product;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.response.ResponseStructure;
import com.springboot.user_app.service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	ProductService proService;
	
	@PostMapping("/create")
	public ResponseEntity<?> registerUser(@RequestBody Product p){
		ResponseStructure<Product> structure=proService.createProduct(p);
		return new ResponseEntity<>(structure,HttpStatus.CREATED);
		
	}
	@GetMapping("/getAllProduct")
	public ResponseEntity<?> getAllProduct(){
		ResponseStructure <List<Product>> structure=proService.getAllProduct();
		return new ResponseEntity<>(structure,HttpStatus.OK);
		
	}
	@GetMapping("/getProductById")
		public ResponseEntity<?> getproductById(@RequestParam Long proId){
			ResponseStructure<Product> structure=proService.getProductById(proId);
			return new ResponseEntity<>(structure,HttpStatus.FOUND);
					
	}
	@GetMapping("/getProductByName")
	public ResponseEntity<?> getProductByName(@RequestParam String name){
		ResponseStructure<Product> structure=proService.getProductByName(name);
		return new ResponseEntity<>(structure,HttpStatus.FOUND);
	}
	@GetMapping("/getProductByType")
	public ResponseEntity<?> getProductByType(@RequestParam String type){
		ResponseStructure<List<Product>> structure=proService.getProductByType(type);
		return new ResponseEntity<>(structure,HttpStatus.FOUND);
	}
	
	@PutMapping("/updateProduct")
	public ResponseEntity<?> updateProdcut(@RequestBody Product p) {
		ResponseStructure<Product> structure = proService.updateProduct(p);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteProduct/{proId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long proId) {
		ResponseStructure<String> structure = proService.deleteProduct(proId);
		return new ResponseEntity<>(structure, HttpStatus.OK);

	}

}
