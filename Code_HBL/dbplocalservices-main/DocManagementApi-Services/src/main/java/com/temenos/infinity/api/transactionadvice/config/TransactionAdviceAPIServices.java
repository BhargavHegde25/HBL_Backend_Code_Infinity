package com.temenos.infinity.api.transactionadvice.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the
 * Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum TransactionAdviceAPIServices implements InfinityServices {

	TRANSACTIONADVICEJSON_LOGIN("TransactionAdviceJSONServices", "login"), 
	TRANSACTIONADVICEJSON_SEARCH("TransactionAdviceJSONServices",
					"getYearlyFilesDetails"), 
	TRANSACTIONADVICEJSON_DOWNLOAD("TransactionAdviceJSONServices", "filedownload"),
	LOAN_SCHEDULED_TRANSACTIONS_MOCK("mockServices", "getDownloadStatementsLoan");;

	private String serviceName, operationName;

	/**
	 * @param serviceName
	 * @param operationName
	 */
	private TransactionAdviceAPIServices(String serviceName, String operationName) {
		this.serviceName = serviceName;
		this.operationName = operationName;
	}

	@Override
	public String getServiceName() {
		return this.serviceName;
	}

	@Override
	public String getOperationName() {
		return this.operationName;
	}

}
