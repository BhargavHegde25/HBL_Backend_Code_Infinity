package com.infinity.dbx.temenos.enrollment;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class EnrollmentPreprocessor extends TemenosBasePreProcessor {

    @Override
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result)
            throws Exception {

        String customerId =
                params.get("customerId") != null ? params.get("customerId").toString() : "";
        String ssn =
                params.get("ssn") != null ? params.get("ssn").toString() : "";
        String phone =
                params.get("contactNumber") != null ? params.get("contactNumber").toString() : "";
        String email =
                params.get("emailId") != null ? params.get("emailId").toString() : "";
        String lastName =
                params.get("lastName") != null ? params.get("lastName").toString() : "";
        String dateOfBirth =
                params.get("dateOfBirth") != null ? params.get("dateOfBirth").toString() : "";

        StringBuilder filter = new StringBuilder();

        if (StringUtils.isNotBlank(customerId)) {
            filter.append("customerId" + "=" + customerId);
        }
        if (StringUtils.isNotBlank(ssn)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("taxId" + "=" + ssn);
        }
        if (StringUtils.isNotBlank(phone)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("phoneNumber" + "=" + phone);
        }
        if (StringUtils.isNotBlank(email)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("email" + "=" + email);
        }
        if (StringUtils.isNotBlank(lastName)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("lastName" + "=" + lastName);
        }
        if (StringUtils.isNotBlank(dateOfBirth)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("dateOfBirth" + "=" + dateOfBirth);
        }

        params.put("filter", filter.toString());

        return Boolean.TRUE;
    }

}
