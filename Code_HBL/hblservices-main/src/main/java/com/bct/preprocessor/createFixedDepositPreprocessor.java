package com.bct.preprocessor;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.utilities.HBLCommonUtility;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class createFixedDepositPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(createFixedDepositPreprocessor.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		ServicesManager sm = request.getServicesManager();
		ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
		String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
		LOG.debug("coreIdentifier##:"+ coreIdentifier);
		String productIdFromReq = request.getParameter("productId");
		LOG.debug("productIdFromReq##:"+ productIdFromReq);
		//String productId = HBLCommonUtility.getProductId(productIdFromReq);
		params.put("customerId", coreIdentifier);
		
		String tenure = request.getParameter("tenure");
		LOG.debug("tenure##:"+ tenure);
		params.put("tenure", tenure+"M");
		//customerId //currency	//productId	//intrestRate	//fromAccount	//amount
		
		return true;
	}
}
