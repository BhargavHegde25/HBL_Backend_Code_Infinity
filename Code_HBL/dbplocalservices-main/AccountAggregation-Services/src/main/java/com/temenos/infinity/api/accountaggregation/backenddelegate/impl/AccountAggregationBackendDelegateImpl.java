package com.temenos.infinity.api.accountaggregation.backenddelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.temenos.infinity.api.accountaggregation.backenddelegate.api.AccountAggregationBackendDelegate;
import com.temenos.infinity.api.accountaggregation.businessdelegate.impl.AccountAggregationBusinessDelegateImpl;
import com.temenos.infinity.api.accountaggregation.config.AccountAggregationServices;
import com.temenos.infinity.api.accountaggregation.config.ServerConfigurations;
import com.temenos.infinity.api.accountaggregation.constant.BackendErrorCodeEnum;
import com.temenos.infinity.api.accountaggregation.constant.ErrorCodeEnum;
import com.temenos.infinity.api.accountaggregation.constant.MSCertificateConstants;
import com.temenos.infinity.api.accountaggregation.dto.BankDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConnectionDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConsentDTO;
import com.temenos.infinity.api.accountaggregation.dto.TermsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;

public class AccountAggregationBackendDelegateImpl implements AccountAggregationBackendDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<BankDTO> getBanks(String countryCode, String authToken) throws ApplicationException {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("countryCode", countryCode);
            headerMap = generateSecurityHeaders(authToken, headerMap);
            String serviceResponse = Executor
                    .invokeService(AccountAggregationServices.ACCOUNTAGGREGATIONJSON_GETBANKSFORCOUNTRY, inputMap,
                            headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null || (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.BACKENDFAILURE.getErrorCode()))) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            } else if (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.NORECORDSFOUND.getErrorCode()))
                throw new ApplicationException(ErrorCodeEnum.ERR_20002);
            JSONArray banksJSONArr = serviceResponseJSON.optJSONArray("Banks");
            List<BankDTO> banks = JSONUtils.parseAsList(banksJSONArr.toString(), BankDTO.class);
            return banks;

        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public String createCustomer(String digitalProfileId, String authToken) throws ApplicationException {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("digitalProfileId", digitalProfileId);
            headerMap = generateSecurityHeaders(authToken, headerMap);

            String serviceResponse =
                    Executor.invokeService(AccountAggregationServices.ACCOUNTAGGREGATIONJSON_CREATECUSTOMER,
                            inputMap, headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null || (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.BACKENDFAILURE.getErrorCode()))) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
            String code = serviceResponseJSON.getString("code");
            JSONObject data = serviceResponseJSON.optJSONObject("data");
            if (data.isNull("id")) {
                return code;
            } else {
                return data.getString("id");
            }
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public ConsentDTO getConsentURL(String digitalProfileId, String javascript_callback_type, String from_date,
            String scopes, String providerCode, String period_days, String fetch_scopes, String operation,
            String authToken)
            throws ApplicationException {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("digitalProfileId", digitalProfileId);
            inputMap.put("javascript_callback_type", javascript_callback_type);
            inputMap.put("from_date", from_date);
            inputMap.put("scopes", scopes);
            inputMap.put("providerCode", providerCode);
            inputMap.put("period_days", period_days);
            inputMap.put("fetch_scopes", fetch_scopes);
            headerMap = generateSecurityHeaders(authToken, headerMap);
            String serviceResponse = new String();
            if (operation.equalsIgnoreCase("createConnection")) {
                serviceResponse =
                        Executor.invokeService(AccountAggregationServices.ACCOUNTAGGREGATIONJSON_GETCONSENTURL,
                                inputMap, headerMap);
            } else if (operation.equalsIgnoreCase("reauthenticateConnection")) {
                serviceResponse = Executor.invokeService(
                        AccountAggregationServices.ACCOUNTAGGREGATIONJSON_REAUTHCONNECTIONURL, inputMap, headerMap);
            }
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null || (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.BACKENDFAILURE.getErrorCode()))) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
            JSONObject data = serviceResponseJSON.getJSONObject("data");
            ConsentDTO responseConsentDTO = JSONUtils.parse(data.toString(), ConsentDTO.class);
            return responseConsentDTO;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public TermsDTO getTermsAndCondition(String termsAndConditionsCode, String languageCode, String operation,
            String bankCode, Map<String, Object> headersMap) throws ApplicationException {
        try {

            Map<String, Object> parametersMap = new HashMap<>();
            parametersMap.put("languageCode", languageCode);
            parametersMap.put("termsAndConditionsCode", termsAndConditionsCode);

            String response = Executor.invokeService(
                    AccountAggregationServices.DBPPRODUCTSERVICES_GETCUSTOMERTERMSANDCONDITIONS, parametersMap,
                    headersMap);

            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(response);
            if (serviceResponseJSON == null) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
            TermsDTO termsDTO = JSONUtils.parse(serviceResponseJSON.toString(), TermsDTO.class);
            termsDTO.setOperation(operation);
            termsDTO.setBankCode(bankCode);
            return termsDTO;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }

    }

    @Override
    public ConnectionDTO loadAccountsWithTransactions(String digitalProfileId, String providerCode, String authToken)
            throws ApplicationException {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("digitalProfileId", digitalProfileId);
            inputMap.put("providerCode", providerCode);
            headerMap = generateSecurityHeaders(authToken, headerMap);

            String serviceResponse = Executor.invokeService(
                    AccountAggregationServices.ACCOUNTAGGREGATIONJSON_LOADACCOUNTSWITHTRANSACTIONS, inputMap,
                    headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            ConnectionDTO responseConnectionDTO = JSONUtils.parse(serviceResponseJSON.toString(), ConnectionDTO.class);
            return responseConnectionDTO;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override

    public ConnectionDTO loadConnection(String digitalProfileId, String authToken) throws ApplicationException {
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("digitalProfileId", digitalProfileId);
        headerMap = generateSecurityHeaders(authToken, headerMap);
        try {
            String serviceResponse = Executor
                    .invokeService(AccountAggregationServices.ACCOUNTAGGREGATIONJSON_LOADCONNECTIONS, inputMap,
                            headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            ConnectionDTO responseConnectionDTO = JSONUtils.parse(serviceResponseJSON.toString(), ConnectionDTO.class);
            return responseConnectionDTO;
        } catch (Exception e) {
            System.out.println(e);
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public ConnectionDTO refreshConnection(String digitalProfileId, String providerCode, String authToken)
            throws ApplicationException {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("digitalProfileId", digitalProfileId);
            inputMap.put("providerCode", providerCode);
            headerMap = generateSecurityHeaders(authToken, headerMap);

            String serviceResponse = Executor
                    .invokeService(AccountAggregationServices.ACCOUNTAGGREGATIONJSON_REFRESHCONNECTION, inputMap,
                            headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null || (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.BACKENDFAILURE.getErrorCode()))) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);

            } else if (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.NORECORDSFOUND.getErrorCode())) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20002);

            } else if (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.REFRESHNOTPOSSIBLE.getErrorCode()))
                throw new ApplicationException(ErrorCodeEnum.ERR_20011);
            // need to implement custom message

            ConnectionDTO responseConnectionDTO = JSONUtils.parse(serviceResponseJSON.toString(), ConnectionDTO.class);
            return responseConnectionDTO;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public ConnectionDTO deleteConnection(String digitalProfileId, String providerCode, String authToken)
            throws ApplicationException {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("digitalProfileId", digitalProfileId);
            inputMap.put("providerCode", providerCode);
            headerMap = generateSecurityHeaders(authToken, headerMap);

            String serviceResponse = Executor
                    .invokeService(AccountAggregationServices.ACCOUNTAGGREGATIONJSON_DELETECONNECTION, inputMap,
                            headerMap);
            JSONObject serviceResponseJSON = Utilities.convertStringToJSON(serviceResponse);
            if (serviceResponseJSON == null || (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.BACKENDFAILURE.getErrorCode()))) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            } else if (serviceResponseJSON != null
                    && serviceResponseJSON.opt("code").equals(BackendErrorCodeEnum.NORECORDSFOUND.getErrorCode()))
                throw new ApplicationException(ErrorCodeEnum.ERR_20002);
            ConnectionDTO responseConnectionDTO = JSONUtils.parse(serviceResponseJSON.toString(), ConnectionDTO.class);
            return responseConnectionDTO;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public void createConsent(String digitalProfileId, String from_date, String providerCode, String period_days,
            String fetch_scopes, String authToken) throws ApplicationException {
        try {
            Map<String, Object> inputMap = new HashMap<>();
            Map<String, Object> headerMap = new HashMap<>();
            inputMap.put("digitalProfileId", digitalProfileId);
            inputMap.put("creationDate", from_date);
            inputMap.put("bankName", providerCode);
            inputMap.put("period_days", period_days);
            inputMap.put("purpose", fetch_scopes);
            headerMap = generateSecurityHeadersForCreateConsent(authToken, headerMap);

            String serviceResponseConsent =
                    Executor.invokeService(AccountAggregationServices.CONSENTJSON_CREATECONSENT, inputMap,
                            headerMap);
            diagnostic.prepareDebug(serviceResponseConsent).log();
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    /**
     * Method to add Authorization Headers for MS
     * 
     * param String authToken param Map<String, Object> headerMap
     * 
     * @return Map<String, Object> headerMap
     */
    private Map<String, Object> generateSecurityHeaders(String authToken, Map<String, Object> headerMap) {
        headerMap.put("Authorization", authToken);
        if (StringUtils.isNotEmpty(ServerConfigurations.ACCAGG_DEPLOYMENT_PLATFORM.getValueIfExists())) {
            if (StringUtils.equalsIgnoreCase(ServerConfigurations.ACCAGG_DEPLOYMENT_PLATFORM.getValueIfExists(),
                    MSCertificateConstants.AWS))
                headerMap.put("x-api-key", ServerConfigurations.ACCAGG_AUTHORIZATION_KEY.getValueIfExists());
            else if (StringUtils.equalsIgnoreCase(ServerConfigurations.ACCAGG_DEPLOYMENT_PLATFORM.getValueIfExists(),
                    MSCertificateConstants.AZURE))
                headerMap.put("x-functions-key", ServerConfigurations.ACCAGG_AUTHORIZATION_KEY.getValueIfExists());
        }
        headerMap.put("roleId", ServerConfigurations.ACCAGG_ROLE_ID.getValueIfExists());
        return headerMap;
    }
	private Map<String, Object> generateSecurityHeadersForCreateConsent(String authToken, Map<String, Object> headerMap) {
        headerMap.put("Authorization", authToken);
        if (StringUtils.isNotEmpty(ServerConfigurations.CONSENT_DEPLOYMENT_PLATFORM.getValueIfExists())) {
            if (StringUtils.equalsIgnoreCase(ServerConfigurations.CONSENT_DEPLOYMENT_PLATFORM.getValueIfExists(),
                    MSCertificateConstants.AWS))
                headerMap.put("x-api-key", ServerConfigurations.CONSENT_AUTHORIZATION_KEY.getValueIfExists());
            else if (StringUtils.equalsIgnoreCase(ServerConfigurations.CONSENT_DEPLOYMENT_PLATFORM.getValueIfExists(),
                    MSCertificateConstants.AZURE))
                headerMap.put("x-functions-key", ServerConfigurations.CONSENT_AUTHORIZATION_KEY.getValueIfExists());
        }
        headerMap.put("roleId", ServerConfigurations.ACCAGG_ROLE_ID.getValueIfExists());
        return headerMap;
    }

}
