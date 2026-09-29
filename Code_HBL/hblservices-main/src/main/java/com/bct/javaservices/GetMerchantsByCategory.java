package com.bct.javaservices;

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

public class GetMerchantsByCategory implements JavaService2{
	 private static final Logger LOG = LogManager.getLogger(GetMerchantsByCategory.class);
		@Override
		public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
				DataControllerResponse response) throws Exception {
			 Result result = new Result();
			 LOG.debug("BCT::GetMerchantsByCategory::");
	         try {
	         	GetAllMerchantOperationsResource merchantCategoriesResource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(GetAllMerchantOperationsResource.class);
				result = merchantCategoriesResource.getMerchantsByCategory(methodId, inputArray, dcRequest, response);
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
