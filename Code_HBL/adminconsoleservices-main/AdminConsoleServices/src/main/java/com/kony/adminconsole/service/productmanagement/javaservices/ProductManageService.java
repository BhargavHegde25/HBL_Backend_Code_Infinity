package com.kony.adminconsole.service.productmanagement.javaservices;


import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.service.productmanagement.resource.api.ProductResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ProductManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
		
		private static final String CREATE_PRODUCT = "createProduct";
		private static final String UPDATE_PRODUCT  = "updateProduct";
		private static final String GET_PRODUCTS = "getProducts";
		private static final String CREATE_PRODUCT_FACILITY = "createProductFacility";
		private static final String UPDATE_PRODUCT_FACILITY = "updateProductFacility";
		private static final String DELETE_PRODUCT_FACILITY = "deleteProductFacility";
		private static final String GET_ALL_PRODUCTGROUPS_CAMPAIGN ="GetAllProductGroupsCampaign";
		private static final String GET_PRODUCTS_BY_PRODUCTGROUP  = "GetProductsByProductGroup";
		
				
		@Override
		public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
				DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
			
			try {

	            if (methodID.equalsIgnoreCase(CREATE_PRODUCT)) {
	                return createProduct(methodID, inputArray, requestInstance, responseInstance);
	            } else if (methodID.equalsIgnoreCase(UPDATE_PRODUCT)) {
	                return updateProduct(methodID, inputArray, requestInstance, responseInstance);
	            } else if (methodID.equalsIgnoreCase(GET_PRODUCTS)) {
	                return getProducts(methodID, inputArray, requestInstance, responseInstance);
	            } else if (methodID.equalsIgnoreCase(CREATE_PRODUCT_FACILITY)) {
	                return createProductFacility(methodID, inputArray, requestInstance, responseInstance);
	            } else if (methodID.equalsIgnoreCase(UPDATE_PRODUCT_FACILITY)) {
	                return updateProductFacility(methodID, inputArray, requestInstance, responseInstance);
	            } else if (methodID.equalsIgnoreCase(DELETE_PRODUCT_FACILITY)) {
	                return deleteProductFacility(methodID, inputArray, requestInstance, responseInstance);
	            }
	            else if (methodID.equalsIgnoreCase(GET_ALL_PRODUCTGROUPS_CAMPAIGN)) {
	                return getAllProductGroupsCampaign(methodID, inputArray, requestInstance, responseInstance);
	            }
	            else if (methodID.equalsIgnoreCase(GET_PRODUCTS_BY_PRODUCTGROUP)) {
	                return getProductsByProductGroup(methodID, inputArray, requestInstance, responseInstance);
	            }
	            
	            return new Result();
	            
	        } catch (Exception e) {
	            Result errorResult = new Result();
	            alert.prepareError("Runtime Exception.Exception Trace:", e).log();
	            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
	            return errorResult;
	        }
		}
		
		private Object createProduct(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.createProduct(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of createProduct: ", e).log();
	            return ErrorCodeEnum.ERR_22135.setErrorCode(new Result());
	        }
	        return result;
	    }
		
		private Object updateProduct(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.updateProduct(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of updateProduct: ", e).log();
	            return ErrorCodeEnum.ERR_22136.setErrorCode(new Result());
	        }
	        return result;
	    }
		
		private Object getProducts(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.getProducts(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of getProducts: ", e).log();
	            return ErrorCodeEnum.ERR_22137.setErrorCode(new Result());
	        }
	        return result;
	    }
		
		private Object getAllProductGroupsCampaign(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.getAllProductGroupsCampaign(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of getProducts: ", e).log();
	            return ErrorCodeEnum.ERR_22247.setErrorCode(new Result());
	        }
	        return result;
	    }
		private Object getProductsByProductGroup(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.getProductsByProductGroup(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of getProducts: ", e).log();
	            return ErrorCodeEnum.ERR_22248.setErrorCode(new Result());
	        }
	        return result;
	    }
		
		private Object createProductFacility(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.createProductFacility(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of createProductFacility: ", e).log();
	            return ErrorCodeEnum.ERR_22146.setErrorCode(new Result());
	        }
	        return result;
	    }
		
		private Object updateProductFacility(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.updateProductFacility(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of updateProductFacility: ", e).log();
	            return ErrorCodeEnum.ERR_22147.setErrorCode(new Result());
	        }
	        return result;
	    }
		
		private Object deleteProductFacility(String methodID, Object[] inputArray,
	            DataControllerRequest requestInstance, DataControllerResponse responseInstance) {
	        
	        Result result = null;
	        try {
	        	ProductResource productResource = DBPAPIAbstractFactoryImpl.getInstance()
	                    .getFactoryInstance(ResourceFactory.class).getResource(ProductResource.class);
	            result = productResource.deleteProductFacility(methodID, inputArray, requestInstance,
	                    responseInstance);
	        } catch (Exception e) {
	            alert.prepareError("Caught exception at invoke of deleteProductFacility: ", e).log();
	            return ErrorCodeEnum.ERR_22147.setErrorCode(new Result());
	        }
	        return result;
	    }

	}
