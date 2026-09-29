package com.temenos.infinity.product.bulkpaymentservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentPODTO;

public interface BulkPaymentPOBusinessDelegate extends BusinessDelegate{

	/**
     * Creates PO at DBX/Product table 
     * @param BulkPaymentPODTO bulkPaymentPODTO 
     * @return {@link BulkPaymentPODTO}
     */
	public BulkPaymentPODTO createPaymentOrder(BulkPaymentPODTO bulkPaymentPODTO);
	
	/**
	 * Gets Payment Orders from DBX/Product table
	 * @param recordId
	 * @return {@link List<BulkPaymentPODTO>}
	 */
	public List<BulkPaymentPODTO> fetchPaymentOrders(String recordId);
	
	/**
     * Deletes Payment Order at DBX/Product table 
     * @param String recordId, String paymentOrderId
     * @return boolean - true if successful otherwise false
     */
	public Boolean deletePaymentOrder(String recordId, String paymentOrderId);
	
	/**
     * Updates Payment Order at DBX/Product table 
     * @param BulkPaymentPODTO bulkPaymentPODTO 
     * @return {@link BulkPaymentPODTO}
     */
	public BulkPaymentPODTO updatePaymentOrder(BulkPaymentPODTO bulkPaymentPODTO);
}
