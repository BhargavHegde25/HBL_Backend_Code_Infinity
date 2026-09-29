package com.temenos.dbx.product.achservices.businessdelegate.api;

import java.util.List;

import org.json.JSONArray;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.achservices.dto.ACHTaxSubTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHTaxTypeDTO;
import com.temenos.dbx.product.achservices.dto.ACHTransactionTypesDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRequestTypeDTO;

/**
 * @author KH2395
 * @version 1.0 
 * Interface for ACHCommonsBusinessDelegate extends {@link BusinessDelegate}
 */

public interface ACHCommonsBusinessDelegate extends BusinessDelegate {
	
	/**
	 * method to fetch template request type from DB
	 * @param transactionType_id  
	 * @return BBTemplateRequestTypeDTO
	 */
	
	public List<BBTemplateRequestTypeDTO> fetchTemplateRequestTypes(String transactionType_id);
	
	/**
	 * method to fetch all the template request type
	 * @return
	 */
	public List<BBTemplateRequestTypeDTO> fetchTemplateRequestTypes();

	/**
	 * method to fetch transaction types
	 * @return List<ACHTransactionTypesDTO>
	 */
	public List<ACHTransactionTypesDTO> fetchBBTransactionTypes();
	
	/**
	 * method to get tax types
	 * @return List<ACHTaxTypeDTO>
	 */
	public List<ACHTaxTypeDTO> fetchTaxType();

	/**
	 * method to get tax sub types
	 * @return List<ACHTaxSubTypeDTO>
	 */
	public List<ACHTaxSubTypeDTO> fetchACHTaxSubType(String taxType);
	
	/**
	 * method to fetch the transcation type for a given ID
	 * @param transactionTypeID
	 * @return
	 */
	public String getTransactionTypeById(int transactionTypeID);

	/**
	 * Method used to calculate total amount while creating or editing template
	 * @param records
	 * @param doesSubRecordExists
	 * @return
	 */
	public Double getTotalAmountFromRecords(JSONArray records, boolean doesSubRecordExists);
	
	/**
	 * Method used to validate total amount while creating or editing template
	 * @param doesSubRecordExists
	 * @param records
	 * @param maxAmount
	 * @return
	 */
	public BBTemplateDTO validateTotalAmount(boolean doesSubRecordExists, String records, double maxAmount);
}
