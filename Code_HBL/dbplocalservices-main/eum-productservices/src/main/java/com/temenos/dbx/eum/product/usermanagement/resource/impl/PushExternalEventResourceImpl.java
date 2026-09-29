package com.temenos.dbx.eum.product.usermanagement.resource.impl;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.PushExternalEventBusinessDelegate;
import com.temenos.dbx.eum.product.usermanagement.resource.api.PushExternalEventResource;

public class PushExternalEventResourceImpl implements PushExternalEventResource {
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    LoggerUtil logger = new LoggerUtil(PushExternalEventResourceImpl.class);

    @Override
    public Result pushUserIdAndActivationCode(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException {
        final String INPUT_USERID = "userId";
        final String INPUT_ACTIVATIONCODE = "activationCode";
        final String EVENTCODE = "SCA_ACTIVATIONCODE";
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

            String userId = StringUtils.isNotBlank(inputParams.get(INPUT_USERID)) ? inputParams.get(INPUT_USERID)
                    : request.getParameter(INPUT_USERID);
            String activationCode = StringUtils.isNotBlank(inputParams.get(INPUT_ACTIVATIONCODE))
                    ? inputParams.get(INPUT_ACTIVATIONCODE)
                    : request.getParameter(INPUT_ACTIVATIONCODE);
            if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(activationCode)) {
                PushExternalEventBusinessDelegate businessDelegate =
                        DBPAPIAbstractFactoryImpl.getBusinessDelegate(PushExternalEventBusinessDelegate.class);
                JsonObject json = new JsonObject();
                json.addProperty("userId", userId);
                json.addProperty("activationCode", activationCode);
                boolean status = businessDelegate.pushExternalEvent(EVENTCODE, json.toString(), request.getHeaderMap());
                if (!status) {
                    ErrorCodeEnum.ERR_10346.setErrorCode(result);
                }

            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10346);

        }
        return result;
    }

}
