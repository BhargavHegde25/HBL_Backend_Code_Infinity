/**
 * 
 */
package com.infinity.dbx.dbp.jwt.auth;

import java.security.PrivateKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.dbp.jwt.auth.utils.TemenosUtils;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.UserProfile;
//import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

public class Authentication {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	private static class Holder {

		private static Authentication instance = new Authentication();
	}

	public static Authentication getInstance() {
		return Holder.instance;
	}

	private Authentication() {

	}

	public String getAuthToken(DataControllerRequest request) {
		AuthCertificate authCertificate = AuthCertificate.getInstance();
		String token = "";
		String jwtUniqueId = UUID.randomUUID().toString();
		HashMap<String, Object> jwtParamsMap = new HashMap<String, Object>();
		try {
			String flowType = request.getParameter(AuthConstants.FLOW_TYPE);
			switch (flowType) {
			case AuthConstants.PRE_LOGIN_FLOW:
				String userName = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE,
						AuthConstants.PROP_PREFIX_TEMENOS, AuthConstants.PROP_SECTION_ENROLLMENT,
						AuthConstants.PROP_PRE_LOGIN_USERNAME);
				String userid = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE, AuthConstants.PROP_PREFIX_TEMENOS,
						AuthConstants.PROP_SECTION_ENROLLMENT, AuthConstants.PROP_PRE_LOGIN_USER_ID);
				jwtUniqueId = getJWTUniqueId(request);
				token = (String) TemenosUtils.getInstance().retreiveFromSession(jwtUniqueId, request);
				if (StringUtils.isNotBlank(token)) {
					boolean istokenExpired = isTokenExpired(token);
					if (!istokenExpired) {
						alert.prepareError("token is retrieved from session & valid").log();
						return token;
					}
					alert.prepareError("token found in session but expired").log();
				}
				jwtParamsMap.put(AuthConstants.PARAM_USERNAME, userName);
				jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID, userid);
				break;
			case AuthConstants.LOGIN_FLOW:
				jwtParamsMap.put(AuthConstants.PARAM_USERNAME,
						request.getParameter(AuthConstants.PARAM_USERNAME));
				jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID,
						request.getParameter(AuthConstants.PARAM_DBX_USER_ID));
				break;
			case AuthConstants.POST_LOGIN_FLOW:
				jwtUniqueId = getJWTUniqueId(request);
				token = (String) TemenosUtils.getInstance().retreiveFromSession(jwtUniqueId, request);
				if (StringUtils.isNotBlank(token)) {
					boolean istokenExpired = isTokenExpired(token);
					if (!istokenExpired) {
						alert.prepareError("token is retrieved from session & valid").log();
						return token;
					}
					alert.prepareError("token found in session but expired").log();
				}
				IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
				Map<String, Object> userAttributes = identityHandler.getUserAttributes();
				if (userAttributes != null && userAttributes.size() > 0) {
					String username = (String) userAttributes.get(AuthConstants.PARAM_USERNAME);
					jwtParamsMap.put(AuthConstants.PARAM_USERNAME, username);
					jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID, identityHandler.getUserId());
				}
				break;

			}
			String roleId = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE,
					AuthConstants.PROP_PREFIX_TEMENOS, AuthConstants.PROP_PREFIX_GENERAL,
					AuthConstants.PROP_ROLE_ID);
			jwtParamsMap.put(AuthConstants.PARAM_ROLE_ID, roleId);
			PrivateKey privateKey = authCertificate.getPrivateKey(request);
			if (privateKey == null) {
				throw new Exception("error in getting private key");
			}
			jwtParamsMap.put(AuthConstants.PARAM_CERT_PRIVATE_KEY, privateKey);
			String hostURL = TemenosUtils.getServerEnvironmentProperty(AuthConstants.DBP_HOST_URL, request);
			if (StringUtils.isBlank(hostURL)) {
				alert.prepareError("DBP_HOST_URL is not specified").log();
				return token;
			}
			jwtParamsMap.put("issuer", hostURL);
			token = generateToken(jwtParamsMap);
			TemenosUtils.getInstance().insertIntoSession(jwtUniqueId, token, request);
			alert.prepareError("token generated and stored in session").log();
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return token;
	}
	public String getAuthToken(Map<String, String> input, Map<String, Object> headerMap) {
		AuthCertificate authCertificate = AuthCertificate.getInstance();
		String token = "";
		String jwtUniqueId = UUID.randomUUID().toString();
		HashMap<String, Object> jwtParamsMap = new HashMap<String, Object>();
		
		try {
			String flowType = input.get(AuthConstants.FLOW_TYPE);
			switch (flowType) {
			case AuthConstants.PRE_LOGIN_FLOW:
				String userName = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE,
						AuthConstants.PROP_PREFIX_TEMENOS, AuthConstants.PROP_SECTION_ENROLLMENT,
						AuthConstants.PROP_PRE_LOGIN_USERNAME);
				String userid = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE, AuthConstants.PROP_PREFIX_TEMENOS,
						AuthConstants.PROP_SECTION_ENROLLMENT, AuthConstants.PROP_PRE_LOGIN_USER_ID);
				jwtUniqueId = getJWTUniqueId(headerMap);
				token = (String) TemenosUtils.getInstance().retreiveFromSession(jwtUniqueId, headerMap);
				if (StringUtils.isNotBlank(token)) {
					boolean istokenExpired = isTokenExpired(token);
					if (!istokenExpired) {
						alert.prepareError("token is retrieved from session & valid").log();
						return token;
					}
					alert.prepareError("token found in session but expired").log();
				}
				jwtParamsMap.put(AuthConstants.PARAM_USERNAME, userName);
				jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID, userid);
				break;
			case AuthConstants.LOGIN_FLOW:
				jwtParamsMap.put(AuthConstants.PARAM_USERNAME,
						input.get(AuthConstants.PARAM_USERNAME));
				jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID,
						input.get(AuthConstants.PARAM_DBX_USER_ID));
				break;
			case AuthConstants.POST_LOGIN_FLOW:
				jwtUniqueId = getJWTUniqueId(headerMap);
				token = (String) TemenosUtils.getInstance().retreiveFromSession(jwtUniqueId, headerMap);
				if (StringUtils.isNotBlank(token)) {
					boolean istokenExpired = isTokenExpired(token);
					if (!istokenExpired) {
						alert.prepareError("token is retrieved from session & valid").log();
						return token;
					}
					alert.prepareError("token found in session but expired").log();
				}
				IdentityHandler identityHandler = ServicesManagerHelper.getServicesManager().getIdentityHandler();
				Map<String, Object> userAttributes = identityHandler.getUserAttributes();
				if (userAttributes != null && userAttributes.size() > 0) {
					String username = (String) userAttributes.get(AuthConstants.PARAM_USERNAME);
					jwtParamsMap.put(AuthConstants.PARAM_USERNAME, username);
					jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID, identityHandler.getUserId());
				}

				break;

			}
			String roleId = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE,
					AuthConstants.PROP_PREFIX_TEMENOS, AuthConstants.PROP_PREFIX_GENERAL,
					AuthConstants.PROP_ROLE_ID);
			jwtParamsMap.put(AuthConstants.PARAM_ROLE_ID, roleId);
			PrivateKey privateKey = authCertificate.getPrivateKey();
			if (privateKey == null) {
				throw new Exception("error in getting private key");
			}
			jwtParamsMap.put(AuthConstants.PARAM_CERT_PRIVATE_KEY, privateKey);
			String hostURL = TemenosUtils.getServerEnvironmentProperty(AuthConstants.DBP_HOST_URL);
			if (StringUtils.isBlank(hostURL)) {
				alert.prepareError("DBP_HOST_URL is not specified").log();
				return token;
			}
			jwtParamsMap.put("issuer", hostURL);
			token = generateToken(jwtParamsMap);
			TemenosUtils.getInstance().insertIntoSession(jwtUniqueId, token, headerMap);
			alert.prepareError("token generated and stored in session").log();
		} catch (Exception e) {
			alert.prepareError("error occured while generating token ", e).log();
		}
		return token;
	}
	

	

	private String generateToken(Map<String, Object> params) throws Exception {
		String jwt = "";
		JWTClaimsSet.Builder JWTbulder = new JWTClaimsSet.Builder().issuer(AuthConstants.TOKEN_ISSUER)
				.audience(AuthConstants.TOKEN_AUDIENCE)
				.subject(String.valueOf(params.get(AuthConstants.PARAM_USERNAME)))
				.jwtID(UUID.randomUUID().toString()).issueTime(Date.from(Instant.now()))
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

			/*
			 * JWEObject jweObject = new JWEObject( new
			 * JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256CBC_HS512)
			 * .contentType("JWT") // required to indicate nested JWT .build(), new
			 * Payload(signedJWT));
			 * 
			 * PublicKey encPublicKey = getPublicKey(encryptionCert); jweObject.encrypt(new
			 * RSAEncrypter((RSAPublicKey) encPublicKey));
			 */

			jwt = signedJWT.serialize();
		}
		return jwt;

	}

	private void addCustomAttributes(JWTClaimsSet.Builder JWTbulder, Map<String, Object> params) {
		JWTbulder.claim(AuthConstants.PARAM_USER_ID, String.valueOf(params.get(AuthConstants.PARAM_USERNAME)));
		JWTbulder.claim(AuthConstants.PARAM_ROLE_ID, String.valueOf(params.get(AuthConstants.PARAM_ROLE_ID)));
		JWTbulder.claim(AuthConstants.PARAM_DBX_USER_ID,
				String.valueOf(params.get(AuthConstants.PARAM_DBX_USER_ID)));
		JWTbulder.claim("_issmeta", String.valueOf(params.get("issuer")) + AuthConstants.AUTH_PUBLIC_KEY_METADATA);
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
			alert.prepareError(e.toString()).log();
		}
		return isTokenExpired;
	}

	private String getJWTUniqueId(DataControllerRequest request) {
		String uniqueId = "";
		if (request != null) {
			String x_kony_authorization = request.getHeader(AuthConstants.PARAM_X_KONY_AUTHORIZATION);
			try {
				JWT jwt = JWTParser.parse(x_kony_authorization);
				uniqueId = jwt.getJWTClaimsSet().getJWTID();
			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
			}
		}
		return uniqueId;
	}
	private String getJWTUniqueId(Map<String, Object> headerMap) {
		String uniqueId = "";
		if (headerMap != null) {
			String x_kony_authorization = (String) headerMap.get(AuthConstants.PARAM_X_KONY_AUTHORIZATION);
			try {
				JWT jwt = JWTParser.parse(x_kony_authorization);
				uniqueId = jwt.getJWTClaimsSet().getJWTID();
			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
			}
		}
		return uniqueId;

	}
	
	public String getPartyAuthToken(DataControllerRequest request, String flowType) throws Exception, MiddlewareException {
		AuthCertificate authCertificate = AuthCertificate.getInstance();
		String token = "";
		String jwtUniqueId = UUID.randomUUID().toString();
		HashMap<String, Object> jwtParamsMap = new HashMap<String, Object>();
		long l = System.currentTimeMillis();
		switch (flowType) {
			case AuthConstants.PRE_LOGIN_FLOW:
				String userName;
				try {
					userName = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE,
							AuthConstants.PROP_PREFIX_TEMENOS, AuthConstants.PARTY_SECTION_ENROLLMENT,
							AuthConstants.PROP_PRE_LOGIN_USERNAME);
				} catch (Exception e1) {
					throw new Exception("Failed to get username. "+e1.getMessage());
				}
				String userid;
				try {
					userid = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE, AuthConstants.PROP_PREFIX_TEMENOS,
							AuthConstants.PARTY_SECTION_ENROLLMENT, AuthConstants.PROP_PRE_LOGIN_USER_ID);
				} catch (Exception e1) {
					throw new Exception("Failed to get userid. "+e1.getMessage());
				}
				jwtUniqueId = TemenosUtils.getJWTUniqueId(request.getHeader(AuthConstants.PARAM_X_KONY_AUTHORIZATION));
				token = (String) TemenosUtils.getInstance().retreiveFromSession(jwtUniqueId, request);
				if (StringUtils.isNotBlank(token)) {
					boolean istokenExpired = TemenosUtils.isTokenExpired(token);
					if (!istokenExpired) {
						return token;
					}			
				}
				jwtParamsMap.put(AuthConstants.PARAM_USERNAME, userName);
				jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID, userid);
				break;
			case AuthConstants.LOGIN_FLOW:
				jwtParamsMap.put(AuthConstants.PARAM_USERNAME,
						request.getParameter(AuthConstants.PARAM_USERNAME));
				jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID,
						request.getParameter(AuthConstants.PARAM_DBX_USER_ID));
				break;
			case AuthConstants.POST_LOGIN_FLOW:

				jwtUniqueId = TemenosUtils.getJWTUniqueId(request.getHeader(AuthConstants.PARAM_X_KONY_AUTHORIZATION));
				token = (String) TemenosUtils.getInstance().retreiveFromSession(jwtUniqueId, request);
				if (StringUtils.isNotBlank(token)) {
					boolean istokenExpired = TemenosUtils.isTokenExpired(token);
					if (!istokenExpired) {
						return token;
					}
				}
				IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
