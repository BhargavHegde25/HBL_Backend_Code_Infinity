package com.temenos.auth.security.resource.impl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.BundleConfigurationHandler;
import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.JSONUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.exceptions.MiddlewareException;
import com.temenos.auth.security.businessdelegate.api.CaptchaBusinessDelegate;
import com.temenos.auth.security.resource.api.CaptchaResource;
import com.temenos.dbx.mfa.businessdelegate.api.MFAServiceBusinessDelegate;
import com.temenos.dbx.mfa.dto.MFAServiceDTO;
import com.temenos.dbx.product.dto.CaptchaDTO;
import com.temenos.dbx.product.dto.CustomerCommunicationDTO;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.usermanagement.businessdelegate.api.UserManagementBusinessDelegate;
import com.temenos.dbx.product.utils.InfinityConstants;

public class CaptchaResourceImpl implements CaptchaResource {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Result getEncodedImage(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        try {
            CaptchaBusinessDelegate captchaBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(CaptchaBusinessDelegate.class);

            MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);

            int captchaLength = getCaptchaLength(dcRequest);

            CaptchaDTO dto = captchaBD.getEncodedImage(captchaLength);

            if (null == dto || StringUtils.isBlank(dto.getEncodedCaptcha())
                    || StringUtils.isBlank(dto.getCaptchaValue())) {
                alert.prepareError("generated encoded image or encoded value as blank").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10342);
            }

