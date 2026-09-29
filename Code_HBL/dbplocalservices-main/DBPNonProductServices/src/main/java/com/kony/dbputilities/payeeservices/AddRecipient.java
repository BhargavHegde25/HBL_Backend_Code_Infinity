package com.kony.dbputilities.payeeservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class AddRecipient implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
        	inputParams.put("Id", HelperMethods.getRandomNumericString(8));
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_CREATE);
        }
        if(HelperMethods.hasRecords(result)) {
    		Result rs = new Result();
    		rs.addAllParams(result.getAllDatasets().get(0).getRecord(0).getAllParams());
    		return rs;
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) throws Exception {

        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);

        if (StringUtils.isBlank(customerId)) {
            return false;
        }

        //boolean status = validate(inputParams, dcRequest, result);
        boolean status = true;
        if (status) {
            String nickName = (String) inputParams.get("payeeNickName");
            String accountNumber = (String) inputParams.get("payeeAccountNumber");
            String name = (String) inputParams.get("payeeName");
            String type = (String) inputParams.get("type");
            String userId = null;
            String isBusinessPayee = (String) inputParams.get("isBusinessPayee");
            String IBAN = (String) inputParams.get("IBAN");
            
            if(StringUtils.isNotBlank(isBusinessPayee) && isBusinessPayee.equals("1")) {
                String organizationId = (String) inputParams.get("companyId");
                if (StringUtils.isBlank(organizationId)) {
                	HelperMethods.setValidationMsg("Not a valid organizationId", dcRequest, result);
                	return false;
                }
                inputParams.put("organizationId", organizationId );
            }                    
            
            userId = (String) inputParams.get("userId");
            
            if(StringUtils.isBlank(userId)) {
            	userId = HelperMethods.getUserIdFromSession(dcRequest);
            }
            
            if (StringUtils.isBlank(userId)) {
                HelperMethods.setValidationMsg("Failed to fetch userId", dcRequest, result);
                status = false;
            }
           
            if(StringUtils.isBlank(name)) {
            	name = (String) inputParams.get("name");
            }
            if(StringUtils.isBlank(nickName)) {
            	nickName = (String) inputParams.get("nickName");
            }
            if(StringUtils.isBlank(accountNumber)) {
            	accountNumber = (String) inputParams.get("accountNumber");
            }
            if(StringUtils.isBlank(type)) {
            	type = (String) inputParams.get("typeId");
            }
            
            if (StringUtils.isBlank(IBAN)) {
            	IBAN = (String) inputParams.get("iban");
            }
            
            if(!StringUtils.isBlank(IBAN)) {
            	inputParams.put("IBAN", IBAN);
            }
            
            inputParams.put("nickName", nickName);
            inputParams.put("accountNumber", accountNumber);
            inputParams.put("name", name);
            inputParams.put("Type_id", type);
            inputParams.put("User_Id", userId);
            inputParams.put("isWiredRecepient", "1");
        }
        return status;
    }

    private boolean validate(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws Exception {
        boolean status = true;
        String nickName = inputParams.get("payeeNickName");
        String accountNumber = inputParams.get("payeeAccountNumber");
        if(StringUtils.isBlank(nickName)) {
        	nickName = (String) inputParams.get("nickName");
        }
        if(StringUtils.isBlank(accountNumber)) {
        	accountNumber = (String) inputParams.get("accountNumber");
        }
        
        if (StringUtils.isBlank(accountNumber) || StringUtils.isBlank(nickName)) {
            status = false;
            HelperMethods.setValidationMsg("Mandatory fields are missing.", dcRequest, result);
        }
        if (status && verifyExistingPayeeByNickName(inputParams, dcRequest, result)) {
            status = false;
            HelperMethods.setValidationMsg("Payee already exists", null, result);
        }

        return status;
    }

    private boolean verifyExistingPayeeByNickName(Map<String, String> inputParams, DataControllerRequest dcRequest,
            Result result) throws Exception {

        String nickName = inputParams.get("payeeNickName");
        String userId = inputParams.get("userId");
        if(StringUtils.isBlank(userId)) {
        	userId = HelperMethods.getUserIdFromSession(dcRequest);
        }
        String companyId = inputParams.get("companyId");
        String filter = "nickName" + DBPUtilitiesConstants.EQUAL + "'" + nickName + "'";
        filter = filter + DBPUtilitiesConstants.AND + "softdelete" + DBPUtilitiesConstants.EQUAL + "'0'";
        filter = filter + DBPUtilitiesConstants.AND + "isWiredRecepient" + DBPUtilitiesConstants.EQUAL + "'1'";
        filter = filter + DBPUtilitiesConstants.AND +  "("+ "User_Id" + DBPUtilitiesConstants.EQUAL + "'" + userId + "'";
        filter = filter + DBPUtilitiesConstants.OR + "organizationId" + DBPUtilitiesConstants.EQUAL + "'" + companyId + "'" + ")";
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        Result payeeDetails = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        return HelperMethods.hasRecords(payeeDetails);
    }
}
