/**
 * 
 */
package com.infinity.dbx.temenos.utils;

import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import com.infinity.dbx.temenos.auth.AuthConstants;
import com.infinity.dbx.temenos.auth.Authentication;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.EncryptionUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.nimbusds.jose.util.IOUtils;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public class GetCertifcateKeys {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
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
	
	public PublicKey getPublicKeyFromFile(String fileName) 
	{
		PublicKey publicKey = null;
		if (StringUtils.isNotBlank(fileName)) {
			byte[] keyBytes = readkeyFile(fileName);
			if (keyBytes != null && keyBytes.length > 0) {
				Security.addProvider(new BouncyCastleProvider());
				X509EncodedKeySpec pkcs8EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
				try {
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					publicKey = keyFactory.generatePublic(pkcs8EncodedKeySpec);
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
			}
		}
		return publicKey;
	}

	public PrivateKey getPrivateKeyFromFile(String fileName)
	{
		PrivateKey privateKey = null;
		if (StringUtils.isNotBlank(fileName)) {
			byte[] keyBytes = readkeyFile(fileName);
			if (keyBytes != null && keyBytes.length > 0) {
				Security.addProvider(new BouncyCastleProvider());
				PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
				try {
					KeyFactory keyFactory = KeyFactory.getInstance("RSA");
					privateKey = keyFactory.generatePrivate(keySpec);
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
			}
		}
		return privateKey;
	}

	private byte[] readkeyFile(String fileName)
	{
		byte[] keyBytes = null;
		InputStream inputStream = Authentication.class.getClassLoader().getResourceAsStream(fileName);
		try {
			String inputStreamString = IOUtils.readInputStreamToString(inputStream, StandardCharsets.UTF_8);
			@SuppressWarnings("resource")
			PemObject pem = new PemReader(new StringReader(inputStreamString)).readPemObject();
			keyBytes = pem.getContent();
		} catch (Exception e) {
			// TODO: handle exception
			alert.prepareError("error in reading the key -",e).log();
		}finally {
        	if (inputStream!=null) {
        		try {
        			inputStream.close();			
        		}
        		catch(Exception e)
        		{
        			alert.prepareError(e.toString()).log();
        		}
        	}
        }
		return keyBytes;
	}
	
	public Map<String, String> getCertKeyPair(DataControllerRequest request)
	{
	    String BackendName = request.getParameter(TemenosConstants.PARAM_BACKEND_NAME) != ""
                ? request.getParameter(TemenosConstants.PARAM_BACKEND_NAME) : TemenosConstants.CONSTANT_TEMPLATE_NAME;
        String certName = request.getParameter(TemenosConstants.PARAM_CERT_NAME) != ""
                ? request.getParameter(TemenosConstants.PARAM_CERT_NAME) : AuthConstants.AUTH_CERT_NAME; 
		HashMap<String,String> certFromDB = getCertFromDB(request, BackendName, certName);
		return certFromDB;
	}
	
	@SuppressWarnings({"resource"})
	public PrivateKey getPrivateKey(Map<String,String> certFromDB,DataControllerRequest request)
	{
		PrivateKey privateKey = null;
		if (certFromDB != null && certFromDB.get(TemenosConstants.PARAM_CERT_PRIVATE_KEY) != null) {
			String ENCRYPTION_KEY = CommonUtils.getServerEnvironmentProperty(TemenosConstants.PRIVATE_ENCRYPTION_KEY, request);
			try {
				String decryptedPrivateKey = EncryptionUtils.decrypt(certFromDB.get(TemenosConstants.PARAM_CERT_PRIVATE_KEY), ENCRYPTION_KEY);
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
	public PrivateKey getPrivateKey(Map<String,String> certFromDB,DataControllerRequest request, boolean forTap)
	{
		PrivateKey privateKey = null;
		if (certFromDB != null && certFromDB.get(TemenosConstants.PARAM_CERT_PRIVATE_KEY) != null) {
			String ENCRYPTION_KEY = CommonUtils.getServerEnvironmentProperty(TemenosConstants.PRIVATE_ENCRYPTION_KEY, request);
			try {
				String decryptedPrivateKey = EncryptionUtils.decrypt(certFromDB.get(TemenosConstants.PARAM_CERT_PRIVATE_KEY), ENCRYPTION_KEY, true);
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
			if (certFromDB != null && certFromDB.get(TemenosConstants.PARAM_CERT_PUBLIC_KEY) != null) {
				PemObject pem = new PemReader(new StringReader(certFromDB.get(TemenosConstants.PARAM_CERT_PUBLIC_KEY))).readPemObject();
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
	
	private  HashMap<String, String> getCertFromDB(DataControllerRequest request, String BackendName, String certName)
	{
		HashMap<String, String> certMap = new HashMap<>();
		StringBuffer queryString = new StringBuffer();
		queryString.append(TemenosConstants.PARAM_BACKEND_NAME+" "); 
		queryString.append(TemenosConstants.EQ+" ");
		queryString.append(BackendName+" ");
		queryString.append(TemenosConstants.AND+" ");
		queryString.append(TemenosConstants.PARAM_CERT_NAME+" ");
		queryString.append(TemenosConstants.EQ+" ");
		queryString.append(certName);
		alert.prepareError("Certificate filter"+ queryString.toString()).log();
		request.addRequestParam_(TemenosConstants.$FILTER, queryString.toString());
		Result backendCertResult = (Result)CommonUtils.callInternalService(TemenosConstants.SERVICE_BACKEND_CERTIFICATE, TemenosConstants.OPERATION_BACKEND_CERTIFICATE_GET, null, null, request, 1, true);
		if (backendCertResult != null && backendCertResult.getDatasetById(TemenosConstants.PARAM_BACKEND_CERTIFICATE) != null) {
			Dataset certDataset = backendCertResult.getDatasetById(TemenosConstants.PARAM_BACKEND_CERTIFICATE);
			if (certDataset.getAllRecords().size() != 0) {
				Record certRecord = certDataset.getRecord(0);
				if (certRecord != null) {
					certMap.put(TemenosConstants.PARAM_CERT_PRIVATE_KEY, certRecord.getParamValueByName(TemenosConstants.PARAM_CERT_PRIVATE_KEY));
					certMap.put(TemenosConstants.PARAM_CERT_PUBLIC_KEY, certRecord.getParamValueByName(TemenosConstants.PARAM_CERT_PUBLIC_KEY));
				}
			}
		}
		return certMap;
	}
}
