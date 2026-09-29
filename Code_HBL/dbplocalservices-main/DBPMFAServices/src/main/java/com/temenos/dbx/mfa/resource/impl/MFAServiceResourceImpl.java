package com.temenos.dbx.mfa.resource.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonObject;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.mfa.MFAConstants;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.mfa.resource.api.MFAServiceResource;

public class MFAServiceResourceImpl implements MFAServiceResource {

    LoggerUtil logger = new LoggerUtil(MFAServiceResourceImpl.class);
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public Result createMFAServiceFromCommunication(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUT_PHONE = "phone";
        final String INPUT_EMAIL = "email";
        Result result = new Result();

        if (StringUtils.isNotBlank("canProceed") && "false".equalsIgnoreCase(dcRequest.getParameter("canProceed"))) {
            return result;
        }

        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

            String phone = StringUtils.isNotBlank(inputParams.get(INPUT_PHONE)) ? inputParams.get(INPUT_PHONE)
                    : dcRequest.getParameter(INPUT_PHONE);
            String email = StringUtils.isNotBlank(inputParams.get(INPUT_EMAIL)) ? inputParams.get(INPUT_EMAIL)
                    : dcRequest.getParameter(INPUT_EMAIL);

            if (StringUtils.isBlank(phone)) {
                phone = dcRequest.getAttribute(INPUT_PHONE);
            }
            if (StringUtils.isBlank(email)) {
                email = dcRequest.getAttribute(INPUT_EMAIL);
            }
            if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(email)) {
                MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                        .getFactoryInstance(BusinessDelegateFactory.class)
                        .getBusinessDelegate(MFAServiceBusinessDelegate.class);
                JsonObject requestPayload = formRequestPayloadJson(dcRequest);

                MFAServiceDTO dto = mfaserviceBD.createMfaService(MFAConstants.SERVICE_ID_PRELOGIN, phone, email,
                        requestPayload.toString(), dcRequest.getHeaderMap());
                String serviceKey = null != dto ? dto.getServiceKey() : null;
                if (StringUtils.isNotBlank(serviceKey)) {
                    result.addParam(new Param("serviceKey", serviceKey, DBPUtilitiesConstants.STRING_TYPE));
                    JsonObject object = new JsonObject();
                    object.addProperty("serviceKey", serviceKey);
                    // result.addParam(new Param(MFAConstants.MFA_ATTRIBUTES, object.toString(),
                    // DBPUtilitiesConstants.STRING_TYPE));
                } else {
                    ErrorCodeEnum.ERR_10235.setErrorCode(result);
                }

            } else {
                ErrorCodeEnum.ERR_10234.setErrorCode(result);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_10235);

        }
        return result;
    }

    private JsonObject formRequestPayloadJson(DataControllerRequest dcRequest) {
        JsonObject object = new JsonObject();
        for (Entry<String, String[]> entry : dcRequest.getSource().entrySet()) {
            String[] value = entry.getValue();
            object.addProperty(entry.getKey(), value[0]);
        }
        if (StringUtils.isNotBlank(dcRequest.getParameter(DBPUtilitiesConstants.ACCOUNTS))) {
            object.addProperty(DBPUtilitiesConstants.ACCOUNTS, dcRequest.getParameter(DBPUtilitiesConstants.ACCOUNTS));
        }
        if (StringUtils.isNotBlank(dcRequest.getParameter("companyInformation"))) {
            object.addProperty("companyInformation", dcRequest.getParameter("companyInformation"));
        }
        if (StringUtils.isNotBlank(dcRequest.getParameter("customerInformation"))) {
            object.addProperty("customerInformation", dcRequest.getParameter("customerInformation"));
        }
        return object;
    }

}