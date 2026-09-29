package com.kony.campaign;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.campaign.common.CampaignConstants;
import com.kony.campaign.dto.Campaign;
import com.kony.campaign.dto.CampaignRequest;
import com.kony.campaign.util.CampaignUtil;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;

public class JobCampaignEvalutor extends AbstractCampaignEvalutor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	private static void fetchEligibleCampaigns(List<Campaign> totalcampaignList, Map<String, Campaign> campmap) {
		for (Campaign c : totalcampaignList)
			if (c != null && c.getDcList() != null && !c.getDcList().isEmpty())
				campmap.put(c.getCampaignId(), c);
	}

	@Override
	public List<Campaign> processAnalyticsResponse(CampaignRequest campaignRequest, Result analyticsCampaignResult,
			List<Campaign> totalcampaignList) throws CampaignException {
		try {

			Set<String> custlist = new HashSet<>();
			Map<String, Campaign> campmap = new HashMap<>();
			Map<String, Integer> campcountmap = new HashMap<>();
			Map<String, Map<String, Integer>> campvscustmap = new HashMap<>();
			fetchEligibleCampaigns(totalcampaignList, campmap);
			Dataset ds = analyticsCampaignResult.getDatasetById(CampaignConstants.LOOP_DATASET);
			if (ds == null)
				return totalcampaignList;
			List<Record> records = ds.getAllRecords();
			for (Record rec : records) {
				if (rec.getParam(CampaignConstants.LOOP_PARAM_CAMPAIGN_ID) == null
						&& rec.getDatasetById(CampaignConstants.CUSTOMERS_DATASET) == null) {
					continue;
				}
				String campaignid = null;
				if (rec.getParam(CampaignConstants.LOOP_PARAM_CAMPAIGN_ID) != null) {
					campaignid = rec.getParam(CampaignConstants.LOOP_PARAM_CAMPAIGN_ID).getValue();
					if (campcountmap.containsKey(campaignid))
						campcountmap.put(campaignid, campcountmap.get(campaignid) + 1);
					else
						campcountmap.put(campaignid, 1);
				}
				if (rec.getDatasetById(CampaignConstants.CUSTOMERS_DATASET) == null || campaignid == null) {
					continue;
				}

				Map<String, Integer> custcountmap = campvscustmap.get(campaignid);
				if (custcountmap == null)
					custcountmap = new HashMap<>();
				campvscustmap.put(campaignid, custcountmap);

				Dataset customers = rec.getDatasetById(CampaignConstants.CUSTOMERS_DATASET);
				List<Record> customersrecords = customers.getAllRecords();
				Set<String> uniquecustomers = new HashSet<>();

				generateUniqueCustomerSet(customersrecords, uniquecustomers);
				updateCustomersCount(uniquecustomers, custlist, custcountmap);
			}
			updateEligibleCampaignCustomerData(campcountmap, campmap, campvscustmap);
			updateDefaultCampaignCustomerData(totalcampaignList, custlist);
		} catch (Exception e) {
			alert.prepareError("Error occured", e).log();
		}
		return totalcampaignList;
	}

	private static void generateUniqueCustomerSet(List<Record> customersrecords, Set<String> uniquecustomers) {
		for (Record customerrecord : customersrecords) {
			if (customerrecord.getParam(CampaignConstants.CUSTOMER_ID) == null)
				continue;
			String custid = customerrecord.getParam(CampaignConstants.CUSTOMER_ID).getValue();
			uniquecustomers.add(custid);
		}
	}

	private static void updateCustomersCount(Set<String> uniquecustomers, Set<String> custlist,
			Map<String, Integer> custcountmap) {

		for (String customer : uniquecustomers) {
			custlist.add(customer);
			if (custcountmap.containsKey(customer))
				custcountmap.put(customer, custcountmap.get(customer) + 1);
			else
				custcountmap.put(customer, 1);
		}

	}

	private static void updateEligibleCampaignCustomerData(Map<String, Integer> campcountmap,
			Map<String, Campaign> campmap, Map<String, Map<String, Integer>> campvscustmap) {
		for (String campaignidref : campcountmap.keySet()) {
			Campaign c = campmap.get(campaignidref);
			Map<String, Integer> custcountmap = campvscustmap.get(campaignidref);
			if (custcountmap != null)
				for (String custid : custcountmap.keySet()) {
					if (custcountmap.get(custid).equals(campcountmap.get(campaignidref))) {
						if (c.getCustomers() == null)
							c.setCustomers(new HashSet<>());
						c.getCustomers().add(custid);

					}
				}
		}
	}

	private static void updateDefaultCampaignCustomerData(List<Campaign> totalcampaignList, Set<String> custlist) {
		for (Campaign c : totalcampaignList) {
			if (c.getDcList() == null || c.getDcList().isEmpty()) {
				if (c.getCustomers() == null)
					c.setCustomers(new HashSet<>());
				c.getCustomers().addAll(custlist);
			}
		}
	}

	@Override
	public Result processEligibleCampaigns(CampaignRequest campaignRequest, List<Campaign> campList,
			Result campaignResult) {

		Map<String, Set<String>> customerscampaigns = new HashMap<>();
		Map<String, Set<String>> campaignscustomers = new HashMap<>();
		try {
			for (Campaign c : campList) {
				String campaignId = c.getCampaignId();
				Set<String> customers = c.getCustomers();
				if (customers != null) {
					for (String customer : customers) {
						processCustomersForCampaigns(customerscampaigns, campaignId, customer);
						processCampaignsForCustomers(campaignscustomers, campaignId, customer);
					}
				}
			}
			updateToCache(customerscampaigns);
			updateToCache(campaignscustomers);

		} catch (MiddlewareException e) {
			alert.prepareError("Error occured", e).log();
		}
		return new Result();
	}

	private static void processCampaignsForCustomers(Map<String, Set<String>> campaignscustomers, String campaignId,
			String customer) {
		if (campaignscustomers.containsKey(campaignId)) {
			campaignscustomers.get(campaignId).add(customer);
		} else {
			Set<String> customersset = new HashSet<>();
			customersset.add(customer);
			campaignscustomers.put(campaignId, customersset);
		}
	}

	private static void processCustomersForCampaigns(Map<String, Set<String>> customerscampaigns, String campaignId,
			String customer) {
		if (customerscampaigns.containsKey(customer)) {
			customerscampaigns.get(customer).add(campaignId);
		} else {
			Set<String> campaigns = new HashSet<>();
			campaigns.add(campaignId);
			customerscampaigns.put(customer, campaigns);
		}
	}

	private static void updateToCache(Map<String, Set<String>> records) throws MiddlewareException {

		diagnostic.prepareDebug("records" + records).log();

		Set<String> keyset = records.keySet();
		for (String key : keyset) {
			if (!records.get(key).isEmpty()) {
				CampaignUtil.updateCache(key, records.get(key).stream().collect(Collectors.joining(",")),
						CampaignUtil.getCacheExpiry());
			}
		}
	}

	@Override
	protected Map<String, Object> getFetchCampaignsInputMap(CampaignRequest campaignRequest) {
		return null;
	}

	@Override
	protected List<Campaign> filterIgnoredCampaigns(List<Campaign> campList, Result campResult)
			throws CampaignException {
		return campList;
	}
}
