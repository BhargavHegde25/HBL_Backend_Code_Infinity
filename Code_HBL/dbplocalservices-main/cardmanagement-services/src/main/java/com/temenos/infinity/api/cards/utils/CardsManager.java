package com.temenos.infinity.api.cards.utils;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CardsManager {

    public static final String CARDS = "CARDS";
    public static final String CARDS_WITH_TYPE = "CARDS_WITH_TYPE";
    public static final String CARD_TRANSACTIONS = "CARD_TRANSACTIONS";
    private FabricRequestManager fabricRequestManager = null;
    private String customerId = null;
    
    public CardsManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public SessionMap getCardsFromSession(String customerId) {
        Object cardsMap = MemoryManager.retrieve(this.fabricRequestManager, CardsManager.CARDS + this.customerId);
        if (null == cardsMap) {
            CardHelper.reloadCardsIntoSession(fabricRequestManager);
        }
        cardsMap = MemoryManager.retrieve(this.fabricRequestManager, CardsManager.CARDS + this.customerId);
        return (SessionMap) cardsMap;
    }
    
    public SessionMap getCardsFromSessionWithType(String customerId) {
        Object cardsMap = MemoryManager.retrieve(this.fabricRequestManager, CardsManager.CARDS_WITH_TYPE + this.customerId);
        if (null == cardsMap) {
            CardHelper.reloadCardsIntoSession(fabricRequestManager);
        }
        cardsMap = MemoryManager.retrieve(this.fabricRequestManager, CardsManager.CARDS_WITH_TYPE + this.customerId);
        return (SessionMap) cardsMap;
    }

    public void saveCardsIntoSession(SessionMap cardsMap) {
        if (null != cardsMap) {
            MemoryManager.save(fabricRequestManager, CardsManager.CARDS + this.customerId, cardsMap);
        }
    }
    
    public void saveCardsIntoSessionWithType(SessionMap cardsMap) {
        if (null != cardsMap) {
            MemoryManager.save(fabricRequestManager, CardsManager.CARDS_WITH_TYPE + this.customerId, cardsMap);
        }
    }
   
    
    public boolean validateCard(String customerId, String cardId) {
        SessionMap cardsMap = getCardsFromSession(customerId);
        if (null == cardsMap) {
            return false;
        }
        return cardsMap.hasKey(cardId);
    }
    
    //TransactionManager methods
    public void saveCardTransactionsIntoSession(SessionMap transactionsMap) {
        if (null != transactionsMap) {
            MemoryManager.save(this.fabricRequestManager, CardsManager.CARD_TRANSACTIONS + this.customerId,
                    transactionsMap);
        }
    }

}