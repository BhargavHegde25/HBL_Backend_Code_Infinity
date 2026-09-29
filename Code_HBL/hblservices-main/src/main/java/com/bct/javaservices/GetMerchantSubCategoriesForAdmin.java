package com.bct.javaservices;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.resource.api.MerchantSubCategoriesResource;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetMerchantSubCategoriesForAdmin implements JavaService2 {
	 private static final Logger LOG = LogManager.getLogger(GetMerchantSubCategoriesForAdmin.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		 Result result = new Result();
			try {
	         	MerchantSubCategoriesResource merchantSubCategoriesResource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(MerchantSubCategoriesResource.class);
	         	HashMap<String, String> map = new HashMap<String, String>();
	         	map.put("category", "CATEGORY");
	         	map.put("subCategory", "ALL");
	         	inputArray[1]=map;
				result = merchantSubCategoriesResource.getMerchantSubCategoriesForAdmin(methodId, inputArray, request, response);
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

