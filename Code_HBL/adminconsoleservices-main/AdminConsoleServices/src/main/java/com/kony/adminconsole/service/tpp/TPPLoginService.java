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
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.dto.InternalUser;
import com.kony.adminconsole.handler.InternalUserHandler;
import com.kony.adminconsole.service.authmodule.UserAndSecurityAttributesService;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class TPPLoginService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static final String INPUT_TOKEN = "token";
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		try {
			
			String jwt_token = request.getParameter(INPUT_TOKEN);
			if(StringUtils.isBlank(jwt_token)) {
				diagnostic.prepareDebug("Missing or Invalid Token found in request").log();
				result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_BAD_REQUEST);
				result.addParam(Param.ERR_MSG, "Missing or Invalid Token in request");
				result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Missing or Invalid Token in request");
				result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20000.getErrorCode());
				return result;
			}
			
			String username = EnvironmentConfiguration.AC_TPP_SYSTEM_USER_USERNAME.getValue(request);
			String issuer = EnvironmentConfiguration.AC_TPP_TOKEN_ISSUER.getValue(request);
			String audience = EnvironmentConfiguration.AC_TPP_TOKEN_AUDIANCE.getValue(request);
			String publicKey = EnvironmentConfiguration.AC_TPP_PUBLIC_KEY.getValue(request);
			publicKey = new String(Base64.decodeBase64(publicKey));
			boolean isValid = validateToken(jwt_token,issuer, audience, publicKey);
			if(!isValid) {
				result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
				result.addParam(Param.ERR_MSG, "Invalid Token");
				result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Invalid Token");
				result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20000.getErrorCode());
				return result;
			}
			
			InternalUser userProfile = InternalUserHandler.getUserProfile(username, request);
			
			UserAndSecurityAttributesService usas = new UserAndSecurityAttributesService();
			Record userAttributes = usas.getUserAttributes(userProfile, request);
			result.addRecord(userAttributes);
			
			String userId = userProfile != null ? userProfile.getId() : StringUtils.EMPTY;
            Record securityAttributes = usas.getSecurityAttributes(userId, request);
            result.addRecord(securityAttributes);
            result.addParam(new Param(FabricConstants.HTTP_STATUS_CODE, Integer.toString(HttpServletResponse.SC_OK),
                    FabricConstants.INT));
			
		}catch(Exception exp) {
			
			alert.prepareError("Encountered exception within TPPLoginService: "+exp).log();
			result.addIntParam(Param.HTTP_STATUS_CODE, HttpServletResponse.SC_UNAUTHORIZED);
			result.addParam(Param.ERR_MSG, "Invalid Token in request");
			result.addParam(FabricConstants.BACKEND_ERROR_MESSAGE_KEY, "Invalid Token in request");
			result.addIntParam(FabricConstants.BACKEND_ERROR_CODE_KEY, ErrorCodeEnum.ERR_20001.getErrorCode());
			
			return result;
		}
		
		return result;
	}
	
	private boolean validateToken(String token, String issuer, String audience, String publicKey) {
		
		boolean signatureVerified = false;
		try {
			
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
		        signatureVerified = true;
		    }catch (InvalidJwtException jxe) {
		    	alert.prepareError("Token Validation Failed: "+jxe).log();
		    }
			
		}catch(Exception exp) {
			alert.prepareError("Encountered exception within validateToken: "+exp).log();
		}
		
		return signatureVerified;
	}
	
	public static RSAPublicKey readPublicKey(String publicKey) throws Exception {
		String publicKeyPEM = publicKey .replace("-----BEGIN PUBLIC KEY-----", "") .replaceAll(System.lineSeparator(), "") .replace("-----END PUBLIC KEY-----", "");
		byte[] encoded = Base64.decodeBase64(publicKeyPEM);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
		return (RSAPublicKey) keyFactory.generatePublic(keySpec);
	}

}
