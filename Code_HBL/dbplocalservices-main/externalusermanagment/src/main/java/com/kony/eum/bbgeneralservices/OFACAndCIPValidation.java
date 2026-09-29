package com.kony.eum.bbgeneralservices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        String ssnNumber = inputParams.get("ssnNumber");
        String dob = inputParams.get("dob");

        Result result = new Result();
        Param p = new Param();
        p.setName("Status");

        boolean isValidDOB = isValidDOB(dob);
        boolean isValidSSN = isValidSSN(ssnNumber);
        
        if (isValidDOB && isValidSSN) {
            p.setValue("true");
            result.addParam(p);
            HelperMethods.setSuccessMsg("Successful", result);
        } else {
            p.setValue("false");
            result.addParam(p);
            if(!isValidDOB) {
                ErrorCodeEnum.ERR_12426.setErrorCode(result);
            } else if(!isValidSSN) {
                ErrorCodeEnum.ERR_29050.setErrorCode(result);
            }
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
        if (ssno == null || ssno.trim().isEmpty()  || ssno.trim().length() == 0) {
            return false;
        } else {
            ssno = ssno.trim();
            String ssnRegExp = "[^a-zA-Z0-9 ]";
            Pattern patternObj = Pattern.compile(ssnRegExp);
            Matcher matcherObj = patternObj.matcher(ssno);
            Boolean isNotValid = matcherObj.find();
            return !isNotValid;
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
