package com.temenos.infinity.api.docmanagement.javaservices;


import org.apache.http.HttpStatus;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.docmanagement.resource.api.DMSResource;
import com.temenos.infinity.api.docmanagement.resource.impl.DMSResourceImpl;

/**
 * 
 * @author TeamEverest
 * @version Java Service end point to login and download file
 * 
 */
public class DownloadFileOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final int SIZE_OF_RANDOM_GENERATED_STRING = 10;

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			DMSResource downloadResource = DBPAPIAbstractFactoryImpl.getResource(DMSResource.class);
			String documentId = request.getParameter("id");
			String revision = request.getParameter("revision");
			String auth_token = request.getHeader("X-Kony-Authorization");
			String operation = "download";

			Result result = downloadResource.loginAndDownload(documentId, revision, operation, auth_token);
			Result res = new Result();
			if (result.getParamValueByName("base64").length() > 0) {
				String fileId = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
				byte[] bytes = java.util.Base64.getMimeDecoder().decode(result.getParamValueByName("base64"));
//				request.getSession().setAttribute(fileId, bytes);
				MemoryManager.saveIntoCache(fileId, bytes, 120);
            	res.addParam("fileId", fileId);
				response.setStatusCode(HttpStatus.SC_OK);
				diagnostic.prepareInfo("DownloadFileOperation -Succesfully constructed the File object").log();
			} else {
				alert.prepareError("DownloadFileOperation-DMS-No records were found with the given selection criteria").log();
				return ErrorCodeEnum.ERR_25001.setErrorCode(new Result()); //is this needed need to check
			}
			return res;
		}
		catch (Exception e) {
			alert.prepareError("DownloadFileOperation" + e).log();
			return ErrorCodeEnum.ERR_25001.setErrorCode(new Result());
		}
	}

}