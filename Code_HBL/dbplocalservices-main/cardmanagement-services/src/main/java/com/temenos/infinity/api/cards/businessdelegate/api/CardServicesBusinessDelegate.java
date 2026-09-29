package com.temenos.infinity.api.cards.businessdelegate.api;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.infinity.api.cards.dto.CardStatementsDTO;
import com.temenos.infinity.api.cards.dto.CardTransactionsDTO;
import com.temenos.infinity.api.cards.dto.CardsDTO;
import com.temenos.infinity.api.cards.dto.CardsProductsDTO;

import com.kony.dbp.exception.ApplicationException;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
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
	
	public Result activateCards(Map<String, String> inputParams, Map<String, Object> headersMap);
	public Result applyForDebitCard(Map<String, Object> inputParams, Map<String, Object> headersMap) throws Exception;
	
	public List<CardTransactionsDTO> fetchCardTransactions(Map<String, Object> inputParams);
	public List<CardsProductsDTO> getCardProducts(Map<String, Object> inputParams)throws ApplicationException;
	public Result updateCardTransaction(Map<String, String> inputParams);

	public Result applyForCreditCard(Map<String, Object> input, Map<String, Object> headersMap);

	public Result updateCard(Map<String, Object> headerMap, Map<String, Object> requestParams, String legalEntityId);
	

}
