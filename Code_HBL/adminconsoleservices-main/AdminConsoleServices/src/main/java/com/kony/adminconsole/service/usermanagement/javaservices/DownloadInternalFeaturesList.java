package com.kony.adminconsole.service.usermanagement.javaservices;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.ResourceFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.service.usermanagement.resource.api.UserFeatureResource;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DownloadInternalFeaturesList implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();

		Result finalresult = new Result();

		try {

			String authToken = CommonUtilities.getAuthToken(requestInstance);
			if(StringUtils.isBlank(authToken)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_20000);
			}

			Result result = null;
			try {
				UserFeatureResource userResource = DBPAPIAbstractFactoryImpl.getInstance()
						.getFactoryInstance(ResourceFactory.class).getResource(UserFeatureResource.class);
				result = userResource.getInternalUserFeatures(methodID, inputArray, requestInstance,
						responseInstance);
			} catch (Exception e) {
				alert.prepareError("Caught exception at invoke of getInternalUserFeatures: ", e).log();
				return ErrorCodeEnum.ERR_22152.setErrorCode(new Result());
			}

			if(null != result ) {

				StringBuilder responseCsvBuilder = new StringBuilder();
				CSVPrinter responseCsvPrinter = CSVFormat.DEFAULT
						.withHeader("Name", "Code", "Description", "NumberOfActions", "Status")
						.print(responseCsvBuilder);


				Dataset dataset = result.getDatasetById("features");
				List<Record> recodList = new ArrayList<>();
				if(null != dataset) {
					recodList = dataset.getAllRecords();
				}
				dataset.getAllRecords();
				
				for (Record rec : recodList) {

					String nameColumn = rec.getParamValueByName("name");
					String codeColumn = rec.getParamValueByName("id");
					
					String descColumn = StringUtils.EMPTY;
					try {
						descColumn = rec.getRecordById("featureDisplayName")
					.getRecordById("en-US").getParamValueByName("displayDescription");
					}catch(Exception exp) {
						
					}
					
					String actionsColumn = rec.getParamValueByName("numberOfActions");
					String statusColumn = rec.getParamValueByName("Status_id");
					if(StringUtils.isNotBlank(statusColumn)) {

						if(statusColumn.equalsIgnoreCase("SID_FEATURE_ACTIVE")) {
							statusColumn= "Active";
						}else if(statusColumn.equalsIgnoreCase("SID_FEATURE_INACTIVE")) {
							statusColumn= "Inactive";
						}
					}

					responseCsvPrinter.printRecord(nameColumn, codeColumn, descColumn,actionsColumn, statusColumn);

				}

				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.EMPLOYEEFEATURES, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.SUCCESSFUL, "Features file download successful");

				Map<String, String> customHeaders = new HashMap<String, String>();
				customHeaders.put("Content-Type", "text/plain; charset=utf-8");
				customHeaders.put("Content-Disposition", "attachment; filename=\"EmployeeFeatures_List.csv\"");

				responseInstance.setAttribute(FabricConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(
						new StringEntity(responseCsvBuilder.toString(), StandardCharsets.UTF_8)));
				responseInstance.getHeaders().putAll(customHeaders);
				responseInstance.setStatusCode(HttpStatus.SC_OK);
			} else {
				AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.EMPLOYEEFEATURES, EventEnum.DOWNLOADFILE,
						ActivityStatusEnum.FAILED, "Internal User Features file download failed");
			}

		} catch (Exception e) {
			alert.prepareError("Failed while downloading features list", e).log();
			ErrorCodeEnum.ERR_20687.setErrorCode(finalresult);

			String errorMessage = "Failed to download Internal Userfeatures list. Please contact administrator.";
			CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
		}
		return finalresult;
	}
}