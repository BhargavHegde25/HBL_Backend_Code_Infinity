package com.temenos.dbx.MessageBinary;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.dbx.product.utils.HTTPOperations;

public class MediaDownloadMessageBinaryOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		String base64 = null;
		ServicesManager serviceManager;
		String isDMSIntegrationEnabled="false";
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			isDMSIntegrationEnabled = configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED");
		} catch (MiddlewareException e) {
			alert.prepareError("Error while fetching DMS_INTEGRATION_ENABLED Parameter").log();
		}
		if(StringUtils.isBlank(isDMSIntegrationEnabled)) {
			isDMSIntegrationEnabled="false";
		}

		String fileName = dcRequest.getParameter("fileName");
		String fieldName = dcRequest.getParameter("binaryAttrName");
		String fileType = fileName.split("\\.")[1];
		String id = dcRequest.getParameter("id");
		try {
			if (isDMSIntegrationEnabled.equalsIgnoreCase("true")) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("documentId", id);
				result = HelperMethods.callApi(dcRequest, params, HelperMethods.getHeaders(dcRequest),
						URLConstants.DOCUMENT_STORAGE_DOWNLOAD);
				base64 = result.getParamValueByName("documentContent");

			} else {
				String olbClaimsToken = dcRequest.getParameter("Auth_Token");
				dcRequest.addRequestParam_(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, olbClaimsToken);
				dcRequest.getHeaderMap().put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION, olbClaimsToken);
				HashMap<String, Object> headerParams = (HashMap<String, Object>) dcRequest.getHeaderMap();

				String hostURL = URLFinder.getPathUrl(URLConstants.DBP_HOST_URL, dcRequest);
				if (StringUtils.isBlank(hostURL)) {
					alert.prepareError("Error while fetching DBP_HOST_URL").log();
					result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY,
							String.valueOf(ErrorCodeEnum.ERR_28010.getErrorCode())));
					result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28010.getMessage()));
					return result;
				}
				String url = hostURL + "/services/data/v1/MessageBinary/binary/media?id=" + id + "&fieldName="
						+ fieldName;
				DBXResult response = HTTPOperations.sendHttpRequest(HTTPOperations.operations.GET, url, "",
						headerParams);
				JsonObject jsonObject;
				jsonObject = new JsonParser().parse((String) response.getResponse()).getAsJsonObject();
				if (jsonObject.has("opstatus") && !jsonObject.get("opstatus").getAsString().equals("0")) {
					alert.prepareError("Error while fetching base64 content").log();
					result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY,
							String.valueOf(ErrorCodeEnum.ERR_26003.getErrorCode())));
					result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_26003.getMessage()));
					return result;
				}
				base64 = jsonObject.get("data").getAsString();
				base64 = new String(Base64.getDecoder().decode(base64));
			}
			byte[] bytes = Base64.getMimeDecoder().decode(base64);

			if (fileType.equals("pdf") || fileType.equals("application/pdf"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/pdf"));
			else if (fileType.equals("jpeg") || fileType.equals("image/jpeg"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "image/jpeg"));
			else if (fileType.equals("jpg") || fileType.equals("image/jpg"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "image/jpeg"));
			else if (fileType.equals("png") || fileType.equals("image/png"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "image/png"));
			else if (fileType.equals("txt") || fileType.equals("text/plain"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "text/plain"));
			else if (fileType.equals("doc") || fileType.equals("application/msword"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/msword"));
			else if (fileType.equals("docx")
					|| fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName,
						"aapplication/vnd.openxmlformats-officedocument.wordprocessingml.document"));
			else if (fileType.equals("csv") || fileType.equals("text/csv"))
				dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "text/csv"));
			try {
				dcResponse.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
						new BufferedHttpEntity(new ByteArrayEntity(bytes)));
			} catch (IOException exception) {
				alert.prepareError("Error while downloading file", exception).log();
				result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY,
						String.valueOf(ErrorCodeEnum.ERR_26003.getErrorCode())));
				result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_26003.getMessage()));
				return result;
			}
			dcResponse.setStatusCode(HttpStatus.SC_OK);
		} catch (Exception exception) {
			alert.prepareError("Error while downloading file", exception).log();
			result.addParam(
					new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_26003.getErrorCode())));
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_26003.getMessage()));
			return result;
		}
		return result;
	}

	private Map<String, String> getCustomHeaders(String filename, String contentType) {
		Map<String, String> customHeaders = new HashMap<>();
		customHeaders.put(HttpHeaders.CONTENT_TYPE, contentType);
		customHeaders.put("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		return customHeaders;
	}
}
