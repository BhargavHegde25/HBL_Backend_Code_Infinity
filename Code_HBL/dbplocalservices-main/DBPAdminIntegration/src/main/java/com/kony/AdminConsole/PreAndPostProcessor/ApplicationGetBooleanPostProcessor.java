package com.kony.AdminConsole.PreAndPostProcessor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class ApplicationGetBooleanPostProcessor implements DataPostProcessor2 {
	
	@Override
	public Object execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Set<String> booleanFlagSet = new HashSet<>();
		booleanFlagSet.addAll(Arrays.asList(new String[] {"showAdsPostLogin","isSortCodeVisible","isBusinessBankingEnabled",
				"isAlertAccountIDLevel","isKeyCloakEnabled","isLanguageSelectionEnabled","isUTCDateFormattingEnabled",
				"isAccountAggregationEnabled","isSelfApprovalEnabled","isAccountCentricCore","isCountryCodeEnabled",
				"isBackEndCurencySymbolEnabled","isFeedbackEnabled","newSettings","stateManagementAvailable"}));
		
        List<Dataset> dsList = result.getAllDatasets();
        
 
        for (Dataset ds : dsList) {
        	
        	List<Record> recordList = ds.getAllRecords();
        
        	for(Record rec : recordList) {
        		List<Param> paramList = rec.getAllParams();
        		
        		for (Param param : paramList) {
        					
        			if(booleanFlagSet.contains(param.getName())) {
        				
        				if(param.getValue().equalsIgnoreCase("0"))  {
        					param.setObjectValue("false");
        				}
        				else if (param.getValue().equalsIgnoreCase("1")) {
        					param.setObjectValue("true");
        				}
     

        			}
        		}
        	}
        }
      
        return result;
        
    }

}
