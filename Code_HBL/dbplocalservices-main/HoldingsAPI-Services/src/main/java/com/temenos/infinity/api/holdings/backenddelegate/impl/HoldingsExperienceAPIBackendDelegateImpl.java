package com.temenos.infinity.api.holdings.backenddelegate.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.temenos.infinity.api.commons.config.InfinityServices;
import com.temenos.infinity.api.commons.constants.FabricConstants;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.commons.invocation.Executor;
import com.temenos.infinity.api.commons.utils.Utilities;
import com.temenos.infinity.api.holdings.backenddelegate.api.HoldingsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.holdings.businessdelegate.api.AccountTransactionsBusinessDelegate;
import com.temenos.infinity.api.holdings.config.HoldingsAPIServices;
import com.temenos.infinity.api.holdings.config.ServerConfigurations;
import com.temenos.infinity.api.holdings.constants.ErrorCodeEnum;
import com.temenos.infinity.api.holdings.constants.MSCertificateConstants;
import com.temenos.infinity.api.holdings.constants.TemenosConstants;
import com.temenos.infinity.api.holdings.dto.AccountTransactionsDTO;
import com.temenos.infinity.api.holdings.util.TransactionTypeProperties;

/**
 * 
 * @author KH2281
 * @version 1.0 Extends the {@link AccountTransactionsBusinessDelegate}
 */
