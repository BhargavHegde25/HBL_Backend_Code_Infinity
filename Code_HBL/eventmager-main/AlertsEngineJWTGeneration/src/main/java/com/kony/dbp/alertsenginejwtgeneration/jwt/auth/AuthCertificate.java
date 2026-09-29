/**
 * 
 */
package com.kony.dbp.alertsenginejwtgeneration.jwt.auth;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbp.alertsenginejwtgeneration.jwt.auth.utils.GetCertifcateKeys;
import com.konylabs.middleware.controller.DataControllerRequest;

public class AuthCertificate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	GetCertifcateKeys certifcateKeys = GetCertifcateKeys.getInstance();

	private volatile static AuthCertificate instance = null;

	private AuthCertificate()
	{

	}
	
	public static AuthCertificate getInstance()
	{
		if (instance == null) {
			synchronized (AuthCertificate.class){
				if (instance == null) {
					instance = new AuthCertificate();
				}
			}
		}
		return instance;
	}
	
	public PrivateKey getPrivateKey(DataControllerRequest request)
	{
		Map<String, String> certMap = getCertMap(request);
		if (certMap == null || certMap.get(AuthConstants.PARAM_CERT_PRIVATE_KEY) == null) {
			alert.prepareError("auth private key is null").log();
			return null;
		}
		return certifcateKeys.getPrivateKey(certMap, request);
	}

	public PublicKey getPublicKey(DataControllerRequest request)
	{
		Map<String, String> certMap = getCertMap(request);
		if (certMap == null || certMap.get(AuthConstants.PARAM_CERT_PRIVATE_KEY) == null) {
			alert.prepareError("auth public key is null").log();
			return null;
		}
		return certifcateKeys.getPublicKey(certMap);
	}

	private Map<String, String> getCertMap(DataControllerRequest request)
	{
		Map<String, String> certMap = null;
		request.addRequestParam_(AuthConstants.PARAM_BACKEND_NAME, AuthConstants.CONSTANT_TEMPLATE_NAME);
		request.addRequestParam_(AuthConstants.PARAM_CERT_NAME, AuthConstants.AUTH_CERT_NAME);
		certMap = certifcateKeys.getCertKeyPair(request);
		return certMap;
	}
}
