package com.bct.javaservices;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.resource.api.GetAllMerchantOperationsResource;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetMerchantCategoriesForAdmin implements JavaService2{
	 private static final Logger LOG = LogManager.getLogger(GetMerchantCategoriesForAdmin.class);
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		 Result result = new Result();
		 LOG.debug("HBL::GetMerchantCategoriesForAdmin"); 
		try {
         	GetAllMerchantOperationsResource merchantCategoriesResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(GetAllMerchantOperationsResource.class);
         	HashMap<String, String> map = new HashMap<String, String>();
         	map.put("category", "ALL");
         	inputArray[1]=map;
			result = merchantCategoriesResource.getMerchantSubCategoriesForAdmin(methodId, inputArray, request, response);
         } catch (ApplicationException e) {
 			e.getErrorCodeEnum().setErrorCode(result);
 			LOG.error("Exception occured while fetching the merchant categories  :" + e.getMessage(), e);
 		} catch (Exception e) {
 			LOG.error("Exception occured while fetching the merchant categories  :" + e.getMessage(), e);
 			ErrorCodeEnum.ERR_10021.setErrorCode(result);
 		}
			return result;
	}

}
