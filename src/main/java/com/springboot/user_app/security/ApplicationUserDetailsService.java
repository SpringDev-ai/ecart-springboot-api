package com.springboot.user_app.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.user_app.entity.Role;
import com.springboot.user_app.repository.UserRepository;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public ApplicationUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.springboot.user_app.entity.User user = userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

		Role role = user.getRole() == null ? Role.USER : user.getRole();
		return User.withUsername(user.getEmail())
				.password(user.getPassword())
				.authorities(new SimpleGrantedAuthority("ROLE_" + role.name()))
				.build();
	}
}
