package com.temenos.dbx.product.payeeservices.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.BillPayPayeeDTO;

/**
 * Handles all the operations on Bill Pay Payees
 * @author KH2638
 * extends {@link BusinessDelegate}
 */
public interface BillPayPayeeBusinessDelegate extends BusinessDelegate {
	
	/**
     * Gets all the Payee details of given payeeIds
     * @param associatedCifs
     * @return List of {@link BillPayPayeeDTO}
     */
	public List<BillPayPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs, String legalEntityId);
	
	/**
     * Creates Payee at DBX/Product table - billpaypayee table
     * @param BillPayPayeeDTO billPayPayeeDTO - contains details for dbx table
     * @return {@link BillPayPayeeDTO}
     */
	public BillPayPayeeDTO createPayeeAtDBX(BillPayPayeeDTO billPayPayeeDTO);

	/**
     * Fetches payee details from dbx table using given payeeId, cif and contractId
     * @param BillPayPayeeDTO billPayPayeeDTO
     * @return List of {@link BillPayPayeeDTO}
     */
	public List<BillPayPayeeDTO> fetchPayeeByIdAtDBX(BillPayPayeeDTO billPayPayeeDTO);

	/**
     * Deletes Payee at dbx table - bill pay payee table
     * @param BillPayPayeeDTO billPayPayeeDTO
     * @return boolean - true if success else false
     */
	public boolean deletePayeeAtDBX(BillPayPayeeDTO billPayPayeeDTO);

}
