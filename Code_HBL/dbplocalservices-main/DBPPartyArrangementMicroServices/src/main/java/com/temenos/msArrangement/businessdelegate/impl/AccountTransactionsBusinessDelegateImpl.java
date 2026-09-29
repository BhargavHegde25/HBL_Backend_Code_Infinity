package com.temenos.msArrangement.businessdelegate.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.temenos.msArrangement.dto.AccountTransactionsDTO;
import com.temenos.msArrangement.businessdelegate.api.AccountTransactionsBusinessDelegate;
import com.temenos.msArrangement.utils.HTTPOperations;
import com.temenos.msArrangement.utils.MWConstants;
import com.temenos.msArrangement.utils.OperationName;

/**
 * 
 * @author KH2281
 * @version 1.0 Extends the {@link AccountTransactionsBusinessDelegate}
 */
public class AccountTransactionsBusinessDelegateImpl implements AccountTransactionsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	protected String key;

	/**
	 * method to get the Account Transactions
	 * 
	 * @return List<AccountTransactionsDTO> of Account Transactions
	 */
	public List<AccountTransactionsDTO> getDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO) {

		List<AccountTransactionsDTO> accountTransactions = new ArrayList<>();
		String Value = null;
		JSONArray records = new JSONArray();
		JSONObject obj = new JSONObject();
		JSONArray transactionTypeFilter = new JSONArray();
		key=mapInputRequest(inputDTO);
		
		String requestUrl = OperationName.GET_ACCOUNTTRANSACTIONS.replaceFirst(MWConstants.paramVariable, OperationName.COMPANY_ID+"-"+inputDTO.getAccountId());

		Value = HTTPOperations.sendHttpRequest("GET", requestUrl, null, null, null);
		if (StringUtils.isNotBlank(Value)) {
			JSONObject accountTransactionsJSONObject = new JSONObject(Value);
			JSONArray updatetransactionID = accountTransactionsJSONObject.getJSONArray(MWConstants.responseJSONKey);
			for (int response = 0; response < updatetransactionID.length(); response++) {
				obj = (JSONObject) updatetransactionID.get(response);
				int categorisactionId = (int) obj.get(MWConstants.categorisationId);
				String transactionType = getTransactionType(categorisactionId);
				obj.put(MWConstants.transactionType, transactionType);
				if (inputDTO.getTransactionType() != null) {
					if (transactionType != null && inputDTO.getTransactionType().equals(transactionType)) {
						transactionTypeFilter.put(obj);
					}
				}
				records.put(obj);
			}
			if (inputDTO.getTransactionType() != null && !inputDTO.getTransactionType().equals(MWConstants.All)) {
				records = transactionTypeFilter;
			}
			if (inputDTO.getIsScheduled() != null) {
				records = updateIsScheduledFlag(records, inputDTO.getIsScheduled());
			}
			if (inputDTO.getOrder() != null) {
				if(key.equals(MWConstants.transactionAmount)) {
					records = DoubleSort(records, inputDTO.getOrder());
				}
				else {
					records = sort(records, inputDTO.getOrder());	
				}
				
			}
			if (inputDTO.getOffset() != null && inputDTO.getLimit() != null) {
				records = filterRecords(records, inputDTO.getOffset(), inputDTO.getLimit());
			}

		} else {
			return null;
		}

		for (int index = 0; index < records.length(); index++) {
			AccountTransactionsDTO accountTransactionsDTO = new AccountTransactionsDTO();
			JSONObject jsonObject = (JSONObject) records.get(index);
			String accountId = (String) jsonObject.get("accountId");
			String transactionDate = (String) jsonObject.get("bookingDate");
			String transactionNotes = (String) jsonObject.get("customerReference");
			String description = (String) jsonObject.get("narrative");
			String scheduledDate = (String) jsonObject.get("processingDate");
			String transactionId = (String) jsonObject.get("transactionReference");
			String transactionType = (String) jsonObject.get("transactionType");
			String currency=(String)jsonObject.get("currency");
			double amount=(double)jsonObject.get("transactionAmount");
			if(jsonObject.has("fromAccountBalance")) {
				double fromAccountBalance=(double)jsonObject.get("fromAccountBalance");
				accountTransactionsDTO.setFromAccountBalance(fromAccountBalance);
			}
			
			if (accountId != null) {
				accountTransactionsDTO.setFromAccountNumber(accountId);
			}
			if (scheduledDate != null) {
				accountTransactionsDTO.setScheduledDate(scheduledDate);
				accountTransactionsDTO.setPostedDate(scheduledDate);
			}
			if (transactionNotes != null) {
				accountTransactionsDTO.setNotes(transactionNotes);
			}
			if (description != null) {
				accountTransactionsDTO.setDescription(description);
			}
			if (transactionDate != null) {
				accountTransactionsDTO.setTransactionDate(transactionDate);
			}
			if (transactionId != null) {
				accountTransactionsDTO.setId(transactionId);
			}
			if (transactionType != null) {
				accountTransactionsDTO.setTransactionType(transactionType);
			}
			if(currency!=null) {
				accountTransactionsDTO.setPayeeCurrency(currency);
			}
			accountTransactionsDTO.setAmount(amount);
			accountTransactionsDTO.setStatusDesc(MWConstants.successful);
		
			accountTransactions.add(accountTransactionsDTO);
		}

		return accountTransactions;
	}

	public String mapInputRequest(AccountTransactionsDTO inputDTO) {
		String returnValue=null;
		if (inputDTO.getSortBy()==null || inputDTO.getSortBy().equals(MWConstants.transactionDate)) {
			returnValue = MWConstants.bookingDate;
		}
		else if(inputDTO.getSortBy().equals(MWConstants.amount)) {
			returnValue =MWConstants.transactionAmount;
		}
		return returnValue;
	}

	public JSONArray sort(JSONArray jsonArr, String order) {

		JSONArray sortedJsonArray = new JSONArray();

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArr.length(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
		}

		Collections.sort(jsonValues, new Comparator<JSONObject>() {

			@Override
			public int compare(JSONObject a, JSONObject b) {
				String valA = new String();
				String valB = new String();
				
				try {
						valA = (String) a.get(key);
						valB = (String) b.get(key);
				} catch (JSONException e) {
					alert.prepareError("Caught exception at invoke of sorting: "+e).log();
				}

				if (order.equals("asc")) {
					return valA.compareTo(valB);
				}
				else {
					return valB.compareTo(valA);
				}
			}
		});

		for (int i = 0; i < jsonArr.length(); i++) {
			sortedJsonArray.put(jsonValues.get(i));
		}
		return sortedJsonArray;
	}

	public JSONArray DoubleSort(JSONArray jsonArr, String order) {

		JSONArray sortedJsonArray = new JSONArray();

		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int i = 0; i < jsonArr.length(); i++) {
			jsonValues.add(jsonArr.getJSONObject(i));
		}
		if (order.equals("desc")) {
		jsonValues.sort((o1, o2) -> Double.compare((double)o2.get(key),(double) o1.get(key)));
		}
		else {
			jsonValues.sort((o1, o2) -> Double.compare((double)o1.get(key),(double) o2.get(key)));	
		}
		for (int i = 0; i < jsonArr.length(); i++) {
			sortedJsonArray.put(jsonValues.get(i));
		}
		return sortedJsonArray;
	}

	public JSONArray filterRecords(JSONArray jsonArr, String offset, String limit) {
		JSONArray filteredJSONArray = new JSONArray();
		int startIndex = Integer.parseInt(offset);
		int lastIndex = startIndex + Integer.parseInt(limit);
		for (int i = startIndex; i < lastIndex && i < jsonArr.length(); i++) {
			filteredJSONArray.put(jsonArr.get(i));
		}
		return filteredJSONArray;
	}

	public String getTransactionType(int catID) {
		Properties configprop = new Properties();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
		try {
			configprop.load(inputStream);
		} catch (IOException e) {

		}finally {
        	if (inputStream!=null) {
        		try {
        			inputStream.close();			
        		}
        		catch(Exception e)
        		{
        			alert.prepareError(e.toString()).log();
        		}
        	}
        }
		if (configprop.getProperty(Integer.toString(catID)) == null) {
			String transactionType = MWConstants.Others;
			return transactionType;
		}
		return configprop.getProperty(Integer.toString(catID));
	}

	public JSONArray updateIsScheduledFlag(JSONArray records, String isScheduled) {
		Date date = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat(MWConstants.dateinFullYearFormat);
		String presentDate = dateFormat.format(date);
		JSONArray updatedResponse = new JSONArray();

		if (isScheduled.equals("true")) {
			for (int response = 0; response < records.length(); response++) {
				JSONObject obj = (JSONObject) records.get(response);
				String processingDate = (String) obj.get(MWConstants.processingDate);
				int compareDate = processingDate.compareTo(presentDate);
				if (compareDate > 0) {
					updatedResponse.put(obj);
				}
			}
		} else {
			for (int response = 0; response < records.length(); response++) {
				JSONObject obj = (JSONObject) records.get(response);
				String processingDate = (String) obj.get(MWConstants.processingDate);
				int compareDate = presentDate.compareTo(processingDate);
				if (compareDate >= 0) {
					updatedResponse.put(obj);
				}
			}
		}

		return updatedResponse;
	}

}
