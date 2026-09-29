package com.temenos.infinity.wealth.tap.processor.post;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.infinity.api.commons.utils.Utilities;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.util.PortfolioWealthUtils;
import com.temenos.infinity.wealth.common.util.PortfolioServiceUtils;

/**
 * @author sarah
 *
 */
public class GetCustomersOrchPostProcessor implements DataPostProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		diagnostic.prepareDebug("==========> GetCustomersOrchPostProcessor TAP - Entered ").log();
		if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
				&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
						|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
			Object coreCustId = request.getParameter("coreCustomerId");
			if (coreCustId == null || coreCustId.equals("")) {
				//diagnostic.prepareDebug("==========> GetWatchlistDBPreProcessor Mock -Error:: invalid operation").log();
				alert.prepareError("Error:Invalid input. Mandatory fields not given").log();
				//result.addParam("status", "Failure");
				result.addParam("error", "Invalid Input! " + TemenosConstants.CORECUSTOMERID + " is mandatory.");
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.FAILURE);
				return result;
			}else {
			String contractType = PortfolioWealthUtils.getContractFromCache(request);
			contractType = (contractType.equalsIgnoreCase("TYPE_ID_WEALTH"))? "true":"false";
			Dataset allocatSet1 = result.getDatasetById("Customers1");
			Dataset allocatSet2 = result.getDatasetById("Customers2");
			//diagnostic.prepareDebug("==========> GetCustomersOrchPostProcessor TAP - No. of records returned initially: " + customers1.length() ).log();
			JSONArray combinedCustomers = new JSONArray();
			JSONArray allCustomers = new JSONArray();
			if(allocatSet1 != null && allocatSet2 !=null) {
				JSONArray customers1 = ResultToJSON.convertDataset(allocatSet1);
				JSONArray customers2 = ResultToJSON.convertDataset(allocatSet2);
			if(customers1.length()==0)
			{
				for(int i=0; i<customers2.length();i++)
				{
					JSONObject customer2Obj = customers2.getJSONObject(i);
					customer2Obj.put("isFavourite", "false");
					allCustomers.put(customer2Obj);
				}
			}
	        for (int i = 0; i < customers1.length(); i++) {
	            JSONObject customer = new JSONObject();
	            JSONObject customer1Obj = customers1.getJSONObject(i);
	            String backendId = customer1Obj.optString("customerId");
                JSONObject matchingCustomer2 = findCustomerByCode(customers2, backendId);
                if (matchingCustomer2 != null) {
                    for (String key : customer1Obj.keySet()) {
                        customer.put(key, customer1Obj.get(key));
                    }

                    for (String key : matchingCustomer2.keySet()) {
                        customer.put(key, matchingCustomer2.get(key));
                    }
                    
                    allCustomers.put(customer);
                    
                    
                }
	        }
			}
			
			combinedCustomers = PortfolioServiceUtils.sortJSONArray(allCustomers);

	        String isMultiCustomer = combinedCustomers.length() == 1 ? "false" : "true";
	        JSONObject finalObject = new JSONObject();
	        finalObject.put("Customers", combinedCustomers);
	        finalObject.put("isWealthUser", contractType);
	        finalObject.put("isMultiCustomer", isMultiCustomer);
	        result = Utilities.constructResultFromJSONObject(finalObject);
	        result.addOpstatusParam("0");
	        result.addHttpStatusCodeParam("200");
	        result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
	        diagnostic.prepareDebug("==========> GetCustomersOrchPostProcessor TAP - Exited ").log();
	        return result;
		}
		}
		else {
			result.addOpstatusParam("0");
			result.addHttpStatusCodeParam("200");
			result.addParam(TemenosConstants.STATUS, TemenosConstants.FAILURE);
	
		}
		diagnostic.prepareDebug("==========> GetCustomersOrchPostProcessor TAP - Exited ").log();
		return result;
	}
	private JSONObject findCustomerByCode(JSONArray customers, String code) {
        for (int i = 0; i < customers.length(); i++) {
            JSONObject customer = customers.getJSONObject(i);
            if (customer.optString("customerId").equals(code)) {
                return customer;
            }
        }
        return null;
    }

}

