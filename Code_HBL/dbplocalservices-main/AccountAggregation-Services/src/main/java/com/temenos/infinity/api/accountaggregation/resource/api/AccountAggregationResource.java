package com.temenos.infinity.api.accountaggregation.resource.api;

import java.util.Map;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.dataobject.Result;

public interface AccountAggregationResource extends Resource {

    Result getTermsAndCondition(String termsAndConditionsCode, String languageCode, String operation, String bankCode,
            Map<String, Object> headersMap);

    Result getBanks(String countryCode, String authToken);

    Result createTermsAndConditions(String digitalProfileId, String javascript_callback_type, String from_date,
            String scopes, String providerCode, String period_days, String fetch_scopes, String operation,
            String authToken, String CompanyId);

    Result updateStatus(String stage, String digitalProfileId, String providerCode, String authToken);

    Result refreshConnection(String digitalProfileId, String providerCode, String authToken, String CompanyId);

    Result deleteOperation(String digitalProfileId, String providerCode, String authToken, String CompanyId);

    public String getProfileIdwithCompanyName(String digitalProfileId, String CompanyId);

}
