package com.kony.adminconsole.service.productmanagement.resource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.kony.adminconsole.commons.handler.EnvironmentConfigurationsHandler;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.preprocessor.ProductsAuthorizationPreProcessor;
import com.kony.adminconsole.service.productmanagement.businessdelegate.api.ProductBusinessDelegate;
import com.kony.adminconsole.service.productmanagement.preprocessor.MCMSTokenGeneration;
import com.kony.adminconsole.service.productmanagement.resource.api.ProductResource;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.OperationName;
import com.kony.adminconsole.utilities.ServiceId;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ProductResourceImpl implements ProductResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	private static final String INPUT_PRODUCT_LINE = "productLine";
	private static final String INPUT_PRODUCT_GROUP = "productGroup";
	private static final String INPUT_PRODUCT_REF = "productRef";
	
	private static final String INPUT_PRODUCT_GROUPS = "productGroups";
	private static final String INPUT_PRODUCT_NAME = "productName";
	private static final String INPUT_AVAILABLE_FROM = "availableFrom";
	private static final String INPUT_AVAILABLE_TO  = "availableTo";
	private static final String INPUT_EXTENSION_DATA = "extensionData";
	private static final String INPUT_PURPOSES = "purposes";
	private static final String INPUT_FEATURES = "features";
	private static final String INPUT_PRODUCT_FACILITIES = "productFacilities";
	private static final String INPUT_DESC = "description";
	private static final String INPUT_DETAILED_DESC = "detailedDesc";
	private static final String INPUT_NOTES = "notes";
	private static final String INPUT_TNC = "termsConditions";
	private static final String INPUT_DISCLOSURE = "disclosure";
	private static final String INPUT_IMAGE_DETAILS = "imageDetails";
	public static final String PARAM_AUTHORIZATION = "Authorization";
	private static final String PARAM_BRANCHREF = "branchRef";
	private static final String INPUT_PRODUCT_ID = "productId";
	private static final String INPUT_FACILITY_ID = "facilityId";
	private static final String INPUT_EXTERNAL_INDICATOR = "externalIndicator";
	private static final String INPUT_PRODUCTFACILITY_ID = "productFacilityId";
	
	
	ProductBusinessDelegate productBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
		    .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(ProductBusinessDelegate.class);
	
	ProductsAuthorizationPreProcessor authObj = new ProductsAuthorizationPreProcessor();
	//MCMSTokenGeneration authObj = new MCMSTokenGeneration();
	
	
	@Override
	public Result createProduct(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		String productLine = StringUtils.EMPTY;
		String productGroup = StringUtils.EMPTY; 
		String productRef = StringUtils.EMPTY;
		String productName = StringUtils.EMPTY;
		String availableFrom = StringUtils.EMPTY;
		String availableTo = StringUtils.EMPTY;
		String purposes = StringUtils.EMPTY;
		String features = StringUtils.EMPTY;
		String productFacilities = StringUtils.EMPTY;
		String description = StringUtils.EMPTY;
		String detailedDesc = StringUtils.EMPTY;
		String notes = StringUtils.EMPTY;
		String termsConditions = StringUtils.EMPTY;
		String disclosure = StringUtils.EMPTY;
		String imageDetails = StringUtils.EMPTY;
		String extensionData = "{}";
		String externalIndicator = "false";
		HashMap<String,String> map = new HashMap<>();
		try{
			if(!CommonUtilities.validateCompleteRequestInputJson(requestInstance)) {
				ErrorCodeEnum.ERR_20541.setErrorCode(result);
				return result;
			}
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_LINE))) {
	            
				ErrorCodeEnum.ERR_22138.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_GROUP))) {
	            
				ErrorCodeEnum.ERR_22139.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_REF))) {
	            
				ErrorCodeEnum.ERR_22140.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_NAME))) {
	            
				ErrorCodeEnum.ERR_22141.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_AVAILABLE_FROM))) {
	            
				ErrorCodeEnum.ERR_22142.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_AVAILABLE_TO))) {
	            
				ErrorCodeEnum.ERR_22143.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PURPOSES))) {
	            
				ErrorCodeEnum.ERR_22144.setErrorCode(result);
				return  result;
	        } else {
	        	try {
	        		new JSONArray(requestInstance.getParameter(INPUT_PURPOSES));
	        	} catch(JSONException exp) {
	        		alert.prepareError("Invalid input param purposes in request: "+exp).log();
	        		ErrorCodeEnum.ERR_22145.setErrorCode(result);
					return  result;
	        	}
	        }
			
			productLine = requestInstance.getParameter(INPUT_PRODUCT_LINE);
			productGroup = requestInstance.getParameter(INPUT_PRODUCT_GROUP); 
			productRef = requestInstance.getParameter(INPUT_PRODUCT_REF);
			productName = requestInstance.getParameter(INPUT_PRODUCT_NAME);
			productName= StringEscapeUtils.escapeHtml(productName);
			availableFrom = requestInstance.getParameter(INPUT_AVAILABLE_FROM);
			availableTo = requestInstance.getParameter(INPUT_AVAILABLE_TO);
			purposes = requestInstance.getParameter(INPUT_PURPOSES);
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_FEATURES))) {
				features = requestInstance.getParameter(INPUT_FEATURES);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_PRODUCT_FACILITIES))) {
				productFacilities =requestInstance.getParameter(INPUT_PRODUCT_FACILITIES);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DESC))) {
				description = requestInstance.getParameter(INPUT_DESC);
				description= StringEscapeUtils.escapeHtml(description);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DETAILED_DESC))) {
				detailedDesc = requestInstance.getParameter(INPUT_DETAILED_DESC);
				detailedDesc = StringEscapeUtils.escapeHtml(detailedDesc);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_NOTES))) {
				notes = requestInstance.getParameter(INPUT_NOTES);
				notes = StringEscapeUtils.escapeHtml(notes);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_TNC))) {
				termsConditions = requestInstance.getParameter(INPUT_TNC);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DISCLOSURE))) {
				disclosure = requestInstance.getParameter(INPUT_DISCLOSURE);
				disclosure = StringEscapeUtils.escapeHtml(disclosure);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_IMAGE_DETAILS))) {
				imageDetails = requestInstance.getParameter(INPUT_IMAGE_DETAILS);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_EXTENSION_DATA))) {
				extensionData = requestInstance.getParameter(INPUT_EXTENSION_DATA);							
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_EXTERNAL_INDICATOR))) {
				externalIndicator = requestInstance.getParameter(INPUT_EXTERNAL_INDICATOR);
			}

			JSONObject extensionData_obj = new JSONObject(extensionData);
			JSONArray additionalAttributes = extensionData_obj.getJSONArray("additionalAttributes");
			
			for(int i=0;i<additionalAttributes.length();i++)
			{
				additionalAttributes.getJSONObject(i).put("key",StringEscapeUtils.escapeHtml(additionalAttributes.getJSONObject(i).get("key").toString()));
				additionalAttributes.getJSONObject(i).put("value",StringEscapeUtils.escapeHtml(additionalAttributes.getJSONObject(i).get("value").toString()));
			}
			extensionData_obj.put("additionalAttributes", additionalAttributes);
			extensionData = extensionData_obj.toString();
			
			
			JSONArray imgDetails = new JSONArray(imageDetails);
			for(int i=0;i<imgDetails.length();i++)
			{
				imgDetails.getJSONObject(i).put("imageUrl",StringEscapeUtils.escapeHtml(imgDetails.getJSONObject(i).get("imageUrl").toString()));
			}
			imageDetails = imgDetails.toString();
			alert.prepareError("Checking1" + imageDetails + productName + description + detailedDesc + notes + disclosure + extensionData ).log();
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			String branchRef = requestInstance.getParameter(PARAM_BRANCHREF);
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put("branchRef", branchRef);
	    	postParametersMap.put("productLine", productLine);
	    	postParametersMap.put("productGroup", productGroup);
	    	postParametersMap.put("productRef", productRef);
	    	postParametersMap.put("productName", productName);
	    	postParametersMap.put("availableFrom", availableFrom);
	    	postParametersMap.put("availableTo", availableTo);
	    	postParametersMap.put("purposes", purposes);
	    	postParametersMap.put("features", features);
	    	postParametersMap.put("productFacilities", productFacilities);
	    	postParametersMap.put("description", description);
	    	postParametersMap.put("detailedDesc", detailedDesc);
	    	postParametersMap.put("notes", notes);
	    	postParametersMap.put("termsConditions", termsConditions);
	    	postParametersMap.put("disclosure", disclosure);
	    	postParametersMap.put("imageDetails", imageDetails);
	    	postParametersMap.put("extensionData", extensionData);
	    	postParametersMap.put("externalIndicator",externalIndicator);
			
	    	JSONObject serviceResponse =
	        		productBusinessDelegate.createProduct(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22135.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Create Product failed response: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create Product with Product Name: "+ productName);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create Product with Product Name: "+ productName + 
                        					", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Product Creation successful, Product Name: "+ productName);
            }
	        
		} catch(Exception e) {
			alert.prepareError("Unexpected Error in createProduct", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22135.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
                    ActivityStatusEnum.FAILED, "Failed to create Product with Product Name: "+ productName);
		}
		
		return result;
	}

	@Override
	public Result updateProduct(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		
		String productLine = StringUtils.EMPTY;
		String productGroup = StringUtils.EMPTY; 
		String productRef = StringUtils.EMPTY;
		String productName = StringUtils.EMPTY;
		String availableFrom = StringUtils.EMPTY;
		String availableTo = StringUtils.EMPTY;
		String purposes = StringUtils.EMPTY;
		String features = StringUtils.EMPTY;
		String description = StringUtils.EMPTY;
		String detailedDesc = StringUtils.EMPTY;
		String notes = StringUtils.EMPTY;
		String termsConditions = StringUtils.EMPTY;
		String disclosure = StringUtils.EMPTY;
		String imageDetails = StringUtils.EMPTY;
		String extensionData = "{}";
		String externalIndicator = "false";
		HashMap<String,String> map = new HashMap<>();
		
		try{
			if(!CommonUtilities.validateCompleteRequestInputJson(requestInstance)) {
				ErrorCodeEnum.ERR_20541.setErrorCode(result);
				return result;
			}
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_LINE))) {
	            
				ErrorCodeEnum.ERR_22138.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_GROUP))) {
	            
				ErrorCodeEnum.ERR_22139.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_REF))) {
	            
				ErrorCodeEnum.ERR_22140.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_NAME))) {
	            
				ErrorCodeEnum.ERR_22141.setErrorCode(result);
				return  result;
	        }
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_AVAILABLE_FROM))) {
	            
				ErrorCodeEnum.ERR_22142.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_AVAILABLE_TO))) {
	            
				ErrorCodeEnum.ERR_22143.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PURPOSES))) {
	            
				ErrorCodeEnum.ERR_22144.setErrorCode(result);
				return  result;
	        } else {
	        	try {
	        		new JSONArray(requestInstance.getParameter(INPUT_PURPOSES));
	        	} catch(JSONException exp) {
	        		alert.prepareError("Invalid input param purposes in request: "+exp).log();
	        		ErrorCodeEnum.ERR_22145.setErrorCode(result);
					return  result;
	        	}
	        }
			
			productLine = requestInstance.getParameter(INPUT_PRODUCT_LINE);
			productGroup = requestInstance.getParameter(INPUT_PRODUCT_GROUP); 
			productRef = requestInstance.getParameter(INPUT_PRODUCT_REF);
			productName = requestInstance.getParameter(INPUT_PRODUCT_NAME);
			productName= StringEscapeUtils.escapeHtml(productName);
			availableFrom = requestInstance.getParameter(INPUT_AVAILABLE_FROM);
			availableTo = requestInstance.getParameter(INPUT_AVAILABLE_TO);
			purposes = requestInstance.getParameter(INPUT_PURPOSES);
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_FEATURES))) {
				features = requestInstance.getParameter(INPUT_FEATURES);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DESC))) {
				description = requestInstance.getParameter(INPUT_DESC);
				description= StringEscapeUtils.escapeHtml(description);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DETAILED_DESC))) {
				detailedDesc = requestInstance.getParameter(INPUT_DETAILED_DESC);
				detailedDesc = StringEscapeUtils.escapeHtml(detailedDesc);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_NOTES))) {
				notes = requestInstance.getParameter(INPUT_NOTES);
				notes = StringEscapeUtils.escapeHtml(notes);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_TNC))) {
				termsConditions = requestInstance.getParameter(INPUT_TNC);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_DISCLOSURE))) {
				disclosure = requestInstance.getParameter(INPUT_DISCLOSURE);
				disclosure = StringEscapeUtils.escapeHtml(disclosure);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_IMAGE_DETAILS))) {
				imageDetails = requestInstance.getParameter(INPUT_IMAGE_DETAILS);				
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_EXTENSION_DATA))) {
				extensionData = requestInstance.getParameter(INPUT_EXTENSION_DATA);
			}
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_EXTERNAL_INDICATOR))) {
				externalIndicator = requestInstance.getParameter(INPUT_EXTERNAL_INDICATOR);
			}
			
			JSONObject extensionData_obj = new JSONObject(extensionData);
			JSONArray additionalAttributes = extensionData_obj.getJSONArray("additionalAttributes");
			StringBuffer additionalAttributesBuffer= new StringBuffer();;
			for(int i=0;i<additionalAttributes.length();i++)
			{
				additionalAttributes.getJSONObject(i).put("key",StringEscapeUtils.escapeHtml(additionalAttributes.getJSONObject(i).get("key").toString()));
				additionalAttributes.getJSONObject(i).put("value",StringEscapeUtils.escapeHtml(additionalAttributes.getJSONObject(i).get("value").toString()));
				
				additionalAttributesBuffer.append(StringEscapeUtils.escapeHtml(additionalAttributes.getJSONObject(i).get("key").toString()));
				additionalAttributesBuffer.append("#$");
				additionalAttributesBuffer.append(StringEscapeUtils.escapeHtml(additionalAttributes.getJSONObject(i).get("value").toString()));
				if(i<additionalAttributes.length()-1) {
					additionalAttributesBuffer.append("|^");
				}
				
			}
			extensionData_obj.put("additionalAttributes",additionalAttributes);
			extensionData = extensionData_obj.toString();
			
			
			JSONArray imgDetails = new JSONArray(imageDetails);
			for(int i=0;i<imgDetails.length();i++)
			{
				imgDetails.getJSONObject(i).put("imageUrl",StringEscapeUtils.escapeHtml(imgDetails.getJSONObject(i).get("imageUrl").toString()));
			}
			imageDetails = imgDetails.toString();
			alert.prepareError("Checking" + imageDetails + productName + description + detailedDesc + notes + disclosure + extensionData ).log();
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			String branchRef = requestInstance.getParameter(PARAM_BRANCHREF);
			
			Map<String, Object> postParametersMap = new HashMap<>();
			String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",requestInstance);
	    	postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);
	    	if(!marketingCatalogBackend.equals("MS")) {
	    		String Type = "";
	    		JSONArray purpose = new JSONArray(purposes);
	    		if(purpose.length()>1) {
				for(int i=0;i<purpose.length();i++)
				{	
					Type = Type + purpose.getJSONObject(i).get("type") +"," ;	
				}
				purposes = Type.substring(0, Type.length() - 1);
	    	} else {
	    		 for(int i=0;i<purpose.length();i++)
					{	
	    				Type = Type + purpose.getJSONObject(i).get("type");	
					}
	    		 purposes = Type;
	    		}	
	    		
	    		
	    	}
	    	postParametersMap.put("branchRef", branchRef);
	    	postParametersMap.put("productLine", productLine);
	    	postParametersMap.put("productGroup", productGroup);
	    	postParametersMap.put("productRef", productRef);
	    	postParametersMap.put("productName", productName);
	    	postParametersMap.put("availableFrom", availableFrom);
	    	postParametersMap.put("availableTo", availableTo);
	    	postParametersMap.put("purposes", purposes);
	    	postParametersMap.put("features", features);
	    	postParametersMap.put("description", description);
	    	postParametersMap.put("detailedDesc", detailedDesc);
	    	postParametersMap.put("notes", notes);
	    	postParametersMap.put("termsConditions", termsConditions);
	    	postParametersMap.put("disclosure", disclosure);
	    	postParametersMap.put("imageDetails", imageDetails);
	    	postParametersMap.put("extensionData", extensionData);
	    	postParametersMap.put("externalIndicator",externalIndicator);
	    	postParametersMap.put("additionalAttributes",additionalAttributesBuffer.toString());
	    	
	    	JSONObject serviceResponse =
	        		productBusinessDelegate.updateProduct(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22136.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Update Product failed response: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update Product, Product Name: "+ productName);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to Update Product, Product Name: "+ productName + 
                        					", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Product Update successful, Product Name: "+ productName);
            }
	        
	    	
		} catch(Exception e) {
			alert.prepareError("Unexpected Error in updateProduct", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22136.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
                    ActivityStatusEnum.FAILED, "Failed to update Product, Product Name: "+ productName);
		}
		
		return result;
	}

	@Override
	public Result getProducts(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String productRef = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		try{
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_PRODUCT_REF))) {
				productRef = requestInstance.getParameter(INPUT_PRODUCT_REF);
			}
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
			//boolean isAuthSuccess = true;
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			String branchRef = requestInstance.getParameter(PARAM_BRANCHREF);
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(PARAM_BRANCHREF, branchRef);
	    	postParametersMap.put(INPUT_PRODUCT_REF, productRef);
	    	
	    	String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",requestInstance);
	    	postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);
	    	
	    	JSONObject serviceResponse =
	        		productBusinessDelegate.getProducts(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22137.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch Products: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch Products,ProductRef : "+ productRef);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch Products,ProductRef : "+ productRef+
                        							", Reason:"+ serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully fetched product details for ProductRef : "+ productRef);
            }
		} catch(Exception e) {
			alert.prepareError("Unexpected Error in getProducts", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22137.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Product, ProductRef : "+ productRef);
		}
		
		return result;
		
	}

	@Override
	public Result createProductFacility(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String productFacilities = StringUtils.EMPTY;
		String productId = StringUtils.EMPTY;
		String productRef = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		try{
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_ID))) {
				ErrorCodeEnum.ERR_22149.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_FACILITIES))) {
				ErrorCodeEnum.ERR_22150.setErrorCode(result);
				return  result;
	        }
			productId = requestInstance.getParameter(INPUT_PRODUCT_ID);
			productFacilities = requestInstance.getParameter(INPUT_PRODUCT_FACILITIES);
			productRef = requestInstance.getParameter("productRef");
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(INPUT_PRODUCT_ID, productId);
	    	postParametersMap.put(INPUT_PRODUCT_FACILITIES, productFacilities);
	    	postParametersMap.put("productRef", productRef);
	    	String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",requestInstance);
	    	postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);
			
			JSONObject serviceResponse =
	        		productBusinessDelegate.createProductFacility(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22146.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to Create Product Facility: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
                        ActivityStatusEnum.FAILED, "Failed to create ProductFacilit for ProductId : "+ productId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
    	                ActivityStatusEnum.FAILED, "Failed to Create ProductFacility for ProductId : "+ productId+
    	                							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	if(!marketingCatalogBackend.equals("MS")) {
                	result.addParam(new Param("status", "Success", FabricConstants.STRING));
                	result.addParam(new Param("message", "ProductFacilities created successfully", FabricConstants.STRING));
                	}
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully created productFacility for ProductId : "+ productId);
            }
		} catch(Exception e) {
			alert.prepareError("Unexpected Error in createProductFacility", e).log();
	        result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
	        ErrorCodeEnum.ERR_22146.setErrorCode(result);
	        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.CREATE,
	                ActivityStatusEnum.FAILED, "Failed to Create ProductFacility for ProductId : "+ productId);
		}
		
		return result;
	}

	@Override
	public Result updateProductFacility(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String productFacilities = StringUtils.EMPTY;
		String productId = StringUtils.EMPTY;
		String productRef = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		try{
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_ID))) {
				ErrorCodeEnum.ERR_22149.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_FACILITIES))) {
				ErrorCodeEnum.ERR_22150.setErrorCode(result);
				return  result;
	        }
			productId = requestInstance.getParameter(INPUT_PRODUCT_ID);
			productFacilities = requestInstance.getParameter(INPUT_PRODUCT_FACILITIES);
			productRef = requestInstance.getParameter("productRef");
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(INPUT_PRODUCT_ID, productId);
	    	postParametersMap.put(INPUT_PRODUCT_FACILITIES, productFacilities);
	    	postParametersMap.put("productRef", productRef);
	    	String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",requestInstance);
	    	postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);
			
			JSONObject serviceResponse =
	        		productBusinessDelegate.updateProductFacility(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22147.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to update Product Facility: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
                        ActivityStatusEnum.FAILED, "Failed to update ProductFacility  for ProductId : "+ productId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	 AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
     	                ActivityStatusEnum.FAILED, "Failed to Update ProductFacility for ProductId : "+ productId+
     	                							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	if(!marketingCatalogBackend.equals("MS")) {
                	result.addParam(new Param("status", "Success", FabricConstants.STRING));
                	result.addParam(new Param("message", "ProductFacilities updated successfully", FabricConstants.STRING));
                	}
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully updated productFacility for ProductId : "+ productId);
            }
		
		} catch(Exception e) {
			alert.prepareError("Unexpected Error in updateProductFacility", e).log();
	        result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
	        ErrorCodeEnum.ERR_22147.setErrorCode(result);
	        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.UPDATE,
	                ActivityStatusEnum.FAILED, "Failed to Update ProductFacility for ProductId : "+ productId);
		}
		
		return result;
	}

	@Override
	public Result deleteProductFacility(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		
		Result result = new Result();
		String facilityId = StringUtils.EMPTY;
		String productId = StringUtils.EMPTY;
		String productFacilityId = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		try{
			
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_PRODUCT_ID))) {
				ErrorCodeEnum.ERR_22149.setErrorCode(result);
				return  result;
	        }
			if (StringUtils.isBlank(requestInstance.getParameter(INPUT_FACILITY_ID))) {
				ErrorCodeEnum.ERR_22134.setErrorCode(result);
				return  result;
	        }
			productId = requestInstance.getParameter(INPUT_PRODUCT_ID);
			facilityId = requestInstance.getParameter(INPUT_FACILITY_ID);
			productFacilityId = requestInstance.getParameter(INPUT_PRODUCTFACILITY_ID);
			
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
	    	
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			
			Map<String, Object> postParametersMap = new HashMap<>();
	    	postParametersMap.put(INPUT_PRODUCT_ID, productId);
	    	postParametersMap.put(INPUT_FACILITY_ID, facilityId);
	    	postParametersMap.put(INPUT_PRODUCTFACILITY_ID, productFacilityId);
	    	
	    	String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",requestInstance);
	    	postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);
			
			JSONObject serviceResponse =
	        		productBusinessDelegate.deleteProductFacility(postParametersMap, backendToken);
	        if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22148.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to delete Product Facility: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.DELETE,
                        ActivityStatusEnum.FAILED, "Failed to delete facility :"+facilityId+" from ProductId : "+ productId);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.DELETE,
    	                ActivityStatusEnum.FAILED, "Failed to delete Facility:"+facilityId+" from ProductId : "+ productId+
    	                							", Reason: "+serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	if(!marketingCatalogBackend.equals("MS")) {
            	result.addParam(new Param("status", "Success", FabricConstants.STRING));
            	result.addParam(new Param("message", "ProductFacilities deleted successfully", FabricConstants.STRING));
            	}
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.DELETE,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully deleted facility:"+facilityId+" from ProductId : "+ productId);
            }
		
		} catch(Exception e) {
			alert.prepareError("Unexpected Error in deleteProductFacility", e).log();
	        result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
	        ErrorCodeEnum.ERR_22148.setErrorCode(result);
	        AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.DELETE,
	                ActivityStatusEnum.FAILED, "Failed to delete Facility:"+facilityId+" from ProductId : "+ productId);
		}
		
		return result;
	}
	public Result loadProductInformation(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) throws DBPApplicationException {
		Result result = new Result();
		String serviceResponse0 =
        		productBusinessDelegate.clearMarketingData();
		
		String serviceResponse =
        		productBusinessDelegate.loadProductLines();
	
		String serviceResponse1 =
        		productBusinessDelegate.loadProductGroups();
		String serviceResponse2 =
        		productBusinessDelegate.loadProducts();
		String serviceResponse3 =
        		productBusinessDelegate.loadProductFacilities();
		List<String> ls = new ArrayList<String>();
		ls.add(serviceResponse0);
		ls.add(serviceResponse);
		ls.add(serviceResponse1);
		ls.add(serviceResponse2);
		ls.add(serviceResponse3);
			return processResponse(ls);
		
	}
	

	
	public Result processResponse(List<String> resultStr)
	{
		Result result = new Result();
		for(String res:resultStr)
		{
			if(!res.equals(ACConstants.SUCCESS))
					{
				result.addParam("status", ACConstants.FAILED);
				result.addParam("errMsg",res);
				return result;
					}
			
		}
		
		result.addParam("status", ACConstants.SUCCESS);
		return result;
	
		
	}

	@Override
	public Result getAllProductGroupsCampaign(String methodID, Object[] inputArray,
			DataControllerRequest requestInstance, DataControllerResponse response) {
		Result result = new Result();
		HashMap<String, String> map = new HashMap<>();
		try {

			boolean isAuthSuccess = authObj.execute(map, requestInstance, response, result);
			// boolean isAuthSuccess = true;
			if (!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			Map<String, Object> postParametersMap = new HashMap<>();
			String marketingCatalogBackend = EnvironmentConfigurationsHandler
					.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND", requestInstance);
			postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);

			JSONObject serviceResponse = productBusinessDelegate.getAllProductGroupsCampaign(postParametersMap,
					requestInstance.getHeaderMap());
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
					|| serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
				ErrorCodeEnum.ERR_22247.setErrorCode(result);
				result.addParam(new Param("status", "Failed", FabricConstants.STRING));
				alert.prepareError("Failed to fetch Products: " + serviceResponse).log();
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Failed to fetch getAllProductGroups ");
				return result;
			} else if (serviceResponse.has("dbpErrMsg")) {
				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
						ActivityStatusEnum.FAILED, "Failed to fetch  getAllProductGroups "
								+ serviceResponse.has("dbpErrMsg"));
				return result;
			} else {

				result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
						ActivityStatusEnum.SUCCESSFUL,
						"Successfully fetched product details for getAllProductGroups ");
			}
		} catch (Exception e) {
			alert.prepareError("Unexpected Error in getAllProductGroups ", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22247.setErrorCode(result);
			AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
					ActivityStatusEnum.FAILED, "Failed to fetch getAllProductGroups ");
		}

		return result;
	}

	

	@Override
	public Result getProductsByProductGroup(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse response) {
		Result result = new Result();
		String productgroupRef = StringUtils.EMPTY;
		HashMap<String,String> map = new HashMap<>();
		try{
			
			if (StringUtils.isNotBlank(requestInstance.getParameter(INPUT_PRODUCT_GROUPS))) {
				productgroupRef = requestInstance.getParameter(INPUT_PRODUCT_GROUPS);
			}
			boolean isAuthSuccess = authObj.execute(map, requestInstance,response,result);
			//boolean isAuthSuccess = true;
			if(!isAuthSuccess) {
				ErrorCodeEnum.ERR_22129.setErrorCode(result);
				return result;
			}
			
			String backendToken = requestInstance.getParameter(PARAM_AUTHORIZATION);
			Map<String, Object> postParametersMap = new HashMap<>();
			String branchRef = requestInstance.getParameter(PARAM_BRANCHREF);
	    	String marketingCatalogBackend = EnvironmentConfigurationsHandler.getServerAppPropertyValue("MARKETING_CATALOG_BACKEND",requestInstance);
	    	postParametersMap.put("marketingCatalogBackend", marketingCatalogBackend);
	    	postParametersMap.put(PARAM_BRANCHREF, branchRef);
	    	postParametersMap.put(INPUT_PRODUCT_GROUPS, productgroupRef);
			JSONObject serviceResponse = productBusinessDelegate.getProductsByProductGroup(postParametersMap,
					requestInstance.getHeaderMap());
			if (serviceResponse == null || !serviceResponse.has(FabricConstants.OPSTATUS)
                    || serviceResponse.getInt(FabricConstants.OPSTATUS) != 0) {
                ErrorCodeEnum.ERR_22248.setErrorCode(result);
                result.addParam(new Param("status", "Failed", FabricConstants.STRING));
                alert.prepareError("Failed to fetch getProductsByProductGroup: "+serviceResponse).log();
                AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch getProductsByProductGroup,ProductRef : "+ productgroupRef);
                return result;
            } else if (serviceResponse.has("dbpErrMsg")) {
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                        ActivityStatusEnum.FAILED, "Failed to fetch getProductsByProductGroup,ProductRef : "+ productgroupRef+
                        							", Reason:"+ serviceResponse.has("dbpErrMsg"));
                return result;
            } else {
            	
            	result = CommonUtilities.constructResultFromJSONObject(serviceResponse);
            	AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                        ActivityStatusEnum.SUCCESSFUL, "Successfully fetched getProductsByProductGroup details for ProductRef : "+ productgroupRef);
            }
		} catch(Exception e) {
			alert.prepareError("Unexpected Error in getProducts", e).log();
            result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
            ErrorCodeEnum.ERR_22248.setErrorCode(result);
            AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.PRODUCTS, EventEnum.SEARCH,
                    ActivityStatusEnum.FAILED, "Failed to fetch Product, ProductRef : "+ productgroupRef);
		}
		
		return result;
	}
}
