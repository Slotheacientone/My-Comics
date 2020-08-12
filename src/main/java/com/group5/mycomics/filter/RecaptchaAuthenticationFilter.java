package com.group5.mycomics.filter;

import com.group5.mycomics.service.CaptchaService;
import com.group5.mycomics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

public class RecaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String ip = request.getRemoteAddr();
        System.out.println(ip);
        String googleResponse = request.getParameter("g-recaptcha-response");
        System.out.println(googleResponse);
        boolean success = captchaService.isSuccess(googleResponse, CaptchaService.LOGIN_ACTION, ip);
        if (success) {
            super.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
                @Override
                public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                    request.getServletContext().getRequestDispatcher("/login.html?error=true").forward(request, response);
                }
            });
            super.setRememberMeServices(new PersistentTokenBasedRememberMeServices("myComic",userService,persistentTokenRepository()));
            return super.attemptAuthentication(request, response);
        }
        try {
            request.getServletContext().getRequestDispatcher("/login.html?captcha-error=true").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(false);
        return null;
    }

    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(this.dataSource);
        return persistentTokenRepository;
    }
}
