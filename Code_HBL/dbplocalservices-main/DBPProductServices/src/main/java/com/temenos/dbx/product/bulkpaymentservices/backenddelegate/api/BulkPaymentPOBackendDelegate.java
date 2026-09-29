package com.temenos.dbx.product.bulkpaymentservices.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.dbx.product.bulkpaymentservices.dto.BulkPaymentPODTO;

public interface BulkPaymentPOBackendDelegate extends BackendDelegate{
	
	/**
	 * method to add Bulk Payment -Payment Order At Backend
	 * @param BulkPaymentPODTO bulkPaymentPODTO - contains Payment Order details
	 * @return BulkPaymentPODTO - contains Payment Order details
	 */
	public BulkPaymentPODTO addPaymentOrder(BulkPaymentPODTO bulkPaymentPODTO, DataControllerRequest dcr);
	
	/**
	 * Gets Payment Orders from Backend
	 * @param recordId
	 * @return {@link List<BulkPaymentPODTO>}
	 */
	public List<BulkPaymentPODTO> fetchPaymentOrders(String recordId, DataControllerRequest dcr);
	
	/**
     * Updates Payment Order at Backend 
     * @param BulkPaymentPODTO bulkPaymentPODTO 
     * @return {@link BulkPaymentPODTO}
     */
	public BulkPaymentPODTO updatePaymentOrder(BulkPaymentPODTO bulkPaymentPODTO, DataControllerRequest dcr);

	/**
     * Deletes Payment Order at Backend 
     * @param String recordId, String paymentOrderId
     * @return boolean - true if successful otherwise false
     */
	public BulkPaymentPODTO deletePaymentOrder(String recordId, String paymentOrderId, DataControllerRequest dcr);
	
	/**
     * Approve Payment Order at Backend 
     * @param String recordId, String paymentOrderId
     * @return boolean - true if successful otherwise false
     */
	public List<BulkPaymentPODTO> approvePaymentOrders(String recordId, DataControllerRequest dcr);
}
