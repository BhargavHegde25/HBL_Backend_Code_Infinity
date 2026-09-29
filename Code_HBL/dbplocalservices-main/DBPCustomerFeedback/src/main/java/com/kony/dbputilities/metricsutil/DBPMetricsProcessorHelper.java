package com.kony.dbputilities.metricsutil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.exceptions.MetricsException;
import com.konylabs.middleware.metrics.KonyCustomMetrics;
import com.konylabs.middleware.metrics.KonyCustomMetricsDataSet;
import com.konylabs.middleware.registry.AppRegistryException;

public class DBPMetricsProcessorHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    public boolean addCustomMetrics(DataControllerRequest request, List<DBPMetricData> custMetrics, Boolean isOld)
            throws MetricsException, ParseException {
        if (null != request.getAttribute(MWConstants.C_CUSTOM_METRICS)) {
            KonyCustomMetrics objKonyCustomMetrics = (KonyCustomMetrics) request
                    .getAttribute(MWConstants.C_CUSTOM_METRICS);
            if (isOld) {
                List<KonyCustomMetricsDataSet> cusMetDataSetList = objKonyCustomMetrics.getCustomMetrics();
                if (null != cusMetDataSetList && cusMetDataSetList.size() > 0) {
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
            throws MetricsException, ParseException {
        KonyCustomMetricsDataSet metricsDataset = addCustomMetricToMetricDataset(custMetrics, null);
        try {
            request.getServicesManager().getKonyCustomMetrics().addCustomMetrics(metricsDataset);
        } catch (AppRegistryException e) {
            alert.prepareError(e.getMessage()).log();
        }
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
            }
        }
        return metricsDataset;
    }

    private Date getCurrentDate(String metricDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.parse(metricDate);
    }
}
