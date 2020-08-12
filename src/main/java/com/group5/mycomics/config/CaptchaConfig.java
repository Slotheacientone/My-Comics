package com.group5.mycomics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
public class CaptchaConfig {
    private String site;
    private String secret;
    private float threshold;

    public String getSite() {
        return site;
    }

    public String getSecret() {
        return secret;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}
