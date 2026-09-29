/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.auth;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import com.infinity.dbx.dbp.jwt.auth.AuthCertificate;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public class GetPublicKeyOperation implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		Dataset publicKeysDataset = new Dataset();
		publicKeysDataset.setId("keys");
		Record publicKeyRecord = new Record();
		addAuthKey(publicKeyRecord, request);
		publicKeysDataset.addRecord(publicKeyRecord);

		result.addDataset(publicKeysDataset);
		result.addOpstatusParam(0);
		result.addHttpStatusCodeParam(200);
		return result;
	}

	void addAuthKey(Record publicKeyRecord, DataControllerRequest request) {
		PublicKey publicKey = AuthCertificate.getInstance().getPublicKey(request);
		// publicKeyRecord.addParam(new Param("n",
		// Base64.encodeBase64String(publicKey.getEncoded())));
		publicKeyRecord.addParam("alg", "RS256");
		publicKeyRecord.addParam("kid", "KONY");
		publicKeyRecord.addParam("kty", "RSA");
		publicKeyRecord.addParam("use", "sig");
		RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;

		BigInteger publicKeyModulus = rsaPublicKey.getModulus();
		BigInteger publicKeyExponent = rsaPublicKey.getPublicExponent();

		String nModulus = Base64.getUrlEncoder().encodeToString(publicKeyModulus.toByteArray());
		String eExponent = Base64.getUrlEncoder().encodeToString(publicKeyExponent.toByteArray());
		publicKeyRecord.addParam("e", eExponent);
		publicKeyRecord.addParam("n", nModulus);

	}

}
