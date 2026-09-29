package com.temenos.infinity.api.transactionadviceapi.businessdelegate.impl;

import java.util.Base64;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.transactionadviceapi.backenddelegate.api.TransactionAdviceAPIBackendDelegate;
import com.temenos.infinity.api.transactionadviceapi.businessdelegate.api.TransactionAdviceBusinessDelegate;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormCookie;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormDownload;

public class TransactionAdviceBusinessDelegateImpl implements TransactionAdviceBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public AutoFormCookie login(String auth_token) {

		TransactionAdviceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(TransactionAdviceAPIBackendDelegate.class);
		AutoFormCookie output = backendDelegate.login(auth_token);
		return output;

	}

	@Override
	public String download(String documentId, String reference, String xsrf, String jsessionid,String auth_token) {
		TransactionAdviceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				
		        .getBackendDelegate(TransactionAdviceAPIBackendDelegate.class);
		byte[] output = backendDelegate.download(documentId, reference, xsrf, jsessionid,auth_token);
		String encoded = Base64.getEncoder().encodeToString(output);

		return encoded;
	}

	@Override
	public AutoFormDownload search(String customerId, String accountId, String transactionRef, String mediaType,
			String transactionType, String page, String xsrf, String jsessionid,String auth_token) {
		TransactionAdviceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
				.getBackendDelegate(TransactionAdviceAPIBackendDelegate.class);
		String cuk = new String();
		if (page.isEmpty() || page == null)
			cuk = customerId + "_" + accountId + "_" + transactionRef + "_p0_" + mediaType + "_" + transactionType;
		else
			cuk = customerId + "_" + accountId + "_" + transactionRef + "_p" + page + "_" + mediaType + "_"
					+ transactionType;
		AutoFormDownload output = new AutoFormDownload();
		try {
			output = backendDelegate.search(cuk, xsrf, jsessionid,auth_token);
		} catch (ApplicationException e) {
			alert.prepareError(e.toString()).log();
		}
		return output;
	}
}