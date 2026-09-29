package com.kony.adminconsole.postprocessor;

import java.util.ArrayList;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.adminconsole.commons.utils.FabricConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Record;
public class GetEligibityCriteriaPostProcessor implements DataPostProcessor2{

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		String authToken = request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
		
		if(authToken == null) {
			if(null != result.getDatasetById("eligibilitycriteria")) {
				List<Record> recs =  new ArrayList<Record>();
				recs =  result.getDatasetById("eligibilitycriteria").getAllRecords();
				
				for (Record record : recs) {
					record.removeParamByName("createdby");
				}
			}
		}
		
		return result;
	} 
	
}
