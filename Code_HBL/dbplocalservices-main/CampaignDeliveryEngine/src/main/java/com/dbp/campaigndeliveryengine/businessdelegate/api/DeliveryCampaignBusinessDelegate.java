package com.dbp.campaigndeliveryengine.businessdelegate.api;

import com.dbp.campaigndeliveryengine.dtoclasses.KMSConfigDTO;
import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;

public interface DeliveryCampaignBusinessDelegate extends BusinessDelegate {

	JsonObject deliverCampaign(String eventspayload, KMSConfigDTO kmsconf);

}
