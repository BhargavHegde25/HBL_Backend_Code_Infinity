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
import com.temenos.infinity.tradefinanceservices.dto.OutwardCollectionsDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.OutwardCollectionsResource;

public class DeleteOutwardCollectionOperation  implements JavaService2 {
	 @Override
	    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
	        OutwardCollectionsResource requestResource = DBPAPIAbstractFactoryImpl.getResource(OutwardCollectionsResource.class);
	        Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
	        OutwardCollectionsDTO inputDto = JSONUtils.parse(new JSONObject(inputParams).toString(), OutwardCollectionsDTO.class);
	        return requestResource.deleteCollection(inputDto, request);
	    }
}
