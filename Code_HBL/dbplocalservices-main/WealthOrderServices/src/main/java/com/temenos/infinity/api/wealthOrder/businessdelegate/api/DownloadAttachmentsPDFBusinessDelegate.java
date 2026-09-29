/**
 * 
 */
package com.temenos.infinity.api.wealthOrder.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author himaja.sridhar
 *
 */
public interface DownloadAttachmentsPDFBusinessDelegate extends BusinessDelegate {
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

	String generatePDF(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap);
	


}
