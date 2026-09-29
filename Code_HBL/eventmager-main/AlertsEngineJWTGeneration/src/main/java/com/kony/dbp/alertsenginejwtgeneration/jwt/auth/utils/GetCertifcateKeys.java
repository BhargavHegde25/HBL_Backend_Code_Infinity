package com.kony.dbp.alertsenginejwtgeneration.jwt.auth.utils;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import com.kony.dbp.alertsenginejwtgeneration.jwt.auth.AuthConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;


public class GetCertifcateKeys {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	
	private static String schemaname = null;
	
	private static class Holder
	{
		private static GetCertifcateKeys instance = new GetCertifcateKeys();
	}
	private GetCertifcateKeys()
	{
		
	}
	public static GetCertifcateKeys getInstance()
	{
		return Holder.instance;
	}
	
	
	public Map<String, String> getCertKeyPair(DataControllerRequest request)
	{
		HashMap<String,String> certFromDB = getCertFromDB(request);
		return certFromDB;
	}
	
	public PrivateKey getPrivateKey(Map<String,String> certFromDB,DataControllerRequest request)
	{
		PrivateKey privateKey = null;
		if (certFromDB != null && certFromDB.get(AuthConstants.PARAM_CERT_PRIVATE_KEY) != null) {
			String ENCRYPTION_KEY = TemenosUtils.getServerEnvironmentProperty(AuthConstants.PRIVATE_ENCRYPTION_KEY, request);
			try {
				String decryptedPrivateKey = EncryptionUtils.decrypt(certFromDB.get(AuthConstants.PARAM_CERT_PRIVATE_KEY), ENCRYPTION_KEY);
				PemObject pem = new PemReader(new StringReader(decryptedPrivateKey)).readPemObject();
				Security.addProvider(new BouncyCastleProvider());
				PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pem.getContent());
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				privateKey = keyFactory.generatePrivate(keySpec);
			} catch (Exception e) {
				alert.prepareError(e.getLocalizedMessage(),e).log();
			}
		}
		return privateKey;
	}
	
	public PublicKey getPublicKey(Map<String,String> certFromDB)
	{
		PublicKey publicKey = null;
		try {
			if (certFromDB != null && certFromDB.get(AuthConstants.PARAM_CERT_PUBLIC_KEY) != null) {
				PemObject pem = new PemReader(new StringReader(certFromDB.get(AuthConstants.PARAM_CERT_PUBLIC_KEY))).readPemObject();
				Security.addProvider(new BouncyCastleProvider());
				X509EncodedKeySpec pkcs8EncodedKeySpec = new X509EncodedKeySpec(pem.getContent());
				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				publicKey = keyFactory.generatePublic(pkcs8EncodedKeySpec);
			}
		} catch (Exception e) {
			alert.prepareError("error reading public key -"+e.getLocalizedMessage(),e).log();
		}
		return publicKey;
	}
	
	private  HashMap<String, String> getCertFromDB(DataControllerRequest request)
	{
		if(schemaname == null)
		{
			schemaname = TemenosUtils.getServerEnvironmentProperty(AuthConstants.DBX_SCHEMA_NAME, request);
		}
		
		HashMap<String, String> certMap = new HashMap<String, String>();
		
		StringBuffer queryString = new StringBuffer();
		queryString.append(AuthConstants.PARAM_BACKEND_NAME+" ");
		queryString.append(AuthConstants.EQ+" ");
		queryString.append(request.getParameter(AuthConstants.PARAM_BACKEND_NAME)+" ");
		queryString.append(AuthConstants.AND+" ");
		queryString.append(AuthConstants.PARAM_CERT_NAME+" ");
		queryString.append(request.getParameter(AuthConstants.PARAM_CERT_NAME));
		
		request.addRequestParam_(AuthConstants.$FILTER, queryString.toString());
		
		Result backendCertResult = (Result)CommonUtils.callInternalService(AuthConstants.SERVICE_BACKEND_CERTIFICATE,CommonUtils.replaceSchemaName( AuthConstants.OPERATION_BACKEND_CERTIFICATE_GET,schemaname),null, null, request, 1, true);
		if (backendCertResult != null && backendCertResult.getDatasetById(AuthConstants.PARAM_BACKEND_CERTIFICATE) != null) {
			Dataset certDataset = backendCertResult.getDatasetById(AuthConstants.PARAM_BACKEND_CERTIFICATE);
			if (certDataset.getAllRecords().size() != 0) {
				Record certRecord = certDataset.getRecord(0);
				if (certRecord != null) {
					certMap.put(AuthConstants.PARAM_CERT_PRIVATE_KEY, certRecord.getParamValueByName(AuthConstants.PARAM_CERT_PRIVATE_KEY));
					certMap.put(AuthConstants.PARAM_CERT_PUBLIC_KEY, certRecord.getParamValueByName(AuthConstants.PARAM_CERT_PUBLIC_KEY));
				}
			}
		}
		return certMap;
	}
}
