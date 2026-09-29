package com.temenos.dbx.product.commons.businessdelegate.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commons.dto.ApplicationDTO;
import com.temenos.dbx.product.commonsutils.CommonUtils;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.constants.ObjectId;
import com.temenos.dbx.product.constants.OperationName;
import com.temenos.dbx.product.constants.ServiceId;

public class ApplicationBusinessDelegateImpl implements ApplicationBusinessDelegate {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public ApplicationDTO properties() {
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId(ServiceId.DBPRBLOCALSERVICEDB).
					withObjectId(null).
					withOperationId(OperationName.DB_APPLICATION_GET).
					withRequestParameters(null).
					build().getResponse();
			
			JSONObject res = new JSONObject(response);
			JSONArray propertyArray = res.getJSONArray("application");
			List<ApplicationDTO> propList = JSONUtils.parseAsList(propertyArray.toString(), ApplicationDTO.class);
			
			if(propList.size() <= 0) {
				return null;
			}
			
			return propList.get(0);
		}
		catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
		}
		return null;
	}

	@Override
	public String getServerTimeStamp() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIMESTAMP_FORMAT);
		try {
			String response = DBPServiceExecutorBuilder.builder().
					withServiceId("TransactionObjects").
			        withObjectId("BankDate").
			        withOperationId(OperationName.GETBANKDATE).
			        withRequestParameters(null).
			        build().getResponse();
			JSONObject responseObj = new JSONObject(response);
			JSONArray jsonArray = CommonUtils.getFirstOccuringArray(responseObj);
			String workingDate = ((JSONObject)jsonArray.get(0)).getString(Constants.CURRENT_WORKING_DATE);
			DateFormat shortDateFormat = new SimpleDateFormat(Constants.SHORT_DATE_FORMAT);
			if(! shortDateFormat.format(new Date()).equalsIgnoreCase(workingDate)) {
				return dateFormat.format(shortDateFormat.parse(workingDate));
			}
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching Server TimeStamp", e).log();
		} 

		ApplicationDTO props = properties();
		if(props == null) {
			props = new ApplicationDTO();
		}
		
		String timezone = props.getTimeZoneOffset();
		String pos = null;
		
		if(timezone != null && timezone.indexOf("-") > 0) {
			pos = "-";
		}
		else if(timezone != null && timezone.indexOf("+") > 0) {
			pos = "+";
		}
		else {
			timezone = "UTC+00:00";
			pos = "+";
		}
		
		String[] offset = timezone.split("\\"+pos);
		String[] time = offset[1].split(":");
		int hours = Integer.parseInt(pos+time[0]);
		int minutes = Integer.parseInt(pos+time[1]);
		int offsetMillis = (hours*60*60 + minutes*60)*1000 ;
		
		TimeZone tz = TimeZone. getDefault();
		tz.setRawOffset(offsetMillis);
		dateFormat.setTimeZone(tz);
		
		Date date = new Date();
		String finalDate = dateFormat.format(date);
		
		return finalDate;
	}
	
	@Override
	public ApplicationDTO getPropertiesFromCache() {
		try {
			String applicationDetails = (String) MemoryManager.getFromCache(DBPUtilitiesConstants.APPLICATION_CACHE_KEY);
			
			if (null == applicationDetails) {
				ApplicationDTO applicationObj = properties();
				MemoryManager.saveIntoCache(DBPUtilitiesConstants.APPLICATION_CACHE_KEY, new JSONObject(applicationObj).toString(), 120);
				return applicationObj;
			}

			JsonObject jsonObj = new JsonParser().parse(applicationDetails).getAsJsonObject().getAsJsonObject();
			return JSONUtils.parse(jsonObj.toString(), ApplicationDTO.class);

		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
			return null;
		}
	}
	
	@Override
	public boolean getIsStateManagementAvailableFromCache() {
		try {
			Object isStateManagementAvailableObj = MemoryManager.getFromCache(DBPUtilitiesConstants.IS_STATE_MANAGEMENT_AVAILABLE);
		
			if (null == isStateManagementAvailableObj) {
				ApplicationDTO applicationObj = properties();
				MemoryManager.saveIntoCache(DBPUtilitiesConstants.IS_STATE_MANAGEMENT_AVAILABLE, applicationObj.isStateManagementAvailable() + "" , 120);
				isStateManagementAvailableObj = MemoryManager.getFromCache(DBPUtilitiesConstants.IS_STATE_MANAGEMENT_AVAILABLE);
			}

			return Boolean.parseBoolean((String) isStateManagementAvailableObj) ;
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
			return false;
		}
	}

	@Override
	public String getBaseCurrencyFromCache() {
		try {
			String baseCurrency = (String) MemoryManager.getFromCache(DBPUtilitiesConstants.CURRENCYCODE);
		
			if (StringUtils.isEmpty(baseCurrency)) {
				ApplicationDTO applicationObj = properties();
				MemoryManager.saveIntoCache(DBPUtilitiesConstants.CURRENCYCODE, applicationObj.getCurrencyCode(), 120);
				baseCurrency = (String) MemoryManager.getFromCache(DBPUtilitiesConstants.CURRENCYCODE);
			}

			return baseCurrency ;
			
		} catch (Exception e) {
			alert.prepareError("Exception caught while fetching application properties", e).log();
			return null;
		}
	}

	@Override
	public boolean getIsSelfApprovalEnabledFromCache(){
		try{
			String isSelfApprovalEnabledStr = (String) MemoryManager.getFromCache(DBPUtilitiesConstants.IS_SELFAPPROVAL_ENABLED);
			if(isSelfApprovalEnabledStr == null){
				ApplicationDTO applicationObj = properties();
				MemoryManager.saveIntoCache(DBPUtilitiesConstants.IS_SELFAPPROVAL_ENABLED, applicationObj.isSelfApprovalEnabled() ? "1" : "0", 120);
				return applicationObj.isSelfApprovalEnabled();
			} else {
				return isSelfApprovalEnabledStr.equals("1") ? true : false;
			}
		} catch (Exception e){
			alert.prepareError("Exception caught while fetcing isSelfApprovalEnabled from application properties: ", e).log();
			return false;
		}
	}
}

