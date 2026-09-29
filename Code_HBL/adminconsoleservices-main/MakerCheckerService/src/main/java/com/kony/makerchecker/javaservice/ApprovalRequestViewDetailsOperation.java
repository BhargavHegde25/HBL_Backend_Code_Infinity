package com.kony.makerchecker.javaservice;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.makerchecker.resource.api.MakerCheckerResource;
import com.kony.makerchecker.utils.ErrorCodesEnum;
import com.kony.makerchecker.utils.UtilConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class ApprovalRequestViewDetailsOperation implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {
		Result result = new Result();

		MakerCheckerResource makerCheckerResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(MakerCheckerResource.class);

		result = makerCheckerResource.approvalRequestViewDetails(methodId, inputArray, request, response);

		return result;
	} catch(Exception e) {
		alert.prepareError("Exception occurred in invoking ApprovalRequestViewDetailsOperation: " + e).log();
        return ErrorCodesEnum.ERR_10005.setErrorCode(new Result());
	}
	}

}