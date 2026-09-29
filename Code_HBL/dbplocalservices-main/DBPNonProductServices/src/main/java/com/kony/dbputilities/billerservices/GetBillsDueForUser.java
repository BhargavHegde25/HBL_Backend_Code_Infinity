package com.kony.dbputilities.billerservices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetBillsDueForUser implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_PAYEE_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(inputParams, result);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private void postProcess(Map inputParams, Result result) throws HttpCallException {
        
    	try {
			List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
			List<Record> transactionsFormatted = new ArrayList<>(); 
			for ( Record transaction:  transactions) {
			    updateDateFormat(transaction);
			    String paidAmount = HelperMethods.getFieldValue(transaction, "paidAmount");
			    String dueAmount = HelperMethods.getFieldValue(transaction, "dueAmount");
			    paidAmount = StringUtils.isBlank(paidAmount) ? "0" : paidAmount;
			    dueAmount = StringUtils.isBlank(dueAmount) ? "0" : dueAmount;
			    if (Double.valueOf(dueAmount).compareTo(Double.valueOf(paidAmount)) <= 0) {
			        transactions.remove(transaction);
			    } else {
			    	transactionsFormatted.add(transaction);
			    }
			}
			if(!transactionsFormatted.isEmpty()) {
				result = new Result();
				Dataset ds = new Dataset("billview");
				ds.addAllRecords(transactionsFormatted);
			    result.addDataset(ds);
			}
		} catch (Exception e) {
			alert.prepareError("Caught exception in GetBillsDueForUser: "+e.getMessage()).log();
		}
        
    }

    private void updateDateFormat(Record transaction) {
        String billDueDate = HelperMethods.getFieldValue(transaction, "billDueDate");
        String billGeneratedDate = HelperMethods.getFieldValue(transaction, "billGeneratedDate");
        String paidDate = HelperMethods.getFieldValue(transaction, "paidDate");

        try {
            if (StringUtils.isNotBlank(billDueDate)) {
                transaction.addParam(new Param("billDueDate",
                        HelperMethods.convertDateFormat(billDueDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(billGeneratedDate)) {
                transaction.addParam(new Param("billGeneratedDate",
                        HelperMethods.convertDateFormat(billGeneratedDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(paidDate)) {
                transaction.addParam(new Param("paidDate",
                        HelperMethods.convertDateFormat(paidDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private String getCategoryName(DataControllerRequest dcRequest, String billerCategoryId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billerCategoryId;
        Result billerMaster = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_CATEGORY_GET);
        return HelperMethods.getFieldValue(billerMaster, "cattegoryName");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String sortBy = (String) inputParams.get("sortBy");
        String order = (String) inputParams.get("order");
        String today = HelperMethods.getCurrentTimeStamp();
        if (StringUtils.isBlank(sortBy)) {
            sortBy = "paidDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }

        StringBuilder filter = new StringBuilder();
        filter.append(DBPUtilitiesConstants.T_USR_ID).append(DBPUtilitiesConstants.EQUAL).append(userId);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("billDueDate").append(DBPUtilitiesConstants.GREATER_EQUAL).append(today);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("softDelete").append(DBPUtilitiesConstants.EQUAL).append("0");
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        inputParams.put(DBPUtilitiesConstants.ORDERBY, sortBy + " " + order);
        return true;
    }
}