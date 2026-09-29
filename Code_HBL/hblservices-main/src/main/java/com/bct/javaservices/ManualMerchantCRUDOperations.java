package com.bct.javaservices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.resource.api.BranchDetailsResource;
import com.bct.custom.resource.api.ManualMerchantsResource;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ManualMerchantCRUDOperations implements JavaService2{
	 private static final Logger LOG = LogManager.getLogger(ManualMerchantCRUDOperations.class);
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		 LOG.debug("HBL:ManualMerchantCRUDOperations::");
      try {
      	ManualMerchantsResource resource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(ManualMerchantsResource.class);
			result = resource.manualMerchantOperations(methodId, inputArray, request);
      } catch (ApplicationException e) {
			LOG.error("Exception occured while fetching the merchant categories details  :" + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception occured while fetching the merchant categories :" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
		}
			return result;
	}

}
