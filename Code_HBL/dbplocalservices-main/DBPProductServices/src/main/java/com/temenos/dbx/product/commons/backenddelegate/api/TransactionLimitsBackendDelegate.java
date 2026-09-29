package com.temenos.dbx.product.commons.backenddelegate.api;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;

public interface TransactionLimitsBackendDelegate extends BackendDelegate{
	
	/**
	 * calculates the amount based on the fx rate
	 * @param currency
	 * @param amount
	 * @return
	 */
	@Deprecated
	public Double fetchConvertedAmount(String currency, String amount, DataControllerRequest request);

	/**
	 * @description - calculates the target amount based on the forex rates, and the fromCurrency and toCurrency
	 * @param amount
	 * @param fromCurrency
	 * @param toCurrency
	 * @param dcr
	 * @return
	 */
	public Double fetchNewConvertedAmount(Double amount, String fromCurrency, String toCurrency, DataControllerRequest dcr);
}
