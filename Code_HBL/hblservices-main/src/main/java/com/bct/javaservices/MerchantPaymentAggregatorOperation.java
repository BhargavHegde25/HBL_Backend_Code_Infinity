package com.bct.javaservices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.resource.api.PaymentAggregatorResource;
import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class MerchantPaymentAggregatorOperation implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(MerchantPaymentAggregatorOperation.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		 LOG.debug("BCT::MerchantPaymentAggregatorOperation::");
       try {
       	PaymentAggregatorResource paymentAggrtorResource = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(ResourceFactory.class).getResource(PaymentAggregatorResource.class);
			result = paymentAggrtorResource.paymentAggregatorCRUDOperation(methodId, inputArray, request, response);
       } catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
			LOG.error("Exception occured in MerchantPaymentAggregatorOperation  :" + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error("Exception occured in MerchantPaymentAggregatorOperation :" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
		}
			return result;
	}
	}