//			if (identityHandler == null) {
//				logger.info("PostLoginFlow#### identityHandler ## object isnt created");
//				String postUserName;
//				try {
//					postUserName = TemenosUtilsCorporate.getProperty(AuthConstantsCorporate.PROPERTIES_FILE,
//							AuthConstantsCorporate.PROP_PREFIX_TEMENOS, AuthConstantsCorporate.PROP_SECTION_ENROLLMENT,
//							AuthConstantsCorporate.PROP_POST_LOGIN_USER_NAME);
//				} catch (Exception e) {
//					throw new TokenGenerationException("Failed to get username. " + e.getMessage());
//				}
//
//				String userId;
//				try {
//					userId = TemenosUtilsCorporate.getProperty(AuthConstantsCorporate.PROPERTIES_FILE,
//							AuthConstantsCorporate.PROP_PREFIX_TEMENOS, AuthConstantsCorporate.PROP_PREFIX_GENERAL,
//							AuthConstantsCorporate.PROP_POST_LOGIN_USERID);
//				} catch (Exception e) {
//					throw new TokenGenerationException("Failed to get userId. " + e.getMessage());
//				}
//				jwtParamsMap.put(AuthConstantsCorporate.PARAM_USERNAME, postUserName);
//				jwtParamsMap.put(AuthConstantsCorporate.PARAM_DBX_USER_ID, userId);
//			} else {
				if(identityHandler != null) {
					UserProfile up = identityHandler.getUserProfile();

					Map<String, Object> userAttributes = null ;
					if (identityHandler.getSecurityAttributes() != null
							&& identityHandler.getSecurityAttributes().get("user_attributes") != null) {
						userAttributes = (Map<String, Object>) (identityHandler.getSecurityAttributes()
								.get("user_attributes"));

						Map<String, Object> secAtrributes = (Map<String, Object>) identityHandler.getSecurityAttributes();
					} else {
						userAttributes = identityHandler.getUserAttributes();
					}

					final Map<String, Object> userAttributes1 = userAttributes;

					if (userAttributes != null && userAttributes.size() > 0) {

						String username = (String) userAttributes.get("username");
						if(StringUtils.isEmpty(username)){
							username = (String) userAttributes.get("user_id");
						}
						jwtParamsMap.put(AuthConstants.PARAM_USERNAME, username);
						jwtParamsMap.put(AuthConstants.PARAM_DBX_USER_ID, identityHandler.getUserId());
					}
				}
				break;

		}
		String roleId = "";
		long time1 = System.currentTimeMillis();
		try {
			roleId = TemenosUtils.getProperty(AuthConstants.PROPERTIES_FILE,
					AuthConstants.PROP_PREFIX_TEMENOS, AuthConstants.PROP_PREFIX_PARTY,
					AuthConstants.PROP_ROLE_ID);
		} catch (Exception e) {
			throw new Exception("Failed to get roleId. "+e.getMessage());
		}
		jwtParamsMap.put(AuthConstants.PARAM_ROLE_ID, roleId);
		PrivateKey privateKey = authCertificate.getPrivateKey(request);
		if (privateKey == null) {
			throw new Exception("Unable to get private key either from cache or DB to generate JWT Token");
		}
		jwtParamsMap.put(AuthConstants.PARAM_CERT_PRIVATE_KEY, privateKey);
		String hostURL = TemenosUtils.getServerEnvironmentProperty(AuthConstants.DBP_HOST_URL, request);
		if (StringUtils.isBlank(hostURL)) {
			return token;
		}
		jwtParamsMap.put("issuer", hostURL);
		token = generateToken(jwtParamsMap);

		if (!flowType.equals(AuthConstants.POST_LOGIN_FLOW))
			TemenosUtils.getInstance().insertIntoSession(jwtUniqueId, token, request);

		return token;
	}

}
