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

public class CardEMIConvertPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(CardEMIConvertPreprocessor.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String bankId = paramHelper.getServerProperty("HBL_BANK_ID");
		String ebankingUser = paramHelper.getServerProperty("S2M_E_BANKING_USER");
		String ebankingPassword = paramHelper.getServerProperty("S2M_E-BANKING_PASSWORD");
		String feeFlag = paramHelper.getServerProperty("CARD_EMI_FEE_FLAG"); //bank will change later based on no of EMI
		String instNumb = paramHelper.getServerProperty("CARD_EMI_INSTA_NUMBER"); //bank will change later based on no of EMI
		
		params.put("bankId", bankId);
		params.put("ebankingUser", ebankingUser);
		params.put("ebankingPassword", ebankingPassword);
		params.put("feeFlag", feeFlag);
		params.put("instNumb", instNumb);
		params.put("postFlag", "P"); //This value always 'P'
		
		return true;
	}
}
