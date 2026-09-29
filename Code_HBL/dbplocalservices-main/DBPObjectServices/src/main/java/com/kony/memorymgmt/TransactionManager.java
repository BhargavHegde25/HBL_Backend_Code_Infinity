package com.kony.memorymgmt;


import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.HelperMethods;
import com.kony.mfaconstant.ServiceNameConstants;
import com.kony.model.TransactionHelper;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionManager {

    public static final String TRANSACTIONS = "TRANSACTIONS";
    // for mobile
    public static final String SCHEDULED_TRANSACTIONS = "SCHEDULED_TRANSACTIONS";
    public static final String POSTED_TRANSACTIONS = "POSTED_TRANSACTIONS";
    public static final String CARD_TRANSACTIONS = "CARD_TRANSACTIONS";
    private static final String CREDIT_CARDS = "CREDIT_CARDS";
    
    private FabricRequestManager fabricRequestManager = null;
    private FabricResponseManager fabricResponseManager = null;
    private String customerId = null;

    public TransactionManager(FabricRequestManager fabricRequestManager, FabricResponseManager fabricResponseManager) {
        this.fabricRequestManager = fabricRequestManager;
        this.fabricResponseManager = fabricResponseManager;
        this.customerId = HelperMethods.getCustomerIdFromSession(fabricRequestManager);
    }

    public SessionMap getTransactionsFromSession(String customerId) {
        Object transactionsMap =
                MemoryManager.retrieve(
                        this.fabricRequestManager, 
                        TransactionManager.TRANSACTIONS + this.customerId);
        return (SessionMap) transactionsMap;
    }

    public void saveTransactionsIntoSession(SessionMap transactionsMap) {
        if (null != transactionsMap) {
            MemoryManager.save(this.fabricRequestManager, TransactionManager.TRANSACTIONS + this.customerId,
                    transactionsMap);
        }
    }

    public SessionMap getScheduledTransactionsFromSession(String customerId) {
        Object transactionsMap = MemoryManager.retrieve(this.fabricRequestManager,
                TransactionManager.SCHEDULED_TRANSACTIONS + this.customerId);
        return (SessionMap) transactionsMap;
    }

    public void saveScheduledTransactionsIntoSession(SessionMap transactionsMap) {
        if (null != transactionsMap) {
            MemoryManager.save(this.fabricRequestManager, TransactionManager.SCHEDULED_TRANSACTIONS + this.customerId,
                    transactionsMap);
        }
    }

    public SessionMap getPostedTransactionsFromSession(String customerId) {
        Object transactionsMap = MemoryManager.retrieve(this.fabricRequestManager,
                TransactionManager.POSTED_TRANSACTIONS + this.customerId);
        return (SessionMap) transactionsMap;
    }

    public void savePostedTransactionsIntoSession(SessionMap transactionsMap) {
        if (null != transactionsMap) {
            MemoryManager.save(this.fabricRequestManager, TransactionManager.POSTED_TRANSACTIONS + this.customerId,
                    transactionsMap);
        }
    }
    
    public SessionMap getCardTransactionsIntoSession(String customerId) {
        Object transactionsMap = MemoryManager.retrieve(this.fabricRequestManager,
                TransactionManager.CARD_TRANSACTIONS + this.customerId);
        return (SessionMap) transactionsMap;
    }
    
    public void saveCardTransactionsIntoSession(SessionMap transactionsMap) {
        if (null != transactionsMap) {
            MemoryManager.save(this.fabricRequestManager, TransactionManager.CARD_TRANSACTIONS + this.customerId,
                    transactionsMap);
        }
    }

    /*
     * Save Credit cards in Session
     */
    public void saveCreditCardsIntoSession(SessionMap creditCards) {
        if (null != creditCards) {
            MemoryManager.save(this.fabricRequestManager, TransactionManager.CREDIT_CARDS + this.customerId,
                    creditCards);
        }
    }
    
    /*
     * Get Credit cards in Session
     */
    public SessionMap getCreditCardsFromSession(String customerId) {
        Object transactionsMap = MemoryManager.retrieve(this.fabricRequestManager,
                TransactionManager.CREDIT_CARDS + this.customerId);
        return (SessionMap) transactionsMap;
    }

    public boolean validateTransactionId(String customerId, String transactionId) {
        SessionMap transactionMap = getTransactionsFromSession(customerId);
        if(transactionMap != null && transactionMap.hasKey(transactionId)) {
            return true;
        }
        transactionMap = getScheduledTransactionsFromSession(customerId);
        if(transactionMap != null && transactionMap.hasKey(transactionId)) {
            return true;
        }
        transactionMap = getPostedTransactionsFromSession(customerId);
        if(transactionMap != null && transactionMap.hasKey(transactionId)) {
            return true;
        }
        transactionMap = TransactionHelper.
                reloadScheduledTransactionsIntoSession(this.fabricRequestManager);
        if(transactionMap != null && transactionMap.hasKey(transactionId)) {
            return true;
        }
        transactionMap = TransactionHelper.
                reloadPostedTransactionsIntoSession(this.fabricRequestManager);
        if(transactionMap != null && transactionMap.hasKey(transactionId)) {
            return true;
        }
        transactionMap = getCardTransactionsIntoSession(customerId);
        if(transactionMap != null && transactionMap.hasKey(transactionId)) {
            return true;
        }
        return false;
    }

    public boolean validateTransactionDate(String frequencyStartDate, String scheduledDate) throws ParseException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date bankDate = sdf.parse(TransactionHelper.getBankDate(fabricRequestManager));
            boolean isValid = true;
            Date date;
            if(StringUtils.isNotBlank(frequencyStartDate)){
                date = sdf.parse(frequencyStartDate);
                isValid = date.compareTo(bankDate) >= 0;
            }
            if(isValid && StringUtils.isNotBlank(scheduledDate)){
                date = sdf.parse(scheduledDate);
                isValid = date.compareTo(bankDate) >= 0;
            }
            return isValid;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean validateTransaction(String customerId, String serviceId, String fromAccount, String toAccount,
            String externalAccountId, String payeeId, String payPersonId) {
        AccountsManager accontManager = new AccountsManager(fabricRequestManager, fabricResponseManager);

        if (ServiceNameConstants.RDC.equalsIgnoreCase(serviceId)) {
            return accontManager.validateInternalAccount(null, toAccount);
        }

        boolean status = accontManager.validateInternalAccount(null, fromAccount);

        if (ServiceNameConstants.RDC.equalsIgnoreCase(serviceId)) {
            status = accontManager.validateInternalAccount(null, toAccount);
        } else if (status) {

            if (ServiceNameConstants.OWN_ACCOUNT_TRANSFER.equalsIgnoreCase(serviceId)) {
                status = accontManager.validateInternalAccount(null, toAccount);
            }

            /*Commenting External Account validation check as part of EIBB-4311(User should be able to do transfer on deleted beneficiary)
            Uncomment the below code once bundle configurations are added*/
//            else if (ServiceNameConstants.INTRA_BANK_TRANSFER.equalsIgnoreCase(serviceId)) {
//                status = (StringUtils.isBlank(toAccount) || accontManager.validateExternalBankToAccount(null, toAccount, true, false))
//                        && (StringUtils.isBlank(externalAccountId) || accontManager.validateExternalBankToAccount(null, externalAccountId, true, false));
//            }
//            else if (ServiceNameConstants.INTER_BANK_ACCOUNT_TRANSFER.equalsIgnoreCase(serviceId)) {
//                status = (StringUtils.isBlank(toAccount) || accontManager.validateExternalBankToAccount(null, toAccount, false, false))
//                        && (StringUtils.isBlank(externalAccountId)|| accontManager.validateExternalBankToAccount(null, externalAccountId, false, false));
//            }
//            else if (ServiceNameConstants.INTERNATIONAL_BANK_TRANSFER.equalsIgnoreCase(serviceId)) {
//                status = (StringUtils.isBlank(toAccount) || accontManager.validateExternalBankToAccount(null, toAccount, false, true))
//                        && (StringUtils.isBlank(externalAccountId)|| accontManager.validateExternalBankToAccount(null, externalAccountId, false, true));
//            }

            else if (ServiceNameConstants.BILL_PAY.equalsIgnoreCase(serviceId)) {
                PayeeManager payeeManager = new PayeeManager(fabricRequestManager, fabricResponseManager);
                status = StringUtils.isBlank(payeeId) || payeeManager.validatePayee(null, payeeId);
            } else if (ServiceNameConstants.DOMESTIC_WIRE_TRANSFER.equalsIgnoreCase(serviceId)) {
                PayeeManager payeeManager = new PayeeManager(fabricRequestManager, fabricResponseManager);
                status = StringUtils.isBlank(payeeId)
                        || payeeManager.validateWireTransferPayee(null, payeeId, "Domestic");
            } else if (ServiceNameConstants.INTERNATIONAL_WIRE_TRANSFER.equalsIgnoreCase(serviceId)) {
                PayeeManager payeeManager = new PayeeManager(fabricRequestManager, fabricResponseManager);
                status = StringUtils.isBlank(payeeId)
                        || payeeManager.validateWireTransferPayee(null, payeeId, "International");
            } else if (ServiceNameConstants.PAY_PERSON_TRANSFER.equalsIgnoreCase(serviceId)) {
                PayPersonManager payPerson = new PayPersonManager(fabricRequestManager, fabricResponseManager);
                status = StringUtils.isBlank(payPersonId) || payPerson.validatePayPerson(null, payPersonId);
            }
        }

        return status;
    }
}