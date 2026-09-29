package com.temenos.infinity.api.docmanagement.acctstatement.javaservices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.constants.Constants;

public class GetAdhocStatementDetails implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			HashMap<String, String> inputParams = (HashMap<String, String>) inputArray[1];
			//String userId = inputParams.get("userId");
			String userId = HelperMethods.getUserIdFromSession(request);
			String customerId = HelperMethods.getCustomerIdFromSession(request);
			diagnostic.prepareDebug("****************** GetAdhocStatementDetails inputParams :"+inputParams).log();
			diagnostic.prepareDebug("****************** GetAdhocStatementDetails userId :"+userId+" ******* customerId :"+customerId).log();
			if(!userId.equals(customerId)) {
				alert.prepareError("Not Authorized user").log();
				return ErrorCodeEnum.ERR_12403.setErrorCode(new Result());
			}
			HashMap<String, String> data = new HashMap<String, String>();
			data.put(Constants.$FILTER, "userId  eq " + userId +" and statementType eq ADHOC and legalEntityId" + DBPUtilitiesConstants.EQUAL +LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request));
			
			diagnostic.prepareDebug("************ GetAdhocStatementDetails data :"+data).log();
			Result getStatementData = HelperMethods.callApi(request, data, HelperMethods.getHeaders(request), URLConstants.ACCOUNTS_STATEMENT_FILES_GET);
			diagnostic.prepareDebug("************ GetAdhocStatementDetails getStatementData :"+ResultToJSON.convert(getStatementData)).log();
			Dataset accountStatementset = getStatementData.getDatasetById("accountsstatementfiles");
			Dataset resultDS = new Dataset("AdhocStatements");
			List<Record> recList = new ArrayList<Record>();
			for (int i=0; i<accountStatementset.getAllRecords().size();i++) {
				Record record = new Record();
				diagnostic.prepareInfo("Record is available for combined statement files").log();
				String fileId = accountStatementset.getRecord(i).getParamValueByName("id");
				String fileType = accountStatementset.getRecord(i).getParamValueByName("fileType");
				String status = accountStatementset.getRecord(i).getParamValueByName("status");
				String generatedDate = accountStatementset.getRecord(i).getParamValueByName("lastmodifiedts");
				String accountIds = accountStatementset.getRecord(i).getParamValueByName("accountIds");
				generatedDate = HelperMethods.convertDateFormat(generatedDate, "yyyy-MM-dd'T'hh:mm:ss'Z'");
				record.addParam("fileName", accountStatementset.getRecord(i).getParamValueByName("fileName"));
				record.addParam("status", status);
				record.addParam("generatedDate", generatedDate);
				record.addParam("fileType", fileType);
				record.addParam("fileId", fileId);
				record.addParam("accountIds", accountIds);
				if(!"InProgress".equalsIgnoreCase(status)) {
					record.addParam("inputPayload", accountStatementset.getRecord(i).getParamValueByName("inputPayload"));
				}				
				final int SIZE_OF_RANDOM_GENERATED_STRING = 10;
				String fileIdSec = HelperMethods.getUniqueNumericString(SIZE_OF_RANDOM_GENERATED_STRING);
				MemoryManager.saveIntoCache(fileIdSec + fileType, inputParams.get("codeCh"));
				MemoryManager.saveIntoCache(inputParams.get("codeCh") + fileType, fileId);
				record.addParam("fileIdSec", fileIdSec);
				recList.add(record);
			}
		    resultDS.addAllRecords(recList);
		    result.addDataset(resultDS);
		    return result;
		} catch (Exception e) {
			alert.prepareError("Error while fetching combined statement details").log();
			result.addParam(new Param(ErrorCodeEnum.ERROR_CODE_KEY, String.valueOf(ErrorCodeEnum.ERR_28031.getErrorCode())));
			result.addParam(new Param(ErrorCodeEnum.ERROR_MESSAGE_KEY, ErrorCodeEnum.ERR_28031.getMessage()));

		}
		diagnostic.prepareDebug("************ GetAdhocStatementDetails final result :"+ResultToJSON.convert(result)).log();
		return result;
	}
}
