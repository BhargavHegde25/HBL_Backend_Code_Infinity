package com.temenos.infinity.api.visaintegrationapi.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the
 * Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum VisaIntegrationApiAPIServices implements InfinityServices {

	GETCARDDETAILS("dbpRbLocalServicesdb", "dbxdb_card_get");

	private String serviceName, operationName;

	/**
	 * @param serviceName
	 * @param operationName
	 */
	private VisaIntegrationApiAPIServices(String serviceName, String operationName) {
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
