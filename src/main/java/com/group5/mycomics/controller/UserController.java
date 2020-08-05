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
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping(value = "/")
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

    @RequestMapping(value = "/forgot-password.html")
    public String forgotPasswordPage() {
        return "user/forgot-password";
    }

    @RequestMapping(value = "/forgot-password-servlet", method = RequestMethod.POST)
    public String forgotPassword(@RequestParam("email") String email) {
        System.out.println(email);
       if(userService.checkUserExist(email)) {
           String pwd = RandomStringUtils.randomAscii(15, 25);
           System.out.println("run controller with password: " + pwd);
           userService.sendForgotPasswordEmail(email, pwd);
           userService.changePassword(email, pwd);
           return "/user/login.html";
       }
       return "/user/forgot-password.html";
    }

    @RequestMapping("/login-facebook")
    public String loginFacebook(HttpServletRequest request) throws ClientProtocolException, IOException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return "redirect:/login.html?facebook=error";
        }
        String accessToken = restFb.getToken(code);
        com.restfb.types.User userFb = restFb.getUserInfo(accessToken);

        if (!userService.checkUserExist(userFb.getId()+"@gmail.com")){
            String email = userFb.getId()+"@gmail.com";
            String pwd = RandomStringUtils.randomAscii(25,45);
            BCryptPasswordEncoder crypt = new BCryptPasswordEncoder(12);
            String pwdEncode = crypt.encode(pwd);
            String role = "ROLE_USER";
            User u = new User(email,pwdEncode,role,userFb.getName(),true);
            userDao.addUser(u);
        }
        return "redirect:/";
    }

}
