package com.temenos.dbx.product.payeeservices.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;
import com.temenos.dbx.product.payeeservices.dto.InterBankPayeeDTO;
import org.json.JSONObject;


/**
 * @author KH2544
 * @version 1.0
 * @Interface InterBankPayeeBusinessDelegate extends {@link BusinessDelegate}
 */

public interface InterBankPayeeBusinessDelegate extends BusinessDelegate
{

    /**
     * Gets all the Payee details of given payeeIds
     * @param associatedCifs
     * @return List of {@link InterBankPayeeDTO}
     */
	public List<InterBankPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs,String legalEntityId);

    /**
     * Creates Payee at DBX/Product table - intrabankpayee table
     * @param  interBankPayeeDTO - contains details for dbx table
     * @return {@link InterBankPayeeDTO}
     */
    public InterBankPayeeDTO createPayeeAtDBX(InterBankPayeeDTO interBankPayeeDTO);

    /**
     * Deletes Payee at dbx table - interbankpayee table
     * @param  interBankPayeeDTO - contains details for payee
     * @return boolean
     */
    public boolean deletePayeeAtDBX(InterBankPayeeDTO interBankPayeeDTO);

    /**
     * Fetches payee details from dbx table using given payeeId, cif and contractId
     * @param InterBankPayeeDTO interBankPayeeDTO
     * @return {@link InterBankPayeeDTO}
     */
    public List<InterBankPayeeDTO> fetchPayeeByIdAtDBX(InterBankPayeeDTO interBankPayeeDTO);

    JSONObject validateForApprovals( DataControllerRequest request, TransactionStatusDTO payloadForValidateForApprovals ) throws ApplicationException;

    boolean checkIfPayeeStatusInPending(DataControllerRequest request, String payeeId ) throws ApplicationException;

}
