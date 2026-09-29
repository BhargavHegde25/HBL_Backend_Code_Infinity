package com.kony.adminconsole.postprocessor;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ProductGroupsGetServicePostValidation implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		
		try {
			
			Dataset productGroups = result.getDatasetById("productGroups");
			List<Record> productGroupRecords = null;
			if(productGroups!=null)
				productGroupRecords= productGroups.getAllRecords();
			if(productGroupRecords!=null &&productGroupRecords.size()>0 ) {
				for(Record productGroupRecord : productGroupRecords) {
					if(!productGroupRecord.hasParamByName("description")) {
						productGroupRecord.addParam("description","");
					}if(!productGroupRecord.hasParamByName("detailedDesc")) {
						productGroupRecord.addParam("detailedDesc","");
					}if(!productGroupRecord.hasParamByName("productGroupId")) {
						productGroupRecord.addParam("productGroupId","");
					}if(!productGroupRecord.hasParamByName("productGroupName")) {
						productGroupRecord.addParam("productGroupName","");
					}if(!productGroupRecord.hasParamByName("productGroupRef")) {
						productGroupRecord.addParam("productGroupRef","");
					}if(!productGroupRecord.hasParamByName("productLineId")) {
						productGroupRecord.addParam("productLineId","");
					}if(!productGroupRecord.hasParamByName("productLineRef")) {
						productGroupRecord.addParam("productLineRef","");
					}
					//Image Details
					
					Dataset productGroupimageDetails = productGroupRecord.getDatasetById("imageDetails");
					List<Record> productGroupimageDetailsRecords = null;
					if (productGroupimageDetails != null)
						productGroupimageDetailsRecords = productGroupimageDetails.getAllRecords();
					if (productGroupimageDetailsRecords != null && productGroupimageDetailsRecords.size() > 0) {
						for (Record imageRecord : productGroupimageDetailsRecords) {
							if (!imageRecord.hasParamByName("height")) {
								imageRecord.addParam("height", "");
							}
							if (!imageRecord.hasParamByName("width")) {
								imageRecord.addParam("width", "");
							}
							if (!imageRecord.hasParamByName("imageType")) {
								imageRecord.addParam("imageType", "");
							}
							if (!imageRecord.hasParamByName("imageUrl")) {
								imageRecord.addParam("imageUrl", "");
							}
						}
					}
				}
			}
			
		}catch(Exception e) {
			result.addParam("Exception", e.toString());
		}
		return result;
	}

}
