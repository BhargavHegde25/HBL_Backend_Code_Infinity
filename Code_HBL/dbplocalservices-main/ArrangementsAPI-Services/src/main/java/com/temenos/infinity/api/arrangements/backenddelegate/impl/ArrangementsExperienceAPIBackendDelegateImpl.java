package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.backenddelegate.api.ArrangementsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.arrangements.businessdelegate.api.ArrangementsBusinessDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.MSCertificateConstants;
import com.temenos.infinity.api.arrangements.constants.TemenosConstants;
import com.temenos.infinity.api.arrangements.dto.AccountsDTO;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.prop.AccountTypeProperties;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;

/**
 * 
 * @author smugesh
 * @version 1.0 Extends the {@link ArrangementsBusinessDelegate}
 */
public class ArrangementsExperienceAPIBackendDelegateImpl implements ArrangementsExperienceAPIBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /**
     * method to get the Accounts
     * 
     * @return List<ArrangementsDTO> of Arrangement Accounts
     */
    @Override
    public ArrayList getArrangements(ArrangementsDTO inputPayload, DataControllerRequest request,
            String authToken, Boolean balanceFlag) throws ApplicationException {

        // Get Request parameters from Request Object
        String companyPartyId = inputPayload.getBackendUserId();
        String accountID = inputPayload.getAccount_id();

        // Load Account Properties
        AccountTypeProperties accountTypeProperties = new AccountTypeProperties(request);

        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("customerId", companyPartyId);
        headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);
        CommonUtils.setCompanyIdToRequest(request);
        // Get DBX Customer id to fetch favourite accounts
        String dbxCustomerId = inputPayload.getCustomerID();
        HashMap<String, String> favouriteAccounts = new HashMap<String, String>();
        if(StringUtils.isNotBlank(dbxCustomerId)) {
            favouriteAccounts = getFavouriteAccounts(dbxCustomerId, request);
        }
        ArrayList resp = new ArrayList();

        // Hit Arrangements Micro Service
        String ArrangementResponse = null;
        JSONObject logoResponse = null;
        JSONObject connectionResponse = null;
        JSONObject connection = null;
        alert.prepareError("AMS Request" + inputMap).log();
        try {
            ArrangementResponse = Executor.invokePassThroughServiceAndGetString(
                    ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTS, inputMap, headerMap);
            alert.prepareError("AMS Response" + ArrangementResponse).log();
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        resp.add(ArrangementResponse);
        alert.prepareError("AMS Response" + ArrangementResponse).log();
        // Initialize Variables
        JSONObject ArrResponse = null;
        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();
        JSONObject companyObj = null;
        if (StringUtils.isNotBlank(ArrangementResponse)) {
            ArrResponse = new JSONObject(ArrangementResponse);
        }
        JSONArray arrangements = ArrResponse.getJSONArray("arrangements");
        HashMap<String, AccountsDTO> accounts = ArrangementsUtils.getAccountsMapFromCache(request);
        if (accounts == null) {
            accounts = new HashMap<String, AccountsDTO>();
        }
        //removing wealth acc from ams response
        String pIDsCSV = "";
        int k=0,pCount=0;
        HashMap<String,JSONObject> portfolioAccMap = new HashMap<String,JSONObject>();
        while(k<arrangements.length()) {
        	JSONObject arrangement = arrangements.getJSONObject(k);
        	if (arrangement.has("productGroup")) {
        		if (arrangement.get("productGroup").equals("Portfolio")) {
        			pCount++;
        			arrangements.remove(k);
        			if (arrangement.has("extArrangementId")) {
        			String pID = arrangement.getString("extArrangementId");
        			pIDsCSV = pIDsCSV.concat(","+pID);      
        			portfolioAccMap.put(pID, arrangement);
        			}
        		}
        		else k++;
        	}
        	else k++;
        }
        for (int i = 0; i < arrangements.length(); i++) {
            ArrangementsDTO acDTO = new ArrangementsDTO();
            JSONObject companyExtensionData = null;
            String companyId = null;
            String extSystemInfo = null;
            JSONObject balances = null;
            logoResponse = null;
            connectionResponse = null;
            connection = null;
            JSONObject arrangement = arrangements.getJSONObject(i);
            Boolean portFlag = false;
            String externalIndicator = null;
            String AccIdWithCompanyId = (String) arrangement.get("linkedReference");
            if (arrangement.has("estmtEnabled")) {
                if (StringUtils.isNotBlank(arrangement.get("estmtEnabled").toString())) {
                    if (arrangement.get("estmtEnabled").toString().contentEquals("true")) {

                        // String Email = getEmail(AccIdWithCompanyId);
                        // if (StringUtils.isNoneBlank(Email)) {
                        // acDTO.setEmail(Email);
                        // acDTO.setElectronicStatementEnabled("true");
                        // } else {
                        // acDTO.setElectronicStatementEnabled("false");
                        // }
                        acDTO.setElectronicStatementEnabled("true");

                    } else {
                        acDTO.setElectronicStatementEnabled("false");
                    }
                } else {
                    acDTO.setElectronicStatementEnabled("false");
                }
            } else {
                acDTO.setElectronicStatementEnabled("false");
            }

            if (arrangement.has("externalIndicator")) {
                acDTO.setExternalIndicator((String) arrangement.get("externalIndicator"));
                externalIndicator = (String) arrangement.get("externalIndicator");
            }
            else {
            	externalIndicator = "false";
            }

			if (arrangement.has("product") && null != arrangement.get("product")
					&& StringUtils.isNotBlank(accountTypeProperties.getValue(arrangement.getString("product")))) {
				acDTO.setAccountType(accountTypeProperties.getValue((String) arrangement.get("product")));
			} else {
				continue;
			}
            
            if(arrangement.has("lendingArrangement")) {
            	JSONObject lendingArrangement = arrangement.optJSONObject("lendingArrangement");
            	if (lendingArrangement.has("currentDue")) {
            		acDTO.setCurrentDue(lendingArrangement.get("currentDue").toString() != null
            		? lendingArrangement.get("currentDue").toString()
            		: "");
            		} else {
            		acDTO.setCurrentDue("");
            		}
            	
            	if (lendingArrangement.has("dueDate")) {
            		acDTO.setDueDate(lendingArrangement.get("dueDate").toString() != null
            		? lendingArrangement.get("dueDate").toString()
            		: "");
            		} else {
            		acDTO.setDueDate("");
            		}
            	
            }
            
            if (arrangement.has("accountCategory")
                    && StringUtils.isNotBlank(arrangement.getString("accountCategory"))) {
                acDTO.setAccountCategory(arrangement.getString("accountCategory"));
            }
            if (arrangement.has("portfolioId")) {
                acDTO.setIsPortFolioAccount(Boolean.TRUE.toString());
            } else {
                acDTO.setIsPortFolioAccount(Boolean.FALSE.toString());
            }

            if (AccIdWithCompanyId != null) {

                companyObj = arrangement.getJSONObject("company");
                companyExtensionData = companyObj.getJSONObject("extensionData");
                if (companyObj.has("companyReference")) {
                    companyId = companyObj.getString("companyReference");
                }
                if (arrangement.has("extSystemInfo")) {
                    if (StringUtils.isNotBlank(externalIndicator)) {
                        if (externalIndicator.equals("true") || externalIndicator.equalsIgnoreCase("Yes")) {
                            try {
								extSystemInfo = (String) arrangement.get("extSystemInfo");
								logoResponse = getLogoURLForAccount(extSystemInfo,authToken);
								acDTO.setBankCode((String) extSystemInfo);
								acDTO.setExternalIndicator("true");
								connectionResponse = getConnectionDetails(companyPartyId, extSystemInfo,authToken);
								if (connectionResponse != null
								        && connectionResponse.getJSONArray("connectionss").length() > 0
								        && connectionResponse.getJSONArray("connectionss").getJSONObject(0) != null)
								    connection = connectionResponse.getJSONArray("connectionss").getJSONObject(0);
								// Set Expiry Date of External Account from
								// Connection Response
								if (connection != null && connection.has("expiresAt")) {
								    acDTO.setExpiresAt((String) connection.get("expiresAt"));
								}
								// Set Connection Alert Days of External Account
								// from Connection Response
								if (connection != null && connection.has("connectionAlertDays")) {
								    acDTO.setConnectionAlertDays((String) connection.get("connectionAlertDays").toString());
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								alert.prepareError(e.getMessage()).log();
							} catch (ApplicationException e) {
								// TODO Auto-generated catch block
								alert.prepareError(e.getMessage()).log();
							}
                        }
                    }
                }
                JSONObject BalanceResponse = new JSONObject();
                if(balanceFlag || "true".equalsIgnoreCase(externalIndicator) || "Yes".equalsIgnoreCase(externalIndicator) ) {
                BalanceResponse = getBalanceForAccount(AccIdWithCompanyId, companyId, authToken);
                }                
                if (BalanceResponse.has("items")) {
                    JSONArray accountBalance = BalanceResponse.getJSONArray("items");
                    if (accountBalance != null && accountBalance.length() > 0) {
                        balances = accountBalance.getJSONObject(0);
                    }
                }
            }

            // Set Values for Account DashBoard
            // Set Account ID
            String accountCompanyId = StringUtils.EMPTY;
            String accountId = StringUtils.EMPTY;
            String ArrangementId = StringUtils.EMPTY;
            String ArrangementIdWithPrefix = StringUtils.EMPTY;
            if (AccIdWithCompanyId.contains("-")) { // GB0010001-78787
                accountId = AccIdWithCompanyId.split("-")[1];
                accountCompanyId = AccIdWithCompanyId.split("-")[0];
            }
            if (arrangement.has("extArrangementId")) {
                ArrangementIdWithPrefix = (String) arrangement.getString("extArrangementId");
                if (ArrangementIdWithPrefix.contains("-"))
                    ArrangementId = ArrangementIdWithPrefix.split("-")[1];
            }

            if (StringUtils.isNotBlank(externalIndicator)
                    && (externalIndicator.equalsIgnoreCase("true") || externalIndicator.equalsIgnoreCase("Yes"))) {
                acDTO.setAccount_id(AccIdWithCompanyId);
                acDTO.setArrangementId(ArrangementIdWithPrefix);
            } else {
                acDTO.setAccount_id(accountId);
                acDTO.setArrangementId(ArrangementId);
            }

            if (logoResponse != null && logoResponse.has("logoUrl")) {
                acDTO.setLogoURL((String) logoResponse.get("logoUrl"));
            }

            // Set Account Type based on products
            if (arrangement.has("productLine")) {
                acDTO.setProductLineName(arrangement.getString("productLine"));
            }
            String product = StringUtils.EMPTY;
            if (arrangement.has("product")) {
                product = arrangement.getString("product");
                acDTO.setProduct(product);
                String ProductDesc = StringUtils.isNotBlank(AccountTypeProperties.getValue(product))
                        ? AccountTypeProperties.getValue(product)
                        : "Others";
                acDTO.setTypeDescription(ProductDesc);
            }

            // Set Account NickName
            if (StringUtils.isNotBlank(arrangement.optString("shortTitle"))) {
                acDTO.setNickName(arrangement.optString("shortTitle"));
            } else if (StringUtils.isNotBlank(arrangement.optString("productDescription"))) {
                acDTO.setNickName(arrangement.optString("productDescription"));
            } else {
                acDTO.setNickName("NA");
            }

            // Set Summary Account Details
            // Set Current Balance for Account
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
                    Date date = null;
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                    String temp = (String) balances.get("processingTime");
                    try {
                        date = (Date) formatter.parse(temp);
                    } catch (ParseException e) {
                        throw new ApplicationException(ErrorCodeEnum.ERR_20049);
                    }
                    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    String processedDate = DATE_FORMAT.format(date);
                    acDTO.setProcessingTime(processedDate);
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
                    } else if (pendingDeposit instanceof BigDecimal) {
                    	acDTO.setPendingDeposit(((BigDecimal) pendingDeposit).doubleValue());
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
                    } else if (pendingWithdrawal instanceof BigDecimal) {
                    	acDTO.setPendingWithdrawal(((BigDecimal) pendingWithdrawal).doubleValue());
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
                    } else if (dividendLastPaidAmount instanceof BigDecimal) {
                    	acDTO.setDividendLastPaidAmount(((BigDecimal) dividendLastPaidAmount).doubleValue());
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
                    } else if (dividendPaidYTD instanceof BigDecimal) {
                    	acDTO.setDividendPaidYTD(((BigDecimal) dividendPaidYTD).doubleValue());
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
                // set outstandingBalance
                if (balances.has("outstandingBalance")) {
                    double outstandingBalance = 0.00;

                    if (StringUtils.isNotBlank((balances.get("outstandingBalance").toString()))) {
                        outstandingBalance = ((Number) (balances.get("outstandingBalance"))).doubleValue();

                        outstandingBalance = outstandingBalance < 0.00 ? Math.abs(outstandingBalance)
                                : outstandingBalance;
                    }

                    acDTO.setOutstandingBalance((Double.toString(outstandingBalance)) != null
                            ? ((Double.toString(outstandingBalance)))
                            : "0.00");
                } else {
                    acDTO.setOutstandingBalance("0.00");
                }
                // set disbursedAmount
                if (balances.has("disbursedAmount")) {
                    Object disbursedAmount = balances.get("disbursedAmount");
                    if (disbursedAmount instanceof Integer) {
                        acDTO.setDisbursedAmount(Math.abs(((Number) disbursedAmount).doubleValue()));
                    }else if (disbursedAmount instanceof BigDecimal) {
                    acDTO.setDisbursedAmount(Math.abs(((BigDecimal) disbursedAmount).doubleValue()));
                    }else {
                        acDTO.setDisbursedAmount(Math.abs((double) balances.get("disbursedAmount")));
                    }
                } else {
                    acDTO.setDisbursedAmount(0.00);
                }
            }

            // Set Last Paid Date
            if (arrangement.has("lastPaymentDate")) {
                acDTO.setLastPaymentDate(arrangement.getString("lastPaymentDate"));
            }

            // Set values for Account Information
            // Set Company Id
            if (companyId != null) {
                acDTO.setCompanyCode(companyId);
            } else {
                acDTO.setCompanyCode(accountCompanyId);
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
            String shortTitle = "";
            JSONArray roles = arrangement.getJSONArray("roles");
            JsonObject priAccHolder = new JsonObject();
            JsonArray jointHolders = new JsonArray();
            for (int j = 0; j < roles.length(); j++) {
                JSONObject role = roles.getJSONObject(j);
                String partyRole = StringUtils.EMPTY;
                if (role.has("partyRole")) {
                    partyRole = role.getString("partyRole");
                }
                acDTO.setPartyRole(partyRole);
                if (partyRole.equalsIgnoreCase("OWNER")) {
                    if (role.has("extensionData")) {
                        JSONObject customerNames = role.getJSONObject("extensionData");
                        if (customerNames.has("shortName")) {
                            JSONArray shortNameArray = customerNames.getJSONArray("shortName");
                            if (shortNameArray != null && shortNameArray.length() != 0) {
                                JSONObject shortNameObj = shortNameArray.getJSONObject(0);
                                if (shortNameObj.has("languageDescription")
                                        && StringUtils.isNotBlank(shortNameObj.getString("languageDescription"))) {
                                    priAccHolder.addProperty("username", shortNameObj.getString("languageDescription"));
                                    priAccHolder.addProperty("fullname", shortNameObj.getString("languageDescription"));
                                    shortTitle = shortNameObj.getString("languageDescription");
                                }
                            }
                        }
                        continue;
                    }

                }
                if (partyRole.equalsIgnoreCase("JOINT.OWNER")) {
                    JsonObject jointHolder = new JsonObject();
                    if (role.has("extensionData")) {
                        JSONObject customerNames = role.getJSONObject("extensionData");
                        if (customerNames.has("shortName")) {
                            JSONArray shortNameArray = customerNames.getJSONArray("shortName");
                            if (shortNameArray != null && shortNameArray.length() != 0) {
                                JSONObject shortNameObj = shortNameArray.getJSONObject(0);
                                if (shortNameObj.has("languageDescription")
                                        && StringUtils.isNotBlank(shortNameObj.getString("languageDescription"))) {
                                    jointHolder.addProperty("username", shortNameObj.getString("languageDescription"));
                                    jointHolder.addProperty("fullname", shortNameObj.getString("languageDescription"));
                                    jointHolder.addProperty("customerId", role.getString("partyId"));
                                }
                            }
                        }
                    }
                    jointHolders.add(jointHolder);
                }
            }
            acDTO.setAccountHolder(priAccHolder.toString());
            acDTO.setJointHolders(jointHolders.toString());
            
            // Set Product Description as account Name
            if (StringUtils.isNotBlank(shortTitle)) {
                acDTO.setAccountName(shortTitle);
                acDTO.setDisplayName(shortTitle);
            } else {
                if (arrangement.has("productDescription")) {
                    acDTO.setAccountName(arrangement.getString("productDescription"));
                    acDTO.setDisplayName(arrangement.getString("productDescription"));
                }
            }

            // Set IBAN number
            if (arrangement.has("iban")) {
                acDTO.setIBAN(arrangement.getString("iban"));
            }
            // Set Support Flag fields
            if (StringUtils.isNotBlank(externalIndicator)) {
                if (externalIndicator.equals("false") || externalIndicator.equals("No")) {
                    acDTO.setSupportBillPay("1");
                    acDTO.setSupportChecks("1");
                    acDTO.setSupportDeposit("1");
                    acDTO.setSupportTransferFrom("1");
                    acDTO.setSupportTransferTo("1");
                }
            }
            acDTO.setIsBusinessAccount("false");

            // Set Favourite Status
            String favouriteStatus = favouriteAccounts.get(acDTO.getAccount_id()) != null
                  ? favouriteAccounts.get(acDTO.getAccount_id())
                    : "0";
            acDTO.setFavouriteStatus(favouriteStatus);

            // Set userName
            acDTO.setUserName(inputPayload.getUserName());
            acDTO.setIsTypeBusiness("0");

            // Set CustomrId
            acDTO.setCustomerID(inputPayload.getCustomerID());

            AccountsDTO accountsObject = new AccountsDTO();

            accountsObject.setAccountId(acDTO.getAccount_id());

            if (StringUtils.isNotBlank(acDTO.getAccountName())) {
                accountsObject.setAccountName(acDTO.getAccountName());
            }
            accountsObject.setAccountType(acDTO.getTypeDescription());
            if (StringUtils.isNotBlank(acDTO.getArrangementId())) {
                accountsObject.setArrangementId(acDTO.getArrangementId());
            }
            if (StringUtils.isNotBlank(acDTO.getCurrencyCode())) {
                accountsObject.setCurrencyCode(acDTO.getCurrencyCode());
            }
            if (StringUtils.isNotBlank(product)) {
                accountsObject.setProductId(product);
            }

            accountsObject.setCompanyCode(accountCompanyId);
            accountsObject.setAccountIdWithCompanyId(AccIdWithCompanyId);

            accounts.put(accountsObject.getAccountId(), accountsObject);

            // Add the account DTO in arrangement DTo List
            arrangementsDTO.add(acDTO);
            }
        //wealth accounts integration
        if (pCount>0) {
            pIDsCSV = pIDsCSV.substring(1);
            Map<String, Object> balInputMap = new HashMap<>();
            Map<String, Object> balHeaderMap = new HashMap<>();
            String HoldingsResponse;
            balHeaderMap = ArrangementsUtils.generateSecurityHeadersForHMS(authToken, headerMap);
            try {
            	balInputMap.put("portfolioId", pIDsCSV);
            	balInputMap.put(Constants.LOOP_COUNT,pCount);
            	balInputMap.put(Constants.LOOP_SEPERATOR,",");
                HoldingsResponse = Executor.invokeService(
                        ArrangementsAPIServices.ARRANGEMENTSORCHSERVICESJSON_GETUSERPORTFOLIODETAILS, balInputMap, balHeaderMap);
                alert.prepareError("HMS Response" + HoldingsResponse).log();
            } catch (Exception e) {
                alert.prepareError("Unable to fetch Holdings Response " + e).log();
                throw new ApplicationException(ErrorCodeEnum.ERR_20049);
            }
            JSONObject PortfolioResponses = null;
    		if (StringUtils.isNotBlank(HoldingsResponse)) {
    			PortfolioResponses = new JSONObject(HoldingsResponse);
            }
    		JSONArray ds =  (JSONArray) PortfolioResponses.get("LoopDataset");
    		for(int dsi=0; dsi<ds.length();dsi++) {
    			ArrangementsDTO acDTO = new ArrangementsDTO();
    			try {
    			JSONObject portfolio =  (JSONObject) ds.get(dsi);
    			JSONArray values = portfolio.getJSONArray("portfolioValuess");
    			JSONObject value = values.getJSONObject(0);
    			JSONObject portfolioDet =portfolioAccMap.get(value.get("portfolioId"));
    			String companyId = portfolioDet.has("company")?portfolioDet.getJSONObject("company").getString("companyReference"):"";
    			String pid =  value.has("portfolioId") ? value.getString("portfolioId"):"";
    			String accountId = pid.replace(companyId+"-","");
    			acDTO.setPortfolioId(pid);
    			acDTO.setAccountType(Constants.INVESTMENTACC);
    			acDTO.setTypeDescription(Constants.INVESTMENTACC);
    			acDTO.setAccount_id(accountId);
    			acDTO.setCompanyCode(companyId);
    			acDTO.setCurrencyCode(value.has("currencyId")?value.getString("currencyId"):"");
    			acDTO.setIsPortFolioAccount(Boolean.toString(portfolioDet.has("isPortFolioAccount")?portfolioDet.getBoolean("isPortFolioAccount"):true));
    			acDTO.setMembership_id(value.has("customerId")?value.getString("customerId"):"");
    			acDTO.setProductGroup(portfolioDet.has("productGroup")?portfolioDet.getString("productGroup"):"");
    			acDTO.setProduct(portfolioDet.has("product")?portfolioDet.getString("product"):"");
    			acDTO.setExternalIndicator(portfolioDet.has("externalIndicator")?portfolioDet.getString("externalIndicator"):"");
    			acDTO.setArrangementId(accountId);
    			acDTO.setMarketValue(value.has("marketValue")?value.get("marketValue").toString():"");
    			acDTO.setValueDate(value.has("valueDate")?value.getString("valueDate"):"");
    			acDTO.setServiceType(portfolioDet.has("productDescription")?portfolioDet.getString("productDescription"):"");
    			acDTO.setAccountName(portfolioDet.has("shortTitle")?portfolioDet.getString("shortTitle"):"");
    			acDTO.setPartyRole(Constants.ROLE_OWNER);
    			//acDTO.setAccountType(accountTypeProperties.getValue(portfolioDet.getString("product")));
    			AccountsDTO accountsObject = new AccountsDTO();
				accountsObject.setAccountId(acDTO.getAccount_id());				
				if (StringUtils.isNotBlank(acDTO.getAccountName())) {
				    accountsObject.setAccountName(acDTO.getAccountName());
				}
				accountsObject.setAccountType(acDTO.getTypeDescription());
				if (StringUtils.isNotBlank(acDTO.getArrangementId())) {
				    accountsObject.setArrangementId(acDTO.getArrangementId());
				}
				if (StringUtils.isNotBlank(acDTO.getCurrencyCode())) {
				    accountsObject.setCurrencyCode(acDTO.getCurrencyCode());
				}
				accountsObject.setProductId(acDTO.getProduct());			
				accountsObject.setCompanyCode(companyId);
				accountsObject.setAccountIdWithCompanyId(pid);				
				accounts.put(accountsObject.getAccountId(), accountsObject);
    			arrangementsDTO.add(acDTO);    			
    			}
    			catch(Exception e) {
    				alert.prepareError(e.toString()).log();
    			}
    		}
        }
        if (accountID == null) {
            Gson gson = new Gson();
            String gsonAccounts = gson.toJson(accounts);
            ArrangementsUtils.insertIntoSession("Accounts", gsonAccounts, request);
        }
        resp.add(arrangementsDTO);
        
        return resp;

    }

    private JSONObject getBalanceForAccount(String accId, String companyId, String authToken)
            throws ApplicationException {

        String AccountId = accId;
        HashMap<String, Object> balInputMap = new HashMap<>();
        Map<String, Object> balHeaderMap = new HashMap<>();
        balInputMap.put("accountId", AccountId);
        balInputMap.put("accountID", AccountId);
        balHeaderMap = ArrangementsUtils.generateSecurityHeadersForHMS(authToken, balHeaderMap);

        String BalanceResponse = null;
        JSONObject Balance = new JSONObject();
        diagnostic.prepareDebug("Holdings Balance Request" + balInputMap.toString()).log();
        try {
            BalanceResponse = Executor.invokeService(
                    ArrangementsAPIServices.HOLDINGSMICROSERVICESJSON_GETACCOUNTBALANCES, balInputMap, balHeaderMap);

        } catch (Exception e) {
            alert.prepareError("Unable to fetch balances " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20042);
        }
        diagnostic.prepareDebug("Holdings Balance Response" + BalanceResponse).log();
        if (StringUtils.isNotBlank(BalanceResponse)) {
            Balance = new JSONObject(BalanceResponse);
        }
        return Balance;
    }

    // TODO:Modify Method Signature - Change response to DTO
    private JSONObject getLogoURLForAccount(String extSystemInfo, String authToken) throws ApplicationException {
        HashMap<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("extSystemInfo", extSystemInfo);
        JSONObject logo = new JSONObject();
        String logoResponse = null;
        diagnostic.prepareDebug("Invoking Logo Request" + inputMap.toString()).log();
        headerMap=generateSecurityHeadersForArrangements(authToken,headerMap);
        try {
            logoResponse = Executor.invokeService(ArrangementsAPIServices.ACCOUNTAGGREGATIONJSON_GETLOGOURL, inputMap,
                    headerMap);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch logo URL " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20043);
        }
        diagnostic.prepareDebug("Invoking Logo Response" + logoResponse).log();
        if (StringUtils.isNotBlank(logoResponse)) {
            // Validate service response before converting to JSON Object
            logo = new JSONObject(logoResponse);
        }
        return logo;
    }

    /**
     * method to get expiry date and connection Details
     * 
     * 
     * @return JSONObject accountTypeType
     */
    private JSONObject getConnectionDetails(String partyId, String bankCode, String authToken) throws ApplicationException {
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("partyId", partyId);
        inputMap.put("bankCode", bankCode);
        JSONObject connectionDetails = new JSONObject();
        String connectionResponse = null;
        diagnostic.prepareDebug("Invoking AAG Connection Details Request" + inputMap.toString()).log();
        Map<String, Object> headerMap = new HashMap<>();
        headerMap=generateSecurityHeadersForArrangements(authToken,headerMap);
        try {
            connectionResponse = Executor
                    .invokeService(ArrangementsAPIServices.ACCOUNTAGGREGATIONJSON_GETCONNECTIONDETAILS, inputMap, headerMap);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Connection Details" + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20051);
        }
        diagnostic.prepareDebug("AAG Connection Details Response" + connectionResponse).log();
        if (StringUtils.isNotBlank(connectionResponse)) {
            // Validate service response before converting to JSON Object
            connectionDetails = new JSONObject(connectionResponse);
        }
        return connectionDetails;
    }

    public HashMap<String, String> getFavouriteAccounts(String dbxCustomerId, DataControllerRequest request)
            throws ApplicationException {

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        HashMap<String, Object> headerParams = new HashMap<String, Object>();
        inputParams.put("$filter", "User_id eq " + dbxCustomerId);
        request.addRequestParam_("$filter", "User_id eq " + dbxCustomerId);

        String DBXCusAccDetails = "";
        diagnostic.prepareDebug("Invoking Favourite Accounts Request" + inputParams.toString()).log();

        try {
            DBXCusAccDetails = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS.getServiceName())

                    .withOperationId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS.getOperationName())

                    .withRequestParameters(inputParams).withRequestHeaders(headerParams)
                    .withDataControllerRequest(request).build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Unable to fetch favourite accounts").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20046);

        }
        diagnostic.prepareDebug("Favourite Accounts Response" + DBXCusAccDetails).log();
        Result dbxResult = new Result();
        if (StringUtils.isNotBlank(DBXCusAccDetails)) {
            dbxResult = JSONToResult.convert(new JSONObject(DBXCusAccDetails).toString());
        }

        Dataset getAccountsDS = dbxResult.getDatasetById(TemenosConstants.DS_DB_ACCOUNTS);
        HashMap<String, String> favouriteAccounts = new HashMap<String, String>();

        if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
            alert.prepareError("if true").log();
            for (Record rec : getAccountsDS.getAllRecords()) {
                favouriteAccounts.put(rec.getParamValueByName(TemenosConstants.DB_ACCOUNTID),
                        rec.getParamValueByName(TemenosConstants.DB_FAVOURITESTATUS));
            }
        }
        return favouriteAccounts;
    }

    /**
     * method to get the email associated with an account from dbxdb
     *
     * 
     * @return String email value
     */
    public String getEmail(String Account_Id) throws ApplicationException {

        HashMap<String, Object> inputParams = new HashMap<String, Object>();
        HashMap<String, Object> headerParams = new HashMap<String, Object>();

        String DBXCusAccDetails = "";
        String Email = null;

        String[] parts = Account_Id.split("-");
        String AccId = parts[1];

        try {
            DBXCusAccDetails = DBPServiceExecutorBuilder.builder()
                    .withServiceId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS.getServiceName())

                    .withOperationId(ArrangementsAPIServices.DBXUSER_GET_DBXCUSTOMERACCOUNTDETAILS.getOperationName())

                    .withRequestParameters(inputParams).withRequestHeaders(headerParams).build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20046);
        }
        Result dbxResult = new Result();
        if (StringUtils.isNotBlank(DBXCusAccDetails)) {
            dbxResult = JSONToResult.convert(new JSONObject(DBXCusAccDetails).toString());
        }

        Dataset getAccountsDS = dbxResult.getDatasetById(TemenosConstants.DS_DB_ACCOUNTS);

        if (getAccountsDS != null && !getAccountsDS.getAllRecords().isEmpty()) {
            alert.prepareError("if true").log();
            for (Record rec : getAccountsDS.getAllRecords()) {

                if (rec.getParamValueByName(TemenosConstants.DB_ACCOUNTID).contentEquals(AccId)) {
                    Email = rec.getParamValueByName(TemenosConstants.DB_EMAIL);
                    break;
                }
            }
        }
        return Email;
    }
        private Map<String, Object> generateSecurityHeadersForArrangements(String authToken, Map<String, Object> headerMap) {
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
    }

