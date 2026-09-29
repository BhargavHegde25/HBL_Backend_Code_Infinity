package com.kony.cards.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * @author Emilia Ivanov
 *
 */
public enum ManageCardsApiAPIServices implements InfinityServices {

	GETCARDDETAILS("dbpRbLocalServicesdb", "dbxdb_card_get");

	private String serviceName, operationName;

	/**
	 * @param serviceName
	 * @param operationName
	 */
	private ManageCardsApiAPIServices(String serviceName, String operationName) {
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
