package com.temenos.dbx.product.forexservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.forexservices.dto.CurrencyDTO;

public interface ForexBusinessDelegate extends BusinessDelegate {

	List<CurrencyDTO> getPopularCurrencies(String baseCurrencyCode, DataControllerRequest dcRequest);

	List<CurrencyDTO> getRecentCurrencies(String customerId, DataControllerRequest dcRequest);

	List<CurrencyDTO> getDashboardCurrencies(String methodID, String customerId, String baseCurrencyCode,
			DataControllerRequest dcRequest);

	List<CurrencyDTO> getDashboardCurrencyRates(String methodID, String customerId, String baseCurrencyCode, String market, String companyCode,
			DataControllerRequest dcRequest);

	boolean updateRecentCurrencies(String customerId, String quoteCurrencyCode, String legalEntityId);
}
