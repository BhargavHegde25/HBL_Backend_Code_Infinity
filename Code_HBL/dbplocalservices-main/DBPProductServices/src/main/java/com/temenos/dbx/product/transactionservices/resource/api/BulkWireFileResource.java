package com.temenos.dbx.product.transactionservices.resource.api;

import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public interface BulkWireFileResource extends Resource {

	/**
	 * (INFO)the bulk wire file line items are queried from table based on "bulkWireFileID" respectively
	 * Fetches the file line items by calling business delegate method to fetch wire file line items
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2301
	 */
	Result getBulkWireFileLineItems(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	/**
	 * (INFO)the bulk wire file format types are queried.
	 * Fetches the file format type details by calling customer business delegate
	 * and accordingly create parameters and call business delegate method to fetch wire file format type details
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2301
	 */
	Result getBulkWireFileFormatTypes(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	  /**
     * (INFO)Based on whether the user type is "Retail" or "Small Business", 
     * (INFO)the bulk wire sample file is queried from the table 
     * Fetches the user attributes by calling customer business delegate to identify user type
     * and accordingly create parameters and call business delegate method to fetch sample bulk wire file
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     * @author KH2384
     */
	Result initiateDownloadBulkWireSampleFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
	 /**
     * (INFO)Based on whether the user type is "Retail" or "Small Business", 
     * (INFO)the bulk wire file is queried for a given "file_id"  from the table based on "user_name" or "organisation_id" of the user respectively
     * Fetches the user attributes by calling customer business delegate to identify user type
     * and accordingly create parameters and call business delegate method to fetch corresponding bulk wire file
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     * @author KH2384
     */
	Result downloadFileBulkWire(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);

	   /**
     * (INFO)Based on whether the user type is "Retail" or "Small Business", 
     * (INFO)the bulk wire files are queried from table based on "user_name" or "organisation_id" of the user repectively
     * Fetches the user attributes by calling customer business delegate to identify user type
     * and accordingly create parameters and call business delegate method to fetch bulk wire files
     * @param methodID
     * @param inputArray
     * @param request
     * @param response
     * @return {@link Result}
     * @author KH2347
     */
    Result getBulkWireFilesForUser(String methodID, Object[] inputArray, DataControllerRequest request,
        DataControllerResponse response);
    
    
    
    /**
    * uploading a bulk wire file of the supported format with valid line items to the table 
    * @param methodID
	* @param inputArray
	* @param request
	* @param response
	* @return {@link Result}
	* @author KH2144
	*/ 
	Result uploadBWFile(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance);

	/**
	 * get method to download BulkWire file
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2624
	 */
	Result downloadFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) ;

	/**
	 * get method to download BulkSampleWire file
	 * @param methodID
	 * @param inputArray
	 * @param request
	 * @param response
	 * @return {@link Result}
	 * @author KH2665
	 */
	Result downloadSampleFile(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response);
}