package com.temenos.infinity.api.accountaggregation.businessdelegate.impl;

import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.accountaggregation.backenddelegate.api.AccountAggregationBackendDelegate;
import com.temenos.infinity.api.accountaggregation.businessdelegate.api.AccountAggregationBusinessDelegate;
import com.temenos.infinity.api.accountaggregation.constant.ErrorCodeEnum;
import com.temenos.infinity.api.accountaggregation.dto.BankDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConnectionDTO;
import com.temenos.infinity.api.accountaggregation.dto.ConsentDTO;
import com.temenos.infinity.api.accountaggregation.dto.TermsDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class AccountAggregationBusinessDelegateImpl implements AccountAggregationBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<BankDTO> getBanks(String countryCode, String authToken) throws ApplicationException {
        try {

            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            List<BankDTO> banks = backendDelegate.getBanks(countryCode, authToken);
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

            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            String code = backendDelegate.createCustomer(digitalProfileId, authToken);
            return code;

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
            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            ConsentDTO consent = backendDelegate.getConsentURL(digitalProfileId, javascript_callback_type, from_date,
                    scopes, providerCode, period_days, fetch_scopes, operation, authToken);
            return consent;

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
            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            TermsDTO terms =
                    backendDelegate.getTermsAndCondition(termsAndConditionsCode, languageCode, operation, bankCode,
                            headersMap);
            return terms;
        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public ConnectionDTO loadAccountsWithTransactions(String digitalProfileId, String providerCode, String authToken)
            throws ApplicationException {
        try {
            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            ConnectionDTO connection =
                    backendDelegate.loadAccountsWithTransactions(digitalProfileId, providerCode, authToken);
            return connection;
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

        try {
            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            ConnectionDTO connection = backendDelegate.loadConnection(digitalProfileId, authToken);
            return connection;

        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (Exception e) {
            System.out.println(e);
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public ConnectionDTO refreshConnection(String digitalProfileId, String providerCode, String authToken)
            throws ApplicationException {
        try {
            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            ConnectionDTO connection = backendDelegate.refreshConnection(digitalProfileId, providerCode, authToken);
            return connection;

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
            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            ConnectionDTO connection = backendDelegate.deleteConnection(digitalProfileId, providerCode, authToken);
            return connection;
        } catch (ApplicationException e) {
            alert.prepareError(e.toString()).log();
            throw e;
        } catch (

        Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

    @Override
    public void createConsent(String digitalProfileId, String from_date, String providerCode, String period_days,
            String fetch_scopes, String authToken) throws ApplicationException {
        try {
            AccountAggregationBackendDelegate backendDelegate =
                    DBPAPIAbstractFactoryImpl.getBackendDelegate(AccountAggregationBackendDelegate.class);
            backendDelegate.createConsent(digitalProfileId, from_date, providerCode, period_days, fetch_scopes,
                    authToken);

        } catch (Exception e) {
            alert.prepareError(e.toString()).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20400);
        }
    }

}
