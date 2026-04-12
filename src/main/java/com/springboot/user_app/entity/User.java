package com.springboot.user_app.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@NotEmpty(will check for empty string "" & null values)
	//@NotBlank
	private long userId;
	//@NotNull
	//@NotEmpty
	@NotBlank
	private String name;
	//@NotNull
	//@NotEmpty
	//@NotBlank
	@Email(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
	private String email;
	private String password;
	private String address;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Order> order;
}
