/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public interface DownloadAttachmentsPDFBackendDelegate extends BackendDelegate {
	
	/**
	 * (INFO) Downloads the attachments in PDF
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @param headersMap
	 * @author 22952
	 */

	byte[] generatePDF(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);

	

}
