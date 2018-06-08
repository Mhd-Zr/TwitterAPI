package com.example.mzreikat.twitterapi;

import com.google.gson.annotations.SerializedName;

public class OAuthToken {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    private String getAccessToken() {
        return accessToken;
    }

    private String getTokenType() {
        return tokenType;
    }

    public String getAuthorization() {
        return getTokenType() + " " + getAccessToken();
    }
}