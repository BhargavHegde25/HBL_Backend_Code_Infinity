package com.kony.adminconsole.service.productmanagement.backenddelegate.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.google.gson.JsonElement;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.service.productmanagement.backenddelegate.api.ProductBackendDelegate;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;


public class ProductBackendDelegateImpl implements ProductBackendDelegate {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	@Override
	public JSONObject createProduct(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_CREATE_PRODUCT)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject createProductFacilities(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_CREATE_PRODUCT_FACILITIES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	@Override
	public JSONObject createProductFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
			
		
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_CREATE_PRODUCT_FEATURES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}
        else {
		       
			JSONArray inputFeatures = new JSONArray(postParametersMap.get("features").toString());
			 JSONObject responseJSON =new JSONObject() ;
			 Map<String, Object> inputParameterMap = new HashMap<>();
			for(int i=0 ; i < inputFeatures.length() ; i++ ) {
				JSONObject featureRecord =  inputFeatures.getJSONObject(i);
				String featureId = featureRecord.getString("featureId");
				inputParameterMap.put("featureCode", featureId);
				inputParameterMap.put("productId", postParametersMap.get("productId").toString());
				inputParameterMap.put("featureId", "FR"+featureId);
				inputParameterMap.put("status", "Active");
				inputParameterMap.put("productRef", postParametersMap.get("productRef").toString());
//				postParametersMap.remove("marketingCatalogBackend");
	            String response = DBPServiceExecutorBuilder.builder().
	                    withServiceId(ServiceId.CRUDLAYER).
	                    withOperationId(OperationName.DB_PRODUCT_FEATURES_CREATE).
	                    withRequestParameters(inputParameterMap).
	                    build().getResponse();
	            responseJSON = CommonUtilities.getStringAsJSONObject(response);
	            //Add actions
	            JSONArray inputFeatureActions = featureRecord.getJSONArray("actions") ;
	            if(inputFeatureActions!=null && inputFeatureActions.length()>0) {
	            	for(int j=0;j<inputFeatureActions.length();j++) {
	            		 Map<String, Object> inputActionParameterMap = new HashMap<>();
	            		 inputActionParameterMap.put("actionsId", inputFeatureActions.getJSONObject(j).getString("actionId"));
	            		 inputActionParameterMap.put("productId", postParametersMap.get("productId").toString());
	            		 inputActionParameterMap.put("featureId", "FR"+featureId);
	            		 inputActionParameterMap.put("status", "Active");
	     	            String response1 = DBPServiceExecutorBuilder.builder().
	    	                    withServiceId(ServiceId.CRUDLAYER).
	    	                    withOperationId(OperationName.DB_PRODUCT_FEATURES_ACTIONS_CREATE).
	    	                    withRequestParameters(inputActionParameterMap).
	    	                    build().getResponse();
	     	           JSONObject  actionresponseJSON = CommonUtilities.getStringAsJSONObject(response1);
	            	}
	            }
			}
			

            return responseJSON;
		}
	}
	
	@Override
	public JSONObject createProductImages(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
	        headerMap.put("backendToken", backendToken);
			
			String serviceResponse =
	                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
	                        .withOperationId(OperationName.OP_CREATE_PRODUCT_IMAGES)
	                        .withRequestParameters(postParametersMap)
	                        .withRequestHeaders(headerMap)
	                        .build()
	                        .getResponse();

	        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}else {
		       
			JSONArray inputImageDetails = new JSONArray(postParametersMap.get("imageDetails").toString());
			 JSONObject responseJSON =new JSONObject() ;
			for(int i=0 ; i < inputImageDetails.length() ; i++ ) {
				JSONObject imgRecord =  inputImageDetails.getJSONObject(i);
				String imageType = imgRecord.getString("imageType");
				String height = imgRecord.getInt("height")+"";
				String width = imgRecord.getInt("width")+"";
				String imageUrl = imgRecord.getString("imageUrl");
				postParametersMap.put("imageType", imageType);
				postParametersMap.put("height", height);
				postParametersMap.put("width", width);
				postParametersMap.put("imageUrl", imageUrl);
				postParametersMap.put("imageId", "PI"+System.currentTimeMillis()+"");
				postParametersMap.put("productRef", postParametersMap.get("productRef").toString());
//				postParametersMap.remove("marketingCatalogBackend");
	            String response = DBPServiceExecutorBuilder.builder().
	                    withServiceId(ServiceId.CRUDLAYER).
	                    withOperationId(OperationName.DB_PRODUCT_IMAGES_CREATE).
	                    withRequestParameters(postParametersMap).
	                    build().getResponse();
	            responseJSON = CommonUtilities.getStringAsJSONObject(response);
			}
			

            return responseJSON;
		}

 
	}
	
	@Override
	public JSONObject getProduct(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
	        headerMap.put("backendToken", backendToken);
			
			String serviceResponse =
	                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
	                        .withOperationId(OperationName.OP_GET_PRODUCT)
	                        .withRequestParameters(postParametersMap)
	                        .withRequestHeaders(headerMap)
	                        .build()
	                        .getResponse();
			return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}else {
			postParametersMap.put("_productRef", postParametersMap.get("productRef").toString());
//			postParametersMap.remove("marketingCatalogBackend");
            String response = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_GET_PRODUCT_DETAILS).
                    withRequestParameters(postParametersMap).
                    build().getResponse();
            JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
            return responseJSON;
		}
		


        
	}
	
	@Override
	public JSONObject getProductList(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
		headerMap.put("backendToken", backendToken);
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_GET_PRODUCT_LIST)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		} else {   
			//String filter = (String) postParametersMap.get("branchRef");
			//postParametersMap.put("$filter", filter);

            String response = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_GET_PRODUCT_LIST).
                    withRequestParameters(postParametersMap).
                    build().getResponse();
            JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
            return responseJSON;
 		}	
	}
	
	@Override
	public JSONObject createProductFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
        if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
    		headerMap.put("backendToken", backendToken);
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_CREATE_PRODUCT_FACILITY)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
        } else {        	
    		try {   			
    	    String str2 = postParametersMap.get("productFacilities").toString();
    		JSONArray productFacilities = new JSONArray(str2);
    		String facilityName =productFacilities.getJSONObject(0).getString("facilityName");
    		String code = productFacilities.getJSONObject(0).getString("code");
    		String facilityId = productFacilities.getJSONObject(0).getString("facilityId");
    		String description = productFacilities.getJSONObject(0).getString("description");
    		Number sequenceNo = productFacilities.getJSONObject(0).getNumber("sequenceNo");
    		String defaultValue = productFacilities.getJSONObject(0).getString("defaultValue");
    		String optionDispType = productFacilities.getJSONObject(0).getString("optionDispType");
    		boolean isMandatory = productFacilities.getJSONObject(0).getBoolean("isMandatory");
    		String productFacilityId = RandomStringUtils.randomAlphanumeric(12).toUpperCase();
    		String productRef = postParametersMap.get("productRef").toString();
    		postParametersMap.put("sequenceNo", sequenceNo);
    		postParametersMap.put("defaultValue", defaultValue);
    		postParametersMap.put("optionDispType", optionDispType);
    		postParametersMap.put("isMandatory", isMandatory);
    		postParametersMap.put("description", description);
    		postParametersMap.put("facilityId", facilityId);
    		postParametersMap.put("code", code);
    		postParametersMap.put("facilityName", facilityName);
    		postParametersMap.put("productRef", productRef);
    		postParametersMap.put("productFacilityId", productFacilityId);
    		
    		//productFacilities = (JSONArray) postParametersMap.get("productFacilities");
    		} catch(Exception e) {
    			alert.prepareError("Unexpected Error in createProductFacility", e).log();
    		}	
        	postParametersMap.remove("productFacilities");
        	postParametersMap.remove("productId");
        	String serviceResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_CREATE_PRODUCT_FACILITY).
                    withRequestParameters(postParametersMap).
                    build().getResponse();

            return CommonUtilities.getStringAsJSONObject(serviceResponse);
        }
	}
	
	@Override
	public JSONObject updateProductFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();      
        if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
    		headerMap.put("backendToken", backendToken);

		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_UPDATE_PRODUCT_FACILITY)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
        } else {
        	try {   			
        	    String str2 = postParametersMap.get("productFacilities").toString();
        		JSONArray productFacilities = new JSONArray(str2);
        		String facilityName =productFacilities.getJSONObject(0).getString("facilityName");
        		String code = productFacilities.getJSONObject(0).getString("code");
        		String facilityId = productFacilities.getJSONObject(0).getString("facilityId");
        		String description = productFacilities.getJSONObject(0).getString("description");
        		Number sequenceNo = productFacilities.getJSONObject(0).getNumber("sequenceNo");
        		String defaultValue = productFacilities.getJSONObject(0).getString("defaultValue");
        		String optionDispType = productFacilities.getJSONObject(0).getString("optionDispType");
        		boolean isMandatory = productFacilities.getJSONObject(0).getBoolean("isMandatory");
        		String productFacilityId = productFacilities.getJSONObject(0).getString("productFacilityId");
        		String productRef = postParametersMap.get("productRef").toString();
        		postParametersMap.put("sequenceNo", sequenceNo);
        		postParametersMap.put("defaultValue", defaultValue);
        		postParametersMap.put("optionDispType", optionDispType);
        		postParametersMap.put("isMandatory", isMandatory);
        		postParametersMap.put("description", description);
        		postParametersMap.put("facilityId", facilityId);
        		postParametersMap.put("code", code);
        		postParametersMap.put("facilityName", facilityName);
        		postParametersMap.put("productRef", productRef);
        		postParametersMap.put("productFacilityId", productFacilityId);
        		
        		//productFacilities = (JSONArray) postParametersMap.get("productFacilities");
        		} catch(Exception e) {
        			alert.prepareError("Unexpected Error in createProductFacility", e).log();
        		}	
        	postParametersMap.remove("productFacilities");
        	postParametersMap.remove("productId");
        	String serviceResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_UPDATE_PRODUCT_FACILITY).
                    withRequestParameters(postParametersMap).
                    build().getResponse();

            return CommonUtilities.getStringAsJSONObject(serviceResponse);
        }
	}
	
	@Override
	public JSONObject deleteProductFacility(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
			headerMap.put("backendToken", backendToken);
			postParametersMap.remove("productFacilityId");
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_DELETE_PRODUCT_FACILITY)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		} else {

			postParametersMap.remove("productId");
			postParametersMap.remove("facilityId");
			String serviceResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_DELETE_PRODUCT_FACILITY).
                    withRequestParameters(postParametersMap).
                    build().getResponse();
	        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}
	}

	@Override
	public JSONObject updateProduct(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
			headerMap.put("backendToken", backendToken);
       
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_UPDATE_PRODUCT)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		} else {
			Map<String, Object> inputParametersMap = new HashMap<>();
			inputParametersMap.put("_description", postParametersMap.get("description"));
			inputParametersMap.put("_detailedDesc", postParametersMap.get("detailedDesc"));
			inputParametersMap.put("_notes", postParametersMap.get("notes"));
			inputParametersMap.put("_termsConditions", postParametersMap.get("termsConditions"));
			inputParametersMap.put("_disclosure", postParametersMap.get("disclosure"));
			inputParametersMap.put("_productRef", postParametersMap.get("productRef"));
			inputParametersMap.put("_productName", postParametersMap.get("productName"));
			inputParametersMap.put("_availableFrom", postParametersMap.get("availableFrom"));
			inputParametersMap.put("_availableTo", postParametersMap.get("availableTo"));
			inputParametersMap.put("_purposes", postParametersMap.get("purposes"));
			inputParametersMap.put("_addAttributes", postParametersMap.get("additionalAttributes"));
			
			
			String serviceResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_UPDATE_PRODUCT_PROC).
                    withRequestParameters(inputParametersMap).
                    build().getResponse();
	        return (JSONObject) CommonUtilities.getStringAsJSONObject(serviceResponse).getJSONArray("records").get(0);
		}
	}
	
	@Override
	public JSONObject getProductFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();		
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
	        headerMap.put("backendToken", backendToken);
	        
	    			String serviceResponse =
	    	                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
	    	                        .withOperationId(OperationName.OP_GET_PRODUCT_FEATURES)
	    	                        .withRequestParameters(postParametersMap)
	    	                        .withRequestHeaders(headerMap)
	    	                        .build()
	    	                        .getResponse();
	    			return CommonUtilities.getStringAsJSONObject(serviceResponse);    
		}else {
			postParametersMap.put("_productId",postParametersMap.get("productId"));
			String serviceResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_GET_PRODUCT_FEATURES).
                    withRequestParameters(postParametersMap).
                    build().getResponse();
	        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}

	}
	
	@Override
	public JSONObject editProductFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_UPDATE_PRODUCT_FEATURES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}
        else {
		       
			JSONArray inputFeatures = new JSONArray(postParametersMap.get("features").toString());
			 JSONObject responseJSON =new JSONObject() ;
			 Map<String, Object> inputParameterMap = new HashMap<>();
			for(int i=0 ; i < inputFeatures.length() ; i++ ) {
				JSONObject featureRecord =  inputFeatures.getJSONObject(i);
				String featureId = featureRecord.getString("featureId");
				inputParameterMap.put("featureCode", featureId);
				inputParameterMap.put("productId", postParametersMap.get("productId").toString());
				inputParameterMap.put("featureId", "FR"+featureId);
				inputParameterMap.put("status", "Active");
				inputParameterMap.put("productRef", postParametersMap.get("productRef").toString());
//				postParametersMap.remove("marketingCatalogBackend");
	            String response = DBPServiceExecutorBuilder.builder().
	                    withServiceId(ServiceId.CRUDLAYER).
	                    withOperationId(OperationName.DB_PRODUCT_FEATURES_EDIT).
	                    withRequestParameters(inputParameterMap).
	                    build().getResponse();
	            responseJSON = CommonUtilities.getStringAsJSONObject(response);
	            //Edit actions
	            JSONArray inputFeatureActions = featureRecord.getJSONArray("actions") ;
	            if(inputFeatureActions!=null && inputFeatureActions.length()>0) {
	            	for(int j=0;j<inputFeatureActions.length();j++) {
	            		 Map<String, Object> inputActionParameterMap = new HashMap<>();
	            		 inputActionParameterMap.put("actionsId", inputFeatureActions.getJSONObject(j).getString("actionId"));
	            		 inputActionParameterMap.put("productId", postParametersMap.get("productId").toString());
	            		 inputActionParameterMap.put("featureId", "FR"+featureId);
	            		 inputActionParameterMap.put("status", "Active");
	     	            String response1 = DBPServiceExecutorBuilder.builder().
	    	                    withServiceId(ServiceId.CRUDLAYER).
	    	                    withOperationId(OperationName.DB_PRODUCT_FEATURES_ACTIONS_EDIT).
	    	                    withRequestParameters(inputActionParameterMap).
	    	                    build().getResponse();
	     	           JSONObject  actionresponseJSON = CommonUtilities.getStringAsJSONObject(response1);
	            	}
	            }
			}
			

            return responseJSON;
		}
	}
	
	@Override
	public JSONObject deleteProductFeatures(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_DELETE_PRODUCT_FEATURES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
        
		}
        else {
		       
			JSONArray productFeatures = new JSONArray(postParametersMap.get("deleteFeatures").toString());

			 JSONObject responseJSON =new JSONObject() ;
			 Map<String, Object> inputParams = new HashMap<String, Object>();
			 inputParams.put("_productId", postParametersMap.get("productId").toString());

			for(int i=0 ; i < productFeatures.length() ; i++ ) {
				
				String featureId = productFeatures.get(i).toString();
				 inputParams.put("_featureId", featureId);
	            String response = DBPServiceExecutorBuilder.builder().
	                    withServiceId(ServiceId.CRUDLAYER).
	                    withOperationId(OperationName.DB_DELETE_PRODUCT_FEATURES).
	                    withRequestParameters(inputParams).
	                    build().getResponse();
	            responseJSON = CommonUtilities.getStringAsJSONObject(response);
	            
			}
			return responseJSON;
		}
	}
	
	@Override
	public JSONObject getProductImages(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
	        headerMap.put("backendToken", backendToken);
			
			String serviceResponse =
	                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
	                        .withOperationId(OperationName.OP_GET_PRODUCT_IMAGES)
	                        .withRequestParameters(postParametersMap)
	                        .withRequestHeaders(headerMap)
	                        .build()
	                        .getResponse();

	        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}else {
			
			String productRef = (String) postParametersMap.get("productRef");
			postParametersMap.put("$filter", "productRef eq "+productRef);
			
            String response = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_GET_PRODUCT_IMAGES_GET).
                    withRequestParameters(postParametersMap).
                    build().getResponse();
            JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
            responseJSON.put("imageDetails",responseJSON.getJSONArray("productImage"));
            return responseJSON;
		}
		

	}
	
	@Override
	public JSONObject updateProductImages(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_UPDATE_PRODUCT_IMAGES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}else {
		       
			JSONArray inputImageDetails = new JSONArray(postParametersMap.get("imageDetails").toString());
			 JSONObject responseJSON =new JSONObject() ;
			for(int i=0 ; i < inputImageDetails.length() ; i++ ) {
				JSONObject imgRecord =  inputImageDetails.getJSONObject(i);
				String imageType = imgRecord.getString("imageType");
				String height = imgRecord.getInt("height")+"";
				String width = imgRecord.getInt("width")+"";
				String imageUrl = imgRecord.getString("imageUrl");
				postParametersMap.put("imageType", imageType);
				postParametersMap.put("height", height);
				postParametersMap.put("width", width);
				postParametersMap.put("imageUrl", imageUrl);
				postParametersMap.put("imageId", "PI"+System.currentTimeMillis()+"");
				postParametersMap.put("productRef", postParametersMap.get("productRef").toString());
//				postParametersMap.remove("marketingCatalogBackend");
	            String response = DBPServiceExecutorBuilder.builder().
	                    withServiceId(ServiceId.CRUDLAYER).
	                    withOperationId(OperationName.DB_PRODUCT_IMAGES_EDIT).
	                    withRequestParameters(postParametersMap).
	                    build().getResponse();
	            responseJSON = CommonUtilities.getStringAsJSONObject(response);
			}
			

         return responseJSON;
		}
	}
	
	@Override
	public JSONObject deleteProductImages(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {
		
		Map<String, Object> headerMap = new HashMap<>();
		if(postParametersMap.get("marketingCatalogBackend").equals("MS")) {
        headerMap.put("backendToken", backendToken);
		
		String serviceResponse =
                DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.PRODUCTMS)
                        .withOperationId(OperationName.OP_DELETE_PRODUCT_IMAGES)
                        .withRequestParameters(postParametersMap)
                        .withRequestHeaders(headerMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
		}else {
		       
			JSONArray inputImageDetails = new JSONArray(postParametersMap.get("imageDetails").toString());
			 JSONObject responseJSON =new JSONObject() ;
			 Map<String, Object> inputParams = new HashMap<String, Object>();
			 inputParams.put("productRef", postParametersMap.get("productRef").toString());
			 inputParams.put("imageType", postParametersMap.get("imageType").toString());
			for(int i=0 ; i < inputImageDetails.length() ; i++ ) {
				JSONObject imgRecord =  inputImageDetails.getJSONObject(i);
				String imageType = imgRecord.getString("imageType");
	            String response = DBPServiceExecutorBuilder.builder().
	                    withServiceId(ServiceId.CRUDLAYER).
	                    withOperationId(OperationName.DB_PRODUCT_IMAGES_DELETE).
	                    withRequestParameters(inputParams).
	                    build().getResponse();
	            responseJSON = CommonUtilities.getStringAsJSONObject(response);
			}
			

      return responseJSON;
		}
	}
	
	@Override
	public JSONObject getAccountLevelFeatureDetails(Map<String, Object> postParametersMap, String backendToken)
			throws DBPApplicationException {

		
        String objectId = "feature";
		String serviceResponse =
                DBPServiceExecutorBuilder.builder()
                		.withServiceId(ServiceId.FEATURE_OBJ_SERVICE)
                		.withObjectId(objectId)
                        .withOperationId(OperationName.OP_GET_ACC_FEATURE_DETAILS)
                        .withRequestParameters(postParametersMap)
                        .build()
                        .getResponse();

        return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}


	public JSONObject getProductLines() throws DBPApplicationException {

		Map<String, Object> postParametersMap = null;

		String serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.T24_PRODUCT_SERVICES)
				.withOperationId(OperationName.GETPRODUCTLINELIST).withRequestParameters(postParametersMap).build()
				.getResponse();

		return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	public JSONObject getProductGroups() throws DBPApplicationException {

		Map<String, Object> postParametersMap = null;

		String serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.T24_PRODUCT_SERVICES)
				.withOperationId(OperationName.GETPRODUCTGROUPLIST).withRequestParameters(postParametersMap).build()
				.getResponse();

		return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}

	public JSONObject getProducts() throws DBPApplicationException {

		Map<String, Object> postParametersMap = null;

		String serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.T24_PRODUCT_SERVICES)
				.withOperationId(OperationName.GETPRODUCTLIST).withRequestParameters(postParametersMap).build()
				.getResponse();
		
		

		return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	public String createProductLines(JSONArray ProductLines) throws DBPApplicationException {


		 Map<String, Object> postParametersMap = new HashMap<>();;
		   
		  String response = null;
		  JSONObject responseJSON = null;
		for(int i=0;i<ProductLines.length();i++)
		  {
			postParametersMap.put("productLineId","PL"+System.currentTimeMillis()+i);
			postParametersMap.put("productLineRef",ProductLines.getJSONObject(i).opt("productLine") );
			postParametersMap.put("productLineName",ProductLines.getJSONObject(i).opt("displayName") );	
			postParametersMap.put("externalIndicator","1" );
			
		
           response = DBPServiceExecutorBuilder.builder().
                  withServiceId(ServiceId.CRUDLAYER).
                  withOperationId(OperationName.DB_CREATE_PRODUCT_LINES).
                  withRequestParameters(postParametersMap).
                  build().getResponse();
           responseJSON = CommonUtilities.getStringAsJSONObject(response);
		  }
		if(responseJSON.opt(ACConstants.HTTP_STATUS_CODE).equals(0))
     		return ACConstants.SUCCESS;
	else
		    return responseJSON.opt("errmsg").toString();

	}
	public String createProductGroups(JSONArray ProductGroups) throws DBPApplicationException {

		   Map<String, Object> postParametersMap = new HashMap<>();;		   
		  String response = null;
		  JSONObject responseJSON = null;
		for(int i=0;i<ProductGroups.length();i++)
		  {
			
			postParametersMap.put("productGroupId","PG"+System.currentTimeMillis() +i);
			postParametersMap.put("productGroupRef",ProductGroups.getJSONObject(i).opt("productGroupId") );
			postParametersMap.put("productLineRef",ProductGroups.getJSONObject(i).opt("productLineId") );
			postParametersMap.put("productGroupName",ProductGroups.getJSONObject(i).opt("displayName") );
			postParametersMap.put("branchRef","GB0010001");		
         response = DBPServiceExecutorBuilder.builder().
                withServiceId(ServiceId.CRUDLAYER).
                withOperationId(OperationName.DB_CREATE_PRODUCT_GROUPS).
                withRequestParameters(postParametersMap).
                build().getResponse();
         responseJSON   = CommonUtilities.getStringAsJSONObject(response);
		  }
			if(responseJSON.opt(ACConstants.HTTP_STATUS_CODE).equals(0))
	     		return ACConstants.SUCCESS;
		else
			    return responseJSON.opt("errmsg").toString();
       			}
	public String createProducts(JSONArray Products) throws DBPApplicationException {

		  Map<String, Object> postParametersMap = new HashMap<>();;
		   
		  String response = null;
		 
		  StringBuilder query = new StringBuilder("");
		  List<String> crs = new ArrayList<String>();
		for(int i=0;i<Products.length();i++)
		  {
		
			
			query = new StringBuilder("");
		       
		        query.append("\"" + "PI"+System.currentTimeMillis() +i+ "\",");
		        query.append("\"" + Products.getJSONObject(i).opt("productId")+ "\",");
		        query.append("\"" + "GB0010001" + "\",");
		        query.append("\"" + Products.getJSONObject(i).opt("productName")+ "\",");
		        query.append("\"" + Products.getJSONObject(i).opt("productLineId")+ "\",");
		        query.append("\"" + Products.getJSONObject(i).opt("productGroupId")+ "\",");
		        query.append("\"" + "Active" + "\",");
		        if(StringUtils.isBlank(Products.getJSONObject(i).optString("availableFromDate"))) {
		        	query.append("null,");
		        }
		        else {
		        	query.append("\"" + Products.getJSONObject(i).opt("availableFromDate")+ "\",");
		        }
		        if(StringUtils.isBlank(Products.getJSONObject(i).optString("availableToDate"))) {
		        	query.append("null");
		        }
		        else {
			        query.append("\"" + Products.getJSONObject(i).opt("availableToDate")+ "\"");
		        }
		
		        crs.add(query.toString());
		  }
		   

        StringBuilder input = new StringBuilder("");
        int queries = crs.size();
        if (queries > 0) {
            for (int query1 = 0; query1 < queries; query1++) {
                String temp = crs.get(query1);
                
                
                if (query1 < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }
        }
       
        	
        	
        	Map<String, Object> inputParams = new HashMap<String, Object>();

            inputParams.put("_queryInput", input.toString());

            response = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_CREATE_PRODUCTS_PROC).
                    withRequestParameters(inputParams).
                    build().getResponse();
            JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
             	
            
            if(responseJSON.opt(ACConstants.HTTP_STATUS_CODE).equals(0))
         		return ACConstants.SUCCESS;
		else
			    return responseJSON.opt("errmsg").toString();
	      
		
	}

	@Override
	public String clearMarketingData() throws DBPApplicationException {
		  Map<String, Object> postParametersMap = new HashMap<>();
		   
		  String response = null;
		  JSONObject responseJSON = null;
		


	
		
         response = DBPServiceExecutorBuilder.builder().
                withServiceId(ServiceId.CRUDLAYER).
                withOperationId(OperationName.DB_DELETE_MARKETINGDATA_PROC).
                withRequestParameters(postParametersMap).
                build().getResponse();
         responseJSON   = CommonUtilities.getStringAsJSONObject(response);
		 
			if(responseJSON.opt(ACConstants.HTTP_STATUS_CODE).equals(0))
	     		return ACConstants.SUCCESS;
		else
			    return responseJSON.opt("errmsg").toString();
	}

	@Override
	public JSONObject getProductConditions() throws DBPApplicationException {
		
		 String[] productTypes = {
		            "INFINITY.ADAMANTIUM.CARD",
		            "INFINITY.VIBRANIUM.CARD",
		            "INFINITY.BESKAR.CARD",
		            "OVERDRAFT.ACCOUNT",
		            "OVERDRAFT.ACCOUNT.SME",
		            "DEPOSIT.LONG",
		            "DEPOSIT.SHORT",
		            "SAVINGS.PLAN",
		            "ADVANCED.CHECKING.ACCOUNT",
		            "BASIC.CHECKING.ACCOUNT",
		            "PREFERRED.CHECKING.ACCOUNT",
		            "CURRENT.ACCOUNT.PREF",
		            "CURRENT.ACCOUNT",
		            "CURRENT.ACCOUNT.SME",
		            "CURRENT.ACCOUNT.DEFAULT",
		            "SAVINGS.PRIME.INFINITY",
		            "SAVINGS.SALARY.INFINITY",
		            "SAVINGS.STANDARD.INFINITY",
		            "SAVINGS.ACCOUNT",
		            "SAVINGS.ACCOUNT.WELCOME",
		            "PERSONAL.LOAN",
		            "BB.SMALL.BUSINESS.LOAN",
		            "BB.PREMIUM.ACCOUNT",
		            "BB.STANDARD.ACCOUNT",
		            "BB.START.UP.ACCOUNT",
		            "MORTGAGE.OFFER",
		            "BRIDGE.LOAN"
		        };
		JSONObject jso = new  JSONObject();
		JSONArray productFacilities = null;
		JSONArray productFacilitiesArr = new JSONArray();
		JSONArray Products = null;
		 for (String productType : productTypes) {
			 Map<String, Object> postParametersMap = new HashMap<>();
			 
			 postParametersMap.put("productRef", productType);
				String serviceResponse = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.T24_PRODUCT_SERVICES)
						.withOperationId(OperationName.GETPRODUCTFACILITIESLIST).withRequestParameters(postParametersMap).build()
						.getResponse();

		JSONObject serviceResponseJSON = CommonUtilities.getStringAsJSONObject(serviceResponse);
		if(serviceResponseJSON.has("Products"))
		{
			Products = serviceResponseJSON.getJSONArray("Products");
			if(Products.length()>0)
			{
				productFacilities=Products.getJSONObject(0).getJSONArray("productFacilities");
				if(productFacilities.length()>0)			
				{
					productFacilitiesArr.putAll(Products);
				}
			
			}
			
		}
				
	        }
	    
			jso.put("productFacilities", productFacilitiesArr);
		

	return jso;
	}

	@Override
	public String createProductfacilities(JSONArray productFacilitiesObj) throws DBPApplicationException {
		  Map<String, Object> postParametersMap = new HashMap<>();;
		   
		  String response = null;
		 
		  StringBuilder query = new StringBuilder("");
		  List<String> crs = new ArrayList<String>();
		 // JSONArray productFacilities = productFacilitiesObj.getJSONArray("productFacilities");
		for(int i=0;i<productFacilitiesObj.length();i++)
		  {
			JSONArray productFacilitiesArr=productFacilitiesObj.getJSONObject(i).optJSONArray("productFacilities");
			for(int j=0;j<productFacilitiesArr.length();j++)
			{
			query = new StringBuilder("");
		       UUID randomUUID = UUID.randomUUID();
					String id = randomUUID.toString();
					id=id.replace("-","").toUpperCase();
				query.append("\"" + "FI"+System.currentTimeMillis() +j+ "\",");
		        query.append("\"" + productFacilitiesArr.getJSONObject(j).opt("service")+ "\",");
		        query.append("\"" + productFacilitiesArr.getJSONObject(j).opt("service")+ "\",");
		        query.append("\"" + productFacilitiesArr.getJSONObject(j).opt("service")+ "\",");
		        query.append("\"" + "0" + "\",");
		        query.append("\"" + productFacilitiesObj.getJSONObject(i).opt("productId") + "\",");
		        query.append("\"" + "1"+ "\",");
		        query.append("\"" + "true"+ "\",");
		        query.append("\"" +  "PF"+id+"\"");
		
		        crs.add(query.toString());
			}
		  }
		   

        StringBuilder input = new StringBuilder("");
        int queries = crs.size();
        if (queries > 0) {
            for (int query1 = 0; query1 < queries; query1++) {
                String temp = crs.get(query1);
                
                
                if (query1 < queries - 1)
                    input.append(temp + "|");
                else
                    input.append(temp);
            }
        }
       
        	
        	
        	Map<String, Object> inputParams = new HashMap<String, Object>();

            inputParams.put("_queryInput", input.toString());

            response = DBPServiceExecutorBuilder.builder().
                    withServiceId(ServiceId.CRUDLAYER).
                    withOperationId(OperationName.DB_CREATE_PRODUCTFACILITIES_PROC).
                    withRequestParameters(inputParams).
                    build().getResponse();
            JSONObject responseJSON = CommonUtilities.getStringAsJSONObject(response);
             	
            
            if(responseJSON.opt(ACConstants.HTTP_STATUS_CODE).equals(0))
         		return ACConstants.SUCCESS;
		else
			    return responseJSON.opt("errmsg").toString();
	      
		
		

	}

	@Override
	public JSONObject getAllProductGroupsCampaign(Map<String, Object> postParametersMap,  Map<String, Object>  headerMap)
			throws DBPApplicationException {
		JSONObject responseObject = new JSONObject();
		try {
		String marketingCatalogBackend = (EnvironmentConfigurationsHandler.getServerAppProperty("MARKETING_CATALOG_BACKEND"));
		if (StringUtils.isNotBlank(marketingCatalogBackend) && "MS".equalsIgnoreCase(marketingCatalogBackend)) {
			String token = "";
			if (headerMap.get("x-kony-authorization") != null)
				token = headerMap.get("x-kony-authorization").toString();
			
			return getAllProductGroupsMS(postParametersMap, token);
			
		} else {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.GETALLPRODUCTGROUPS_CAMPAIGN_PROC)
					.withRequestParameters(postParametersMap).build().getResponse();

			return CommonUtilities.getStringAsJSONObject(response);
		}
		} catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to fetch product from MS: " + exp).log();
			
		}
		return responseObject;
		
	}

	@Override
	public JSONObject getProductsByProductGroup(Map<String, Object> postParametersMap,Map<String, Object>  headerMap)
			throws DBPApplicationException {
		JSONObject responseObject = new JSONObject();
		try {
		String marketingCatalogBackend = (EnvironmentConfigurationsHandler.getServerAppProperty("MARKETING_CATALOG_BACKEND"));
		if (StringUtils.isNotBlank(marketingCatalogBackend) && "MS".equalsIgnoreCase(marketingCatalogBackend)) {
			String token = "";
			if (headerMap.get("x-kony-authorization") != null)
				token = headerMap.get("x-kony-authorization").toString();
			
			return getProductsByProductGroupMS(postParametersMap, token);
			
		} else {
			String response = DBPServiceExecutorBuilder.builder().withServiceId(ServiceId.CRUDLAYER)
					.withOperationId(OperationName.GETPRODUCTSBYPRODUCTGROUP_PROC)
					.withRequestParameters(postParametersMap).build().getResponse();

			return CommonUtilities.getStringAsJSONObject(response);
		}
		} catch (Exception exp) {
			alert.prepareError("Encountered exception while trying to fetch product from MS: " + exp).log();
			
		}
		return responseObject;
		
		
		

	}
	
	private JSONObject getAllProductGroupsMS(Map<String, Object> postParametersMap, String token ) throws Exception
	{
			String serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.PRODUCT_MANAGEMENT_MS, null, OperationName.GET_PRODUCTGROUPS_CAMPAIGN, postParametersMap, null, token);
					return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
	
	private JSONObject getProductsByProductGroupMS(Map<String, Object> postParametersMap, String token) throws Exception
	{
			String serviceResponse = DBPServiceInvocationWrapper.invokeServiceAndGetJSON(ServiceId.PRODUCT_MANAGEMENT_MS, null, OperationName.GET_PRODUCTS_BY_PRODUCTGROUP, postParametersMap, null, token);
					return CommonUtilities.getStringAsJSONObject(serviceResponse);
	}
}
