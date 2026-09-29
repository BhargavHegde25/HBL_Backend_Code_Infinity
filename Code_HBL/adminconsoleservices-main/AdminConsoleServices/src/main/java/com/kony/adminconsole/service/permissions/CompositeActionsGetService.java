package com.kony.adminconsole.service.permissions;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.handler.ActionHandler;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Result;

/**
 * GetUserOrRoleCompositeActions service will fetch Role or User level composite actions
 * 
 * @author Rishi Gupta
 * 
 */
public class CompositeActionsGetService implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
        Result processedResult = new Result();
        try {
            String userID = requestInstance.getParameter("User_id");
            String roleID = requestInstance.getParameter("Role_id");
            String permissionID = requestInstance.getParameter("Permission_id");

            if (StringUtils.isBlank(permissionID)) {
                return ErrorCodeEnum.ERR_20743.setErrorCode(processedResult);
            }
            JSONObject response = ActionHandler.getAggregateCompositeActions(userID, roleID, permissionID,
                    requestInstance, "id,Action_id,Name,Description,isEnabled,Feature_id", "1");
            if (response.has("ErrorEnum")) {
                ErrorCodeEnum errorEnumType = (ErrorCodeEnum) response.get("ErrorEnum");
                return errorEnumType.setErrorCode(processedResult);
            }
            Dataset finalCompositeActionsDataset = CommonUtilities
                    .constructDatasetFromJSONArray(response.getJSONArray("CompositeActions"));
            finalCompositeActionsDataset.setId("CompositeActions");
            processedResult.addDataset(finalCompositeActionsDataset);
            return processedResult;
        } catch (Exception e) {
            return ErrorCodeEnum.ERR_20001.setErrorCode(processedResult);
        }
    }
}
