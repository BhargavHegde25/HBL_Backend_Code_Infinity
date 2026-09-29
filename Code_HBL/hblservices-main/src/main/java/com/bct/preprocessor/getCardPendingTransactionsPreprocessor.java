package com.bct.preprocessor;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.StringUtil;

import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class getCardPendingTransactionsPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(getCardPendingTransactionsPreprocessor.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String bankId = paramHelper.getServerProperty("HBL_BANK_ID");
		String ebankingUser = paramHelper.getServerProperty("S2M_E_BANKING_USER");
		String ebankingPassword = paramHelper.getServerProperty("S2M_E-BANKING_PASSWORD");
		
		/** This section has to delete post testing 
		 * Start
		 */
		
		//params.put("cardNumber", "4101020000014231");
		params.put("dateFrom", "20/04/2020");
		
		/** End
		 * 
		 */
		params.put("bankId", bankId);
		params.put("ebankingUser", ebankingUser);
		params.put("ebankingPassword", ebankingPassword);
		
		return true;
	}
}
