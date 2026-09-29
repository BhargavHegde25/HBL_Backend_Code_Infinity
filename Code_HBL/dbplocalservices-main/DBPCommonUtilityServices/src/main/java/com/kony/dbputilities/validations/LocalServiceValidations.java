package com.kony.dbputilities.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalServiceValidations {
    private LocalServiceValidations() {
    }

    public static boolean validateEmailFormat(String email) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validateCountryCodeFormat(String countryCode) {
        String regex = "^\\+[0-9]{1,2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(countryCode);
        return matcher.matches();
    }

    /*
     * @SuppressWarnings({ "unchecked", "rawtypes" }) public static JsonObject callGetApi(String url, String
     * filterQuery) throws HttpCallException { HttpConnector httpConn = new HttpConnector(); Map inputParams = new
     * HashMap(); inputParams.put(DBPUtilitiesConstants.FILTER, filterQuery); JsonObject response =
     * httpConn.invokeHttpPost( URLFinder.getCompleteUrl(url), inputParams, null); return (null == response) ? new
     * JsonObject() : response; }
     */
}
