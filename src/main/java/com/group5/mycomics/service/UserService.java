package com.group5.mycomics.service;

import com.group5.mycomics.dao.UserDao;
import com.group5.mycomics.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findUser(email);
        if(user==null){
            System.out.println("User not found");
            throw new UsernameNotFoundException("User with email: " + email + "was not found in database");
        }

        return null;
    }
}
