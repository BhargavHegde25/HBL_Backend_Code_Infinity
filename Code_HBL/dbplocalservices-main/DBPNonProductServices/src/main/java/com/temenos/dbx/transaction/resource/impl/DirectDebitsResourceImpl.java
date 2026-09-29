package com.temenos.dbx.transaction.resource.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.transaction.businessdelegate.api.DirectDebitsBusinessDelegate;
import com.temenos.dbx.transaction.resource.api.DirectDebitsResource;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.dbx.transaction.dto.DirectDebitDTO;

public class DirectDebitsResourceImpl implements DirectDebitsResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	/**
	 * @author sribarani.vasthan resourceImpl getDirectDebits: returns list of Direct debits in the form of a Json array
	 */
	@Override
	public Result getDirectDebits(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		// creating an instance for DirectDebitsBusinessDelegate class
		DirectDebitsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(DirectDebitsBusinessDelegate.class);

		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			inputParams.put("accountID", "1425365132");
			String accountID = inputParams.get("accountID") == null ? "" : inputParams.get("accountID");
			DirectDebitDTO inputDTO = new DirectDebitDTO();
			inputDTO.setAccountID(accountID);
			if (StringUtils.isBlank(accountID)) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}

			List<DirectDebitDTO> directDebitsDTO;

			directDebitsDTO = businessDelegate.getDirectDebits(inputDTO);
			JSONArray rulesJSONArr = new JSONArray(directDebitsDTO);
			JSONObject responseObj = new JSONObject();
			responseObj.put("Transactions", rulesJSONArr);
			result = JSONToResult.convert(responseObj.toString());
			Dataset dataset = new Dataset();
			Record totalRecord = new Record();
			totalRecord.addParam(new Param("totalSize", String.valueOf(rulesJSONArr.length())));
			dataset.addRecord(totalRecord);
			dataset.setId("Meta");
			result.addDataset(dataset);
		} catch (Exception e) {
			alert.prepareError("Caught exception in getDirectDebits " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

	/**
	 * @author sribarani.vasthan resourceImpl cancelDirectDebit: Removes data from
	 *         mock directDebitsList
	 */
	@Override
	public Result cancelDirectDebit(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		@SuppressWarnings("unchecked")
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		inputParams.put("directDebitStatus", "CANCELLED");
		try {
			// creating an instance for DirectDebitsBusinessDelegate class
			DirectDebitsBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(DirectDebitsBusinessDelegate.class);
			JSONObject serviceResponseJSON = Utilities.convertStringToJSON(request.getParameter("requestBody"));
			String directDebitId;
			if(serviceResponseJSON.has("directDebitId"))
			 directDebitId=serviceResponseJSON.opt("directDebitId").toString();
			else
				directDebitId=serviceResponseJSON.opt("Id").toString();
			DirectDebitDTO directDebitDTO = businessDelegate
					.cancelDirectDebit(directDebitId,inputParams.get("directDebitStatus").toString());
			JSONObject JSONResponse = new JSONObject(directDebitDTO);
			result = JSONToResult.convert(JSONResponse.toString());
		} catch (Exception e) {
			alert.prepareError("Caught exception in cancelDirectDebit " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}
		return result;
	}

}
