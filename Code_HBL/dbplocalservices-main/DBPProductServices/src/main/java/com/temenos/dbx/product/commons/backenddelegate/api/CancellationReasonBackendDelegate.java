package com.temenos.dbx.product.commons.backenddelegate.api;

import java.util.List;

import com.dbp.core.api.BackendDelegate;
import com.temenos.dbx.product.commons.dto.CancellationReasonDTO;
import com.konylabs.middleware.controller.DataControllerRequest;


public interface CancellationReasonBackendDelegate extends BackendDelegate {
	/**
	 * method to fetch Bulk Payment Uploaded Files
	 * @param DataControllerRequest
	 * @return List of BulkPaymentFileDTO contains all uploaded files details
	 */
	public List<CancellationReasonDTO> fetchCancellationReasons(DataControllerRequest dcr);

}
