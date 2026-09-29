package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BulkWireDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireTemplateLineItemDTO;
import com.temenos.dbx.product.transactionservices.dto.PayeeDTO;

/**
 * Handles all the operations on Bulk Wires
 * extends {@link BusinessDelegate}
 */
public interface BulkWiresBusinessDelegate extends BusinessDelegate{

	/**
     * Invokes DBP RB Local Stored procedure for fetching the Bulk Wire  files and templates for a Retail and Small Business user at the Core and also supports searching, sorting and pagination functionality
     * @param createdBy - The id of the user in the session, based on which bulkwires are fetched.
     * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
     * @param pageOffset - No:of records to skip in the queried result 
     * @param pageSize - No:of records to take in the queried result for Final result  
     * @param searchString - consists of text on which the search has to be performed.
     * @param bulkWireCategoryFilter - the response is filtered to get only files/only templates based on this value of File/Template.
     * @param isDomesticFilePermitted - whether user has access to view domestic wire transfer bulk files.
     * @param isInternationalFilePermitted - whether user has access to view international wire transfer bulk files.
     * @param isDomesticTemplatePermitted - whether user has access to view domestic wire transfer bulk templates.
     * @param isInternationalTemplatePermitted - whether user has access to view international wire transfer bulk templates.
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2347
     */
    List<BulkWireDTO> getBulkWiresForUser(String createdBy, String sortByParam, String sortOrder, Object pageOffset, Object pageSize, String searchString,String bulkWireCategoryFilter, Boolean isDomesticFilePermitted,Boolean isInternationalFilePermitted,Boolean isDomesticTemplatePermitted,Boolean isInternationalTemplatePermitted);
    /**
     * Create BulkWireTemplate and BulkWireTemplateLineItems
     * @param bulkWireTemplateString - Contains BulkWireTemplate fields in CSV format
     * @param bulkWireTemplateLineItemsString - Contains multiple BulkWireTemplateLineItems records in CSV format
     * @return {@link Object}
     */
    Object createBulkWireTemplate(String bulkWireTemplateString, String bulkWireTemplateLineItemsString);

    /**
     * Update BulkWireTemplate and BulkWireTemplateLineItems
     * @param bulkWireTemplateString - Contains BulkWireTemplate fields in CSV format
     * @param bulkWireTemplateLineItemsString_update - Contains multiple BulkWireTemplateLineItems records in CSV format
     * @param bulkWireTemplateLineItemsString_create - Contains multiple BulkWireTemplateLineItems records in CSV format
     * @return {@link Object}
     */
    Object updateBulkWireTemplate(String bulkWireTemplateString, String bulkWireTemplateLineItemsString_update, String bulkWireTemplateLineItemsString_create);
	
	/**
	 * Invokes DBP RB Local service for fetching the Bulk Wire Transfer template line items for a template
	 * @param bulkWireFileID - For file, the bulk wire file line items are fetched based on this  value
	 * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
	 * @param searchString - The string to be searched in the records when wire template line items are fetched using the bulkWireTemplateID
	 * @param groupBy - The name of the column in the line items table, based on which the response has to be grouped and returned.(EX : International/Domestic - bulkWireTransferType || EXISTINGRECIPIENT/MANUALLYADDED/EXTRACTEDFROMFILE - templateRecipientCategory, etc) 
	 * @return list of{@link BulkWireTemplateLineItemDTO}
	 * @author KH2347
	 */
	List<BulkWireTemplateLineItemDTO> getBulkWireTemplateLineItems(String bulkWireTemplateID,String sortByParam,String sortOrder,String searchString,String groupBy);
	
	  /**
     * Returns whether the user has access to the BulkWire  Template.
     * @param templateId - Id of the template, whose access to the user has to be checked
     * @return {@Boolean}
     * @author KH2347
     */
    Boolean isTemplateAccessibleByUser(String templateId, Map<String,Object> customerMap);
	

	/**
     * Delete BulkWireTemplate and its LineItems
     * @param bulkWireTemplateId
     * @return Boolean
     */
	Boolean deleteBulkWireTemplate(String bulkWireTemplateId);

	/**
     * Delete BulkWireTemplateLineItems
     * @param bulkWireTemplateId
     * @param bulkWireTemplateLineItemIDs - contains csv format of bulkWireTemplateLineItemIDs
     * @return {@link BulkWireDTO}
     */
	BulkWireDTO deleteBulkWireTemplateRecipient(String bulkWireTemplateId, String bulkWireTemplateLineItemIDs);
	
	/**
     * Update BulkWireTemplate table.
     * @param BWTemplateDetails
     * @return {@link BulkWireDTO}
     */
	BulkWireDTO updateBulkWireTemplate(Map<String, Object> BWTemplateDetails);
	
	/**
     * Get Unselected Payees For BulkWire Template
     * @param bulkWireTemplateId
     * @param sortByParam
     * @param sortOrder
     * @param searchString
     * @param user_id
     * @param isDomesticPermitted
     * @param isInternationalPermitted
     * @return List<PayeeDTO>
     */
	List<PayeeDTO> getUnselectedPayeesForBWTemplate(String bulkWireTemplateId, String sortByParam,
			String sortOrder, String searchString, String user_id, int isDomesticPermitted, int isInternationalPermitted);
	
	/**
     * Get the count of domestic and international transactions in the template, which is identified by either bulkWireTemplateId or bulkwiretemplatelineitemIDs given in the input
     * @param bulkWireTemplateId
     * @param bulkwiretemplatelineitemIDs
     * @return JsonObject which consists of keys noOfDomesticTransactions,noOfInternationalTransactions
     */
	JSONObject getBWTemplateDomesticInternationalCount(String bulkWireTemplateId, String bulkwiretemplatelineitemIDs);
	
	/**
     * Update BulkWireTemplate Recipient Count
     * @param userId
     * @param companyId
     * @param payeeId
     * @return Boolean
     */
	Boolean UpdateBulkWireTemplateRecipientCount(String userId, String companyId, String payeeId);
	
	/**
     * fetches transaction entry from wiretransfers table for the given wireTemplateExecution_id
     * @param wireTemplateExecution_id - Execution_ID on which the table is queried
     * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
     * @param searchString - consists of text on which the search has to be performed.
     * @param isDomesticPermitted
     * @param isInternationalPermitted
     * @return Object
     */
	public Object fetchTransactionsByWireTemplateExecutionId(String wireTemplateExecution_id, String sortByParam, String sortOrder, String searchString, String statusFilter, int isDomesticPermitted, int isInternationalPermitted);	

	/**
     * fetches transaction entry from wiretransfers table for the given wireFileExecution_id
     * @param wireFileExecution_id - Execution_ID on which the table is queried
     * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
     * @param searchString - consists of text on which the search has to be performed.
     * @param isDomesticPermitted
     * @param isInternationalPermitted
     * @return Object
     */
	public Object fetchTransactionsByWireExecutionId(String wireFileExecutionId, String sortByParam, String sortOrder, String searchString, String statusFilter, int isDomesticPermitted, int isInternationalPermitted);
	
}
