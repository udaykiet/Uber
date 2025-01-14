package com.ups.uber.services;

import com.ups.uber.entities.User;
import com.ups.uber.exceptions.ResourceNotFoundException;
import com.ups.uber.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .orElseThrow(()-> new ResourceNotFoundException("user not found with email:"+username));

    }

    public User getUserById(Long userId){
       return userRepository.findById(userId)
               .orElseThrow(()-> new BadCredentialsException("user not found wth id:" + userId));
    }
}
