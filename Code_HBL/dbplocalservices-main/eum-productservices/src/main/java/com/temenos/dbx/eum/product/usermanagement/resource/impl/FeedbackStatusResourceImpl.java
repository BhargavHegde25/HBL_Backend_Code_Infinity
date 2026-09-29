package com.temenos.dbx.eum.product.usermanagement.resource.impl;

import java.util.Map;
import java.util.UUID;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.FeedBackStatusBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.FeedBackStatusResource;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.dto.FeedBackStatusDTO;

public class FeedbackStatusResourceImpl implements FeedBackStatusResource {
	private static LoggerUtil logger;


	@Override
	public Object getStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		logger = new LoggerUtil(CustomerCommunicationResourceImpl.class);
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String deviceId = inputParams.get("deviceId");
		String customerId = inputParams.get("Customer_id");

		FeedBackStatusDTO backStatusDTO = new FeedBackStatusDTO();
		backStatusDTO.setCustomerID(customerId);
		backStatusDTO.setDeviceID(deviceId);

		FeedBackStatusBusinessDelegate backBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(FeedBackStatusBusinessDelegate.class);

		DBXResult dbxResult = backBusinessDelegate.get(backStatusDTO, dcRequest.getHeaderMap());

		String id = "";

		if(dbxResult.getResponse() != null) {
			backStatusDTO = (FeedBackStatusDTO) dbxResult.getResponse();
			id = backStatusDTO.getId();
			backStatusDTO.setChanged(true);

		}
		else
		{
			id = UUID.randomUUID().toString();
			backStatusDTO.setNew(true);
		}
		String createdts = HelperMethods.getCurrentTimeStamp();

		backStatusDTO.setCustomerID(customerId);
		backStatusDTO.setFeedbackID(UUID.randomUUID().toString());
		backStatusDTO.setCreatedts(createdts);
		backStatusDTO.setStatus("false");
		backStatusDTO.setDeviceID(deviceId);
		backStatusDTO.setId(id);

		backBusinessDelegate.update(backStatusDTO, dcRequest.getHeaderMap());

		result.addParam(new Param("feedbackUserId", backStatusDTO.getFeedbackID(), "String"));

		return result;
	}


	@Override
	public Result updateStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object createStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		// TODO Auto-generated method stub
		return null;
	}


}
