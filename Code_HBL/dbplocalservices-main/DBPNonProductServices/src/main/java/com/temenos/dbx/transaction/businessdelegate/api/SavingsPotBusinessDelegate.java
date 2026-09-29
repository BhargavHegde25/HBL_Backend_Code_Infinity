package com.temenos.dbx.transaction.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.transaction.dto.SavingsPotDTO;

public interface SavingsPotBusinessDelegate extends BusinessDelegate{
	
	/**
	 * Calls the mock service for creating a new savingsPot for the given accountId of the user with the given details
	 * inputParams consists of follwoing keys
	 * @param accountId - For a user, all the saving's pots for the user are fetched based on this value
	 * @param potName - Name of the pot
	 * @param potType - Type of the pot(Goal/Budget)
	 * @param savingType - Type of the Goal[vehicle,house,etc]
	 * @param targetAmount - Target Amount of the Savings Pot
	 * @param frequency - Frequency in which money should be deposited to the goal
	 * @param startDate - Start date of the savings pot
	 * @param endDate - By when should the Savings pot, should reach it's target amount
	 * @param periodicContribution - For a Goal, how much amount should be added to the goal based on frequency.
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 */
	public SavingsPotDTO createSavingsPot(Map<String, Object> inputParams);

	/**
	 * Calls the mock service for closing the savingsPot with the given Id
	 * @param potContractId - The reference id of the Savings Pot in the mock DB 
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 */
	public SavingsPotDTO closeSavingsPot(String potContractId);
	
	/**
	 * Calls the mock service for funding and withdrawing amount from the savingsPot with the given Id
	 * @param potContractId - The reference id of the Savings Pot in the mock DB 
	 * @param amount - The amount involved in fund/withdraw to the savings pot
	 * @param isCreditDebit - If Value is Credit, it is considered as Fund to savings pot and if value is Debit it is considered as Withdraw from the savings pot
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 */
	public SavingsPotDTO updateSavingsPotBalance(String potContractId,String amount, String isCreditDebit);

	/**
	 * Calls the mock service for updating savingsPot with the given Id
	 * @param updateSavingsPotMap - It Consists of following fields
	 * potContractId - The reference id of the Savings Pot in the mock DB 
	 * The below mentioned parameters are the one's which are optional, meaning they will be sent only if that value for the pot has to be changed.
	 * “potName" - Name of the Pot
	 * “startDate” - Start Date of the Pot
	 * “endDate”  -  By when should the Savings pot, should reach it's target amount
	 * “savingType” - Type of the Goal
	 * “frequency” - Frequency in which money should be deposited to the goal
	 * “targetAmount” - Target Amount of the Savings Pot
	 * “scheduledPaymentAmount” - For a Goal, how much amount should be added to the goal based on frequency.
	 * @return {@link SavingsPotDTO}
	 * @author KH2347
	 */
	public SavingsPotDTO updateSavingsPot(Map<String, Object> updateSavingsPotMap);

}
