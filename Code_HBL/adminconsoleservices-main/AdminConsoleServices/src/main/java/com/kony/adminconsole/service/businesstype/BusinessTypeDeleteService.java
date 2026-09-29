package com.kony.adminconsole.service.businesstype;

import com.hbl.adminconsole.getlistcache.GetListCacheInvalidator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class BusinessTypeDeleteService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			return invokeOperation(methodID, inputArray, requestInstance, responseInstance);
		} finally {
			// Online-banking getList cache: this operation changes data getList returns. Runs even
			// after a part-way failure, because some rows may already be written.
			GetListCacheInvalidator.permissionsChanged();
		}
	}

	private Object invokeOperation(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		try {

			String businessType_id = requestInstance.getParameter("id");
			String sigtype = "";
			if (StringUtils.isBlank(businessType_id)) {
				ErrorCodeEnum.ERR_20866.setErrorCode(result);
				alert.prepareError("businesstype id cannot be empty").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			}
			// Fetch customers of a businesstype
			Map<String, String> inputMap = new HashMap<String, String>();
			inputMap.put(ODataQueryConstants.FILTER, "BusinessType_id eq '" + businessType_id + "'");
			inputMap.put(ODataQueryConstants.SELECT, "Customer_id");
			String readCustomersResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERBUSINESSTYPE_READ, inputMap,
					null, requestInstance);

			JSONObject readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomersResponse);
			if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
					&& readCustomerResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readCustomerResponseJSON.has("customerbusinesstype")) {
				JSONArray readCustomersJSONArray = readCustomerResponseJSON.optJSONArray("customerbusinesstype");
				if (readCustomersJSONArray != null && readCustomersJSONArray.length() > 0) {
					alert.prepareError("More than one customer linked to businesstype").log();
					ErrorCodeEnum.ERR_21351.setErrorCode(result);
					result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					return result;
				}
			}
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER, "BusinessType_id eq '" + businessType_id + "'");
			String readGroupsResponse = Executor.invokeService(ServiceURLEnum.GROUPBUSINESSTYPE_READ, inputMap, null,
					requestInstance);
			JSONObject readGroupsResponseJSON = CommonUtilities.getStringAsJSONObject(readGroupsResponse);
			if (readGroupsResponse != null && readGroupsResponseJSON.has(FabricConstants.OPSTATUS)
					&& readGroupsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readGroupsResponseJSON.has("groupbusinesstype")) {
				JSONArray readGroupsJSONArray = readGroupsResponseJSON.optJSONArray("groupbusinesstype");
				if (readGroupsJSONArray != null && readGroupsJSONArray.length() > 0) {
					String deleteGroupsResponse = Executor.invokeService(ServiceURLEnum.GROUPBUSINESSTYPE_DELETE,
							inputMap, null, requestInstance);
					JSONObject deleteGroupsResponseJSON = CommonUtilities.getStringAsJSONObject(deleteGroupsResponse);
					if (deleteGroupsResponseJSON != null && deleteGroupsResponseJSON.has(FabricConstants.OPSTATUS)
							&& deleteGroupsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
						result.addParam(new Param("status", "Success", FabricConstants.STRING));						
					} else {
						alert.prepareError("Delete group linked to businesstype failed").log();
						ErrorCodeEnum.ERR_21351.setErrorCode(result);
						result.addParam(new Param("status", "Failure", FabricConstants.STRING));
					}
				}
			}
			inputMap.clear();
			JSONArray readBusinessTypeJSONArray = null;
			inputMap.put(ODataQueryConstants.FILTER, "BusinessType_id eq '" + businessType_id + "'");
			inputMap.put(ODataQueryConstants.SELECT, "Signatory_id");
			String readBusinessTypeResponse = Executor.invokeService(ServiceURLEnum.BUSINESSSIGNATORY_READ, inputMap, null,
					requestInstance);
			JSONObject readBusinessTypeResponseJSON = CommonUtilities.getStringAsJSONObject(readBusinessTypeResponse);
			if (readBusinessTypeResponseJSON != null && readBusinessTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& readBusinessTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
					&& readBusinessTypeResponseJSON.has("businesssignatory")) {
				readBusinessTypeJSONArray = readBusinessTypeResponseJSON.optJSONArray("businesssignatory");
				//Stored all signatories associated with businss type to be deleted
			}
			// Deleting business signatory
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER, "BusinessType_id eq '" + businessType_id + "'");
			String deleteBusinessSignatoryResponse = Executor.invokeService(ServiceURLEnum.BUSINESSSIGNATORY_DELETE, inputMap,
					null, requestInstance);

			JSONObject deleteBusinessSignatoryResponseJSON = CommonUtilities
					.getStringAsJSONObject(deleteBusinessSignatoryResponse);
			if (deleteBusinessSignatoryResponseJSON != null && deleteBusinessSignatoryResponseJSON.has(FabricConstants.OPSTATUS)
					&& deleteBusinessSignatoryResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result.addParam(new Param("status", "Success", FabricConstants.STRING));
			} else {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21339.setErrorCode(result);
				alert.prepareError("Error in deleting business type").log();
			}
			//delete business type
			inputMap.clear();
			inputMap.put(ODataQueryConstants.FILTER, "id eq '" + businessType_id + "'");
			String deleteBusinessTypeResponse = Executor.invokeService(ServiceURLEnum.BUSINESSTYPE_DELETE, inputMap,
					null, requestInstance);
			JSONObject deleteBusinessTypeResponseJSON = CommonUtilities
					.getStringAsJSONObject(deleteBusinessTypeResponse);
			if (deleteBusinessTypeResponseJSON != null && deleteBusinessTypeResponseJSON.has(FabricConstants.OPSTATUS)
					&& deleteBusinessTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
				result.addParam(new Param("status", "Success", FabricConstants.STRING));
			} else {
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				ErrorCodeEnum.ERR_21339.setErrorCode(result);
				alert.prepareError("Error in deleting business type").log();
			}
			inputMap.clear();
			for (int i = 0; readBusinessTypeJSONArray!= null && i < readBusinessTypeJSONArray.length(); i++) {
				sigtype = readBusinessTypeJSONArray.optJSONObject(i).optString("Signatory_id");
				inputMap.put(ODataQueryConstants.FILTER, "Signatory_id eq '" + sigtype + "'");
				inputMap.put(ODataQueryConstants.SELECT, "id");
				String readBusinessTypeIdResponse = Executor.invokeService(ServiceURLEnum.BUSINESSSIGNATORY_READ, inputMap, null,
						requestInstance);
				JSONObject readBusinessTypeIdResponseJSON = CommonUtilities
						.getStringAsJSONObject(readBusinessTypeIdResponse);
				if (readBusinessTypeIdResponseJSON != null && readBusinessTypeIdResponseJSON.has(FabricConstants.OPSTATUS)
						&& readBusinessTypeIdResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& readBusinessTypeIdResponseJSON.has("businesssignatory")) {
					JSONArray readBusinessTypeIdJSONArray = readBusinessTypeIdResponseJSON.optJSONArray("businesssignatory");
					if (readBusinessTypeIdJSONArray == null || readBusinessTypeIdJSONArray.length() < 1) {
						inputMap.clear();
						inputMap.put(ODataQueryConstants.FILTER, "id eq '" + sigtype + "'");
						String deleteSignatoryTypeResponse = Executor.invokeService(ServiceURLEnum.SIGNATORYTYPE_DELETE,
								inputMap, null, requestInstance);
						JSONObject deleteSignatoryTypeResponseJSON = CommonUtilities
								.getStringAsJSONObject(deleteSignatoryTypeResponse);
						if (deleteSignatoryTypeResponseJSON != null
								&& deleteSignatoryTypeResponseJSON.has(FabricConstants.OPSTATUS)
								&& deleteSignatoryTypeResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
							result.addParam(new Param("status", "Success", FabricConstants.STRING));
						} else {
							result.addParam(new Param("status", "Failure", FabricConstants.STRING));
							ErrorCodeEnum.ERR_21339.setErrorCode(result);
							alert.prepareError("Error in deleting signatory type.").log();
						}
					}
				}
			}
			
		} catch (Exception e) {
			ErrorCodeEnum.ERR_21339.setErrorCode(result);
			alert.prepareError("Error in deleting business type.", e).log();
		}
		return result;
	}
}