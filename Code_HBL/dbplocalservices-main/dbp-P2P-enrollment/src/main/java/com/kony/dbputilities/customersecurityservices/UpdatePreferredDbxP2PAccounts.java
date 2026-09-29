package com.kony.dbputilities.customersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;


public class UpdatePreferredDbxP2PAccounts implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
		if (StringUtils.isBlank(customerId)) {
			Param p = new Param("result", "Customer Not Found", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			result.addParam(p);

			return result;
		}
		HelperMethods.removeNullValues(inputParams);
		Map<String, String> preferenceInput = new HashMap<>();
		result = HelperMethods.callGetApi(dcRequest, "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId,
				HelperMethods.getHeaders(dcRequest), URLConstants.CUSTOMERPREFERENCE_GET);

		if (HelperMethods.hasRecords(result) && !inputParams.isEmpty()) {
			preferenceInput.put("id", HelperMethods.getFieldValue(result, "id"));
			if (StringUtils.isNotBlank(inputParams.get("default_from_account_p2p"))) {
				preferenceInput.put("DefaultFromAccountP2P", inputParams.get("default_from_account_p2p"));
			}
			if (StringUtils.isNotBlank(inputParams.get("default_to_account_p2p"))) {
				preferenceInput.put("DefaultToAccountP2P", inputParams.get("default_to_account_p2p"));
			}
			result = HelperMethods.callApi(dcRequest, preferenceInput, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERPREFERENCE_UPDATE);
		} else {
			result = new Result();
			Param p = new Param("result", "Customer Not Found", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
			result.addParam(p);
			return result;
		}

		// Apparently email and phone wont come.
		/*
		 * preferenceInput = new HashMap<String,String>();
		 * preferenceInput.put(DBPUtilitiesConstants.FILTER, filter); result =
		 * HelperMethods.callApi(dcRequest,preferenceInput,HelperMethods.getHeaders(
		 * dcRequest),URLConstants.CUSTOMERCOMMUNICATION_GET); ds =
		 * result.getDatasetById(DBPUtilitiesConstants.DS_CUSTOMERCOMMUNICATION);
		 * Map<String, String> communicationInput = null; if(null != ds && null !=
		 * ds.getAllRecords() && ds.getAllRecords().size() > 0){ List<Record> recordList
		 * = ds.getAllRecords(); for(Record rec: recordList) { String typeId =
		 * rec.getParam("Type_id").getValue(); String id =
		 * rec.getParam("id").getValue();
		 * 
		 * switch(typeId){
		 * 
		 * case COMMUNICATION_TYPE_EMAIL:
		 * 
		 * String email = (String) inputParams.get(DBPInputConstants.EMAIL); if(null !=
		 * email && "" != email ) { communicationInput = new HashMap<String,String>();
		 * communicationInput.put("id", id); communicationInput.put("Type_id", typeId);
		 * communicationInput.put("Value", email); result =
		 * HelperMethods.callApi(dcRequest,communicationInput,HelperMethods.getHeaders(
		 * dcRequest),URLConstants.CUSTOMERCOMMUNICATION_UPDATE); } break;
		 * 
		 * case COMMUNICATION_TYPE_PHONE: String phone = (String)
		 * inputParams.get(DBPInputConstants.PHONE); if(null != phone && "" != phone ) {
		 * communicationInput = new HashMap<String,String>();
		 * communicationInput.put("id", id); communicationInput.put("Type_id", typeId);
		 * communicationInput.put("Value", phone); result =
		 * HelperMethods.callApi(dcRequest,communicationInput,HelperMethods.getHeaders(
		 * dcRequest),URLConstants.CUSTOMERCOMMUNICATION_UPDATE); } break;
		 * 
		 * default: break;
		 * 
		 * } }
		 * 
		 * }else { result= new Result(); Param p = new
		 * Param("result","Customer communication details Not Found",DBPConstants.
		 * FABRIC_STRING_CONSTANT_KEY); result.addParam(p); return result; }
		 */

		Result finalResult = new Result();
		Param p = new Param("result", "Updated Successfully", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
		finalResult.addParam(p);

		return finalResult;
	}
}