package com.temenos.infinity.api.arrangements.resource.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.TokenUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.businessdelegate.api.GetAccountsBySearchBusinessDelegate;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.resource.api.GetAccountsBySearchResource;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.transact.tokenmanager.exception.CertificateNotRegisteredException;

public class GetAccountsBySearchResourceImpl implements GetAccountsBySearchResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public Result getAccounts(String accountId, String membershipId, String taxId, String authToken,
            DataControllerRequest request) throws ApplicationException {

        Result result = new Result();
        if (StringUtils.isBlank(authToken)) {
            return ErrorCodeEnum.ERR_20055.setErrorCode(new Result());
        }
        List<ArrangementsDTO> accountsDTO = null;
        try {
        	CommonUtils.setCompanyIdToRequest(request);
            if ((StringUtils.isNotBlank(accountId) || StringUtils.isNotBlank(membershipId)
                    || StringUtils.isNotBlank(taxId))) {
                if (StringUtils.isNotBlank(accountId)) {
                    GetAccountsBySearchBusinessDelegate accountsDelegateInstance = DBPAPIAbstractFactoryImpl
                            .getBusinessDelegate(GetAccountsBySearchBusinessDelegate.class);

                    accountsDTO = accountsDelegateInstance.getAccounts("accountId", accountId, authToken);
                } else if (StringUtils.isNotBlank(membershipId)) {
                    try {
                        String CompanyId = ArrangementsUtils.getUserAttributeFromIdentity(request,
                                TemenosConstants.COMPANYID);
                        membershipId = membershipId.contains("-") ? membershipId : CompanyId + "-" + membershipId;
                    } catch (Exception e) {
                        alert.prepareError(e.getMessage()).log();
                        throw new ApplicationException(ErrorCodeEnum.ERR_20050);
                    }
                    GetAccountsBySearchBusinessDelegate accountsDelegateInstance = DBPAPIAbstractFactoryImpl
                            .getBusinessDelegate(GetAccountsBySearchBusinessDelegate.class);

                    accountsDTO = accountsDelegateInstance.getAccounts("customerId", membershipId, authToken);

                } else if (StringUtils.isNotBlank(taxId)) {
                    GetAccountsBySearchBusinessDelegate accountsDelegateInstance = DBPAPIAbstractFactoryImpl
                            .getBusinessDelegate(GetAccountsBySearchBusinessDelegate.class);

                    accountsDTO = accountsDelegateInstance.getAccounts("socialSecurityId", taxId, authToken);

                }
                if (accountsDTO != null && !accountsDTO.isEmpty()) {
                    JsonObject resultObj = new JsonObject();
                    JsonArray array = new JsonArray();

                    for (ArrangementsDTO accountDTO : accountsDTO) {
                        String accountNumber = accountDTO.getAccount_id();
                        if (StringUtils.isNotBlank(accountNumber)) {
                            JsonObject accountObject = new JsonObject();
                            accountObject.addProperty("Account_id", accountNumber.contains("-")
                                    ? StringUtils.split(accountNumber, "-")[1]
                                    : accountNumber);
                            accountObject.addProperty("IsOrganizationAccount", "true");
                            accountObject.addProperty("Membership_id", accountDTO.getMembership_id());
                            accountObject.addProperty("Taxid", accountDTO.getTaxId());
                            accountObject.addProperty("AccountHolder", accountDTO.getAccountHolder());
                            accountObject.addProperty("AccountName", accountDTO.getAccountName());
                            accountObject.addProperty("AccountType", accountDTO.getTypeDescription());
                            accountObject.addProperty("Type_id", getTypeId(accountDTO.getTypeDescription()));
                            accountObject.addProperty("StatusDesc", "Active");
                            accountObject.addProperty("arrangementId", accountDTO.getArrangementId());
                            accountObject.addProperty("CurrentBalance",
                                    Double.toString(accountDTO.getCurrentBalance()));
                            accountObject.addProperty("AvailableBalance",
                                    Double.toString(accountDTO.getAvailableBalance()));
                            accountObject.addProperty("RoutingNumber", accountDTO.getRoutingNumber());
                            accountObject.addProperty("SwiftCode", accountDTO.getSwiftCode());
                            accountObject.addProperty("DividendRate", Float.toString(accountDTO.getDividendRate()));
                            accountObject.addProperty("DividendPaidYTD",
                                    Double.toString(accountDTO.getDividendPaidYTD()));
                            accountObject.addProperty("LastDividendPaidAmount",
                                    Double.toString(accountDTO.getDividendLastPaidAmount()));
                            accountObject.addProperty("LastDividendPaidDate", accountDTO.getDividendLastPaidDate());

                            accountObject.addProperty("OpeningDate", accountDTO.getOpeningDate());
                            accountObject.addProperty("CurrencyCode", accountDTO.getCurrencyCode());

                            array.add(accountObject);

                        }
                    }

                    resultObj.add("Accounts", array);
                    result = JSONToResult.convert(resultObj.toString());

                } else {
                    ErrorCodeEnum.ERR_11026.setErrorCode(result);
                }

            } else {
                ErrorCodeEnum.ERR_11024.setErrorCode(result);
            }
        } catch (ApplicationException e) {
            alert.prepareError(e.getMessage()).log();
            throw e;
        }
        return result;

    }

    private String getTypeId(String typeDescription) {
        if (StringUtils.isBlank(typeDescription)) {
            return "1";
        } else if ("Checking".equals(typeDescription)) {
            return "1";
        } else if ("Savings".equals(typeDescription)) {
            return "2";
        } else {
            return "1";
        }
    }

    @Override
    public Result getAccountsByCoreCustomerIdSearch(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws ApplicationException {
        Result result = new Result();
        Map<String, String> inputParams = (Map<String, String>) inputArray[1];
        String coreCustomerId = inputParams.containsKey("coreCustomerId") ? inputParams.get("coreCustomerId")
                : request.getParameter("coreCustomerId");
        try {
            String authToken = "";
            Map<String, String> inputMap = new HashMap<>();
			inputMap.put("customerId",ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id"));
			authToken = TokenUtils.getAMSAuthToken(inputMap);
            CommonUtils.setCompanyIdToRequest(request);
            GetAccountsBySearchBusinessDelegate accountSearchDelegateInstance = DBPAPIAbstractFactoryImpl
                    .getBusinessDelegate(GetAccountsBySearchBusinessDelegate.class);
            JSONObject json = accountSearchDelegateInstance.getAccountsByCoreCustomerIdSearch(coreCustomerId,
                    authToken);
            result = JSONToResult.convert(json.toString());
        } catch (CertificateNotRegisteredException e) {
            alert.prepareError("Certificate Not Registered" + e).log();
            ErrorCodeEnum.ERR_20055.setErrorCode(result);
        } catch (ApplicationException e) {
            ErrorCodeEnum.ERR_20057.setErrorCode(result);
        } catch (Exception e) {
            ErrorCodeEnum.ERR_20057.setErrorCode(result);
        }
        return result;
    }
}
