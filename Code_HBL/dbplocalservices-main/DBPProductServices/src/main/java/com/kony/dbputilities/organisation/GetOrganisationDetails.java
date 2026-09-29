package com.kony.dbputilities.organisation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganisationDetails implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result;
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        if (StringUtils.isBlank(inputParams.get("id"))) {
            try {
                inputParams.put("id", HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest));
            } catch (HttpCallException e) {

                alert.prepareError(e.getMessage()).log();
            }
        }
        String name = inputParams.get("Name");
        String email = inputParams.get("Email");
        String tin = inputParams.get("Tin");
        String orgId = inputParams.get("id");

        Map<String, String> input = new HashMap<>();

        if (StringUtils.isNotBlank(orgId)) {
            input.put("_orgId", orgId);
        } else {
            input.put("_orgId", "");
        }
        if (StringUtils.isNotBlank(name)) {
            input.put("_orgName", name);
        } else {
            input.put("_orgName", "");
        }
        if (StringUtils.isNotBlank(email)) {
            input.put("_orgEmail", email);
        } else {
            input.put("_orgEmail", "");
        }
        if (StringUtils.isNotBlank(tin)) {
            input.put("_orgTaxId", tin);
        } else {
            input.put("_orgTaxId", "");
        }

        Result rs = new Result();
        try {
            rs = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANIZATION_DETAILS_GET_PROC);
        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        ;
        if (StringUtils.isNotBlank(inputParams.get("id"))) {
            result = postProcess(rs, dcRequest, inputParams);
        } else {
            result = postProcess(rs, dcRequest);
        }

        return result;
    }

    private Result postProcess(Result organisations, DataControllerRequest dcRequest, Map<String, String> inputParams) {

        Result result = new Result();
        Dataset orgDetails = new Dataset("organisationdetails");
        Dataset orgOwnDetails = new Dataset("organisationownerdetails");
        Record org = null;
        if (HelperMethods.hasRecords(organisations)) {
            for (Record organisationRecord : organisations.getAllDatasets().get(0).getAllRecords()) {
                String orgId = organisationRecord.getParam("id").getValue();
                if (StringUtils.isNotBlank(orgId)) {
                    org = getRecord(organisationRecord, dcRequest);
                    orgDetails.addRecord(org);
                    if (StringUtils.isNotBlank(organisationRecord.getParamValueByName("orgStatus"))
                            && (DBPUtilitiesConstants.ORG_STATUS_PENDING
                                    .equals(organisationRecord.getParamValueByName("orgStatus"))
                                    || DBPUtilitiesConstants.ORG_STATUS_REJECTED
                                            .equals(organisationRecord.getParamValueByName("orgStatus")))) {
                        orgOwnDetails.addRecord(getRecordOwnerDetails(organisationRecord, dcRequest));
                    }
                }

            }
        }
        result.addDataset(orgDetails);
        result.addDataset(orgOwnDetails);
        return result;
    }

    private Result postProcess(Result organisations, DataControllerRequest dcRequest) {
        Result result = new Result();
        Dataset orgDetails = new Dataset("organisationdetails");

        Record org = null;
        if (HelperMethods.hasRecords(organisations)) {
            for (Record organisationRecord : organisations.getAllDatasets().get(0).getAllRecords()) {
                String orgId = organisationRecord.getParam("id").getValue();
                if (StringUtils.isNotBlank(orgId)) {

                    if (StringUtils.isNotBlank(organisationRecord.getParamValueByName("orgStatus"))
                            && (DBPUtilitiesConstants.ORG_STATUS_PENDING
                                    .equals(organisationRecord.getParamValueByName("orgStatus"))
                                    || DBPUtilitiesConstants.ORG_STATUS_REJECTED
                                            .equals(organisationRecord.getParamValueByName("orgStatus")))) {
                        continue;
                    }
                    org = getRecord(organisationRecord, dcRequest);
                    orgDetails.addRecord(org);
                }

            }
        }

        result.addDataset(orgDetails);
        return result;
    }

    private Record getRecordOwnerDetails(Record rs, DataControllerRequest dcRequest) {

        Result result = new Result();
        String id = rs.getParamValueByName("id");
        String filter = "orgemp_orgid" + DBPUtilitiesConstants.EQUAL + id + DBPUtilitiesConstants.AND
                + "isAuthSignatory" + DBPUtilitiesConstants.EQUAL + "true" + DBPUtilitiesConstants.AND + "isOwner"
                + DBPUtilitiesConstants.EQUAL + "true";
        try {
            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONEMPLOYEEVIEW_GET);
        } catch (HttpCallException e) {
           alert.prepareError(e.toString()).log();
        }

        Record rec = new Record();

        rec.addParam(new Param("FirstName", HelperMethods.getFieldValue(result, "FirstName")));
        rec.addParam(new Param("MiddleName", HelperMethods.getFieldValue(result, "MiddleName")));
        rec.addParam(new Param("LastName", HelperMethods.getFieldValue(result, "LastName")));
        rec.addParam(new Param("DOB", HelperMethods.getFieldValue(result, "DateOfBirth")));
        rec.addParam(new Param("Ssn", HelperMethods.getFieldValue(result, "Ssn")));
        rec.addParam(new Param("CustomerId", HelperMethods.getFieldValue(result, "customer_id")));
        return rec;
    }

    private Record getRecord(Record rec, DataControllerRequest dcRequest) {
        String orgId = rec.getParam("id").getValue();

        String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;

        Result result = new Result();
        try {

            result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ORGANISATIONMEMBERSHIP_GET);

        } catch (HttpCallException e) {
            alert.prepareError(e.getMessage()).log();
        }
        if (HelperMethods.hasRecords(result)) {
            StringBuilder taxId = new StringBuilder();
            for (Record orgmemberDetails : result.getAllDatasets().get(0).getAllRecords()) {
                if (StringUtils.isNotBlank(taxId)) {
                    taxId.append(",");
                }
                taxId.append(HelperMethods.getFieldValue(orgmemberDetails, "Taxid"));
            }
            rec.addStringParam("taxId", taxId.toString());
        }
        return rec;
    }

}
