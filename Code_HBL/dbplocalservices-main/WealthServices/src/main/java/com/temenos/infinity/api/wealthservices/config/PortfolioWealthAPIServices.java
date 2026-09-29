package com.temenos.infinity.api.wealthservices.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the
 * Arrangements Experience API
 * 
 * @author 19459
 *
 */
public enum PortfolioWealthAPIServices implements InfinityServices {
	
	WEALTH_GETREFINITIVTOKEN("WealthPortfolioRefServices", "getToken"),
	SERVICE_BACKEND_CERTIFICATE("dbpRbLocalServicesdb", "dbxdb_backendcertificate_get");
	

	private String serviceName, operationName;

	/**
	 * @param serviceName
	 * @param operationName
	 */
	private PortfolioWealthAPIServices(String serviceName, String operationName) {
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
