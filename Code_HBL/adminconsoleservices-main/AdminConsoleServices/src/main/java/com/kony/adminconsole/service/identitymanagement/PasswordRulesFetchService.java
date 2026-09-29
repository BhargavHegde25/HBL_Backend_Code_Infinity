package com.kony.adminconsole.service.identitymanagement;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Service to fetch password rules from the back end
 * 
 * @author Mohit Khosla (KH2356)
 */

public class PasswordRulesFetchService implements JavaService2 {

    public static final String PASSWORD_RULES = "passwordrules";
    public static final String MIN_LENGTH = "minLength";
    public static final String MAX_LENGTH = "maxLength";
    public static final String ATLEAST_ONE_LOWER_CASE = "atleastOneLowerCase";
    public static final String ATLEAST_ONE_UPPER_CASE = "atleastOneUpperCase";
    public static final String ATLEAST_ONE_NUMBER = "atleastOneNumber";
    public static final String ATLEAST_ONE_SYMBOL = "atleastOneSymbol";
    public static final String CHAR_REPEAT_COUNT = "charRepeatCount";
    public static final String SUPPORTED_SYMBOLS = "supportedSymbols";

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {

        Result result = new Result();

        try {

            boolean isCustomer = true;

            String passwordRuleForCustomer = requestInstance.getParameter("passwordRuleForCustomer");
            if (passwordRuleForCustomer != null && passwordRuleForCustomer.equals("false")) {
                isCustomer = false;
            }

            // ** Reading from 'passwordrules' table **
            Map<String, String> passwordRulesTableMap = new HashMap<>();
            passwordRulesTableMap.put(ODataQueryConstants.FILTER, "IsCustomer eq " + (isCustomer ? 1 : 0));

            String readPasswordRulesResponse = Executor.invokeService(ServiceURLEnum.PASSWORDRULES_READ,
                    passwordRulesTableMap, null, requestInstance);
            JSONObject readPasswordRulesResponseJSON = CommonUtilities.getStringAsJSONObject(readPasswordRulesResponse);

            if (readPasswordRulesResponseJSON != null && readPasswordRulesResponseJSON.has(FabricConstants.OPSTATUS)
                    && readPasswordRulesResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
                    && readPasswordRulesResponseJSON.has(PASSWORD_RULES)) {

                JSONObject passwordRulesResponse = readPasswordRulesResponseJSON.getJSONArray(PASSWORD_RULES)
                        .getJSONObject(0);

                Record passwordRulesRecord = new Record();
                passwordRulesRecord.setId(PASSWORD_RULES);

                if (StringUtils.isNotBlank(passwordRulesResponse.optString(MIN_LENGTH))
                        && StringUtils.isNotBlank(passwordRulesResponse.optString(MAX_LENGTH))
                        && StringUtils.isNotBlank(passwordRulesResponse.optString(ATLEAST_ONE_LOWER_CASE))
                        && StringUtils.isNotBlank(passwordRulesResponse.optString(ATLEAST_ONE_UPPER_CASE))
                        && StringUtils.isNotBlank(passwordRulesResponse.optString(ATLEAST_ONE_NUMBER))
                        && StringUtils.isNotBlank(passwordRulesResponse.optString(ATLEAST_ONE_SYMBOL))
                        && StringUtils.isNotBlank(passwordRulesResponse.optString(CHAR_REPEAT_COUNT))
                        && StringUtils.isNotBlank(passwordRulesResponse.optString(SUPPORTED_SYMBOLS))) {

                    passwordRulesRecord.addParam(
                            new Param(MIN_LENGTH, passwordRulesResponse.getString(MIN_LENGTH), FabricConstants.INT));
                    passwordRulesRecord.addParam(
                            new Param(MAX_LENGTH, passwordRulesResponse.getString(MAX_LENGTH), FabricConstants.INT));
                    passwordRulesRecord.addParam(new Param(ATLEAST_ONE_LOWER_CASE,
                            passwordRulesResponse.getString(ATLEAST_ONE_LOWER_CASE), FabricConstants.BOOLEAN));
                    passwordRulesRecord.addParam(new Param(ATLEAST_ONE_UPPER_CASE,
                            passwordRulesResponse.getString(ATLEAST_ONE_UPPER_CASE), FabricConstants.BOOLEAN));
                    passwordRulesRecord.addParam(new Param(ATLEAST_ONE_NUMBER,
                            passwordRulesResponse.getString(ATLEAST_ONE_NUMBER), FabricConstants.BOOLEAN));
                    passwordRulesRecord.addParam(new Param(ATLEAST_ONE_SYMBOL,
                            passwordRulesResponse.getString(ATLEAST_ONE_SYMBOL), FabricConstants.BOOLEAN));
                    passwordRulesRecord.addParam(new Param(CHAR_REPEAT_COUNT,
                            passwordRulesResponse.getString(CHAR_REPEAT_COUNT), FabricConstants.INT));
                    passwordRulesRecord.addParam(new Param(SUPPORTED_SYMBOLS,
                            passwordRulesResponse.getString(SUPPORTED_SYMBOLS), FabricConstants.STRING));
                } else {
                    throw new ApplicationException(ErrorCodeEnum.ERR_21071);
                }

                result.addRecord(passwordRulesRecord);
            } else {
                ErrorCodeEnum.ERR_21071.setErrorCode(result);
            }
        } catch (ApplicationException ae) {
            ae.getErrorCodeEnum().setErrorCode(result);
            alert.prepareError("ApplicationException occured in PasswordRulesFetchService. Error: ", ae).log();
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20001.setErrorCode(result);
            alert.prepareError("Exception occured in PasswordRulesFetchService. Error: ", e).log();
        }

        return result;
    }
}
