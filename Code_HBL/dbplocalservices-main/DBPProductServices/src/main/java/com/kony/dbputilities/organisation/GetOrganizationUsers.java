package com.kony.dbputilities.organisation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.sessionmanager.SessionScope;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganizationUsers implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

        Map<String, String> loggedInUserInfo = HelperMethods.getCustomerFromIdentityService(dcRequest);

        if (HelperMethods.isAuthenticationCheckRequiredForService(loggedInUserInfo)) {
            Set<String> userPermissions = SessionScope.getAllPermissionsFromIdentityScope(dcRequest);
            if (!userPermissions.contains("USER_MANAGEMENT_VIEW")) {
                ErrorCodeEnum.ERR_10051.setErrorCode(result);
                return result;
            }
            inputParams.put("Organization_id", HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest));
        }

        Connection connection = DBManager.getConnection(dcRequest);
        try {
            PreparedStatement statement = getStatement(dcRequest, inputParams, connection);
            ResultSet rs = statement.executeQuery();

            result = postProcess(rs, dcRequest);
            statement.close();
            rs.close();
        } catch (SQLException e) {

            alert.prepareError(e.getMessage()).log();
        }

        finally {
            try {
                connection.close();
            } catch (SQLException e) {

                alert.prepareError(e.getMessage()).log();
            }
        }

        return result;

    }

    private PreparedStatement getStatement(DataControllerRequest dcRequest, Map<String, String> inputParams,
            Connection connection) {

        String searchString = inputParams.get("searchString");
        String order = inputParams.get("order");
        String sortBy = inputParams.get("sortBy");
        String org_id = inputParams.get("Organization_id");
        String offset = inputParams.get("offset");
        String limit = inputParams.get("limit");
        if (StringUtils.isBlank(org_id)) {
            try {
                org_id = HelperMethods.getOrganizationIDfromLoggedInUser(dcRequest);
            } catch (HttpCallException e) {

                alert.prepareError(e.getMessage()).log();
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("select * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
                + ".organisationemployeesview where ");
        if (StringUtils.isBlank(org_id)) {
            sb.append("orgemp_orgid != null");
        } else {
            sb.append("orgemp_orgid = ?");
            if (StringUtils.isNotBlank(searchString)) {
                searchString = "%" + searchString + "%";
                sb.append(" and (");
                sb.append("UserName like ?");
                sb.append(" or ");
                sb.append("FirstName like ?");
                sb.append(" or ");
                sb.append("custcomm_value  like ?");
                sb.append(" or ");
                sb.append("role_name  like ?");
                sb.append(" or ");
                sb.append("Status_id  like ?");
                sb.append(")");
            }
            sb.append(" order by");
            if (StringUtils.isBlank(sortBy)) {
                sortBy = "createdts";
            }
            if (sortBy.equals("status")) {
                sortBy = "Status_id";
            }
            sb.append(" ").append(sortBy).append(" ");
            if (StringUtils.isBlank(order)) {
                order = " asc ";
            }
            sb.append(order);
            if (StringUtils.isNotBlank(limit) && StringUtils.isNotBlank(offset)) {
				String jdbcUrl=QueryFormer.getDBType(dcRequest);
				sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetOrganizationUsers_Limit").getQuery());
				/*
				 * sb.append(" limit ? "); sb.append(" offset ?");
				 */
            }
        }

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sb.toString());
            if (StringUtils.isNotBlank(org_id)) {
                statement.setString(1, org_id);
                if (StringUtils.isNotBlank(searchString)) {
                    statement.setString(2, searchString);
                    statement.setString(3, searchString);
                    statement.setString(4, searchString);
                    statement.setString(5, searchString);
                    statement.setString(6, searchString);
                    if (StringUtils.isNotBlank(limit) && StringUtils.isNotBlank(offset)) {
                        statement.setString(7, offset);
                        statement.setString(8, limit);
                    }
                } else {
                    if (StringUtils.isNotBlank(limit) && StringUtils.isNotBlank(offset)) {
                        statement.setString(2, offset);
                        statement.setString(3, limit);
                    }
                }
            }
        } catch (SQLException e) {

            alert.prepareError(e.getMessage()).log();
        }

        return statement;
    }

    private Result postProcess(ResultSet rs, DataControllerRequest dcRequest) throws SQLException, HttpCallException {

        Result result = new Result();
        Dataset orgDetails = new Dataset("organizationEmployee");
        Record org = null;
        ArrayList<String> list = new ArrayList<>();

        while (rs.next()) {
            if (recordNotExists(list, rs)) {
                org = getRecord(rs, dcRequest);
                orgDetails.addRecord(org);
            }
        }
        result.addDataset(orgDetails);

        return errorCodeAssignment(result);
    }

    private boolean recordNotExists(ArrayList<String> list, ResultSet rs) throws SQLException {
        if (list.contains(rs.getString("customer_id"))) {
            return false;
        }
        list.add(rs.getString("customer_id"));
        return true;
    }

    private Record getRecord(ResultSet rs, DataControllerRequest dcRequest) throws SQLException, HttpCallException {

        Record rec = new Record();
        String id = rs.getString("customer_id");
        if (id.equalsIgnoreCase(rs.getString("UserName"))
                && DBPUtilitiesConstants.CUSTOMER_STATUS_NEW.equalsIgnoreCase(rs.getString("Status_id"))) {
            rec.addParam(new Param("UserName", "N/A", MWConstants.STRING));
        } else {
            rec.addParam(new Param("UserName", rs.getString("UserName"), MWConstants.STRING));
        }
        rec.addParam(new Param("FirstName", rs.getString("FirstName"), MWConstants.STRING));
        rec.addParam(new Param("MiddleName", rs.getString("MiddleName"), MWConstants.STRING));
        rec.addParam(new Param("LastName", rs.getString("LastName"), MWConstants.STRING));
        rec.addParam(new Param("id", rs.getString("customer_id"), MWConstants.STRING));
        rec.addParam(new Param("status", getStatus(rs.getString("Status_id")), MWConstants.STRING));
        rec.addParam(new Param("statusId", rs.getString("Status_id"), MWConstants.STRING));
        rec.addParam(new Param("createdts", rs.getString("createdts"), MWConstants.STRING));
        rec.addParam(new Param("Lastlogintime", rs.getString("Lastlogintime"), MWConstants.STRING));
        rec.addParam(new Param("role_name", rs.getString("role_name"), MWConstants.STRING));
        rec.addParam(new Param("DrivingLicenseNumber", maskData(rs, "DrivingLicenseNumber"), MWConstants.STRING));
        rec.addParam(new Param("DateOfBirth", rs.getString("DateOfBirth"), MWConstants.STRING));
        rec.addParam(
                new Param("isAuthSignatory", String.valueOf(rs.getBoolean("isAuthSignatory")), MWConstants.STRING));
        rec.addParam(new Param("signatorytypeId", rs.getString("signatorytypeId"), MWConstants.STRING));
        rec.addParam(new Param("signatorytypeName", rs.getString("signatorytypeName"), MWConstants.STRING));
        rec.addParam(new Param("Ssn", maskData(rs, "Ssn"), MWConstants.STRING));
        rec.addParam(new Param("Group_id", rs.getString("Group_id"), MWConstants.STRING));
        rec.addParam(new Param("role_name", rs.getString("role_name"), MWConstants.STRING));
        if (rs.getString("createdby") != null && StringUtils.isNotBlank(rs.getString("createdby"))) {
            rec.addParam(new Param("createdby", rs.getString("createdby"), MWConstants.STRING));
        }

        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + id;
        Result result = new Result();
        addFlags(id, dcRequest, rec);
        result = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.CUSTOMER_COMMUNICATION_GET);

        List<Record> commTypes = result.getAllDatasets().get(0).getAllRecords();

        for (Record commType : commTypes) {
            String type = HelperMethods.getFieldValue(commType, "Type_id");

            if (DBPUtilitiesConstants.COMM_TYPE_PHONE.equalsIgnoreCase(type)) {
                rec.addParam(new Param("Phone", HelperMethods.getFieldValue(commType, "Value"), MWConstants.STRING));
            }
            if (DBPUtilitiesConstants.COMM_TYPE_EMAIL.equalsIgnoreCase(type)) {
                rec.addParam(new Param("Email", HelperMethods.getFieldValue(commType, "Value"), MWConstants.STRING));
            }
        }

        return rec;
    }

    public String maskData(ResultSet rs, String name) {
        String value = null;
        try {
            value = rs.getString(name);
        } catch (SQLException e) {
            alert.prepareError("Exception while fetching colum :" + name).log();
        }
        if (StringUtils.isBlank(value))
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append(value.charAt(0));
        sb.append("XXXXXXX");
        sb.append(value.charAt(value.length() - 1));
        return sb.toString();

    }

    private void addFlags(String employeId, DataControllerRequest dcRequest, Record empRecord) {
        String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + employeId;
        try {
            Result customerFlagStatus = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CUSTOMERFLAG_GET);
            String list = "";
            if (HelperMethods.hasRecords(customerFlagStatus)) {
                for (Record record : customerFlagStatus.getAllDatasets().get(0).getAllRecords()) {
                    if (!list.isEmpty()) {
                        list += ",";
                    }
                    list += record.getParamValueByName("Status_id");
                }
            }

            empRecord.addParam(new Param("RiskStatus", list));
        } catch (HttpCallException e) {

            alert.prepareError(e.getMessage()).log();
        }

    }

    private String getStatus(String statusId) throws HttpCallException {
        Map<String, String> map = HelperMethods.getCustomerStatus();

        for (String status : map.keySet()) {
            String value = map.get(status);
            if (value.equals(statusId)) {
                return status;
            }
        }
        return null;
    }

    private Result errorCodeAssignment(Result result) {

        if (HelperMethods.hasRecords(result)) {

            Param p = new Param("Status", HelperMethods.getFieldValue(result, "Status"),
                    DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(p);

            p = new Param("success", "Records exists", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(p);
        } else if (HelperMethods.hasError(result)) {
            Param p = new Param("errorCode", ErrorCodes.ERROR_SEARCHING_RECORD, DBPUtilitiesConstants.STRING_TYPE);
            result.addParam(p);
            p = new Param("errorMessage", HelperMethods.getError(result), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(p);
        } else {
            Param p = new Param("errorCode", ErrorCodes.RECORD_FOUND, DBPUtilitiesConstants.STRING_TYPE);
            result.addParam(p);
            p = new Param("success", "Records doesn't exists", DBPConstants.FABRIC_STRING_CONSTANT_KEY);
            result.addParam(p);

        }

        return result;
    }

}