package com.bct.preprocessor;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.codec.digest.DigestUtils;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class NEABillpaymentsPreprocessor implements DataPreProcessor2{
	public static LoggerUtil logger = new LoggerUtil(NEABillpaymentsPreprocessor.class);
	

	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest arg1, DataControllerResponse arg2, Result arg3)
			throws Exception {
		   String neaPaymentsUserId = EnvironmentConfigurationsHandler.getServerProperty("NEA_PAYMENTS_USERID");
	       String neaPaymentsPassword = EnvironmentConfigurationsHandler.getServerProperty("NEA_PAYMENTS_PASSWORD");
	       String neaPaymentsAccessCode = EnvironmentConfigurationsHandler.getServerProperty("NEA_PAYMENTS_ACCESS_CODE");
	       String neaSALT = EnvironmentConfigurationsHandler.getServerProperty("NEA_SALT");
	       String randomid=String.valueOf(generateBatchId());
	       logger.debug("HBL:NEABillpaymentsPreprocessor:payload:"+inputMap);
	       inputMap.put("userId", neaPaymentsUserId);
	       inputMap.put("password", neaPaymentsPassword);
	       inputMap.put("accessCode", neaPaymentsAccessCode);
	       inputMap.put("processId", randomid);
	       // below two params are used for NEA ConfirmBillPaid service
	       String txnId="HBL-"+randomid;
	       inputMap.put("txnId", txnId);
	       String txnSignature=generateTxnString(neaPaymentsUserId, neaPaymentsPassword, neaPaymentsAccessCode, txnId, neaSALT);
				//String encodedContent = HBLTxnToken(txnSignature);
				inputMap.put("txnSignature", txnSignature); 
		return true;
	}
	
	public String generateBatchId(){
		String randomId=HelperMethods.getUniqueNumericString(16);
		String batchId = "HBL" + "-" +randomId ;
		return batchId;
	}
	public String generateTxnString(String userId, String password, String accessCode, String txnId, String neaSALT) throws Exception {
		String txnString=userId+password+accessCode+txnId+neaSALT;
		logger.debug("HBL:NEABillpaymentsPreprocessor:txnSignature before Hashing :"+txnString);
		 txnString = DigestUtils.sha256Hex(txnString);//HBLTxnToken(txnString);
		logger.debug("HBL:NEABillpaymentsPreprocessor:txnSignature after Hashing :"+txnString);
		return txnString;
		
		
	}
	public String HBLTxnToken(String contentToEncode) throws Exception {
		 String nea_cert_path= EnvironmentConfigurationsHandler.getServerProperty("NEA_CERT_PATH");
		 logger.debug("HBL:NEABillpaymentsPreprocessor:HBLTxnToken: nea_cert_path :"+nea_cert_path);
		 String neaCertUserName=EnvironmentConfigurationsHandler.getServerProperty("NEA_CERT_USERNAME");
		 String neaCertPassword = EnvironmentConfigurationsHandler.getServerProperty("NEA_CERT_PASSWORD");
		String certPath = nea_cert_path;
        byte[] certStore = Files.readAllBytes(Paths.get(certPath));
        
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
       
		keyStore.load(new ByteArrayInputStream(certStore), neaCertPassword.toCharArray());
        
        String alias = keyStore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, neaCertPassword.toCharArray());
        
        byte[] utf8Data = contentToEncode.getBytes("UTF-8");
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(utf8Data);
        
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }

}
