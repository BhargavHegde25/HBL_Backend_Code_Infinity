package com.temenos.infinity.api.savingspot.backenddelegate.api;

import java.util.List;
import java.util.Map;

import com.dbp.core.api.BackendDelegate;
import com.temenos.infinity.api.savingspot.dto.SavingsPotCategoriesDTO;
import com.temenos.infinity.api.savingspot.dto.SavingsPotDTO;

public interface SavingsPotBackendDelegate extends BackendDelegate{
	
	/**
	 * Invokes microservice for fetching details of all savingsPot's for the given fundingAccountId of the user
	 * @param fundingAccountId - For a user, all the saving's pots for the user are fetched based on this value
	 * @return list of{@link SavingsPotDTO}
	 * @author KH1769
	 * @param headersMap 
	 */
	List<SavingsPotDTO> getAllSavingsPot(String fundingAccountId, Map<String, Object> headersMap);
	
	/**
	 * Invokes microservice for fetching the list of categories 
	 * @param type - type of category that we need for goal
	 * @return list of{@link SavingsPotCategoriesDTO}
	 * @author 19459
	 * @param headersMap 
	 */
	List<SavingsPotCategoriesDTO> getCategories(String type, Map<String, Object> headersMap);

	/**
	 * Invokes the micro service for creating a new savingsPot for the given accountId of the user with the given details
	 * @param fundingAccountHoldingId - For a user, all the saving's pots for the user are fetched based on this value
	 * @param partyId - user ID (customerId)
	 * @param productId - Id of the type of funding account
	 * @param potAccountId - The reference id of the Savings Pot in the mock DB 
	 * @param potName - Name of the pot
	 * @param potType - Type of the pot(Goal/Budget)
	 * @param savingsType - Type of the Goal[vehicle,house,etc]
	 * @param currency - currency of the transaction(INR,USD,etc)
	 * @param targetAmount - Target Amount of the Savings Pot
	 * @param targetPeriod - Period in with the target amount has to be achieved
	 * @param frequency - Frequency in which money should be deposited to the goal
	 * @param startDate - Start date of the savings pot
	 * @param endDate - By when should the Savings pot, should reach it's target amount
	 * @param periodicContribution - For a Goal, how much amount should be added to the goal based on frequency.
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	 SavingsPotDTO createSavingsPot(String fundingAccountHoldingId,String partyId,String productId,String potAccountId,String potName,String potType,String savingsType,String currency,String targetAmount,String targetPeriod,String frequency,String startDate,String endDate,String periodicContribution, Map<String, Object> headersMap);

	/**
	 * Invokes the micro service for closing the savingsPot with the given Id
	 * @param savingsPotId - The  id of the Savings Pot in the micro service  DB 
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	 SavingsPotDTO closeSavingsPot(String savingsPotId, Map<String, Object> headersMap);
	
	/**
	 * Invokes the micro service for funding and withdrawing amount from the savingsPot with the given Id
	 * @param savingsPotId - The reference id of the Savings Pot in the mock DB 
	 * @param amount - The amount involved in fund/withdraw to the savings pot
	 * @param isCreditDebit - If Value is Credit, it is considered as Fund to savings pot and if value is Debit it is considered as Withdraw from the savings pot
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	 SavingsPotDTO updateSavingsPotBalance(String savingsPotId,String amount, String isCreditDebit, Map<String, Object> headersMap);

	/**
	 * Invokes the micro service for updating savingsPot with the given Id
	 * @param updateSavingsPotMap - It Consists of following fields
	 * savingsPotId - The reference id of the Savings Pot in the mock DB 
	 * The below mentioned parameters are the one's which are optional, meaning they will be sent only if that value for the pot has to be changed.
	 * “potName" - Name of the Pot
	 * “startDate” - Start Date of the Pot
	 * “endDate”  -  By when should the Savings pot, should reach it's target amount
	 * “savingType” - Type of the Goal
	 * “frequency” - Frequency in which money should be deposited to the goal
	 * “targetAmount” - Target Amount of the Savings Pot
	 * “targetPeriod” - Period in which target amount has to be achieved(Mentioned in months as "12M","24M",etc)
	 * “periodicContribution” - For a Goal, how much amount should be added to the goal based on frequency.
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 * @param headersMap 
	 */
	 SavingsPotDTO updateSavingsPot(Map<String, Object> updateSavingsPotMap, Map<String, Object> headersMap);

}
