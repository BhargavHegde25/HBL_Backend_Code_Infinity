package com.kony.dbp.cardservices.businessdelegate.api;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.kony.dbp.cardservices.dto.CardStatementsDTO;
import com.kony.dbp.cardservices.dto.CardTransactionsDTO;
import com.kony.dbp.cardservices.dto.CardsDTO;
import com.kony.dbp.cardservices.dto.CardsProductsDTO;

import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

/**
 * 
 * @author KH2394
 * @version 1.0
 * Interface for CardServicesBusinessDelegate extends {@link BusinessDelegate}
 *
 */
public interface CardServicesBusinessDelegate extends BusinessDelegate {
	/**
	 *  method to get the Cards 
	 *  return list of {@link CardsDTO}
	 * @param userId 
	 */

	public List<CardStatementsDTO> getStatements(String card_id, String month, String userId) throws ApplicationException;
	
	public Result activateCards(Map<String, String> inputParams);
	public Result applyForDebitCard(Map<String, Object> inputParams) throws Exception;
	
	public List<CardTransactionsDTO> fetchCardTransactions(Map<String, Object> inputParams);
	public List<CardsProductsDTO> getCardProducts(Map<String, Object> inputParams)throws ApplicationException;
	public Result updateCardTransaction(Map<String, String> inputParams);

	public Result applyForCreditCard(HashMap<String, Object> input);
	

}
