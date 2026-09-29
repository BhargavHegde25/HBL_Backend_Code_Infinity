package com.temenos.dbx.product.payeeservices.businessdelegate.api;

import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.WireTransfersPayeeDTO;

/**
 * Handles all the operations on WireTransfers Payees
 * @author KH2660
 * extends {@link BusinessDelegate}
 */
public interface WireTransfersPayeeBusinessDelegate extends BusinessDelegate {
	
	/**
     * Gets all the Payees based on the filter from dbx wire transfer payee table
     * @param associatedCifs 
     * @param typeId - Domestic or International
     * @return List of {@link WireTransfersPayeeDTO}
     */
	public List<WireTransfersPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs, String typeId);
	
	/**
     * Creates Payee at DBX/Product table - wiretransferspayee table
     * @param WireTransfersPayeeDTO wireTransfersPayeeDTO - contains details for dbx table
     * @return {@link WireTransfersPayeeDTO}
     */
	public WireTransfersPayeeDTO createPayeeAtDBX(WireTransfersPayeeDTO wireTransfersPayeeDTO);

	/**
     * Fetches payee details from dbx table using given payeeId
     * @param BillPayPayeeDTO billPayPayeeDTO
     * @return List of {@link WireTransfersPayeeDTO}
     */
	public List<WireTransfersPayeeDTO> fetchPayeeByIdAtDBX(WireTransfersPayeeDTO wireTransfersPayeeDTO);
	
	/**
     * Deletes Payee at dbx table - wire transfer payee table
     * @param WireTransfersPayeeDTO wireTransfersPayeeDTO
     * @return boolean - true if success else false
     */
	public boolean deletePayeeAtDBX(WireTransfersPayeeDTO wireTransfersPayeeDTO);


}
