package com.bct.eSewa;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class validateEsewaIdService implements JavaService2{
	private static final Logger logger = LogManager.getLogger(validateEsewaIdService.class);
	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		
		ServicesManager sm = request.getServicesManager();
		 ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
	     String ESEWA_BASE_URL = paramHelper.getServerProperty("ESEWA_BASE_URL");
	     String ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY = paramHelper.getServerProperty("ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY");
	     String ESEWA_CLIENT_SIGNATURE_PRIVATEKEY = paramHelper.getServerProperty("ESEWA_CLIENT_SIGNATURE_PRIVATEKEY");
	     String ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY = paramHelper.getServerProperty("ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY");
	     String ESEWA_SERVER_SIGNATURE_PUBLICKEY = paramHelper.getServerProperty("ESEWA_SERVER_SIGNATURE_PUBLICKEY");
	     String ESEWA_CLINET_ID = paramHelper.getServerProperty("ESEWA_CLINET_ID");
	     String ESEWA_SWIFT_CODE = paramHelper.getServerProperty("ESEWA_SWIFT_CODE");
	     String eSewaEntitlement = paramHelper.getServerProperty("ESEWA_ENITLEMENT");
	     
	     String VALIDATION_URL = ESEWA_BASE_URL + "/api/auth/load/validate_esewa_id/v1";
	     
		 Result result = new Result();
		 
		 //String channel = eSewaLimitCheck.getCurrentChannel(request);
		 //logger.debug("channel details ##"+ channel);
		 Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		 logger.error(" inputParams validateEsewaIdService ***"+inputParams);
		 String targetedMobile = request.getParameter("targetedMobile");
		 String targetedAmount = request.getParameter("targetedAmount");
		 String frmAccNumber = request.getParameter("frmAccNumber");
		 logger.debug("targetedMobile ##"+ targetedMobile);
		 logger.debug("targetedAmount ##"+ targetedAmount);
		 logger.debug("frmAccNumber ##"+ frmAccNumber);
		 
		 /*** eSewa disable for production ***/
		 
		 if (eSewaEntitlement.equalsIgnoreCase("false")) {
				result.setParam(new Param("message", "eSewa Service is currently not available!"));
				result.addParam("errmsg", "eSewa Service is currently not available!");
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));

				return result;
			}
		 
		 /*** End ****/
		 
		 String res = "";
			try {
				res = EsewaValidationClient.validateeSewaId(request, targetedMobile, targetedAmount,
						ESEWA_SERVER_DATA_ENCRYPTION_PUBLICKEY, ESEWA_CLIENT_SIGNATURE_PRIVATEKEY,
						ESEWA_CLIENT_DATA_ENCRYPTION_PRIVATE_KEY, ESEWA_SERVER_SIGNATURE_PUBLICKEY, ESEWA_CLINET_ID,
						ESEWA_SWIFT_CODE, VALIDATION_URL);
				if (res == null || res.isEmpty()) {
					result.setParam(new Param("message",
							"We’re experiencing issues with eSewa services. Please try again later."));
					result.addParam("errmsg", "We’re experiencing issues with eSewa services. Please try again later.");
					result.setParam(new Param("opstatus", "0"));
					result.setParam(new Param("httpStatusCode", "200"));

					return result;
				}
			} catch (Exception e) {
				result.setParam(
						new Param("message", "We’re experiencing issues with eSewa services. Please try again later."));
				result.addParam("errmsg", "We’re experiencing issues with eSewa services. Please try again later.");
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));

				return result;
			}
		 
		 //{"success":true,"code":"0","message":"Account details obtained successfully.","bankCode":"ESEWA","accountName":"esewa","maskedAccountName":"es*** ","accounts":[{"accountNumber":"9841432993","maskedAccount":"######2993"}]}

		 ObjectMapper mapper = new ObjectMapper();
         EsewaResponse resData = mapper.readValue(res, EsewaResponse.class);
         System.out.println("Success: " + resData.isSuccess());
         System.out.println("Code: " + resData.getCode());
         
         
         if(resData.isSuccess() && resData.getCode().equalsIgnoreCase("0")) {
        	 //Success
        	 String message = validateeSewaLimit(request, targetedAmount, frmAccNumber, targetedMobile);
        	 logger.debug("final message ##"+ message); 
 			if (message != null && !message.trim().isEmpty()) {
 				result.setParam(new Param("message", message));
 				result.addParam("errmsg", message);
 				result.setParam(new Param("opstatus", "0"));
 				result.setParam(new Param("httpStatusCode", "200"));

 				return result;
 			}
        	 result.setParam(new Param("success", resData.isSuccess()+""));
        	 result.setParam(new Param("code", resData.getCode()));
        	 result.setParam(new Param("message", resData.getMessage()));
        	 result.setParam(new Param("bankCode", resData.getBankCode()));
        	 result.setParam(new Param("accountName", resData.getAccountName()));
        	 result.setParam(new Param("maskedAccountName", resData.getMaskedAccountName()));
        	 result.setParam(new Param("accountNumber", resData.getAccounts().get(0).getAccountNumber()));
        	 result.setParam(new Param("maskedAccount", resData.getAccounts().get(0).getMaskedAccount()));
        	 result.setParam(new Param("responseData", resData.toString()));
     		 result.setParam(new Param("opstatus", "0"));
     		 result.setParam(new Param("httpStatusCode", "200"));
         }else {
        	 //Failure
        	 result.setParam(new Param("success", resData.isSuccess()+""));
        	 result.setParam(new Param("code", resData.getCode()));
        	 result.setParam(new Param("message", resData.getMessage()));
        	 result.setParam(new Param("responseData", resData.toString()));
     		 result.setParam(new Param("opstatus", "0"));
     		 result.setParam(new Param("httpStatusCode", "200"));
         }
         
		 
		return result;
	}
	
	public String validateeSewaLimit(DataControllerRequest request, String amount, String frmAccNumber, String eSewaId) {
		String message = "";
		try {
			String channel = eSewaLimitCheck.getCurrentChannel(request);
			logger.debug("channel ##" + channel);
			double amnt = Double.parseDouble(amount);

			double totalToday;
			totalToday = eSewaLimitCheck.getTodayTransactionAmountTotal(request, frmAccNumber, eSewaId, channel);
			logger.debug("totalToday ##" + totalToday);
			double totalMonth = eSewaLimitCheck.getThisMonthTotalTransactionAmount(request, frmAccNumber, eSewaId,
					channel);
			logger.debug("totalMonth ##" + totalMonth);
			int monthlyCount = eSewaLimitCheck.getMonthlyTransactionCountByAccount(request, frmAccNumber, channel);
			logger.debug("monthlyCount ##" + monthlyCount);
			int dailyCount = eSewaLimitCheck.getDailyTransactionCountByAccount(request, frmAccNumber, channel);
			logger.debug("dailyCount ##" + dailyCount);
			int dailyWalletCount = eSewaLimitCheck.getDailyTransactionCountByWallet(request, frmAccNumber, channel);
			logger.debug("dailyWalletCount ##" + dailyWalletCount);

			if (channel.equalsIgnoreCase("OnlineBanking")) {
				/** OLB Minimum Amount per Transaction check **/
				double OLB_MIN_AMT_PER_TXN = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "OLB_MIN_AMT_PER_TXN"));
				logger.debug("OLB_MIN_AMT_PER_TXN ##" + OLB_MIN_AMT_PER_TXN);
				double OLB_MAX_AMT_PER_TXN = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "OLB_MAX_AMT_PER_TXN"));
				logger.debug("OLB_MAX_AMT_PER_TXN ##" + OLB_MAX_AMT_PER_TXN);
				double OLB_MAX_AMT_PER_DAY = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "OLB_MAX_AMT_PER_DAY"));
				logger.debug("OLB_MAX_AMT_PER_DAY ##" + OLB_MAX_AMT_PER_DAY);
				double OLB_MAX_AMT_PER_MONTH = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "OLB_MAX_AMT_PER_MONTH"));
				logger.debug("OLB_MAX_AMT_PER_MONTH ##" + OLB_MAX_AMT_PER_MONTH);
				int OLB_MAX_TXN_PER_DAY = Integer
						.parseInt(eSewaLimitCheck.geteSewaChannelLimit(request, "OLB_MAX_TXN_PER_DAY"));
				logger.debug("OLB_MAX_TXN_PER_DAY ##" + OLB_MAX_TXN_PER_DAY);
				int OLB_MAX_TXN_PER_MONTH = Integer
						.parseInt(eSewaLimitCheck.geteSewaChannelLimit(request, "OLB_MAX_TXN_PER_MONTH"));
				logger.debug("OLB_MAX_TXN_PER_MONTH ##" + OLB_MAX_TXN_PER_MONTH);
				int OLB_MAX_TXN_PER_DAY_WALLET_ACCOUNT = Integer
						.parseInt(eSewaLimitCheck.geteSewaChannelLimit(request, "OLB_MAX_TXN_PER_DAY_WALLET_ACCOUNT"));
				logger.debug("OLB_MAX_TXN_PER_DAY_WALLET_ACCOUNT ##" + OLB_MAX_TXN_PER_DAY_WALLET_ACCOUNT);

				if (amnt < OLB_MIN_AMT_PER_TXN) {
					message = "The amount must be at least" + " NPR " + OLB_MIN_AMT_PER_TXN + "per transaction.";
					logger.debug("OLB_MIN_AMT_PER_TXN message ##" + message);
				} else if (amnt > OLB_MAX_AMT_PER_TXN) {
					message = "The amount exceeds the maximum allowed per transaction of" + " NPR "
							+ OLB_MAX_AMT_PER_TXN + ".";
					logger.debug("OLB_MAX_AMT_PER_TXN message ##" + message);
				} else if (!eSewaLimitCheck.isWithinDailyLimit(amnt, totalToday, OLB_MAX_AMT_PER_DAY)) {
					message = "You have reached your daily transaction limit of " + " NPR " + OLB_MAX_AMT_PER_DAY + ".";
					logger.debug("OLB_MAX_AMT_PER_DAY message ##" + message);
				} else if (!eSewaLimitCheck.isWithinMonthlyLimit(amnt, totalMonth, OLB_MAX_AMT_PER_MONTH)) {
					message = "You have reached your monthly transaction limit of" + " NPR " + OLB_MAX_AMT_PER_MONTH
							+ ".";
					logger.debug("OLB_MAX_TXN_PER_MONTH ##" + message);
				} else if (dailyCount > OLB_MAX_TXN_PER_DAY) {
					message = "You have reached the maximum number of transactions allowed for today.";
					logger.debug("dailyCount message ##" + message);
				} else if (monthlyCount > OLB_MAX_TXN_PER_MONTH) {
					message = "You have reached the maximum number of transactions allowed for this month.";
					logger.debug("monthlyCount message ##" + message);
				} else if (dailyWalletCount > OLB_MAX_TXN_PER_DAY_WALLET_ACCOUNT) {
					message = "You have reached the maximum number of transactions allowed today for entered wallet account.";
					logger.debug("dailyWalletCount message ##" + message);
				}

			} else if (channel.equalsIgnoreCase("MobileBanking")) {
				double MB_MIN_AMT_PER_TXN = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "MB_MIN_AMT_PER_TXN"));
				double MB_MAX_AMT_PER_TXN = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "MB_MAX_AMT_PER_TXN"));
				double MB_MAX_AMT_PER_DAY = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "MB_MAX_AMT_PER_DAY"));
				double MB_MAX_AMT_PER_MONTH = Double
						.parseDouble(eSewaLimitCheck.geteSewaChannelLimit(request, "MB_MAX_AMT_PER_MONTH"));
				int MB_MAX_TXN_PER_DAY = Integer
						.parseInt(eSewaLimitCheck.geteSewaChannelLimit(request, "MB_MAX_TXN_PER_DAY"));
				int MB_MAX_TXN_PER_MONTH = Integer
						.parseInt(eSewaLimitCheck.geteSewaChannelLimit(request, "MB_MAX_TXN_PER_MONTH"));
				int MB_MAX_TXN_PER_DAY_WALLET_ACCOUNT = Integer
						.parseInt(eSewaLimitCheck.geteSewaChannelLimit(request, "MB_MAX_TXN_PER_DAY_WALLET_ACCOUNT"));

				if (amnt < MB_MIN_AMT_PER_TXN) {
					message = "The amount must be at least" + " NPR " + MB_MIN_AMT_PER_TXN + "per transaction.";
				} else if (amnt > MB_MAX_AMT_PER_TXN) {
					message = "The amount exceeds the maximum allowed per transaction of" + " NPR " + MB_MAX_AMT_PER_TXN
							+ ".";
				} else if (!eSewaLimitCheck.isWithinDailyLimit(amnt, totalToday, MB_MAX_AMT_PER_DAY)) {
					message = "You have reached your daily transaction limit of " + " NPR " + MB_MAX_AMT_PER_DAY + ".";
				} else if (!eSewaLimitCheck.isWithinMonthlyLimit(amnt, totalMonth, MB_MAX_AMT_PER_MONTH)) {
					message = "You have reached your monthly transaction limit of" + " NPR " + MB_MAX_AMT_PER_MONTH
							+ ".";
				} else if (dailyCount > MB_MAX_TXN_PER_DAY) {
					message = "You have reached the maximum number of transactions allowed for today.";
				} else if (monthlyCount > MB_MAX_TXN_PER_MONTH) {
					message = "You have reached the maximum number of transactions allowed for this month.";
				} else if (dailyWalletCount > MB_MAX_TXN_PER_DAY_WALLET_ACCOUNT) {
					message = "You have reached the maximum number of transactions allowed today for entered wallet account.";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return message;
		}
		return message;
	}

}
