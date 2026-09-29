package com.temenos.dbx.product.organization.resource.api;

import com.dbp.core.api.Resource;
import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author Infinity DBX
 *
 */
public interface OrganizationResource extends Resource {

	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param dcRequest
	 * @param dcResponse
	 * @return Result
	 * @throws ApplicationException
	 */
	public Result getListOfOrganisationsByStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;

	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param dcRequest
	 * @param dcResponse
	 * @return
	 * @throws ApplicationException
	 */
	public Result updateOrganizationStatus(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;

	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param dcRequest
	 * @param dcResponse
	 * @return
	 * @throws ApplicationException
	 */
	public Result createOrganization(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;
	
	/**
	 * 
	 * @param methodID
	 * @param inputArray
	 * @param dcRequest
	 * @param dcResponse
	 * @return org Business signatory types
	 * @throws ApplicationException
	 */
	public Result getOrganizationBusinessSignatoryTypes(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException;

}