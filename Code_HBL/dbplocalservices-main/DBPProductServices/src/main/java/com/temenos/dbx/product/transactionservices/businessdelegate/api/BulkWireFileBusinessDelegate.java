package com.temenos.dbx.product.transactionservices.businessdelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.transactionservices.dto.AccountTypesDTO;
import com.temenos.dbx.product.transactionservices.dto.AccountsDTO;
import com.temenos.dbx.product.transactionservices.dto.ApplicationDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileFormatTypeDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireFileLineItemsDTO;
import com.temenos.dbx.product.transactionservices.dto.BulkWireSampleFileDTO;
import com.temenos.dbx.product.transactionservices.dto.CountryListDTO;
import com.temenos.dbx.product.transactionservices.dto.CurrencyListDTO;
import com.temenos.dbx.product.transactionservices.dto.LineItemDTO;

/**
 * Handles all the operations on Bulk Wire transactions
 * extends {@link BusinessDelegate}
 */
public interface BulkWireFileBusinessDelegate  extends BusinessDelegate {

	/**
	 * Invokes DBP RB Local service for fetching the Bulk Wire Transfer file line items for a file
	 * @param bulkWireFileID - For file, the bulk wire file line items are fetched based on this  value
	 * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
	 * @param searchString - The string to be searched in the records when wire file line items are fetched using the bulkWireFileID
	 * @return list of{@link BulkWireFileLineItemsDTO}
	 * @author KH2301
	 */
	List<BulkWireFileLineItemsDTO> getBulkWireFileLineItems(String bulkWireFileID,String sortByParam,String sortOrder,String searchString);

	/**
	 * Invokes DBP RB Local service for fetching the Bulk Wire Transfer file format type details
	 * @param no inputs are required 
	 * @return list of{@link BulkWireFileFormatTypeDTO}
	 * @author KH2301
	 */
	List<BulkWireFileFormatTypeDTO> getBulkWireFileFormatTypes();
	 
    /**
     * Invokes DBP RB Local service for fetching the Bulk Wire sample file for a Retail and Small Business user at the Core
     * @param fileCategory - Category of File that need to be returned
     * @return {@link BulkWireSampleFileDTO}
     * @author KH2384
     */
    BulkWireSampleFileDTO downloadSampleFileBulkWire(String fileCategory);
   	 
    /**
     * Invokes DBP RB Local service for fetching the Bulk Wire files for a Retail and Small Business user at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2384
     */
	BulkWireFileDTO downloadFileBulkWire(String fileID);
	

    /**
     * Invokes DBP RB Local Stored procedure for fetching the Bulk Wire Transactions files for a Retail and Small Business user at the Core and also supports search functionality
     * @param createdBy - For retails user, the bulk wire files are fetched based on this  value
     * @param sortByParam - The column in the table to be considered for sorting the result
     * @param sortOrder - The order in which the sorting has to happen
     * @param pageOffset - No:of records to skip in the queried result 
     * @param pageSize - No:of records to take in the queried result for Final result  
     * @param searchString - consists of text on which the search has to be performed.
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2347
     */
    List<BulkWireFileDTO> getBulkWireFilesForUser(String createdBy, String sortByParam, String sortOrder, Object pageOffset, Object pageSize, String searchString);
    
    /**
     * Invokes DBP RB Local service for uploading the Bulk Wire file for a Retail and Small Business user at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2144
     */
	List<BulkWireFileDTO> uploadBWFile(BulkWireFileDTO bwfileDTO);
	
	/**
     * Invokes DBP RB Local service for creating the Bulk Wire file line items for a Retail and Small Business user at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2144
     */
	List<BulkWireFileLineItemsDTO> createLineItems(LineItemDTO lineItemsDTO);
	
	/**
     * Invokes DBP RB Local service for fetching the supported currencies at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2144
     */
	List<CurrencyListDTO> getCurrencyList();
	
	/**
     * Invokes DBP RB Local service for fetching the supported countries at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2144
     */
	List<CountryListDTO> getCountryList();
	
	/**
     * Invokes DBP RB Local service for updating the Bulk Wire file delete status for a Retail and Small Business user at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2144
     */
	List<BulkWireFileDTO> deleteBWFile(String fileId);
	
	/**
     * Invokes DBP RB Local service for fetching the accounts at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2144 m
     */
	List<AccountsDTO> getAccounts();
	
	/**
     * Invokes DBP RB Local service for fetching the account types at the Core
     * @return {@link BulkWireTransactionsDTO}
     * @author KH2144
     */
	List<AccountTypesDTO> getAccountTypes();

    /**
     * Returns whether the user has access to the BulkWire  file.
     * @param FileId - Id of the file, whose access to the user has to be checked
     * @return {@Boolean}
     * @author KH2347
     */
    Boolean isFileAccessibleByUser(String fileId, Map<String,Object> customerMap);
    
    /**
     * Invokes DBP RB Local service for fetching the application properties
     * @return {@link ApplicationDTO}
     * @author KH2144
     */
	List<ApplicationDTO> getApplicationProp();

	/**
     * Get the count of domestic and international transactions in the file, which is identified by bulkWireFileId given in the input
     * @param bulkWirefileID
     * @return JsonObject which consists of keys noOfDomesticTransactions,noOfInternationalTransactions
     */
	JSONObject getBWFileDomesticInternationalCount(String bulkWirefileID);
	
    
}

