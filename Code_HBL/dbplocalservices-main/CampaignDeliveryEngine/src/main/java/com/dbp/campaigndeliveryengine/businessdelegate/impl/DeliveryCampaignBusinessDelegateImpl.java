package com.dbp.campaigndeliveryengine.businessdelegate.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.campaigndeliveryengine.businessdelegate.api.DeliveryCampaignBusinessDelegate;
import com.dbp.campaigndeliveryengine.dtoclasses.*;
import com.dbp.campaigndeliveryengine.utils.*;
import com.dbp.campaigndeliveryengine.utils.CampaignDeliveryEngineConstants;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.*;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DeliveryCampaignBusinessDelegateImpl implements DeliveryCampaignBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public JsonObject deliverCampaign(String eventspayload, KMSConfigDTO kmsconf) {
		JsonObject result = new JsonObject();
		JsonElement eventselement = new JsonParser().parse(eventspayload);
		if (!eventselement.isJsonArray()) {
			result.addProperty(CampaignDeliveryEngineConstants.SUCCESS, CampaignDeliveryEngineConstants.FALSE);
			result.addProperty(CampaignDeliveryEngineConstants.DBPERRMSG, "eventspayload is not valid.");
			return result;
		}

		List<EventDTO> events = processJsonArray(eventselement.getAsJsonArray());

		Set<String> corecustomers = new HashSet<>();
		for (EventDTO e : events) {
			corecustomers.add(e.getCorecustomerid());
		}

		Map<String, Set<CoreCustomerDTO>> corecusttocustmapping = getCustomerFromCoreCustomers(corecustomers);

		Set<CoreCustomerDTO> customers = new HashSet<>();

		List<EventDTO> newlist = new ArrayList<>();
		for (EventDTO event : events) {
			if (corecusttocustmapping.containsKey(event.getCorecustomerid())) {
				Set<CoreCustomerDTO> multiplecustomers = corecusttocustmapping.get(event.getCorecustomerid());
				customers.addAll(multiplecustomers);
				int count = 0;
				for (CoreCustomerDTO cust : multiplecustomers) {
					if (count == 0)
						event.setCustomerid(cust.getCustid());
					else {
						EventDTO e = new EventDTO(event.getEventjson(), event.getCorecustomerid(), cust.getCustid());
						e.setCustomerid(cust.getCustid());
						newlist.add(e);
					}
					count++;
				}
			}
		}
		events.addAll(newlist);
		if (customers.isEmpty()) {
			result.addProperty(CampaignDeliveryEngineConstants.SUCCESS, CampaignDeliveryEngineConstants.FALSE);
			result.addProperty(CampaignDeliveryEngineConstants.DBPERRMSG, "No customer Exist for this event");
			return result;
		}

		Map<String, CommunicationNodeDTO> communicationdata = new HashMap<>();
		Map<String, CustomerInfoDTO> customerinfo = new HashMap<>();
		getCustomerDataFromCore(customers, communicationdata, customerinfo);
		diagnostic.prepareDebug(communicationdata.toString()).log();
		diagnostic.prepareDebug(customerinfo.toString()).log();
		callOrchService(events, communicationdata, customerinfo);
		result.addProperty(CampaignDeliveryEngineConstants.SUCCESS, CampaignDeliveryEngineConstants.TRUE);
		return result;
	}

	private static Map<String, Object> generatePayload(List<EventDTO> events,
			Map<String, CommunicationNodeDTO> commdata, Map<String, CustomerInfoDTO> customerinfo) {
		Map<String, Object> requestParameters = new HashMap<>();

		StringBuilder jsonelem = new StringBuilder();
		StringBuilder customerids = new StringBuilder();
		StringBuilder mobile = new StringBuilder();
		StringBuilder email = new StringBuilder();
		StringBuilder fname = new StringBuilder();
		StringBuilder lname = new StringBuilder();
		StringBuilder corecustids = new StringBuilder();
		int eventcount = 0;
		for (EventDTO event : events) {
			if (event.getCustomerid() == null || event.getEventjson() == null)
				continue;
			CommunicationNodeDTO comm = commdata.get(event.getCustomerid());
			CustomerInfoDTO custdata = customerinfo.get(event.getCustomerid());

			jsonelem.append(event.getEventjson()).append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			customerids.append(event.getCustomerid()).append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			if (event.getCorecustomerid() == null)
				event.setCorecustomerid(CampaignDeliveryEngineConstants.NULL_STRING);
			corecustids.append(event.getCorecustomerid()).append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			if (custdata == null || custdata.getFname() == null)
				fname.append(CampaignDeliveryEngineConstants.NULL_STRING)
						.append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			else
				fname.append(custdata.getFname()).append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);

			if (custdata == null || custdata.getLname() == null)
				lname.append(CampaignDeliveryEngineConstants.NULL_STRING)
						.append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			else
				lname.append(custdata.getLname()).append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);

			if (comm == null || comm.getEmail() == null)
				email.append(CampaignDeliveryEngineConstants.NULL_STRING)
						.append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			else
				email.append(comm.getEmail()).append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			if (comm == null || comm.getMobile() == null)
				mobile.append(CampaignDeliveryEngineConstants.NULL_STRING)
						.append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			else
				mobile.append(comm.getMobile()).append(CampaignDeliveryEngineConstants.LOOP_SEPARATOR);
			eventcount++;

		}
		requestParameters.put(CampaignDeliveryEngineConstants.SERVICE_EVENTJSON, jsonelem);
		requestParameters.put(CampaignDeliveryEngineConstants.SERVICE_CUSTOMERID, customerids);
		requestParameters.put(CampaignDeliveryEngineConstants.SERVICE_CORECUSTOMERID, corecustids);

		requestParameters.put(CampaignDeliveryEngineConstants.SERVICE_EMAIL, email);
		requestParameters.put(CampaignDeliveryEngineConstants.SERVICE_MOBILE, mobile);
		requestParameters.put(CampaignDeliveryEngineConstants.SERVICE_FNAME, fname);
		requestParameters.put(CampaignDeliveryEngineConstants.SERVICE_LNAME, lname);

		requestParameters.put("loop_count", eventcount);
		requestParameters.put("loop_separator", CampaignDeliveryEngineConstants.LOOP_SEPARATOR);

		return requestParameters;

	}

	public static void callOrchService(List<EventDTO> events, Map<String, CommunicationNodeDTO> commdata,
			Map<String, CustomerInfoDTO> customerinfo) {

		Map<String, Object> requestParameters = generatePayload(events, commdata, customerinfo);
		diagnostic.prepareDebug("Calling orch with" + requestParameters).log();
		try {
			CampaignDeliveryEngineUtils.callInternalService(requestParameters,
					CampaignDeliveryEngineConstants.CAMPAIGNDELIVERY_ORCH,
					CampaignDeliveryEngineConstants.CAMPAIGNDELIVERY_ORCH_OPERATION, null);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}
	}

	private static String processGenericData(Dataset commdataset, boolean isMobile) {
		String mobileoremail = null;
		List<Record> phonerecords = commdataset.getAllRecords();
		if (phonerecords != null) {
			for (Record custcommdata : phonerecords) {
				boolean isPrimary = custcommdata.getParamValueByName("isPrimary") != null
						&& custcommdata.getParamValueByName("isPrimary").equals("true");
				if (mobileoremail == null && isPrimary) {
					String countryCode = custcommdata
							.getParamValueByName(CampaignDeliveryEngineConstants.PHONECOUNTRYCODE) != null
									? custcommdata.getParamValueByName(CampaignDeliveryEngineConstants.PHONECOUNTRYCODE)
									: "";
					mobileoremail = isMobile ? new StringBuilder().append(countryCode)
							.append(custcommdata.getParamValueByName(CampaignDeliveryEngineConstants.VAL)).toString()
							: custcommdata.getParamValueByName(CampaignDeliveryEngineConstants.VAL);
				}
			}
		}
		return mobileoremail;
	}

	private static void processCustomerData(Result res, Map<String, CommunicationNodeDTO> communicationdata,
			Map<String, CustomerInfoDTO> custinfo) {
		Dataset ds = res.getDatasetById("LoopDataset");
		if (ds == null)
			return;
		List<Record> records = ds.getAllRecords();
		for (Record rec : records) {
			Dataset custdataset = rec.getDatasetById("customer");
			if (custdataset == null)
				continue;
			for (Record r : custdataset.getAllRecords()) {
				String custid = r.getParamValueByName("id");
				if (custid == null)
					continue;
				CustomerInfoDTO c = new CustomerInfoDTO(r.getParamValueByName("FirstName"),
						r.getParamValueByName("LastName"));

				String mobile = null;
				String email = null;
				Dataset commdataset = r.getDatasetById("ContactNumbers");
				if (commdataset != null) {
					mobile = processGenericData(commdataset,true);
				}
				commdataset = r.getDatasetById("EmailIds");
				if (commdataset != null) {
					email = processGenericData(commdataset,false);
				}
				CommunicationNodeDTO comm = new CommunicationNodeDTO(mobile, email);
				custinfo.put(custid, c);
				communicationdata.put(custid, comm);
			}
		}
	}

	private static Map<String, Set<CoreCustomerDTO>> getCustomerFromCoreCustomers(Set<String> corecustomers) {

		Map<String, Set<CoreCustomerDTO>> corecusttocust = new HashMap<>();
		if (corecustomers.isEmpty())
			return corecusttocust;
		String corecuststr = corecustomers.stream().collect(Collectors.joining(","));
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("_corecustomers", corecuststr);
		Result response = null;
		try {
			response = DBPServiceExecutorBuilder.builder().withOperationId("dbxdb_getCustomersIdFromCoreId")
					.withRequestParameters(requestParameters)
					.withServiceId(CampaignDeliveryEngineConstants.CAMPAIGN_DB_SERVICE).build().getResult();
			alert.prepareError("Response"+response).log();
		} catch (Exception e) {
			diagnostic.prepareDebug("Error  occured while fetching Customers:", e).log();
		}
		if (response == null)
			return corecusttocust;
		Dataset rr = response.getDatasetById(CampaignDeliveryEngineConstants.RECORDS);
		if (rr == null)
			return corecusttocust;
		List<CoreCustomerDTO> dbres = new ArrayList<>();
		try {
			dbres = JSONUtils.parseAsList(ResultToJSON.convertDataset(rr).toString(), CoreCustomerDTO.class);
		} catch (IOException e) {
			diagnostic.prepareDebug("Error occured:", e).log();
			return corecusttocust;
		}
		for (CoreCustomerDTO c : dbres) {
			if (corecusttocust.containsKey(c.getCorecustid())) {
				Set<CoreCustomerDTO> customers = corecusttocust.get(c.getCorecustid());
			
				customers.add(c);
				corecusttocust.put(c.getCorecustid(), customers);
			} else {
				Set<CoreCustomerDTO> customers = new HashSet<>();
				customers.add(c);
				corecusttocust.put(c.getCorecustid(), customers);
			}
		}
		return corecusttocust;
	}

	private static void getCustomerDataFromCore(Set<CoreCustomerDTO> customers,
			Map<String, CommunicationNodeDTO> communicationdata, Map<String, CustomerInfoDTO> custinfo) {
		if (customers == null || customers.isEmpty())
			return;
		// call DB service and fill Map
		Map<String, Object> requestParameters = new HashMap<>();
		requestParameters.put("loop_count", customers.size());
		requestParameters.put("loop_seperator", ",");
		StringBuffer customerIds = new StringBuffer();
		StringBuffer legalEntityIds = new StringBuffer();
		
		Iterator<CoreCustomerDTO> iterator = customers.iterator();
		while(iterator.hasNext()) {
			CoreCustomerDTO c = iterator.next(); 
			customerIds.append(c.getCustid());
			legalEntityIds.append(c.getCompanyLegalUnit());
			if(iterator.hasNext()) {
				customerIds.append(",");
				legalEntityIds.append(",");
			}
		}
		
		requestParameters.put("customerId", customerIds.toString());
		requestParameters.put("legalEntityId", legalEntityIds.toString());
		alert.prepareError("requestParameters" + requestParameters).log();
		Result res = null;
		try {
			res = CampaignDeliveryEngineUtils.callInternalService(requestParameters, "CustomerData_Orch",
					"getCustomerDetails_Orch", null);
			if (res != null)
				diagnostic.prepareDebug(ResultToJSON.convert(res)).log();
			if (res != null)
				processCustomerData(res, communicationdata, custinfo);
		} catch (Exception e) {
			diagnostic.prepareDebug("Error occured", e).log();
		}

	}

	private static List<EventDTO> processJsonArray(JsonArray eventsarray) {
		List<EventDTO> events = new ArrayList<>();
		JsonElement otherdata = null;
		String corecustid = null;
		for (JsonElement event : eventsarray) {
			if (CampaignDeliveryEngineUtils.isMemberExistsAndValid(event, "eventId")) {
				if (CampaignDeliveryEngineUtils.isMemberExistsAndValid(event, "otherData")) {
					otherdata = event.getAsJsonObject().get("otherData").getAsJsonObject();
					if (!CampaignDeliveryEngineUtils.isMemberExistsAndValid(otherdata, "customerId")) {
						continue;
					}
					corecustid = otherdata.getAsJsonObject().get("customerId").toString();
					if (corecustid != null) {
						corecustid = corecustid.replace("\"", "");
						EventDTO eventdto = new EventDTO(event, corecustid, null);
						events.add(eventdto);
					}
				}
			}
		}
		return events;
	}
}
