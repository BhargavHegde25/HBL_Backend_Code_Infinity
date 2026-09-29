package com.kony.dbputilities.sortutil;

import java.util.Comparator;

import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.dataobject.Record;

public class SortRecordByParamValue implements Comparator<Record> {
    private boolean asc = false;
    private String paramName = "";

    public SortRecordByParamValue(String paramName, boolean order) {
        this.paramName = paramName;
        this.asc = order;
    }

    @Override
    public int compare(Record record1, Record record2) {
        String str1 = HelperMethods.getFieldValue(record1, paramName);
        String str2 = HelperMethods.getFieldValue(record2, paramName);
        if (asc) {
            return str1.compareTo(str2);
        }
        return str2.compareTo(str1);
    }
}