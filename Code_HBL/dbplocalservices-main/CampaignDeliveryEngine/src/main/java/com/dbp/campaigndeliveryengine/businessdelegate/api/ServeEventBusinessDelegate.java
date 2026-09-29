package com.dbp.campaigndeliveryengine.businessdelegate.api;

import com.dbp.campaigndeliveryengine.dtoclasses.CommunicationNodeDTO;
import com.dbp.campaigndeliveryengine.dtoclasses.CustomerInfoDTO;
import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonElement;

public interface ServeEventBusinessDelegate extends BusinessDelegate {

	public void serveEvent(JsonElement event, String customerid, String corecustomerid, CommunicationNodeDTO commdata,
			CustomerInfoDTO customer);

}
