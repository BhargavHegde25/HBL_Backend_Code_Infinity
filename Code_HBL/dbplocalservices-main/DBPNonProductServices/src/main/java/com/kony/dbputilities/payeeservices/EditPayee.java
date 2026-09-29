package com.kony.dbputilities.payeeservices;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class EditPayee implements JavaService2 {

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_UPDATE);
        } else {
            result.addParam(new Param("errmsg", "Not a valid User", "String"));
        }
        
        if(HelperMethods.hasRecords(result)) {
    		Result rs = new Result();
    		rs.addAllParams(result.getAllDatasets().get(0).getRecord(0).getAllParams());
    		return rs;
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String id = (String) inputParams.get("payeeId");
		if (StringUtils.isBlank(id)) {
			id = (String) inputParams.get("id");
        }	
        		
        if (StringUtils.isBlank(id)) {
            HelperMethods.setValidationMsg("Please provide payee id.", dcRequest, result);
            status = false;
        }
        
        String userid = null;
        if(inputParams.get("userId") != null) {
        	userid = inputParams.get("userId").toString();
        }
        else {
        	userid = HelperMethods.getCustomerIdFromSession(dcRequest);
        }

        if (StringUtils.isBlank(userid)) 
            return false;
        
        String isBusinessPayee = null;
        String organizationId = null;
        
        if(inputParams.get("isBusinessPayee") != null)
            isBusinessPayee = inputParams.get("isBusinessPayee").toString();
        
        if(StringUtils.isNotBlank(isBusinessPayee) && "1".equals(isBusinessPayee) && inputParams.get("companyId") != null) {
        	organizationId = inputParams.get("companyId").toString();
        }
        
        String filter = "User_Id" + DBPUtilitiesConstants.EQUAL + userid;
        
        if(StringUtils.isNotBlank(organizationId)) {
        	filter = filter + DBPUtilitiesConstants.OR + "organizationId" + DBPUtilitiesConstants.EQUAL + organizationId;
        }
        
        filter = "(" + filter + ")";
        
        Result result2 = HelperMethods.callGetApi(dcRequest, filter.toString(), HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);

        if (!HelperMethods.hasRecords(result2)) {
            return false;
        }

        boolean isValid = false;
        List<Record> list = HelperMethods.getDataSet(result2).getAllRecords();
        for (Record record : list) {
            if (record.getParam("Id").getValue().equals(id)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            return false;
        }

        String nickName = (String) inputParams.get("payeeNickName");
        if (StringUtils.isBlank(nickName)) {
        	nickName = (String) inputParams.get("nickName");
        }
        String ebillEnable = (String) inputParams.get("EBillEnable");
        if (StringUtils.isBlank(ebillEnable)) {
        	ebillEnable = (String) inputParams.get("ebillEnable");
        }
        String name = (String) inputParams.get("payeeName");
        if (StringUtils.isBlank(name)) {
        	name = (String) inputParams.get("name");
        }
        inputParams.put("Id", id);
        inputParams.put("nickName", nickName);
        inputParams.put("eBillEnable", ebillEnable);
        inputParams.put("name", name);
        
        removeNullValues(inputParams);

        filter = filter + DBPUtilitiesConstants.AND + "Id" + DBPUtilitiesConstants.EQUAL + id;
        
        Result chkResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        if (HelperMethods.hasRecords(chkResult)) {
            status = true;
        } else {
            status = false;
        }
        return status;
    }
    public static void removeNullValues(Map<String, String> map) {
    	Iterator<String> itr = map.keySet().iterator();
    	while (itr.hasNext()) {
    		Object key = itr.next();
    		if((String) map.get(key)==null) {
    			itr.remove();
    		}
    	}
    }
}
