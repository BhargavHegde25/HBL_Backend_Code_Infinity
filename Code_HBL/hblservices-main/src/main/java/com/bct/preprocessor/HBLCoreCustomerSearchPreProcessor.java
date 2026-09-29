package com.bct.preprocessor;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;



public class HBLCoreCustomerSearchPreProcessor extends TemenosBasePreProcessor{

	 private static final Logger LOG = LogManager.getLogger(HBLCoreCustomerSearchPreProcessor.class);

	    @Override
	    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
	            Result result)
	            throws Exception {

	        String id = getKey(request, params, "_id");
	        String name = getKey(request, params, "_name");
	        String phone = getKey(request, params, "_phone");
	        String email = getKey(request, params, "_email");
	        String dateOfBirth = getKey(request, params, "_dateOfBirth");
	        String status = getKey(request, params, "_status");
	        String city = getKey(request, params, "_city");
	        String country = getKey(request, params, "_country");
	        String zipCode = getKey(request, params, "_zipCode");
	        String taxId = getKey(request, params, "_taxId");
	        String accountNumber = getKey(request, params, "_accountNumber");
	        String accountName = getKey(request, params, "_accountName");
	        String mobileNumber = getKey(request, params, "_mobileNumber");
	        
	        
	        StringBuilder sb = new StringBuilder();
	        if (StringUtils.isNotBlank(id)) {
	            sb.append("customerId").append("=").append(id);
	            
	        }
	        if (StringUtils.isNotBlank(name)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("lastName").append("=").append(name);
	        }
	        /*
	        if (StringUtils.isNotBlank(phone)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("phoneNumber").append("=").append(phone);
	        }
	        if (StringUtils.isNotBlank(email)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("email").append("=").append(email);
	        }
	        */
	        if (StringUtils.isNotBlank(dateOfBirth)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("dateOfBirth").append("=").append(dateOfBirth);
	        }
	        if (StringUtils.isNotBlank(city)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("addressCity").append("=").append(city);
	        }
	        if (StringUtils.isNotBlank(status)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("customerStatus").append("=").append(status);
	        }
	        if (StringUtils.isNotBlank(country)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("country").append("=").append(country);
	        }
	        if (StringUtils.isNotBlank(zipCode)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("postCode").append("=").append(zipCode);
	        }
	        if (StringUtils.isNotBlank(taxId)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            //sb.append("taxId").append("=").append(taxId);//in T24 taxId is mapped to legalId
	            sb.append("legalId").append("=").append(taxId);
	        }
	        
	        // HBL Requirement to validate customer
	        
	        if (StringUtils.isNotBlank(accountNumber)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("accountNo").append("=").append(accountNumber);
	        }
	        
	        if (StringUtils.isNotBlank(mobileNumber)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("phoneNo").append("=").append(mobileNumber);
	        }
	        
	        if (StringUtils.isNotBlank(email)) {
	            if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("emailId").append("=").append(email);
	        }
	        if (StringUtils.isNotBlank(accountName)) {
	            /*if (StringUtils.isNotBlank(sb.toString()))
	                sb.append("&");
	            sb.append("accountHolderName").append("=").append(accountName);
	            */
	        }
	       
	        params.put("filter", sb.toString());
	        LOG.error("Filter corecustomer search preprocessor :" + sb.toString());
	        return Boolean.TRUE;
	    }

	    private String getKey(DataControllerRequest request, HashMap params, String key) {
	        if (params.containsKey(key) && params.get(key) != null && params.get(key) != "")
	            return params.get(key).toString();
	        else if (request.getParameter(key) != null && request.getParameter(key) != "")
	            return request.getParameter(key);
	        return "";
	    }
	}

