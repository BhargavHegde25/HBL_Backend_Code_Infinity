package com.infinity.dbx.temenos.externalAccounts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

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

public class GetSameBankAccountPostProcessor extends BasePostProcessor implements ExternalAccountsConstants{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	  public Result execute(Result result, DataControllerRequest request,
	      DataControllerResponse response) throws Exception {
		
		String errmsg = CommonUtils.getParamValue(result, PARAM_ERR_MSG);
		alert.prepareError("Error " + errmsg).log();
		if(!"".equalsIgnoreCase(errmsg) && errmsg.contains("No records")) {
			return TemenosUtils.getEmptyResult(DS_EXTERNAL_ACCOUNTS);
		}
		
		Dataset externalAccounts = result.getDatasetById(DS_EXTERNAL_ACCOUNTS);
		List<Record> domesticRecords = new ArrayList<Record>();
		
		if(externalAccounts != null) {
			
			// Get all records
			List<Record> records = externalAccounts.getAllRecords();
			ListIterator<Record> iter = records.listIterator();
			
			SimpleDateFormat t24Format = new SimpleDateFormat(T24_FORMAT);
			SimpleDateFormat dbxDateFormat = new SimpleDateFormat(Constants.DATE_YYYYMMDD);
			
			HashMap<String, ExternalAccount> domesticBeneficiaries = new HashMap<String, ExternalAccount>();
			
			while(iter.hasNext()) {
				
				// Get the next record from the iterator
				Record record = iter.next();
				
				String  transactionType = CommonUtils.getParamValue(record, TRANSACTION_TYPE);
				String createdOn = CommonUtils.getParamValue(record, CREATED_ON);
				
				if(TRANSACTION_TYPE_ACIB.equalsIgnoreCase(transactionType)) {					
					
					if(!"".equalsIgnoreCase(createdOn)) {
						Date date = new Date();
						date = (Date) t24Format.parse(createdOn);
						
						record.getParamByName(CREATED_ON).setValue(dbxDateFormat.format(date));
					}
					record.addParam(new Param(IS_INTERNATIONAL_ACCOUNT, Constants.FALSE, Constants.PARAM_DATATYPE_STRING));
					record.addParam(new Param(IS_SAMEBANK_ACCOUNT, Constants.TRUE, Constants.PARAM_DATATYPE_STRING));
					record.addParam(new Param(IS_VERIFIED, Constants.TRUE, Constants.PARAM_DATATYPE_STRING));
					domesticRecords.add(record);
					
					ExternalAccount externalAccount = TemenosUtils.copyToExtrenalAccount(ExternalAccount.class, record);
					domesticBeneficiaries.put(externalAccount.getAccountNumber(), externalAccount);
				}
			}
			
			Gson gson = new Gson();

			String gsonBeneficiaries = gson.toJson(domesticBeneficiaries);		
			
			TemenosUtils temenosUtils = TemenosUtils.getInstance();
			
			temenosUtils.insertIntoSession(TemenosConstants.SESSION_ATTRIB_DOMESTIC_ACCOUNT, gsonBeneficiaries, request);
			
		}
		
		Result finalResult = new Result();
		Dataset domesticDS = new Dataset(DS_EXTERNAL_ACCOUNTS);
		domesticDS.addAllRecords(domesticRecords);
		finalResult.addDataset(domesticDS);
		finalResult.addOpstatusParam(0);
		finalResult.addHttpStatusCodeParam(200);
		return finalResult;
	}
}
