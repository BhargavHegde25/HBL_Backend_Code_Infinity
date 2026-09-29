package com.kony.makerchecker.javaservice;

import org.apache.commons.lang3.StringUtils;

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

public class MakerCheckerConfigManageService implements JavaService2 {

	private static final String GET_MAKER_CHECKER_CONFIGURATIONS = "getMakerCheckerConfigurations";
	private static final String EDIT_MAKER_CHECKER_CONFIGURATIONS = "editMakerCheckerConfigurations";

	private static final Alert alert = Logger.forAlert().forModule(UtilConstants.INFINITY, UtilConstants.SPOTLIGHT);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		try {
			if (StringUtils.equalsIgnoreCase(methodId, GET_MAKER_CHECKER_CONFIGURATIONS)) {
				return getMakerCheckerConfigurations(methodId, inputArray, request, response);
			} else if (StringUtils.equalsIgnoreCase(methodId, EDIT_MAKER_CHECKER_CONFIGURATIONS)) {
				return editMakerCheckerConfigurations(methodId, inputArray, request, response);
			}
		} catch (Exception e) {
			Result result = new Result();
			alert.prepareError("Exception in Maker Checker Config Manage Service.", e).log();
			ErrorCodesEnum.ERR_10025.setErrorCode(result);
			return result;
		}
		return new Result();
	}

	private Object getMakerCheckerConfigurations(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		MakerCheckerResource makerCheckerResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(MakerCheckerResource.class);
		result = makerCheckerResource.getMakerCheckerConfig(methodId, inputArray, request, response);
		return result;
	}

	private Object editMakerCheckerConfigurations(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result result = new Result();
		MakerCheckerResource makerCheckerResource = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(ResourceFactory.class).getResource(MakerCheckerResource.class);
		result = makerCheckerResource.updateMakerCheckerConfig(methodId, inputArray, request, response);
		return result;
	}

}
