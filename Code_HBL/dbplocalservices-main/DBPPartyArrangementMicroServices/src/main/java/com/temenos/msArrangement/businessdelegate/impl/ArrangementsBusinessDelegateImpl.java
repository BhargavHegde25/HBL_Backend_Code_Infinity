package com.temenos.msArrangement.businessdelegate.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.temenos.msArrangement.businessdelegate.api.ArrangementsBusinessDelegate;
import com.temenos.msArrangement.dto.ArrangementsDTO;
import com.temenos.msArrangement.utils.HTTPOperations;
import com.temenos.msArrangement.utils.OperationName;

/**
 * 
 * @author smugesh
 * @version 1.0 Extends the {@link ArrangementsBusinessDelegate}
 */
public class ArrangementsBusinessDelegateImpl implements ArrangementsBusinessDelegate {
    /**
     * method to get the Accounts
     * 
     * @return List<ArrangementsDTO> of Arrangement Accounts
     */


    String EndPoint = "";
    String URL = "";

    public List<ArrangementsDTO> getArrangements(String partyId, String productLineId) {

        // Initialize Variables
        JSONObject ArrResponse = null;
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();
        JSONObject companyObj = null;

        // Get Request parameters from Request Object

        if (partyId != null) {
            // Add company Id with party id
            String companyPartyId = OperationName.COMPANY_ID + "-" + partyId;
            // Construct EndPoints
            EndPoint = OperationName.GET_ACCOUNTS;
            EndPoint = EndPoint.replaceFirst("param", companyPartyId);
            URL = EndPoint + "?productline=" + productLineId;

            // Hit Arrangements Micro Service
            String ArrangementResponse = null;
            ArrangementResponse = HTTPOperations.sendHttpRequest("GET", URL, null, null, null);
            if (StringUtils.isNotBlank(ArrangementResponse)) {
                ArrResponse = new JSONObject(ArrangementResponse);
            }
            JSONArray arrangements = ArrResponse.getJSONArray("arrangements");
            for (int i = 0; i < arrangements.length(); i++) {
                ArrangementsDTO acDTO = new ArrangementsDTO();
                JSONObject companyExtensionData = null;
                String companyId = null;
                JSONObject balances = null;
                JSONObject arrangement = arrangements.getJSONObject(i);
                String AccId = (String) arrangement.get("linkedReference");
                if (AccId != null) {
                    companyObj = arrangement.getJSONObject("company");
                    companyExtensionData = companyObj.getJSONObject("extensionData");
                    if (companyObj.has("companyReference")) {
                        companyId = companyObj.getString("companyReference");
                    }
                    JSONObject BalanceResponse = getBalanceForAccount(AccId, companyId);
                    JSONArray accountBalance = BalanceResponse.getJSONArray("items");
                    if (accountBalance != null && accountBalance.length() > 0) { 
                        balances = accountBalance.getJSONObject(0);
                    }
                }

                // Set Values for Account DashBoard
                // Set Account ID
                acDTO.setAccount_id(AccId);

                // Set Product Description as account Name
                if (arrangement.has("productDescription")) {
                    acDTO.setAccountName(arrangement.getString("productDescription"));
                    acDTO.setDisplayName(arrangement.getString("productDescription"));
                }

                // Set Account Type based on products
                String ProductLine = "";
                String Product = "";
                if (arrangement.has("productLine")) {
                    ProductLine = arrangement.getString("productLine");
                }
                if (arrangement.has("product")) {
                    Product = arrangement.getString("product");
                }
                if (ProductLine.equals("ACCOUNTS") && Product.equals("CURRENT.ACCOUNT")) {
                    acDTO.setTypeDescription("Checking");
                } else if (ProductLine.equals("ACCOUNTS") && Product.equals("SAVINGS.ACCOUNT")) {
                    acDTO.setTypeDescription("Savings");
                } else {
                    acDTO.setTypeDescription("Others");
                }

                // Set Account NickName
                if (arrangement.has("shortTitle")) {
                    acDTO.setNickName(arrangement.getString("shortTitle"));
                }


                // Set Account Available Balance
                if (balances.has("availableBalance")) {
                    acDTO.setAvailableBalance((Double) balances.get("availableBalance"));
                }

                // Set Summary Account Details
                // Set Current Balance for Account
                if (balances.has("onlineActualBalance")) {
                    acDTO.setCurrentBalance((int) balances.get("onlineActualBalance"));
                }

                // Set Pending Deposit
                if (balances.has("pendingDeposit")) {
                    acDTO.setPendingDeposit((Double) balances.get("pendingDeposit"));
                } else {
                    acDTO.setPendingDeposit(0.00);
                }

                // Set Pending Withdrawal
                if (balances.has("pendingWithdrawal")) {
                    acDTO.setPendingWithdrawal((Double) balances.get("pendingWithdrawal"));
                } else {
                    acDTO.setPendingWithdrawal(0.00);
                }

                // Set Balance and Interest Details
                // Set Dividend Last paid Amount
                if (balances.has("dividendLastPaidAmount")) {
                    acDTO.setDividendLastPaidAmount((Double) balances.get("dividendLastPaidAmount"));
                } else {
                    acDTO.setDividendLastPaidAmount(0.00);
                }

                // Set Dividend Last Paid Date
                if (balances.has("dividendLastPaidDate")) {
                    acDTO.setDividendLastPaidDate(balances.getString("dividendLastPaidDate"));
                }

                // Set Dividend Last Paid YTD
                if (balances.has("dividendPaidYTD")) {
                    acDTO.setDividendPaidYTD((Double) balances.get("dividendPaidYTD"));
                } else {
                    acDTO.setDividendPaidYTD(0.00);
                }

                // Set Dividend Rate
                if (balances.has("dividendRate")) {
                    acDTO.setDividendRate((float) balances.get("dividendRate"));
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
                for (int j = 0; j < roles.length(); j++) {
                    JSONObject role = roles.getJSONObject(j);
                    String partyRole = "";
                    if (role.has("partyRole")) {
                        partyRole = role.getString("partyRole");
                    }
                    if (partyRole.equals("Owner")) {
                        if (role.has("firstName") && role.has("lastName")) {
                            priAccHolder.put("fullname",
                                    role.getString("firstName") + " " + role.getString("lastName"));
                        } else {
                            if (role.has("firstName")) {
                                priAccHolder.put("fullname", role.getString("firstName"));
                            }
                        }
                    }
                    if (partyRole.equals("Joint.Owner")) {
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
                if(priAccHolder.has("fullname")){
                    jointHolders.put(priAccHolder);
                }
                  if(jointHolder.has("fullname")){
                     jointHolders.put(jointHolder);
                 }
                acDTO.setJointHolders(jointHolders.toString());
                // Set Support Flag fields
                acDTO.setSupportBillPay("1");
                acDTO.setSupportChecks("1");
                acDTO.setSupportDeposit("1");
                acDTO.setSupportTransferFrom("1"); 
                acDTO.setSupportTransferTo("1");

                // Add the account DTO in arrangement DTo List
                arrangementsDTO.add(acDTO);
            }
        }
        return arrangementsDTO;
    }

    // Get Balance Call
    JSONObject getBalanceForAccount(String accId, String companyId) {
        EndPoint = OperationName.GET_ACCOUNT_BALANCES;
        String AccountId = companyId + "-" + accId;
        EndPoint = EndPoint.replaceFirst("param", AccountId);
        String BalanceResponse = null;
        BalanceResponse = HTTPOperations.sendHttpRequest("GET", EndPoint, null, null, null);
        if (StringUtils.isNotBlank(BalanceResponse)) {
            return new JSONObject(BalanceResponse);
        }
        return new JSONObject();
    }

}
