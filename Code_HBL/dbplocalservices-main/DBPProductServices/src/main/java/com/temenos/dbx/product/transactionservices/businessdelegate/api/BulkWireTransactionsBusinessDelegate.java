package com.temenos.dbx.product.transactionservices.businessdelegate.api;


import java.util.Map;
import java.util.List;
import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileTransactionsDetailDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireTemplateTransactionsDetailDTO;

/**
 * Handles all the operations on Bulk Wire transactions
 * extends {@link BusinessDelegate}
 */

public interface BulkWireTransactionsBusinessDelegate extends BusinessDelegate {

    /**
     * Invokes Integration Services for creating Bulk Wire Transaction Details
     * @param requestParameters
     * @return {@link BulkWireFileTransactionsDetailDTO}
     * @author KH1769
     */
    BulkWireFileTransactionsDetailDTO createBulkWireTransactionDetails(Map<String, Object> requestParameters);
    
    /**
     * Invokes Integration Services for creating Bulk Wire Transaction Details
     * @param requestParameters
     * @return {@link BulkWireFileTransactionsDetailDTO}
     * @author KH1769
     */
    BulkWireFileDTO updateBulkWireFiles(Map<String, Object> requestParameters);
    
    
    /**
     * Invokes DBP RB Local Stored procedure for fetching the details of all the  transactions done on a Bulk Wire File
     * @param bulkWirefileID - Details of the transactions performed on the file with this file ID are fetched 
     * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
     * @param pageOffset - No:of records to skip in the queried result 
     * @param pageSize - No:of records to take in the queried result for Final result  
     * @param searchString - consists of text on which the search has to be performed.
     * @return {@link bulkWireFileTransactionDetailsDTO}
     * @author KH2264
     */
    List<BulkWireFileTransactionsDetailDTO> getBulkWireFileTransactionsDetail(String bulkWirefileID,String sortByParam,String sortOrder, Object pageOffset,
    		Object pageSize,String searchString);
    
    /**
     * Invokes DBP RB Service for inserting into bulkwiretemplatetransactdetails table as creating Bulk Wire Transaction Details for template.
     * @param requestParameters
     * @return {@link BulkWireTemplateTransactionsDetailDTO}
     * @author KH2347
     */
	BulkWireTemplateTransactionsDetailDTO createBulkWireTemplateTransactionDetails(
			Map<String, Object> bWTransactonDetails);

	/**
     * Invokes DBP RB Local Stored procedure for fetching the details of all the  transactions done on a Bulk Wire Template
     * @param bulkWireTemplateID - Details of the transactions performed on the template with this template ID are fetched 
     * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
     * @param pageOffset - No:of records to skip in the queried result 
     * @param pageSize - No:of records to take in the queried result for Final result  
     * @param searchString - consists of text on which the search has to be performed.
     * @return {@link bulkWireTemplateTransactionDetailsDTO}
     * @author KH2347
     */
	List<BulkWireTemplateTransactionsDetailDTO> getBulkWireTemplateTransactionDetail(String bulkWireTemplateID,
			String sortBy, String sortOrder, Object pageOffset, Object pageSize, String searchString);
}