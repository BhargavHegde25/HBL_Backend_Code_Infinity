package com.infinity.dbx.temenos;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.api.processor.IdentityHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

public class TemenosBasePreProcessor implements DataPreProcessor2, TemenosConstants {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		String userId = CommonUtils.getBackendIdFromCache(request, CONSTANT_TEMPLATE_NAME, Constants.PARAM_CUSTOMER_ID,
				"1");
		if(StringUtils.isBlank(userId)) {
			userId = CommonUtils.getBackendIdFromIdentity(request, BACKEND_ID, CONSTANT_TEMPLATE_NAME, Constants.PARAM_CUSTOMER_ID,
					"1");
		}

		params.put(USER_ID, userId);
		request.addRequestParam_(TemenosConstants.FLOW_TYPE, TemenosConstants.PRE_LOGIN_FLOW);
		String serviceName = request.getParameter("current_appID").toUpperCase();
		String authToken = "";
		if (serviceName.equals("T24ISPAYMENTORDERS")) {
			request.addRequestParam_("issuer", "Fabric");
			authToken = TokenUtils.getT24AuthToken(request);
			if (StringUtils.isBlank(authToken)) {
				return false;
			}

		} else
			authToken = TokenUtils.getT24AuthToken(request);

		request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);
		IdentityHandler identityHandler = request.getServicesManager().getIdentityHandler();
		Map<String, Object> userAttributes = identityHandler.getUserAttributes();
		String companyId = null;
		companyId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
		if(StringUtils.isBlank(companyId)) {
			if (userAttributes != null && userAttributes.size() > 0) {
				companyId = (String) userAttributes.get(TemenosConstants.PARAM_LEGALENTITYID);
				if (StringUtils.isNotBlank(companyId)) {
					diagnostic.prepareDebug("legalEntityId fetched from Identity: " + companyId + "for user : "
							+ userAttributes.get(TemenosConstants.PARAM_LEGALENTITYID)).log();
					request.addRequestParam_("companyId", companyId);
				}
				else
				{
				 companyId = (String) userAttributes.get(TemenosConstants.PARAM_COMPANYID);
				if (StringUtils.isNotBlank(companyId)) {
					diagnostic.prepareDebug("CompanyID fetched from Identity: " + companyId + "for user : "
							+ userAttributes.get(TemenosConstants.PARAM_USER_ID)).log();
					request.addRequestParam_("companyId", companyId);
				} else if (StringUtils.isNotBlank(request.getParameter("customerId"))) {
					companyId = getCompanyIdFromDB(request.getParameter("customerId"));
					request.addRequestParam_("companyId", companyId);
					diagnostic.prepareDebug("CompanyID fetched from Identity: " + companyId + "for user : "
							+ request.getParameter("customerId")).log();
				}
				}
			} else if (StringUtils.isNotBlank(request.getParameter("customerId"))) {
				companyId = getCompanyIdFromDB(request.getParameter("customerId"));
				if (StringUtils.isBlank(companyId))
					alert.prepareError("Company ID not present in DB for given customer ID").log();
				request.addRequestParam_("companyId", companyId);
				diagnostic.prepareDebug(
						"CompanyID fetched from DBXDB: " + companyId + "for user : " + request.getParameter("customerId")).log();
			} else
				alert.prepareError("Company ID not present in Identity Handler or Customer ID not provided to fetch from DB").log();
		}

		if (StringUtils.isBlank(companyId)){
			companyId = request.getHeader("companyId");
		}

		/*if(StringUtils.isBlank(companyId)) {
		    companyId = EnvironmentConfigurationsHandler.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
		}*/
		if (StringUtils.isNotBlank(companyId)) {
			request.getHeaderMap().put("companyId", companyId);
		}
		
		if (serviceName.equalsIgnoreCase("T24BulkPaymentAPIs")) {
			String userSignOnName = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request)
					.get("UserName");
			request.getHeaderMap().put("aliasInfo", userSignOnName);
		}

		alert.prepareError("user id " + params.get(USER_ID) + " Token:" + request.getParameter(PARAM_AUTHORIZATION)).log();
		return Boolean.TRUE;
	}

	/*
	 * Execute when request having no session info
	 */
	public void execute(HashMap params, DataControllerRequest request) throws Exception {

		String authToken = TokenUtils.getT24AuthToken(request);
		request.addRequestParam_(TemenosConstants.PARAM_AUTHORIZATION, authToken);

	}

	/**
	 * Get Company ID from DB
	 * 
	 * @param customerId
	 */
	public String getCompanyIdFromDB(String customerId) {

		Map<String, Object> inputMap = new HashMap<>();
		StringBuffer queryString = new StringBuffer();
		queryString.append("Customer_id" + " ");
		queryString.append("eq" + " ");
		queryString.append(customerId);
		queryString.append(" and ");
		queryString.append("BackendType" + " ");
		queryString.append("eq" + " ");
		queryString.append("T24");

		inputMap.put("$filter", queryString.toString());
		String companyIdResponse = null;
		try {
			companyIdResponse = Executor.invokeService(T24IrisAPIServices.SERVICE_BACKEND_IDENTIFIER, inputMap, null);
			diagnostic.prepareDebug("Backend Identifier" + companyIdResponse).log();
		} catch (Exception e1) {
			alert.prepareError("Service call to dbxdb failed " + e1.toString()).log();
		}
		JSONObject serviceResponseJSON = Utilities.convertStringToJSON(companyIdResponse);
		if (serviceResponseJSON == null) {
			alert.prepareError("EmptyResponse no backend identifier available for User").log();
		}
		if (serviceResponseJSON.has("backendidentifier")
				&& serviceResponseJSON.getJSONArray("backendidentifier").length() > 0) {
			JSONObject responseObj = serviceResponseJSON.getJSONArray("backendidentifier").getJSONObject(0);
			if (StringUtils.isNotBlank(responseObj.getString("CompanyId")))
				return responseObj.getString("CompanyId");
			else {
				alert.prepareError("EmptyResponse no backend identifier available for User").log();
				return "";
			}
		}
		alert.prepareError("EmptyResponse no backend identifier available for User").log();
		return "";
	}

}
