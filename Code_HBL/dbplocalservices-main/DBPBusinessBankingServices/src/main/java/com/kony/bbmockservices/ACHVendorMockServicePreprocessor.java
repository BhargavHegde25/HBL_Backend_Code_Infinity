package com.kony.bbmockservices;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class ACHVendorMockServicePreprocessor implements DataPreProcessor {

    @Override
    public boolean execute(HashMap inputs, DataControllerRequest dcr, Result output) throws Exception {
        output.addParam(new Param("Status", getStatusBasedOnDate(inputs.get("effectiveDate").toString())));
        
        SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG", "SUN");
        output.addParam(new Param("ReferenceID",  secureRandomGenerator.nextInt(100000) + ""));
        
        return false;
    }

    public String getStatusBasedOnDate(String EffectiveDate) throws ParseException {
        try {
            SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Date str1 = ts;
            String[] arr = sdfo.format(str1).split(" ");
            Date d1 = sdfo.parse(EffectiveDate);
            Date d2 = sdfo.parse(arr[0]);

            if (d1.after(d2)) {
                return "Sent";
            } else if (d1.before(d2) || d1.equals(d2)) {
                return "Executed";
            } else {
                return "Rejected";
            }
        } catch (ParseException exp) {
            return "Rejected";
        }
    }
}
