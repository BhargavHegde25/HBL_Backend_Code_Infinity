package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.BulkPaymentPayeeDTO;

public interface BulkPaymentsPayeeBackendDelegate extends BackendDelegate{
		
	/**
	 * Gets Beneficiary name
	 * @param accountNumber
	 * @return {@link ExternalPayeeBackendDTO}
	 */
	public  BulkPaymentPayeeDTO getBeneficiaryNameFromAccountId(String accountNumber, DataControllerRequest dcr);
	
	/**
	 * Gets Bank details of the respective IBAN
	 * @param IBAN
	 * @return {@link ExternalPayeeBackendDTO}
	 */
	public  BulkPaymentPayeeDTO validateIBANandGetBankDetails(String iban, DataControllerRequest dcr);	

	/**
	 * Gets Bank details of the respective BIC
	 * @param BIC
	 * @return {@link ExternalPayeeBackendDTO}
	 */
	public  BulkPaymentPayeeDTO getBankDetailsFromBIC(String bic, DataControllerRequest dcr);
	
	/**
	 * Gets BICs and Bank details list
	 * @return {@link List<ExternalPayeeBackendDTO>}
	 */
	public  List<BulkPaymentPayeeDTO> getAllBICsAndBankDetails(DataControllerRequest dcr);
	
	
}
