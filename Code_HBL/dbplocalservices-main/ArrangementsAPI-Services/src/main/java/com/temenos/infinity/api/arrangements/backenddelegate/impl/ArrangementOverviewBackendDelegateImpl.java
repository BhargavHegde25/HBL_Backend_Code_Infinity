package com.temenos.infinity.api.arrangements.backenddelegate.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infinity.dbx.dbp.jwt.auth.AuthConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.backenddelegate.api.ArrangementOverviewBackendDelegate;
import com.temenos.infinity.api.arrangements.config.ArrangementsAPIServices;
import com.temenos.infinity.api.arrangements.config.ServerConfigurations;
import com.temenos.infinity.api.arrangements.constants.Constants;
import com.temenos.infinity.api.arrangements.constants.ErrorCodeEnum;
import com.temenos.infinity.api.arrangements.constants.MSCertificateConstants;
import com.temenos.infinity.api.arrangements.dto.AccountsDTO;
import com.temenos.infinity.api.arrangements.dto.ArrangementsDTO;
import com.temenos.infinity.api.arrangements.prop.AccountTypeProperties;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;
import com.temenos.infinity.api.arrangements.utils.CommonUtils;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;

/**
 * 
 * @author smugesh
 * @version 1.0 Extends the {@link ArrangementOverviewBackendDelegate}
 */
