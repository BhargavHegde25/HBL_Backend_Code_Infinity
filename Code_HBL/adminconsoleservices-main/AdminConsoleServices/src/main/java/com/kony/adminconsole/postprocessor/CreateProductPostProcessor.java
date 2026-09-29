package com.kony.adminconsole.postprocessor;

import org.apache.commons.lang3.StringUtils;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class CreateProductPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			if (StringUtils.isBlank(result.getParamValueByName("code"))) {
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
						ActivityStatusEnum.SUCCESSFUL, "Product Created Successfully");
			} else {
				AuditHandler.auditAdminActivity(request, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
						ActivityStatusEnum.FAILED,
						"Failed to create product :" + result.getParamValueByName("message"));
			}
			return result;
		}

		catch (Exception e) {
			result.addParam(new Param("FailureReason", e.getMessage()));
			ErrorCodeEnum.ERR_20716.setErrorCode(result);
			return null;
		}
	}
}
