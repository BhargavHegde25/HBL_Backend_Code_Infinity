package com.bct.preprocessor;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.utilities.HBLCommonUtility;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class getCreditCardsPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(getCreditCardsPreprocessor.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String bankId = paramHelper.getServerProperty("HBL_BANK_ID");
		String ebankingUser = paramHelper.getServerProperty("S2M_E_BANKING_USER");
		String ebankingPassword = paramHelper.getServerProperty("S2M_E-BANKING_PASSWORD");
		
		String CustomerId = params.get("customerId")!=null?params.get("customerId").toString():"";
		if(StringUtils.isBlank(CustomerId))
		CustomerId=HBLCommonUtility.getCoreBackendId(request);
		//String CustomerId = "843371";
		LOG.debug("CustomerId ##: " + CustomerId);
		String customerId = getForamtCustomerId(CustomerId);
		LOG.debug("CustomerId Post format##: " + customerId);
		
		/** Customer ID length should be 8 for S2M service calls 
		 * To Get CREDIT cards from S2M , customer Id format should be 'CXXXXXXX'
		 * Here C is first character of customer id and next 7 characters are actual customer ID.
		 * Current HBL users customer Id is 6 digits length, so adding prefix zero to the customer ID to match 8 digit length
		 * *********/
		
		params.put("bankId", bankId);
		params.put("ebankingUser", ebankingUser);
		params.put("ebankingPassword", ebankingPassword);
		params.put("customerId", customerId);
		return true;
	}
	
	public String getForamtCustomerId(String customerId) {
		int length = customerId.length();
		if(length == 6) {
			customerId = "C"+"0"+customerId;
		}else if(length == 4) {
			customerId = "C"+"000"+customerId;
		}else if(length == 5) {
			customerId = "C"+"00"+customerId;
		}else if(length == 7) {
			customerId = "C"+customerId;
		}else{
			customerId = "C"+customerId;
		}
		LOG.debug("CustoemrID: "+ customerId);
		return customerId;
	}
}
