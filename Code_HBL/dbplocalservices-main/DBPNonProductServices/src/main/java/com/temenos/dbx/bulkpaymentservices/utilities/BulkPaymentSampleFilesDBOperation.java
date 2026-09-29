package com.temenos.dbx.bulkpaymentservices.utilities;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONException;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.temenos.dbx.product.constants.ServiceId;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Result;

public class BulkPaymentSampleFilesDBOperation {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static final String SCHEMA_NAME = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME");
	public static final String DB_BULKPAYMENTSAMPLEFILES_GET = SCHEMA_NAME + "_bulkpaymentsamplefiles_get";
	public static final String SAMPLE_FILES = "Sample_Files";
	
	
	public Result getBulkPaymentSampleFiles() {
		
		String serviceName = ServiceId.DBPRBLOCALSERVICEDB;
		String operationName = DB_BULKPAYMENTSAMPLEFILES_GET;

		Result result;
		try {
			result = DBPServiceExecutorBuilder.builder()
					.withServiceId(serviceName)
					.withObjectId(null)
					.withOperationId(operationName)
					.withRequestParameters(null)
					.build().getResult();
			
		} catch (JSONException je) {
			alert.prepareError("Failed to fetch bulk payment sample files", je).log();
			return ErrorCodeEnum.ERR_21218.setErrorCode(new Result());
		} catch (Exception e) {
			alert.prepareError("Caught exception at getBulkPaymentSampleFiles: ", e).log();
			return ErrorCodeEnum.ERR_21218.setErrorCode(new Result());
		}

		return result;
	}

}
