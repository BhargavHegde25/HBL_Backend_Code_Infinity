package com.temenos.dbx.product.payeeservices.backenddelegate.api;


import java.util.List;

import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeBackendDTO;
import com.temenos.dbx.product.payeeservices.dto.P2PPayeeDTO;

import com.temenos.dbx.product.payeeservices.dto.PayeesFilterDTO;



/**
 * @author KH2544
 * @version 1.0

 * @Interface P2PPayeeBusinessDelegate extends {@link BusinessDelegate}

 */

public interface P2PPayeeBackendDelegate extends BackendDelegate
{
	/**
     * Creates Payee at Backend - payperson table
     * @param P2PPayeeDTO p2pPayeeDTO - contains details for dbx table
     * @return {@link P2PPayeeDTO}
     */
	public P2PPayeeBackendDTO createPayee(P2PPayeeBackendDTO p2pPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
	 * TODO- make this a looping concurrent orchestration call
     * Gets all the Payees associated to user using cif
     * @param Set<String> payeeIds
     * @param P2PPayeeBackendDTO p2pPayeeBackendDTOs - list of payees
     * @param FilterDTO filterDTO - contains filter params
     * @return List of {@link P2PPayeeBackendDTO}
     */
	public List<P2PPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Edits Payee at Backend - payee table
     * @param BillPayPayeeBackendDTO billPayPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link P2PPayeeBackendDTO}
     */
	public P2PPayeeBackendDTO editPayee(P2PPayeeBackendDTO p2pPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Deletes Payee at backend table - payee table
     * @param P2PPayeeBackendDTO P2PPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link P2PPayeeBackendDTO}
     */
	public P2PPayeeBackendDTO deletePayee(P2PPayeeBackendDTO p2pPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);

}
