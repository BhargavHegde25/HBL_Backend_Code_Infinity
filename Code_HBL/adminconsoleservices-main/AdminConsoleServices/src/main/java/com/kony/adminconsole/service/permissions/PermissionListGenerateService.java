package com.kony.adminconsole.service.permissions;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
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

public class PermissionListGenerateService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception{
		Result result = new Result();
		try {
			String searchText = requestInstance.getParameter("searchText");
			String status = requestInstance.getParameter("status");
			
			Map<String, String> permissionsViewMap = new HashMap<String, String>();
            permissionsViewMap.put(ODataQueryConstants.SELECT,
                    "Permission_Name, Permission_Desc, Role_Count, Users_Count, Status");

            StringBuilder filterString = new StringBuilder();

            if (StringUtils.isNotBlank(status)) {
                String[] statuses = status.split("_");
                filterString.append("(");
                for (int i = 0; i < statuses.length - 1; ++i) {
                    filterString.append("Status eq '" + statuses[i] + "'");
                    filterString.append(" or ");
                }
                filterString.append("Status eq '" + statuses[statuses.length - 1] + "')");
            }

            if (filterString != null) {
                permissionsViewMap.put(ODataQueryConstants.FILTER, filterString.toString());
            }

            String permissionsViewResponse = Executor.invokeService(ServiceURLEnum.PERMISSIONS_VIEW_READ,
                    permissionsViewMap, null, requestInstance);
            if(permissionsViewResponse == null) {
            	throw new ApplicationException(ErrorCodeEnum.ERR_20000);
            }
            JSONObject permissionsViewResponseJSON = CommonUtilities.getStringAsJSONObject(permissionsViewResponse);
            JSONArray permissions = new JSONArray();
            if (permissionsViewResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && permissionsViewResponseJSON.getJSONArray("permissions_view") != null) {
                 permissions = permissionsViewResponseJSON.getJSONArray("permissions_view");
            }
            String fileId = Integer.toString(CommonUtilities.generateRandomWithRange(100000, 999999));
            result.addParam("fileId", fileId);
            result.addParam("response", permissionsViewResponse);
            MemoryManager.saveIntoCache(fileId, permissionsViewResponse,60);
            MemoryManager.saveIntoCache("searchText", searchText);
            return result;
		}catch (ApplicationException e) {
            alert.prepareError(" ApplicationException while downloading permissions file", e).log();
            e.getErrorCodeEnum().setErrorCode(result);
            CommonUtilities.fileDownloadFailure(responseInstance, e.getErrorCodeEnum().getMessage());
            return result;
        }
	}
}
