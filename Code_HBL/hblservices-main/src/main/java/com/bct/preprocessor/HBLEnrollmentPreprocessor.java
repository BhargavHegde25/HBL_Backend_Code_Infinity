package com.bct.preprocessor;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbputilities.util.JSONUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class HBLEnrollmentPreprocessor extends TemenosBasePreProcessor {

    @Override
    public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result)
            throws Exception {

        String customerId =
                params.get("customerId") != null ? params.get("customerId").toString() : "";
       /* String ssn =
                params.get("ssn") != null ? params.get("ssn").toString() : "";
                */
        String legalDocumentName =
                params.get("legalDocumentName") != null ? params.get("legalDocumentName").toString() : "";
        String legalId =
                params.get("legalId") != null ? params.get("legalId").toString() : "";
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
        /*if (StringUtils.isNotBlank(ssn)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("taxId" + "=" + ssn);
        }
        */
        if (StringUtils.isNotBlank(legalId)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("legalId" + "=" + legalId);
        }
        if (StringUtils.isNotBlank(legalDocumentName)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            String IDType_id = StringUtils.isNotBlank(legalDocumentName)? getIdentityType().get(legalDocumentName): "";
            filter.append("legalDocumentName" + "=" + IDType_id);
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
       /* if (StringUtils.isNotBlank(lastName)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("lastName" + "=" + lastName);
        }
        */
        if (StringUtils.isNotBlank(lastName)) {
            if (StringUtils.isNotBlank(filter.toString())) {
                filter.append("&");
            }
            filter.append("customerName" + "=" + lastName);
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
    
    private Map<String, String> getIdentityType() {
        Map<String, String> idTypeMap = new HashMap<String, String>();
        
        idTypeMap.put("ID_DRIVING_LICENSE", "DRIVING.LICENSE");
        idTypeMap.put("ID_SOCAIL_SECURITY_NO","SOCIAL.SECURITY.NO");
        idTypeMap.put("ID_PASSPORT","PASSPORT");
        idTypeMap.put("ID_PAN_NO","PAN");
        idTypeMap.put("ID_CITIZEN_CERTIFICATE","CITIZEN.CERT");
        idTypeMap.put("Military ID","MILITARY.ID");
        idTypeMap.put( "ID State","NATIONAL.ID");
        idTypeMap.put("Foreign Government ID","FED.GOVT.ID");
        idTypeMap.put("ID_AADHAR_CARD","ADHAR.CARD");
        idTypeMap.put("ID_BIRTH_CERTIFICATE","BIRTH.CERT");
        idTypeMap.put("ID_FCY_LICENSE/VISA_EXPIRY","FCY.LICENSE.VISA.EXPIRY");
        idTypeMap.put("ID_INCORPORATION_CERTIFICATE","INCORP.CERT");
        idTypeMap.put("ID_INDIAN_EMBASY_CERTIFICATE","IND.EMB.CERT");
        idTypeMap.put("ID_LEGAL_ENTITY_IDENTIFIER","LEI");
        idTypeMap.put("ID_NATIONAL_ID","NATIONAL.ID");
        idTypeMap.put("ID_REFEGUEES_CERTIFICATE","REFUGEE.CERT");
        idTypeMap.put("ID_REGISTERED_CERTIFICATE","REG.CERTIFICATE");
        idTypeMap.put("ID_SCHOOL_ID/CAMPUS_ID","SCHOOL.CAMP");
        idTypeMap.put("ID_VAT_ID","VAT.ID");
        idTypeMap.put("ID_VOTERS_ID","VOTER.ID");
        return idTypeMap;

    }


}
