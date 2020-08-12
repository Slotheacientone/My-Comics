package com.group5.mycomics.entity;

import com.fasterxml.jackson.annotation.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "success",
        "score",
        "action",
        "challenge_ts",
        "hostname",
})
public class GoogleResponse {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("score")
    private float score;

    @JsonProperty("action")
    private String action;


    public boolean isSuccess() {
        return success;
    }

    public String getChallengeTs() {
        return challengeTs;
    }

    public String getHostname() {
        return hostname;
    }

    public float getScore() {
        return score;
    }

    public String getAction() {
        return action;
    }
}
