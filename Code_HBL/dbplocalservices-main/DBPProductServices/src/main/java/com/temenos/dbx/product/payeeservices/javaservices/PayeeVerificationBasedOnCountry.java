package com.temenos.dbx.product.payeeservices.javaservices;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.infinity.dbx.temenos.transactions.TransactionConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


public class PayeeVerificationBasedOnCountry implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		//String nickName = (String) inputParams.get("nickName");
		String beneficiaryName = (String) inputParams.get("beneficiaryName");
		JSONObject bundleConfig = TemenosUtils.getBundleConfigurations(TransactionConstants.DBP_BUNDLE,"VERIFY_PAYEE_ERR_MAPPING", request);
        String payeeVerificationErrCodes = "";
        if (bundleConfig != null) {
            JSONArray configurations = bundleConfig.optJSONArray("configurations");
            if (configurations != null && configurations.length() > 0) {
                JSONObject copErrMap = configurations.optJSONObject(0);
                if (copErrMap.has(TransactionConstants.DBP_CONFIG_TABLE_VALUE)) {
                	payeeVerificationErrCodes = copErrMap.getString(TransactionConstants.DBP_CONFIG_TABLE_VALUE);
                }
            }
        }
        JSONObject errCodeMap = new JSONObject(payeeVerificationErrCodes);
		Result result = new Result();
		if(errCodeMap != null && errCodeMap.has(beneficiaryName)) {
			result.addParam(new Param("payeeVerification",""));
			result.addParam(new Param("payeeVerificationStatus", "Failure"));
			result.addParam(new Param("payeeVerificationErrMsg", errCodeMap.getString(beneficiaryName)));
		}else if (StringUtils.isNotBlank(beneficiaryName) && "Stanly".equalsIgnoreCase(beneficiaryName)) {
			result.addParam(new Param("payeeVerificationStatus", "Failure"));
			result.addParam(new Param("payeeVerificationErrMsg", errCodeMap.getString("CLOSE_MATCH")));
			result.addParam(new Param("payeeVerificationName","Stanley"));
			result.addParam(new Param("payeeVerification",""));
		}else {
			result.addParam(new Param("payeeVerificationStatus", "Success"));
			result.addParam(new Param("payeeVerification","Success"));
			result.addParam(new Param("payeeVerificationErrMsg", ""));
		}
		return result;

	}
}