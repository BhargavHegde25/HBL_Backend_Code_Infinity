package com.temenos.infinity.api.usermanagement.utilities;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MiddlewareException;

public final class CustomerSession {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
	/*
	 * Fetches the customer attributes from the identity session
	 */
	public static Map<String, Object> getCustomerMap(DataControllerRequest request) {

		try {
			Map<String, Object>  customer = request.getServicesManager().getIdentityHandler().getUserAttributes();
			return customer;
		} catch (MiddlewareException e) {
			alert.prepareError("Error while fetching customer attributes from the identity session", e).log();
		}
		catch (NullPointerException e) {
			alert.prepareError("Error occurred: ", e).log();
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

		if("TYPE_ID_BUSINESS".equals(getCustomerType(customer)))
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
     *Fetches the Customer_Name from the customer map and returns it
     */
    public static String getBankId(Map < String, Object > customer) {

        try {
            return customer.get("Bank_id").toString();
        } catch (NullPointerException e) {
        	alert.prepareError("Error while fetching UserName from customer map", e).log();
        }
        return null;
    }
}
