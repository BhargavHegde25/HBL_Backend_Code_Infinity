package com.kony.adminconsole.service.tpp;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.HttpsJwks;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.dto.InternalUser;
import com.kony.adminconsole.handler.InternalUserHandler;
import com.kony.adminconsole.service.authmodule.UserAndSecurityAttributesService;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class TPPLoginPostProcessor implements DataPostProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	public static final String username = "admin1";
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		
		Result finalResult = new Result();
		try {
			
			String kid = result.getParamValueByName("kid");
			if(!StringUtils.equals(kid, request.getParameter("kid"))) {
				alert.prepareError("Invalid kid in request from TPP").log();
				finalResult.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
				finalResult.addParam(Param.ERR_MSG, "Invalid Token");
				finalResult.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Invalid Token");
				finalResult.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20000.getErrorCode());
				return finalResult;
			}
			
			String username = EnvironmentConfiguration.AC_TPP_SYSTEM_USER_USERNAME.getValue(request);
			String jwksEndPoint = EnvironmentConfiguration.AC_TPP_TOKEN_PUBLIC_KEY_URL.getValue(request);
			String issuer = EnvironmentConfiguration.AC_TPP_TOKEN_ISSUER.getValue(request);
			String audience = EnvironmentConfiguration.AC_TPP_TOKEN_AUDIANCE.getValue(request);
			boolean isValid = validateToken(request.getParameter("jwt_token"), jwksEndPoint, issuer, audience);
			if(!isValid) {
				finalResult.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
				finalResult.addParam(Param.ERR_MSG, "Invalid Token");
				finalResult.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Invalid Token");
				finalResult.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20000.getErrorCode());
				return finalResult;
			}
			
			InternalUser userProfile = InternalUserHandler.getUserProfile(username, request);
			
			UserAndSecurityAttributesService usas = new UserAndSecurityAttributesService();
			Record userAttributes = usas.getUserAttributes(userProfile, request);
			finalResult.addRecord(userAttributes);
			
			String userId = userProfile != null ? userProfile.getId() : StringUtils.EMPTY;
            Record securityAttributes = usas.getSecurityAttributes(userId, request);
            finalResult.addRecord(securityAttributes);
			
		}catch(Exception exp) {
			alert.prepareError("Encountered exception within TPPLoginPostProcessor: "+exp).log();
			finalResult.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
			finalResult.addParam(Param.ERR_MSG, "Invalid Token");
			finalResult.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Invalid Token");
			finalResult.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
			
			return finalResult;
		}
		
		return finalResult;
	}
	
		
	public static RSAPublicKey readPublicKey(String publicKey) throws Exception {
//		String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
		String publicKeyPEM = publicKey .replace("-----BEGIN PUBLIC KEY-----", "") .replaceAll(System.lineSeparator(), "") .replace("-----END PUBLIC KEY-----", "");
		byte[] encoded = Base64.decodeBase64(publicKeyPEM);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}

	private boolean validateToken(String token, String jwksEndPoint, String issuer, String audience) {

		boolean signatureVerified = false;
		try {

			HttpsJwks httpsJkws = new HttpsJwks(jwksEndPoint);
			HttpsJwksVerificationKeyResolver httpsJwksKeyResolver = new HttpsJwksVerificationKeyResolver(httpsJkws);
			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
					.setRequireExpirationTime()
					.setRequireSubject()
					.setExpectedIssuer(issuer)
					.setExpectedAudience(audience)
		            .setVerificationKeyResolver(httpsJwksKeyResolver)
		            .setJwsAlgorithmConstraints(ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
		            .build();
			try
		    {
		        //  Validate the JWT and process it to the Claims
		        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
		        signatureVerified = true;
		    }catch (InvalidJwtException jxe) {
		    	alert.prepareError("Token Validation Failed: "+jxe).log();
		    	signatureVerified = false;
		    }
		    
		} catch (Exception exp) {
			
			alert.prepareError("Encountered exception while validating signature: "+exp).log();
			signatureVerified =false;
		}
		
		return signatureVerified;
	}
	
	public static void main(String[] args) throws Exception {


		String token = "";
		String issuer = "";
		String audience = "";
		String publicKey = "";
			
		JwtConsumer jwtConsumer = new JwtConsumerBuilder()
				.setRequireExpirationTime()
				.setRequireSubject()
				.setExpectedIssuer(issuer)
				.setExpectedAudience(audience) 
				.setVerificationKey(readPublicKey(publicKey))
	            .setJwsAlgorithmConstraints(ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256)
	            .build();
		try
	    {
	        //  Validate the JWT and process it to the Claims
	        JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
	        System.out.println("true");
	    }catch (InvalidJwtException jxe) {
	    	alert.prepareError("Token Validation Failed: "+jxe).log();
	    	System.out.println("false");
	    }
			
		/* Code to verify Signature
		String id_token = "";
		String publicKey ="";
		JWSVerifier verifier = new RSASSAVerifier(readPublicKey(publicKey));
		
		SignedJWT signedJWT = SignedJWT.parse(id_token);
		if (signedJWT.verify(verifier)) {
			System.out.println(true);
		} else {
			System.out.println(false);
		}
		*/
	}
}
