package com.temenos.dbx.product.usermanagement.backenddelegate.api;

import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.temenos.dbx.product.dto.CustomerDTO;
import com.temenos.dbx.product.dto.DBXResult;

/**
 * 
 * @author DBX Team
 *
 */
public interface ProspectBackendDelegate extends BackendDelegate {

	/**
	 * 
	 * @param customerDTO
	 * @param headerMap
	 * @return
	 */
	public DBXResult update(CustomerDTO customerDTO, Map<String, Object> headerMap);

	/**
	 * 
	 * @param customerDTO
	 * @param headerMap
	 * @return
	 */
	public DBXResult create(CustomerDTO customerDTO, Map<String, Object> headerMap);

}
