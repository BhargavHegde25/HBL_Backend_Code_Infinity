/**
 * 
 */
package com.infinity.dbx.temenos.auth;

import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**	
 * @author Gopinath Vaddepally - KH2453
 *	
 */
public class GetPublicKeyOperation implements JavaService2 {

	/* (non-Javadoc)
	 * @see com.konylabs.middleware.common.JavaService2#invoke(java.lang.String, java.lang.Object[], com.konylabs.middleware.controller.DataControllerRequest, com.konylabs.middleware.controller.DataControllerResponse)
	 */
	@Override
	public Object invoke(String methodId, Object[] params, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Result result = new Result();
		Dataset publicKeysDataset = new Dataset();
		publicKeysDataset.setId("public_Keys");
		Record publicKeyRecord = new Record();
		addAuthKey(publicKeyRecord, request);
		publicKeysDataset.addRecord(publicKeyRecord);
		result.addDataset(publicKeysDataset);
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return result;
	}
	
	void addAuthKey(Record publicKeyRecord,DataControllerRequest request)
	{
		PublicKey publicKey = AuthCertificate.getInstance().getPublicKey(request);
		publicKeyRecord.addParam(new Param("public_key", Base64.encodeBase64String(publicKey.getEncoded())));
		publicKeyRecord.addParam("alg", "RSA");
		publicKeyRecord.addParam("key_id", "KA01");
	}
}
