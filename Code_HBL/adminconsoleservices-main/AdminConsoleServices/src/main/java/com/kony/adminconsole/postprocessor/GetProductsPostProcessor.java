package com.kony.adminconsole.postprocessor;

import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;

public class GetProductsPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Log4j2Configurator.getInstance();
		try {
			Dataset marketingCatalog = result.getDatasetById("marketingCatalog");
			Dataset finalDataset = new Dataset("records");
			if(marketingCatalog!=null) {
				List<Record> MCRecords = marketingCatalog.getAllRecords();
				for (Record MCRecord : MCRecords) {
					Dataset productGroups = MCRecord.getDatasetById("productGroups");
					if(productGroups!=null) {
						List<Record> productGroupRecords = productGroups.getAllRecords();
					for(Record productGroupRecord : productGroupRecords ) {
						Dataset productRecords = productGroupRecord.getDatasetById("records");
						List<Record> finalRecords = productRecords.getAllRecords();
						for(Record finalRecord : finalRecords) {
							finalRecord.addParam("MarketingStateId",StringUtils.EMPTY);
							finalRecord.addParam("rates",StringUtils.EMPTY);
							finalRecord.addParam("softdeleteflag",Boolean.FALSE.toString());
							finalRecord.addParam("otherproducttype_Name",StringUtils.EMPTY);
							finalRecord.addParam("termsAndConditions",StringUtils.EMPTY);
							finalRecord.addParam("otherproducttype_Description",StringUtils.EMPTY);
							finalRecord.addParam("isLeadSupported",StringUtils.EMPTY);
							finalRecord.addParam("SecondaryProduct_id",StringUtils.EMPTY);
							finalRecord.addParam("features",StringUtils.EMPTY);
							finalRecord.addParam("otherproducttype_id",StringUtils.EMPTY);
							finalRecord.addParam("info",StringUtils.EMPTY);
							finalRecord.addParam("Type_id",StringUtils.EMPTY);
							finalRecord.addParam("lastmodifiedts",StringUtils.EMPTY);
							finalDataset.addRecord(finalRecord);
						}
							
					}
					}
				}
			}
			result.addDataset(finalDataset);
			result.removeDatasetById("marketingCatalog");
		} catch (Exception e) {
			result.addParam("Exception", e.toString());
			
		}
	return result;
	}

}
