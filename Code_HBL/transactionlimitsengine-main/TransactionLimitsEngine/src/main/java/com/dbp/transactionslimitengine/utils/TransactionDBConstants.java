package com.dbp.transactionslimitengine.utils;

import java.util.HashMap;
import java.util.Map;

public class TransactionDBConstants {
	private TransactionDBConstants() {

	}

	public static final Map<String, String> dbservicemap = genMap();

	private static Map<String, String> genMap() {
		Map<String, String> tempmap = new HashMap<>();
		tempmap.put("P2PTRANSFERS", "{schema_name}_p2ptransfers_get");
		tempmap.put("BILLPAYTRANSFERS", "{schema_name}_billpaytransfers_get");
		tempmap.put("INTRABANKTRANSFERS", "{schema_name}_intrabanktransfers_get");
		tempmap.put("WIRETRANSFERS", "{schema_name}_wiretransfers_view_get");
		tempmap.put("INTERBANKFUNDTRANSFERS", "{schema_name}_interbankfundtransfers_get");
		tempmap.put("INTERNATIONALFUNDTRANSFERS", "{schema_name}_internationalfundtransfers_get");
		tempmap.put("ACHTRANSACTION", "{schema_name}_achtransaction_view_get");
		tempmap.put("ACHFILE", "{schema_name}_achfile_view_get");
		tempmap.put("OWNACCOUNTTRANSFERS", "{schema_name}_ownaccounttransfers_get");
		tempmap.put("BULK_PAYMENT_REQUEST_SUBMIT", "{schema_name}_bulkpaymentrecord_view_get");
		tempmap.put("BULK_PAYMENT_SINGLE_SUBMIT", "{schema_name}_bulkpaymentrecord_view_get");
		tempmap.put("BULK_PAYMENT_MULTIPLE_SUBMIT", "{schema_name}_bulkpaymentrecord_view_get");
		return tempmap;

	}

}
