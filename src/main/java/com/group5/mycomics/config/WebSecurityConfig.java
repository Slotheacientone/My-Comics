package com.group5.mycomics.config;

import com.group5.mycomics.filter.RecaptchaAuthenticationFilter;
import com.group5.mycomics.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Autowired
    private DataSource dataSource;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public RecaptchaAuthenticationFilter recaptchaAuthenticationFilter() throws Exception {
        RecaptchaAuthenticationFilter recaptchaAuthenticationFilter = new RecaptchaAuthenticationFilter();
        recaptchaAuthenticationFilter.setAuthenticationManager(authenticationManagerBean());
        return recaptchaAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/index.html", "/login.html", "/logout").permitAll();
        http.addFilterBefore(recaptchaAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests().and().formLogin().loginPage("/login.html").loginProcessingUrl("/login").defaultSuccessUrl("/index.html").and().logout().logoutUrl("/logout").logoutSuccessUrl("/index.html");
        http.authorizeRequests().and().rememberMe().tokenRepository(this.persistentTokenRepository()).tokenValiditySeconds(30 * 24 * 60 * 60); // 1 month
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl persistentTokenRepository = new JdbcTokenRepositoryImpl();
        persistentTokenRepository.setDataSource(this.dataSource);
        return persistentTokenRepository;
    }

}
