package com.kony.dbputilities.metrics;

public class DBPMetricData {

    public static final String STRING = "string";
    public static final String BOOLEAN = "boolean";
    public static final String LONG = "long";
    public static final String DOUBLE = "double";
    public static final String DATE = "date";
    public static final String TIMESTAMP = "timestamp";

    private String metricName;
    private String metricValue;
    private String metricType;

    public String getMetricName() {
        return metricName;
    }

    public String getMetricValue() {
        return metricValue;
    }

    public String getMetricType() {
        return metricType;
    }

    public DBPMetricData(String metricName, String metricValue, String metricType) {
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.metricType = metricType;
    }
}
