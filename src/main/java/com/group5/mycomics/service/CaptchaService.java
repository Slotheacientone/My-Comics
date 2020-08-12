package com.group5.mycomics.service;

import com.group5.mycomics.config.CaptchaConfig;
import com.group5.mycomics.entity.GoogleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class CaptchaService {

    public static final String LOGIN_ACTION = "login";

    @Autowired
    private CaptchaConfig captchaConfig;

    @Autowired
    private RestTemplate restTemplate;

    public boolean isSuccess(String response, String action, String ip) {
        URI verifyUri = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", captchaConfig.getSecret(), response, ip));
        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
        System.out.println(googleResponse.isSuccess());
        System.out.println(googleResponse.getScore());
        return googleResponse.isSuccess() && googleResponse.getAction().equals(action)
                && !(googleResponse.getScore() < captchaConfig.getThreshold());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