public class HoldingsExperienceAPIBackendDelegateImpl implements HoldingsExperienceAPIBackendDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /**
     * method to get the Account Transactions
     * 
     * @return List<AccountTransactionsDTO> of Account Transactions
     * @throws ApplicationException
     */
    @Override
    public List<AccountTransactionsDTO> getDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO,
            String authToken) throws ApplicationException {
        JSONArray records = new JSONArray();
        List<AccountTransactionsDTO> accountTransactions = new ArrayList<>();
        String Value = null;
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("accountId", inputDTO.getAccountId());
        inputMap.put("limit", inputDTO.getLimit());
        headerMap = generateSecurityHeaders(authToken, headerMap);
        diagnostic.prepareDebug("Holdings Transactions Request : " + inputMap.toString()).log();
        try {
            Value = Executor.invokeService(HoldingsAPIServices.HOLDINGSMICROSERVICESJSON_GETACCOUNTTRANSACTIONS,
                    inputMap, headerMap);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
        diagnostic.prepareDebug("Holdings Transactions Response : " + Value).log();
        records = performAdditionalFilteringOnReponse(Value, inputDTO, records);
        if (records != null) {
            accountTransactions = convertResponseToDTO(accountTransactions, records);
            return accountTransactions;
        } else {
            return null;
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
        if (StringUtils.isNotEmpty(ServerConfigurations.HOLDINGS_DEPLOYMENT_PLATFORM.getValueIfExists())) {
            if (StringUtils.equalsIgnoreCase(ServerConfigurations.HOLDINGS_DEPLOYMENT_PLATFORM.getValueIfExists(),
                    MSCertificateConstants.AWS))
                headerMap.put("x-api-key", ServerConfigurations.HOLDINGS_AUTHORIZATION_KEY.getValueIfExists());
            else if (StringUtils.equalsIgnoreCase(ServerConfigurations.HOLDINGS_DEPLOYMENT_PLATFORM.getValueIfExists(),
                    MSCertificateConstants.AZURE))
                headerMap.put("x-functions-key", ServerConfigurations.HOLDINGS_AUTHORIZATION_KEY.getValueIfExists());
        }
        headerMap.put("roleId", ServerConfigurations.HOLDINGS_ROLE_ID.getValueIfExists());
        return headerMap;
    }

    @Override
    public List<AccountTransactionsDTO> getSearchDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO,String authToken )
            throws ApplicationException {
        JSONArray records = new JSONArray();
        List<AccountTransactionsDTO> accountTransactions = new ArrayList<>();
        String Value = null;
        Map<String, Object> headerMap = new HashMap<>();
        headerMap = generateSecurityHeaders(authToken, headerMap);
        Map<String, Object> inputMap = new HashMap<>();
		if (StringUtils.isNotBlank(inputDTO.getAccountId())) {
			inputMap.put("accountId", inputDTO.getAccountId());
		}
		if (StringUtils.isNotBlank(inputDTO.getTransactionType())) {
			inputMap.put("transactionType", inputDTO.getTransactionType());
		}
		if (StringUtils.isNotBlank(inputDTO.getSearchMinAmount())) {
			inputMap.put("searchMinAmount", inputDTO.getSearchMinAmount());
		}
		if (StringUtils.isNotBlank(inputDTO.getSearchMaxAmount())) {
			inputMap.put("searchMaxAmount", inputDTO.getSearchMaxAmount());
		}
		if (StringUtils.isNotBlank(inputDTO.getSearchStartDate())) {
			inputMap.put("searchStartDate", inputDTO.getSearchStartDate());
		}
		if (StringUtils.isNotBlank(inputDTO.getSearchEndDate())) {
			inputMap.put("searchEndDate", inputDTO.getSearchEndDate());
		}
		if (StringUtils.isNotBlank(inputDTO.getOffset())) {
			inputMap.put("offset", inputDTO.getOffset());
		}
		if (StringUtils.isNotBlank(inputDTO.getLimit())) {
			inputMap.put("limit", inputDTO.getLimit());
		}
		if (StringUtils.isNotBlank(inputDTO.getTransactionId())) {
			inputMap.put("transactionId", inputDTO.getTransactionId());
		}
		if (StringUtils.isNotBlank(inputDTO.getSearchDescription())) {
			inputMap.put("searchDescription", inputDTO.getSearchDescription());
		}
        diagnostic.prepareDebug("Holdings Search Transactions Request : " + inputMap.toString()).log();;
        
        if (StringUtils.isNotBlank(inputDTO.getSearchType()) && inputDTO.getSearchType().equals("download")) {
        	try {
	            Value = Executor.invokeService(HoldingsAPIServices.HOLDINGSMICROSERVICESJSON_SEARCHTRANSACTIONSTIMED, inputMap,
	            		headerMap);
	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
	        }
	        diagnostic.prepareDebug("Holdings Search Transactions Response : " + Value).log();
	        
	        JSONObject resp = new JSONObject(Value);
			
			if (resp.has("errmsg") && resp.getString("errmsg").contains("timed out")) {
				String eventTriggered = "";
				try {
					eventTriggered = Executor.invokeService(HoldingsAPIServices.DOC_GENERATEADHOC, inputMap,
							headerMap);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					alert.prepareError(e.getMessage()).log();
				}
    			JSONObject event = new JSONObject(eventTriggered);
    			if (event.has("opstatus") && event.getInt("opstatus")==0) {
    				event.append("fileId", "Event triggered");
    			}
    			else return null;	
			}				
			
	        records = performAdditionalFilteringOnReponse(Value, inputDTO, records);
	        if (records != null) {
	            accountTransactions = convertResponseToDTO(accountTransactions, records);
	            return accountTransactions;
	        } else {
	            return null;
	        }
	        
	        
        }
        else {	
	        try {
	            Value = Executor.invokeService(HoldingsAPIServices.HOLDINGSMICROSERVICESJSON_SEARCHTRANSACTIONS, inputMap,
	            		headerMap);
	        } catch (Exception e) {
	            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
	        }
	        diagnostic.prepareDebug("Holdings Search Transactions Response : " + Value).log();
	        records = performAdditionalFilteringOnReponse(Value, inputDTO, records);
	        if (records != null) {
	            accountTransactions = convertResponseToDTO(accountTransactions, records);
	            return accountTransactions;
	        } else {
	            return null;
	        }
        }
    }

    /**
     * method to convert the response to DTO as we need to introduce some
     * additional fields in the response
     * 
     * @return List<AccountTransactionsDTO> of Account Transactions
     */
    public List<AccountTransactionsDTO> convertResponseToDTO(List<AccountTransactionsDTO> accountTransactions,
            JSONArray records) {

        for (int index = 0; index < records.length(); index++) {
            AccountTransactionsDTO accountTransactionsDTO = new AccountTransactionsDTO();
            JSONObject jsonObject = (JSONObject) records.get(index);
            String accountId = jsonObject.has("accountId") ? (String) jsonObject.get("accountId") : null;
            if (accountId != null) {
                accountTransactionsDTO.setAccountId(accountId);
                accountTransactionsDTO.setFromAccountNumber(accountId);
            }
            String checkNumber = jsonObject.has("checkNumber") ? (String) jsonObject.get("checkNumber") : null;
            if (checkNumber != null) {
                accountTransactionsDTO.setCheckNumber(checkNumber);
      
            }
            
            String bookingDate = jsonObject.has("bookingDate") ? (String) jsonObject.get("bookingDate") : null;
            if (bookingDate != null) {
                accountTransactionsDTO.setScheduledDate(bookingDate);
                accountTransactionsDTO.setBookingDate(bookingDate);
            }
            String valueDate = jsonObject.has("valueDate") ? (String) jsonObject.get("valueDate") : null;
            if (valueDate != null) {
                accountTransactionsDTO.setValueDate(valueDate);
                accountTransactionsDTO.setPostedDate(valueDate);
                accountTransactionsDTO.setTransactionDate(valueDate);
            }
            String transaction_Id = jsonObject.has("transaction_Id") ? (String) jsonObject.get("transaction_Id") : null;
            if (transaction_Id != null) {
                accountTransactionsDTO.setTransaction_Id(transaction_Id);
            }
            if (jsonObject.has("customerReference")) {
                String customerReference = (String) jsonObject.get("customerReference");
                if (customerReference != null) {
                    accountTransactionsDTO.setCustomerReference(customerReference);
                    accountTransactionsDTO.setNotes(customerReference);
                }
            }
            String narrative = (String) jsonObject.get("narrative");
            if (narrative != null && narrative.length() != 0) {
                accountTransactionsDTO.setNarrative(narrative);
                accountTransactionsDTO.setDescription(narrative);
            } else {
                accountTransactionsDTO.setNarrative("NA");
            }
			/*
			 * String processingDate = jsonObject.has("processingDate") ? (String)
			 * jsonObject.get("processingDate") : null; if (processingDate != null) {
			 * accountTransactionsDTO.setProcessingDate(processingDate);
			 * accountTransactionsDTO.setTransactionDate(processingDate); }
			 */
            if (jsonObject.has("externalReference") && jsonObject.get("externalReference")!=null && StringUtils.isNotBlank(jsonObject.get("externalReference").toString())) {
                String transactionReference = (String) jsonObject.get("externalReference");
                if (transactionReference != null) {
                    accountTransactionsDTO.setTransactionReference(transactionReference);
                    accountTransactionsDTO.setId(transactionReference);
                }
            }
            else if (jsonObject.has("transactionReference")) {
                String transactionReference = (String) jsonObject.get("transactionReference");
                if (transactionReference != null) {
                    accountTransactionsDTO.setTransactionReference(transactionReference);
                    accountTransactionsDTO.setId(transactionReference);
                }
            }
            if (jsonObject.has("transactionId")) {
                String transactionReference = (String) jsonObject.get("transactionId");
                if (transactionReference != null) {
                    accountTransactionsDTO.setTransactionId(transactionReference);
                }
            }
            if (jsonObject.has("transactionType")) {
                String transactionType = (String) jsonObject.get("transactionType");
                if (transactionType != null) {
                    accountTransactionsDTO.setTransactionType(transactionType);
                }
            }
            if (jsonObject.has("categorisactionId")) {
                Object categoryIdObj = jsonObject.get("categorisactionId");
                if (categoryIdObj instanceof Integer) {
                    accountTransactionsDTO.setCategorisactionId(jsonObject.getInt("categorisactionId"));
                }
                if (categoryIdObj instanceof String) {
                    String categoryId = (String) jsonObject.get("categorisactionId");
                    if (StringUtils.isNotBlank(categoryId)) {
                        int categorisactionId = Integer.parseInt(categoryId);
                    accountTransactionsDTO.setCategorisactionId(categorisactionId); 
                    }
                }
            }
            if (jsonObject.has("currency")) {
                String currency = (String) jsonObject.get("currency");
                if (currency != null) {
                    accountTransactionsDTO.setCurrency(currency);
                    accountTransactionsDTO.setPayeeCurrency(currency);
                    accountTransactionsDTO.setTransactionCurrency(currency);
                }
            }
            double transactionAmount = jsonObject.has("transactionAmount")
                    ? Double.parseDouble((String) jsonObject.get("transactionAmount")) : 0;
            accountTransactionsDTO.setTransactionAmount(transactionAmount);
            accountTransactionsDTO.setAmount(transactionAmount);
            accountTransactionsDTO.setStatusDesc("successful");
            if (jsonObject.has("balanceSnapshot")) {
                double fromAccountBalance = Double.parseDouble((String) jsonObject.get("balanceSnapshot"));
                accountTransactionsDTO.setFromAccountBalance(fromAccountBalance);
            }
            if (jsonObject.has("id")) {
                accountTransactionsDTO.setTransactionId(jsonObject.getString("id"));
            }

            accountTransactionsDTO.setIsScheduled("false");
            accountTransactionsDTO.setStatusDescription("Successful");
            accountTransactions.add(accountTransactionsDTO);
        }

        return accountTransactions;
    }

    /**
     * method to convert perform filtering on the response base on the request
     * payload
     * 
     * @return JSONArray of records
     */
    public JSONArray performAdditionalFilteringOnReponse(String Value, AccountTransactionsDTO inputDTO,
            JSONArray records) {
        JSONObject obj = new JSONObject();
        if (StringUtils.isNotBlank(Value)) {
            JSONObject accountTransactionsJSONObject = new JSONObject(Value);
            if (accountTransactionsJSONObject.has("items")) {
                String transactionType = null;
                JSONArray updatetransactionID = accountTransactionsJSONObject.getJSONArray("items");
                // Inserting transactionType value based on the key retrieved
                // from the response
                // and filtering based on the transactionType input field.
                for (int response = 0; response < updatetransactionID.length(); response++) {
                    obj = (JSONObject) updatetransactionID.get(response);
                    if (obj.has("categorisactionId")) {
                        String categoryId = (String) obj.get("categorisactionId");
                        if (categoryId != null) {
                            int categorisactionId = Integer.parseInt(categoryId);
                            obj.put("categorisactionId", categorisactionId);
                            transactionType = getTransactionType(categorisactionId);
                            obj.put("transactionType", transactionType);
                        }
                    } else {
                        obj.put("transactionType", "Others");
                    }
                    String narrative = (String) obj.get("narrative");
                    if (narrative != null && narrative.length() == 0) {
                        obj.put("narrative", "NA");
                    }
                    records.put(obj);
                }
                return records;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * method to get the transactionType value which is mapped from response
     * field to config file
     * 
     * @return String transationType value
     */
    public String getTransactionType(int catID) {
        String value = TransactionTypeProperties.getValue(Integer.toString(catID));
        if (StringUtils.isBlank(value)) {
            String transactionType = "Others";
            return transactionType;
        }
        return value;
    }

    /**
     * method to filter the records whose filtration happens on isScheduled flag
     * 
     * @return JSONArray records
     */
    public JSONArray filterRecordsBasedOnIsScheduledFlag(JSONArray records, String isScheduled) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String presentDate = dateFormat.format(date);
        JSONArray updatedResponse = new JSONArray();

        if (isScheduled.equals("true")) {
            for (int response = 0; response < records.length(); response++) {
                JSONObject obj = (JSONObject) records.get(response);
                String processingDate = (String) obj.get("processingDate");
                int compareDate = processingDate.compareTo(presentDate);
                if (compareDate > 0) {
                    updatedResponse.put(obj);
                }
            }
        } else {
            for (int response = 0; response < records.length(); response++) {
                JSONObject obj = (JSONObject) records.get(response);
                String processingDate = (String) obj.get("processingDate");
                int compareDate = presentDate.compareTo(processingDate);
                if (compareDate >= 0) {
                    updatedResponse.put(obj);
                }
            }
        }

        return updatedResponse;
    }

    @Override
    public List<AccountTransactionsDTO> getPendingTransactionsDetailsFromT24(AccountTransactionsDTO inputDTO,
            DataControllerRequest request) throws ApplicationException {
        String Value = null;

        Map<String, Object> inputMap = new HashMap<>();

        inputMap.put("accountID", inputDTO.getAccountId());
        inputMap.put("searchStartDate", inputDTO.getSearchStartDate());

        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER,
                request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER));
        headerMap.put(FabricConstants.X_KONY_REPORTING_PARAMS_HEADER,
                request.getHeader(FabricConstants.X_KONY_REPORTING_PARAMS_HEADER));
        headerMap.put(TemenosConstants.TRANSACTION_PERMISSION,
                request.getHeader(TemenosConstants.TRANSACTION_PERMISSION));
        diagnostic.prepareDebug("T24 Pending Transactions Request : " + inputMap.toString()).log();

        try {
            Value = DBPServiceExecutorBuilder.builder()
                    .withServiceId(HoldingsAPIServices.T24_GETPENDINGTRANSACTIONS.getServiceName())

                    .withOperationId(HoldingsAPIServices.T24_GETPENDINGTRANSACTIONS.getOperationName())

                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            alert.prepareError("Unable to fetch pending transactions").log();
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
        diagnostic.prepareDebug("T24 Pending Transactions Response : " + Value).log();
        List<AccountTransactionsDTO> responseList = convertResponseToDTO(Value);

        return responseList;
    }

    public List<AccountTransactionsDTO> convertResponseToDTO(String Value) {
        JSONArray responseArray = new JSONArray();
        List<AccountTransactionsDTO> accountTransactions = new ArrayList<AccountTransactionsDTO>();
        if (StringUtils.isNotBlank(Value)) {
            JSONObject accountTransactionsJSONObject = new JSONObject(Value);
            if (accountTransactionsJSONObject.has("Transactions")) {
                responseArray = accountTransactionsJSONObject.getJSONArray("Transactions");
            }
            for (int index = 0; index < responseArray.length(); index++) {
                AccountTransactionsDTO accountTransactionsDTO = new AccountTransactionsDTO();
                JSONObject jsonObject = (JSONObject) responseArray.get(index);
                String accountId = (String) jsonObject.get("accountId");
                if (accountId != null) {
                    accountTransactionsDTO.setAccountId(accountId);
                    accountTransactionsDTO.setFromAccountNumber(accountId);
                }
                String processingDate = (String) jsonObject.get("transactionDate");
                if (processingDate != null) {
                    accountTransactionsDTO.setTransactionDate(processingDate);
                }
                if (jsonObject.has("transactionId")) {
                    String transactionReference = (String) jsonObject.get("transactionId");
                    if (transactionReference != null) {
                        accountTransactionsDTO.setId(transactionReference);
                        accountTransactionsDTO.setTransactionId(transactionReference);
                    }
                }
                if (jsonObject.has("transactionType")) {
                    String transactionType = (String) jsonObject.get("transactionType");
                    if (transactionType != null) {
                        accountTransactionsDTO.setTransactionType(transactionType);
                    }
                }
                if (jsonObject.has("payeeCurrency")) {
                    String currency = (String) jsonObject.get("payeeCurrency");
                    if (currency != null) {
                        accountTransactionsDTO.setPayeeCurrency(currency);
                    }
                }
                if (jsonObject.has("description")) {
                    String customerReference = (String) jsonObject.get("description");
                    if (customerReference != null) {
                        accountTransactionsDTO.setNotes(customerReference);
                        accountTransactionsDTO.setDescription(customerReference);
                    }
                }
                Object transactionAmount = jsonObject.get("amount");
                if (transactionAmount instanceof Integer) {
                    accountTransactionsDTO.setTransactionAmount(((Number) transactionAmount).doubleValue());
                    accountTransactionsDTO.setAmount(((Number) transactionAmount).doubleValue());
                } else if (transactionAmount instanceof String) {
                    accountTransactionsDTO.setTransactionAmount(Double.parseDouble((String) transactionAmount));
                    accountTransactionsDTO.setAmount(Double.parseDouble((String) transactionAmount));
                }else if (transactionAmount instanceof BigDecimal) {
                    accountTransactionsDTO.setTransactionAmount(((BigDecimal) transactionAmount).doubleValue());
                    accountTransactionsDTO.setAmount(((BigDecimal) transactionAmount).doubleValue());
                }
                else {
                    accountTransactionsDTO.setTransactionAmount((double) transactionAmount);
                    accountTransactionsDTO.setAmount((double) transactionAmount);
                }

                accountTransactionsDTO.setStatusDesc("Pending");
                if (jsonObject.has("fromAccountBalance")) {
                    Object fromAccountBalance = jsonObject.get("fromAccountBalance");
                    if (fromAccountBalance instanceof Integer) {
                        accountTransactionsDTO.setFromAccountBalance(((Number) fromAccountBalance).doubleValue());
                    } else if (fromAccountBalance instanceof String) {
                        accountTransactionsDTO.setFromAccountBalance(Double.parseDouble((String) fromAccountBalance));
                    } else if (fromAccountBalance instanceof BigDecimal) {
                        accountTransactionsDTO.setFromAccountBalance(((BigDecimal) fromAccountBalance).doubleValue());
                    } 
                    else {
                        accountTransactionsDTO.setFromAccountBalance((double) fromAccountBalance);
                    }
                }

                accountTransactionsDTO.setIsScheduled("false");

                accountTransactions.add(accountTransactionsDTO);
            }
            return accountTransactions;
        }
        return null;
    }

    @Override
    public List<AccountTransactionsDTO> getPendingAndPostedTransactions(AccountTransactionsDTO inputDTO,
            DataControllerRequest request, String authToken) throws ApplicationException {

        String value = null;
        Map<String, Object> inputMap = new HashMap<>();
    
        inputMap.put("accountID", inputDTO.getAccountId());
        inputMap.put("searchStartDate", inputDTO.getSearchStartDate());
        inputMap.put("order", inputDTO.getOrder());
        inputMap.put("offset", inputDTO.getOffset());
        inputMap.put("limit", inputDTO.getLimit());
        inputMap.put("transactionType", inputDTO.getTransactionType());
        inputMap.put("sortBy", inputDTO.getSortBy());
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER,
                request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER));
        headerMap.put(FabricConstants.X_KONY_REPORTING_PARAMS_HEADER,
                request.getHeader(FabricConstants.X_KONY_REPORTING_PARAMS_HEADER));
        // To Set Admin Privileges.
        if (StringUtils.isNotBlank(request.getParameter(TemenosConstants.TRANSACTION_PERMISSION))) {
            headerMap.put(TemenosConstants.TRANSACTION_PERMISSION,
                    request.getParameter(TemenosConstants.TRANSACTION_PERMISSION));
        }
        headerMap = generateSecurityHeaders(authToken, headerMap);
        diagnostic.prepareDebug("Pending and Posted Transactions Request : " + inputMap.toString()).log();
        try {
            value = DBPServiceExecutorBuilder.builder()
                    .withServiceId(HoldingsAPIServices.HOLDINGSMICROSERVICESJSON_GETACCOUNTPENDINGANDPOSTEDTRANSACTIONS
                            .getServiceName())
                    .withOperationId(
                            HoldingsAPIServices.HOLDINGSMICROSERVICESJSON_GETACCOUNTPENDINGANDPOSTEDTRANSACTIONS
                                    .getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
        
        diagnostic.prepareDebug("Pending and Posted Transactions Response : " + value).log();
        JSONObject serviceResponseJSON = Utilities.convertStringToJSON(value);
        List<AccountTransactionsDTO> accountTransactionsDTOList = new ArrayList<AccountTransactionsDTO>();
        try {
            if (serviceResponseJSON == null || serviceResponseJSON.optJSONArray("records") == null) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
            JSONArray banksJSONArr = serviceResponseJSON.optJSONArray("records");
            accountTransactionsDTOList = JSONUtils.parseAsList(banksJSONArr.toString(), AccountTransactionsDTO.class);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
        return accountTransactionsDTOList;
    }

    @Override
    public List<AccountTransactionsDTO> getLoanScheduleTransactions(AccountTransactionsDTO inputDTO,
            String isFutureRequired, DataControllerRequest request, String authToken) throws ApplicationException {

        String value = null;
        Map<String, Object> inputMap = new HashMap<>();
        inputMap.put("accountID", inputDTO.getAccountId());
        Map<String, Object> headerMap = new HashMap<>();
       
        headerMap.put(FabricConstants.X_KONY_AUTHORIZATION_HEADER,
                request.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER));
        headerMap.put(FabricConstants.X_KONY_REPORTING_PARAMS_HEADER,
                request.getHeader(FabricConstants.X_KONY_REPORTING_PARAMS_HEADER));
        headerMap = generateSecurityHeaders(authToken, headerMap);
        // To Set Admin Privileges.
        if (StringUtils.isNotBlank(request.getParameter(TemenosConstants.TRANSACTION_PERMISSION))) {
            headerMap.put(TemenosConstants.TRANSACTION_PERMISSION,
                    request.getParameter(TemenosConstants.TRANSACTION_PERMISSION));
        }
        diagnostic.prepareDebug("HMS Loan Schedule Request : " + inputMap.toString()).log();
        try {
            value = DBPServiceExecutorBuilder.builder()
                    .withServiceId(
                            HoldingsAPIServices.HOLDINGSMICROSERVICESJSON_GETLOANSCHEDULETRANSACTIONS.getServiceName())
                    .withOperationId(HoldingsAPIServices.HOLDINGSMICROSERVICESJSON_GETLOANSCHEDULETRANSACTIONS
                            .getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
        diagnostic.prepareDebug("HMS Loan Schedule Response : " + value).log();
        JSONObject serviceResponseJSON = Utilities.convertStringToJSON(value);
        List<AccountTransactionsDTO> accountTransactionsDTOList = new ArrayList<AccountTransactionsDTO>();
        try {
            if (serviceResponseJSON == null || serviceResponseJSON.optJSONArray("paymentSchedules") == null) {
                throw new ApplicationException(ErrorCodeEnum.ERR_20041);
            }
            JSONArray loanSchedulesArray = serviceResponseJSON.optJSONArray("paymentSchedules");
            loanSchedulesArray = updateScheduleTransaction(loanSchedulesArray, isFutureRequired);
            accountTransactionsDTOList = JSONUtils.parseAsList(loanSchedulesArray.toString(),
                    AccountTransactionsDTO.class);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }
        return accountTransactionsDTOList;
    }

	private JSONArray updateScheduleTransaction(JSONArray loanSchedulesArray, String isFutureRequired) {
		JSONArray finalscheduleArray = new JSONArray();
		float amount = 0;
		float principalAmount = 0;
		float charge = 0;
		float tax = 0;
		float interest = 0;
		float outstandingAmount = 0;
		//loanSchedulesArray.remove(0);
		for (int i = 0; i < loanSchedulesArray.length(); i++) {
			JSONObject loanSchedule = loanSchedulesArray.getJSONObject(i);
			principalAmount = loanSchedule.has("principleAmount") && loanSchedule.get("principleAmount") != null
					? Float.parseFloat(loanSchedule.get("principleAmount").toString())
					: 0;
			charge = loanSchedule.has("chargeAmount") && loanSchedule.get("chargeAmount") != null
					? Float.parseFloat(loanSchedule.get("chargeAmount").toString())
					: 0;
			String status = loanSchedule.has("status") && loanSchedule.get("status") != null
					? loanSchedule.get("status").toString()
					: "";
			String date = loanSchedule.has("scheduleDate") && loanSchedule.get("scheduleDate") != null
					? loanSchedule.get("scheduleDate").toString()
					: "";

			outstandingAmount = loanSchedule.has("outstandingBalance") && loanSchedule.get("outstandingBalance") != null
					? Float.parseFloat(loanSchedule.get("outstandingBalance").toString())
					: 0;

			if (status.equalsIgnoreCase("SETTLED")) {
				loanSchedule.put("installmentType", "PAID");
			} else if (status.equalsIgnoreCase("AGING")) {
				loanSchedule.put("installmentType", "DUE");
			} else {
				loanSchedule.put("installmentType", "FUTURE");
			}

			interest = loanSchedule.has("interestAmount") && loanSchedule.get("interestAmount") != null
					? Float.parseFloat(loanSchedule.get("interestAmount").toString())
					: 0;

			tax = loanSchedule.has("taxAmount") && loanSchedule.get("taxAmount") != null
					? Float.parseFloat(loanSchedule.get("taxAmount").toString())
					: 0;

			amount = principalAmount + interest + charge + tax;

			amount = amount != 0 ? (float) (Math.round(amount * 100.0) / 100.0) : 0;

			outstandingAmount = outstandingAmount != 0 ? (float) (Math.round(outstandingAmount * 100.0) / 100.0) : 0;

			principalAmount = principalAmount != 0 ? (float) (Math.round(principalAmount * 100.0) / 100.0) : 0;
			interest = interest != 0 ? (float) (Math.round(interest * 100.0) / 100.0) : 0;
			tax = tax != 0 ? (float) (Math.round(tax * 100.0) / 100.0) : 0;
			loanSchedule.put("interest", String.valueOf(interest));
			loanSchedule.put("principal", String.valueOf(principalAmount));
			// loanSchedule.put("cumulativeInterest", 0)// Back end is not
			// supporting
			// loanSchedule.put("insurance", 0) // Back end is not supporting
			loanSchedule.put("tax", String.valueOf(tax));
			loanSchedule.put("charges", String.valueOf(charge));
			loanSchedule.put("outstandingBalance", String.valueOf(outstandingAmount));
			loanSchedule.put("date", date);
			loanSchedule.put("installmentAmount", String.valueOf(amount));
			loanSchedule.put("amount", String.valueOf(amount));

			if (!loanSchedule.get("installmentType").equals("FUTURE")) {
				finalscheduleArray.put(loanSchedule);
			} else if (isFutureRequired.equalsIgnoreCase("true")) {
				finalscheduleArray.put(loanSchedule);
			}

		}
		return finalscheduleArray;
	}
    
    @Override
    public List<AccountTransactionsDTO> getDeviceRegistrationDetails(AccountTransactionsDTO inputPayLoad,
            DataControllerRequest request) throws ApplicationException {
        List<AccountTransactionsDTO> listDTO = new ArrayList<AccountTransactionsDTO>();
        StringBuilder sb = new StringBuilder();
        String deviceRegistrationDetails = null;
        sb.append("Customer_username").append(" eq ").append(inputPayLoad.getUserName()).append(" and ")
                .append("Device_id").append(" eq ").append(inputPayLoad.getDeviceId()).append(" and ")
                .append("Status_id").append(" eq ").append("SID_DEVICE_REGISTERED");
        Map<String, Object> inputMap = new HashMap<>();
        Map<String, Object> headerMap = new HashMap<>();
        inputMap.put("$filter", sb.toString());
        request.addRequestParam_("$filter", sb.toString());
        try {
            deviceRegistrationDetails = DBPServiceExecutorBuilder.builder()
                    .withServiceId(HoldingsAPIServices.DBXUSER_GET_ACCOUNTSOVERVIEW.getServiceName())
                    .withOperationId(HoldingsAPIServices.DBXUSER_GET_ACCOUNTSOVERVIEW.getOperationName())
                    .withRequestParameters(inputMap).withRequestHeaders(headerMap).withDataControllerRequest(request)
                    .build().getResponse();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCodeEnum.ERR_20041);
        }

        if (StringUtils.isNotBlank(deviceRegistrationDetails)) {
            JSONArray responseArray = new JSONArray();
            JSONObject accountPreviewJSONObject = new JSONObject(deviceRegistrationDetails);
            if (accountPreviewJSONObject.has("customer_device_information_view")) {
                responseArray = accountPreviewJSONObject.getJSONArray("customer_device_information_view");
            }
            for (int index = 0; index < responseArray.length(); index++) {
                AccountTransactionsDTO transactionsDTO = new AccountTransactionsDTO();
                JSONObject jsonObject = (JSONObject) responseArray.get(index);
                String channelDescription = (String) jsonObject.get("Channel_Description");
                if (channelDescription != null) {
                    transactionsDTO.setChannelDescription(channelDescription);
                }
                listDTO.add(transactionsDTO);
            }

        }
        return listDTO;
    }
}
