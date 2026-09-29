package com.kony.memorymgmt;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.model.PayeeHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class PayeeManager {

    public static final String PAYEES = "PAYEES";
    public static final String WIRE_TRANSFER_PAYEES = "WIRE_TRANSFER_PAYEES";
    public static final String BULK_WIRE_ID = "BULK_WIRE_ID";
    public static final String BULK_WIRE_TRANSFER_PAYEES = "BULK_WIRE_TRANSFER_PAYEES";
    public static final String BULK_WIRE_FILE_ID = "BULK_WIRE_FILE_ID";
    public static final String BULK_WIRE_FILE_LINES = "BULK_WIRE_FILE_LINES";
    private FabricRequestManager fabricRequestManager = null;
    private String customerId = null;

    public PayeeManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public SessionMap getPayeesFromSession(String customerId) {
        SessionMap payeesMap =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, PayeeManager.PAYEES + this.customerId);
        if (null == payeesMap || payeesMap.isEmpty()) {
            payeesMap = PayeeHelper.reloadPayeesIntoSession(fabricRequestManager);
        }
        return payeesMap;
    }

    public SessionMap getWireTransferPayeesFromSession(String customerId) {
        SessionMap wirePayeesmap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                PayeeManager.WIRE_TRANSFER_PAYEES + this.customerId);
        if (null == wirePayeesmap || wirePayeesmap.isEmpty()) {
            wirePayeesmap = PayeeHelper.reloadWireTransferPayeesIntoSession(fabricRequestManager);
            // wirePayeesmap = (SessionMap)MemoryManager.retrieve(this.fabricRequestManager,
            // PayeeManager.WIRE_TRANSFER_PAYEES);
        }
        return wirePayeesmap;
    }
    public SessionMap getBulkwireFileLineItemsFromSession(String customerId) {
        SessionMap payeesMap =
                (SessionMap) MemoryManager.retrieve(this.fabricRequestManager, PayeeManager.BULK_WIRE_TRANSFER_PAYEES + this.customerId);
        return payeesMap;
    }

    public void savePayeesIntoSession(SessionMap payeesMap) {
        if (null != payeesMap) {
            MemoryManager.save(this.fabricRequestManager, PayeeManager.PAYEES + this.customerId, payeesMap);
        }
    }

    public void saveWireTransferPayeesIntoSession(SessionMap wirePayeesMap) {
        if (null != wirePayeesMap) {
            MemoryManager.save(fabricRequestManager, PayeeManager.WIRE_TRANSFER_PAYEES + this.customerId,
                    wirePayeesMap);
        }
    }
    public void saveBulkwireFileLineItemsIntoSession(SessionMap wirePayeesMap) {
        if (null != wirePayeesMap) {
            MemoryManager.save(fabricRequestManager, PayeeManager.BULK_WIRE_TRANSFER_PAYEES + this.customerId,
                    wirePayeesMap);
        }
    }
    
    public void saveBulkwireFileItemsIntoSession(SessionMap wirePayeesMap) {
        if (null != wirePayeesMap) {
            MemoryManager.save(fabricRequestManager, PayeeManager.BULK_WIRE_FILE_LINES + this.customerId,
                    wirePayeesMap);
        }
    }
    public SessionMap getBulkWireFilesFromSession(String customerId) {
        SessionMap wirePayeesmap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                PayeeManager.BULK_WIRE_FILE_LINES + this.customerId);
        
        return wirePayeesmap;
    }
    public boolean ValidateBulkwireFileItemsIntoSession(String customerId, String accountNumber) {
        SessionMap payeesMap = getBulkWireFilesFromSession(customerId);
        if (null == payeesMap) {
            return false;
        }
        return payeesMap.hasKey(accountNumber);
    }
    
    public void saveBulkWireFileIntoSession(SessionMap BulkWiresMap) {
        if (null != BulkWiresMap) {
            MemoryManager.save(fabricRequestManager, PayeeManager.BULK_WIRE_ID + this.customerId,
            		BulkWiresMap);
        }
    }
    public SessionMap getBulkWireIdFromSession(String customerId) {
        SessionMap wirePayeesmap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
                PayeeManager.BULK_WIRE_ID + this.customerId);
        
        return wirePayeesmap;
    }
    public boolean validateBulkWireId(String customerId,String bulkWireID ) {
        SessionMap payeesMap = getBulkWireIdFromSession(customerId);
        if (null == payeesMap) {
            return false;
        }
        return payeesMap.hasKey(bulkWireID);
    }

    public boolean validatePayee(String customerId, String payeeId) {
        SessionMap payeesMap = getPayeesFromSession(customerId);
        if (null == payeesMap) {
            return false;
        }
        return payeesMap.hasKey(payeeId);
    }
    
    
    public boolean ValidateBulkwireFileLineItemsIntoSession(String customerId, String accountNumber) {
        SessionMap payeesMap = getBulkwireFileLineItemsFromSession(customerId);
        if (null == payeesMap) {
            return false;
        }
        return payeesMap.hasKey(accountNumber);
    }

    public boolean validateWireTransferPayee(String customerId, String payeeId, String wireAccountType) {
        SessionMap wirePayeesMap = getWireTransferPayeesFromSession(customerId);
        if (null == wirePayeesMap || wireAccountType == null) {
            return false;
        }

        return wirePayeesMap.hasKey(payeeId)
                && wireAccountType.equalsIgnoreCase(wirePayeesMap.getAttributeValueForKey(payeeId, "wireAccountType"));
    }
    
    public boolean validateWireTransferPayeeAccountNumber(String customerId, String payeeId, String payeeAccountNumber) {
        SessionMap wirePayeesMap = getWireTransferPayeesFromSession(customerId);

        return wirePayeesMap.hasKey(payeeId)
                && payeeAccountNumber.equalsIgnoreCase(wirePayeesMap.getAttributeValueForKey(payeeId, "payeeAccountNumber"));
    }
    
    public boolean validateWireTransferPayeeId(String customerId, String payeeId) {
        SessionMap wirePayeesMap = getWireTransferPayeesFromSession(customerId);

        return wirePayeesMap.hasKey(payeeId);
    }
    
    public boolean validateBillPayPayeeAccountNumber(String customerId, String payeeId, String payeeAccountNumber) {
        SessionMap payeesMap = getPayeesFromSession(customerId);
        if (null == payeesMap) {
            return false;
        }
        return payeesMap.hasKey(payeeId) && payeeAccountNumber
                .equalsIgnoreCase(payeesMap.getAttributeValueForKey(payeeId, "payeeAccountNumber"));
    }
    
    public void saveBulkWireFileIdsIntoSession(SessionMap BulkWiresMap) {
    if (null != BulkWiresMap) {
    MemoryManager.save(fabricRequestManager, PayeeManager.BULK_WIRE_FILE_ID + this.customerId,
    BulkWiresMap);
    }
    }
    public SessionMap getBulkWireFileIdsFromSession(String customerId) {
    SessionMap bulkWireFilesMap = (SessionMap) MemoryManager.retrieve(this.fabricRequestManager,
    PayeeManager.BULK_WIRE_FILE_ID + this.customerId);
    return bulkWireFilesMap;
    }
    public boolean validateBulkWireFileId(String customerId,String bulkWireFileID ) {
    SessionMap bulkWireFilesMap = getBulkWireFileIdsFromSession(customerId);
    if (null == bulkWireFilesMap) {
    return false;
    }
    return bulkWireFilesMap.hasKey(bulkWireFileID);
    }



}