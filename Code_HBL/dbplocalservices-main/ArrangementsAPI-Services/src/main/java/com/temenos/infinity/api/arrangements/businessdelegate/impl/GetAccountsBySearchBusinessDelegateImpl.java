package com.temenos.infinity.api.arrangements.businessdelegate.impl;

import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.arrangements.backenddelegate.api.GetAccountsArrangementsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.GetAccountsBySearchBusinessDelegate;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class GetAccountsBySearchBusinessDelegateImpl implements GetAccountsBySearchBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<ArrangementsDTO> getAccounts(String filterKey, String filterValue, String authToken)
            throws ApplicationException {

        List<ArrangementsDTO> arrangementsDTO = null;

        try {
            GetAccountsArrangementsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                    .getBackendDelegate(GetAccountsArrangementsExperienceAPIBackendDelegate.class);

            arrangementsDTO = backendDelegate.getArrangements(filterKey, filterValue, authToken);
        } catch (ApplicationException e) {
            alert.prepareError(e.getMessage()).log();
            throw e;
        }
        return arrangementsDTO;
    }

    @Override
    public JSONObject getAccountsByCoreCustomerIdSearch(String coreCustomerId, String authToken)
            throws ApplicationException {
        GetAccountsArrangementsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(GetAccountsArrangementsExperienceAPIBackendDelegate.class);
        return backendDelegate.getCoreCustomerArrangements(coreCustomerId, authToken);
    }

}
