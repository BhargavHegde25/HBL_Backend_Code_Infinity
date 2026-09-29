package com.temenos.infinity.api.accountaggregation.businessdelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.accountaggregation.dto.BankDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConnectionDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConsentDTO;
import com.temenos.infinity.api.accountaggregation.dto.TermsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public interface AccountAggregationBusinessDelegate extends BusinessDelegate {

    List<BankDTO> getBanks(String countryCode, String authToken) throws ApplicationException;

    String createCustomer(String digitalProfileId, String authToken) throws ApplicationException;

    TermsDTO getTermsAndCondition(String termsAndConditionsCode, String languageCode, String operation, String bankCode,Map<String,Object> headersMap)
            throws ApplicationException;

    ConsentDTO getConsentURL(String digitalProfileId, String javascript_callback_type, String from_date, String scopes,
            String providerCode, String period_days, String fetch_scopes, String operation, String authToken) throws ApplicationException;

    ConnectionDTO loadAccountsWithTransactions(String digitalProfileId, String providerCode, String authToken)
            throws ApplicationException;

    ConnectionDTO loadConnection(String digitalProfileId, String authToken) throws ApplicationException;

    ConnectionDTO deleteConnection(String digitalProfileId, String providerCode, String authToken) throws ApplicationException;

    void createConsent(String digitalProfileId, String from_date, String providerCode, String period_days,
            String fetch_scopes, String authToken) throws ApplicationException;

    ConnectionDTO refreshConnection(String digitalProfileId, String providerCode, String authToken) throws ApplicationException;

}
