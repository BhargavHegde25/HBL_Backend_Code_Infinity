package com.temenos.dbx.transaction.resource.impl;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.businessdelegate.api.SwiftCodeBusinessDelegate;
import com.temenos.dbx.transaction.dto.SwiftCodeDTO;
import com.temenos.dbx.transaction.resource.api.SwiftCodeResource;


public class SwiftCodeResourceImpl implements SwiftCodeResource{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getSwiftBICcode(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		// Initialization of business Delegate Class
		SwiftCodeBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(SwiftCodeBusinessDelegate.class);

		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			String bankName = inputParams.get("bankName") == null ? "" : inputParams.get("bankName");
			String city = inputParams.get("city") == null ? "" : inputParams.get("city");
			String country = inputParams.get("country") == null ? "" : inputParams.get("country");
			String iban = inputParams.get("iban") == null ? "" : inputParams.get("iban");
			String branchName = inputParams.get("branchName") == null ? "" : inputParams.get("branchName");
			List<SwiftCodeDTO> bicDTO;
			if(iban.isEmpty()) {
				bicDTO= businessDelegate.getSwiftBICcode(bankName, city, country, branchName);
			}else {
				bicDTO= businessDelegate.getSwiftBICcode(iban);
			}

			if (bicDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			JSONArray rulesJSONArr = new JSONArray(bicDTO);
			JSONObject responseObj = new JSONObject();
			responseObj.put("swiftCodes", rulesJSONArr);
			result = JSONToResult.convert(responseObj.toString());
		} catch (Exception e) {
			alert.prepareError("Caught exception at getSwiftCode " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

}
