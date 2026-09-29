/**
 * 
 */
package com.temenos.infinity.api.wealth.businessdelegate.impl;

import java.util.Base64;
import java.util.Map;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.temenos.infinity.api.wealth.backenddelegate.api.DownloadAttachmentsPDFBackendDelegate;
import com.temenos.infinity.api.wealth.businessdelegate.api.DownloadAttachmentsPDFBusinessDelegate;

/**
 * @author himaja.sridhar
 *
 */
public class DownloadAttachmentsPDFBusinessDelegateImpl implements DownloadAttachmentsPDFBusinessDelegate {

	@Override
	public String generatePDF(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response, Map<String, Object> headersMap) {
		DownloadAttachmentsPDFBackendDelegate downloadAttachmentsPDFBackendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(DownloadAttachmentsPDFBackendDelegate.class);
		//result = downloadAttachmentsPDFBackendDelegate.generatePDF(methodID, inputArray, request, response, headersMap);
		byte[] output = downloadAttachmentsPDFBackendDelegate.generatePDF(methodID, inputArray, request, response,headersMap);
		String encoded = Base64.getEncoder().encodeToString(output);
		//String encoded = output.toString();
		//return result;
		return encoded;
	}
	
	

}
