package com.temenos.infinity.api.docmanagement.javaservices;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.docmanagement.resource.impl.DMSResourceImpl;

public class GetStatementsDownloaded implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		String fileId = request.getParameter("fileId");
		byte[] obj = (byte[]) MemoryManager.getFromCache(fileId);
		if(obj == null)
		{
			alert.prepareError("Not Authorized user").log();
			return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
		}
		String mediaType = request.getParameter("mediaType");
		String fileName = request.getParameter("fileName");
		if(!mediaType.equals("")&& !fileName.equals("")) {
			response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
				new BufferedHttpEntity(new ByteArrayEntity(obj)));
			response.getHeaders().putAll(getCustomHeaders(mediaType,fileName));
			response.setStatusCode(HttpStatus.SC_OK);
			diagnostic.prepareInfo("GetStatementsDownloaded -Succesfully constructed the File object").log();
		}
		return new Result();
	}
	
	
	public Map<String, String> getCustomHeaders(String mediaType, String fileName){
		Map<String, String> responseHeaders = new HashMap<String, String>();
		if (mediaType.equalsIgnoreCase("png")) {
			responseHeaders.put(HttpHeaders.CONTENT_TYPE, "image/png");
			responseHeaders.put("Content-Disposition", "attachment; filename=\"" + fileName + ".png" + "\"");
		} else {
			responseHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
			responseHeaders.put("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf" + "\"");
		}
		return responseHeaders;
	}
}
