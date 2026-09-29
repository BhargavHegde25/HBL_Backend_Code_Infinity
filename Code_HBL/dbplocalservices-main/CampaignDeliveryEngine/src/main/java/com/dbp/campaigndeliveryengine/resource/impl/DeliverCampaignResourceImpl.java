package com.dbp.campaigndeliveryengine.resource.impl;

import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.campaigndeliveryengine.businessdelegate.api.DeliveryCampaignBusinessDelegate;
import com.dbp.campaigndeliveryengine.dtoclasses.KMSConfigDTO;
import com.dbp.campaigndeliveryengine.resource.api.DeliverCampaignResource;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineConstants;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineUtils;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.registry.AppRegistryException;

public class DeliverCampaignResourceImpl implements DeliverCampaignResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result deliverCampaign(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {
		Result res = new Result();
		DeliveryCampaignBusinessDelegate deliveryCampaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(DeliveryCampaignBusinessDelegate.class);
		String events = null;
		Map<String, String> inputparams = null;
		if (inputArray != null && inputArray.length > 1) {
			inputparams = (Map<String, String>) inputArray[1];
		}
		if (inputparams != null) {
			events = inputparams.get("events");
		}
		if (events == null) {
			res.addParam(new Param(CampaignDeliveryEngineConstants.SUCCESS, CampaignDeliveryEngineConstants.FALSE,
					CampaignDeliveryEngineConstants.STRING));
			res.addParam(new Param(CampaignDeliveryEngineConstants.DBPERRMSG,
					"Invalid events data or events data is null.", CampaignDeliveryEngineConstants.STRING));
		}
		KMSConfigDTO kmsconf = getKmsEnvVariables(request);
		JsonObject result = deliveryCampaignBusinessDelegate.deliverCampaign(events, kmsconf);
		res.addParam(new Param(CampaignDeliveryEngineConstants.SUCCESS,
				result.get(CampaignDeliveryEngineConstants.SUCCESS).getAsString(),
				CampaignDeliveryEngineConstants.STRING));

		if (result.has(CampaignDeliveryEngineConstants.DBPERRMSG))
			res.addParam(new Param(CampaignDeliveryEngineConstants.DBPERRMSG,
					result.get(CampaignDeliveryEngineConstants.DBPERRMSG).getAsString(),
					CampaignDeliveryEngineConstants.STRING));
		res.addParam(
				new Param(CampaignDeliveryEngineConstants.SUCCESS, "true", CampaignDeliveryEngineConstants.STRING));
		return res;
	}

	private KMSConfigDTO getKmsEnvVariables(DataControllerRequest dcreq) {
		KMSConfigDTO kmsconf = new KMSConfigDTO();
		try {
			kmsconf.setEmailurl(CampaignDeliveryEngineUtils.getConfigProperty(CampaignDeliveryEngineConstants.DBX_KMS_EMAIL));
			kmsconf.setSmsurl(CampaignDeliveryEngineUtils.getConfigProperty(CampaignDeliveryEngineConstants.DBX_KMS_SMS));
			kmsconf.setPushurl(CampaignDeliveryEngineUtils.getConfigProperty(CampaignDeliveryEngineConstants.DBX_KMS_PUSH));
			kmsconf.setKmsapikey(CampaignDeliveryEngineUtils.getConfigProperty(CampaignDeliveryEngineConstants.DBX_KMS_API_KEY));
			kmsconf.setAppkey(CampaignDeliveryEngineUtils.getConfigProperty(CampaignDeliveryEngineConstants.DBX_KMS_APPKEY));
		} catch (Exception e) {
			alert.prepareError("Error occured in fetching env variables", e).log();
		}

		return kmsconf;
	}
}
