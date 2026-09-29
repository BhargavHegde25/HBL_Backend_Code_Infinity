/**
 * 
 */
package com.infinity.dbx.temenos.auth;

import java.security.PrivateKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.constants.EnrollmentConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.processor.IdentityHandler;
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

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
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
		HashMap<String, Object> jwtParamsMap = new HashMap<>();
		try {
			String flowType = request.getParameter(TemenosConstants.FLOW_TYPE);
			switch (flowType) {
			case TemenosConstants.PRE_LOGIN_FLOW:
				String userName = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE,
						TemenosConstants.PROP_PREFIX_TEMENOS, EnrollmentConstants.PROP_SECTION_ENROLLMENT,
						EnrollmentConstants.PROP_PRE_LOGIN_USERNAME);
				String userid = CommonUtils.getProperty(TemenosConstants.TEMENOS_PROPERTIES_FILE, TemenosConstants.PROP_PREFIX_TEMENOS,
						EnrollmentConstants.PROP_SECTION_ENROLLMENT, EnrollmentConstants.PROP_PRE_LOGIN_USER_ID);
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
				jwtParamsMap.put(TemenosConstants.PARAM_USERNAME, userName);
				jwtParamsMap.put(TemenosConstants.PARAM_DBX_USER_ID, userid);
				break;
			case TemenosConstants.LOGIN_FLOW:
				jwtParamsMap.put(TemenosConstants.PARAM_USERNAME,
						request.getParameter(TemenosConstants.PARAM_USERNAME));
				jwtParamsMap.put(TemenosConstants.PARAM_DBX_USER_ID,
						request.getParameter(TemenosConstants.PARAM_DBX_USER_ID));
				break;
			case TemenosConstants.POST_LOGIN_FLOW:
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
					String username = (String) userAttributes.get(TemenosConstants.PARAM_USERNAME);
					jwtParamsMap.put(TemenosConstants.PARAM_USERNAME, username);
					jwtParamsMap.put(TemenosConstants.PARAM_DBX_USER_ID, identityHandler.getUserId());
				}
				break;

			}
			PrivateKey privateKey = authCertificate.getPrivateKey(request);
			if (privateKey == null) {
				throw new Exception("error in getting private key");
			}
			jwtParamsMap.put(TemenosConstants.PARAM_CERT_PRIVATE_KEY, privateKey);
			String hostURL = CommonUtils.getServerEnvironmentProperty(Constants.DBP_HOST_URL, request);
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

	private String generateToken(Map<String, Object> params) throws Exception {
		String jwt = "";
		JWTClaimsSet.Builder JWTbulder = new JWTClaimsSet.Builder().issuer(AuthConstants.TOKEN_ISSUER)
				.audience(AuthConstants.TOKEN_AUDIENCE)
				.subject(String.valueOf(params.get(TemenosConstants.PARAM_USERNAME)))
				.jwtID(UUID.randomUUID().toString()).issueTime(Date.from(Instant.now()))
				.expirationTime(Date.from(Instant.ofEpochSecond(Instant.now().getEpochSecond() + 1800)));

		addCustomAttributes(JWTbulder, params);

		JWTClaimsSet claimsSet = JWTbulder.build();
		JOSEObjectType joseObjectType = new JOSEObjectType("JWT");
		SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
				.keyID(AuthConstants.TOKEN_HEADER_KEY).type(joseObjectType).build(), claimsSet);

		PrivateKey privateKey = (PrivateKey) params.get(TemenosConstants.PARAM_CERT_PRIVATE_KEY);
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
		JWTbulder.claim(TemenosConstants.PARAM_USER_ID, String.valueOf(params.get(TemenosConstants.PARAM_USERNAME)));
		JWTbulder.claim(TemenosConstants.PARAM_ROLE_ID, AuthConstants.TOKEN_ROLE_ID);
		JWTbulder.claim(TemenosConstants.PARAM_DBX_USER_ID,
				String.valueOf(params.get(TemenosConstants.PARAM_DBX_USER_ID)));
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
			String x_kony_authorization = request.getHeader(TemenosConstants.PARAM_X_KONY_AUTHORIZATION);
			try {
				JWT jwt = JWTParser.parse(x_kony_authorization);
				uniqueId = jwt.getJWTClaimsSet().getJWTID();
			} catch (Exception e) {
				alert.prepareError(e.toString()).log();
			}
		}
		return uniqueId;
	}

}
