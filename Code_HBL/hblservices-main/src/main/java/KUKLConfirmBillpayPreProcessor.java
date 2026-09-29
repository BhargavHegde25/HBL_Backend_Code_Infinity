import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import com.bct.preprocessor.KUKLGetCustomerBillInfoPreProcessor;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class KUKLConfirmBillpayPreProcessor implements DataPreProcessor2{
	public static LoggerUtil logger = new LoggerUtil(KUKLConfirmBillpayPreProcessor.class);

	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response, Result result)
			throws Exception {
		   String userId = EnvironmentConfigurationsHandler.getServerProperty("KUKL_PAYMENT_AUTH_USER");
	       String password = EnvironmentConfigurationsHandler.getServerProperty("KUKL_PAYMENT_AUTH_PASSWORD");
	       String merchantId = EnvironmentConfigurationsHandler.getServerProperty("KUKL_PAYMENT_MERCHANT_ID");
	       String bankId = EnvironmentConfigurationsHandler.getServerProperty("KUKL_PAYMENT_BANK_ID");
	       String branchcode = EnvironmentConfigurationsHandler.getServerProperty("KUKL_PAYMENT_BRANCH_CODE");
	       String randomid=String.valueOf(generateBatchId());
	       inputMap.put("merchantId", merchantId);
	       //inputMap.put("txnReferenceNo", "<<DebitReferenceId>>");
	       inputMap.put("txnReferenceNo", randomid);
	       inputMap.put("bankId", bankId);
	       inputMap.put("txnDate", generateTxnDate());
	       inputMap.put("branchcode", branchcode);
	       //inputMap.put("password", neaPaymentsPassword);
		   //Map<String, Object> headermap = new HashMap<String, Object>();
		   //headermap.put("Authorization", AUTHORIZATION);
	       String Authorization=generateBasicAuthorization(userId, password);
	       logger.debug("HBL:KUKLConfirmBillpayPreProcessor:payload:"+inputMap);
	       request.getHeaderMap().put(TemenosConstants.PARAM_AUTHORIZATION, Authorization);
		return true;
	}
	
	public String generateBasicAuthorization(String userName, String password) {
		String authorization=userName+":"+password;
		authorization= Base64.getEncoder().encodeToString(authorization.getBytes());
		return "Basic "+authorization;
	}
	public String generateBatchId(){
		String batchId = "HBL" + "-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return batchId;
	}
	public String generateTxnDate(){
		String batchId =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return batchId;
	}

}
