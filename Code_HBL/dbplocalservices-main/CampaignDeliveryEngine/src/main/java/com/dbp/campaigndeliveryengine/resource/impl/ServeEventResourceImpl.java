package com.dbp.campaigndeliveryengine.resource.impl;

import java.util.Map;

import com.dbp.campaigndeliveryengine.businessdelegate.api.DeliveryCampaignBusinessDelegate;
import com.dbp.campaigndeliveryengine.businessdelegate.api.ServeEventBusinessDelegate;
import com.dbp.campaigndeliveryengine.dtoclasses.CommunicationNodeDTO;
import com.dbp.campaigndeliveryengine.dtoclasses.CustomerInfoDTO;
import com.dbp.campaigndeliveryengine.resource.api.ServeEventResource;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineConstants;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineUtils;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonElement;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ServeEventResourceImpl implements ServeEventResource {

	@Override
	public Result serveEvent(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		String eventjsonstring = null;
		String customerId = null;
		String fname = null;
		String lname = null;
		String email = null;
		String mobile = null;
		String corecustomerid = null;
		JsonElement eventjson = null;
		Map<String, String> inputParams = null;
		if (inputArray != null && inputArray.length > 1) {
			inputParams = (Map<String, String>) inputArray[1];
		}

		if (inputParams == null)
			return CampaignDeliveryEngineUtils.returnResult(false, "Input is null");

		eventjsonstring = inputParams.get(CampaignDeliveryEngineConstants.SERVICE_EVENTJSON);
		customerId = inputParams.get(CampaignDeliveryEngineConstants.SERVICE_CUSTOMERID);
		corecustomerid = inputParams.get(CampaignDeliveryEngineConstants.SERVICE_CORECUSTOMERID);
		email = inputParams.get(CampaignDeliveryEngineConstants.SERVICE_EMAIL);
		mobile = inputParams.get(CampaignDeliveryEngineConstants.SERVICE_MOBILE);
		fname = inputParams.get(CampaignDeliveryEngineConstants.SERVICE_FNAME);
		lname = inputParams.get(CampaignDeliveryEngineConstants.SERVICE_LNAME);

		if (eventjsonstring == null || customerId == null)
			return CampaignDeliveryEngineUtils.returnResult(false, "Invalid input");
		if ((eventjson = CampaignDeliveryEngineUtils.parseString(eventjsonstring)) == null) {
			return CampaignDeliveryEngineUtils.returnResult(false, "Json parse error");
		}
		if (corecustomerid == null || corecustomerid.equals(CampaignDeliveryEngineConstants.EMPTY_STRING)
				|| corecustomerid.equals(CampaignDeliveryEngineConstants.NULL_STRING))
			corecustomerid = null;
		if (email == null || email.equals(CampaignDeliveryEngineConstants.EMPTY_STRING)
				|| email.equals(CampaignDeliveryEngineConstants.NULL_STRING))
			email = null;
		if (mobile == null || mobile.equals(CampaignDeliveryEngineConstants.EMPTY_STRING)
				|| mobile.equals(CampaignDeliveryEngineConstants.NULL_STRING))
			mobile = null;
		if (fname == null || fname.equals(CampaignDeliveryEngineConstants.EMPTY_STRING)
				|| fname.equals(CampaignDeliveryEngineConstants.NULL_STRING))
			fname = null;
		if (lname == null || lname.equals(CampaignDeliveryEngineConstants.EMPTY_STRING)
				|| lname.equals(CampaignDeliveryEngineConstants.NULL_STRING))
			lname = null;
		ServeEventBusinessDelegate serveEventBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(ServeEventBusinessDelegate.class);
		serveEventBusinessDelegate.serveEvent(eventjson, customerId, corecustomerid,
				new CommunicationNodeDTO(mobile, email), new CustomerInfoDTO(fname, lname));
		return CampaignDeliveryEngineUtils.returnResult(true, CampaignDeliveryEngineConstants.EMPTY_STRING);
	}
}
