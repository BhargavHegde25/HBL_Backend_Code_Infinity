package com.kony.dbputilities.demoservices;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.regex.RegularExpression;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateDemoData implements JavaService2 {

    public static JsonArray features = null;

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Map<String, String> inputmap = HelperMethods.getInputParamMap(inputArray);

        Result result = new Result();

        String countryCode = URLFinder.getPathUrl("DMS_DEFAULT_COUNTRY", dcRequest);
        if (StringUtils.isNotBlank(countryCode)) {
            inputmap.put("countryCode", countryCode);
        }

        boolean isWealthCustomer = StringUtils.isNotBlank(dcRequest.getParameter("isWealthCustomer"))
                ? Boolean.valueOf(dcRequest.getParameter("isWealthCustomer").toString())
                : false;
        if (validate(dcRequest, inputmap, result)) {

            if (isWealthCustomer) {
                DemoDataWealthService wealthService = new DemoDataWealthService();
                return wealthService.invoke(methodId, inputArray, dcRequest, dcResponse);
            }
            DemoDataService service = getDemoDataService(inputmap.get("countryCode"));
            return service.invoke(methodId, inputArray, dcRequest, dcResponse);
        }
        return result;
    }

    private DemoDataService getDemoDataService(String countryCode) {
        DemoDataService service = null;
        if ("United Kingdom".equalsIgnoreCase(countryCode)) {
            service = new DemoDataUKService();
        } else if ("Spain".equalsIgnoreCase(countryCode)) {
            service = new DemoDataSpainService();
        } else if ("Germany".equalsIgnoreCase(countryCode)) {
            service = new DemoDataGermanService();
        } else if ("France".equalsIgnoreCase(countryCode)) {
            service = new DemoDataFrenchService();
        } else {
            service = new DemoDataUSService();
        }
        return service;
    }

    private boolean validate(DataControllerRequest dcRequest, Map<String, String> inputmap, Result result)
            throws HttpCallException {
        if (!isAdminUser(inputmap.get("adminUsername"), inputmap.get("adminPassword"), dcRequest)) {
            HelperMethods.setValidationMsg("You need to be a Super Admin to create a User", dcRequest, result);
            result.addStringParam("errmsg", "You need to be a Super Admin to create a User");
            return false;
        }

        if (StringUtils.isNotBlank(inputmap.get("newPassword"))) {
            RegularExpression expression = new RegularExpression(
                    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,16}$");
            if (expression.matches(inputmap.get("newPassword"))) {
                String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
                String hashedPassword = BCrypt.hashpw(inputmap.get("newPassword"), salt);
                inputmap.put("Password", hashedPassword);
            } else {
                HelperMethods.setValidationMsg(
                        "The password must have at least one upper case letter, one number and one special character.",
                        dcRequest, result);
                result.addStringParam("errmsg",
                        "The password must have at least one upper case letter, one number and one special character.");
                return false;
            }
        } else {
            HelperMethods.setValidationMsg(DBPUtilitiesConstants.PROVIDE_VALID_PASSWORD, dcRequest, result);
            result.addStringParam("errmsg", DBPUtilitiesConstants.PROVIDE_VALID_PASSWORD);
            return false;
        }
        return true;
    }

    private boolean isAdminUser(String adminUser, String adminPassword, DataControllerRequest dcRequest)
            throws HttpCallException {
        StringBuilder sb = new StringBuilder();
        sb.append("UserName").append(DBPUtilitiesConstants.EQUAL).append(adminUser).append(DBPUtilitiesConstants.AND)
                .append("Password").append(DBPUtilitiesConstants.EQUAL)
                .append("'" + "$2a$11$qggzt0IGyrC84TuZAAB0zuN2jS8GFCJOmXknnGIGBtb2WvtPQqLXq" + "'");
        Result user = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMERVERIFY_GET);
        if (HelperMethods.hasRecords(user)) {
            String isSuperAdmin = HelperMethods.getFieldValue(user, "isSuperAdmin");
            return "true".equalsIgnoreCase(isSuperAdmin) || "1".equals(isSuperAdmin);
        }
        return false;
    }

    public static JsonArray getFeaturesList(DataControllerRequest dcRequest) throws HttpCallException {
        Result result = HelperMethods.callGetApi(dcRequest, null, HelperMethods.getHeaders(dcRequest),
                URLConstants.FEATURE_GET);
        if (features == null) {
            features = new JsonArray();
            for (com.konylabs.middleware.dataobject.Record r : result.getDatasetById("feature").getAllRecords()) {
                JsonObject json = new JsonObject();
                json.addProperty("featureId", r.getParamValueByName("id"));
                features.add(json);
            }
        }
        return features;

    }
}
