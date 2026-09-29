package com.infinity.dbx.temenos.accounts;

import java.util.List;

import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * Deterministic text form of a Fabric Result for equivalence tests. It keeps the order of params, datasets and
 * records, and includes each param's name, type and value, so two Results with the same text are
 * indistinguishable to Fabric's serialisation. Fabric's own ResultToJSON cannot run outside a Fabric server.
 */
final class ResultCanonical {

    private ResultCanonical() {
    }

    static String of(Result result) {
        if (result == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder("Result{");
        params(sb, result.getParamList());
        datasets(sb, result.getDataSets());
        records(sb, result.getRecords());
        return sb.append('}').toString();
    }

    private static void record(StringBuilder sb, Record record) {
        sb.append("Record(").append(record.getId()).append("){");
        params(sb, record.getParams());
        datasets(sb, record.getDatasets());
        records(sb, record.getRecords());
        sb.append('}');
    }

    private static void params(StringBuilder sb, List<Param> params) {
        if (params == null) {
            return;
        }
        for (Param p : params) {
            sb.append(p.getName()).append(':').append(p.getType()).append('=').append(p.getValue()).append(';');
        }
    }

    private static void datasets(StringBuilder sb, List<Dataset> datasets) {
        if (datasets == null) {
            return;
        }
        for (Dataset d : datasets) {
            sb.append("Dataset(").append(d.getId()).append(")[");
            records(sb, d.getAllRecords());
            sb.append(']');
        }
    }

    private static void records(StringBuilder sb, List<Record> records) {
        if (records == null) {
            return;
        }
        for (Record r : records) {
            record(sb, r);
        }
    }
}
