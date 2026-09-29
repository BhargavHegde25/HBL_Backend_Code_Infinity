package com.temenos.infinity.api.transactionadviceapi.config;

import com.temenos.infinity.api.commons.config.InfinityServices;

/**
 * 
 * {@link Enum} maintaining the list of Fabric services & operations of the Arrangements Experience API
 * 
 * @author Aditya Mankal
 *
 */
public enum TransactionAdviceAPIServices implements InfinityServices {

    TRANSACTIONADVICEJAVA_LOGINANDVIEW("TransactionAdviceJava", "loginAndView"),
    TRANSACTIONADVICEJSON_LOGIN("TransactionAdviceJSON", "login"),
    TRANSACTIONADVICEJSON_SEARCH("TransactionAdviceJSON", "searchByCUK"),
    DBPUSERATTRIBUTES_GETUSERATTRIBUTEDETAILS("dbpUserAttributes","getUserAttributesDetails"),
    TRANSACTIONADVICEJSON_DOWNLOAD("TransactionAdviceJSON", "download");

    private String serviceName, operationName;

    /**
     * @param serviceName
     * @param operationName
     */
    private TransactionAdviceAPIServices(String serviceName, String operationName) {
        this.serviceName = serviceName;
        this.operationName = operationName;
    }

    @Override
    public String getServiceName() {
        return this.serviceName;
    }

    @Override
    public String getOperationName() {
        return this.operationName;
    }

}
