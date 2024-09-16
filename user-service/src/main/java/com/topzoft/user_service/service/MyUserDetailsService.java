package com.topzoft.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.topzoft.user_service.model.User;
import com.topzoft.user_service.model.UserPrincipal;
import com.topzoft.user_service.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = repository.findByEmail(username);
		
		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		
		return new UserPrincipal(user) ;
	}

}
