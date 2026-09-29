package com.temenos.infinity.tradefinanceservices.businessdelegate.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.tradefinanceservices.backenddelegate.api.DashboardBackendDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.DashboardBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.dto.DashboardDTO;
import com.temenos.infinity.tradefinanceservices.utils.DashboardStatusEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants.DASHBOARD_DATE_FORMAT;
import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.*;

/**
 * @author naveen.yerra
 */
public class DashboardBusinessDelegateImpl implements DashboardBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final List<String> productValues = Arrays.asList("IMPORT_LC", "IMPORT_DRAWING", "IMPORT_AMENDMENT",
            "EXPORT_LC", "EXPORT_DRAWING", "EXPORT_AMENDMENT", "ISSUED_GT_AND_SBLC", "ISSUED_GT_AND_SBLC_AMENDMENT",
            "CLAIM_RECEIVED", "RECEIVED_GT_AND_SBLC", "RECEIVED_GT_AND_SBLC_AMENDMENT", "CLAIM_INITIATED",
            "INWARD_COLLECTION", "INWARD_COLLECTION_AMENDMENT", "OUTWARD_COLLECTION", "OUTWARD_COLLECTION_AMENDMENT");
    private static final List<String> productKeys = Arrays.asList("Import LC", "Import Drawing", "Import Amendment",
            "Export LC", "Export Drawing", "Export Amendment", "Issued GT & SBLC", "Issued GT & SBLC Amendment",
            "Claim Received", "Received GT & SBLC", "Received GT & SBLC Amendment", "Claim Initiated",
            "Inward Collection", "Inward Collection Amendment", "Outward Collection", "Outward Collection Amendment");
    private static final Map<String, String> productMapper = IntStream.range(0, productKeys.size()).boxed()
            .collect(Collectors.toMap(productKeys::get, productValues::get));
    DashboardBackendDelegate dashboardBackendDelegate = DBPAPIAbstractFactoryImpl
            .getBackendDelegate(DashboardBackendDelegate.class);
    private static final List<String> paymentStatusList = Arrays.asList("SETTLED", "PAID");
    public List<DashboardDTO> fetchReceivables(Map<String, Object> inputParams, DataControllerRequest request) {
        List<DashboardDTO> receivables = dashboardBackendDelegate.fetchReceivables(inputParams, request);
        if(receivables != null && receivables.size() > 0) {
            String customerId = fetchCustomerFromSession(request);
            try {
                receivables = receivables.stream()
                        .filter(map -> map.getExpiryDate() != null)
                        .peek(map -> {
                            map.setCustomerId(customerId);
                            map.setExpiryDate(formatDate2(map.getExpiryDate()));
                            map.setAvailableBalance(getAvailableBalance(map.getCreditAccount(), customerId));
                            map.setAccount(map.getCreditAccount() != null ? map.getCreditAccount() : "");
                            map.setAccountCurrency(getAccountCurrency(map.getCreditAccount(), customerId));
                            map.setBalanceWithCurrency(getBalanceWithCurrency(map.getAccountCurrency(), map.getAvailableBalance()));
                            map.setPaymentStatus(getPaymentStatus(map.getPaymentStatus(), map.getExpiryDate(), "Received", map.getStatus(), map.getProduct()));
                        }).collect(Collectors.toList());
                return receivables;
            } catch (Exception e) {
                alert.prepareError("Error occurred while setting the data ", e).log();
                return null;
            }
        }
        return receivables;
    }

    public List<DashboardDTO> fetchPayables(Map<String, Object> inputParams, DataControllerRequest request) {
        List<DashboardDTO> payables = dashboardBackendDelegate.fetchPayables(inputParams, request);
        if (payables != null && payables.size() > 0) {
            String customerId = fetchCustomerFromSession(request);
            try {
                payables = payables.stream()
                        .filter(map -> map.getExpiryDate() != null)
                        .peek(map -> {
                            map.setCustomerId(customerId);
                            map.setExpiryDate(formatDate2(map.getExpiryDate()));
                            map.setAvailableBalance(getAvailableBalance(map.getDebitAccount(), customerId));
                            map.setAccount(map.getCreditAccount() != null ? map.getDebitAccount() : "");
                            map.setAccountCurrency(getAccountCurrency(map.getDebitAccount(), customerId));
                            map.setBalanceWithCurrency(getBalanceWithCurrency(map.getAccountCurrency(), map.getAvailableBalance()));
                            map.setPaymentStatus(getPaymentStatus(map.getPaymentStatus(), map.getExpiryDate(), "Paid", map.getStatus(), map.getProduct()));
                        }).collect(Collectors.toList());
                return payables;
            } catch (Exception e) {
                alert.prepareError("Error occurred while setting the data ", e).log();
                return null;
            }
        }
        return payables;
    }

    public List<DashboardDTO> fetchAllDetails(Map<String, Object> inputParams, DataControllerRequest request) {
        return dashboardBackendDelegate.fetchAllDetails(inputParams, request);
    }

    public JSONObject fetchTradeDashboardConfiguration(String userId) {
        return dashboardBackendDelegate.fetchTradeDashboardConfiguration(userId);
    }

    public boolean createTradeDashboardConfiguration(Map<String, Object> map) {
        return dashboardBackendDelegate.createTradeDashboardConfiguration(map);
    }
    public boolean updateTradeDashboardConfiguration(Map<String, Object> map) {
        return dashboardBackendDelegate.updateTradeDashboardConfiguration(map);
    }

    private String getPaymentStatus(String paymentStatus, String expiryDate, String settleStatus, String status, String product) {
        SimpleDateFormat sdf = new SimpleDateFormat(DASHBOARD_DATE_FORMAT);
        Date date = new Date();
        if(paymentStatus == null) {
            try {
                if(DashboardStatusEnum.valueOf(productMapper.get(product)).getOverdueStatus().contains(status.toUpperCase())) {
                    if (sdf.parse(expiryDate).before(sdf.parse(sdf.format(date))))
                        return "Overdue";
                    else
                        return "Pending";
                } else {
                    return "Pending";
                }
            } catch (ParseException e) {
                alert.prepareError("Error while parsing the date ", e).log();
                return "Pending";
            }
        } else if(paymentStatusList.contains(paymentStatus.toUpperCase())) {
            return settleStatus;
        } else {
            return "Pending";
        }
    }

    private String getAvailableBalance(String account, String CustomerId) {
        if(account != null)
            return getAvailableAccountBalance(CustomerId, account);
        return "";
    }

    private String getAccountCurrency(String account, String CustomerId) {
        if (account != null)
            return TradeFinanceCommonUtils.getAccountCurrency(CustomerId, account);
        return "";
    }

    private String getBalanceWithCurrency(String accountCurrency, String balance) {
        if(accountCurrency !=null && !accountCurrency.equals("") && balance != null && !balance.equals(""))
            return getCurrencySymbol(accountCurrency) + balance;
        return  "";
    }
}
