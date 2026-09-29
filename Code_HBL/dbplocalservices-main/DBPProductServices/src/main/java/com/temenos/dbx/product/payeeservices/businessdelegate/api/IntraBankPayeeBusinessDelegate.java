package com.temenos.dbx.product.payeeservices.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.payeeservices.dto.IntraBankPayeeDTO;
import org.json.JSONObject;


/**
 * @author KH2544
 * @version 1.0
 * @Interface InterBankPayeeBusinessDelegate extends {@link BusinessDelegate}
 */

public interface IntraBankPayeeBusinessDelegate extends BusinessDelegate
{
	/**
     * Gets all the Payee details of given payeeIds
     * @param associatedCifs
     * @return List of {@link IntraBankPayeeDTO}
     */
	public List<IntraBankPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs,String legalEntityId);
	
	/**
     * Creates Payee at DBX/Product table - intrabankpayee table
     * @param IntraBankPayeeDTO billPayPayeeDTO - contains details for dbx table
     * @return {@link IntraBankPayeeDTO}
     */
	public IntraBankPayeeDTO createPayeeAtDBX(IntraBankPayeeDTO intraBankPayeeDTO);

	/**
     * Fetches payee details from dbx table using given payeeId, cif and contractId
     * @param IntraBankPayeeDTO intraBankPayeeDTO
     * @return {@link IntraBankPayeeDTO}
     */
	public List<IntraBankPayeeDTO> fetchPayeeByIdAtDBX(IntraBankPayeeDTO intraBankPayeeDTO);

	/**
     * Deletes Payee at dbx table - intrabankpayee table
     * @param  intraBankPayeeBackendDTO - contains details for backend table
     * @return boolean
     */
    public boolean deletePayeeAtDBX(IntraBankPayeeDTO intraBankPayeeBackendDTO);

	JSONObject validateForApprovals( DataControllerRequest request, TransactionStatusDTO payloadForValidateForApprovals ) throws ApplicationException;

    boolean checkIfPayeeStatusInPending( DataControllerRequest request, String payeeId ) throws ApplicationException;
}
