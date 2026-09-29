package com.temenos.dbx.product.payeeservices.businessdelegate.api;


import java.util.List;
import java.util.Set;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeDTO;

/**
 * @author KH2544
 * @version 1.0

 * @Interface P2PPayeeBusinessDelegate extends {@link BusinessDelegate}

 */

public interface P2PPayeeBusinessDelegate extends BusinessDelegate
{
	/**
     * Creates Payee at DBX/Product table - p2ppayee table
     * @param P2PPayeeDTO p2pPayeeDTO - contains details for dbx table
     * @return {@link P2PPayeeDTO}
     */
	public P2PPayeeDTO createPayeeAtDBX(P2PPayeeDTO p2pPayeeDTO);

	/**
     * Fetches payee details from dbx table using given payeeId and cif, if cif is not given then it filters based on payeeId
     * @param (P2PPayeeDTO p2pPayeeDTO)
     * @return List of {@link P2PPayeeDTO}
     */
	public List<P2PPayeeDTO> fetchPayeeByIdAtDBX(P2PPayeeDTO p2pPayeeDTO);

	/**
     * Edits Payee at backend table - payee table
     * Gets all the Payees based on the filter from dbx p2ppayee table
     * @param P2PPayeeDTO p2pPayeeFilterDTO - Contains CustomerId and CompanyId based on which the filter is created
     * @return List of {@link P2PPayeeDTO}
     */
	public List<P2PPayeeDTO> fetchPayeesFromDBX(Set<String> associatedCifs, String legalEntityId);
	

	public boolean editPayeeAtDBX(String id);
	
	/**
     * Deletes Payee at DBX/Product table - p2ppayee table
     * @param String id
     * @return boolean - true if successful otherwise false
     */
	public boolean deletePayeeAtDBX(P2PPayeeDTO p2pPayeeDTO);

}
