package com.kony.dbputilities.extaccountservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.util.ErrorCodeEnum;
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

public class GetExternalAccounts implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    /*
     * @SuppressWarnings("rawtypes")
     * 
     * @Override public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
     * DataControllerResponse dcResponse) throws Exception { Result result = new Result(); Map inputParams =
     * HelperMethods.getInputParamMap(inputArray);
     * 
     * if (preProcess(inputParams, dcRequest, result)) { result =
     * HelperMethods.callApi(inputParams,HelperMethods.getHeaders(dcRequest), URLConstants.EXT_ACCOUNTS_GET); }
     * 
     * return result; }
     */

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
    	return ErrorCodeEnum.ERR_99999.setErrorCode(new Result());
    }
    private PreparedStatement getStatement(Map<String, String> inputParams, DataControllerRequest dcRequest, Connection con) {
        String searchString = inputParams.get("searchString");
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        StringBuilder sb = new StringBuilder();
        sb.append("select * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
                + ".externalaccount where ");
        sb.append("User_id = ?");
        sb.append(" and ");
        sb.append("softDelete = '0'");
        if (StringUtils.isNotBlank(searchString)) {
            searchString = "%" + searchString + "%";
            sb.append(" and (");
            sb.append("nickName like " + "?");
            sb.append(" or ");
            sb.append("firstName like " + "?");
            sb.append(" or ");
            sb.append("lastName like " + "?");
            sb.append(" or ");
            sb.append("beneficiaryName like " + "?");
            sb.append(" or ");
            sb.append("bankName like " + "?");
            sb.append(" or ");
            sb.append("IBAN like " + "?");
            sb.append(" or ");
            sb.append("sortCode like " + "?");
            sb.append(" or ");
            sb.append("accountNumber like " + "?");
            sb.append(")");
        }
        String query = sb.toString();
        PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(query);
		} catch (SQLException e) {

			alert.prepareError(e.getMessage()).log();
		}
		try {
			if (statement != null) {
				statement.setString(1, userId);
				if (StringUtils.isNotBlank(searchString)) {
					statement.setString(2, searchString);
					statement.setString(3, searchString);
					statement.setString(4, searchString);
					statement.setString(5, searchString);
					statement.setString(6, searchString);
					statement.setString(7, searchString);
					statement.setString(8, searchString);
					statement.setString(9, searchString);
				}
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
        return statement;
    }
    private String getQuery(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        String searchString = inputParams.get("searchString");
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        StringBuilder sb = new StringBuilder();
        sb.append("select * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
                + ".externalaccount where ");
        sb.append("User_id = '" + userId + "'");
        sb.append(" and ");
        sb.append("softDelete = '0'");
        if (StringUtils.isNotBlank(searchString)) {
            searchString = "'%" + searchString + "%'";
            sb.append(" and (");
            sb.append("nickName like " + searchString);
            sb.append(" or ");
            sb.append("firstName like " + searchString);
            sb.append(" or ");
            sb.append("lastName like " + searchString);
            sb.append(" or ");
            sb.append("beneficiaryName like " + searchString);
            sb.append(" or ");
            sb.append("bankName like " + searchString);
            sb.append(" or ");
            sb.append("IBAN like " + searchString);
            sb.append(" or ");
            sb.append("sortCode like " + searchString);
            sb.append(" or ");
            sb.append("accountNumber like " + searchString);
            sb.append(")");
        }
        return sb.toString();
    }

    private Result postProcess(ResultSet rs) throws SQLException {
        Result result = new Result();
        Dataset extAccounts = new Dataset("externalaccount");
        Record accnt = null;
        while (rs.next()) {
            accnt = getRecord(rs);
            extAccounts.addRecord(accnt);
        }
        result.addDataset(extAccounts);
        return result;
    }

    private Record getRecord(ResultSet rs) throws SQLException {
        Record rec = new Record();
        rec.addParam(new Param("Id", rs.getString("Id"), MWConstants.STRING));
        rec.addParam(new Param("accountNumber", rs.getString("accountNumber"), MWConstants.STRING));
        rec.addParam(new Param("nickName", rs.getString("nickName"), MWConstants.STRING));
        rec.addParam(new Param("firstName", rs.getString("firstName"), MWConstants.STRING));
        rec.addParam(new Param("lastName", rs.getString("lastName"), MWConstants.STRING));
        rec.addParam(new Param("routingNumber", rs.getString("routingNumber"), MWConstants.STRING));
        rec.addParam(new Param("accountType", rs.getString("accountType"), MWConstants.STRING));
        rec.addParam(new Param("notes", rs.getString("notes"), MWConstants.STRING));
        rec.addParam(new Param("countryName", rs.getString("countryName"), MWConstants.STRING));
        rec.addParam(new Param("swiftCode", rs.getString("swiftCode"), MWConstants.STRING));
        rec.addParam(new Param("beneficiaryName", rs.getString("beneficiaryName"), MWConstants.STRING));
        rec.addParam(new Param("isInternationalAccount", String.valueOf(rs.getBoolean("isInternationalAccount")),
                MWConstants.STRING));
        rec.addParam(new Param("bankName", rs.getString("bankName"), MWConstants.STRING));
        rec.addParam(
                new Param("isSameBankAccount", String.valueOf(rs.getBoolean("isSameBankAccount")), MWConstants.STRING));
        rec.addParam(new Param("softDelete", rs.getString("softDelete"), MWConstants.STRING));
        rec.addParam(new Param("isVerified", String.valueOf(rs.getBoolean("isVerified")), MWConstants.STRING));
        rec.addParam(new Param("createdOn", rs.getString("createdOn"), MWConstants.STRING));
        rec.addParam(new Param("IBAN", rs.getString("IBAN"), MWConstants.STRING));
        rec.addParam(new Param("addressNickName", rs.getString("addressNickName"), MWConstants.STRING));
        rec.addParam(new Param("addressLine1", rs.getString("addressLine1"), MWConstants.STRING));
        rec.addParam(new Param("city", rs.getString("city"), MWConstants.STRING));
        rec.addParam(new Param("zipcode", rs.getString("zipcode"), MWConstants.STRING));
        rec.addParam(new Param("country", rs.getString("country"), MWConstants.STRING));

        return rec;
    }

    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) private boolean preProcess(Map inputParams, DataControllerRequest
     * dcRequest, Result result) { boolean status = true; String searchStr = (String) inputParams
     * .get(DBPInputConstants.SEARCH_STR); String userId = HelperMethods.getUserIdFromSession(dcRequest); StringBuilder
     * sb = new StringBuilder(); sb.append(DBPUtilitiesConstants.USER_ID)
     * .append(DBPUtilitiesConstants.EQUAL).append(userId); sb.append(DBPUtilitiesConstants.AND);
     * sb.append(DBPUtilitiesConstants.SOFT_DEL) .append(DBPUtilitiesConstants.EQUAL).append("0"); if
     * (StringUtils.isNotBlank(searchStr)) { searchStr = "'" + searchStr + "'"; sb.append(DBPUtilitiesConstants.AND);
     * sb.append("("); sb.append(DBPUtilitiesConstants.F_NAME) .append(DBPUtilitiesConstants.EQUAL).append(searchStr);
     * sb.append(DBPUtilitiesConstants.OR); sb.append(DBPUtilitiesConstants.L_NAME)
     * .append(DBPUtilitiesConstants.EQUAL).append(searchStr); sb.append(DBPUtilitiesConstants.OR);
     * sb.append(DBPUtilitiesConstants.N_NAME) .append(DBPUtilitiesConstants.EQUAL).append(searchStr);
     * sb.append(DBPUtilitiesConstants.OR); sb.append("beneficiaryName")
     * .append(DBPUtilitiesConstants.EQUAL).append(searchStr); sb.append(")"); }
     * inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
     * 
     * return status; }
     */
}
