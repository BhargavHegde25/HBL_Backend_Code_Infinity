package com.dbp.transactionslimitengine.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dbp.transactionslimitengine.businessdelegate.impl.TransactionsLimitBusinessDelegateImpl;

public enum FEATUREENUM {

	P2PTRANSFERS, INTRABANKTRANSFERS, WIRETRANSFERS, INTERBANKFUNDTRANSFERS, INTERNATIONALFUNDTRANSFERS,
	BILLPAYTRANSFERS, ACHTRANSACTION, ACHFILE, OWNACCOUNTTRANSFERS,BULK_PAYMENT_REQUEST_SUBMIT,BULK_PAYMENT_SINGLE_SUBMIT,BULK_PAYMENT_MULTIPLE_SUBMIT;
	private static final Logger logger = LogManager.getLogger(TransactionsLimitBusinessDelegateImpl.class);
	// static final Properties
	static final Map<String, FEATUREENUM> OPERATIONS_MAPPER = getMapper();

	public static String getDBServiceKey(String featureid) {
		if (OPERATIONS_MAPPER.get(featureid) == null)
			return null;

		logger.debug(TransactionDBConstants.dbservicemap);
		logger.debug(OPERATIONS_MAPPER.get(featureid));
		logger.debug(TransactionDBConstants.dbservicemap.get(OPERATIONS_MAPPER.get(featureid).toString()));
		return TransactionDBConstants.dbservicemap.get(OPERATIONS_MAPPER.get(featureid).toString());
	}

	public static Map<String, FEATUREENUM> getMapper() {
		Map<String, FEATUREENUM> map = new HashMap<>();
		map.put("BILL_PAY_CREATE", FEATUREENUM.BILLPAYTRANSFERS);
		map.put("P2P_CREATE", FEATUREENUM.P2PTRANSFERS);
		map.put("INTRA_BANK_FUND_TRANSFER_CREATE", FEATUREENUM.INTRABANKTRANSFERS);
		map.put("DOMESTIC_WIRE_TRANSFER_CREATE", FEATUREENUM.WIRETRANSFERS);
		map.put("INTERNATIONAL_WIRE_TRANSFER_CREATE", FEATUREENUM.WIRETRANSFERS);
		map.put("INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE", FEATUREENUM.INTERBANKFUNDTRANSFERS);
		map.put("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE", FEATUREENUM.INTERNATIONALFUNDTRANSFERS);
		map.put("ACH_PAYMENT_CREATE", FEATUREENUM.ACHTRANSACTION);
		map.put("ACH_COLLECTION_CREATE", FEATUREENUM.ACHTRANSACTION);
		map.put("ACH_FILE_UPLOAD", FEATUREENUM.ACHFILE);
		map.put("TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE", FEATUREENUM.OWNACCOUNTTRANSFERS);
		map.put("BULK_PAYMENT_REQUEST_SUBMIT", FEATUREENUM.BULK_PAYMENT_REQUEST_SUBMIT);
		map.put("BULK_PAYMENT_SINGLE_SUBMIT", FEATUREENUM.BULK_PAYMENT_SINGLE_SUBMIT);
		map.put("BULK_PAYMENT_MULTIPLE_SUBMIT", FEATUREENUM.BULK_PAYMENT_MULTIPLE_SUBMIT);
		return map;
	}
}
