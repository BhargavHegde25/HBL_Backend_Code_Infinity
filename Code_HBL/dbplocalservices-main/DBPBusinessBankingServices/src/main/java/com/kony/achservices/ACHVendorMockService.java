package com.kony.achservices;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.kony.dbputilities.util.CommonUtils;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/***
 * This is to implement ACHVendorMockService
 */
public class ACHVendorMockService implements JavaService2 {
    /**
     * @param s
     * @param objects
     * @param dcRequest
     * @param dcResponse
     * @return
     * @throws Exception
     * @description Base invoke method
     */
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {
        Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);
        String EffectiveDate = inputParams.get("EffectiveDate");

        Result result = new Result();
        Record rec = new Record();
        Param p = new Param();

        p.setName("Status");
        String status = achVendorMockServiceHelper(EffectiveDate);
        p.setValue(CommonUtils.getStatusid(dcRequest, status));

        rec.addParam(p);
        result.addRecord(rec);
        return result;
    }

    /**
     *
     * @param EffectiveDate
     * @return status(String)
     * @throws ParseException
     */
    public String achVendorMockServiceHelper(String EffectiveDate) throws ParseException {
        SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        Date str1 = ts;
        String[] arr = sdfo.format(str1).split(" ");
        Date d1 = sdfo.parse(EffectiveDate);
        Date d2 = sdfo.parse(arr[0]);

        if (d1.after(d2)) {
            return "Sent";
        } else if (d1.before(d2)) {
            return "Rejected";
        } else if (d1.equals(d2)) {
            return "Completed";
        }

        return null;
    }
}
