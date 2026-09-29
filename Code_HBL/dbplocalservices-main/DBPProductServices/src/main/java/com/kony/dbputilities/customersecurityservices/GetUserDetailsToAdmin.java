package com.kony.dbputilities.customersecurityservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.usermanagement.resource.api.ProfileManagementResource;

public class GetUserDetailsToAdmin implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	static final String CUSTOMER_SEARCH = "CUSTOMER_SEARCH";
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {

		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String accountNumber = inputParams.get("accountNumber");
		if(StringUtils.isBlank(accountNumber) ) {
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			result.addErrMsgParam("Misssing input param accountNumber");
		}
		dcRequest.addRequestParam_("_cardorAccountnumber", accountNumber);
		if (StringUtils.isBlank(dcRequest.getParameter("_searchType"))){
			dcRequest.addRequestParam_("_searchType", CUSTOMER_SEARCH);
		}
		
		if (StringUtils.isBlank(dcRequest.getParameter("_sortVariable"))){
			dcRequest.addRequestParam_("_sortVariable", "name");
		}
		
		if (StringUtils.isBlank(dcRequest.getParameter("_sortDirection"))){
			dcRequest.addRequestParam_("_sortDirection", "ASC");
		}
		if (StringUtils.isBlank(dcRequest.getParameter("_pageOffset"))){
			dcRequest.addRequestParam_("_pageOffset", "0");
		}
		if (StringUtils.isBlank(dcRequest.getParameter("_sortDirection"))){
			dcRequest.addRequestParam_("_pageSize", "20");
		}
		
        try {
            ProfileManagementResource resource = DBPAPIAbstractFactoryImpl.getResource(ProfileManagementResource.class);
            result = resource.searchCustomer(methodID, inputArray, dcRequest, dcResponse);
        } catch (Exception e) {
            alert.prepareError("Caught exception while searching Customer using accountNumber: " + e).log();
        }

        return result;
	}
	

}
