package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CustomerSearchByPartyMSPreProcessor implements DataPreProcessor2 {

    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
    @Override
    public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
		Log4j2Configurator.getInstance();

        String id = request.getParameter("_id");
        String phone = request.getParameter("_phone");
        String username = request.getParameter("_username");
        String email = request.getParameter("_email");
        String IDType = request.getParameter("_IDType");
        String IDValue = request.getParameter("_IDValue");
        String query = StringUtils.EMPTY;
        String SSN = request.getParameter("_SSN");
        String TIN = request.getParameter("_TIN");
        String name = request.getParameter("_name");
        if (id != null && !id.equalsIgnoreCase("null")) {
            query = query.concat("customerId=" + id + "&");
        }
        if (phone != null && !phone.equalsIgnoreCase("null")) {
            query = query.concat("contactNumber=" + phone + "&");
        }
        if (email != null && !email.equalsIgnoreCase("null")) {
            query = query.concat("emailId=" + email + "&");
        }
        if (name != null && !name.equalsIgnoreCase("null")) {
            query = query.concat("firstName=" + name + "&");
        }
        if ((IDType != null && !IDType.equalsIgnoreCase("null")) || (SSN != null && !SSN.equalsIgnoreCase("null"))
                || (TIN != null && !TIN.equalsIgnoreCase("null"))) {
            if (IDType != null && IDType.equalsIgnoreCase("ID_DRIVING_LICENSE")) {
                IDType = "5";
            }
            if (IDType != null && IDType.equalsIgnoreCase("ID_PASSPORT")) {
                IDType = "4";
            }
            if (SSN != null && !SSN.equalsIgnoreCase("null")) {
                IDType = "1";
                IDValue = SSN;
            }
            if (TIN != null && !TIN.equalsIgnoreCase("null")) {
                IDType = "3";
                IDValue = TIN;
            }
            query = query.concat("partyIdentificationType=" + IDType + "&" + "partyIdentificationNumber=" + IDValue);
        }

        if (query.endsWith("&")) {
            query = query.substring(0, query.length() - 1);
        }
        inputMap.put("query", query);
        return true;
    }

}
