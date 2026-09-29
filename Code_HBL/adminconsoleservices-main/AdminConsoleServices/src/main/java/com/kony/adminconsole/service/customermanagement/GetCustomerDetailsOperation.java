package com.kony.adminconsole.service.customermanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerDetailsOperation implements JavaService2 {
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();
		String customerId = request.getParameter("prospectId");
		JSONObject customerResponse = null;
		if (StringUtils.isAnyBlank(customerId))
			throw new ApplicationException(ErrorCodeEnum.ERR_22085);
		
		String authToken= CommonUtilities.getAuthToken(request);
		StringBuilder filterQuery = new StringBuilder();
		filterQuery.append("id eq " + "'" + customerId + "' ");
		customerResponse = getCustomerDetails(filterQuery.toString(), authToken);

		JSONArray customerData = customerResponse.optJSONArray("customer");
		result.addParam("customerData", customerData.toString());
		result =  CommonUtilities.withSuccessParams(result);
		return result;
	}
	//
	public JSONObject getCustomerDetails(String filterQuery, String authToken)
			throws DBPApplicationException, Exception {
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put(ACConstants.FILTER, filterQuery);
        String response = DBPServiceExecutorBuilder.builder().withServiceId("CRUDLayer").withOperationId("dbxdb_customer_get")
        .withRequestParameters(requestParameters).withFabricAuthToken(authToken).build().getResponse();
        JSONObject res = new JSONObject(response);
		if(res.optInt("httpStatusCode") != 0) {
			throw new ApplicationException(ErrorCodeEnum.ERR_22222);
		}
		return res;
	}
	//
	@SuppressWarnings("unchecked")
    public static String getFabricAuthToken(DataControllerRequest dataControllerRequest) {
        Object authToken = null;
        if (dataControllerRequest == null)
            return null;
        authToken = dataControllerRequest.getHeader(ACConstants.X_KONY_AUTHORIZATION_HEADER);
        if (authToken == null) {
            Map<String, Object> queryParams = (Map<String, Object>) dataControllerRequest.getAttribute("queryparams");
            if (queryParams != null)
                authToken = queryParams.get(ACConstants.DBP_KONY_AUTHORIZATION_TOKEN_QUERY_PARAM);
        }
        return String.class.cast(authToken);
    }
}
