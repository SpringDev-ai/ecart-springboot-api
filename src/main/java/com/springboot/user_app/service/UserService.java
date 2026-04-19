package com.springboot.user_app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.customException.IncorrectPasswordException;
import com.springboot.user_app.dao.UserDao;
import com.springboot.user_app.dto.AuthResponseDto;
import com.springboot.user_app.dto.UserResponseDto;
import com.springboot.user_app.entity.Role;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.response.ResponseStructure;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;
	
	public ResponseStructure<UserResponseDto> registeruser(User u) {
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		if (u.getRole() == null) {
			u.setRole(Role.USER);
		}
		User u1=userDao.registerUser(u);
		UserResponseDto resDto =new UserResponseDto();
		ResponseStructure<UserResponseDto> structure = new ResponseStructure<UserResponseDto>();
		if(u1!=null) {
			 resDto = modelMapper.map(u, UserResponseDto.class);
			 structure.setData(resDto);
			 structure.setTime(LocalDateTime.now());
			 structure.setMessage("User Account created");
			 structure.setHttpStatusCode(201);
		}
		return structure;
		
	}
	public ResponseStructure<List<User>> getAllUser(){
		List<User> list=userDao.getAllUser();
//		List<UserResponseDto> resDto =new ArrayList<>();
//		ResponseStructure<List<UserResponseDto>> structure = new ResponseStructure<List<UserResponseDto>>();
//		resDto=(List<UserResponseDto>) modelMapper.map(list, UserResponseDto.class);
		ResponseStructure<List<User>> structure=new ResponseStructure<>();
		structure.setData(list);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("User Found");
		structure.setHttpStatusCode(200);
		return structure;
		
	}
	public ResponseStructure<UserResponseDto> getUserById(Long id){
		Optional<User> getUser=userDao.getUserById(id);
		if(getUser.isPresent()) {
			UserResponseDto resDto =new UserResponseDto();
			ResponseStructure<UserResponseDto> structure = new ResponseStructure<UserResponseDto>();
				 resDto = modelMapper.map(getUser.get(), UserResponseDto.class);
				 structure.setData(resDto);
				 structure.setTime(LocalDateTime.now());
				 structure.setMessage("User ID Found");
				 structure.setHttpStatusCode(200);
			
			return structure;
		}
		else {
			throw new IdNotFoundException("IdNotFound");
		}
	}
	public ResponseStructure<AuthResponseDto> userLogin(String email,String passowrd){
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, passowrd));
		} catch (BadCredentialsException e) {
			throw new IncorrectPasswordException();
		}

		User getUser=userDao.getuserByEmail(email);
		AuthResponseDto authResponseDto = new AuthResponseDto(getUser.getEmail(), getUser.getRole(), "BASIC");
		ResponseStructure<AuthResponseDto> structure = new ResponseStructure<>();
		structure.setData(authResponseDto);
		structure.setTime(LocalDateTime.now());
		structure.setMessage("Login Success");
		structure.setHttpStatusCode(200);
		return structure;
	}
	public ResponseStructure<UserResponseDto> updateUserService(User u) {
		User existingUser = userDao.getUserById(u.getUserId())
				.orElseThrow(() -> new IdNotFoundException("IdNotFound"));
		if (u.getPassword() == null || u.getPassword().isBlank()) {
			u.setPassword(existingUser.getPassword());
		} else {
			u.setPassword(passwordEncoder.encode(u.getPassword()));
		}
		if (u.getRole() == null) {
			u.setRole(existingUser.getRole());
		}
		User u1=userDao.updateUser(u);
		UserResponseDto resDto =new UserResponseDto();
		ResponseStructure<UserResponseDto> structure = new ResponseStructure<UserResponseDto>();
		if(u1!=null) {
			 resDto = modelMapper.map(u1, UserResponseDto.class);
			 structure.setData(resDto);
			 structure.setTime(LocalDateTime.now());
			 structure.setMessage("User Account Updated");
			 structure.setHttpStatusCode(201);
		}
		return structure;
	}
	public ResponseStructure<UserResponseDto> deleteUserService(Long userId) {
		Optional<User> getUser=userDao.getUserById(userId);
		if(getUser.isPresent()) {
	    userDao.deleteUser(userId);
		UserResponseDto resDto =new UserResponseDto();
		ResponseStructure<UserResponseDto> structure = new ResponseStructure<UserResponseDto>();
			 resDto = modelMapper.map(getUser.get(), UserResponseDto.class);
			 structure.setData(resDto);
			 structure.setTime(LocalDateTime.now());
			 structure.setMessage("User Deleted");
			 structure.setHttpStatusCode(201);
			 return structure;
		}
		else {
			throw new IdNotFoundException("IdNotFound");
		}
		
	}
}
