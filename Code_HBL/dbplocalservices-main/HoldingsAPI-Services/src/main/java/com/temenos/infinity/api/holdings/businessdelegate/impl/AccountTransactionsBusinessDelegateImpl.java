package com.temenos.infinity.api.holdings.businessdelegate.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.api.holdings.backenddelegate.api.HoldingsExperienceAPIBackendDelegate;
import com.temenos.infinity.api.holdings.businessdelegate.api.AccountTransactionsBusinessDelegate;
import com.temenos.infinity.api.holdings.dto.AccountTransactionsDTO;

/**
 * 
 * @author KH2281
 * @version 1.0 Extends the {@link AccountTransactionsBusinessDelegate}
 */
public class AccountTransactionsBusinessDelegateImpl implements AccountTransactionsBusinessDelegate {

    /**
     * method to get the Account Transactions
     * 
     * @return List<AccountTransactionsDTO> of Account Transactions
     * @throws ApplicationException
     * @throws UnsupportedEncodingException
     */
    public List<AccountTransactionsDTO> getDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO,
            String authToken) throws ApplicationException {
        HoldingsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(HoldingsExperienceAPIBackendDelegate.class);
        List<AccountTransactionsDTO> records = backendDelegate.getDetailsFromHoldingsMicroService(inputDTO, authToken);
        return records;
    }

    /**
     * method to get the Search Transactions
     * 
     * @return List<AccountTransactionsDTO> of Account Transactions
     * @throws ApplicationException
     */

    public List<AccountTransactionsDTO> getSearchDetailsFromHoldingsMicroService(AccountTransactionsDTO inputDTO,String authToken)
            throws ApplicationException {
        HoldingsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(HoldingsExperienceAPIBackendDelegate.class);
        List<AccountTransactionsDTO> records = backendDelegate.getSearchDetailsFromHoldingsMicroService(inputDTO,authToken);
        return records;
    }

    /**
     * method to get the Account Pending Transactions
     * 
     * @return List<AccountTransactionsDTO> of Account Transactions
     * @throws ApplicationException
     */
    @Override
    public List<AccountTransactionsDTO> getPendingTransactionsDetailsFromT24(AccountTransactionsDTO inputDTO,
            DataControllerRequest request) throws ApplicationException {
        HoldingsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(HoldingsExperienceAPIBackendDelegate.class);
        List<AccountTransactionsDTO> records = backendDelegate.getPendingTransactionsDetailsFromT24(inputDTO, request);
        return records;
    }

    /**
     * method to sort JSONArray of records based on key which is of String Type
     * 
     * @return JSONArray of sorted records
     */
    @Override
    public List<AccountTransactionsDTO> sortBasedOnTransactionDate(List<AccountTransactionsDTO> transactionsDTOList,
            String order, String sortKey) {

        Collections.sort(transactionsDTOList, new Comparator<AccountTransactionsDTO>() {

            @Override
            public int compare(AccountTransactionsDTO a, AccountTransactionsDTO b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.getTransactionDate();
                    valB = (String) b.getTransactionDate();
                } catch (JSONException e) {
                    return -1;
                }
                
                int compValue = 0; 
                if (order.equals("asc") && valA != null && valB != null) {
                    compValue = valA.compareTo(valB);
                } else {
                    if (valA != null && valB != null)
                        compValue = valB.compareTo(valA);
                }
                return compValue;

            }

        });

        return transactionsDTOList;
    }

    /**
     * method to sort JSONArray of records based on key which is of Double Type
     * 
     * @return JSONArray of sorted records
     */
    @Override
    public List<AccountTransactionsDTO> sortBasedOnAmount(List<AccountTransactionsDTO> transactionsDTOList,
            String order, String sortKey) {

        if (order.equals("desc")) {
            transactionsDTOList.sort((o1, o2) -> Double.compare((double) o2.getAmount(), (double) o1.getAmount()));
        } else {
            transactionsDTOList.sort((o1, o2) -> Double.compare((double) o1.getAmount(), (double) o2.getAmount()));
        }

        return transactionsDTOList;
    }

    /**
     * method to filter records based on offset and limit values
     * 
     * @return JSONArray of filtered records
     */
    @Override
    public List<AccountTransactionsDTO> paginationBasedOnOffsetAndLimit(int offset, int limit,
            List<AccountTransactionsDTO> transactionsDTOList) {
        List<AccountTransactionsDTO> filteredJSONArray = new ArrayList<AccountTransactionsDTO>();
        int startIndex = (offset);
        int lastIndex = startIndex + (limit);
        for (int i = startIndex; i < lastIndex && i < transactionsDTOList.size(); i++) {
            filteredJSONArray.add(transactionsDTOList.get(i));
        }
        return filteredJSONArray;
    }

    @Override
    public List<AccountTransactionsDTO> filterRecordsBasedOnTransactionType(String transactionType,
            List<AccountTransactionsDTO> transactionsDTOList) {
        List<AccountTransactionsDTO> filteredRecords = new ArrayList<AccountTransactionsDTO>();
        if (transactionType != null) {
            for (int index = 0; index < transactionsDTOList.size(); index++) {
                AccountTransactionsDTO jsonObject = transactionsDTOList.get(index);
                double amount = (double) jsonObject.getAmount();
                String recordTransactionType = (String) jsonObject.getTransactionType();
                if (transactionType.equals("Deposits") && amount > 0) {
                    filteredRecords.add(jsonObject);
                }
                if (transactionType.equals("Withdrawals") && amount < 0) {
                    filteredRecords.add(jsonObject);
                }
                if (transactionType.equals("Checks")) {
                    if ("CheckWithdrawal".equals(recordTransactionType)) {
                        filteredRecords.add(jsonObject);
                    }
                }
            }
        }
        return filteredRecords;
    }

    @Override
    public List<AccountTransactionsDTO> getPendingAndPostedTransactions(AccountTransactionsDTO inputDTO,
            DataControllerRequest request, String authToken) throws ApplicationException {
        // TODO Auto-generated method stub
        HoldingsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(HoldingsExperienceAPIBackendDelegate.class);
        List<AccountTransactionsDTO> records = backendDelegate.getPendingAndPostedTransactions(inputDTO, request,
                authToken);
        return records;
    }

    @Override
    public List<AccountTransactionsDTO> getloanScheduleTransactions(AccountTransactionsDTO inputDTO,
            String isFutureRequired, DataControllerRequest request, String authToken) throws ApplicationException {
        // TODO Auto-generated method stub
        HoldingsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(HoldingsExperienceAPIBackendDelegate.class);
        List<AccountTransactionsDTO> records = backendDelegate.getLoanScheduleTransactions(inputDTO, isFutureRequired,
                request, authToken);
        return records;
    }

    @Override
    public List<AccountTransactionsDTO> getDeviceRegistrationDetails(AccountTransactionsDTO inputPayLoad,
            DataControllerRequest request) throws ApplicationException {
        HoldingsExperienceAPIBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl
                .getBackendDelegate(HoldingsExperienceAPIBackendDelegate.class);
        List<AccountTransactionsDTO> records = backendDelegate.getDeviceRegistrationDetails(inputPayLoad, request);
        return records;
    }

}