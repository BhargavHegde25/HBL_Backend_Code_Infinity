package com.kony.campaign;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.kony.campaign.businessdelegate.api.CampaignBusinessDelegate;
import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.dto.EventDTO;
import com.konylabs.middleware.api.events.EventData;
import com.konylabs.middleware.api.events.EventSubscriber;
import com.konylabs.middleware.api.events.IntegrationEventSubscriber;

@IntegrationEventSubscriber(topics = { "/events/campaignengineexternalevent" })
public class ExternalEventSubscriber implements EventSubscriber{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	
	@Override
	public void onEvent(EventData eventData) {
		try {
		   if(diagnostic.isDebugEnabled()){			
			 diagnostic.prepareDebug("ExternalEventSubscriber invoked by subscribing in OnEvent").log();
			  diagnostic.prepareDebug("eventData.getData()" + eventData.getData()).log();
			}
		    CampaignBusinessDelegate campaignBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			            .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(CampaignBusinessDelegate.class);
			  EventDTO event = new EventDTO();
			  JsonObject eventsJsonObj =  new JsonParser().parse(eventData.getData().toString()).getAsJsonObject();
			  eventsJsonObj = eventsJsonObj.get(CampaignConstants.PARAM_EVENTS).getAsJsonObject();
			  event.setEventId(eventsJsonObj.get(CampaignConstants.EXTERNAL_EVENT_CODE).getAsString());
			  JsonObject eventDataObj = eventsJsonObj.get(CampaignConstants.PARAM_EVENT_DATA).getAsJsonObject();
			  if(eventDataObj.has(CampaignConstants.REQ_CUSTOMER_DATA)){
				  JsonObject customerDataObj = eventDataObj.get(CampaignConstants.REQ_CUSTOMER_DATA).getAsJsonObject();
				  if(customerDataObj.has(CampaignConstants.REQ_CUSTOMER_ID)) {
				    event.setCoreCustId(customerDataObj.get(CampaignConstants.REQ_CUSTOMER_ID).getAsString());
				  }
			  }
			if(event.getCoreCustId() != null ) {
			  event.setEventData(eventDataObj);
			  campaignBusinessDelegate.getExternalCampaigns(event);
			}else {
				alert.prepareError("corecustomerID is manadatory").log();
			}
		} catch (JsonSyntaxException e) {
			alert.prepareError("Error while parsing event input " ,e).log();		
		}
		catch (Exception e) {
			alert.prepareError("Error while sending event" , e).log();
		}
	}

}
