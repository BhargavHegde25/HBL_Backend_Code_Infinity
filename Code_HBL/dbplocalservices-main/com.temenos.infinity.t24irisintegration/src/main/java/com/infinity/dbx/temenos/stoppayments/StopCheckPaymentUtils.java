package com.infinity.dbx.temenos.stoppayments;

import java.math.BigDecimal;

public class StopCheckPaymentUtils implements StopPaymentConstants{
	public static String convertCheckReasonToDBX(String checkReason)
	{
		if (checkReason.equalsIgnoreCase(PARAM_T24CHECKREASON_DUPLICATE)) {
			checkReason = PARAM_CHECKREASON_DUPLICATE;
		} else if (checkReason.equalsIgnoreCase(PARAM_T24CHECKREASON_INSUFFICIENT_FUNDS)) {
			checkReason = PARAM_CHECKREASON_INSUFFICIENT_FUNDS;
		} else if (checkReason.equalsIgnoreCase(PARAM_T24CHECKREASON_LOST_OR_STOLEN)) {
			checkReason = PARAM_CHECKREASON_LOST_OR_STOLEN;
		} else if (checkReason.equalsIgnoreCase(PARAM_T24CHECKREASON_DEFECTIVE_GOODS)) {
			checkReason = PARAM_CHECKREASON_DEFECTIVE_GOODS;
		} else {
			checkReason = PARAM_CHECKREASON_OTHERS;
		}
		return checkReason;
		
	}
	
	//Convert the Amount values string to double
	public static Double convertAmountToDouble(Object amountObject){
	    Double amount = 0.0;
            if (amountObject instanceof Integer) {
                amount = ((Number) amountObject).doubleValue();
            } else if (amountObject instanceof String) {
                amount = Double.parseDouble((String) amountObject);
            } else if (amountObject instanceof BigDecimal) {
                amount = ((BigDecimal) amountObject).doubleValue();
            } else {
                amount = (double) amountObject;
            }
	    return amount;
	}
}
