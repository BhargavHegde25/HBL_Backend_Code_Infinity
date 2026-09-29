package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.infinity.api.arrangements.backenddelegate.api.GetAccountsArrangementsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.prop.AccountTypeProperties;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

public class GetAccountsArrangementsExperienceAPIBackendDelegateImpl
        implements GetAccountsArrangementsExperienceAPIBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public List<ArrangementsDTO> getArrangements(String filterKey, String filterValue, String authToken)
            throws ApplicationException {

        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        String ArrangementResponse = null;
        InfinityServices seriveDetails = null;
        try {
            if ("accountId".equals(filterKey)) {
                seriveDetails = ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTSBYACCOUNTID;
            } else if ("customerId".equals(filterKey)) {
                seriveDetails = ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTSBYCUSTOMERID;
            } else if ("socialSecurityId".equals(filterKey)) {
                seriveDetails = ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTSBYSOCIALSECURITYID;
            } else {
                return arrangementsDTO;
            }

            inputMap.put(filterKey, filterValue);
            headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);

            try {
                ArrangementResponse = Executor.invokePassThroughServiceAndGetString(seriveDetails, inputMap, headerMap);

            } catch (Exception e) {
                alert.prepareError("Unable to fetch Arrangements " + e).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20049);
            }

            // TODO copied the below code form ArrangementsExperienceAPIBackendDelegateImpl class and, need to expose
            // the business logic implementation as a seperate method so that the below logic can be replace by calling
            // to that instead of duplication.

            JSONObject ArrResponse = new JSONObject();

            JSONObject companyObj = null;
            if (StringUtils.isNotBlank(ArrangementResponse)) {
                ArrResponse = new JSONObject(ArrangementResponse);
            }
            JSONArray arrangements = new JSONArray();
            if (ArrResponse.has("arrangements")) {
                arrangements = ArrResponse.getJSONArray("arrangements");
            } else if (ArrResponse.has("arrangementResponses")) {
                arrangements = ArrResponse.getJSONArray("arrangementResponses");
            }
            for (int i = 0; i < arrangements.length(); i++) {
                ArrangementsDTO acDTO = new ArrangementsDTO();
                JSONObject companyExtensionData = null;
                String companyId = null;
                JSONObject balances = null;
                JSONObject arrangement = arrangements.getJSONObject(i);
                String AccId = (String) arrangement.get("linkedReference");
                if (AccId != null) {
                    acDTO.setAccount_id(AccId);
                    companyObj = arrangement.getJSONObject("company");
                    companyExtensionData = companyObj.getJSONObject("extensionData");
                    if (companyObj.has("companyReference")) {
                        companyId = companyObj.getString("companyReference");
                    }

                    JSONObject BalanceResponse = getBalanceForAccount(AccId, companyId, authToken);
                    if (BalanceResponse.has("items")) {
                        JSONArray accountBalance = BalanceResponse.getJSONArray("items");
                        if (accountBalance != null && accountBalance.length() > 0) {
                            balances = accountBalance.getJSONObject(0);
                        }
                    }
                }

                if (arrangement.has("arrangementId")) {
                    acDTO.setArrangementId(arrangement.getString("arrangementId"));
                }

                if (arrangement.has("productDescription")) {
                    acDTO.setAccountName(arrangement.getString("productDescription"));
                    acDTO.setDisplayName(arrangement.getString("productDescription"));
                }

                // Set Account Type based on products
                String product = StringUtils.EMPTY;
                if (arrangement.has("product")) {
                    product = arrangement.getString("product");
                }
                acDTO.setTypeDescription(AccountTypeProperties.getValue(product));

                // Set Account NickName
                if (arrangement.has("shortTitle")) {
                    acDTO.setNickName(arrangement.getString("shortTitle"));
                }

                if (balances != null) {
                    // Set Account Available Balance
                    if (balances.has("availableBalance")) {
                        Object availableBalance = balances.get("availableBalance");
                        if (availableBalance instanceof Integer) {
                            acDTO.setAvailableBalance(((Number) availableBalance).doubleValue());
                        } else if (availableBalance instanceof BigDecimal) {
                        	acDTO.setAvailableBalance(((BigDecimal) availableBalance).doubleValue());
                        } else {
                            acDTO.setAvailableBalance((double) balances.get("availableBalance"));
                        }
                    }
                    if (balances.has("processingTime")) {
                        acDTO.setProcessingTime((String) balances.get("processingTime"));
                    }
                    if (balances.has("onlineActualBalance")) {
                        Object onlineActualBalance = balances.get("onlineActualBalance");
                        if (onlineActualBalance instanceof Integer) {
                            acDTO.setCurrentBalance(((Number) onlineActualBalance).doubleValue());
                        } else if (onlineActualBalance instanceof BigDecimal) {
                        	acDTO.setCurrentBalance(((BigDecimal) onlineActualBalance).doubleValue());
                        } else {
                            acDTO.setCurrentBalance((double) balances.get("onlineActualBalance"));
                        }
                    }

                    if (balances.has("pendingDeposit")) {
                        Object pendingDeposit = balances.get("pendingDeposit");
                        if (pendingDeposit instanceof Integer) {
                            acDTO.setPendingDeposit(((Number) pendingDeposit).doubleValue());
                        } else {
                            acDTO.setPendingDeposit((double) balances.get("pendingDeposit"));
                        }
                    } else {
                        acDTO.setPendingDeposit(0.00);
                    }

                    if (balances.has("pendingWithdrawal")) {
                        Object pendingWithdrawal = balances.get("pendingWithdrawal");
                        if (pendingWithdrawal instanceof Integer) {
                            acDTO.setPendingWithdrawal(((Number) pendingWithdrawal).doubleValue());
                        } else {
                            acDTO.setPendingWithdrawal((double) balances.get("pendingWithdrawal"));
                        }
                    } else {
                        acDTO.setPendingWithdrawal(0.00);
                    }

                    if (balances.has("dividendLastPaidAmount")) {
                        Object dividendLastPaidAmount = balances.get("dividendLastPaidAmount");
                        if (dividendLastPaidAmount instanceof Integer) {
                            acDTO.setDividendLastPaidAmount(((Number) dividendLastPaidAmount).doubleValue());
                        } else {
                            acDTO.setDividendLastPaidAmount((double) balances.get("dividendLastPaidAmount"));
                        }
                    } else {
                        acDTO.setDividendLastPaidAmount(0.00);
                    }
                    // Set Dividend Last Paid Date
                    if (balances.has("dividendLastPaidDate")) {
                        acDTO.setDividendLastPaidDate(balances.getString("dividendLastPaidDate"));
                    }
                    if (balances.has("dividendPaidYTD")) {
                        Object dividendPaidYTD = balances.get("dividendPaidYTD");
                        if (dividendPaidYTD instanceof Integer) {
                            acDTO.setDividendPaidYTD(((Number) dividendPaidYTD).doubleValue());
                        } else {
                            acDTO.setDividendPaidYTD((double) balances.get("dividendPaidYTD"));
                        }
                    } else {
                        acDTO.setDividendPaidYTD(0.00);
                    }

                    // Set Dividend Rate
                    if (balances.has("dividendRate")) {
                        acDTO.setDividendRate((float) balances.get("dividendRate"));
                    }
                }

                // Set values for Account Information
                // Set Company Id
                if (companyId != null) {
                    acDTO.setCompanyCode(companyId);
                }

                // Set Routing Number
                if (companyExtensionData.has("sortCode")) {
                    acDTO.setRoutingNumber(companyExtensionData.getString("sortCode"));
                }

                // Set Swift Code
                if (companyExtensionData.has("accountWithBankBIC")) {
                    acDTO.setSwiftCode(companyExtensionData.getString("accountWithBankBIC"));
                }

                // Set Currency Code
                if (arrangement.has("currency")) {
                    acDTO.setCurrencyCode(arrangement.getString("currency"));
                }

                // Set Bank Name
                if (arrangement.has("activeBranch")) {
                    acDTO.setBankname(arrangement.getString("activeBranch"));
                }

                // Set Creation Date
                if (arrangement.has("creationDate")) {
                    acDTO.setOpeningDate(arrangement.getString("creationDate"));
                }

                // Set Account Status
                if (arrangement.has("arrangementStatus")) {
                    acDTO.setStatusDesc(arrangement.getString("arrangementStatus"));
                }

                // Set Primary Account Holder
                // Set Role Info By looping through the Role array
                JSONArray roles = arrangement.getJSONArray("roles");
                JSONObject priAccHolder = new JSONObject();
                JSONArray jointHolders = new JSONArray();
                JSONObject jointHolder = new JSONObject();
                String membershipId = null;
                for (int j = 0; j < roles.length(); j++) {
                    JSONObject role = roles.getJSONObject(j);
                    String partyRole = StringUtils.EMPTY;
                    if (role.has("partyRole")) {
                        partyRole = role.getString("partyRole");
                    }
                    if (role.has("partyId") && StringUtils.isBlank(membershipId)) {
                        membershipId = role.getString("partyId");
                    }
                    if (partyRole.equalsIgnoreCase("OWNER")) {
                        if (role.has("firstName") && role.has("lastName")) {
                            priAccHolder.put("fullname",
                                    role.getString("firstName") + " " + role.getString("lastName"));
                        } else {
                            if (role.has("firstName")) {
                                priAccHolder.put("fullname", role.getString("firstName"));
                            }
                        }
                    }
                    if (partyRole.equalsIgnoreCase("Joint.Owner")) {
                        if (role.has("firstName") && role.has("lastName")) {
                            jointHolder.put("fullname", role.getString("firstName") + " " + role.getString("lastName"));
                        } else {
                            if (role.has("firstName")) {
                                jointHolder.put("fullname", role.getString("firstName"));
                            }
                        }
                    }
                }
                acDTO.setAccountHolder(priAccHolder.toString());
                if (priAccHolder.has("fullname")) {
                    jointHolders.put(priAccHolder);
                }
                if (jointHolder.has("fullname")) {
                    jointHolders.put(jointHolder);
                }

                if (StringUtils.isNotBlank(membershipId)) {
                    acDTO.setMembership_id(membershipId);
                }

                if (StringUtils.isNotBlank(membershipId)) {
                    acDTO.setTaxId(membershipId);
                }

                acDTO.setJointHolders(jointHolders.toString());

                // Add the account DTO in arrangement DTo List
                arrangementsDTO.add(acDTO);

            }
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }

        return arrangementsDTO;

    }

    private JSONObject getBalanceForAccount(String accId, String companyId, String authToken)
            throws ApplicationException {
        String AccountId = accId;
        HashMap<String, Object> balInputMap = new HashMap<>();
        Map<String, Object> balHeaderMap = new HashMap<>();
        balInputMap.put("accountId", AccountId);
        balHeaderMap = ArrangementsUtils.generateSecurityHeadersForHMS(authToken, balHeaderMap);

        String BalanceResponse = null;
        JSONObject Balance = new JSONObject();
        try {
            BalanceResponse =
                    Executor.invokeService(ArrangementsAPIServices.HOLDINGSMICROSERVICESJSON_GETACCOUNTBALANCES,
                            balInputMap, balHeaderMap);

        } catch (Exception e) {
            alert.prepareError("Unable to fetch balances " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20042);
        }
        if (StringUtils.isNotBlank(BalanceResponse)) {
            Balance = new JSONObject(BalanceResponse);
        }
        return Balance;
    }

    @Override
    public JSONObject getCoreCustomerArrangements(String coreCustomerId, String authToken)
            throws ApplicationException {
        JSONObject response = new JSONObject();
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        InfinityServices seriveDetails = null;
        try {
            seriveDetails = ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTSBYCUSTOMERID;
            inputMap.put("customerId", coreCustomerId);
            headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);
            String arrangementsString =
                    Executor.invokePassThroughServiceAndGetString(seriveDetails, inputMap, headerMap);
            if (arrangementsString != null)
                response = new JSONObject(arrangementsString);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        return response;
    }

}
