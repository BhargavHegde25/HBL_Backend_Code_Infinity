package com.kony.adminconsole.jwt.auth.utils;

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

import com.kony.adminconsole.jwt.auth.AuthConstantsC360;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;


public class GetCertifcateKeysC360 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	
	private static class Holder
	{
		private static GetCertifcateKeysC360 instance = new GetCertifcateKeysC360();
	}
	private GetCertifcateKeysC360()
	{
		
	}
	public static GetCertifcateKeysC360 getInstance()
	{
		return Holder.instance;
	}
	
	public Map<String, String> getCertKeyPair(DataControllerRequest request)
	{
		HashMap<String,String> certFromDB = getCertFromDB(request);
		return certFromDB;
	}
	
	@SuppressWarnings({"resource"})
	public PrivateKey getPrivateKey(Map<String,String> certFromDB,DataControllerRequest request)
	{
		PrivateKey privateKey = null;
		if (certFromDB != null && certFromDB.get(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY) != null) {
			String ENCRYPTION_KEY = TemenosUtilsC360.getServerEnvironmentProperty(AuthConstantsC360.PRIVATE_ENCRYPTION_KEY, request);
			try {
				String decryptedPrivateKey = EncryptionUtilsC360.decrypt(certFromDB.get(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY), ENCRYPTION_KEY);
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
	
	@SuppressWarnings({"resource"})
	public PublicKey getPublicKey(Map<String,String> certFromDB)
	{
		PublicKey publicKey = null;
		try {
			if (certFromDB != null && certFromDB.get(AuthConstantsC360.PARAM_CERT_PUBLIC_KEY) != null) {
				PemObject pem = new PemReader(new StringReader(certFromDB.get(AuthConstantsC360.PARAM_CERT_PUBLIC_KEY))).readPemObject();
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
		HashMap<String, String> certMap = new HashMap<String, String>();
		StringBuffer queryString = new StringBuffer();
		queryString.append(AuthConstantsC360.PARAM_BACKEND_NAME+" ");
		queryString.append(AuthConstantsC360.EQ+" ");
		queryString.append(request.getParameter(AuthConstantsC360.PARAM_BACKEND_NAME)+" ");
		queryString.append(AuthConstantsC360.AND+" ");
		queryString.append(AuthConstantsC360.PARAM_CERT_NAME+" ");
		queryString.append(request.getParameter(AuthConstantsC360.PARAM_CERT_NAME));
		request.addRequestParam_(AuthConstantsC360.$FILTER, queryString.toString());
		Result backendCertResult = (Result)CommonUtilsC360.callInternalService(AuthConstantsC360.SERVICE_BACKEND_CERTIFICATE, AuthConstantsC360.OPERATION_BACKEND_CERTIFICATE_GET, null, null, request, 1, true);
		if (backendCertResult != null && backendCertResult.getDatasetById(AuthConstantsC360.PARAM_BACKEND_CERTIFICATE) != null) {
			Dataset certDataset = backendCertResult.getDatasetById(AuthConstantsC360.PARAM_BACKEND_CERTIFICATE);
			if (certDataset.getAllRecords().size() != 0) {
				Record certRecord = certDataset.getRecord(0);
				if (certRecord != null) {
					certMap.put(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY, certRecord.getParamValueByName(AuthConstantsC360.PARAM_CERT_PRIVATE_KEY));
					certMap.put(AuthConstantsC360.PARAM_CERT_PUBLIC_KEY, certRecord.getParamValueByName(AuthConstantsC360.PARAM_CERT_PUBLIC_KEY));
				}
			}
		}
		return certMap;
	}
}
