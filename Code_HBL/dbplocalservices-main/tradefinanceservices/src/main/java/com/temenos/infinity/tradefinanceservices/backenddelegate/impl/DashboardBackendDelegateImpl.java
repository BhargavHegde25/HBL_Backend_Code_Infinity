package com.temenos.infinity.tradefinanceservices.backenddelegate.impl;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.Gson;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.DashboardBackendDelegate;
import com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices;
import com.temenos.infinity.tradefinanceservices.dto.DashboardDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

import static com.temenos.infinity.tradefinanceservices.config.TradeFinanceAPIServices.*;
import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.*;

/**
 * @author naveen.yerra
 */
public class DashboardBackendDelegateImpl implements DashboardBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final List<String> allTradesList = Arrays.asList("ClaimsReceived", "ReceivedAmendments", "Amendments", "ExportLcAmendments", "GuaranteesLC",
            "Guarantees", "GuaranteeLCAmendments", "ExportLCDrawings", "LetterOfCredits", "drawings", "InwardCollections",
            "ExportLetterOfCredits", "InwardCollectionAmendments", "GuaranteeClaims", "OutwardCollections", "OutwardCollectionAmendments");

    //Receivables Status
    private static final List<String> outwardCollectionStatus = Arrays.asList("APPROVED", "OVERDUE", "SETTLED");
    private static final List<String> claimsInitiatedStatus = Arrays.asList("PROCESSING WITH BANK", "CLAIM HONOURED");
    private static final List<String> exportLcDrawingsStatus = Arrays.asList("APPROVED", "SETTLED");

    //Payables Status
    private static final List<String> importLcDrawingStatus = Arrays.asList("SUBMITTED TO BANK", "SETTLED");
    private static final List<String> claimsReceivedStatus = Arrays.asList("PROCESSING BY BANK", "CLAIM HONOURED (APPLICANT REJECTED)", "CLAIM HONOURED (PENDING CONSENT)", "CLAIM HONOURED");
    private static final List<String> inwardCollectionStatus = Arrays.asList("APPROVED", "OVERDUE", "PAY DUE", "SETTLED");

    public List<DashboardDTO> fetchReceivables(Map<String, Object> inputParams, DataControllerRequest request) {

        String fetchResponse;
        try {
            fetchResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(DASHBOARD_ORCH_FETCHRECEIVABLES.getServiceName()).
                    withObjectId(null).
                    withOperationId(DASHBOARD_ORCH_FETCHRECEIVABLES.getOperationName()).
                    withDataControllerRequest(request).
                    build().getResponse();
        } catch (DBPApplicationException e) {
            alert.prepareError("Error occurred while hitting the service", e).log();
            return null;
        }

        JSONObject jsonResponse = new JSONObject(fetchResponse);
        List<DashboardDTO> receivables = new ArrayList<>();
        List<DashboardDTO> receivablesList;
        try{
            if (jsonResponse.has(EXPORT_LC_DRAWINGS_GET)) {
                receivablesList = JSONUtils.parseAsList(jsonResponse.getJSONArray(EXPORT_LC_DRAWINGS_GET).toString(), DashboardDTO.class);
                receivables.addAll(getListByStatus(receivablesList, exportLcDrawingsStatus));
            }

            if (jsonResponse.has(OUTWARD_COLLECTIONS_GET)) {
                receivablesList = JSONUtils.parseAsList(jsonResponse.getJSONArray(OUTWARD_COLLECTIONS_GET).toString(), DashboardDTO.class);
                receivables.addAll(getListByStatus(receivablesList, outwardCollectionStatus));
            }

            if (jsonResponse.has(GUARANTEE_CLAIMS_GET)) {
                receivablesList = JSONUtils.parseAsList(jsonResponse.getJSONArray(GUARANTEE_CLAIMS_GET).toString(), DashboardDTO.class);
                receivables.addAll(getListByStatus(receivablesList, claimsInitiatedStatus));
            }
            return receivables;
        } catch(Exception e) {
            alert.prepareError("Error occurred while parsing the response", e).log();
            return null;
        }
    }

    @Override
    public List<DashboardDTO> fetchPayables(Map<String, Object> inputParams, DataControllerRequest request) {
        String fetchResponse;
        try {
            fetchResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(DASHBOARD_ORCH_FETCHPAYABLES.getServiceName()).
                    withObjectId(null).
                    withOperationId(DASHBOARD_ORCH_FETCHPAYABLES.getOperationName()).
                    withDataControllerRequest(request).
                    build().getResponse();
        } catch (DBPApplicationException e) {
            alert.prepareError("Error occurred while hitting the service", e).log();
            return null;
        }

        JSONObject jsonResponse = new JSONObject(fetchResponse);
        List<DashboardDTO> payables = new ArrayList<>();
        List<DashboardDTO> payablesList;
        try{
            if (jsonResponse.has(CLAIMS_RECEIVED_GET)) {
                payablesList = JSONUtils.parseAsList(jsonResponse.getJSONArray(CLAIMS_RECEIVED_GET).toString(), DashboardDTO.class);
                payables.addAll(getListByStatus(payablesList, claimsReceivedStatus));
            }

            if (jsonResponse.has(IMPORT_DRAWINGS_GET)) {
                payablesList = JSONUtils.parseAsList(jsonResponse.getJSONArray(IMPORT_DRAWINGS_GET).toString(), DashboardDTO.class);
                payables.addAll(getListByStatus(payablesList, importLcDrawingStatus));
            }

            if (jsonResponse.has(INWARD_COLLECTIONS_GET)) {
                payablesList = JSONUtils.parseAsList(jsonResponse.getJSONArray(INWARD_COLLECTIONS_GET).toString(), DashboardDTO.class);
                payables.addAll(getListByStatus(payablesList, inwardCollectionStatus));
            }
            return payables;
        } catch(Exception e) {
            alert.prepareError("Error occurred while parsing the response", e).log();
            return null;
        }
    }

    private List<DashboardDTO> getListByStatus(List<DashboardDTO> list, List<String> status) {
        return list.stream().filter(map -> map.getStatus() != null && status.contains(map.getStatus().toUpperCase())).collect(
                Collectors.toList());
    }

    @Override
    public List<DashboardDTO> fetchAllDetails(Map<String, Object> inputParams, DataControllerRequest request) {
        String fetchResponse;
        try {
            fetchResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(DASHBOARD_ORCH_FETCHALLTRADES.getServiceName()).
                    withObjectId(null).
                    withOperationId(DASHBOARD_ORCH_FETCHALLTRADES.getOperationName()).
                    withDataControllerRequest(request).
                    build().getResponse();
        } catch (DBPApplicationException e) {
            alert.prepareError("Error occurred while hitting the service", e).log();
            return null;
        }

        JSONObject jsonResponse = new JSONObject(fetchResponse);
        ArrayList<Map<String, Object>> overviewResponse = new ArrayList<>();
        Gson gson = new Gson();
        for(String key : allTradesList) {
            if(jsonResponse.has(key))
                overviewResponse.addAll(gson.fromJson(String.valueOf(jsonResponse.getJSONArray(key)), ArrayList.class));
        }
        List<DashboardDTO> overviewDtoResponse;
        try {
            overviewDtoResponse = JSONUtils.parseAsList(new JSONArray(overviewResponse).toString(), DashboardDTO.class);
            overviewDtoResponse = overviewDtoResponse.stream()
                    .peek(map -> map.setTradeStatus(getTradeStatus(map.getStatus(), map.getPaymentStatus())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            alert.prepareError("Error occurred while parsing the response", e).log();
            return null;
        }
        return overviewDtoResponse;
    }

    public JSONObject fetchTradeDashboardConfiguration(String userId) {
        String fetchResponse;
        String filter = "userId" + DBPUtilitiesConstants.EQUAL + userId;
        Map<String, Object>  inputMap = new HashMap<>();
        inputMap.put(DBPUtilitiesConstants.FILTER, filter);
        try {
            fetchResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_TRADECONFIG_GET.getServiceName()).
                    withObjectId(null).
                    withOperationId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_TRADECONFIG_GET.getOperationName()).
                    withRequestParameters(inputMap).
                    build().getResponse();
        } catch (DBPApplicationException e) {
            alert.prepareError("Error occurred while hitting the service", e).log();
            return null;
        }

        return new JSONObject(fetchResponse).getJSONArray("tradefinanceconfiguration").optJSONObject(0);
    }
    public boolean createTradeDashboardConfiguration(Map<String, Object> map) {
        String fetchResponse;
        try {
            fetchResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_TRADECONFIG_CREATE.getServiceName()).
                    withObjectId(null).
                    withOperationId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_TRADECONFIG_CREATE.getOperationName()).
                    withRequestParameters(map).
                    build().getResponse();
        } catch (DBPApplicationException e) {
            alert.prepareError("Error occurred while hitting the service").log();
            return false;
        }
        try {
            return new JSONObject(fetchResponse).getJSONArray("tradefinanceconfiguration").getJSONObject(0).has("userId");
        } catch (Exception e) {
            alert.prepareError("Error occurred while parsing response", e).log();
            return false;
        }
    }

    public boolean updateTradeDashboardConfiguration(Map<String, Object> map) {
        String fetchResponse;
        try {
            fetchResponse = DBPServiceExecutorBuilder.builder().
                    withServiceId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_TRADECONFIG_UPDATE.getServiceName()).
                    withObjectId(null).
                    withOperationId(TradeFinanceAPIServices.DBPRBLOCALSERVICE_TRADECONFIG_UPDATE.getOperationName()).
                    withRequestParameters(map).
                    build().getResponse();
        } catch (DBPApplicationException e) {
            alert.prepareError("Error occurred while hitting the service", e).log();
            return false;
        }
        try {
            return new JSONObject(fetchResponse).getJSONArray("tradefinanceconfiguration").getJSONObject(0).has("userId");
        } catch (Exception e) {
            alert.prepareError("Error occurred while parsing response", e).log();
            return false;
        }
    }

    private String getTradeStatus(String status, String paymentStatus) {
        String tradeStatus = paymentStatus;
        tradeStatus = tradeStatus != null ? (tradeStatus.equalsIgnoreCase("settled") ||
                tradeStatus.equalsIgnoreCase("paid") ? tradeStatus : status) : status;

        if(tradeStatus != null) {
            switch (status.toUpperCase()) {
                case "RETURNED BY BANK" :  return PARAM_STATUS_RETURNED_by_BANK;

                case "NEW" : return PARAM_STATUS_PENDING_CONSENTS;

                case "PARTIALLY SETTLED" : return PARAM_STATUS_PENDING_CLOSURE;

                case "DRAFT" : return PARAM_STATUS_PENDING_DRAFTS;

                case "SETTLED" :
                case "PAID" :  return PARAM_STATUS_RECENTLY_COMPLETED;

                case "REJECTED" :
                case "CLAIM REJECTED" :
                case "REJECTED BY BANK" :  return PARAM_STATUS_RECENTLY_REJECTED;
            }
        }
        return tradeStatus != null ? tradeStatus : "";
    }
}
