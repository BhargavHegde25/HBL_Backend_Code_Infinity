package com.bct.preprocessor;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class cardChangePINPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(cardChangePINPreprocessor.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String bankId = paramHelper.getServerProperty("HBL_BANK_ID");
		String ebankingUser = paramHelper.getServerProperty("S2M_E_BANKING_USER");
		String ebankingPassword = paramHelper.getServerProperty("S2M_E-BANKING_PASSWORD");
		
		params.put("Bank_id", bankId);
		params.put("User", ebankingUser);
		params.put("PWD", ebankingPassword);
		params.put("in_fees", "0"); //No fee for change pin
		params.put("ref_type", "N"); //Always Value is 'N'
		
		return true;
	}
}
