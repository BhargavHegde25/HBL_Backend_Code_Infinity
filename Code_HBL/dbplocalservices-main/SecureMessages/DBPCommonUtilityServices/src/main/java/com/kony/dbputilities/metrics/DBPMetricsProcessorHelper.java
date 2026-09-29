package com.kony.dbputilities.metrics;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MetricsException;
import com.konylabs.middleware.metrics.KonyCustomMetrics;
import com.konylabs.middleware.metrics.KonyCustomMetricsDataSet;
import com.konylabs.middleware.registry.AppRegistryException;

public class DBPMetricsProcessorHelper {

    public boolean addCustomMetrics(DataControllerRequest request, List<DBPMetricData> custMetrics, Boolean isOld)
            throws MetricsException, ParseException, AppRegistryException {
        KonyCustomMetrics objKonyCustomMetrics = request.getServicesManager().getKonyCustomMetrics();
        if (null != objKonyCustomMetrics) {
            if (isOld) {
                List<KonyCustomMetricsDataSet> cusMetDataSetList = objKonyCustomMetrics.getCustomMetrics();
                if (null != cusMetDataSetList && !cusMetDataSetList.isEmpty()) {
                    KonyCustomMetricsDataSet konyCustomMetricsDataSet = cusMetDataSetList.get(0);
                    addCustomMetricToMetricDataset(custMetrics, konyCustomMetricsDataSet);
                }
            } else {
                KonyCustomMetricsDataSet metricsDataset = addCustomMetricToMetricDataset(custMetrics, null);
                objKonyCustomMetrics.addCustomMetrics(metricsDataset);
            }
        } else {
            addCustomMetrics(request, custMetrics);
        }
        return true;
    }

    private boolean addCustomMetrics(DataControllerRequest request, List<DBPMetricData> custMetrics)
            throws MetricsException, ParseException, AppRegistryException {
        KonyCustomMetrics kmetrics = request.getServicesManager().getKonyCustomMetrics();
        KonyCustomMetricsDataSet metricsDataset = addCustomMetricToMetricDataset(custMetrics, null);
        kmetrics.addCustomMetrics(metricsDataset);
        return true;
    }

    private KonyCustomMetricsDataSet addCustomMetricToMetricDataset(List<DBPMetricData> custMetrics,
            KonyCustomMetricsDataSet metricsDataset) throws MetricsException, ParseException {
        metricsDataset = (null == metricsDataset) ? new KonyCustomMetricsDataSet() : metricsDataset;
        for (int i = 0; i < custMetrics.size(); i++) {
            DBPMetricData metricData = custMetrics.get(i);
            switch (metricData.getMetricType()) {
                case DBPMetricData.STRING:
                    metricsDataset.setMetricsString(metricData.getMetricName(), metricData.getMetricValue());
                    break;
                case DBPMetricData.BOOLEAN:
                    metricsDataset.setMetricsBoolean(metricData.getMetricName(),
                            Boolean.parseBoolean(metricData.getMetricValue()));
                    break;
                case DBPMetricData.LONG:
                    metricsDataset.setMetricsLong(metricData.getMetricName(),
                            Long.parseLong(metricData.getMetricValue()));
                    break;
                case DBPMetricData.DOUBLE:
                    metricsDataset.setMetricsDouble(metricData.getMetricName(),
                            Double.parseDouble(metricData.getMetricValue()));
                    break;
                case DBPMetricData.DATE:
                    metricsDataset.setMetricsDate(metricData.getMetricName(),
                            getCurrentDate(metricData.getMetricValue()));
                    break;
                case DBPMetricData.TIMESTAMP:
                    metricsDataset.setMetricsTimestamp(metricData.getMetricName(), metricData.getMetricValue());
                    break;
                default:
                    metricsDataset.setMetricsString(metricData.getMetricName(), metricData.getMetricValue());
            }
        }
        return metricsDataset;
    }

    private Date getCurrentDate(String metricDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(metricDate);
    }
}
