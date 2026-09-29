package com.hbl.productservicesExtn.utills;

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
	    public long refreshTokenExpiry; // Tracks when refresh token will expire
	    long refreshTokenIssuedAt; // Tracks when refresh token was originally issued
	    public String responseCode;
	    public String responseMessage;
	    public String statusCode;
	    
		@Override
		public int hashCode() {
			return Objects.hash(accessToken, accessTokenExpiry, refreshToken, refreshTokenExpiry, refreshTokenIssuedAt,
					responseCode, responseMessage, statusCode);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CIPSTokenCache other = (CIPSTokenCache) obj;
			return Objects.equals(accessToken, other.accessToken) && accessTokenExpiry == other.accessTokenExpiry
					&& Objects.equals(refreshToken, other.refreshToken)
					&& refreshTokenExpiry == other.refreshTokenExpiry
					&& refreshTokenIssuedAt == other.refreshTokenIssuedAt
					&& Objects.equals(responseCode, other.responseCode)
					&& Objects.equals(responseMessage, other.responseMessage)
					&& Objects.equals(statusCode, other.statusCode);
		}
		@Override
		public String toString() {
			return "CIPSTokenCache [accessToken=" + accessToken + ", refreshToken=" + refreshToken
					+ ", accessTokenExpiry=" + accessTokenExpiry + ", refreshTokenExpiry=" + refreshTokenExpiry
					+ ", refreshTokenIssuedAt=" + refreshTokenIssuedAt + ", responseCode=" + responseCode
					+ ", responseMessage=" + responseMessage + ", statusCode=" + statusCode + "]";
		}
	    

}
