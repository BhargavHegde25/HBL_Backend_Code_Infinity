package com.kony.adminconsole.service.locationsandlocationservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.MemoryManager;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class LocationsListGenerateService implements JavaService2{
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception{
		Result result = new Result();
		try {
			String searchText = requestInstance.getParameter("searchText");
			String status = requestInstance.getParameter("status");
			String type = requestInstance.getParameter("type");
			Map<String, String> locationViewMap = new HashMap<String, String>();
            locationViewMap.put(ODataQueryConstants.SELECT, "Name, Code, Description, PhoneNumber, Type_id, Status_id");

            StringBuilder filterString = new StringBuilder();

            if (StringUtils.isNotBlank(type)) {
                String[] types = type.split("_");
                filterString.append("(");
                for (int i = 0; i < types.length - 1; ++i) {
                    filterString.append("Type_id eq '" + types[i] + "'");
                    filterString.append(" or ");
                }
                filterString.append("Type_id eq '" + types[types.length - 1] + "')");
            }

            if (StringUtils.isNotBlank(status)) {
                if (filterString != null) {
                    filterString.append(" and ");
                }
                String[] statuses = status.split("_");
                filterString.append("(");
                for (int i = 0; i < statuses.length - 1; ++i) {
                    filterString.append("Status_id eq '" + statuses[i] + "'");
                    filterString.append(" or ");
                }
                filterString.append("Status_id eq '" + statuses[statuses.length - 1] + "')");
            }

            if (filterString != null) {
                locationViewMap.put(ODataQueryConstants.FILTER, filterString.toString());
            }

            String locationResponse = Executor.invokeService(ServiceURLEnum.LOCATION_VIEW_READ, locationViewMap, null,
                    requestInstance);
            if(locationResponse == null) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_20000);
            }
            JSONObject locationResponseJSON = CommonUtilities.getStringAsJSONObject(locationResponse);
            JSONArray locations = new JSONArray();
            if (locationResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && locationResponseJSON.getJSONArray("location_view") != null) {
            	locations = locationResponseJSON.getJSONArray("location_view");
            }
            String fileId = Integer.toString(CommonUtilities.generateRandomWithRange(100000, 999999));
            result.addParam("fileId", fileId);
            MemoryManager.saveIntoCache(fileId, locations.toString(),60);
            MemoryManager.saveIntoCache("searchText", searchText);
            return result;
		}catch (ApplicationException e) {
            alert.prepareError(" ApplicationException while downloading locations file", e).log();
            e.getErrorCodeEnum().setErrorCode(result);
            CommonUtilities.fileDownloadFailure(responseInstance, e.getErrorCodeEnum().getMessage());
            return result;
        }
	}
}
