package com.kony.dbputilities.payeeservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
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

public class GetWireTransferRecipient implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    /*
     * @SuppressWarnings("rawtypes")
     * 
     * @Override public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
     * DataControllerResponse dcResponse) throws Exception { Result result = new Result(); Map inputParams =
     * HelperMethods.getInputParamMap(inputArray); if(preProcess(inputParams, dcRequest, result)){ result =
     * HelperMethods.callApi(inputParams, HelperMethods.getHeaders(dcRequest), URLConstants.PAYEE_GET); } return result;
     * }
     */

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) {
        Result result = null;
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		try {
			if (preProcess(dcRequest)) {
				
				try (Connection connection = DBManager.getConnection(dcRequest);
						PreparedStatement stmt = getStatement(inputParams, dcRequest, connection);
						ResultSet rs = stmt.executeQuery();) {
					result = postProcess(rs);
				} catch (Exception e) {
					alert.prepareError(e.getMessage()).log();
				}
			}
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
        
        return result;
    }

    private boolean preProcess(DataControllerRequest dcRequest) throws HttpCallException {
        String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        return !StringUtils.isBlank(customerId);
    }
    private PreparedStatement getStatement(Map<String, String> inputParams, DataControllerRequest dcRequest,Connection con) {
    	String searchString = inputParams.get("searchString");
        String limit = inputParams.get("limit");
        String offset = inputParams.get("offset");
        String sortBy = inputParams.get("sortBy");
        String order = inputParams.get("order");
        String payeeId = inputParams.get("id");
        String userFilter = "";
        String userId = inputParams.get("customerId");
        String companyId = inputParams.get("companyId");
        String jdbcUrl = QueryFormer.getDBType(dcRequest);
        String schema = URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest);
        if(StringUtils.isNotBlank(userId)) {
        	userFilter = DBPUtilitiesConstants.P_USER_ID + " = ? ";//1
        }
        
        
        if(StringUtils.isNotBlank(companyId)) {
        	if(StringUtils.isNotBlank(userFilter)) {
        		userFilter = userFilter + DBPUtilitiesConstants.OR;
        	}
    		userFilter = userFilter + DBPUtilitiesConstants.P_ORGANIZATION_ID + " = ? ";//2 checkcondition
        }
        if(StringUtils.isBlank(userFilter)) {
        	userFilter = DBPUtilitiesConstants.P_USER_ID + " = " + HelperMethods.getUserIdFromSession(dcRequest);
        }
        userFilter = "(" + userFilter + ")";
        if (StringUtils.isBlank(order)) {
            order = "asc";
        }
        
        if (StringUtils.isBlank(sortBy)) {
			order = VariableUtils.quote("nickName", dcRequest);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_Payee_Get").getQuery().replace("?1", schema));

		if (StringUtils.isBlank(payeeId)) {
			sb.append(userFilter);
		} else {
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_PayeeId_Get").getQuery() + "?");
		}
		sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_Payee_Where").getQuery());
		if (StringUtils.isNotBlank(searchString)) {
			searchString = "%" + searchString + "%";
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_SearchString_Get").getQuery().replace("?1", "?"));
		}
        if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
            sb.append(" order by " + sortBy + " " + order);
        }
        if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
        	sb.append(SqlQueryEnum.valueOf(jdbcUrl+"_GetUserPendingTransactions_getTrans_limit").getQuery().replace("?1", offset).replace("?2", limit));
        }        
        String query = sb.toString();
        PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(query);
		} catch (SQLException e) {

			alert.prepareError(e.getMessage()).log();
		}
		try {
			int flag = 0;
			if (statement != null) {
				if (StringUtils.isNotBlank(payeeId)) {
					statement.setString(1, payeeId);
				} else if (StringUtils.isNotBlank(userId)) {
					statement.setString(1, userId);
					if (StringUtils.isNotBlank(companyId)) {
						statement.setString(2, companyId);
						if (StringUtils.isNotBlank(companyId)) {
							if (StringUtils.isNotBlank(searchString)) {
								flag = 1;
								statement.setString(3, searchString);
								statement.setString(4, searchString);
								statement.setString(5, searchString);
								statement.setString(6, searchString);
								statement.setString(7, searchString);
								statement.setString(8, searchString);
							}
						}
					}
				} else if (StringUtils.isNotBlank(companyId)) {
					statement.setString(1, companyId);
				}

				if (StringUtils.isNotBlank(searchString) && flag!=1) {
					statement.setString(2, searchString);
					statement.setString(3, searchString);
					statement.setString(4, searchString);
					statement.setString(5, searchString);
					statement.setString(6, searchString);
					statement.setString(7, searchString);
				}

			}
		} catch (SQLException e) {
			alert.prepareError(e.toString()).log();
		}
		return statement;
    }

	private String getQuery(Map<String, String> inputParams, DataControllerRequest dcRequest) {
		String jdbcUrl = QueryFormer.getDBType(dcRequest);
		String schema = URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest);
		String searchString = inputParams.get("searchString");
		String limit = inputParams.get("limit");
		String offset = inputParams.get("offset");
		String sortBy = inputParams.get("sortBy");
		String order = inputParams.get("order");
		String payeeId = inputParams.get("id");
		String userFilter = "";
		String userId = inputParams.get("customerId");
		String companyId = inputParams.get("companyId");

		if (StringUtils.isNotBlank(userId)) {
			userFilter = DBPUtilitiesConstants.P_USER_ID + " = '" + userId + "'";
		}
		if (StringUtils.isNotBlank(companyId)) {
			if (StringUtils.isNotBlank(userFilter)) {
				userFilter = userFilter + DBPUtilitiesConstants.OR;
			}
			userFilter = userFilter + DBPUtilitiesConstants.P_ORGANIZATION_ID + " = '" + companyId + "'";
		}
		if (StringUtils.isBlank(userFilter)) {
			userFilter = DBPUtilitiesConstants.P_USER_ID + " = " + HelperMethods.getUserIdFromSession(dcRequest);
		}
		userFilter = "(" + userFilter + ")";
		if (StringUtils.isBlank(order)) {
			order = "asc";
		}
		if (StringUtils.isBlank(sortBy)) {
			order = VariableUtils.quote("nickName", dcRequest);
		}
		StringBuilder sb = new StringBuilder();
		sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_Payee_Get").getQuery().replace("?1", schema));

		if (StringUtils.isBlank(payeeId)) {
			sb.append(userFilter);
		} else {
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_PayeeId_Get").getQuery() + payeeId);
		}
		sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_Payee_Where").getQuery());
		if (StringUtils.isNotBlank(searchString)) {
			searchString = "'%" + searchString + "%'";
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_SearchString_Get").getQuery().replace("?1", searchString));
		}
		if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
			sb.append(" order by " + sortBy + " " + order);
		}
		if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
			sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetUserPendingTransactions_getTrans_limit").getQuery().replace("?1", offset).replace("?2", limit));
		}
		return sb.toString();
	}

    private Result postProcess(ResultSet rs) throws SQLException {
        Result result = new Result();
        Dataset extAccounts = new Dataset("payee");
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
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int count = rsMetaData.getColumnCount();
        String col = null;
        for (int i = 1; i <= count; i++) {
            col = rsMetaData.getColumnLabel(i);
            rec.addParam(new Param(col, rs.getString(col), MWConstants.STRING));
        }
        return rec;
    }

    /*
     * @SuppressWarnings({ "rawtypes", "unchecked" }) private boolean preProcess(Map inputParams, DataControllerRequest
     * dcRequest, Result result) { String sortBy = (String)inputParams.get("sortBy"); String order =
     * (String)inputParams.get("order"); String searchString = (String)inputParams.get("searchString"); String offset =
     * (String)inputParams.get("offset"); String limit = (String)inputParams.get("limit"); String userId =
     * HelperMethods.getUserIdFromSession(dcRequest); if(StringUtils.isBlank(order)){ order = "asc"; } StringBuilder
     * filter = new StringBuilder(); filter.append("User_id").append(DBPUtilitiesConstants.EQUAL).append(userId);
     * filter.append(DBPUtilitiesConstants.AND);
     * filter.append("softDelete").append(DBPUtilitiesConstants.EQUAL).append("0");
     * filter.append(DBPUtilitiesConstants.AND);
     * filter.append("isWiredRecepient").append(DBPUtilitiesConstants.EQUAL).append( "1");
     * 
     * if(StringUtils.isNotBlank(sortBy)){ inputParams.put(DBPUtilitiesConstants.ORDERBY, sortBy+" "+order);
     * if(StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)){ inputParams.put(DBPUtilitiesConstants.SKIP,
     * offset); inputParams.put(DBPUtilitiesConstants.TOP, limit); } }else if(StringUtils.isNotBlank(searchString)){
     * filter.append(DBPUtilitiesConstants.AND).append("(");
     * filter.append("nickName").append(DBPUtilitiesConstants.EQUAL).append( searchString);
     * filter.append(DBPUtilitiesConstants.OR); filter.append("lastName").append(DBPUtilitiesConstants.EQUAL).append(
     * searchString); filter.append(DBPUtilitiesConstants.OR);
     * filter.append("firstName").append(DBPUtilitiesConstants.EQUAL).append( searchString);
     * filter.append(DBPUtilitiesConstants.OR);
     * filter.append("name").append(DBPUtilitiesConstants.EQUAL).append(searchString );
     * filter.append(DBPUtilitiesConstants.OR); filter.append("bankName").append(DBPUtilitiesConstants.EQUAL).append(
     * searchString); filter.append(DBPUtilitiesConstants.OR);
     * filter.append("wireAccountType").append(DBPUtilitiesConstants.EQUAL).append( searchString); filter.append(")");
     * }else if(StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)){
     * inputParams.put(DBPUtilitiesConstants.ORDERBY, "nickName asc"); inputParams.put(DBPUtilitiesConstants.SKIP,
     * offset); inputParams.put(DBPUtilitiesConstants.TOP, limit); }else{ inputParams.put(DBPUtilitiesConstants.ORDERBY,
     * "nickName asc"); } inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString()); return true; }
     */
}
