package com.springboot.user_app.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.springboot.user_app.customException.EmailNotFoundException;
import com.springboot.user_app.customException.IdNotFoundException;
import com.springboot.user_app.entity.User;
import com.springboot.user_app.repository.UserRepository;

@Repository
public class UserDao {
	
	@Autowired
	UserRepository userRepo;
	
	public User registerUser(User u) {
		User u1=userRepo.save(u);
		return u1;
		
	}
	
	public List<User> getAllUser(){
		List<User> userList=userRepo.findAll();
			return userList;
	}
	
	public Optional<User> getUserById(Long id) {
		return userRepo.findById(id);
	}
	public User getuserByEmail(String mail){
		Optional<User> op=userRepo.findByEmail(mail);
		return	op.orElseThrow(EmailNotFoundException::new);
		
	}
	public User updateUser(User user) {
		Optional<User> op = userRepo.findById(user.getUserId());
		if(op.isPresent()) {
			return userRepo.save(user);
		}
		return op.orElseThrow(()-> new IdNotFoundException());
		
	}
	public String deleteUser(Long userId) {
		userRepo.deleteById(userId);
		return "Student Deleted";
	}

}
