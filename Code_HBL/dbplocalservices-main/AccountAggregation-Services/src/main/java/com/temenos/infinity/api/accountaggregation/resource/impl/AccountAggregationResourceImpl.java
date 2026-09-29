package com.temenos.infinity.api.accountaggregation.resource.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPErrorCodeSetter;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.accountaggregation.businessdelegate.api.AccountAggregationBusinessDelegate;
import com.temenos.infinity.api.accountaggregation.config.ServerConfigurations;
import com.temenos.infinity.api.accountaggregation.constant.BackendErrorCodeEnum;
import com.temenos.infinity.api.accountaggregation.constant.ErrorCodeEnum;
import com.temenos.infinity.api.accountaggregation.dto.BankDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConnectionDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConsentDTO;
import com.temenos.infinity.api.accountaggregation.dto.TermsDTO;
import com.temenos.infinity.api.accountaggregation.resource.api.AccountAggregationResource;
import com.temenos.infinity.api.accountaggregation.utils.AccountAggregationUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class AccountAggregationResourceImpl implements AccountAggregationResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    private static final String COUNTRY_CODE_PATTERN = "[A-Z]{2}";

    @Override
    public Result getBanks(String countryCode, String authToken) {
        Result result = new Result();
        try {
            AccountAggregationBusinessDelegate accountAggregationBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountAggregationBusinessDelegate.class);

            if (StringUtils.isBlank(countryCode) || !countryCode.matches(COUNTRY_CODE_PATTERN)) {
                return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(authToken)) {
                return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
            }

            List<BankDTO> banksDTOList = accountAggregationBusinessDelegate.getBanks(countryCode, authToken);
            String banksStr = JSONUtils.stringifyCollectionWithTypeInfo(banksDTOList, BankDTO.class);
            JSONArray banksJSONArr = new JSONArray(banksStr);
            if (banksJSONArr.length() <= 0) {
                return ErrorCodeEnum.ERR_20002.setErrorCode(new Result());
            }
            JSONObject resultJSON = new JSONObject();
            resultJSON.put("Banks", banksJSONArr);
            result = JSONToResult.convert(resultJSON.toString());

            List<String> bankCodes = new ArrayList<String> ();
            if(banksDTOList!=null) {
            	for(BankDTO bankDTO:banksDTOList) {
            		bankCodes.add(bankDTO.getBankCode());
            	}
            }
    		AccountAggregationUtils.saveIntoCache("BankCodes", bankCodes.toString());
            
            return result;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20004.setErrorCode(new Result());
        }
    }

    @Override
    public Result createTermsAndConditions(String digitalProfileId, String javascript_callback_type, String from_date,
            String scopes, String providerCode, String period_days, String fetch_scopes, String operation,
            String authToken, String CompanyId) {

        Result result = new Result();
        String code = StringUtils.EMPTY;
        try {
            if (StringUtils.isBlank(digitalProfileId) || StringUtils.isBlank(providerCode)
                    || StringUtils.isBlank(javascript_callback_type) || StringUtils.isBlank(from_date)
                    || StringUtils.isBlank(scopes) || StringUtils.isBlank(period_days)
                    || StringUtils.isBlank(fetch_scopes) || StringUtils.isBlank(operation)) {
                return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(authToken)) {
                return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
            }
            AccountAggregationBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountAggregationBusinessDelegate.class);
            digitalProfileId = this.getProfileIdwithCompanyName(digitalProfileId, CompanyId);
            code = customerBusinessDelegate.createCustomer(digitalProfileId, authToken);
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20003.setErrorCode(new Result());
        }

        try {
            if (!StringUtils.isBlank(code)
                    || (code.equals(BackendErrorCodeEnum.CUSTOMER_RECORD_EXISTS.getErrorCode()))) {

                AccountAggregationBusinessDelegate customerBusinessDelegate = DBPAPIAbstractFactoryImpl
                        .getBusinessDelegate(AccountAggregationBusinessDelegate.class);
                ConsentDTO OutputConsentDTO = customerBusinessDelegate.getConsentURL(digitalProfileId,
                        javascript_callback_type, from_date, scopes, providerCode, period_days, fetch_scopes, operation,
                        authToken);
                customerBusinessDelegate.createConsent(digitalProfileId, from_date, providerCode, period_days,
                        fetch_scopes, authToken);
                JSONObject responseObj = new JSONObject(OutputConsentDTO);
                result = JSONToResult.convert(responseObj.toString());

            }
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20006.setErrorCode(new Result());
        }
        return result;
    }

    @Override
    public Result getTermsAndCondition(String termsAndConditionsCode, String languageCode, String operation,
            String bankCode,Map<String,Object> headersMap) {
        try {
            Result result = new Result();

            AccountAggregationBusinessDelegate AccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountAggregationBusinessDelegate.class);
            TermsDTO terms = AccountsBusinessDelegate.getTermsAndCondition(termsAndConditionsCode, languageCode,
                    operation, bankCode,headersMap);
            JSONObject responseObj = new JSONObject(terms);
            result = JSONToResult.convert(responseObj.toString());
            return result;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20400.setErrorCode(new Result());
        }
    }

    @Override
    public Result updateStatus(String stage, String digitalProfileId, String providerCode, String authToken) {
        try {
            Result result = new Result();
            if (StringUtils.isBlank(digitalProfileId) || StringUtils.isBlank(providerCode)) {
                return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
            }
            AccountAggregationBusinessDelegate AccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountAggregationBusinessDelegate.class);
            if (StringUtils.isBlank(authToken)) {
                return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
            }
            switch (stage) {
            case "start":

                ConnectionDTO ConnectionDTO;
                ConnectionDTO = AccountsBusinessDelegate.loadConnection(digitalProfileId, authToken);
                JSONObject responseObjConnections = new JSONObject(ConnectionDTO);
                result = JSONToResult.convert(responseObjConnections.toString());
                return result;
            case "finish":

                ConnectionDTO accountsDTO = AccountsBusinessDelegate.loadAccountsWithTransactions(digitalProfileId,
                        providerCode, authToken);
                JSONObject responseObjAccounts = new JSONObject(accountsDTO);
                result = JSONToResult.convert(responseObjAccounts.toString());
                return result;
            default:
                return result;
            }
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20400.setErrorCode(new Result());
        }
    }

    @Override
    public Result refreshConnection(String digitalProfileId, String providerCode, String authToken, String CompanyId) {
        try {
            Result result = new Result();
            if (StringUtils.isBlank(digitalProfileId) || StringUtils.isBlank(providerCode)) {
                return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(authToken)) {
                return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
            }
            AccountAggregationBusinessDelegate AccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountAggregationBusinessDelegate.class);
            digitalProfileId = this.getProfileIdwithCompanyName(digitalProfileId, CompanyId);
            ConnectionDTO connectionDTO = AccountsBusinessDelegate.refreshConnection(digitalProfileId, providerCode,
                    authToken);
            JSONObject responseObj = new JSONObject(connectionDTO);
            result = JSONToResult.convert(responseObj.toString());
            return result;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20007.setErrorCode(new Result());
        }
    }

    @Override
    public Result deleteOperation(String digitalProfileId, String providerCode, String authToken, String CompanyId) {
        try {
            AccountAggregationBusinessDelegate AccountsBusinessDelegate = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(AccountAggregationBusinessDelegate.class);

            if (StringUtils.isBlank(digitalProfileId) || StringUtils.isBlank(providerCode)) {
                return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
            }
            if (StringUtils.isBlank(authToken)) {
                return ErrorCodeEnum.ERR_20042.setErrorCode(new Result());
            }
            digitalProfileId = this.getProfileIdwithCompanyName(digitalProfileId, CompanyId);
            ConnectionDTO connectionDTO = AccountsBusinessDelegate.deleteConnection(digitalProfileId, providerCode,
                    authToken);
            JSONObject responseObj = new JSONObject(connectionDTO);
            Result result = JSONToResult.convert(responseObj.toString());
            return result;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            return DBPErrorCodeSetter.setError(e.getErrorCodeEnum(), new Result());
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            return ErrorCodeEnum.ERR_20400.setErrorCode(new Result());
        }
    }

    @Override
    public String getProfileIdwithCompanyName(String digitalProfileId, String CompanyId) {
        try {
            if (!StringUtils.isBlank(CompanyId)) {
                digitalProfileId = CompanyId + "-" + digitalProfileId;
            } else {
                digitalProfileId = ServerConfigurations.AMS_COMPANYID.getValue() + "-" + digitalProfileId;
            }
            return digitalProfileId;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
        }
        return digitalProfileId;
    }

}
