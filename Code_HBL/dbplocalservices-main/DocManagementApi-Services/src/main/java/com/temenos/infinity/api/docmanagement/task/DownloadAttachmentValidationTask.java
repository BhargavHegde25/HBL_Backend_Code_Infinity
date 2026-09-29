package com.temenos.infinity.api.docmanagement.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.object.task.ObjectProcessorTask;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class DownloadAttachmentValidationTask implements ObjectProcessorTask {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean process(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager)
			throws Exception {
		if (!isDACEnabled()) {
			diagnostic.prepareDebug("data access control is disabled").log();
			return true;
		}
		diagnostic.prepareDebug("data access control is enabled").log();
		String userId = null;
		String fileID = null;
		boolean status = true;
		if (StringUtils.isNotBlank(fabricRequestManager.getQueryParamsHandler().getParameter("fileID"))
				&& StringUtils.isNotBlank(fabricRequestManager.getQueryParamsHandler().getParameter("Auth_Token"))) {
			userId = fabricRequestManager.getQueryParamsHandler().getParameter("customerId");
			fileID = fabricRequestManager.getQueryParamsHandler().getParameter("fileID");
			diagnostic.prepareDebug("userId from client:" + userId).log();
			
				JsonObject attachments = new JsonObject();
				Map<String, String> dataMapFilter = new HashMap<String, String>();
				Map<String, String> headersMap = new HashMap<String, String>();
				if (isDMSIntegrationEnabled()) {
					dataMapFilter.put("userId", userId);
					attachments = HelperMethods.callApiJson(fabricRequestManager, dataMapFilter, headersMap,
							URLConstants.DOCUMENT_STORAGE_SEARCH);
				} else {
					dataMapFilter.put(DBPUtilitiesConstants.FILTER, "paymentFileID" + DBPUtilitiesConstants.EQUAL + fileID);
					dataMapFilter.put(DBPUtilitiesConstants.SELECT, "paymentFileID");
					attachments = HelperMethods.callApiJson(fabricRequestManager, dataMapFilter, headersMap,
							URLConstants.PAYMENT_FILES_GET);
				}
				if (attachments.isJsonNull()
						|| (attachments.has("opstatus") && !attachments.get("opstatus").getAsString().equals("0"))) {
					status = false;
					diagnostic.prepareDebug("Attachments are null or returned opstatus is non-zero").log();
				} else if (attachments.has("opstatus") && attachments.get("opstatus").getAsString().equals("0")) {
					if (isDMSIntegrationEnabled()) {
						String documentsList = attachments.get("documentsList").toString();
						if (StringUtils.isBlank(documentsList)) {
							status = false;
							diagnostic.prepareDebug("File with given fileID is not available in retrieved attachments with opstatus as zero").log();
						}
					} else {
						JsonArray paymentFiles = attachments.get("paymentfiles").getAsJsonArray();
						if (paymentFiles.size() == 0) {
							status = false;
							diagnostic.prepareDebug("File with given fileID is not available in retrieved attachments with opstatus as zero").log();
						}
					}
				}
			
		} else {
			status = false;
			diagnostic.prepareDebug("One or more required input parameters are missing - Auth token, customerId, fileID").log();
		}
		if (!status) {
			JsonObject resPayload = null;
			if (HelperMethods.isJsonNotNull(fabricResponseManager.getPayloadHandler().getPayloadAsJson())) {
				resPayload = fabricResponseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
			}
			resPayload = ErrorCodeEnum.ERR_12403.setErrorCode(resPayload);
			fabricResponseManager.getPayloadHandler().updatePayloadAsJson(resPayload);
		}
		return status;
	}

	public boolean isDACEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDacEnabled = configurableParametersHelper.getServerProperty("DAC_ENABLED");
			return StringUtils.isBlank(isDacEnabled)
					|| BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DAC_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}

	public boolean isDMSIntegrationEnabled() {
		ServicesManager serviceManager;
		try {
			serviceManager = ServicesManagerHelper.getServicesManager();
			ConfigurableParametersHelper configurableParametersHelper = serviceManager
					.getConfigurableParametersHelper();
			String isDMSIntegrationEnabled = configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED");
			if(StringUtils.isBlank(isDMSIntegrationEnabled))
				return false;
			return BooleanUtils.toBoolean(configurableParametersHelper.getServerProperty("DMS_INTEGRATION_ENABLED"));
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return true;
	}
}
