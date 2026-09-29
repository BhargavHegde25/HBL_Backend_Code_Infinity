package com.kony.alertslogservices.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.alertslogservices.core.AbstractLogJavaService;
import com.kony.alertslogservices.core.BaseActivity;
import com.kony.alertslogservices.dtoclasses.AlertActivityDTO;
import com.kony.alertslogservices.util.ArchivalConstants;
import com.kony.alertslogservices.util.Helpermethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class AlertsLogArchiveService extends AbstractLogJavaService {

	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	private static final Map<String, Class<? extends BaseActivity>> METHODID_TO_LOGCLASS_MAPPER = mapMethodIdToLogClass();

	@Override
	public Object execute(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) {
		try {
			String logRetentionPeriodStrinmonths = Helpermethods
					.getConfigProperty(ArchivalConstants.LOG_RETENTION_PERIOD_IN_MONTHS);
			if (logRetentionPeriodStrinmonths == null || logRetentionPeriodStrinmonths.equals("")) {
				diagnostic.prepareDebug(ArchivalConstants.LOG_RETENTION_PERIOD_IN_MONTHS + "parameter is not configured.").log();
				return Helpermethods.returnResult(false,
						ArchivalConstants.LOG_RETENTION_PERIOD_IN_MONTHS + "parameter is not configured.");
			}

			Integer logRetentionPeriodinmonths = Integer.parseInt(logRetentionPeriodStrinmonths);
			String archiveBefore = Helpermethods.calculateArchivingDate(logRetentionPeriodinmonths);
			AlertsLogArchiveDAO.archiveLogs(METHODID_TO_LOGCLASS_MAPPER.get(methodID), archiveBefore);
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception in LogArchiveService", e).log();
			return Helpermethods.returnResult(false, e.getMessage());
		}
		return Helpermethods.returnResult(true, "");
	}

	private static Map<String, Class<? extends BaseActivity>> mapMethodIdToLogClass() {
		Map<String, Class<? extends BaseActivity>> map = new HashMap<>();
		map.put("archiveAlertActivityData", AlertActivityDTO.class);
		return map;
	}

}
