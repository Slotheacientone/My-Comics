package com.group5.mycomics.service;

import com.group5.mycomics.dao.UserDao;
import com.group5.mycomics.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private MailService mailService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findUser(email);
        if(user==null){
            System.out.println("User not found: " + email);
            throw new UsernameNotFoundException("User with email: " + email + "was not found in database");
        }
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(grantedAuthority);
        UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),list);
        return userDetails;
    }

    public boolean checkUserExist(String email){
        User user = userDao.findUser(email);
        return user != null;
    }

    public void sendForgotPasswordEmail(String email, String password) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your new password");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText("Your new random password: " + password);
        System.out.println("run send forgot password");
        mailService.sendEmail(mailMessage);
    }

    public void sendVerifyEmail(String email, String code) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Your verify code");
        mailMessage.setFrom("<MAIL>");
        mailMessage.setText("Your verify code: " + code);
        mailService.sendEmail(mailMessage);
    }

    public void addUser(User user){
        BCryptPasswordEncoder crypt = new BCryptPasswordEncoder(12);
        String pwdEncode = crypt.encode(user.getPassword());
        user.setPassword(pwdEncode);
        userDao.addUser(user);
    }

    public User findUser(String email){
        return userDao.findUser(email);
    }

    public void changePassword(String email, String password){
        userDao.updateUserPassword(email, password);
    }
}
