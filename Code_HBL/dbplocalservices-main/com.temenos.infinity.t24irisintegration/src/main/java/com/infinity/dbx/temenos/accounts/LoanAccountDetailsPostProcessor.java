package com.infinity.dbx.temenos.accounts;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbx.BasePostProcessor;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class LoanAccountDetailsPostProcessor extends BasePostProcessor implements AccountsConstants {

	@Override
	public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Dataset interestDs = result.getDatasetById(DS_INTERESTS);
		Dataset schedules=result.getDatasetById(DS_SCHEDULES);
		double interestPaid = 0;
		if (interestDs != null) {
			for (Record rec : interestDs.getAllRecords()) {
				String dividendPaidYTD = CommonUtils.getParamValue(rec, INTEREST_PAID);
				dividendPaidYTD=dividendPaidYTD==null||"".equals(dividendPaidYTD)?"0":dividendPaidYTD;
				if (dividendPaidYTD.contains("|")) {
					String paidYTD[] = dividendPaidYTD.split("|");
					interestPaid = interestPaid + Double.parseDouble(paidYTD[0]);
				} else {
					interestPaid = interestPaid + Double.parseDouble(dividendPaidYTD);
				}				
			}
			result.addParam(new Param(INTEREST_PAID, String.valueOf(interestPaid), Constants.PARAM_DATATYPE_STRING));
		}
		
		if (schedules != null) {
			if (schedules.getAllRecords().size() > 0) {
				Record loanDetailRecord = schedules.getRecord(0);
				String futureScheduleCount = loanDetailRecord.getParamValueByName("futureScheduleCount")==null?"0": loanDetailRecord.getParamValueByName("futureScheduleCount");
				String paidScheduleCount = loanDetailRecord.getParamValueByName("paidScheduleCount")==null?"0": loanDetailRecord.getParamValueByName("paidScheduleCount");
				String dueScheduleCount = loanDetailRecord.getParamValueByName("dueScheduleCount")==null?"0": loanDetailRecord.getParamValueByName("dueScheduleCount");
				result.addParam(new Param(PAID_INSTALLMENT_COUNT, paidScheduleCount));
				result.addParam(new Param(OVERDUE_INSTALLMENT_COUNT, dueScheduleCount));
				result.addParam(new Param(FUTURE_INSTALLMENT_COUNT, futureScheduleCount));
				for (Record rec : schedules.getAllRecords()) {
				String schedulePaymentType = CommonUtils.getParamValue(rec, SCHEDULE_PAYMENTTYPE);
					if (schedulePaymentType.equals("Constant Repayment")) {
						String schedulePaymentFrequency = CommonUtils.getParamValue(rec, SCHEDULE_PAYMENT_FREQUENCY);
						result.addParam(new Param(REPAYMENT_FREQUENCY, schedulePaymentFrequency));
					} 
				}
			}
		}
		
		String[] interestRate = CommonUtils.getParamValue(result, INTEREST_RATE).split("%");
		result.getParamByName(INTEREST_RATE).setValue(interestRate[0]);
		String nextPaymentAmount;
		JsonObject accountHolderjson = new JsonObject();
		String accountHolder = CommonUtils.getParamValue(result, PARAM_ACCOUNT_HOLDER);
		accountHolderjson.addProperty(PARAM_USERNAME, accountHolder);
		accountHolderjson.addProperty(PARAM_FULLNAME, accountHolder);
		if (CommonUtils.getParamValue(result, NEXT_PAYMENT_AMOUNT) == null
				|| CommonUtils.getParamValue(result, NEXT_PAYMENT_AMOUNT).isEmpty())
			{nextPaymentAmount = "0";
			result.addParam(new Param(NEXT_PAYMENT_AMOUNT, nextPaymentAmount, Constants.PARAM_DATATYPE_STRING));
			}
		result.addParam(new Param(PARAM_ACCOUNT_HOLDER, accountHolderjson.toString(), Constants.PARAM_DATATYPE_STRING));
		
        // Add joint holders in result
        String jointHolder = CommonUtils.getParamValue(result, PARAM_ACCOUNT_JOINT_HOLDER) != ""
                ? CommonUtils.getParamValue(result, PARAM_ACCOUNT_JOINT_HOLDER) : "";
        if (StringUtils.isNotBlank(jointHolder)) {
            JsonArray accountJointHolders = new JsonArray();
            JsonObject accountJointHolder = new JsonObject();
            accountJointHolder.addProperty(PARAM_USERNAME, jointHolder);
            accountJointHolder.addProperty(PARAM_FULLNAME, jointHolder);
            accountJointHolders.add(accountJointHolder);
            result.addParam(new Param(PARAM_ACCOUNT_JOINT_HOLDER, accountJointHolders.toString(),
                    Constants.PARAM_DATATYPE_STRING));
        }
        return result;
	}
}
