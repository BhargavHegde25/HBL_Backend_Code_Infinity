package com.bct.preprocessor;

import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class getFDRatesPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(getFDRatesPreprocessor.class);

	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		String depositType = request.getParameter("depositType");
		LOG.debug("depositType##:" + depositType);
		String FDType  = "";
		if(depositType.equals("1")) {
			//NFD
			LOG.debug("depositType1##:" + depositType);
			FDType = "Normal Fixed Deposit";
		}else if(depositType.equals("2")) {
			//HFD
			LOG.debug("depositType2##:" + depositType);
			FDType = "Himal Remit Fixed Deposit";
		}else if(depositType.equals("3")) {
			//SFD
			LOG.debug("depositType3##:" + depositType);
			FDType = "Structure Fixed Deposit";
		}
		//LOG.debug("depositType##:" + depositType);
		//String FDType = getDepositType(depositType.trim());
		
		LOG.debug("depositType fianl val##:" + FDType);
		params.put("depositType", URLEncoder.encode(FDType));
		return true;
	}

}
