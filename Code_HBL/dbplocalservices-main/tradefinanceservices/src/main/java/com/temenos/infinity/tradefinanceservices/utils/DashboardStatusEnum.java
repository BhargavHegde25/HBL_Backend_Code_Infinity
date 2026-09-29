package com.temenos.infinity.tradefinanceservices.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author naveen.yerra
 */
public enum DashboardStatusEnum {

    //Payables
    IMPORT_DRAWING(Collections.singletonList("SUBMITTED TO BANK")),
    CLAIM_RECEIVED(Collections.singletonList("CLAIM HONOURED (PENDING CONSENT)")),
    INWARD_COLLECTION(Collections.singletonList("OVERDUE")),

    //Receivables
    EXPORT_DRAWING(Collections.singletonList("APPROVED")),
    CLAIM_INITIATED(Collections.singletonList("PROCESSING WITH BANK")),
    OUTWARD_COLLECTION(Collections.singletonList("OVERDUE")),

    //Others
    IMPORT_LC(new ArrayList<>()),
    IMPORT_AMENDMENT(new ArrayList<>()),
    EXPORT_LC(new ArrayList<>()),
    EXPORT_AMENDMENT(new ArrayList<>()),
    ISSUED_GT_AND_SBLC(new ArrayList<>()),
    ISSUED_GT_AND_SBLC_AMENDMENT(new ArrayList<>()),
    RECEIVED_GT_AND_SBLC(new ArrayList<>()),
    RECEIVED_GT_AND_SBLC_AMENDMENT(new ArrayList<>()),
    INWARD_COLLECTION_AMENDMENT(new ArrayList<>()),
    OUTWARD_COLLECTION_AMENDMENT(new ArrayList<>());

    private final List<String> overdueStatus;

    DashboardStatusEnum(List<String> overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    public List<String> getOverdueStatus() {
        return overdueStatus;
    }

}
