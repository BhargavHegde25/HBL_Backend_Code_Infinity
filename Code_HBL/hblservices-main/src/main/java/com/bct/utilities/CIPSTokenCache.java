package com.bct.utilities;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CIPSTokenCache {
	 	public String accessToken;
	    public String refreshToken;
	    public long accessTokenExpiry;
	    long refreshTokenExpiry; // Tracks when refresh token will expire
	    long refreshTokenIssuedAt; // Tracks when refresh token was originally issued
	    public String responseCode;
	    public String responseMessage;
	    public String statusCode;
		@Override
		public String toString() {
			return "CIPSTokenCache [accessToken=" + accessToken + ", refreshToken=" + refreshToken
					+ ", accessTokenExpiry=" + accessTokenExpiry + ", refreshTokenExpiry=" + refreshTokenExpiry
					+ ", refreshTokenIssuedAt=" + refreshTokenIssuedAt + ", responseCode=" + responseCode
					+ ", responseMessage=" + responseMessage + ", statusCode=" + statusCode + "]";
		}
	    
	    

}
