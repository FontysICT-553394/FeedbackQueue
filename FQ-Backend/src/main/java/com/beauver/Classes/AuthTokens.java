package com.beauver.Classes;

public class AuthTokens {
    public String accessToken;
    public String refreshToken;

    public AuthTokens() {}

    public AuthTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}