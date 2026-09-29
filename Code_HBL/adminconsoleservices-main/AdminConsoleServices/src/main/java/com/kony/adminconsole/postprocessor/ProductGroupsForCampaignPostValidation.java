package com.kony.adminconsole.postprocessor;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ProductGroupsForCampaignPostValidation implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			Dataset marketingCatalog = result.getDatasetById("marketingCatalog");
			Dataset finalDataset = new Dataset("productGroups");
			if(marketingCatalog!=null) {
				List<Record> MCRecords = marketingCatalog.getAllRecords();
				for (Record MCRecord : MCRecords) {
					Dataset productGroups = MCRecord.getDatasetById("productGroups");
					if(productGroups!=null) {
						List<Record> productGroupRecords = productGroups.getAllRecords();
					for(Record productGroupRecord : productGroupRecords ) {
						if(!productGroupRecord.hasParamByName("productGroupName")) {
							productGroupRecord.addParam("productGroupName","");
						}if(!productGroupRecord.hasParamByName("productGroupId")) {
							productGroupRecord.addParam("productGroupId","");
						}
						finalDataset.addRecord(productGroupRecord);
					}
					}
				}
			}
			result.addDataset(finalDataset);
			result.removeDatasetById("marketingCatalog");
		} catch (Exception e) {
			result.addParam("Exception", e.toString());
			result.addParam("Test5", "test");
		}
		return result;
	}

}