public class ArrangementOverviewBackendDelegateImpl implements ArrangementOverviewBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    /**
     * method to get the Accounts
     * 
     * @return List<ArrangementsDTO> of Arrangement Accounts
     */
    @Override
    public List<ArrangementsDTO> getArrangementOverview(ArrangementsDTO inputPayload, DataControllerRequest request,
            String authToken) throws ApplicationException {

        // Get Request parameters from Request Object
        String accountID = inputPayload.getAccount_id();
        String companyPartyId = inputPayload.getBackendUserId();
        
        //Load Account Properties
        AccountTypeProperties accountTypeProperties = new AccountTypeProperties(request);
        
        

        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        String mortgageString = null;
		/*
		 * if(accountID.equalsIgnoreCase("MORT12345")) {
		 * 
		 * try { mortgageString =
		 * Executor.invokePassThroughServiceAndGetString((InfinityServices)
		 * ArrangementsAPIServices.MOCKMORTGAGEMS_GETMORTGAGEDETAILS, inputMap,
		 * headerMap); alert.prepareError("AMS Response" + mortgageString).log(); } catch (Exception e)
		 * { alert.prepareError("Unable to fetch Arrangements " + e).log(); } JSONArray mortgages = new
		 * JSONArray(mortgageString); JSONObject mortgage = mortgages.getJSONObject(0);
		 * List<ArrangementsDTO> mDTOs = new ArrayList<>(); ArrangementsDTO mDTO = new
		 * ArrangementsDTO(); mDTO.setAccount_id(mortgage.has("accountID") ?
		 * mortgage.get("accountID").toString() : ""); mDTO.setIBAN(mortgage.has("IBAN")
		 * ? mortgage.get("IBAN").toString() : "");
		 * mDTO.setOriginalAmount(mortgage.has("originalAmount") ?
		 * mortgage.get("originalAmount").toString() : "");
		 * mDTO.setNextPaymentAmount(mortgage.has("nextPaymentAmount") ?
		 * mortgage.get("nextPaymentAmount").toString() : "");
		 * mDTO.setPaymentDue(mortgage.has("NextPaymentDue") ?
		 * mortgage.get("NextPaymentDue").toString() : "");
		 * mDTO.setOutstandingBalance(mortgage.has("outstandingBalance") ?
		 * mortgage.get("outstandingBalance").toString() : "");
		 * mDTO.setPaidInstallmentsCount(mortgage.has("paidInstallmentsCount") ?
		 * mortgage.get("paidInstallmentsCount").toString() : "");
		 * mDTO.setOverDueInstallmentsCount(mortgage.has("overDueInstallmentsCount") ?
		 * mortgage.get("overDueInstallmentsCount").toString() : "");
		 * mDTO.setFutureInstallmentsCount(mortgage.has("futureInstallmentsCount") ?
		 * mortgage.get("futureInstallmentsCount").toString() : "");
		 * mDTO.setInterestPaidYTD(mortgage.has("interestPaidYTD") ?
		 * mortgage.get("interestPaidYTD").toString() : "");
		 * mDTO.setLastPaymentAmount(mortgage.has("lastPaymentAmount") ?
		 * mortgage.get("lastPaymentAmount").toString() : "");
		 * mDTO.setLastPaymentDate(mortgage.has("lastPaymentDate") ?
		 * mortgage.get("lastPaymentDate").toString() : "");
		 * mDTO.setRePaymentFrequency(mortgage.has("rePaymentFrequency") ?
		 * mortgage.get("rePaymentFrequency").toString() : "");
		 * mDTO.setInterestRate(mortgage.has("interestRate") ?
		 * mortgage.get("interestRate").toString() : "");
		 * mDTO.setSanctionedDate(mortgage.has("sanctionedDate") ?
		 * mortgage.get("sanctionedDate").toString() : "");
		 * mDTO.setMaturityDate(mortgage.has("maturityDate") ?
		 * mortgage.get("maturityDate").toString() : "");
		 * mDTO.setTermAmount(mortgage.has("sanctionedAmount") ?
		 * mortgage.get("sanctionedAmount").toString() : ""); mDTOs.add(mDTO); return
		 * mDTOs; }
		 */
      
      
        inputMap.put("accountID", accountID);
        headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);
        headerMap.put("companyid",CommonUtils.getCompanyId(request));
        CommonUtils.setCompanyIdToRequest(request);
        // Hit Arrangements Micro Service
        String ArrangementResponse = null;
        JSONObject logoResponse = null;
        JSONObject connectionResponse = null;
        JSONObject connection = null;
        JSONObject arrangement = null;
        JSONObject ArrResponse = null;
        try {
            ArrangementResponse = Executor.invokePassThroughServiceAndGetString(
                    ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTOVERVIEW, inputMap, headerMap);
            alert.prepareError("ArrangementResponse Response: " + ArrangementResponse).log();
            if (StringUtils.isNotBlank(ArrangementResponse)) {
                ArrResponse = new JSONObject(ArrangementResponse);
            }

            arrangement = ArrResponse.getJSONArray("arrangementResponses").getJSONObject(0);

        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        // Initialize Variables

        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();
        JSONObject companyObj = null;
        ArrangementsDTO acDTO = new ArrangementsDTO();
        JSONObject companyExtensionData = null;
        String companyId = null;
        String extSystemInfo = null;
        JSONObject balances = null;
        logoResponse = null;
        connectionResponse = null;
        connection = null;
        String AccIdWithCompanyId = (String) arrangement.get("linkedReference");
        String externalIndicator = null;
        if (arrangement.has("externalIndicator")) {
            acDTO.setExternalIndicator((String) arrangement.get("externalIndicator"));
            externalIndicator = (String) arrangement.get("externalIndicator");
        }
        if (AccIdWithCompanyId != null ) {
            companyObj = arrangement.getJSONObject("company");
            companyExtensionData = companyObj.getJSONObject("extensionData");
            if (companyObj.has("companyReference")) {
                companyId = companyObj.getString("companyReference");
            }
            if (arrangement.has("extSystemInfo") && StringUtils.isNotBlank(companyPartyId)) {
                if (StringUtils.isNotBlank(externalIndicator)) {
                    if (externalIndicator.equals("true") || externalIndicator.equalsIgnoreCase("Yes")) {
                        extSystemInfo = (String) arrangement.get("extSystemInfo");
                        logoResponse = getLogoURLForAccount(extSystemInfo, authToken);
                        acDTO.setBankCode((String) extSystemInfo);
                        if(StringUtils.isNotBlank(companyPartyId)) {
                            connectionResponse = getConnectionDetails(companyPartyId, extSystemInfo,authToken);
                            if (connectionResponse != null && connectionResponse.getJSONArray("connectionss").length() > 0
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
                        }
                    }
                }
            }
            JSONObject BalanceResponse = getBalanceForAccount(AccIdWithCompanyId, companyId, authToken);
            alert.prepareError("BalanceResponse Response: " + BalanceResponse).log();
            if (BalanceResponse.has("items")) {
                JSONArray accountBalance = BalanceResponse.getJSONArray("items");
                if (accountBalance != null && accountBalance.length() > 0) {
                    balances = accountBalance.getJSONObject(0);
                }
            }
        }
        try {
        // Set Values for Account DashBoard
        // Set Account ID
        String accountCompanyId = StringUtils.EMPTY;
        String accountId = StringUtils.EMPTY;
        String ArrangementId = StringUtils.EMPTY;
        String ArrangementIdWithPrefix = StringUtils.EMPTY;
        if (AccIdWithCompanyId.contains("-")) {
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
            if (arrangement.getString("productLine").equalsIgnoreCase("LENDING")) {
				if (arrangement.has("lendingArrangement")) {
					JSONObject lendingArrangement = arrangement.getJSONObject("lendingArrangement");
					if (lendingArrangement.has("lastPaymentAmount")) {
						acDTO.setLastPaymentAmount(lendingArrangement.get("lastPaymentAmount").toString() != null
								? lendingArrangement.get("lastPaymentAmount").toString()
								: "");
					} else {
						acDTO.setLastPaymentAmount("");
					}
					if (lendingArrangement.has("totalDueAmount")) {
						acDTO.setTotalDueAmount(lendingArrangement.get("totalDueAmount").toString() != null
								? lendingArrangement.get("totalDueAmount").toString()
								: "");
					} else {
						acDTO.setTotalDueAmount("");
					}
					if (lendingArrangement.has("termAmount")) {
						acDTO.setTermAmount((lendingArrangement.get("termAmount").toString()) != null
								? (lendingArrangement.get("termAmount").toString())
								: "");
					} else {
						acDTO.setTermAmount("");
					}
					if (lendingArrangement.has("lastPaymentDate")) {
						acDTO.setLastPaymentDate((lendingArrangement.getString("lastPaymentDate")) != null
								? (lendingArrangement.getString("lastPaymentDate"))
								: "");
					} else {
						acDTO.setLastPaymentDate("");
					}
					if (lendingArrangement.has("sanctionedDate")) {
						acDTO.setSanctionedDate((lendingArrangement.getString("sanctionedDate")) != null
								? (lendingArrangement.getString("sanctionedDate"))
								: "");
					} else {
						acDTO.setSanctionedDate("");
					}
					if (lendingArrangement.has("maturityDate")) {
						acDTO.setMaturityDate((lendingArrangement.getString("maturityDate")) != null
								? (lendingArrangement.getString("maturityDate"))
								: "");
					} else {
						acDTO.setMaturityDate("");
					}
					if (lendingArrangement.has("totalDueAmount")) {
						acDTO.setTotalDueAmount(lendingArrangement.get("totalDueAmount").toString() != null
								? lendingArrangement.get("totalDueAmount").toString()
								: "");
					} else {
						acDTO.setTotalDueAmount("");
					}
					if (lendingArrangement.has("currentDue")) {
						acDTO.setNextPaymentAmount(lendingArrangement.get("currentDue").toString() != null
								? lendingArrangement.get("currentDue").toString()
								: "");
					} else {
						acDTO.setTotalDueAmount("");
					}
					if (lendingArrangement.has("dueDate")) {
						acDTO.setNextPaymentDate(lendingArrangement.get("dueDate").toString() != null
								? lendingArrangement.get("dueDate").toString()
								: "");
					} else {
						acDTO.setTotalDueAmount("");
					}
				} else {
					acDTO.setLastPaymentAmount("");
					acDTO.setTotalDueAmount("");
					acDTO.setTermAmount("");
					acDTO.setLastPaymentDate("");
					acDTO.setSanctionedDate("");
					acDTO.setMaturityDate("");
					acDTO.setNextPaymentAmount("");
					acDTO.setNextPaymentDate("");

				}
				if (arrangement.has("interests")) {
					JSONArray interests = null;
					JSONObject interest = new JSONObject();
					JSONObject obj =  new JSONObject();
					interests = arrangement.getJSONArray("interests");
					for (int i = 0; i < interests.length(); i++) {
						 obj = interests.getJSONObject(i);
						if (obj.has("interestPropertyName")) {
							if (obj.getString("interestPropertyName").equalsIgnoreCase("PRINCIPALINT")) {
								interest = obj;
								break;
							}
						}
					}
					if (interest.has("interestFrequency")) {
						acDTO.setRePaymentFrequency(interest.get("interestFrequency").toString() != null
								? interest.get("interestFrequency").toString()
								: "");
					} else {
						acDTO.setRePaymentFrequency("");
					}
					if (interest.has("interestRate")) {
						acDTO.setInterestRate(interest.get("interestRate").toString() != null
								? interest.get("interestRate").toString()
								: "");
					} else {
						acDTO.setInterestRate("");
					}
					if (interest.has("interestYtd")) {
						acDTO.setInterestPaidYTD(interest.get("interestYtd").toString() != null
								? interest.get("interestYtd").toString()
								: "");
					} else {
						acDTO.setInterestPaidYTD("");
					}
				}
				else
				{
					acDTO.setRePaymentFrequency("");
					acDTO.setInterestRate("");
					acDTO.setInterestPaidYTD("");
				}
			}
			if (arrangement.getString("productLine").equalsIgnoreCase("DEPOSITS")
					|| arrangement.getString("productLine").equalsIgnoreCase("ACCOUNTS")) {
				if (arrangement.has("interests")) {
					JSONArray interests = new JSONArray();
					JSONObject interest = new JSONObject();

					if (arrangement.getString("productLine").equalsIgnoreCase("DEPOSITS")) {
						interests = arrangement.getJSONArray("interests");
						interest = interests.getJSONObject(0);
					}
					if (arrangement.getString("productLine").equalsIgnoreCase("ACCOUNTS")) {
						interests = arrangement.getJSONArray("interests");
						for (int i = 0; i < interests.length(); i++) {
							JSONObject obj = interests.getJSONObject(i);
							if (obj.has("interestPropertyName")) {
								if (obj.getString("interestPropertyName").equalsIgnoreCase("CRINTEREST")) {
									interest = obj;
									
									if (interest.has("interestRate")) {
										try {
											String interestRate = interest.get("interestRate").toString();
											acDTO.setDividendRate(Float.parseFloat(interestRate));
										} catch (Exception e) {
											// TODO Auto-generated catch block
											alert.prepareError(e.getMessage()).log();
										} 
									}
									
									break;
								}
							}
						}
					}

					if (interest.has("interestRate")) {
						acDTO.setInterestRate(interest.get("interestRate").toString() != null
								? interest.get("interestRate").toString()
								: "");
					} else {
						acDTO.setInterestRate("");
					}
					if (interest.has("interestAccrued")) {
						acDTO.setAccruedInterest(interest.get("interestAccrued").toString() != null
								? interest.get("interestAccrued").toString()
								: "");
					} else {
						acDTO.setAccruedInterest("");
					}
					if (interest.has("lastPeriodEndDate")) {
						acDTO.setLastPaymentDate(interest.get("lastPeriodEndDate").toString() != null
								? interest.get("lastPeriodEndDate").toString()
								: "");
					} else {
						acDTO.setLastPaymentDate("");
					}
					if (interest.has("interestYtd")) {
						Object interestYtd = interest.get("interestYtd");
						if (interestYtd instanceof Integer) {
							acDTO.setDividendPaidYTD(((Number) interestYtd).doubleValue());
						} else if (interestYtd instanceof BigDecimal) {
	                    	acDTO.setDividendPaidYTD(((BigDecimal) interestYtd).doubleValue());
	                    } else {
							acDTO.setDividendPaidYTD((double) interest.get("interestYtd"));
						}
					} else {
						acDTO.setDividendPaidYTD(0.00);
					}
					if (interest.has("lastPeriodInterest")) {
						Object lastPeriodInterest = interest.get("lastPeriodInterest");
						if (lastPeriodInterest instanceof Integer) {
							acDTO.setDividendLastPaidAmount(((Number) lastPeriodInterest).doubleValue());
						} else if (lastPeriodInterest instanceof BigDecimal) {
	                    	acDTO.setDividendLastPaidAmount(((BigDecimal) lastPeriodInterest).doubleValue());
	                    } else {
							acDTO.setDividendLastPaidAmount((double) interest.get("lastPeriodInterest"));
						}
					} else {
						acDTO.setDividendLastPaidAmount(0.00);
					}
					if (arrangement.getString("productLine").equalsIgnoreCase("DEPOSITS")) {
						if (interest.has("interestDue")) {
							acDTO.setTermInterestDue(interest.get("interestDue").toString() != null
									? interest.get("interestDue").toString()
									: "");
						} else {
							acDTO.setTermInterestDue("");
						}
						if (interest.has("interestDueDate")) {
							acDTO.setInterestDueDate(interest.get("interestDueDate").toString() != null
									? interest.get("interestDueDate").toString()
									: "");
						} else {
							acDTO.setInterestDueDate("");
						}
					}
				} else {
					acDTO.setInterestRate("");
					acDTO.setAccruedInterest("");
					acDTO.setLastPaymentDate("");
					acDTO.setDividendPaidYTD(0.00);
					acDTO.setDividendLastPaidAmount(0.00);
				}
			}
		}
        String product = StringUtils.EMPTY;
        if (arrangement.has("product")) {
            product = arrangement.getString("product");
            acDTO.setProduct(product);
            String ProductDesc = StringUtils.isNotBlank(AccountTypeProperties.getValue(product))
                    ? AccountTypeProperties.getValue(product) : "Others";
            acDTO.setTypeDescription(ProductDesc);
            if (acDTO.getTypeDescription() != null && acDTO.getTypeDescription().equalsIgnoreCase("Loan")) {
                try {
                    String getScheduleCounts = Executor.invokePassThroughServiceAndGetString(
                            ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETLOANINSTALLMENTOVERVIEW, inputMap,
                            headerMap);
                    JSONObject schedules = new JSONObject(getScheduleCounts);
                    String futureCount = "0";
                    String paidCount = "0";
                    String overDueCount = "0";
                    if (schedules.has("scheduleStatus")) {
                        JSONObject scheduleCounts = schedules.getJSONObject("scheduleStatus");
                        if (scheduleCounts.has("futureScheduleCount")
                                && scheduleCounts.get("futureScheduleCount") != null
                                && StringUtils.isNotBlank(scheduleCounts.get("futureScheduleCount").toString())) {
                            futureCount = scheduleCounts.get("futureScheduleCount").toString();
                        }
                        if (scheduleCounts.has("paidScheduleCount") && scheduleCounts.get("paidScheduleCount") != null
                                && StringUtils.isNotBlank(scheduleCounts.get("paidScheduleCount").toString())) {
                            paidCount = scheduleCounts.get("paidScheduleCount").toString();
                        }
                        if (scheduleCounts.has("dueScheduleCount") && scheduleCounts.get("dueScheduleCount") != null
                                && StringUtils.isNotBlank(scheduleCounts.get("dueScheduleCount").toString())) {
                            overDueCount = scheduleCounts.get("dueScheduleCount").toString();
                        }
                    }
                    acDTO.setFutureInstallmentsCount(futureCount);
                    acDTO.setPaidInstallmentsCount(paidCount);
                    acDTO.setOverDueInstallmentsCount(overDueCount);

                } catch (Exception e) {
                    alert.prepareError("Error while fetching schedules").log();
                }
//                if (arrangement.has("outstandingBalance")) {
//                    acDTO.setOutstandingBalance((String) arrangement.get("outstandingBalance"));
//                } else {
//                    acDTO.setOutstandingBalance("0");
//                }

            }
        }
        if (acDTO.getTypeDescription() == null) {
            String ProductDesc = StringUtils.isNotBlank(AccountTypeProperties.getValue(product))
                    ? AccountTypeProperties.getValue(product) : "Others";
            acDTO.setTypeDescription(ProductDesc);
        } 

        // Set Account NickName
        if (arrangement.has("shortTitle")) {
            acDTO.setNickName(arrangement.getString("shortTitle"));
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
                //acDTO.setPendingDeposit(0.00);
            	//pendingDeposit = onlineActualBalance - availableBalance
            	if(balances.has("availableBalance") && balances.has("onlineActualBalance")) {
            		//getCurrent Balance is onlineActualBalance
                	double pendingDeposit = acDTO.getCurrentBalance() - acDTO.getAvailableBalance();
                	acDTO.setPendingDeposit(pendingDeposit);
            	}else {
            	acDTO.setPendingDeposit(0.00);
            	}

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

//            if (balances.has("dividendLastPaidAmount")) {
//                Object dividendLastPaidAmount = balances.get("dividendLastPaidAmount");
//                if (dividendLastPaidAmount instanceof Integer) {
//                    acDTO.setDividendLastPaidAmount(((Number) dividendLastPaidAmount).doubleValue());
//                } else {
//                    acDTO.setDividendLastPaidAmount((double) balances.get("dividendLastPaidAmount"));
//                }
//            } else {
//                acDTO.setDividendLastPaidAmount(0.00);
//            }
            // Set Dividend Last Paid Date
            if (balances.has("dividendLastPaidDate")) {
                acDTO.setDividendLastPaidDate(balances.getString("dividendLastPaidDate"));
            }
//            if (balances.has("dividendPaidYTD")) {
//                Object dividendPaidYTD = balances.get("dividendPaidYTD");
//                if (dividendPaidYTD instanceof Integer) {
//                    acDTO.setDividendPaidYTD(((Number) dividendPaidYTD).doubleValue());
//                } else {
//                    acDTO.setDividendPaidYTD((double) balances.get("dividendPaidYTD"));
//                }
//            } else {
//                acDTO.setDividendPaidYTD(0.00);
//            }

            // Set Dividend Rate
            if (balances.has("dividendRate")) {
                acDTO.setDividendRate((float) balances.get("dividendRate"));
            }
          //set outstandingBalance
			if (balances.has("outstandingBalance")) {
				 
				double outstandingBalance=0.00;					
				
				if(StringUtils.isNotBlank((balances.get("outstandingBalance").toString())))
				{
				 outstandingBalance = ((Number) (balances.get("outstandingBalance"))).doubleValue();
				
				outstandingBalance = outstandingBalance < 0.00
						? Math.abs(outstandingBalance)
						: outstandingBalance;
				}	
				
				acDTO.setOutstandingBalance((Double.toString(outstandingBalance)) != null
						? ((Double.toString(outstandingBalance)))
						: "0.00");
				acDTO.setPrincipalValue((Double.toString(outstandingBalance)) != null
						? ((Double.toString(outstandingBalance)))
						: "0.00");
			} else {
				acDTO.setOutstandingBalance("0.00");
				acDTO.setPrincipalValue("0.00");
			}
			//set disbursedAmount
			if (balances.has("disbursedAmount")) {
				Object disbursedAmount = balances.get("disbursedAmount");
				if (disbursedAmount instanceof Integer) {
					acDTO.setDisbursedAmount(Math.abs(((Number) disbursedAmount).doubleValue()));
				} else {
					acDTO.setDisbursedAmount(Math.abs((double) balances.get("disbursedAmount")));
				}
			} else {
				acDTO.setDisbursedAmount(0.00);
			}
        }

        // Set Last Paid DateR
//        if (arrangement.has("lastPaymentDate")) {
//            acDTO.setLastPaymentDate(arrangement.getString("lastPaymentDate"));
//        }

        // Set values for Account Information
        // Set Company Id
        if (companyId != null) {
            acDTO.setCompanyCode(companyId);
        }else{
            acDTO.setCompanyCode(accountCompanyId);
        }

        // Set Routing Number
        if (companyExtensionData != null){
            if(companyExtensionData.has("sortCode")) {
                acDTO.setRoutingNumber(companyExtensionData.getString("sortCode"));
            }
    
            // Set Swift Code
            if (companyExtensionData.has("accountWithBankBIC")) {
                acDTO.setSwiftCode(companyExtensionData.getString("accountWithBankBIC"));
            }
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

        // Set userName
        acDTO.setUserName(inputPayload.getUserName());
        acDTO.setIsTypeBusiness("0");

        // Set CustomrId
        acDTO.setCustomerID(inputPayload.getCustomerID());
        // Add the account DTO in arrangement DTo List
        arrangementsDTO.add(acDTO);
        }
        catch(Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String st = sw.toString();
			 alert.prepareError("Exception in getDetails"+e).log();
			 alert.prepareError("Exception in getDetails stack trace"+st).log();
        }
        return arrangementsDTO;

    }
    
    @Override
    public List<ArrangementsDTO> getArrangementBulkOverview(String ArrangementResponse,ArrangementsDTO inputPayload, DataControllerRequest request,
            String authToken) throws ApplicationException {

        // Get Request parameters from Request Object
        String accountID = inputPayload.getAccount_id();
        String companyPartyId = inputPayload.getBackendUserId();
        
        //Load Account Properties
        AccountTypeProperties accountTypeProperties = new AccountTypeProperties(request);

        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("accountID", accountID);
        headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);
        // Hit Arrangements Micro Service
        //String ArrangementResponse = null;
        JSONObject logoResponse = null;
        JSONObject connectionResponse = null;
        JSONObject connection = null;
        alert.prepareError("AMS Request " + inputMap).log();
        JSONObject ArrResponse = new JSONObject();
        
        //commenting for perf enhancement
        try {
        	//if(ArrangementResponse==null && accountID!=null) {
                ArrangementResponse = Executor.invokePassThroughServiceAndGetString(
                        ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTOVERVIEW, inputMap, headerMap);
                alert.prepareError("AMS response" + ArrangementResponse).log();
                if (StringUtils.isNotBlank(ArrangementResponse)) {
                    ArrResponse = new JSONObject(ArrangementResponse);
                }
        	//}

        } catch (Exception e) {
            alert.prepareError("Unable to fetch Arrangements " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20049);
        }
        
        // Initialize Variables
        
        HashMap<String,JSONObject> accBalances = getBalanceForAccounts(accountID, authToken);

        List<ArrangementsDTO> arrangementsDTO = new ArrayList<>();
        ArrResponse = new JSONObject(ArrangementResponse);
        
        JSONArray arrangements=null;
        if(ArrResponse.has("arrangements")) {
        	arrangements = ArrResponse.getJSONArray("arrangements");
        }else {
        	arrangements = ArrResponse.getJSONArray("arrangementResponses");
        }
        
        HashMap<String, AccountsDTO> accounts = ArrangementsUtils.getAccountsMapFromCache(request);
        if(accounts== null) {
            accounts = new HashMap<String, AccountsDTO>();
        }
        for(int i=0; i<arrangements.length(); i++) {
    	 JSONObject arrangement = arrangements.getJSONObject(i);
    	 if(arrangement.has("linkedReference")) {
    	 String AccIdWithCompanyId = (String) arrangement.get("linkedReference");
         if(StringUtils.isNotBlank(AccIdWithCompanyId)&&accountID.contains(AccIdWithCompanyId)) {
            JSONObject companyObj = null;
            ArrangementsDTO acDTO = new ArrangementsDTO();
            JSONObject companyExtensionData = null;
            String companyId = null;
            String extSystemInfo = null;
            JSONObject balances = null;
            logoResponse = null;
            connectionResponse = null;
            connection = null;
            if (arrangement.has("additionalErrorInfo")
                    && StringUtils.isNotBlank(arrangement.getString("additionalErrorInfo"))) {
                if (arrangement.has("arrangementRef")) {
                    String account = arrangement.getString("arrangementRef");
                    alert.prepareError("Skipping the missing arrangement" + account).log();
                }
                continue;
            }
            
            String externalIndicator = null;
            if (arrangement.has("externalIndicator")) {
                acDTO.setExternalIndicator((String) arrangement.get("externalIndicator"));
                externalIndicator = (String) arrangement.get("externalIndicator");
            }
            
            if (arrangement.has("estmtEnabled")) {
                if (StringUtils.isNotBlank(arrangement.get("estmtEnabled").toString())) {
                    if (arrangement.get("estmtEnabled").toString().contentEquals("true")) {

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
            
            if (AccIdWithCompanyId != null) {
                companyObj = arrangement.getJSONObject("company");
                companyExtensionData = companyObj.getJSONObject("extensionData");
                if (companyObj.has("companyReference")) {
                    companyId = companyObj.getString("companyReference");
                }
                if (arrangement.has("extSystemInfo")) {
                    if (StringUtils.isNotBlank(externalIndicator)) {
                        if (externalIndicator.equals("true") || externalIndicator.equalsIgnoreCase("Yes")) {
                            extSystemInfo = (String) arrangement.get("extSystemInfo");
                            logoResponse = getLogoURLForAccount(extSystemInfo, authToken);
                            acDTO.setBankCode((String) extSystemInfo);
                            connectionResponse = getConnectionDetails(companyPartyId, extSystemInfo,authToken);
                            if (connectionResponse != null && connectionResponse.getJSONArray("connectionss").length() > 0
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
                        }
                    }
                }
                if ( accBalances.containsKey(AccIdWithCompanyId)){
                	balances=accBalances.get(AccIdWithCompanyId);
                }
            }
    
            // Set Values for Account DashBoard
            // Set Account ID
            String accountCompanyId = StringUtils.EMPTY;
            String accountId = StringUtils.EMPTY;
            String ArrangementId = StringUtils.EMPTY;
            String ArrangementIdWithPrefix = StringUtils.EMPTY;
            if (AccIdWithCompanyId.contains("-")) {
                accountId = AccIdWithCompanyId.split("-")[1];
                accountCompanyId = AccIdWithCompanyId.split("-")[0];
            }
            if (arrangement.has("extArrangementId")) {
                ArrangementIdWithPrefix = (String) arrangement.getString("extArrangementId");
                if (ArrangementIdWithPrefix.contains("-"))
                    ArrangementId = ArrangementIdWithPrefix.split("-")[1];
            }
            
            if (arrangement.has("accountCategory")
                    && StringUtils.isNotBlank(arrangement.getString("accountCategory"))) {
                acDTO.setAccountCategory(arrangement.getString("accountCategory"));
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
    
            
            if (arrangement.has("portfolioId")) {
                acDTO.setIsPortFolioAccount(Boolean.TRUE.toString());
            } else {
                acDTO.setIsPortFolioAccount(Boolean.FALSE.toString());
            }
    
            // Set Account Type based on products
            if (arrangement.has("productLine")) {
                acDTO.setProductLineName(arrangement.getString("productLine"));
                if (arrangement.getString("productLine").equalsIgnoreCase("LENDING")) {
                    if (arrangement.has("lendingArrangement")) {
                        JSONObject lendingArrangement = arrangement.getJSONObject("lendingArrangement");
                        if (lendingArrangement.has("lastPaymentAmount")) {
                            acDTO.setLastPaymentAmount(lendingArrangement.get("lastPaymentAmount").toString() != null
                                    ? lendingArrangement.get("lastPaymentAmount").toString()
                                    : "");
                        } else {
                            acDTO.setLastPaymentAmount("");
                        }
                        if (lendingArrangement.has("totalDueAmount")) {
                            acDTO.setTotalDueAmount(lendingArrangement.get("totalDueAmount").toString() != null
                                    ? lendingArrangement.get("totalDueAmount").toString()
                                    : "");
                        } else {
                            acDTO.setTotalDueAmount("");
                        }
                        if (lendingArrangement.has("termAmount")) {
                            acDTO.setTermAmount((lendingArrangement.get("termAmount").toString()) != null
                                    ? (lendingArrangement.get("termAmount").toString())
                                    : "");
                        } else {
                            acDTO.setTermAmount("");
                        }
                        if (lendingArrangement.has("lastPaymentDate")) {
                            acDTO.setLastPaymentDate((lendingArrangement.getString("lastPaymentDate")) != null
                                    ? (lendingArrangement.getString("lastPaymentDate"))
                                    : "");
                        } else {
                            acDTO.setLastPaymentDate("");
                        }
                        if (lendingArrangement.has("sanctionedDate")) {
                            acDTO.setSanctionedDate((lendingArrangement.getString("sanctionedDate")) != null
                                    ? (lendingArrangement.getString("sanctionedDate"))
                                    : "");
                        } else {
                            acDTO.setSanctionedDate("");
                        }
                        if (lendingArrangement.has("maturityDate")) {
                            acDTO.setMaturityDate((lendingArrangement.getString("maturityDate")) != null
                                    ? (lendingArrangement.getString("maturityDate"))
                                    : "");
                        } else {
                            acDTO.setMaturityDate("");
                        }
                        if (lendingArrangement.has("totalDueAmount")) {
                            acDTO.setTotalDueAmount(lendingArrangement.get("totalDueAmount").toString() != null
                                    ? lendingArrangement.get("totalDueAmount").toString()
                                    : "");
                        } else {
                            acDTO.setTotalDueAmount("");
                        }
                        if (lendingArrangement.has("currentDue")) {
                            acDTO.setNextPaymentAmount(lendingArrangement.get("currentDue").toString() != null
                                    ? lendingArrangement.get("currentDue").toString()
                                    : "");
                        } else {
                            acDTO.setTotalDueAmount("");
                        }
                        if (lendingArrangement.has("dueDate")) {
                            acDTO.setNextPaymentDate(lendingArrangement.get("dueDate").toString() != null
                                    ? lendingArrangement.get("dueDate").toString()
                                    : "");
                        } else {
                            acDTO.setTotalDueAmount("");
                        }
                    } else {
                        acDTO.setLastPaymentAmount("");
                        acDTO.setTotalDueAmount("");
                        acDTO.setTermAmount("");
                        acDTO.setLastPaymentDate("");
                        acDTO.setSanctionedDate("");
                        acDTO.setMaturityDate("");
                        acDTO.setNextPaymentAmount("");
                        acDTO.setNextPaymentDate("");
    
                    }
                }
            }
            String product = StringUtils.EMPTY;
            if (arrangement.has("product")) {
                product = arrangement.getString("product");
                acDTO.setProduct(product);
                String ProductDesc = StringUtils.isNotBlank(AccountTypeProperties.getValue(product))
                        ? AccountTypeProperties.getValue(product) : "Others";
                acDTO.setTypeDescription(ProductDesc);
                if (acDTO.getTypeDescription() != null && acDTO.getTypeDescription().equalsIgnoreCase("Loan")) {
                    try {
                        String getScheduleCounts = Executor.invokePassThroughServiceAndGetString(
                                ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETLOANINSTALLMENTOVERVIEW, inputMap,
                                headerMap);
                        JSONObject schedules = new JSONObject(getScheduleCounts);
                        String futureCount = "0";
                        String paidCount = "0";
                        String overDueCount = "0";
                        if (schedules.has("scheduleStatus")) {
                            JSONObject scheduleCounts = schedules.getJSONObject("scheduleStatus");
                            if (scheduleCounts.has("futureScheduleCount")
                                    && scheduleCounts.get("futureScheduleCount") != null
                                    && StringUtils.isNotBlank(scheduleCounts.get("futureScheduleCount").toString())) {
                                futureCount = scheduleCounts.get("futureScheduleCount").toString();
                            }
                            if (scheduleCounts.has("paidScheduleCount") && scheduleCounts.get("paidScheduleCount") != null
                                    && StringUtils.isNotBlank(scheduleCounts.get("paidScheduleCount").toString())) {
                                paidCount = scheduleCounts.get("paidScheduleCount").toString();
                            }
                            if (scheduleCounts.has("dueScheduleCount") && scheduleCounts.get("dueScheduleCount") != null
                                    && StringUtils.isNotBlank(scheduleCounts.get("dueScheduleCount").toString())) {
                                overDueCount = scheduleCounts.get("dueScheduleCount").toString();
                            }
                        }
                        acDTO.setFutureInstallmentsCount(futureCount);
                        acDTO.setPaidInstallmentsCount(paidCount);
                        acDTO.setOverDueInstallmentsCount(overDueCount);
    
                    } catch (Exception e) {
                        alert.prepareError("Error while fetching schedules").log();
                    }
    //                if (arrangement.has("outstandingBalance")) {
    //                    acDTO.setOutstandingBalance((String) arrangement.get("outstandingBalance"));
    //                } else {
    //                    acDTO.setOutstandingBalance("0");
    //                }
    
                }
            }
            if (acDTO.getTypeDescription() == null) {
                String ProductDesc = StringUtils.isNotBlank(AccountTypeProperties.getValue(product))
                        ? AccountTypeProperties.getValue(product) : "Others";
                acDTO.setTypeDescription(ProductDesc);
            } 
    
            // Set Account NickName
            if (arrangement.has("shortTitle")) {
                acDTO.setNickName(arrangement.getString("shortTitle"));
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
                    }else if (dividendPaidYTD instanceof BigDecimal) {
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
              //set outstandingBalance
                if (balances.has("outstandingBalance")) {
                     
                    double outstandingBalance=0.00;                 
                    
                    if(StringUtils.isNotBlank((balances.get("outstandingBalance").toString())))
                    {
                     outstandingBalance = ((Number) (balances.get("outstandingBalance"))).doubleValue();
                    
                    outstandingBalance = outstandingBalance < 0.00
                            ? Math.abs(outstandingBalance)
                            : outstandingBalance;
                    }   
                    
                    acDTO.setOutstandingBalance((Double.toString(outstandingBalance)) != null
                            ? ((Double.toString(outstandingBalance)))
                            : "0.00");
                } else {
                    acDTO.setOutstandingBalance("0.00");
                }
                //set disbursedAmount
                if (balances.has("disbursedAmount")) {
                    Object disbursedAmount = balances.get("disbursedAmount");
                    if (disbursedAmount instanceof Integer) {
                        acDTO.setDisbursedAmount(Math.abs(((Number) disbursedAmount).doubleValue()));
                    } else {
                        acDTO.setDisbursedAmount(Math.abs((double) balances.get("disbursedAmount")));
                    }
                } else {
                    acDTO.setDisbursedAmount(0.00);
                }
            }
    
            // Set Last Paid Date
    //        if (arrangement.has("lastPaymentDate")) {
    //            acDTO.setLastPaymentDate(arrangement.getString("lastPaymentDate"));
    //        }
    
            // Set values for Account Information
            // Set Company Id
            if (companyId != null) {
                acDTO.setCompanyCode(companyId);
            }else{
                acDTO.setCompanyCode(accountCompanyId);
            }
            
            if (accountId != null) {
                acDTO.setAccount_id(accountId);
            }else{
                acDTO.setAccount_id(accountId);
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
    
            // Set userName
            acDTO.setUserName(inputPayload.getUserName());
            acDTO.setIsTypeBusiness("0");
    
            // Set CustomrId
            acDTO.setCustomerID(inputPayload.getCustomerID());
            // Add the account DTO in arrangement DTo List
            //arrangementsDTO.add(acDTO); 
            
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

            acDTO.setProductGroup(acDTO.getProduct());
            // Add the account DTO in arrangement DTo List
            arrangementsDTO.add(acDTO);
           }
    	 }
    	}
        
        Gson gson = new Gson();
        String gsonAccounts = gson.toJson(accounts);
        ArrangementsUtils.insertIntoSession("Accounts", gsonAccounts, request);
        return arrangementsDTO;

    }
    
 private HashMap<String,JSONObject> getBalanceForAccounts(String accId, String authToken)
            throws ApplicationException {

    	String AccountId = accId;
        HashMap<String, Object> balInputMap = new HashMap<>();
        Map<String, Object> balHeaderMap = new HashMap<>();
        balInputMap.put("accountId", AccountId);
        balInputMap.put("accountID", AccountId);
        balHeaderMap = ArrangementsUtils.generateSecurityHeadersForHMS(authToken, balHeaderMap);
        String BalanceResponse = null;
        JSONObject Balance = new JSONObject();
        try {
            BalanceResponse = Executor.invokeService(
                    ArrangementsAPIServices.HOLDINGSMICROSERVICESJSON_GETACCOUNTBALANCES, balInputMap, balHeaderMap);

        } catch (Exception e) {
            alert.prepareError("Unable to fetch balances " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20042);
        }
        if (StringUtils.isNotBlank(BalanceResponse)) {
            Balance = new JSONObject(BalanceResponse);
        }
        HashMap<String,JSONObject> balMap = new HashMap<>();
        if (Balance.has("items")) {
        	JSONArray balArr = (JSONArray) Balance.get("items");
        	for (int j = 0;j<balArr.length();j++) {
        		JSONObject balObj = (JSONObject) balArr.get(j);
        		if (balObj.has("accountId")) {
        			balMap.put((String) balObj.get("accountId"), balObj);
        		}
        	}
        }
        
        return balMap;
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
        try {
            BalanceResponse = Executor.invokeService(
                    ArrangementsAPIServices.HOLDINGSMICROSERVICESJSON_GETACCOUNTBALANCES, balInputMap, balHeaderMap);

        } catch (Exception e) {
            alert.prepareError("Unable to fetch balances " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20042);
        }
        if (StringUtils.isNotBlank(BalanceResponse)) {
            Balance = new JSONObject(BalanceResponse);
        }
        return Balance;
    }

    // TODO:Modify Method Signature - Change response to DTO
    private JSONObject getLogoURLForAccount(String extSystemInfo,String authToken) throws ApplicationException {
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("extSystemInfo", extSystemInfo);
        JSONObject logo = new JSONObject();
        String logoResponse = null;
        Map<String, Object> headerMap = new HashMap<>();
        headerMap=generateSecurityHeadersForArrangements(authToken,headerMap);
        try {
            logoResponse = Executor.invokeService(ArrangementsAPIServices.ACCOUNTAGGREGATIONJSON_GETLOGOURL, inputMap,
                    headerMap);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch logo URL " + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20043);
        }
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
    private JSONObject getConnectionDetails(String partyId, String bankCode,String authToken) throws ApplicationException {
        HashMap<String, Object> inputMap = new HashMap<>();
        inputMap.put("partyId", partyId);
        inputMap.put("bankCode", bankCode);
        JSONObject connectionDetails = new JSONObject();
        String connectionResponse = null;
        Map<String, Object> headerMap = new HashMap<>();
        headerMap=generateSecurityHeadersForArrangements(authToken,headerMap);
        try {
            connectionResponse = Executor
                    .invokeService(ArrangementsAPIServices.ACCOUNTAGGREGATIONJSON_GETCONNECTIONDETAILS, inputMap,headerMap);
        } catch (Exception e) {
            alert.prepareError("Unable to fetch Connection Details" + e).log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20051);
        }
        if (StringUtils.isNotBlank(connectionResponse)) {
            // Validate service response before converting to JSON Object
            connectionDetails = new JSONObject(connectionResponse);
        }
        return connectionDetails;
    }

	
	@Override
	public JSONObject getAccountDetailsForCombinedStatement(String accountID, String authToken)
			throws ApplicationException {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		JSONObject accoutDetails = new JSONObject();
		dataMap.put("accountID", accountID);
		String arrangementResponse = null;
		JSONObject arrResponse = null;
		JSONObject arrangement = null;
		Map<String, Object> headerMap = new HashMap<>();
		headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);
		try {
			arrangementResponse = Executor.invokePassThroughServiceAndGetString(
					ArrangementsAPIServices.ARRANGEMENTSMICROSERVICESJSON_GETARRANGEMENTOVERVIEW, dataMap, headerMap);
			if (StringUtils.isNotBlank(arrangementResponse)) {
				arrResponse = new JSONObject(arrangementResponse);
			}

			arrangement = arrResponse.getJSONArray("arrangementResponses").getJSONObject(0);

		} catch (Exception e) {
			alert.prepareError("Unable to fetch Arrangements " + e).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_20049);
		}
		if (arrangement.has("shortTitle")) {
			accoutDetails.put("nickName", arrangement.getString("shortTitle"));
		}
		if (arrangement.has("productDescription")) {
			accoutDetails.put("accountName", arrangement.getString("productDescription"));
		}
		if (arrangement.has("currency")) {
			accoutDetails.put("currencyCode", arrangement.getString("currency"));
		}
		if (arrangement.has("productGroup")) {
			accoutDetails.put("accountType", arrangement.getString("productGroup"));
		}
		return accoutDetails;
	}

	@Override
	public JSONObject getAccountDetailsForCombinedStatementfromT24(String accountID, String authToken) throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("accountID", accountID);
		String arrangementT24Response = null;
		JSONObject t24ArrResponse = new JSONObject();
		Map<String, Object> headerMap = new HashMap<>();
		headerMap = ArrangementsUtils.generateSecurityHeaders(authToken, headerMap);
		arrangementT24Response = Executor.invokeService(
				ArrangementsAPIServices.T24ISACCOUNTS_GETACCOUNTDETAILSFORCOMBINEDSTMTS, dataMap, headerMap);
		if (StringUtils.isNotBlank(arrangementT24Response)) {
			t24ArrResponse = new JSONObject(arrangementT24Response);
		}
		return t24ArrResponse;
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
	 
	public Result getSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		Map<String, Object> headersMap = HelperMethods.addJWTAuthHeader(request, request.getHeaderMap(),
				AuthConstants.POST_LOGIN_FLOW);
		String arrangementId = inputParams.get("arrangementId").toString();
		String currencyId = inputParams.get("currencyId").toString();
		String transactionAmount = inputParams.get("transactionAmount").toString();
		String activityId = inputParams.get("activityId").toString() != null ? inputParams.get("activityId").toString()
				: "";
		JSONArray overrideDetails = new JSONArray();
		inputMap.put("arrangementId", arrangementId);
		inputMap.put("currencyId", currencyId);
		inputMap.put("transactionAmount", transactionAmount);
		inputMap.put("activityId", activityId);
		inputMap.put("overrideDetails", overrideDetails);
		JSONObject serviceResponse1 = new JSONObject();
		JSONObject finalServiceResponse = new JSONObject();
		String serviceResponse2 = null;

		JSONObject errHandling = new JSONObject();
		String serviceResponse = null;
		Result result = new Result();

		String PAYMENT_BACKEND = EnvironmentConfigurationsHandler.getValue("PAYMENT_BACKEND");

		try {
			if (StringUtils.isNotBlank(arrangementId) && StringUtils.isNotBlank(currencyId)
					&& StringUtils.isNotBlank(transactionAmount)) {
				if (PAYMENT_BACKEND.equalsIgnoreCase("T24")) {
					serviceResponse = Executor.invokeService(
							ArrangementsAPIServices.T24MORTGAGESERVICES_GETSIMULATEDRESULTS, inputMap, headersMap);
					
					finalServiceResponse = new JSONObject(serviceResponse.toString());
					JSONObject override = finalServiceResponse.getJSONObject("override");
				    int status = finalServiceResponse.getInt("httpStatusCode");
				    if(status == 400) {
				    	JSONArray overrideDetail = override.getJSONArray("overrideDetails");
						inputMap.replace("overrideDetails", overrideDetails, overrideDetail);
						serviceResponse2 = Executor.invokeService(
								ArrangementsAPIServices.T24MORTGAGESERVICES_GETSIMULATEDRESULTS, inputMap, headersMap);
						finalServiceResponse = new JSONObject(serviceResponse2.toString());
				    }
				} else {
					serviceResponse2 = Executor.invokePassThroughServiceAndGetString(
							(InfinityServices) ArrangementsAPIServices.MORTGAGE_GETMORTGAGESIMULATEDRESULTS, inputMap,
							headersMap);
					finalServiceResponse = new JSONObject(serviceResponse2.toString());
				}

				//serviceResponse1 = new JSONObject(finalServiceResponse.toString());
				if (finalServiceResponse.getInt("opstatus") != 0 || finalServiceResponse.has("errmsg")) {
					result = ErrorCodeEnum.ERR_200518.setErrorCode(new Result());
				} else {
					result = JSONToResult.convert(finalServiceResponse.toString());
				}

			} else {
				result = ErrorCodeEnum.ERR_11024.setErrorCode(new Result());
			}

		} catch (Exception e) {
			alert.prepareError("Error while invoking Javaservice").log();
			// errHandling.put("errMsg","Error while invoking Javaservice");
			result = ErrorCodeEnum.ERR_200519.setErrorCode(new Result());
		}
		return result;
	}

	@Override
	public List<ArrangementsDTO> getLatestBalances(String accountIdListStr, String authToken)
			throws ApplicationException {

		HashMap<String, JSONObject> accBalances = getBalanceForAccounts(accountIdListStr, authToken);
		JSONObject balances;
		ArrayList arrangementsWithBalances = new ArrayList<ArrangementsDTO>();
		String accountsList[] = accountIdListStr.split(" ");
		for (String accountID : accountsList) {
			ArrangementsDTO acDTO = new ArrangementsDTO();
			balances = accBalances.get(accountID);
			String accountIDWihtoutCompanyId = accountID;
			if (accountID.contains("-")) { // TODO: Handle External accounts scenario.
				accountIDWihtoutCompanyId = accountID.split("-")[1];
			}
			acDTO.setAccount_id(accountIDWihtoutCompanyId);
			populateAccountBalances(acDTO, balances);
			arrangementsWithBalances.add(acDTO);
		}
		return arrangementsWithBalances;
	}

	
	private void populateAccountBalances(ArrangementsDTO acDTO,JSONObject balances) throws ApplicationException{
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
            } else {
            	acDTO.setAvailableBalance(0.0);
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
            } else {
            	acDTO.setCurrentBalance(0.0);
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
                }else if (dividendPaidYTD instanceof BigDecimal) {
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
          //set outstandingBalance
            if (balances.has("outstandingBalance")) {
                 
                double outstandingBalance=0.00;                 
                
                if(StringUtils.isNotBlank((balances.get("outstandingBalance").toString())))
                {
                 outstandingBalance = ((Number) (balances.get("outstandingBalance"))).doubleValue();
                
                outstandingBalance = outstandingBalance < 0.00
                        ? Math.abs(outstandingBalance)
                        : outstandingBalance;
                }   
                
                acDTO.setOutstandingBalance((Double.toString(outstandingBalance)) != null
                        ? ((Double.toString(outstandingBalance)))
                        : "0.00");
            } else {
                acDTO.setOutstandingBalance("0.00");
            }
            acDTO.setCurrencyCode(balances.get("currencyId").toString());
            //set disbursedAmount
            if (balances.has("disbursedAmount")) {
                Object disbursedAmount = balances.get("disbursedAmount");
                if (disbursedAmount instanceof Integer) {
                    acDTO.setDisbursedAmount(Math.abs(((Number) disbursedAmount).doubleValue()));
                } else {
                    acDTO.setDisbursedAmount(Math.abs((double) balances.get("disbursedAmount")));
                }
            } else {
                acDTO.setDisbursedAmount(0.00);
            }
        }
	}
	
	@Override
	public Result createAndGetPayOffSimulatedResults(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		Map<String, Object> inputMap = new HashMap<>();
		Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
		HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
		Map<String, Object> headersMap = HelperMethods.addJWTAuthHeader(request, request.getHeaderMap(),
				AuthConstants.POST_LOGIN_FLOW);
		String arrangementId = inputParams.get("arrangementId").toString();
		String effectiveDate = inputParams.get("effectiveDate").toString();
		JSONArray overrideDetails = new JSONArray();
		inputMap.put("arrangementId", arrangementId);
		inputMap.put("effectiveDate", effectiveDate);

		JSONObject serviceResponse1 = new JSONObject();
		JSONObject finalServiceResponse = new JSONObject();
		String serviceResponse2 = null;

		JSONObject errHandling = new JSONObject();
		String serviceResponse = null;
		Result result = new Result();

		//TODO: Validate arrangement id is available for the user or not.

		try {
					serviceResponse = Executor.invokeService(
							ArrangementsAPIServices.T24MORTGAGESERVICES_CREATEPAYOFFSIMULATION, inputMap, headersMap);
					
					finalServiceResponse = new JSONObject(serviceResponse.toString());
					alert.prepareError("Create Payoff simulation response"+serviceResponse.toString()).log();
				    int status = finalServiceResponse.getInt("httpStatusCode");
				    int opstatus = finalServiceResponse.getInt("opstatus");
				    if(status == 400) {
				    	if(finalServiceResponse.has("overrideDetails")) {
				    		JSONArray overrideDetail = finalServiceResponse.getJSONArray("overrideDetails");
				    		String errorMessage="";
				    		if(overrideDetail!=null && overrideDetail.length()>0) {
				    			
				    			for(int i=0;i<overrideDetail.length();i++) {
				    				JSONObject override = 	overrideDetail.getJSONObject(i);
				    				if(override!=null & override.has("description")) {
				    					errorMessage=errorMessage+override.getString("description")+". ";
				    				}
				    			}
				    			
				    		}
				    		finalServiceResponse.append("dbpErrMsg", errorMessage);
				    		result = JSONToResult.convert(finalServiceResponse.toString());
				    		result.addErrMsgParam(errorMessage.trim());
				    		result.addOpstatusParam(5001);
				    		result.addParam("dbpErrCode", "5001");
				    		
				    	}else {
				    		result = ErrorCodeEnum.ERR_200520.setErrorCode(new Result());
				    	}

				    }else {
				    	String transactionStatus = finalServiceResponse.getString("transactionStatus");
				    	if("Live".equalsIgnoreCase(transactionStatus)) {
				    		String getServiceResponse = Executor.invokeService(
									ArrangementsAPIServices.T24MORTGAGESERVICES_GETPAYOFFSIMULATION, inputMap, headersMap);
				    		alert.prepareError("Get Payoff simulation response"+getServiceResponse.toString()).log();
				    		JSONObject getServiceResponseObj = new JSONObject(getServiceResponse.toString());
				    		int getOpstatus = getServiceResponseObj.getInt("opstatus");
				    		if(getOpstatus!=0 || getServiceResponseObj.has("errmsg")) {
				    			result = ErrorCodeEnum.ERR_200521.setErrorCode(new Result());
				    		}else {
				    			//TODO: check if date matching, if not do a looping call until date match or 50 seconds.
				    			result = JSONToResult.convert(getServiceResponse.toString());
				    		}
				    		
				    		return result;
				    	}else {
				    		//Created service failed. Do error handling
				    	}
				    }


		} catch (Exception e) {
			alert.prepareError("Error while invoking Javaservice").log();
			// errHandling.put("errMsg","Error while invoking Javaservice");
			result = ErrorCodeEnum.ERR_200519.setErrorCode(new Result());
		}
		return result;
	}
	
	
}
