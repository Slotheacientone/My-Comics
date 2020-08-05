package com.group5.mycomics.controller;

import com.group5.mycomics.common.RestFB;
import com.group5.mycomics.entity.User;
import com.group5.mycomics.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@Controller
public class UserController {
    @Autowired
    private RestFB restFb;

    @Autowired
    private UserService userService;

    HashMap<String, String> maps = new HashMap<String, String>();

    @RequestMapping(value = {"/", "/index.html"})
    public String index(HttpSession session, Principal principal) {
        return "user/index";
    }

    @RequestMapping(value = "/login.html")
    public String loginPage() {
        return "user/login";
    }

    @RequestMapping(value = "/verify.html")
    public String verifyPage() {
        return "user/verify";
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
        if (userService.checkUserExist(email)) {
            String pwd = RandomStringUtils.randomAscii(15, 25);
            System.out.println("run controller with password: " + pwd);
            userService.sendForgotPasswordEmail(email, pwd);
            userService.changePassword(email, pwd);
            return "/user/login.html";
        }
        return "/user/forgot-password.html";
    }

    @RequestMapping(value = "/register-servlet", method = RequestMethod.POST)
    public String register(HttpSession session, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("name") String name) {
        if (userService.checkUserExist(email))
            return "redirect:/";
        String code = RandomStringUtils.randomAlphanumeric(6);

        //send mail here
        userService.sendVerifyEmail(email, code);

        maps.put(email, code);
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        session.setAttribute("name", name);

        return "/user/verify.html";
    }

    @RequestMapping(value = "/verify-servlet", method = RequestMethod.POST)
    public String verify(@RequestParam("verify-code") String code, @RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("name") String name) {
        if (!maps.get(email).contentEquals(code)) {
            return "user/verify.html";
        }
        User user = new User(email, password, "ROLE_USER", name);
        userService.addUser(user);
        return "redirect:/";
    }

    @RequestMapping("/login-facebook")
    public String loginFacebook(HttpServletRequest request) throws ClientProtocolException, IOException, IOException {
        String code = request.getParameter("code");
        if (code == null || code.isEmpty()) {
            return "redirect:/login.html?facebook=error";
        }
        String accessToken = restFb.getToken(code);
        com.restfb.types.User userFb = restFb.getUserInfo(accessToken);

        if (!userService.checkUserExist(userFb.getId() + "@gmail.com")) {
            String email = userFb.getId() + "@gmail.com";
            String pwd = RandomStringUtils.randomAscii(25, 45);
            String role = "ROLE_USER";
            User u = new User(email, pwd, role, userFb.getName());
            userService.addUser(u);
        }
        return "redirect:/";
    }

}
