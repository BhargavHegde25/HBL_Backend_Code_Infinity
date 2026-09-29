package com.temenos.auth.admininteg.operation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 *  @author g.sailendra
 *  Java Service end point which will fetch the data for the customer from the customerlegalentity table
 *          
 */

public class GetAllEntitiesOfCustomerOperation implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Result result = new Result();
		Result customerLegalEntity = new Result();
        Map<String, String> inputParams = new HashMap<>();
        try {
            Result allLegalEntities = (Result) new GetLegalEntitiesOperation().invoke(methodId, inputArray,
                requestInstance, responseInstance);
            alert.prepareError("allLegalEntity" + allLegalEntities).log();
            String CustomerId = HelperMethods.getCustomerIdFromSession(requestInstance);
            if (StringUtils.isBlank(CustomerId)) {
                result.addErrMsgParam("customerId was null or empty");
                return result;
            }
            if (!HelperMethods.hasRecords(allLegalEntities)) {
                result.addErrMsgParam("company legal units response was empty or null");
                return result;
            }
            String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + CustomerId;
            inputParams.put(DBPUtilitiesConstants.FILTER, filter);
            alert.prepareError("filter to get all entities : " + inputParams).log();
            customerLegalEntity = HelperMethods.callApi(requestInstance, inputParams, HelperMethods.getHeaders(requestInstance), URLConstants.CUSTOMERLEGALENTITY_GET);
            inputParams.clear();

            Map < String, String > customerLegalEntitySet = new HashMap < > ();
            customerLegalEntitySet = getCompanyNameMap(allLegalEntities);
            alert.prepareError("customerLegalEntitySet" + customerLegalEntitySet).log();
            result = createResultForGetEntities(customerLegalEntitySet, customerLegalEntity);
            customerLegalEntitySet.clear();
            return result;
        } catch (Exception e) {
            result.addErrMsgParam("error accured while fetching entities of customer");
            return result;
        }
	}
	public static Map<String, String> getCompanyNameMap (Result CompanyName) {
		Map<String, String> customerLegalEntitySet = new HashMap<>();
		
			List<Record> customerLegalEntityRecords = CompanyName
					.getDatasetById("companyLegalUnits").getAllRecords();
							
			for(Record s : customerLegalEntityRecords) {
				customerLegalEntitySet.put(s.getParamValueByName("id"),s.getParamValueByName("companyName"));
			}
		return  customerLegalEntitySet;
	}
	
	public static Result createResultForGetEntities(Map<String, String> customerLegalEntitySet,Result customerLegalEntity) {
		Iterator<Record> customerLegalEntitiesITR = customerLegalEntity
				.getAllDatasets().get(0).getAllRecords().iterator();
		Result returnResult = new Result();
		Dataset returnDs = new Dataset();
		returnDs.setId("customerlegalentity");
		returnResult.addDataset(returnDs);
		Record temp = null;
		String leid = "";
		while(customerLegalEntitiesITR.hasNext()) {
			temp = customerLegalEntitiesITR.next();
			leid = temp.getParamValueByName(DBPUtilitiesConstants.LEGALENTITYID);
			temp.addParam("companyName", customerLegalEntitySet.get(leid));
			returnDs.addRecord(temp);
		}
		return returnResult;	
	}
}
