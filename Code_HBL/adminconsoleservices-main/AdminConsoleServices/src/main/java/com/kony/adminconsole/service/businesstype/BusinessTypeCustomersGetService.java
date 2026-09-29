package com.kony.adminconsole.service.businesstype;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class BusinessTypeCustomersGetService implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        try {
            Result processedResult = new Result();
            requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);
            Map<String, String> postParametersMap = new HashMap<String, String>();
            String businessType_id = requestInstance.getParameter("id");
            if (StringUtils.isBlank(businessType_id)) {
            	Param result_param = new Param("Invalid", "id cannot be null", FabricConstants.STRING);
				processedResult.addParam(result_param);
				return processedResult;
            }            
            postParametersMap.put(ODataQueryConstants.FILTER,
                    "BusinessType_id eq '" + businessType_id + "'");
            String readBusinessTypeCustomersResponse = Executor.invokeService(ServiceURLEnum.CUSTOMERBUSINESSTYPE_READ,
                    postParametersMap, null, requestInstance);
            JSONObject readBusinessTypeCustomersResponseJSON = CommonUtilities
                    .getStringAsJSONObject(readBusinessTypeCustomersResponse);

            if (readBusinessTypeCustomersResponseJSON != null && readBusinessTypeCustomersResponseJSON.has(FabricConstants.OPSTATUS)
                    && readBusinessTypeCustomersResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                JSONArray readBusinessTypeCustomerIdsJSONArray = readBusinessTypeCustomersResponseJSON
                        .getJSONArray("customerbusinesstype");
                Dataset GroupsDataSet = new Dataset();
                GroupsDataSet.setId("BusinessTypeCustomers");
                for (int indexVar = 0; indexVar < readBusinessTypeCustomerIdsJSONArray.length(); indexVar++) {
                    JSONObject currServiceJSONObject = readBusinessTypeCustomerIdsJSONArray.getJSONObject(indexVar);
                    Record currRecord = new Record();
                    Param Group_id_Param = new Param("BusinessType_id", currServiceJSONObject.getString("BusinessType_id"),
                            FabricConstants.STRING);
                    currRecord.addParam(Group_id_Param);

                    Map<String, String> postParametersMap1 = new HashMap<String, String>();
                    postParametersMap1.put(ODataQueryConstants.FILTER,
                            "id eq '" + Group_id_Param.getValue().toString() + "'");
                    String readCustomerResponse = Executor.invokeService(ServiceURLEnum.CUSTOMER_READ,
                            postParametersMap1, null, requestInstance);
                    JSONObject readCustomerResponseJSON = CommonUtilities.getStringAsJSONObject(readCustomerResponse);
                    if (readCustomerResponseJSON != null && readCustomerResponseJSON.has(FabricConstants.OPSTATUS)
                            && readCustomerResponseJSON.getInt(FabricConstants.OPSTATUS) == 0) {
                        JSONArray readCustomerJSONArray = readCustomerResponseJSON.getJSONArray("customer");
                        if (readCustomerJSONArray.length() != 0) {
                            JSONObject currCustomerJSONObject = readCustomerJSONArray.getJSONObject(0);
                            String FullName_String = "";
                            if (currCustomerJSONObject.has("FirstName")) {
                                FullName_String = currCustomerJSONObject.getString("FirstName");
                            }
                            if (currCustomerJSONObject.has("LastName")) {
                                FullName_String += currCustomerJSONObject.getString("LastName");
                            }
                            Param FullName_Param = new Param("FullName", FullName_String, FabricConstants.STRING);
                            currRecord.addParam(FullName_Param);
                            if (currCustomerJSONObject.has("UserName")) {
                                Param Username_Param = new Param("Username",
                                        currCustomerJSONObject.getString("UserName"), FabricConstants.STRING);
                                currRecord.addParam(Username_Param);
                            } else {
                                Param Username_Param = new Param("Username", "", FabricConstants.STRING);
                                currRecord.addParam(Username_Param);
                            }
                            if (currCustomerJSONObject.has("Status_id")) {
                                Param Status_id_Param = new Param("Status_id",
                                        currCustomerJSONObject.getString("Status_id"), FabricConstants.STRING);
                                currRecord.addParam(Status_id_Param);
                            } else {
                                Param Status_id_Param = new Param("Status_id", "", FabricConstants.STRING);
                                currRecord.addParam(Status_id_Param);
                            }
                            if (currCustomerJSONObject.has("modifiedby")) {
                                Param UpdatedBy_Param = new Param("UpdatedBy",
                                        currCustomerJSONObject.optString("modifiedby"), FabricConstants.STRING);
                                currRecord.addParam(UpdatedBy_Param);
                            } else {
                                Param UpdatedBy_Param = new Param("UpdatedBy", "", FabricConstants.STRING);
                                currRecord.addParam(UpdatedBy_Param);
                            }
                            if (currCustomerJSONObject.has("lastmodifiedts")) {
                                Param UpdatedOn_Param = new Param("UpdatedOn",
                                        currCustomerJSONObject.getString("lastmodifiedts"), FabricConstants.STRING);
                                currRecord.addParam(UpdatedOn_Param);
                            } else {
                                Param UpdatedOn_Param = new Param("UpdatedOn", "", FabricConstants.STRING);
                                currRecord.addParam(UpdatedOn_Param);
                            }

                        } else {
                            Param FullName_Param = new Param("FullName", "", FabricConstants.STRING);
                            currRecord.addParam(FullName_Param);
                            Param Username_Param = new Param("Username", "", FabricConstants.STRING);
                            currRecord.addParam(Username_Param);
                            Param Status_id_Param = new Param("Status_id", "", FabricConstants.STRING);
                            currRecord.addParam(Status_id_Param);
                            Param UpdatedBy_Param = new Param("UpdatedBy", "", FabricConstants.STRING);
                            currRecord.addParam(UpdatedBy_Param);
                            Param UpdatedOn_Param = new Param("UpdatedOn", "", FabricConstants.STRING);
                            currRecord.addParam(UpdatedOn_Param);
                        }
                    } else {
                        ErrorCodeEnum.ERR_20426.setErrorCode(processedResult);
                        return processedResult;
                    }
                    GroupsDataSet.addRecord(currRecord);
                }

                processedResult.addDataset(GroupsDataSet);
                return processedResult;
            }
            ErrorCodeEnum.ERR_20426.setErrorCode(processedResult);
            return processedResult;
        } catch (Exception e) {
            Result errorResult = new Result();
            diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
            ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
            return errorResult;
        }
    }

}