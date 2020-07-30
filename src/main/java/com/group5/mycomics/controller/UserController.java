package com.group5.mycomics.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {
    @RequestMapping(value= "/index.html")
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

}
