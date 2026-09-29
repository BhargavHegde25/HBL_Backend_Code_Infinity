package com.temenos.infinity.api.financemanagementservices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetPFMTrasactions implements JavaService2 {
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        if (preProcess(inputParams, dcRequest, result)) {
        	StringBuffer queryString = new StringBuffer();
        	int count=0;
        	if(inputParams.get("categoryId")!=null){
        		queryString.append("categoryId"+ " eq "+(String) inputParams.get("categoryId"));
        		count++;
        	}
        	if(inputParams.get("monthId")!=null) {
        		if (count==1) queryString.append(" and ");
        		queryString.append("monthId" + " eq "+(String) inputParams.get("monthId"));
        		count++;
        	}
        	if(inputParams.get("transactionId")!=null) {
        		if (count>=1) queryString.append(" and ");
        		queryString.append("id" + " eq "+(String) inputParams.get("transactionId"));
        		count++;
        	}
        	if(inputParams.get("year")!=null) {
        		if (count>=1)queryString.append(" and ");
        		count++;
        		queryString.append("year" + " eq "+(String) inputParams.get("year"));
        	}
        	inputParams.put("$filter", queryString.toString());
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PFM_TRANSACTIONS_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            result = postProcess(dcRequest, result, inputParams);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private Result postProcess(DataControllerRequest dcRequest, Result result, Map inputParams)
            throws HttpCallException {
        String getUncategorisedCount = (String) inputParams.get("getUncategorisedCount");
        if (StringUtils.isNotBlank(getUncategorisedCount)) {
            Result count = new Result();
            String size = String.valueOf(result.getAllDatasets().get(0).getAllRecords().size());
            Param p = new Param("result", size, DBPUtilitiesConstants.STRING_TYPE);
            count.addParam(p);
            return count;
        } else {
            List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
            Map<String, String> categoryNames = getCategoryNames(dcRequest);
            for (Record transaction : transactions) {
                updatePfmCategoryName(transaction, categoryNames);
                updateDateFormat(transaction);
            }
            return result;
        }
    }

    private void updateDateFormat(Record transaction) {
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
        try {
            if (StringUtils.isNotBlank(transactionDate)) {
                transaction.addParam(new Param("transactionDate",
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ssXXX"), "String"));
            }
        } catch (Exception e) {
        }
    }

    private Map<String, String> getCategoryNames(DataControllerRequest dcRequest) throws HttpCallException {
        Result pfmCategory = HelperMethods.callApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.PFM_CATEGORY_GET);
        Map<String, String> categoryNames = new HashMap<>();
        List<Record> types = pfmCategory.getAllDatasets().get(0).getAllRecords();
        for (Record type : types) {
            categoryNames.put(HelperMethods.getFieldValue(type, "id"),
                    HelperMethods.getFieldValue(type, "categoryName"));
        }
        return categoryNames;
    }

    private void updatePfmCategoryName(Record transaction, Map<String, String> categoryNames) throws HttpCallException {
        String pfmcategoryId = HelperMethods.getFieldValue(transaction, "categoryId");
        if (StringUtils.isNotBlank(pfmcategoryId)) {
            transaction.addParam(
                    new Param("categoryName", categoryNames.get(pfmcategoryId), DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String getMonthlyTransactions = inputParams.get("getMonthlyTransactions");
        String getYearlyTransactions = inputParams.get("getYearlyTransactions");
        String getUncategorisedCount = inputParams.get("getUncategorisedCount");
        String transactionId = inputParams.get("transactionId");
        String categoryId = inputParams.get("categoryId");
        String sortby = inputParams.get("sortby");
        String order = inputParams.get("order");
        if (StringUtils.isBlank(sortby)) {
            sortby = "transactionDate";
        }
        if (StringUtils.isBlank(order)) {
            order = "desc";
        }
        if (StringUtils.isNotBlank(getMonthlyTransactions) || StringUtils.isNotBlank(getYearlyTransactions)) {
            getMonthlyTransactions(dcRequest, inputParams, result);
        } else if (StringUtils.isNotBlank(getUncategorisedCount)) {
            getUncategorisedCount(dcRequest, inputParams);
        } else if (StringUtils.isNotBlank(transactionId)) {
            getPfmTransactionById(inputParams);
        } else if (StringUtils.isNotBlank(categoryId)) {
            String uncategorizedId = getUncategorizedId(dcRequest);
            if (categoryId.equals(uncategorizedId)) {
                getUncategorisedList(inputParams, uncategorizedId);
            } else {
                getPfmTransactionList(inputParams, uncategorizedId);
            }
        }
        inputParams.put(DBPUtilitiesConstants.ORDERBY, sortby + " " + order);
        return status;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void getPfmTransactionList(Map inputParams, String uncategorizedId) {
        String monthId = (String) inputParams.get(DBPUtilitiesConstants.MONTH_ID);
        String year = (String) inputParams.get(DBPUtilitiesConstants.YEAR);
        String categoryId = (String) inputParams.get("categoryId");
        StringBuilder filter = new StringBuilder();
        filter.append("categoryId").append(DBPUtilitiesConstants.EQUAL).append(categoryId);
        filter.append(DBPUtilitiesConstants.AND);
        filter.append("categoryId").append(DBPUtilitiesConstants.NOT_EQ).append(uncategorizedId);

        if (!StringUtils.isBlank(monthId)) {
            filter.append(DBPUtilitiesConstants.AND);
            filter.append(DBPUtilitiesConstants.MONTH_ID).append(DBPUtilitiesConstants.EQUAL).append(monthId);
        }

        if (!StringUtils.isBlank(year)) {
            filter.append(DBPUtilitiesConstants.AND);
            filter.append(DBPUtilitiesConstants.YEAR).append(DBPUtilitiesConstants.EQUAL).append(year);
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void getUncategorisedList(Map inputParams, String uncategorizedId) {
        StringBuilder filter = new StringBuilder();
        filter.append("categoryId").append(DBPUtilitiesConstants.EQUAL).append(uncategorizedId);
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void getPfmTransactionById(Map inputParams) {
        String transactionId = (String) inputParams.get("transactionId");
        StringBuilder filter = new StringBuilder();
        filter.append("id").append(DBPUtilitiesConstants.EQUAL).append(transactionId);
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void getUncategorisedCount(DataControllerRequest dcRequest, Map inputParams) throws HttpCallException {
        String uncategorizedId = getUncategorizedId(dcRequest);
        StringBuilder filter = new StringBuilder();
        filter.append("categoryId").append(DBPUtilitiesConstants.EQUAL).append(uncategorizedId);
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void getMonthlyTransactions(DataControllerRequest dcRequest, Map inputParams, Result result)
            throws HttpCallException {
        String uncategorizedId = getUncategorizedId(dcRequest);
        String monthId = (String) inputParams.get(DBPUtilitiesConstants.MONTH_ID);
        String year = (String) inputParams.get(DBPUtilitiesConstants.YEAR);
        StringBuilder filter = new StringBuilder();
        filter.append("categoryId").append(DBPUtilitiesConstants.NOT_EQ).append(uncategorizedId);

        if (validateMonthId(inputParams, result)) {
            filter.append(DBPUtilitiesConstants.AND);
            filter.append(DBPUtilitiesConstants.MONTH_ID).append(DBPUtilitiesConstants.EQUAL).append(monthId);
        }

        if (validateYear(inputParams, result)) {
            filter.append(DBPUtilitiesConstants.AND);
            filter.append(DBPUtilitiesConstants.YEAR).append(DBPUtilitiesConstants.EQUAL).append(year);
        }

        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
    }

    private boolean validateYear(Map<String, String> inputParams, Result result) {
        String year = inputParams.get(DBPUtilitiesConstants.YEAR);
        if (StringUtils.isBlank(year)) {
            HelperMethods.setValidationMsg("Please send a Valid Year", null, result);
            return false;
        }
        return true;
    }

    private String getUncategorizedId(DataControllerRequest dcRequest) throws HttpCallException {
        String filter = "categoryName" + DBPUtilitiesConstants.EQUAL + "Uncategorised";
        Result pfmCategory = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PFM_CATEGORY_GET);
        return HelperMethods.getFieldValue(pfmCategory, "id");
    }

    @SuppressWarnings("rawtypes")
    private boolean validateMonthId(Map inputParams, Result result) {
        String monthId = (String) inputParams.get(DBPUtilitiesConstants.MONTH_ID);
        if (StringUtils.isBlank(monthId)) {
            HelperMethods.setValidationMsg("Please send a Valid Month", null, result);
            return false;
        }
        return true;
    }
}
