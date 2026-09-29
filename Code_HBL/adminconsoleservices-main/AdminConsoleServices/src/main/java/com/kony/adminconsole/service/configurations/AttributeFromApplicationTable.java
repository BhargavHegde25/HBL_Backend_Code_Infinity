package com.kony.adminconsole.service.configurations;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.adminconsole.handler.CustomerHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AttributeFromApplicationTable implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		Result result = new Result();
		
		String[] attributes = request.getParameter("attribute").trim().split(",");
		try {
			
			if(attributes.length == 0) {
				ErrorCodeEnum.ERR_21808.setErrorCode(result);
				return result;
			} else {
				for(int index = 0; index < attributes.length; index++){
					attributes[index] = attributes[index].trim();
				}
			}
			
			CustomerHandler handler = new CustomerHandler();
			String[] attrValues = handler.getAttributesFromApplicationTable(attributes, request);
			
			for(int index = 0; index < attributes.length; index++){
				if(!StringUtils.isEmpty(attrValues[index])){
					result.addStringParam(attributes[index], attrValues[index]);
				}
			}
			
		} catch(Exception exp) {
			alert.prepareError("Exception occured in AttributeFromApplicationTable Service : ", exp).log();
			ErrorCodeEnum.ERR_21809.setErrorCode(result);
		}
		
		return result;
	}

}
