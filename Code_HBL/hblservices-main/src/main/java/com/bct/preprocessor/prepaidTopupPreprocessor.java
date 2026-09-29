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
import com.bct.utilities.HBLCommonUtility;

public class prepaidTopupPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(prepaidTopupPreprocessor.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String bankId = paramHelper.getServerProperty("HBL_BANK_ID");
		String ebankingUser = paramHelper.getServerProperty("S2M_E_BANKING_USER");
		String ebankingPassword = paramHelper.getServerProperty("S2M_E-BANKING_PASSWORD");
		String nprCurrencyValue = paramHelper.getServerProperty("CARD_TOPUP_NPR_CURRENCY_CONVERSION");
		String usdCurrencyValue = paramHelper.getServerProperty("CARD_TOPUP_USD_CURRENCY_CONVERSION");
		
		String topupAmou = request.getParameter("topupAmou");
		LOG.debug("topupAmou ##:"+ topupAmou);
		
		String cCardNumber = request.getParameter("cCardNumber");
		LOG.debug("cCardNumber ##:"+ cCardNumber);
		
		String mxpAccountNumber = request.getParameter("mxpAccountNumber");
		LOG.debug("mxpAccountNumber ##:"+ mxpAccountNumber);
		
		String topupCurrency = request.getParameter("topupCurrency");
		LOG.debug("topupCurrency ##:"+ topupCurrency);
		
		double feeValue = HBLCommonUtility.getTopupFee(Double.parseDouble(topupAmou), request);
		LOG.debug("feeValue ##:"+ String.valueOf(feeValue));
		
		String currencyVal = (topupCurrency.equalsIgnoreCase("NPR")) ?nprCurrencyValue : usdCurrencyValue;
		
		params.put("bankId", bankId);
		params.put("ebankingUser", ebankingUser);
		params.put("ebankingPassword", ebankingPassword);
		params.put("cCardRefNumber", "N"); 
		params.put("topupFees", String.valueOf(feeValue));
		params.put("topupCurrency",currencyVal);
		
		return true;
	}
}
