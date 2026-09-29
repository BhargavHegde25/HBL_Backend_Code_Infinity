package com.kony.adminconsole.jwt.auth;

import java.security.PrivateKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.jwt.auth.utils.TemenosUtilsC360;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

public class AnalyticsSpotlight {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	private static class Holder {

		private static AnalyticsSpotlight instance = new AnalyticsSpotlight();
	}

	public static AnalyticsSpotlight getInstance() {
		return Holder.instance;
	}

	private AnalyticsSpotlight() {

	}

	public String getAuthToken(DataControllerRequest request, org.json.JSONObject valueResponseJSON) {
		AuthCertificateC360 authCertificate = AuthCertificateC360.getInstance();
		String token = "";
		String jwtUniqueId = UUID.randomUUID().toString();
		HashMap<String, Object> jwtParamsMap = new HashMap<String, Object>();
		try {
			// Insights Application ID
			jwtParamsMap.put(AuthConstantsC360.INSIGHTS_APPLICATION_ID,
					EnvironmentConfigurationsHandler.getServerAppPropertyValue("INSIGHTS_APPLICATIONID", request));
			// return URL
			jwtParamsMap.put(AuthConstantsC360.RETURN_URL,
					EnvironmentConfigurationsHandler.getServerAppPropertyValue("AC_LOGIN_URL", request));
			// License GUID
			jwtParamsMap.put(AuthConstantsC360.LICENSE_GUID, valueResponseJSON.getString("LICENSEGUID"));
			// User Org Ids
			jwtParamsMap.put(AuthConstantsC360.USER_ORG_ID, valueResponseJSON.getString("USER_ORG_IDS"));
			// admin
			jwtParamsMap.put(AuthConstantsC360.IS_ADMIN, valueResponseJSON.getString("ADMIN"));

			jwtUniqueId = getJWTUniqueId(request);
			token = (String) TemenosUtilsC360.getInstance().retreiveFromSession(jwtUniqueId, request);
			if (StringUtils.isNotBlank(token)) {
				boolean istokenExpired = isTokenExpired(token);
				if (!istokenExpired) {
					alert.prepareError("token is retrieved from session & valid").log();
					return token;
				}
				alert.prepareError("token found in session but expired").log();
			}
			PrivateKey privateKey = authCertificate.getPrivateKey(request);
			if (privateKey == null) {
				throw new Exception("error in getting private key");
			}
			jwtParamsMap.put(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY, privateKey);
			token = generateToken(jwtParamsMap);
			TemenosUtilsC360.getInstance().insertIntoSession(jwtUniqueId, token, request);
			alert.prepareError("token generated and stored in session").log();

		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return token;
	}

	private String generateToken(Map<String, Object> params) throws Exception {
		String jwt = "";
		JWTClaimsSet.Builder JWTbulder = new JWTClaimsSet.Builder().issueTime(Date.from(Instant.now()))
				.expirationTime(Date.from(Instant.ofEpochSecond(Instant.now().getEpochSecond() + 1800)));
		
		JWTbulder.claim(AuthConstantsC360.INSIGHTS_APPLICATION_ID, params.get(AuthConstantsC360.INSIGHTS_APPLICATION_ID).toString());
		JWTbulder.claim(AuthConstantsC360.RETURN_URL, params.get(AuthConstantsC360.RETURN_URL).toString());
		JWTbulder.claim(AuthConstantsC360.LICENSE_GUID, params.get(AuthConstantsC360.LICENSE_GUID).toString());
		JWTbulder.claim(AuthConstantsC360.USER_ORG_ID, params.get(AuthConstantsC360.USER_ORG_ID).toString());
		JWTbulder.claim(AuthConstantsC360.IS_ADMIN, params.get(AuthConstantsC360.IS_ADMIN).toString());
		
		JWTClaimsSet claimsSet = JWTbulder.build();
		JOSEObjectType joseObjectType = new JOSEObjectType("JWT");
		SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
				.keyID(AuthConstantsC360.TOKEN_HEADER_KEY).type(joseObjectType).build(), claimsSet);

		PrivateKey privateKey = (PrivateKey) params.get(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY);
		if (privateKey != null) {
			JWSSigner signer = new RSASSASigner(privateKey);
			signedJWT.sign(signer);
			jwt = signedJWT.serialize();
		}
		return jwt;

	}

	private boolean isTokenExpired(String token) {
		boolean isTokenExpired = false;
		try {
			JWT parse = JWTParser.parse(token);
			Date expirationTime = parse.getJWTClaimsSet().getExpirationTime();
			if (new Date().after(expirationTime)) {
				isTokenExpired = true;
			}
		} catch (ParseException e) {
			alert.error(e);
		}
		return isTokenExpired;
	}

	private String getJWTUniqueId(DataControllerRequest request) {
		String uniqueId = "";
		if (request != null) {
			String x_kony_authorization = request.getHeader(AuthConstantsC360.PARAM_X_KONY_AUTHORIZATION);
			try {
				JWT jwt = JWTParser.parse(x_kony_authorization);
				uniqueId = jwt.getJWTClaimsSet().getJWTID();
			} catch (Exception e) {
				alert.error(e);
			}
		}
		return uniqueId;
	}
}
