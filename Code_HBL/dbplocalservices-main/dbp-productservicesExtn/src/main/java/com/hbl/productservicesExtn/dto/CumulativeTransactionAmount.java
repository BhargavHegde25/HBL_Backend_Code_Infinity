package com.hbl.productservicesExtn.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.transactionslimitengine.utils.TransactionsLimitConstants;
import com.dbp.transactionslimitengine.utils.TransactionsLimitErrorCodesEnum;
import com.google.gson.JsonObject;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class CumulativeTransactionAmount {
	static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final Logger logger = LogManager.getLogger(CumulativeTransactionAmount.class);
	
	public static JsonObject GetCumulativeTransactionAmount(Map<String,Object> inputParams){
		JsonObject retval = new JsonObject();
		String transactionstartdate=inputParams.get("scheduledDate")!=null?inputParams.get("scheduledDate").toString():"";
		String companyid=inputParams.get("companyId")!=null?inputParams.get("companyId").toString():"";
		String channel=inputParams.get("channel")!=null?inputParams.get("channel").toString():"";
		String operationid="dbxdb_GetCumulativeTransactionAmount";
		try {
			transactionstartdate = transactionstartdate.substring(0, 10);
			transactionstartdate = transactionstartdate + " 00:00:00";
			LocalDateTime transday = parseDate(transactionstartdate);
			if (transday == null) {
				retval.addProperty(TransactionsLimitConstants.DBPERRMSG,TransactionsLimitErrorCodesEnum.ERROR_INVALIDDATEFORMAT.getErrMsg());
				return retval;
			}
			processDailyTransactions(transday, companyid, retval, operationid, channel);
			processMonthlyTransactions(transday, companyid, retval, operationid, channel);
		}catch (Exception e) {
			logger.debug("Exception ", e);
			retval.addProperty(TransactionsLimitConstants.DBPERRMSG, e.getMessage());
			return retval;
		}
		return retval;
	}
	private static void processDailyTransactions(LocalDateTime transday, String companyid, JsonObject retval, String operationid, String channel) {
		Map<String, Object> inputmap = new HashMap<>();
		String transactionstartdate = transday.toLocalDate().toString();
		String transactionendday = getNdaysAfterDate(transday, 1).toLocalDate().toString();
		inputmap.put("in_start_Date", transactionstartdate);
		inputmap.put("in_end_Date", transactionendday);
		inputmap.put("in_companyId", companyid);
		inputmap.put("in_paidBy", channel);
		logger.debug("HBL:: inputmap: processDailyTransactions:" + inputmap.toString());
		Result res = callDBService(TransactionsLimitConstants.TRANSACTIONLIMIT_DB_SERVICE, operationid, inputmap);
		parseDBResponse(res, retval, TransactionsLimitConstants.DAILY, channel);
		logger.debug("HBL:: processDailyTransactions: channel:"+channel+":Response:"+ retval.toString());
	}
	private static void processMonthlyTransactions(LocalDateTime transday, String companyid, JsonObject retval, String operationid, String channel) {
		logger.debug("HBL:: inputmap: processMonthlyTransactions:");
		Map<String, Object> inputmap = new HashMap<>();
		LocalDate today = transday.toLocalDate();
		LocalDate currentransactionmonthstartday = today.withDayOfMonth(1);
		LocalDate currentransactionmonthendday = today.withDayOfMonth(today.lengthOfMonth());
		currentransactionmonthendday = currentransactionmonthendday.plusDays(1);
		String transactionstartdate = currentransactionmonthstartday.toString();
		String transactionendday =currentransactionmonthendday.toString();
		inputmap.put("in_start_Date", transactionstartdate);
		inputmap.put("in_end_Date", transactionendday);
		inputmap.put("in_companyId", companyid);
		inputmap.put("in_paidBy", channel);
		logger.debug("HBL:: inputmap: processMonthlyTransactions:" + inputmap.toString());
		Result res = callDBService(TransactionsLimitConstants.TRANSACTIONLIMIT_DB_SERVICE, operationid, inputmap);
		parseDBResponse(res, retval, "Monthly", channel);
		logger.debug("HBL:: processDailyTransactions: channel:"+channel+":Response:"+ retval.toString());
	}
	private static LocalDateTime parseDate(String date) {
		LocalDateTime t = null;
		try {
			t = LocalDateTime.parse(date, dtf);
		} catch (Exception e) {
			logger.debug("Parsing error", e);
		}
		return t;
	}
	private static LocalDateTime getNdaysAfterDate(LocalDateTime date, int noofdays) {
		if (date == null)
			return null;
		LocalDateTime t1 = null;
		t1 = date.plusDays(noofdays);
		return t1;
	}
	private static void parseDBResponse(Result res, JsonObject retval, String type, String channel) {
		String dbpErrMsg=res.getParamValueByName("dbpErrMsg");
		String dbpErrCode=res.getParamValueByName("dbpErrCode");
		JSONObject responseObj = new JSONObject(ResultToJSON.convert(res));
		logger.debug("HBL:: parseDBResponse: type:"+type+"channel:"+channel+" :responseObj:"+responseObj);
		if( StringUtils.isBlank(dbpErrMsg) ) {
			JSONObject respo = responseObj.getJSONArray("records").getJSONObject(0);
			String totalTransAmount=respo.has("total_sum")?respo.get("total_sum").toString():"0";
			double returnamount = Double.parseDouble(totalTransAmount);
				if (type.equals(TransactionsLimitConstants.DAILY))
					retval.addProperty("dailyLimit", returnamount);
				else if (type.equalsIgnoreCase("Monthly")) {
					retval.addProperty("monthlyLimit", returnamount);
				}
		}else {
			retval.addProperty(TransactionsLimitConstants.DBPERRMSG, dbpErrMsg);
		}
	}
	private static Result callDBService(String serviceid, String operationid, Map<String, Object> inputmap) {
		Result res = new Result();
		try {
			res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
					.withServiceId(serviceid).build().getResult();
		} catch (Exception e) {
			logger.debug("Error occured in fetching limits", e);
			res.addParam(new Param(TransactionsLimitConstants.DBPERRMSG, e.getMessage(), "String"));
			return res;

		}
		return res;
	}

}
