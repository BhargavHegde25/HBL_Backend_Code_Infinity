package com.kony.auditlogservices.service;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.auditlogservices.core.AbstractLogJavaService;
import com.kony.auditlogservices.core.BaseActivity;
import com.kony.auditlogservices.dtoclasses.AuditActivityDTO;
import com.kony.auditlogservices.dtoclasses.MoneyMovementLogDTO;
import com.kony.auditlogservices.util.ArchivalConstants;
import com.kony.auditlogservices.util.Helpermethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;

public class AuditActivityArchiveService extends AbstractLogJavaService {
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
			Date archiveBefore = Helpermethods.calculateArchivingDate(logRetentionPeriodinmonths);
			AuditActivityArchiveDAO.archiveLogs(METHODID_TO_LOGCLASS_MAPPER.get("archiveAuditActivityData"),
					archiveBefore);
			AuditActivityArchiveDAO.archiveLogs(METHODID_TO_LOGCLASS_MAPPER.get("MoneyMovementLog"), archiveBefore);
		} catch (Exception e) {
			diagnostic.prepareDebug("Exception in LogArchiveService", e).log();
			return Helpermethods.returnResult(false, e.getMessage());
		}
		return Helpermethods.returnResult(true, "");
	}

	private static Map<String, Class<? extends BaseActivity>> mapMethodIdToLogClass() {
		Map<String, Class<? extends BaseActivity>> map = new HashMap<>();
		map.put("archiveAuditActivityData", AuditActivityDTO.class);
		map.put("MoneyMovementLog", MoneyMovementLogDTO.class);
		return map;
	}
}
