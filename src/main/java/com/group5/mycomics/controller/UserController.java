package com.group5.mycomics.controller;

import com.group5.mycomics.common.RestFB;
import com.group5.mycomics.dao.UserDao;
import com.group5.mycomics.entity.User;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    private RestFB restFb;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value= "/")
    public String index(){
        return "user/index";
    }

    @RequestMapping(value= "/login.html")
    public String loginPage(){
        return "user/login";
    }

    @RequestMapping(value= "/register.html")
    public String registerPage(){
        return "user/register";
    }

    @RequestMapping("/login-facebook")
    public String loginFacebook(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return "redirect:/login.html?facebook=error";
        }
        String accessToken = restFb.getToken(code);
        com.restfb.types.User userFb = restFb.getUserInfo(accessToken);

        User user = null;
        if (user == null){
            String email = userFb.getId()+"@gmail.com";
            BCryptPasswordEncoder crypt = new BCryptPasswordEncoder(12);
            String pwdEncode = crypt.encode("22031999");
            String role = "ROLE_USER";
            User u = new User(email,pwdEncode,role,userFb.getName(),true);
            UserDao.addUser(u);
        }
        return "redirect:/user/index.html";
    }


}
