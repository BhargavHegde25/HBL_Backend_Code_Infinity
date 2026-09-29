package com.kony.adminconsole.service.locationsandlocationservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.handler.AuditHandler;
import com.kony.adminconsole.utilities.ActivityStatusEnum;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.EventEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ModuleNameEnum;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LocationsCSVGenerateService implements JavaService2{

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception{
		Result processedResult = new Result();
		
		try {
			String locationfileId = requestInstance.getParameter("locationfileId");
			
			Map<String, String> locationfileTableMap = new HashMap<String, String>();
            locationfileTableMap.put(ODataQueryConstants.SELECT, "id");
            locationfileTableMap.put(ODataQueryConstants.TOP, "1");

            String readLocationResponse = Executor.invokeService(ServiceURLEnum.LOCATIONFILE_READ,
                    locationfileTableMap, null, requestInstance);
            
            if(readLocationResponse == null) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_20000);
            }
            String fileId = Integer.toString(CommonUtilities.generateRandomWithRange(100000, 999999));
            processedResult.addParam("fileId", fileId);
            if (locationfileId != null && locationfileId.equals("locationTemplate")) {
            	MemoryManager.saveIntoCache("locationfileId", locationfileId,60);
            }
            else {
            	String locationfileclob = "";

                // ** Reading entry from 'locationfile' table **
                   locationfileTableMap.clear();
                   locationfileTableMap.put(ODataQueryConstants.SELECT, "locationfileclob");
                   locationfileTableMap.put(ODataQueryConstants.FILTER, "id eq '" + locationfileId + "'");

                   String readLocationfileResponse = Executor.invokeService(ServiceURLEnum.LOCATIONFILE_READ,
                           locationfileTableMap, null, requestInstance);
                   JSONObject readLocationfileJSON = CommonUtilities.getStringAsJSONObject(readLocationfileResponse);

                   if (readLocationfileJSON.getInt(FabricConstants.OPSTATUS) == 0
                           && readLocationfileJSON.getJSONArray("locationfile") != null) {
                       AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.DOWNLOADFILE,
                               ActivityStatusEnum.SUCCESSFUL, "CSV file downloaded successfully");
                       locationfileclob = readLocationfileJSON.getJSONArray("locationfile").getJSONObject(0)
                               .getString("locationfileclob");
                   } else {
                       AuditHandler.auditAdminActivity(requestInstance, ModuleNameEnum.LOCATIONS, EventEnum.DOWNLOADFILE,
                               ActivityStatusEnum.FAILED, "CSV file download failed");

                   }
                   
                   MemoryManager.saveIntoCache(fileId, locationfileclob,60);
                  
            }
            return processedResult;
		}catch (Exception e) {
            alert.prepareError("Exception while downloading Locations CSV file", e).log();
            ErrorCodeEnum.ERR_20687.setErrorCode(processedResult);

            String errorMessage = "Failed to download Locations CSV file. Please contact administrator.";
            CommonUtilities.fileDownloadFailure(responseInstance, errorMessage);
            return processedResult;
        }
		
	}
}
