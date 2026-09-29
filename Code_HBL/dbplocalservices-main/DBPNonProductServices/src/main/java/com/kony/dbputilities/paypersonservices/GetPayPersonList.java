package com.kony.dbputilities.paypersonservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
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

public class GetPayPersonList implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    /*
     * @Override public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
     * DataControllerResponse dcResponse) throws Exception { Result result = new Result(); Map<String, String>
     * inputParams = HelperMethods.getInputParamMap(inputArray);
     * 
     * if (preProcess(inputParams, dcRequest, result)) { result =
     * HelperMethods.callApi(inputParams,HelperMethods.getHeaders(dcRequest), URLConstants.PAYPERSON_GET); }
     * 
     * return result; }
     */

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
		Result result = null;
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		
		try (Connection con = DBManager.getConnection(dcRequest);
				PreparedStatement pstmt = getStatement(inputParams, dcRequest, con);
				ResultSet rs = pstmt.executeQuery();) {
			result = postProcess(rs);
		} catch (SQLException e) {

			alert.prepareError(e.getMessage()).log();
		}

		return result;
    }

	private PreparedStatement getStatement(Map<String, String> inputParams, DataControllerRequest dcRequest,
			Connection con) {
		String jdbcUrl = QueryFormer.getDBType(dcRequest);
		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		String searchString = inputParams.get("searchString");
		String limit = inputParams.get("limit");
		String offset = inputParams.get("offset");
		String sortBy = inputParams.get("sortBy");
		String order = inputParams.get("order");
		String id = inputParams.get("id");
		StringBuilder sb = new StringBuilder();
		/*
		 * sb.append("select * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".payperson where "); sb.append("User_id = '" + userId + "'");
		 * sb.append(" and "); sb.append("isSoftDelete = '0'");
		 */
		if (StringUtils.isNotBlank(id)) {
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetPayPersonList_id").getQuery()
					.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", "?"));// id -1
		} else {
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetPayPersonList").getQuery()
					.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", "?"));// userId-1
		}

		if (StringUtils.isNotBlank(searchString)) {
			searchString = "%" + searchString + "%";
			/*
			 * sb.append(" and ("); sb.append("nickName like " + searchString);
			 * sb.append(" or "); sb.append("firstName like " + searchString);
			 * sb.append(" or "); sb.append("lastName like " + searchString);
			 * sb.append(" or "); sb.append("name like " + searchString); sb.append(")");
			 */
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetPayPersonList_search").getQuery().replace("?1", "?"));// 4
		}
		if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
			// sb.append(" order by " + sortBy + " " + order);
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetPayPersonList_order").getQuery().replace("?1", sortBy)
					.replace("?2", order));
		}
		if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetPayPersonList_limit").getQuery().replace("?1", offset)
					.replace("?2", limit));
			// sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_getTrans_limit").getQuery());
			// sb.append(" limit " + offset + ", " + limit);
		}
		String query = sb.toString();
		query = query.replace("'?'", "?");
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(query);
		} catch (SQLException e) {

			alert.prepareError(e.getMessage()).log();
		}
		try {
			if (statement != null) {
				if (StringUtils.isNotBlank(id)) {
					statement.setString(1, id);
				} else {
					statement.setString(1, userId);
				}
				if (StringUtils.isNotBlank(searchString)) {
					statement.setString(2, searchString);
					statement.setString(3, searchString);
					statement.setString(4, searchString);
					statement.setString(5, searchString);
				}
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}
		return statement;

	}
    private String getQuery(Map<String, String> inputParams, DataControllerRequest dcRequest) {
    	String jdbcUrl=QueryFormer.getDBType(dcRequest);
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String searchString = inputParams.get("searchString");
        String limit = inputParams.get("limit");
        String offset = inputParams.get("offset");
        String sortBy = inputParams.get("sortBy");
        String order = inputParams.get("order");
        String id = inputParams.get("id");
        StringBuilder sb = new StringBuilder();
		/*
		 * sb.append("select * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) + ".payperson where "); sb.append("User_id = '" + userId + "'");
		 * sb.append(" and "); sb.append("isSoftDelete = '0'");
		 */
        if (StringUtils.isNotBlank(id)) {
            sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayPersonList_id").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,dcRequest)).replace("?2", id));
        }
        else {
        	sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayPersonList").getQuery().replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,dcRequest)).replace("?2", userId));
        }
        
        if (StringUtils.isNotBlank(searchString)) {
            searchString = "'%" + searchString + "%'";
			/*
			 * sb.append(" and ("); sb.append("nickName like " + searchString);
			 * sb.append(" or "); sb.append("firstName like " + searchString);
			 * sb.append(" or "); sb.append("lastName like " + searchString);
			 * sb.append(" or "); sb.append("name like " + searchString); sb.append(")");
			 */
            sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayPersonList_search").getQuery().replace("?1", searchString));
        }
        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
            //sb.append(" order by " + sortBy + " " + order);
        	sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayPersonList_order").getQuery().replace("?1", sortBy).replace("?2", order));
        }
        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
        	sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetPayPersonList_limit").getQuery().replace("?1", offset).replace("?2", limit));
			//sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_getTrans_limit").getQuery());
        	//sb.append(" limit " + offset + ", " + limit);
        }
        return sb.toString();
    }

    private Result postProcess(ResultSet rs) throws SQLException {
        Result result = new Result();
        Dataset extAccounts = new Dataset("payperson");
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
        rec.addParam(new Param("id", rs.getString("id"), MWConstants.STRING));
        rec.addParam(new Param("firstName", rs.getString("firstName"), MWConstants.STRING));
        rec.addParam(new Param("lastName", rs.getString("lastName"), MWConstants.STRING));
        rec.addParam(new Param("phone", rs.getString("phone"), MWConstants.STRING));
        rec.addParam(new Param("email", rs.getString("email"), MWConstants.STRING));
        rec.addParam(new Param("secondaryEmail", rs.getString("secondaryEmail"), MWConstants.STRING));
        rec.addParam(new Param("secondoryPhoneNumber", rs.getString("secondoryPhoneNumber"), MWConstants.STRING));
        rec.addParam(new Param("secondaryEmail2", rs.getString("secondaryEmail2"), MWConstants.STRING));
        rec.addParam(new Param("secondaryPhoneNumber2", rs.getString("secondaryPhoneNumber2"), MWConstants.STRING));
        rec.addParam(
                new Param("primaryContactForSending", rs.getString("primaryContactForSending"), MWConstants.STRING));
        rec.addParam(new Param("nickName", rs.getString("nickName"), MWConstants.STRING));
        rec.addParam(new Param("name", rs.getString("name"), MWConstants.STRING));
        rec.addParam(new Param("isSoftDelete", String.valueOf(rs.getBoolean("isSoftDelete")), MWConstants.STRING));
        return rec;
    }

    /*
     * private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result) {
     * boolean status = true; String userId = HelperMethods.getUserIdFromSession(dcRequest); String searchString =
     * inputParams.get("searchString"); String limit = inputParams.get("limit"); String offset =
     * inputParams.get("offset"); String sortBy = inputParams.get("sortBy"); String order = inputParams.get("order");
     * StringBuilder sb = new StringBuilder();
     * 
     * sb.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId); sb.append(DBPUtilitiesConstants.AND);
     * sb.append("isSoftDelete").append(DBPUtilitiesConstants.EQUAL) .append("0"); if
     * (StringUtils.isNotBlank(searchString)) { searchString = "'" + searchString + "'";
     * sb.append(DBPUtilitiesConstants.AND); sb.append("("); sb.append("nickName").append(DBPUtilitiesConstants.EQUAL)
     * .append(searchString); sb.append(DBPUtilitiesConstants.OR);
     * sb.append("firstName").append(DBPUtilitiesConstants.EQUAL) .append(searchString);
     * sb.append(DBPUtilitiesConstants.OR); sb.append("lastName").append(DBPUtilitiesConstants.EQUAL)
     * .append(searchString); sb.append(DBPUtilitiesConstants.OR); sb.append("name").append(DBPUtilitiesConstants.EQUAL)
     * .append(searchString); sb.append(")"); }else if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit))
     * { inputParams.put(DBPUtilitiesConstants.SKIP, offset); inputParams.put(DBPUtilitiesConstants.TOP, limit); }
     * if(StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)){
     * inputParams.put(DBPUtilitiesConstants.ORDERBY, sortBy+ " "+ order); }
     * inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString()); return status; }
     */
}
