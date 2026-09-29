package com.kony.dbputilities.alertservices;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

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

public class GetNotifications implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {

		Result result = new Result();

		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);

		Connection connection = DBManager.getConnection(dcRequest);
		try {
			PreparedStatement stmt = getStatement(dcRequest, inputParams, connection);
			ResultSet rs = stmt.executeQuery();
			result = postProcess(rs);
		} catch (Exception e) {
			alert.prepareError(e.getMessage()).log();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				alert.prepareError(e.getMessage()).log();
			}
		}
		return result;
	}

	private PreparedStatement getStatement(DataControllerRequest dcRequest, Map<String, String> inputParams,
			Connection con) {

		String searchString = inputParams.get("searchString");

		String userId = HelperMethods.getUserIdFromSession(dcRequest);
		Calendar cal = Calendar.getInstance();
		String toDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), null);
		cal.add(Calendar.DATE, -30);
		String fromDate = HelperMethods.getFormattedTimeStamp(cal.getTime(), null);
		String jdbcUrl=QueryFormer.getDBType(dcRequest);
		String userNotificationId = inputParams.get("userNotificationId");
		String query = "select n.* from " + 
				SqlQueryEnum.valueOf(jdbcUrl+"_NotificationView_Select").getQuery().replace("?7",URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,dcRequest));

		if (StringUtils.isNotBlank(userNotificationId)) {
			query += SqlQueryEnum.valueOf(jdbcUrl+"_userNotificationId").getQuery();

		}
		if (StringUtils.isNotBlank(searchString)) {
			searchString = searchString.replace("%", "\\%");
			searchString = searchString.replace("_", "\\_");
			searchString = "%" + searchString + "%";
			query = query + SqlQueryEnum.valueOf(jdbcUrl+"_NotificationView_where").getQuery();
		}
		query = query + SqlQueryEnum.valueOf(jdbcUrl+"_NotificationView_orderBy").getQuery();
		fromDate = fromDate.replace('T', ' ');
		toDate = toDate.replace('T', ' ');
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement(query);
		} catch (SQLException e) {

			alert.prepareError(e.getMessage()).log();
		}

		try {
			if (statement != null) {
				statement.setString(1, userId);
				statement.setString(2, fromDate);
				statement.setString(3, toDate);
				if (StringUtils.isNotBlank(userNotificationId)) {
					statement.setString(4, userNotificationId);
					if (StringUtils.isNotBlank(searchString)) {
						statement.setString(5, searchString);
						statement.setString(6, searchString);
					}
				} else {
					if (StringUtils.isNotBlank(searchString)) {
						statement.setString(4, searchString);
						statement.setString(5, searchString);
					}
				}
			}
		} catch (SQLException e) {
		}

		return statement;
	}

	private Result postProcess(ResultSet rs) throws SQLException, ParseException {
		Result result = new Result();
		Dataset billers = new Dataset("notificationview");
		Record biller = null;
		while (rs.next()) {
			biller = getRecord(rs);
			billers.addRecord(biller);
		}
		result.addDataset(billers);
		return result;
	}

	private Record getRecord(ResultSet rs) throws SQLException, ParseException {
		Record biller = new Record();
		biller.addParam(new Param("notificationId", rs.getString("notificationId"), MWConstants.STRING));
		biller.addParam(new Param("imageURL", rs.getString("imageURL"), MWConstants.STRING));
		biller.addParam(new Param("isRead", rs.getString("isRead"), MWConstants.STRING));
		biller.addParam(
				new Param("notificationActionLink", rs.getString("notificationActionLink"), MWConstants.STRING));
		biller.addParam(new Param("notificationModule", rs.getString("notificationModule"), MWConstants.STRING));
		biller.addParam(new Param("notificationSubject", rs.getString("notificationSubject"), MWConstants.STRING));
		biller.addParam(new Param("notificationSubModule", rs.getString("notificationSubModule"), MWConstants.STRING));
		biller.addParam(new Param("notificationText", rs.getString("notificationText"), MWConstants.STRING));
		biller.addParam(new Param("receivedDate",
				String.valueOf(
						HelperMethods.convertDateFormat(rs.getString("receivedDate"), "yyyy-MM-dd'T'HH:mm:ss'Z'")),
				MWConstants.STRING));
		biller.addParam(new Param("userNotificationId", rs.getString("userNotificationId"), MWConstants.STRING));
		biller.addParam(new Param("actionButtonLabelName", rs.getString("actionButtonLabelName"), MWConstants.STRING));
		biller.addParam(new Param("notificationCategory", rs.getString("notificationCategory"), MWConstants.STRING));
		return biller;
	}
}
