package com.temenos.dbx.product.forexservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.forexservices.dto.CurrencyDTO;

public interface ForexBackendDelegate extends BackendDelegate {
	CurrencyDTO getBaseCurrencyFromBackend(DataControllerRequest dcRequest, String methodID,String market, String companyCode, String countryCode);
	List<CurrencyDTO> getCurrencyListFromBackend();
	CurrencyDTO getCurrencyRatesFromBackend(DataControllerRequest dcRequest, String methodID, String baseCurrencyCode, String quoteCurrencyCode, String market, String companyCode);
	List<CurrencyDTO> getRecentOrPopularCurrencyRatesFromBackend(DataControllerRequest dcRequest, String methodID,String string, String string2, String string3, String string4,
			int loopCount);
}
