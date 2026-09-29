package com.kony.dbputilities.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class SortingUtil {

    private final String KEY_NAME;
    private String ORDER;

    public SortingUtil(String sortBy, String order) {
        this.KEY_NAME = sortBy;
        this.ORDER = "desc";

        if (StringUtils.isNotBlank(order)) {
            this.ORDER = order;
        }
    }

    public class ComparatorUtil implements Comparator<Record> {

        @Override
        public int compare(Record objectA, Record objectB) {
            String valA = objectA.getParamValueByName(KEY_NAME);
            String valB = objectB.getParamValueByName(KEY_NAME);
            int x;

            if (StringUtils.isBlank(valA) || StringUtils.isBlank(valB)) {
                x = StringUtils.isNotBlank(valA) ? 1 : -1;
            } else if (HelperMethods.isStringADouble(valA) && HelperMethods.isStringADouble(valB)) {
                double a = Double.parseDouble(valA);
                double b = Double.parseDouble(valB);
                x = Double.compare(a, b);
            } else if (HelperMethods.isStringADate(valA) && HelperMethods.isStringADate(valB)) {
                x = getDateDifference(valA, valB);
            } else {
                x = valA.compareTo(valB);
            }

            if ("asc".equalsIgnoreCase(ORDER)) {
                return x;
            } else {
                return -1 * x;
            }
        }
    }

    public Result sort(Result result) {
        if (!HelperMethods.hasRecords(result)) {
            return result;
        }
        Dataset inputdataset = result.getAllDatasets().get(0);
        Dataset returnDataset = new Dataset();
        returnDataset.setId(inputdataset.getId());
        List<Record> datasetRecords = new ArrayList<Record>();
        for (int i = 0; i < inputdataset.getAllRecords().size(); i++) {
            datasetRecords.add(inputdataset.getRecord(i));
        }
        if (!datasetRecords.isEmpty()) {
            ComparatorUtil comparatorObj = this.new ComparatorUtil();
            Collections.sort(datasetRecords, comparatorObj);
            returnDataset.addAllRecords(datasetRecords);
            Result retResult = new Result();
            retResult.addDataset(returnDataset);
            return retResult;
        }
        return result;
    }

    private int getDateDifference(String valA, String valB) {
        Date dateA = HelperMethods.getFormattedTimeStamp(valA);
        Calendar calenderA = Calendar.getInstance();
        calenderA.setTime(dateA);

        Date dateB = HelperMethods.getFormattedTimeStamp(valB);
        Calendar calenderB = Calendar.getInstance();
        calenderB.setTime(dateB);

        long millisA = calenderA.getTimeInMillis();
        long millisB = calenderB.getTimeInMillis();

        return (int) (millisA - millisB);
    }

}
