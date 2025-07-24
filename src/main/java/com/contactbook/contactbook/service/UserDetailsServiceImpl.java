package com.contactbook.contactbook.service;

import com.contactbook.contactbook.entity.User;
import com.contactbook.contactbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> userOptional;
        if (usernameOrEmail.contains("@")) {
            userOptional = userRepository.findByEmail(usernameOrEmail);
        } else {
            userOptional = userRepository.findByUsername(usernameOrEmail);
        }

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User not found with identifier: " + usernameOrEmail)
        );

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