            MFAServiceDTO mfaserviceDTO = new MFAServiceDTO();
            mfaserviceDTO.setIsVerified("false");
            String encrptedPayload = captchaBD.generateEncodedCaptchaPayload(dto);
            mfaserviceDTO.setPayload(encrptedPayload);
            mfaserviceDTO = mfaserviceBD.create(mfaserviceDTO, dcRequest.getHeaderMap());
            if (null == mfaserviceDTO || StringUtils.isBlank(mfaserviceDTO.getServiceKey())) {
                alert.prepareError("Error occured while creating mfaservice").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10342);
            }

            result.addParam(new Param("serviceKey", mfaserviceDTO.getServiceKey(), DBPUtilitiesConstants.STRING_TYPE));
            result.addParam(new Param("encodedImage", dto.getEncodedCaptcha(), DBPUtilitiesConstants.STRING_TYPE));
            return result;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10342);
        }
    }

    private int getCaptchaLength(DataControllerRequest dcRequest) {
        int captchaLength = DBPUtilitiesConstants.DEFAULT_CAPTCHA_LENGTH;
        Map<String, String> configurations = BundleConfigurationHandler
                .fetchBundleConfigurations(BundleConfigurationHandler.BUDLENAME_C360, dcRequest);
        String captchaLengthString =
                configurations.get(BundleConfigurationHandler.CAPTCHA_LENGTH);
        if (StringUtils.isNotBlank(captchaLengthString)) {
            try {
                captchaLength = Integer.parseInt(captchaLengthString);
            } catch (NumberFormatException e) {
                alert.prepareError(e.getMessage()).log();
                captchaLength = DBPUtilitiesConstants.DEFAULT_CAPTCHA_LENGTH;
            }
        }
        return captchaLength;
    }

    @Override
    public Result verifyCaptcha(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUTPARAM_CAPTCHAVALUE = "captchaValue";
        final String INPUTPARAM_SERVICEKEY = "serviceKey";
        final String MFASERVICE_PAYLOAD_CAPTCHA = "captchaValue";
        String encryptedPayload;
        String storedCaptchaValue;
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String serviceKey = StringUtils.isNotBlank(inputParams.get(INPUTPARAM_SERVICEKEY))
                    ? inputParams.get(INPUTPARAM_SERVICEKEY)
                    : dcRequest.getParameter(INPUTPARAM_SERVICEKEY);
            String captchaValue = StringUtils.isNotBlank(inputParams.get(INPUTPARAM_CAPTCHAVALUE))
                    ? inputParams.get(INPUTPARAM_CAPTCHAVALUE)
                    : dcRequest.getParameter(INPUTPARAM_CAPTCHAVALUE);

            if (StringUtils.isBlank(serviceKey) || StringUtils.isBlank(captchaValue)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10343);
            }

            MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);
            MFAServiceDTO dto = new MFAServiceDTO();
            dto.setServiceKey(serviceKey);

            List<MFAServiceDTO> dtoList = mfaserviceBD.getMfaService(dto, null, null);
            if (null == dtoList || dtoList.isEmpty() || StringUtils.isBlank(dtoList.get(0).getServiceKey())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10344);
            }

            dto = dtoList.get(0);
            encryptedPayload = dto.getPayload();

            if (StringUtils.isBlank(encryptedPayload)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10344);
            }

            String payloadString = CryptoText.decrypt(encryptedPayload);
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(payloadString).isJsonObject() ? parser.parse(payloadString).getAsJsonObject()
                    : null;
            if (JSONUtil.isJsonNull(json) || !JSONUtil.hasKey(json, MFASERVICE_PAYLOAD_CAPTCHA)
                    || !json.get(MFASERVICE_PAYLOAD_CAPTCHA).isJsonPrimitive()
                    || StringUtils.isBlank(json.get(MFASERVICE_PAYLOAD_CAPTCHA).getAsString())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10344);
            }

            storedCaptchaValue = json.get(MFASERVICE_PAYLOAD_CAPTCHA).getAsString();
            CaptchaBusinessDelegate captchaBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(CaptchaBusinessDelegate.class);

            if (captchaBD.verifyCaptchaValue(captchaValue, storedCaptchaValue, true)) {
                MFAServiceDTO mfaDTO = new MFAServiceDTO();
                mfaDTO.setServiceKey(serviceKey);
                mfaDTO.setIsVerified("true");
                mfaserviceBD.updateMfaService(mfaDTO, dcRequest.getHeaderMap());
            } else {
                int captchaLength = getCaptchaLength(dcRequest);
                CaptchaDTO captchaDTO = captchaBD.getEncodedImage(captchaLength);
                String encrptedPayload = captchaBD.generateEncodedCaptchaPayload(captchaDTO);
                dto = new MFAServiceDTO();
                dto.setServiceKey(serviceKey);
                dto.setPayload(encrptedPayload);
                mfaserviceBD.updateMfaService(dto, dcRequest.getHeaderMap());
                result.addParam(
                        new Param("encodedImage", captchaDTO.getEncodedCaptcha(), DBPUtilitiesConstants.STRING_TYPE));
                ErrorCodeEnum.ERR_10345.setErrorCode(result);
            }
            return result;
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10347);
        }
    }

    @Override
    public Result getEncodedImageForRetailUserEnrollment(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        Result result = new Result();
        try {
            CaptchaBusinessDelegate captchaBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(CaptchaBusinessDelegate.class);

            MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);

            int captchaLength = getCaptchaLength(dcRequest);

            CaptchaDTO dto = captchaBD.getEncodedImage(captchaLength);

            if (null == dto || StringUtils.isBlank(dto.getEncodedCaptcha())
                    || StringUtils.isBlank(dto.getCaptchaValue())) {
                alert.prepareError("generated encoded image or encoded value as blank").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10807);
            }

            MFAServiceDTO mfaserviceDTO = new MFAServiceDTO();
            mfaserviceDTO.setIsVerified("false");
            String encrptedPayload = captchaBD.generateEncodedCaptchaPayloadForRetailUserEnrollment(dto);
            mfaserviceDTO.setPayload(encrptedPayload);
            mfaserviceDTO = mfaserviceBD.create(mfaserviceDTO, dcRequest.getHeaderMap());
            if (null == mfaserviceDTO || StringUtils.isBlank(mfaserviceDTO.getServiceKey())) {
                alert.prepareError("Error occured while creating mfaservice").log();
                throw new ApplicationException(ErrorCodeEnum.ERR_10807);
            }

            result.addParam(new Param("serviceKey", mfaserviceDTO.getServiceKey(), DBPUtilitiesConstants.STRING_TYPE));
            result.addParam(new Param("encodedImage", dto.getEncodedCaptcha(), DBPUtilitiesConstants.STRING_TYPE));
            return result;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10807);
        }
    }

    @Override
    public Result verifyCaptchaForEnrollment(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUTPARAM_CAPTCHAVALUE = "captchaValue";
        final String INPUTPARAM_SERVICEKEY = "serviceKey";
        final String MFASERVICE_PAYLOAD_CAPTCHA = "captchaValue";
        String encryptedPayload;
        String storedCaptchaValue;
        Result result = new Result();
        try {
            Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
            String serviceKey = StringUtils.isNotBlank(inputParams.get(INPUTPARAM_SERVICEKEY))
                    ? inputParams.get(INPUTPARAM_SERVICEKEY)
                    : dcRequest.getParameter(INPUTPARAM_SERVICEKEY);
            String captchaValue = StringUtils.isNotBlank(inputParams.get(INPUTPARAM_CAPTCHAVALUE))
                    ? inputParams.get(INPUTPARAM_CAPTCHAVALUE)
                    : dcRequest.getParameter(INPUTPARAM_CAPTCHAVALUE);

            if (StringUtils.isBlank(serviceKey) || StringUtils.isBlank(captchaValue)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10808);
            }

            MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(MFAServiceBusinessDelegate.class);
            MFAServiceDTO dto = new MFAServiceDTO();
            dto.setServiceKey(serviceKey);

            List<MFAServiceDTO> dtoList = mfaserviceBD.getMfaService(dto, null, null);
            if (null == dtoList || dtoList.isEmpty() || StringUtils.isBlank(dtoList.get(0).getServiceKey())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10809);
            }

            dto = dtoList.get(0);
            encryptedPayload = dto.getPayload();

            if (StringUtils.isBlank(encryptedPayload)) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10809);
            }

            String payloadString = CryptoText.decrypt(encryptedPayload);
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(payloadString).isJsonObject() ? parser.parse(payloadString).getAsJsonObject()
                    : null;
            if (JSONUtil.isJsonNull(json) || !JSONUtil.hasKey(json, MFASERVICE_PAYLOAD_CAPTCHA)
                    || !json.get(MFASERVICE_PAYLOAD_CAPTCHA).isJsonPrimitive()
                    || StringUtils.isBlank(json.get(MFASERVICE_PAYLOAD_CAPTCHA).getAsString())
                    || StringUtils.isBlank(JSONUtil.getString(json, DBPUtilitiesConstants.IS_RETAILUSER_ENROLLEMENT))) {
                throw new ApplicationException(ErrorCodeEnum.ERR_10809);
            }

            storedCaptchaValue = json.get(MFASERVICE_PAYLOAD_CAPTCHA).getAsString();
            CaptchaBusinessDelegate captchaBD = DBPAPIAbstractFactoryImpl.getInstance()
                    .getFactoryInstance(BusinessDelegateFactory.class)
                    .getBusinessDelegate(CaptchaBusinessDelegate.class);

            if (captchaBD.verifyCaptchaValue(captchaValue, storedCaptchaValue, true)) {
                MFAServiceDTO mfaDTO = new MFAServiceDTO();
                mfaDTO.setServiceKey(serviceKey);
                mfaDTO.setIsVerified("true");
                mfaserviceBD.updateMfaService(mfaDTO, dcRequest.getHeaderMap());
            } else {
                int captchaLength = getCaptchaLength(dcRequest);
                CaptchaDTO captchaDTO = captchaBD.getEncodedImage(captchaLength);
                String encrptedPayload = captchaBD.generateEncodedCaptchaPayloadForRetailUserEnrollment(captchaDTO);
                dto = new MFAServiceDTO();
                dto.setServiceKey(serviceKey);
                dto.setPayload(encrptedPayload);
                mfaserviceBD.updateMfaService(dto, dcRequest.getHeaderMap());
                result.addParam(
                        new Param("encodedImage", captchaDTO.getEncodedCaptcha(), DBPUtilitiesConstants.STRING_TYPE));
                ErrorCodeEnum.ERR_10810.setErrorCode(result);
            }
            return result;
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorCodeEnum());
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10811);
        }
    }

    @Override
    public Result deleteServiceKeyBasedOnCaptchaValue(String methodID, Object[] inputArray,
            DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
        final String INPUTPARAM_CAPTCHAVALUE = "captchaValue";
        final String INPUTPARAM_SERVICEKEY = "serviceKey";
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        String serviceKey = StringUtils.isNotBlank(inputParams.get(INPUTPARAM_SERVICEKEY))
                ? inputParams.get(INPUTPARAM_SERVICEKEY)
                : dcRequest.getParameter(INPUTPARAM_SERVICEKEY);
        String captchaValue = StringUtils.isNotBlank(inputParams.get(INPUTPARAM_CAPTCHAVALUE))
                ? inputParams.get(INPUTPARAM_CAPTCHAVALUE)
                : dcRequest.getParameter(INPUTPARAM_CAPTCHAVALUE);

        if (StringUtils.isBlank(serviceKey) || StringUtils.isBlank(captchaValue)) {
            throw new ApplicationException(ErrorCodeEnum.ERR_10808);

        }
        MFAServiceBusinessDelegate mfaserviceBD = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class)
                .getBusinessDelegate(MFAServiceBusinessDelegate.class);
        MFAServiceDTO dto = new MFAServiceDTO();
        dto.setServiceKey(serviceKey);
        mfaserviceBD.deleteMfaService(serviceKey, dcRequest.getHeaderMap());
        return new Result();
    }
    
    @Override
    public Result verifyCustomer(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws JSONException, UnsupportedEncodingException, DBPApplicationException, MiddlewareException {

        Result result = new Result();
        Map<String, String> map = HelperMethods.getInputParamMap(inputArray);
        String serviceKey = StringUtils.isNotBlank(map.get("serviceKey")) ? map.get("serviceKey")
                : dcRequest.getParameter("serviceKey");
        
		Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
		inputParams.put("serviceKey", serviceKey);
		String serviceId = "eumProductServices";
		String operationName = "verifyDbxUser"; 
		diagnostic.debug("HBL:CaptchaResourceImpl:verifyCustomer:dcRequest.getHeaderMAp():"+dcRequest.getHeaderMap());
		 String authHeader = dcRequest.getHeaderMap().get("X-Kony-Authorization").toString();
		 diagnostic.debug("HBL:CaptchaResourceImpl:verifyCustomer:authHeader:"+authHeader);
         JsonObject json = ServiceCallHelper.invokeServiceAndGetJson(serviceId, null, 
			   operationName,inputParams, dcRequest.getHeaderMap(), 
			   dcRequest.getHeaderMap().get("X-Kony-Authorization").toString());
		result = JSONToResult.convert(json.toString());
        return result;
    }


}
