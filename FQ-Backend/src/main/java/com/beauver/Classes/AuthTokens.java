package com.beauver.Classes;

import com.google.gson.annotations.Expose;

public class AuthTokens {

    @Expose
    public String accessToken;

    @Expose
    public String refreshToken;

    public AuthTokens() {}

    public AuthTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}