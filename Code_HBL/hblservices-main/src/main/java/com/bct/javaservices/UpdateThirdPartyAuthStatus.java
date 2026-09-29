package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.utilities.Utils;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.common.net.HttpHeaders;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.accounts.AccountsConstants;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPDatasetConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.kony.eum.dbputilities.kms.KMSUtil;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.usermanagement.backenddelegate.api.CommunicationBackendDelegate;

import org.json.JSONObject;

public class UpdateThirdPartyAuthStatus implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(UpdateThirdPartyAuthStatus.class);

	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse result)
			throws Exception {
		Result result1 = new Result();
		try {
			String UserName = request.getParameter("userName");
			String customerId = getCustomerIDFromUsername(request, UserName);
			/**
			 * Updating customer Table IsOlbAllowed Flag with 1 return from method
			 * updateThirdpartyAuthCustomerTable as this Admin disabled thirdparty auth to
			 * the user
			 */
			Integer flagUpdate = updateThirdpartyAuthCustomerTable(request, "0", UserName, customerId);
			if (flagUpdate == 1) {
				result1.setParam(new Param("is2FAEnrollDisabled", "true"));
				// send email notification to the user as he ADMIN disable third party AUTH
				triggerEmail(request, customerId);
			} else {
				result1.setParam(new Param("is2FAEnrollDisabled", "false"));
			}

			result1.setParam(new Param("opstatus", "0"));
			result1.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in UpdateThirdPartyAuthStatus:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result1);
			result1.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result1.addParam(new Param("success", "false"));
		}
		return result1;
	}

	private int updateThirdpartyAuthCustomerTable(DataControllerRequest request, String thirdpartyAuthFlag,
			String UserName, String customerid) throws Exception {
		HashMap<String, Object> inputParams = new HashMap<String, Object>();
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();

		/**
		 * IsOlbAllowed Flag - 1 as true indicates thirdpartyAuth Enabled IsOlbAllowed
		 * Flag - 0 as false indicates thirdpartyAuth Disabled
		 */
		inputParams.put("IsOlbAllowed", thirdpartyAuthFlag);
		inputParams.put("IsStaffMember", "1");
		inputParams.put("UserName", UserName);
		inputParams.put("id", customerid);
		String serviceName = TemenosConstants.SERVICE_BACKEND_CERTIFICATE;
		String operationName = "dbxdb_customer_update";

		try {
			Result result = CommonUtils.callIntegrationService(request, inputParams, serviceHeaders, serviceName,
					operationName, false);
			LOG.debug("Post updateThirdpartyAuthCustomerTable resutl:" + ResultToJSON.convert(result));
			if (StringUtils.isNotEmpty(result.getParamValueByName("updatedRecords"))) {
				try {
					if (Integer.parseInt(result.getParamValueByName("updatedRecords")) > 0) {
						// resetThirdpartyFlag(request, thirdpartyAuthFlag, UserName, customerid);
						return 1;
					} else
						return 0;
				} catch (Exception e) {
					LOG.debug("Couldn't Parse updated records Integer from String");
					return 1;
				}
			}

		} catch (Exception e) {
			LOG.debug("updateThirdpartyAuthCustomerTable Exception: " + e);
		}
		return 0;
	}
	
	private String getCustomerIDFromUsername(DataControllerRequest request, String UserName) {

		String customerid = "";
		try {

			String filter = CommonUtils.buildOdataCondition(TemenosConstants.PARAM_USERNAME, Constants.EQUAL, UserName);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, TemenosConstants.OP_CUSTOMER_GET, false);
			Dataset customerDataset = result.getDatasetById(TemenosConstants.DS_CUSTOMER);
			if (null != customerDataset) {
				customerid = customerDataset.getRecord(0).getParamValueByName("id");
			} else {
				LOG.debug("Else getThirdpartyAuthFlag:");
			}
			LOG.debug("getThirdpartyAuthFlag id:" + customerid);
		} catch (Exception e) {
			LOG.error("Error while retrieving CustomerType_id for Customer " + UserName);
		}
		return customerid;

	}
	
	/**
	 * Trigger email to the customer when ADMIN disable his
	 * THIRDPART AUTH from Spotlight!
	 * @Author: Lokesh G
	 * */
	
	private void triggerEmail(DataControllerRequest dcRequest, String customerId) throws HttpCallException {
		try {
			JSONObject obj = getContactDetails(customerId, dcRequest);
			LOG.debug("getContactDetails value:" + obj.toString());
			String email = obj.optString("email");
			LOG.debug("triggerEmail value:" + email);
			String userName = dcRequest.getParameter("userName");

			Map<String, String> input = new HashMap<>();
			input.put("Subscribe", "true");
			input.put("FirstName", userName);
			input.put("EmailType", "ThirdpartyAuth");
			input.put("LastName", userName);
			JSONObject addContext = new JSONObject();
			addContext.put("userName", userName);
			input.put("AdditionalContext", KMSUtil.getOTPContent(null, null, addContext));
			input.put("Email", email);
			Map<String, String> headers = HelperMethods.getHeaders(dcRequest);
			headers.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
			HelperMethods.callApiAsync(dcRequest, input, headers, URLConstants.DBX_SEND_EMAIL_ORCH);
		} catch (Exception e) {
			LOG.debug("triggerEmail exception:" + e);
		}
	}
	
	public JSONObject getContactDetails(String customerId, DataControllerRequest request) {
		CommunicationBackendDelegate communicationBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(CommunicationBackendDelegate.class);
		CustomerCommunicationDTO customerCommunicationDTO = new CustomerCommunicationDTO();
		customerCommunicationDTO.setCustomer_id(customerId);
		DBXResult communicationResponse = communicationBackendDelegate
				.getPrimaryMFACommunicationDetails(customerCommunicationDTO, request.getHeaderMap());
		JsonObject customerCommunication = ((JsonObject) communicationResponse.getResponse());
		JSONObject communicationObj = new JSONObject();
		if (customerCommunication.has(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
				&& customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION).isJsonArray()) {
			JsonArray communicationArray = customerCommunication.get(DBPDatasetConstants.DATASET_CUSTOMERCOMMUNICATION)
					.getAsJsonArray();
			for (JsonElement jsonelement : communicationArray) {
				JsonObject object = jsonelement.getAsJsonObject();
				if ("COMM_TYPE_EMAIL".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
					communicationObj.put("email", JSONUtil.getString(object, "Value"));
				if ("COMM_TYPE_PHONE".equalsIgnoreCase(JSONUtil.getString(object, "Type_id")))
					communicationObj.put("phone", JSONUtil.getString(object, "Value"));
			}
		}
		LOG.error("getContactDetails:" + communicationObj);
		return communicationObj;
	}
}
