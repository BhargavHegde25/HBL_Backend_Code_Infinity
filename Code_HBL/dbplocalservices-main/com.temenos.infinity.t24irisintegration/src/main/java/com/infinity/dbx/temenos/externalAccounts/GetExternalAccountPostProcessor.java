package com.infinity.dbx.temenos.externalAccounts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetExternalAccountPostProcessor extends BasePostProcessor implements ExternalAccountsConstants{
	
	@Override
	  public Result execute(Result result, DataControllerRequest request,
	      DataControllerResponse response) throws Exception {
		
		String errmsg = CommonUtils.getParamValue(result, PARAM_ERR_MSG);
		if(!"".equalsIgnoreCase(errmsg) && errmsg.contains("No records")) {
			return TemenosUtils.getEmptyResult(DS_EXTERNAL_ACCOUNTS);
		}
		
		Dataset externalAccounts = result.getDatasetById(DS_EXTERNAL_ACCOUNTS);
				
		if(externalAccounts != null) {

			List<Record> extRecords = new ArrayList<Record>();
			
			SimpleDateFormat t24Format = new SimpleDateFormat(T24_FORMAT);
			
			SimpleDateFormat dbxDateFormat = new SimpleDateFormat(Constants.DATE_YYYYMMDD);
			
			// Get all records
			List<Record> records = externalAccounts.getAllRecords();
			ListIterator<Record> iter = records.listIterator();
			HashMap<String, ExternalAccount> sameBankAccounts = new HashMap<String, ExternalAccount>();
			HashMap<String, ExternalAccount> internationalAccounts = new HashMap<String, ExternalAccount>();
			HashMap<String, ExternalAccount> otherBankAccounts = new HashMap<String, ExternalAccount>();
			
			while(iter.hasNext()) {
				// Get the next record from the iterator
				Record record = iter.next();
				
				String  transactionType = CommonUtils.getParamValue(record, TRANSACTION_TYPE);
				
				String createdOn = CommonUtils.getParamValue(record, CREATED_ON);
				if(!"".equalsIgnoreCase(createdOn)) {
					Date date = new Date();
					date = (Date) t24Format.parse(createdOn);
					
					record.getParamByName(CREATED_ON).setValue(dbxDateFormat.format(date));
				}
								
				record.addParam(new Param(IS_VERIFIED, Constants.TRUE, Constants.PARAM_DATATYPE_STRING));
				record.addParam(new Param(ACCOUNT_TYPE, Constants.ACCOUNT_TYPE_SAVINGS, Constants.PARAM_DATATYPE_STRING));
				String accountNumber = CommonUtils.getParamValue(record, ACCOUNT_NUMBER);
				record.addParam(new Param(ACCOUNT_NUMBER, accountNumber, Constants.PARAM_DATATYPE_STRING));
				
				if(TRANSACTION_TYPE_OTIB.equalsIgnoreCase(transactionType)) {
					record.addParam(new Param(IS_SAMEBANK_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
					record.addParam(new Param(IS_INTERNATIONAL_ACCOUNT, Constants.TRUE, Constants.PARAM_DATATYPE_STRING));
					ExternalAccount externalAccount = TemenosUtils.copyToExtrenalAccount(ExternalAccount.class, record);
					internationalAccounts.put(accountNumber, externalAccount);
					
				}
				else if(TRANSACTION_TYPE_ACIB.equalsIgnoreCase(transactionType)){
					record.addStringParam(BENEFICIARY_NAME, CommonUtils.getParamValue(record, BENEFICIARY_SHORT_NAME));
					record.addParam(new Param(IS_SAMEBANK_ACCOUNT, Constants.TRUE, Constants.PARAM_DATATYPE_STRING));
					record.addParam(new Param(IS_INTERNATIONAL_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
					ExternalAccount externalAccount = TemenosUtils.copyToExtrenalAccount(ExternalAccount.class, record);
					sameBankAccounts.put(accountNumber, externalAccount);
				}
				else {
					record.addStringParam(IBAN, accountNumber);
					record.addParam(new Param(IS_SAMEBANK_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
					record.addParam(new Param(IS_INTERNATIONAL_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
					ExternalAccount externalAccount = TemenosUtils.copyToExtrenalAccount(ExternalAccount.class, record);
					otherBankAccounts.put(accountNumber, externalAccount);
				}
				
				extRecords.add(record);
			}
			
			Gson gson = new Gson();
			
			TemenosUtils temenosUtils = TemenosUtils.getInstance();

			if(!sameBankAccounts.isEmpty()) {
				String gsonSameBankAccounts = gson.toJson(sameBankAccounts);
				temenosUtils.insertIntoSession(TemenosConstants.SESSION_ATTRIB_DOMESTIC_ACCOUNT, gsonSameBankAccounts, request);
			}
			
			if(!internationalAccounts.isEmpty()) {
				String gsonInternationalAccounts = gson.toJson(internationalAccounts);
				temenosUtils.insertIntoSession(TemenosConstants.SESSION_ATTRIB_INTERNATIONAL_ACCOUNT, gsonInternationalAccounts, request);
			}
			if(!otherBankAccounts.isEmpty()) {
				String gsonOtherBankAccounts = gson.toJson(otherBankAccounts);
				temenosUtils.insertIntoSession(TemenosConstants.SESSION_ATTRIB_OTHERBANK_ACCOUNT, gsonOtherBankAccounts, request);
			}
			Dataset externalAccountsDS = new Dataset(DS_EXTERNAL_ACCOUNTS);
			externalAccountsDS.addAllRecords(extRecords);
			result.removeDatasetById(DS_EXTERNAL_ACCOUNTS);
			result.addDataset(externalAccountsDS);
			
		}
				
		return result;
	}
}
