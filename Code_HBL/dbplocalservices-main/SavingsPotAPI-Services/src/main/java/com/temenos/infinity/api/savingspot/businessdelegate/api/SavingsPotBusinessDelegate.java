package com.temenos.infinity.api.savingspot.businessdelegate.api;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.savingspot.dto.SavingsPotCategoriesDTO;
import com.temenos.infinity.api.savingspot.dto.SavingsPotDTO;

public interface SavingsPotBusinessDelegate extends BusinessDelegate{
	
	/**
	 * Calls the microservice for fetching details of all savingsPot's for the given fundingAccountId of the user
	 * @param fundingAccountId - For a user, all the saving's pots for the user are fetched based on this value
	 * @return list of{@link SavingsPotDTO}
	 * @author KH1769
	 * @param headersMap 
	 */
	List<SavingsPotDTO> getAllSavingsPot(String fundingAccountId, Map<String, Object> headersMap);

	/**
	 * Calls the  microservice for fetching the list of categories 
	 * @param type - type of category that we need for goal
	 * @return list of{@link SavingsPotCategoriesDTO}
	 * @author 19459
	 * @param headersMap 
	 */
	List<SavingsPotCategoriesDTO> getCategories(String type, Map<String, Object> headersMap);
	
	/**
	 * Calls the micro service for create savingsPot with the given Id
	 * @param All params - It Consists of  fields needed to create savingsPot
     * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	SavingsPotDTO createSavingsPot(String fundingAccountHoldingId,String partyId,String productId,String potAccountId,String potName,String potType,String savingsType,String currency,String targetAmount,String targetPeriod,String frequency,String startDate,String endDate,String periodicContribution, Map<String, Object> headersMap);

	/**
	 * Calls the micro service for closing the savingsPot with the given Id
	 * @param savingsPotId - The  id of the Savings Pot in the micro service  DB 
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	 SavingsPotDTO closeSavingsPot(String savingsPotId, Map<String, Object> headersMap);
	
	/**
	 * Calls the micro service for funding and withdrawing amount from the savingsPot with the given Id
	 * @param savingsPotId - The reference id of the Savings Pot in the mock DB 
	 * @param amount - The amount involved in fund/withdraw to the savings pot
	 * @param isCreditDebit - If Value is Credit, it is considered as Fund to savings pot and if value is Debit it is considered as Withdraw from the savings pot
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	 SavingsPotDTO updateSavingsPotBalance(String savingsPotId,String amount, String isCreditDebit, Map<String, Object> headersMap);

	/**
	 * Calls the micro service for updating savingsPot with the given Id
	 * @param updateSavingsPotMap - It Consists of fields which needs to be updated
     * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	 SavingsPotDTO updateSavingsPot(Map<String, Object> updateSavingsPotMap, Map<String, Object> headersMap);

}
