package com.kony.bbgeneralservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class OFACAndCIPValidation implements JavaService2 {
    /**
     *
     * @param s
     * @param objects
     * @param dcRequest
     * @param dcResponse
     * @return result
     * @throws Exception
     */
    @Override
    public Object invoke(String s, Object[] objects, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
            throws Exception {

        Map<String, String> inputParams = HelperMethods.getInputParamMap(objects);

        inputParams.get("ssnNumber");
        String dob = inputParams.get("dob");

        Result result = new Result();
        Param p = new Param();
        p.setName("Status");

        if (isValidDOB(dob)) {
            p.setValue("true");
            result.addParam(p);
            HelperMethods.setSuccessMsg("Successful", result);
        } else {
            p.setValue("false");
            result.addParam(p);
            ErrorCodeEnum.ERR_12426.setErrorCode(result);
            // HelperMethods.setValidationMsgwithCode("User Verification failed.", "3401",
            // result);
        }
        return result;

    }

    /**
     *
     * @param ssno
     * @return boolean
     */
    /**
     * Helper function to validate SSN
     */
    public boolean isValidSSN(String ssno) {
        if (ssno == null || ssno.length() != 9) {
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     * @param dob
     * @return boolean
     * @throws ParseException
     */
    /**
     * Helper function to validate date of birth
     */
    public boolean isValidDOB(String dob) {
        Date givenDate = parseDate(dob);
        if (givenDate == null) {
            return false;
        }

        Date currentDate = new Date();
        double ageInDays = TimeUnit.MILLISECONDS.toDays(currentDate.getTime() - givenDate.getTime());

        if (ageInDays / 365 > 18) {
            return true;
        } else {
            return false;
        }

    }

    private Date parseDate(String dob) {
        SimpleDateFormat[] expectedFormats = new SimpleDateFormat[] { new SimpleDateFormat("MM-dd-yyyy"),
                new SimpleDateFormat("yyyy-MM-dd") };
        for (int i = 0; i < expectedFormats.length; i++) {
            try {
                expectedFormats[i].setLenient(false);
                return expectedFormats[i].parse(dob);
            } catch (ParseException e) {
            }
        }
        return null;
    }

}
