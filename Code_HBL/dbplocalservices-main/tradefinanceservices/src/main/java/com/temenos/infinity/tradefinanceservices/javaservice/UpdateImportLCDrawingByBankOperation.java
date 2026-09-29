package com.temenos.infinity.tradefinanceservices.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.tradefinanceservices.dto.DrawingsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.LetterOfCreditDrawingsResource;

public class UpdateImportLCDrawingByBankOperation implements JavaService2{
	 public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		 LetterOfCreditDrawingsResource requestResource = DBPAPIAbstractFactoryImpl.getResource(LetterOfCreditDrawingsResource.class);
	        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
	        DrawingsDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), DrawingsDTO.class);
	        return requestResource.updateImportLCDrawingByBank(inputDto, request);
	    }
}
