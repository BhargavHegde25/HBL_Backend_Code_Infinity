package com.kony.memorymgmt;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.model.PayPersonHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class PayPersonManager {

    public static final String PAY_PERSON = "PAY_PERSON";
    private FabricRequestManager fabricRequestManager = null;
    private String customerId = null;

    public PayPersonManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public SessionMap getPayPersonsFromSession(String customerId) {
        SessionMap payPersonMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                PayPersonManager.PAY_PERSON + this.customerId);
        if (null == payPersonMap || payPersonMap.isEmpty()) {
            payPersonMap = PayPersonHelper.reloadPayPersonsIntoSession(fabricRequestManager);
        }
        return payPersonMap;
    }

    public void savePayPersonsIntoSession(SessionMap payPersonMap) {
        if (null != payPersonMap) {
            MemoryManager.save(this.fabricRequestManager, PayPersonManager.PAY_PERSON + this.customerId, payPersonMap);
        }
    }

    public boolean validatePayPerson(String customerId, String payPersonId) {
        SessionMap payPersonMap = getPayPersonsFromSession(customerId);
        if (null == payPersonMap) {
            return false;
        }
        return payPersonMap.hasKey(payPersonId);
    }

}