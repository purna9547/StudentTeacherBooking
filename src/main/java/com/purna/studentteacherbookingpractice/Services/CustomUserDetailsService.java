package com.purna.studentteacherbookingpractice.Services;


import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Repositries.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String emailUsername) throws UsernameNotFoundException {
        Users users= userRepo.findByEmailOrUserName(emailUsername,emailUsername);
//        System.out.println(emailUsername);
//        System.out.println("jhgfd");
//        System.out.println(users);
        if (users==null){
            throw new UsernameNotFoundException("User not found error");

        }
        if(users.getAction().equals("Pending")){
            throw new DisabledException("User is not approved");
        }


        Collection<? extends GrantedAuthority> authorities = authorities(users);

        return new org.springframework.security.core.userdetails.User(users.getUserName(), users.getPassword(), authorities);
    }
    public Collection<? extends GrantedAuthority> authorities(Users users) {
        String rolesString = users.getRole().toString();
        String[] rolesArray = rolesString.split(",");

        return Arrays.stream(rolesArray).map(role -> new SimpleGrantedAuthority("ROLE_" + role.trim())).collect(Collectors.toList());
    }
}
