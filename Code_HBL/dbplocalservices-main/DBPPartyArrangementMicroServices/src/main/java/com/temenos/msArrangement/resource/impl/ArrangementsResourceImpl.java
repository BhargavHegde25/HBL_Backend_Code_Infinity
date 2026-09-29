package com.temenos.msArrangement.resource.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.msArrangement.businessdelegate.api.ArrangementsBusinessDelegate;
import com.temenos.msArrangement.dto.ArrangementsDTO;
import com.temenos.msArrangement.resource.api.ArrangementsResource; 

/**
 * 
 * @author smugesh
 * @version 1.0 Extends the {@link AccountsResource} 
 */
public class ArrangementsResourceImpl implements ArrangementsResource {
    
    //Initialize Result Object
    Result result = new Result(); 
    JSONObject responseObj = new JSONObject();
    //Get Instance of OfficerBusiness Delegate
    ArrangementsBusinessDelegate AccountsDelegateInstance = DBPAPIAbstractFactoryImpl.getInstance()
           .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ArrangementsBusinessDelegate.class);
    
    //Implementing the get Accounts method
    @Override
    public Result getArrangementAccounts(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) {
        
        String userID = HelperMethods.getCustomerIdFromSession(request);
        if (userID == null) {
            userID = request.getParameter("partyId"); 
        }
        
        String productLineId = request.getParameter("productLineId");
        if (productLineId == null) {
            productLineId = "ACCOUNTS";
        } 
        
        List<ArrangementsDTO> accountsDTO = AccountsDelegateInstance.getArrangements(userID, productLineId); 
        
        JSONArray accountsDTOArray = new JSONArray(accountsDTO); 
        for (int i = 0; i < accountsDTOArray.length(); i++) {
            JSONObject arrangement = accountsDTOArray.getJSONObject(i);
            String id = arrangement.getString("account_id");
            arrangement.put("Account_id", id);
            arrangement.remove("account_id");
        }
        responseObj.put("Accounts", accountsDTOArray);
        result = JSONToResult.convert(responseObj.toString()); 
        return result; 
    }
       
}