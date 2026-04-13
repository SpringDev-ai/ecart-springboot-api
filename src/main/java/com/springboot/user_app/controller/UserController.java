package com.springboot.user_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.user_app.dto.UserLoginRequestDto;
import com.springboot.user_app.dto.UserResponseDto;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.repository.UserRepository;
import com.springboot.user_app.response.ResponseStructure;
import com.springboot.user_app.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User u){
		ResponseStructure<UserResponseDto> structure=userService.registeruser(u);
		return new ResponseEntity<>(structure,HttpStatus.CREATED);
		
	}
	@GetMapping("/getAllUser")
	public ResponseEntity<?> getAllUser(){
		ResponseStructure <List<User>> structure=userService.getAllUser();
		return new ResponseEntity<>(structure,HttpStatus.OK);
		
	}
	@GetMapping("/getUserById")
		public ResponseEntity<?> getUserByID(@RequestParam Long UserId){
			ResponseStructure<UserResponseDto> structure=userService.getUserById(UserId);
			return new ResponseEntity<>(structure,HttpStatus.FOUND);
					
	}
	@PostMapping("/userLogin")
	public ResponseEntity<?> userLogin(@RequestBody UserLoginRequestDto u){
		
		ResponseStructure<UserResponseDto> structure=userService.userLogin(u.getEmail(), u.getPassword());
		return new ResponseEntity<>(structure,HttpStatus.OK);
	}
	
	@PutMapping("/updateUser")
	public ResponseEntity<?> updateStudent(@RequestBody User u) {
		ResponseStructure<UserResponseDto> structure = userService.updateUserService(u);
		return new ResponseEntity<>(structure, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteUser/{uId}")
	public ResponseEntity<?> deleteStudent(@PathVariable Long uId) {
		ResponseStructure<UserResponseDto> structure = userService.deleteUserService(uId);
		return new ResponseEntity<>(structure, HttpStatus.OK);

	}
	
}
