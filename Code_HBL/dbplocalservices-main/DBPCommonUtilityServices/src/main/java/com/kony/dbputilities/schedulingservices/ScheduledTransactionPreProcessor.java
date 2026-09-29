package com.kony.dbputilities.schedulingservices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class ScheduledTransactionPreProcessor {

    public boolean execute(Map<String, String> map, DataControllerRequest request, Result result) {
        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String currentDate = ts.substring(0, 10);
        boolean isScheduled = true;
        String filter = DBPUtilitiesConstants.SCHEDULED_DATE + DBPUtilitiesConstants.LESS_EQUAL + currentDate
                + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.TRANS_IS_SCHEDULED + DBPUtilitiesConstants.EQUAL
                + isScheduled + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.STATUS_DESC
                + DBPUtilitiesConstants.EQUAL + DBPUtilitiesConstants.TRANS_STATUS_DESC_PENDING
                + DBPUtilitiesConstants.AND + DBPUtilitiesConstants.SOFT_DELETE_FLAG + DBPUtilitiesConstants.EQUAL
                + false;
        map.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }
}
