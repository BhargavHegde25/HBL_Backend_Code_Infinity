package com.kony.adminconsole.service.customermanagement;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.dto.MemberSearchBean;
import com.kony.adminconsole.exception.DBPAuthenticationException;
import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.DBPServices;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CustomerSearchProfileLink implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	static final String CUSTOMER_SEARCH = "CUSTOMER_SEARCH";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {

		Result processedResult = new Result();
		try {
			if (StringUtils.isEmpty(requestInstance.getParameter("Customer_id"))) {
				ErrorCodeEnum.ERR_20688.setErrorCode(processedResult);
				return processedResult;
			}
			if (StringUtils.isEmpty(requestInstance.getParameter("Customer_username"))) {
				ErrorCodeEnum.ERR_20863.setErrorCode(processedResult);
				return processedResult;
			}

			String customerId = requestInstance.getParameter("Customer_id");
			String username = requestInstance.getParameter("Customer_username");
			String dateOfBirth = requestInstance.getParameter("DateOfBirth");
			String SSN = null;
			String customerType_id = null;
			String lastName = null;
			JSONObject customerViewJson;
			try {
				JSONObject getResponseJSON = DBPServices.computeCustomerBasicInformation(customerId, username,
						requestInstance);
				if (getResponseJSON != null && getResponseJSON.has(FabricConstants.OPSTATUS)
						&& getResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
						&& getResponseJSON.has("customerbasicinfo_view")) {

					customerViewJson = getResponseJSON.getJSONObject("customerbasicinfo_view");
					SSN = customerViewJson.getString("SSN");
					customerType_id = customerViewJson.getString("CustomerType_id");
					lastName = customerViewJson.getString("LastName");
					dateOfBirth = customerViewJson.getString("DateOfBirth");
				}
			} catch (DBPAuthenticationException dbpException) {

				ErrorCodeEnum.ERR_20933.setErrorCode(processedResult);
				processedResult.addParam(new Param("FailureReason", dbpException.getMessage(), FabricConstants.STRING));
				return processedResult;
			} catch (Exception e) {
				ErrorCodeEnum.ERR_20717.setErrorCode(processedResult);
				processedResult.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
				return processedResult;
			}

			MemberSearchBean memberSearchBean = new MemberSearchBean();
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
			memberSearchBean.setSearchType(CUSTOMER_SEARCH);
			memberSearchBean.setSsn(SSN);
			memberSearchBean.setCustomerName(lastName);
			memberSearchBean.setSortVariable(requestInstance.getParameter("_sortVariable"));
			memberSearchBean.setSortDirection(requestInstance.getParameter("_sortDirection"));
			memberSearchBean.setPageOffset(requestInstance.getParameter("_pageOffset"));
			memberSearchBean.setPageSize(requestInstance.getParameter("_pageSize"));

			JSONObject customers = DBPServices.searchCustomers(authToken, memberSearchBean.getSearchType(),
					memberSearchBean, requestInstance);

			if (customers.has("records")) {
				JSONArray recordsArray = customers.getJSONArray("records");
				JSONArray resultJSONArray = new JSONArray();
				for (int indexVar = 0; indexVar < recordsArray.length(); indexVar++) {
					JSONObject currJSONObject = recordsArray.getJSONObject(indexVar);
					String dob = String.valueOf(currJSONObject.get("DateOfBirth"));
					String custType = currJSONObject.getString("CustomerTypeId");
					String custStatus = currJSONObject.getString("Status_id");
					if (currJSONObject.has("ssn")) {
						currJSONObject.put("ssn", CustomerHandler.maskSSN(currJSONObject.get("ssn").toString()));
					}
					if (currJSONObject.has("Ssn")) {
						currJSONObject.put("Ssn", CustomerHandler.maskSSN(currJSONObject.get("Ssn").toString()));
					}
					if (dob.equals(dateOfBirth) && !custStatus.equals("SID_CUS_INACTIVE")
							&& StringUtils.isNotBlank(custType) && StringUtils.isNotBlank(customerType_id)
							&& ((custType.equals("TYPE_ID_RETAIL") && customerType_id.equals("TYPE_ID_BUSINESS"))
									|| (custType.equals("TYPE_ID_BUSINESS") && (currJSONObject.has("isAuthSignatory")
								&& currJSONObject.getBoolean("isAuthSignatory") == true)
											&& customerType_id.equals("TYPE_ID_RETAIL")))) {
						if (!currJSONObject.has("isCombinedUser") || (currJSONObject.has("isCombinedUser")
								&& currJSONObject.getBoolean("isCombinedUser") == false)) {
							resultJSONArray.put(currJSONObject);
						}
					}
				}
				Dataset recordsDataset = CommonUtilities.constructDatasetFromJSONArray(resultJSONArray);
				recordsDataset.setId("records");
				Param recordsStatus = new Param("Status", "Records returned: " + resultJSONArray.length(),
						FabricConstants.STRING);
				processedResult.addDataset(recordsDataset);
				processedResult.addParam(recordsStatus);
				processedResult.addParam(
						new Param("TotalResultsFound", String.valueOf(resultJSONArray.length()), FabricConstants.INT));

			} else {
				ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
				return processedResult;

			}

			return processedResult;
		} catch (Exception e) {
			alert.prepareError("Unexpected error in Customer search profile link: ", e).log();
			processedResult.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_20716.setErrorCode(processedResult);
			return processedResult;
		}
	}

}
