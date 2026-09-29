/**
 * 
 */
package com.kony.dbp.alertsenginejwtgeneration.jwt.auth;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbp.alertsenginejwtgeneration.jwt.auth.utils.TemenosUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class Authentication {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static class Holder {

		private static Authentication instance = new Authentication();
	}

	public static Authentication getInstance() {
		return Holder.instance;
	}

	private Authentication() {

	}

	public String getAuthToken(DataControllerRequest request) {
		
		if(diagnostic.isDebugEnabled())
		{
			diagnostic.prepareDebug("Inside getAuthToken").log();
		}

		AuthCertificate authCertificate = AuthCertificate.getInstance();
		String token = "";
		HashMap<String, Object> jwtParamsMap = new HashMap<String, Object>();

		try {

			String userName = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE, AuthConstants.PROP_PREFIX_TEMENOS,
					AuthConstants.PROP_SECTION_ENROLLMENT, AuthConstants.PROP_PRE_LOGIN_USERNAME);
			String userid = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE, AuthConstants.PROP_PREFIX_TEMENOS,
					AuthConstants.PROP_SECTION_ENROLLMENT, AuthConstants.PROP_PRE_LOGIN_USER_ID);
			String roleId = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE, AuthConstants.PROP_PREFIX_TEMENOS,
					AuthConstants.PROP_PREFIX_GENERAL, AuthConstants.PROP_ROLE_ID);
			
			jwtParamsMap.put(AuthConstants.PARAM_USERNAME, userName);
			jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID, userid);
			jwtParamsMap.put(AuthConstants.PARAM_ROLE_ID, roleId);
			
			PrivateKey privateKey = authCertificate.getPrivateKey(request);
			
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("privateKey : " + privateKey).log();
			}

			
			if (privateKey == null) {
				throw new Exception("error in getting private key");
			}
			
			jwtParamsMap.put(AuthConstants.PARAM_CERT_PRIVATE_KEY, privateKey);
			
			String hostURL = TemenosUtils.getServerEnvironmentProperty(AuthConstants.DBP_HOST_URL, request);
		
			jwtParamsMap.put("issuer", hostURL);
			
			token = generateToken(jwtParamsMap);
			
			if(diagnostic.isDebugEnabled())
			{
				diagnostic.prepareDebug("token : " + token).log();
			}
	
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return token;
	}

	private String generateToken(Map<String, Object> params) throws Exception {
		String jwt = "";
		JWTClaimsSet.Builder JWTbulder = new JWTClaimsSet.Builder().issuer(AuthConstants.TOKEN_ISSUER)
				.audience(AuthConstants.TOKEN_AUDIENCE)
				.subject(String.valueOf(params.get(AuthConstants.PARAM_USERNAME))).jwtID(UUID.randomUUID().toString())
				.issueTime(Date.from(Instant.now()))
				.expirationTime(Date.from(Instant.ofEpochSecond(Instant.now().getEpochSecond() + 1800)));

		addCustomAttributes(JWTbulder, params);

		JWTClaimsSet claimsSet = JWTbulder.build();
		JOSEObjectType joseObjectType = new JOSEObjectType("JWT");
		SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
				.keyID(AuthConstants.TOKEN_HEADER_KEY).type(joseObjectType).build(), claimsSet);

		PrivateKey privateKey = (PrivateKey) params.get(AuthConstants.PARAM_CERT_PRIVATE_KEY);
		if (privateKey != null) {
			JWSSigner signer = new RSASSASigner(privateKey);

			signedJWT.sign(signer);

			jwt = signedJWT.serialize();
		}
		return jwt;

	}

	private void addCustomAttributes(JWTClaimsSet.Builder JWTbulder, Map<String, Object> params) {
		JWTbulder.claim(AuthConstants.PARAM_USER_ID, String.valueOf(params.get(AuthConstants.PARAM_USERNAME)));
		JWTbulder.claim(AuthConstants.PARAM_ROLE_ID, String.valueOf(params.get(AuthConstants.PARAM_ROLE_ID)));
		JWTbulder.claim(AuthConstants.PARAM_DBX_USER_ID,String.valueOf(params.get(AuthConstants.PARAM_DBX_USER_ID)));
		JWTbulder.claim("_issmeta", String.valueOf(params.get("issuer")) + AuthConstants.AUTH_PUBLIC_KEY_METADATA);
	}

}
