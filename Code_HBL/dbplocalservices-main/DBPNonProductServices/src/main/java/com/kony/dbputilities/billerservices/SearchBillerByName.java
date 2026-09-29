package com.kony.dbputilities.billerservices;

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

public class SearchBillerByName implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /*
     * public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
     * DataControllerResponse dcResponse) throws Exception { Result result = new Result(); Map inputParams =
     * HelperMethods.getInputParamMap(inputArray); if(preProcess(inputParams, dcRequest, result)){ result =
     * HelperMethods.callApi(inputParams,HelperMethods.getHeaders(dcRequest), URLConstants.BILL_MASTER_GET); }
     * if(HelperMethods.hasRecords(result)){ postProcess(dcRequest, result); } return result; }
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
		} catch (Exception e) {

			alert.prepareError(e.getMessage()).log();
		}

		return result;
	}

    /*
     * private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException { List<Record>
     * billerMasters = result.getAllDatasets().get(0).getAllRecords(); for (Record billerMaster : billerMasters) {
     * String billerCategoryId = HelperMethods.getFieldValue(billerMaster, "billerCategoryId");
     * billerMaster.addParam(new Param("billerCategoryName", getCategoryName(dcRequest, billerCategoryId),
     * DBPUtilitiesConstants.STRING_TYPE)); } }
     */

    private Result postProcess(ResultSet rs) throws SQLException {
        Result result = new Result();
        Dataset billers = new Dataset("billermaster");
        Record biller = null;
        while (rs.next()) {
            biller = getRecord(rs);
            billers.addRecord(biller);
        }
        result.addDataset(billers);
        return result;
    }

	private PreparedStatement getStatement(Map<String, String> inputParams, DataControllerRequest dcRequest, Connection con) {
		String searchString = inputParams.get("searchString");
		// Validate Biller Name
		String pattern = "^[a-zA-Z0-9\\s_-]+$";
		if (!searchString.matches(pattern)) {
			return null;
		}
		searchString = "%" + searchString + "%";
		String limit = inputParams.get("limit");
		if (StringUtils.isBlank(limit)) {
			limit = "10";
		}
		String dbType = QueryFormer.getDBType(dcRequest);
		String query = SqlQueryEnum.valueOf(dbType + "_SearchBillerByName").getQuery();
		query = query.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", "?")
				.replace("?3", limit);
		alert.prepareError(query).log();
		/*
		 * String query = "select bm.*,bc.categoryName from " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".billermaster bm, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) +
		 * ".billercategory bc where bc.id = bm.billerCategoryId and bm.billerName like "
		 * + searchString + " limit " + limit;
		 */
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(query);
		} catch (SQLException e) {

			alert.prepareError(e.getMessage()).log();
		}
		try {
			if (StringUtils.isNotBlank(searchString)) {
				statement.setString(1, searchString);
			}
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		}
		return statement;
	}
    /*
     * private String getCategoryName(DataControllerRequest dcRequest, String billerCategoryId) throws HttpCallException
     * { String filter = "id" + DBPUtilitiesConstants.EQUAL + billerCategoryId; Result billerMaster =
     * HelperMethods.callGetApi(filter, HelperMethods.getHeaders(dcRequest), URLConstants.BILL_CATEGORY_GET); return
     * HelperMethods.getFieldValue(billerMaster, "cattegoryName"); }
     */

    /*
     * @SuppressWarnings({ "unchecked", "rawtypes" }) private boolean preProcess(Map inputParams, DataControllerRequest
     * dcRequest, Result result) { String searchString = (String)inputParams.get("searchString"); searchString = "'" +
     * searchString + "'"; String limit = (String)inputParams.get("limit"); String filter = "billerName" +
     * DBPUtilitiesConstants.EQUAL + searchString; inputParams.put(DBPUtilitiesConstants.FILTER, filter);
     * if(StringUtils.isNotBlank(limit)){ inputParams.put(DBPUtilitiesConstants.TOP, limit); } return true; }
     */
//added a new parameter to receive jdbcUrl
    private String getQuery(Map<String, String> inputParams, DataControllerRequest dcRequest) {
        String searchString = inputParams.get("searchString");
        //Validate Biller Name
        String pattern= "^[a-zA-Z0-9\\s_-]+$";
        if(!searchString.matches(pattern)){
            return "";
        }
        searchString = "'%" + searchString + "%'";
        String limit = inputParams.get("limit");
        if (StringUtils.isBlank(limit)) {
            limit = "10";
        }
        String dbType=QueryFormer.getDBType(dcRequest);
        String query=SqlQueryEnum.valueOf(dbType+"_SearchBillerByName").getQuery();
        query = query.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)).replace("?2", searchString).replace("?3", limit);
        alert.prepareError(query).log();
		/*
		 * String query = "select bm.*,bc.categoryName from " +
		 * URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) +
		 * ".billermaster bm, " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,
		 * dcRequest) +
		 * ".billercategory bc where bc.id = bm.billerCategoryId and bm.billerName like "
		 * + searchString + " limit " + limit;
		 */
        return query;
    }

    private Record getRecord(ResultSet rs) throws SQLException {
        Record biller = new Record();
        biller.addParam(new Param("billerName", rs.getString("billerName"), MWConstants.STRING));
        biller.addParam(new Param("accountNumber", rs.getString("accountNumber"), MWConstants.STRING));
        biller.addParam(new Param("zipCode", rs.getString("zipCode"), MWConstants.STRING));
        biller.addParam(new Param("mobileNumber", rs.getString("mobileNumber"), MWConstants.STRING));
        biller.addParam(new Param("phoneNumber", rs.getString("phoneNumber"), MWConstants.STRING));
        biller.addParam(new Param("address", rs.getString("address"), MWConstants.STRING));
        biller.addParam(new Param("relationshipNumber", rs.getString("relationshipNumber"), MWConstants.STRING));
        biller.addParam(new Param("policyNumber", rs.getString("policyNumber"), MWConstants.STRING));
        biller.addParam(new Param("city", rs.getString("city"), MWConstants.STRING));
        biller.addParam(new Param("state", rs.getString("state"), MWConstants.STRING));
        biller.addParam(new Param("ebillSupport", String.valueOf(rs.getBoolean("ebillSupport")), MWConstants.STRING));
        // biller.addParam(new Param("ebillSupport","true",MWConstants.STRING));
        biller.addParam(new Param("billerCategoryId", rs.getString("billerCategoryId"), MWConstants.STRING));
        biller.addParam(new Param("billerCategoryName", rs.getString("categoryName"), MWConstants.STRING));
        biller.addParam(new Param("id", String.valueOf(rs.getInt("id")), MWConstants.STRING));
        return biller;
    }
}