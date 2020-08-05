package com.group5.mycomics.controller;

import com.group5.mycomics.common.RestFB;
import com.group5.mycomics.dao.UserDao;
import com.group5.mycomics.entity.User;
import com.group5.mycomics.service.MailService;
import com.group5.mycomics.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class UserController {
    @Autowired
    private RestFB restFb;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/index.html")
    public String index() {
        return "user/index";
    }

    @RequestMapping(value = "/login.html")
    public String loginPage() {
        return "user/login";
    }

    @RequestMapping(value = "/register.html")
    public String registerPage() {
        return "user/register";
    }

    @RequestMapping(value = "forgot-password.html")
    public String forgotPasswordPage() {
        return "user/forgot-password";
    }

    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public String forgotPassword(HttpServletRequest request) {
        String email = request.getParameter("email");
       if(userService.checkUserExist(email)) {
           String pwd = RandomStringUtils.randomAscii(15, 25);
           userService.sendForgotPasswordEmail(email, pwd);
           userService.changePassword(email, pwd);
           return "login.html";
       }
       return "forgot-password.html";
    }

    @RequestMapping("/login-facebook")
    public String loginFacebook(HttpServletRequest request) throws ClientProtocolException, IOException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return "redirect:/login.html?facebook=error";
        }
        String accessToken = restFb.getToken(code);
        com.restfb.types.User userFb = restFb.getUserInfo(accessToken);


        if (userService.checkUserExist(userFb.getId()+"@gmail.com")){
            String email = userFb.getId()+"@gmail.com";
            BCryptPasswordEncoder crypt = new BCryptPasswordEncoder(12);
            String pwdEncode = crypt.encode("22031999");
            String role = "ROLE_USER";
            User u = new User(email,pwdEncode,role,userFb.getName(),true);
            userDao.addUser(u);
        }
        return "redirect:/";
    }

}
