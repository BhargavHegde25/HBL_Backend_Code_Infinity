/**
 * 
 */
package com.kony.adminconsole.jwt.auth;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.adminconsole.jwt.auth.utils.GetCertifcateKeysC360;
import com.kony.adminconsole.jwt.auth.utils.TemenosUtilsC360;
import com.konylabs.middleware.controller.DataControllerRequest;

public class AuthCertificateC360 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	GetCertifcateKeysC360 certifcateKeys = GetCertifcateKeysC360.getInstance();

	private volatile static AuthCertificateC360 instance = null;

	private AuthCertificateC360()
	{

	}
	
	public static AuthCertificateC360 getInstance()
	{
		if (instance == null) {
			synchronized (AuthCertificateC360.class){
				if (instance == null) {
					instance = new AuthCertificateC360();
				}
			}
		}
		return instance;
	}
	
	public PrivateKey getPrivateKey(DataControllerRequest request)
	{
		Map<String, String> certMap = getCertMap(request);
		if (certMap == null || certMap.get(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY) == null) {
			alert.prepareError("auth private key is null").log();
			return null;
		}
		return certifcateKeys.getPrivateKey(certMap, request);
	}

	public PublicKey getPublicKey(DataControllerRequest request)
	{
		Map<String, String> certMap = getCertMap(request);
		if (certMap == null || certMap.get(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY) == null) {
			alert.prepareError("auth public key is null").log();
			return null;
		}
		return certifcateKeys.getPublicKey(certMap);
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> getCertMap(DataControllerRequest request)
	{
		TemenosUtilsC360 temenosUtils = TemenosUtilsC360.getInstance();
		Map<String, String> certMap = null;
		String cachedData = (String)temenosUtils.getDataFromCache(request, AuthConstantsC360.AUTH_CERT_CACHE_KEY);
		if (StringUtils.isNotBlank(cachedData)) {
			certMap = (Map<String, String>)temenosUtils.buildObjectFromJSONString(cachedData);
			alert.prepareError("auth keys retrieved from cache").log();
		}
		else
		{
			request.addRequestParam_(AuthConstantsC360.PARAM_BACKEND_NAME, AuthConstantsC360.CONSTANT_TEMPLATE_NAME);
			request.addRequestParam_(AuthConstantsC360.PARAM_CERT_NAME, AuthConstantsC360.AUTH_CERT_NAME);
			certMap = certifcateKeys.getCertKeyPair(request);
			temenosUtils.insertDataIntoCache(request, certMap, AuthConstantsC360.AUTH_CERT_CACHE_KEY, AuthConstantsC360.AUTH_CERT_CACHE_TIME);
			alert.prepareError("auth keys stored in cache").log();
		}
		return certMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> getCertMap(DataControllerRequest request, String backendName, String key)
	{
		TemenosUtilsC360 temenosUtils = TemenosUtilsC360.getInstance();
		Map<String, String> certMap = null;
		String cachedData = (String)temenosUtils.getDataFromCache(request, key);
		if (StringUtils.isNotBlank(cachedData)) {
			certMap = (Map<String, String>)temenosUtils.buildObjectFromJSONString(cachedData);
			alert.prepareError("auth keys retrieved from cache").log();
		}
		else
		{
			request.addRequestParam_(AuthConstantsC360.PARAM_BACKEND_NAME, backendName);
			request.addRequestParam_(AuthConstantsC360.PARAM_CERT_NAME, AuthConstantsC360.AUTH_CERT_NAME);
			certMap = certifcateKeys.getCertKeyPair(request);
			temenosUtils.insertDataIntoCache(request, certMap, AuthConstantsC360.AUTH_CERT_CACHE_KEY, AuthConstantsC360.AUTH_CERT_CACHE_TIME);
			alert.prepareError("auth keys stored in cache").log();
		}
		return certMap;
	}
}
