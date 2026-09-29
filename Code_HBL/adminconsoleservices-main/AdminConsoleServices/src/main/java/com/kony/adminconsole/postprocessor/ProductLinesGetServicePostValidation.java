package com.kony.adminconsole.postprocessor;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ProductLinesGetServicePostValidation implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		try {
			Dataset productLines = result.getDatasetById("productLines");
			List<Record> productLinesRecords = null;
			if(productLines!=null)
				productLinesRecords = productLines.getAllRecords();
			if(productLinesRecords!=null && productLinesRecords.size()>0) {
				for(Record productLinesRecord : productLinesRecords) {
					if(!productLinesRecord.hasParamByName("productLineId")) {
						productLinesRecord.addParam("productLineId","");
					}if(!productLinesRecord.hasParamByName("productLineName")) {
						productLinesRecord.addParam("productLineName","");
					}if(!productLinesRecord.hasParamByName("productLineRef")) {
						productLinesRecord.addParam("productLineRef","");
					}if(!productLinesRecord.hasParamByName("externalIndicator")) {
						productLinesRecord.addParam("externalIndicator","");
					}
				}
			}
		}catch(Exception e) {
			result.addParam("Exception",e.toString());
		}
		return result;
	}

}
