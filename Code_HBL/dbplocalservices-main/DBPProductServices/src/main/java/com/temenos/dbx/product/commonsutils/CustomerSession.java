package com.temenos.dbx.product.commonsutils;

import java.util.*;
import java.util.stream.Collectors;

import com.kony.dbputilities.util.LegalEntityUtil;
import com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.util.ApprovalUtilities;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;

public final class CustomerSession {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	/*
	 * Fetches the customer attributes from the identity session
	 */
	public static Map<String, Object> getCustomerMap(DataControllerRequest request) {

		try {
			Map<String, Object> customer = null;
			try {
				customer = CustomerSessionsUtil.getLoggedInUserAttributesMap(request);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				alert.prepareError(e.toString()).log();
			}
			return customer;
		} catch (NullPointerException e) {
			alert.prepareError(e.toString()).log();
		}
		 return null;
	}
	
	/*
     *Fetches the Customer_Type from the customer map and returns it
     */
	public static String getCustomerType(Map<String, Object> customer) {

		try {
			return customer.get("CustomerType_id").toString();
		} 
		catch (NullPointerException e) {
			alert.prepareError("Error while fetching CustomerType_id from customer map", e).log();
		}
		 return null;
	}
	
	/*
     *Fetches the Customer_Id from the customer map and returns it
     */
	public static String getCustomerId(Map<String, Object> customer) {

		try {
			return customer.get("customer_id").toString();
		} 
		catch (NullPointerException e) {
			alert.prepareError("Error while fetching customer_id from customer map", e).log();
		}
		 return null;
	}
	
	/*
     * Checks whether the user in the customer map is a Retail user or not and returns the same
     */
	public static boolean IsRetailUser(Map<String, Object> customer) {

		if("TYPE_ID_RETAIL".equals(getCustomerType(customer)))
				return true;
		return false;
	}
	
	/*
     * Checks whether the user in the customer map is a Small Business user or not and returns the same
     */
	public static boolean IsBusinessUser(Map<String, Object> customer) {

		if(Constants.TYPE_ID_BUSINESS.equals(getCustomerType(customer)))
				return true;
		return false;
	}
   
	/*
     * Checks whether the user in the customer map is a Combined Access user or not and returns the same
     */
	public static boolean IsCombinedUser(Map<String, Object> customer) {

		try {
			String isCombinedUser = customer.get("isCombinedUser").toString();
			if ("true".equals(isCombinedUser)) {
				return true;
			} else if ("false".equals(isCombinedUser)) {
				return false;
			}
		} catch (NullPointerException e) {
			alert.prepareError("Error while fetching isCombinedUser from customer map", e).log();
		}

		return false;
	}
   
	/*
     * Checks whether the logged in user is DBP_API user from C360
     */
	public static boolean IsAPIUser(Map<String, Object> customer) {

		if("DBP_API_USER".equals(getCustomerType(customer)))
				return true;
		return false;
	}
	
	/*
     *Fetches the Organization_Id from the customer map and returns it
     */
	public static String getCompanyId(Map < String, Object > customer) {

        try {
            String companyId = customer.get("Organization_Id").toString();
            if("".equals(companyId)) {
            	return null;
            }
            return companyId;
        } catch (NullPointerException e) {
        	alert.prepareError("Error while fetching Organization_Id from customer map", e).log();
        }
        return null;
    }
   
	/*
     *Fetches the Customer_Name from the customer map and returns it
     */
    public static String getCustomerName(Map < String, Object > customer) {

        try {
            return customer.get("UserName").toString();
        } catch (NullPointerException e) {
        	alert.prepareError("Error while fetching UserName from customer map", e).log();
        }
        return null;
    }
    
    /*
     *Fetches the Customer's FirstName and LastName from the customer map and returns it
     */
    public static String getCustomerCompleteName(Map < String, Object > customer) {
    	
        try {
             if(customer.get("FirstName")!=null && customer.get("LastName")!=null) 
            	 return (customer.get("FirstName").toString() + " " + customer.get("LastName").toString());
             else if(customer.get("FirstName")!=null)
            	 return customer.get("FirstName").toString();
        	 else if(customer.get("LastName")!=null)
            	 return customer.get("LastName").toString(); 
            	 
        } catch (NullPointerException e) {
        	alert.prepareError("Error while fetching UserName from customer map", e).log();
        }
        return "-";
    }
    
    /**
     * Fetches customer's SecurityAttributes from the identity scope and 
	 * filters permissions from session scope to check for requiredActionIds and returns the intersection set
     * @param request
     * @param requiredActionIds
     * @return
     */
	public static String getPermittedActionIds(DataControllerRequest request, List<String> requiredActionIds) {

		try {

			
		 	JSONObject featuresAndpermissionsObj = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(request);
	    	Object permissionsObj =  featuresAndpermissionsObj.get("permissions");	
			
			if(permissionsObj != null ) {
				String activeActions = "";
				String permissions = permissionsObj.toString();
				permissions = permissions.replaceAll("\"", "");
				permissions = permissions.substring(1, permissions.length()-1);
				
				List<String> permissionList = Arrays.asList(permissions.split(","));
				
				Set<String> result = permissionList.stream().distinct().filter(requiredActionIds::contains).collect(Collectors.toSet());
				
				if(result.size() > 0) {
					activeActions = result.toString();
					activeActions = activeActions.replaceAll("\\s","");
					activeActions = activeActions.substring(1, activeActions.length()-1);
					return activeActions;
				}
			}
			
		} 
		catch (NullPointerException e) {
			alert.prepareError(e.toString()).log();
		}
		 return null;
	}

	public static Set<String> getPermittedActionIdsSet(DataControllerRequest request){
		try {
			JSONObject featuresAndpermissionsObj = LegalEntityUtil.getUserCurrentLegalEntityFeaturePermissions(request);
	    	Object permissionsObj =  featuresAndpermissionsObj.get("permissions");	
			
			if(permissionsObj != null ) {
				String permissions = permissionsObj.toString();
				permissions = permissions.replaceAll("\"", "");
				permissions = permissions.substring(1, permissions.length()-1);
				return new HashSet<>(Arrays.asList(permissions.split(",")));
			}
		}
		catch (NullPointerException e) {
			alert.prepareError(e.toString()).log();
		}
		return null;
	}
}