package com.bct.utilities;

public class TokenCache {
    public String accessToken;
    public String refreshToken;
    public long accessTokenExpiry;
    long refreshTokenExpiry; // Tracks when refresh token will expire
    long refreshTokenIssuedAt; // Tracks when refresh token was originally issued
    public String responseCode;
    public String responseMessage;
    public String statusCode;
}
