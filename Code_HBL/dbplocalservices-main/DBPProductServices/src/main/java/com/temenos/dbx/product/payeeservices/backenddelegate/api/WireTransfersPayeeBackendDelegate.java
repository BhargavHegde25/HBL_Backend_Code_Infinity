package com.temenos.dbx.product.payeeservices.backenddelegate.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.payeeservices.dto.WireTransfersPayeeBackendDTO;

/**
 * @author Subrahmanyam Yadav
 * @version 1.0
 * @Interface WireTransfersPayeeBackendDelegate extends {@link BackendDelegate}
 */
public interface WireTransfersPayeeBackendDelegate extends BackendDelegate {
	
	/**
     * Gets all the Payees associated to user using cif
     * @param Set<String> payeeIds
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return List of {@link WireTransfersPayeeBackendDTO}
     */
	public List<WireTransfersPayeeBackendDTO> fetchPayees(Set<String> payeeIds, 
			Map<String, Object> headerParams, DataControllerRequest dcRequest);

	/**
     * Creates Payee at Backend - payee table
     * @param WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link WireTransfersPayeeBackendDTO}
     */
	public WireTransfersPayeeBackendDTO createPayee(WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Deletes Payee at backend table - payee table
     * @param WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link WireTransfersPayeeBackendDTO}
     */
	public WireTransfersPayeeBackendDTO deletePayee(WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	/**
     * Edits Payee at backend table - payee table
     * @param WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO - contains details for backend table
     * @param headerParams - request header params
     * @param DataControllerRequest dcRequest - contains identity handler which is used to fetch UserId at core service
     * @return {@link WireTransfersPayeeBackendDTO}
     */
	public WireTransfersPayeeBackendDTO editPayee(WireTransfersPayeeBackendDTO wireTransfersPayeeBackendDTO,
			Map<String, Object> headerParams, DataControllerRequest dcRequest);
	
	

}
