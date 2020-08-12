package com.group5.mycomics.filter;

import com.group5.mycomics.service.CaptchaService;
import com.group5.mycomics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RecaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String ip = request.getRemoteAddr();
        System.out.println(ip);
        String googleResponse = request.getParameter("g-recaptcha-response");
        System.out.println(googleResponse);
        boolean success = captchaService.isSuccess(googleResponse, CaptchaService.LOGIN_ACTION, ip);
        System.out.println("response: " + success);
        if (success) {
            System.out.println(true);
            System.out.println(request.getParameter("email"));
            System.out.println(request.getParameter("password"));
            UserDetails userDetails = userService.loadUserByUsername(request.getParameter("email"));
            System.out.println(userDetails.getUsername());
            System.out.println(userDetails.getPassword());
            return super.attemptAuthentication(request, response);
        }
        try {
            request.getServletContext().getRequestDispatcher("/login.html").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(false);
        return null;
    }
}
