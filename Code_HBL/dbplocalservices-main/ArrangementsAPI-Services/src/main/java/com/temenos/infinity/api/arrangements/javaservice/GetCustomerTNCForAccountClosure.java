package com.temenos.infinity.api.arrangements.javaservice;

import java.util.HashMap;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetCustomerTNCForAccountClosure implements JavaService2 {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        String termsAndConditionsCode = null;
        String language = DBPUtilitiesConstants.TNC_DEFAULT_LANGUAGE;
        String versionIdfromAdmin = null;
        String versionId = null;
        String filter = null;
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        
       
      

        if (StringUtils.isNotBlank(inputParams.get("languageCode"))) {
            language = inputParams.get("languageCode");
        }
        termsAndConditionsCode = inputParams.get("termsAndConditionsCode");

        if (StringUtils.isBlank(termsAndConditionsCode)) {
            ErrorCodeEnum.ERR_10184.setErrorCode(result);
            return result;
        }
        
       
        inputParams.put("appId", HelperMethods.getAppId(dcRequest));
        HelperMethods.removeNullValues(inputParams);
        Result termsAndConditionsFromAdmin =
                AdminUtil.invokeAPI(inputParams, URLConstants.GET_TERMS_AND_CONDITIONS, dcRequest);
        alert.prepareError("termsAndConditionsFromAdmin response 1" +termsAndConditionsFromAdmin).log();
        if (HelperMethods.hasDBPErrorMSG(termsAndConditionsFromAdmin)) {
            ErrorCodeEnum.ERR_10188.setErrorCode(result);
            return result;
        }
        Map<String, Object> params = new HashMap<>();
      params.clear();
      params.put("partyId", customerId);
   
        
        String authToken = AdminUtil.getForceLoginAccessToken();
       Result partyConsentDetails = ServiceCallHelper.invokeServiceAndGetResult(params,
          HelperMethods.getHeaders(dcRequest), URLConstants.GET_PARTY_CONSENT_DETAILS, authToken);

        if (partyConsentDetails != null) {
            Dataset ds = partyConsentDetails.getDatasetById("partyConsentDetails");
            if (ds != null && ds.getAllRecords().size() > 0) {
                String globalContentId = termsAndConditionsFromAdmin.getParamValueByName("contentId");
               for(Record record : ds.getAllRecords()) {
                    String contentId = record.getParamValueByName("termsNConditionContentId");
                   if (contentId != null && globalContentId != null && contentId.equalsIgnoreCase(globalContentId)
               ) {
                        result.addParam(new Param("alreadySigned", "true", DBPUtilitiesConstants.STRING_TYPE));
                      return result;
                    }
                }
            }
        }
        return termsAndConditionsFromAdmin;
    }
}
