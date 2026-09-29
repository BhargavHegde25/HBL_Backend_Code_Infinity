package com.temenos.dbx.product.payeeservices.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.payeeservices.dto.InternationalPayeeDTO;
import org.json.JSONObject;


/**
 * @author KH2544
 * @version 1.0
 * @Interface InterBankPayeeBusinessDelegate extends {@link BusinessDelegate}
 */

public interface InternationalPayeeBusinessDelegate extends BusinessDelegate
{
	/**
     * Gets all the Payee details of given payeeIds
     * @param associatedCifs
     * @return List of {@link InternationalPayeeDTO}
     */
	public List<InternationalPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs,String legalEntityId);
	
	/**
     * Creates Payee at DBX/Product table - internationalpayee table
     * @param internationalpayeeDTO billPayPayeeDTO - contains details for dbx table
     * @return {@link internationalpayeeDTO}
     */
	public InternationalPayeeDTO createPayeeAtDBX(InternationalPayeeDTO internationalpayeeDTO);

	/**
     * Fetches payee details from dbx table using given payeeId, cif and contractId
     * @param InternationalPayeeDTO internationalPayeeDTO
     * @return {@link InternationalPayeeDTO}
     */
	public List<InternationalPayeeDTO> fetchPayeeByIdAtDBX(InternationalPayeeDTO internationalpayeeDTO);
	
	/**
     * Deletes Payee at DBX/Product table - internationalpayee table
     * @param InternationalpayeeDTO internationalpayeeDTO - contains details for dbx table
     * @return boolean -  true if success else false
     */
	public boolean deletePayeeAtDBX(InternationalPayeeDTO internationalpayeeDTO);

	JSONObject validateForApprovals( DataControllerRequest request, TransactionStatusDTO payloadForValidateForApprovals ) throws ApplicationException;

	boolean checkIfPayeeStatusInPending(DataControllerRequest request, String payeeId ) throws ApplicationException;
}
