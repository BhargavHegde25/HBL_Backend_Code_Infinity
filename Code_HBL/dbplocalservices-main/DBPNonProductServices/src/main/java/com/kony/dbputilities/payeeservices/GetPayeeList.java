package com.kony.dbputilities.payeeservices;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.dbutil.SqlQueryEnum;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
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

public class GetPayeeList implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		try {
			Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
			if (preProcess(inputParams, dcRequest, result)) {
				if (StringUtils.isNotBlank(inputParams.get("query"))) {
					result = invokeDB(inputParams, dcRequest);
				} else {
					result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
							URLConstants.PAYEE_GET);
				}
			}
			if (HelperMethods.hasRecords(result)) {
				postProcess(dcRequest, result);
			}
		} catch (ApplicationException e) {
			e.getErrorCodeEnum().setErrorCode(result);
		}
		return result;
	}

	public Result invokeDB(Map<String, String> inputParams, DataControllerRequest dcRequest) {
		Result result = null;
		try {
			Connection con = DBManager.getConnection(dcRequest);
			if (StringUtils.isNotBlank(inputParams.get("query"))) {
				try (PreparedStatement pstmt = con.prepareStatement(inputParams.get("query"));
						ResultSet rs = pstmt.executeQuery()) {
					result = postProcess(rs);
				} catch (SQLException e) {
					alert.prepareError(e.getMessage()).log();
				}
			} else {
				StringBuilder sb = new StringBuilder();
				String payeeId = inputParams.get("id");

				String searchString = inputParams.get("searchString");
				String limit = inputParams.get("limit");
				String offset = inputParams.get("offset");
				String sortBy = inputParams.get("sortBy");
				String order = inputParams.get("order");

				if (StringUtils.isBlank(order)) {
					order = "asc";
				}
				if (StringUtils.isBlank(sortBy)) {
					order = "nickName";
				}

				sb.append(
						"select * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) + ".payee where ");
				String User_Id = HelperMethods.getUserIdFromSession(dcRequest);

				if (StringUtils.isNotBlank(payeeId)) {
					sb.append("Id = ?");
				} else {
					sb.append("User_Id = ?");
				}

				sb.append(" and ");
				sb.append("softDelete = '0'");
				if (StringUtils.isNotBlank(searchString)) {
					searchString = "%" + searchString + "%";
					sb.append(" and (");
					sb.append("nickName like " + "?");// 2
					sb.append(" or ");
					sb.append("firstName like " + "?");// 3
					sb.append(" or ");
					sb.append("lastName like " + "?");// 4
					sb.append(" or ");
					sb.append("name like " + "?");// 5
					sb.append(" or ");
					sb.append("bankName like " + "?");// 6
					sb.append(" or ");
					sb.append("companyName like " + "?");// 7
					sb.append(")");
				}
				if (StringUtils.isNotBlank(sortBy) && StringUtils.isNotBlank(order)) {
					sb.append(" order by " + sortBy + " " + order);
				}
				if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
					String jdbcUrl = QueryFormer.getDBType(dcRequest);
					sb.append(SqlQueryEnum.valueOf(jdbcUrl + "_GetUserPendingTransactions_getTrans_limit").getQuery()
							.replace("?1", offset).replace("?2", limit));
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
						if (StringUtils.isNotBlank(payeeId)) {
							statement.setString(1, payeeId);
						} else {
							statement.setString(1, User_Id);
						}
						if (StringUtils.isNotBlank(searchString)) {
							statement.setString(2, searchString);
							statement.setString(3, searchString);
							statement.setString(4, searchString);
							statement.setString(5, searchString);
							statement.setString(6, searchString);
							statement.setString(7, searchString);
						}
					}
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
				try {
					ResultSet rs = statement.executeQuery();
					result = postProcess(rs);
				} catch (Exception e) {
					alert.prepareError(e.toString()).log();
				}
			}
		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
		}

		return result;
	}

	private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException {
		List<Record> payees = result.getAllDatasets().get(0).getAllRecords();
		SecureRandom generator = new SecureRandom();
		for (Record payee : payees) {
			Record bill = getCurrentBillForPayee(dcRequest, HelperMethods.getFieldValue(payee, "Id"));
			// not correct way
			Param id = new Param("billId", "0", DBPUtilitiesConstants.STRING_TYPE);
			payee.addParam(id);
			payee.addParam(new Param("lastPaidAmount", String.valueOf(generator.nextInt(150)),
					DBPUtilitiesConstants.STRING_TYPE));
			payee.addParam(new Param("lastPaidDate", updateDateFormat(HelperMethods.getCurrentTimeStamp()),
					DBPUtilitiesConstants.STRING_TYPE));
			if (null != bill) {
				id.setValue(HelperMethods.getFieldValue(bill, "id"));
				payee.addParam(
						new Param("lastPaidDate", updateDateFormat(HelperMethods.getFieldValue(bill, "paidDate")),
								DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(
						new Param("billDueDate", updateDateFormat(HelperMethods.getFieldValue(bill, "billDueDate")),
								DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(new Param("billDescription", HelperMethods.getFieldValue(bill, "description"),
						DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(new Param("paidAmount", HelperMethods.getFieldValue(bill, "paidAmount"),
						DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(new Param("dueAmount", HelperMethods.getFieldValue(bill, "dueAmount"),
						DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(new Param("billGeneratedDate",
						updateDateFormat(HelperMethods.getFieldValue(bill, "billGeneratedDate")),
						DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(new Param("ebillURL", HelperMethods.getFieldValue(bill, "ebillURL"),
						DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(new Param("billerCategory", HelperMethods.getFieldValue(bill, "billerCategory"),
						DBPUtilitiesConstants.STRING_TYPE));
				payee.addParam(new Param("ebillSupport", HelperMethods.getFieldValue(bill, "ebillSupport"),
						DBPUtilitiesConstants.STRING_TYPE));
			} else {
				payee.addParam(new Param("ebillSupport",
						getEbillSupport(dcRequest, HelperMethods.getFieldValue(payee, "billermaster_id")),
						DBPUtilitiesConstants.STRING_TYPE));
			}
		}
	}

	private String updateDateFormat(String aDate) {
		try {
			if (StringUtils.isNotBlank(aDate)) {
				return HelperMethods.convertDateFormat(aDate, "yyyy-MM-dd'T'hh:mm:ss'Z'");
			}
		} catch (Exception e) {
		}
		return aDate;
	}

	private String getEbillSupport(DataControllerRequest dcRequest, String billermasterId) throws HttpCallException {
		String filter = "id" + DBPUtilitiesConstants.EQUAL + billermasterId;
		Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.BILL_MASTER_GET);
		return HelperMethods.getFieldValue(rs, "ebillSupport");
	}

	private Record getCurrentBillForPayee(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
		StringBuilder sb = new StringBuilder();
		sb.append("payeeId").append(DBPUtilitiesConstants.EQUAL).append(payeeId);
		sb.append(DBPUtilitiesConstants.AND);
		sb.append("billDueDate").append(DBPUtilitiesConstants.GREATER_EQUAL)
				.append(HelperMethods.getCurrentTimeStamp());
		Result bill = HelperMethods.callGetApi(dcRequest, sb.toString(), HelperMethods.getHeaders(dcRequest),
				URLConstants.BILL_PAYEE_GET);
		if (HelperMethods.hasRecords(bill)) {
			return bill.getAllDatasets().get(0).getRecord(0);
		}
		return null;
	}

	private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws ApplicationException {
		String sortBy = inputParams.get(DBPUtilitiesConstants.SORTBY);
		String searchString = inputParams.get("searchString");
		String offset = inputParams.get(DBPUtilitiesConstants.P_OFFSET);
		String limit = inputParams.get(DBPUtilitiesConstants.P_LIMIT);
		String payeeId = inputParams.get(DBPUtilitiesConstants.P_ID);
		String order = inputParams.get(DBPUtilitiesConstants.ORDER);
		String userId = inputParams.get("customerId");

		if (StringUtils.isBlank(userId)) {
			userId = HelperMethods.getUserIdFromSession(dcRequest);
		}
		String organizationId = inputParams.get("companyId");

		if ("payeeNickName".equalsIgnoreCase(sortBy)) {
			if (!StringUtils.isNotBlank(order)) {
				order = "asc";
			}
			setSortedPayeeListByUser(inputParams, userId, organizationId, order, offset, limit);
		} else if (StringUtils.isNotBlank(searchString)) {
			// setSearchPayeeByName(inputParams, userId, searchString);
			inputParams.put("query", getQuery(inputParams, dcRequest, userId, organizationId));
		} else if (StringUtils.isNotBlank(offset) && StringUtils.isNotBlank(limit)) {
			setPayeeListByUser(inputParams, userId, organizationId, offset, limit);
		} else if (!StringUtils.isNotBlank(payeeId) || payeeId.contains("$")) {
			setPayeeListByUser(inputParams, userId, organizationId);
		} else {
			setPayeeById(inputParams, payeeId);
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setSortedPayeeListByUser(Map inputParams, String userId, String organizationId, String order,
			String offset, String limitset) {
		setPayeeListByUser(inputParams, userId, organizationId);
		inputParams.put(DBPUtilitiesConstants.ORDERBY, "nickName " + order);
		if (StringUtils.isNotBlank(offset)) {
			inputParams.put(DBPUtilitiesConstants.SKIP, offset);
		}
		if (StringUtils.isNotBlank(limitset)) {
			inputParams.put(DBPUtilitiesConstants.TOP, limitset);
		}
	}

	/*
	 * @SuppressWarnings({ "rawtypes", "unchecked" }) private void
	 * setSearchPayeeByName(Map inputParams, String userId, String searchString) {
	 * StringBuilder sb = new StringBuilder(); String nullVal = null; searchString =
	 * "'"+searchString+"'"; sb.append("(");
	 * sb.append("nickName").append(DBPUtilitiesConstants.EQUAL)
	 * .append(searchString); sb.append(DBPUtilitiesConstants.OR);
	 * sb.append("firstName").append(DBPUtilitiesConstants.EQUAL)
	 * .append(searchString); sb.append(DBPUtilitiesConstants.OR);
	 * sb.append("lastName").append(DBPUtilitiesConstants.EQUAL)
	 * .append(searchString); sb.append(DBPUtilitiesConstants.OR);
	 * sb.append("name").append(DBPUtilitiesConstants.EQUAL) .append(searchString);
	 * sb.append(")"); sb.append(DBPUtilitiesConstants.AND);
	 * sb.append(DBPUtilitiesConstants.P_USER_ID)
	 * .append(DBPUtilitiesConstants.EQUAL).append(userId);
	 * sb.append(DBPUtilitiesConstants.AND);
	 * sb.append("softDelete").append(DBPUtilitiesConstants.EQUAL).append("0");
	 * sb.append(DBPUtilitiesConstants.AND).append("(");
	 * sb.append("isWiredRecepient").append(DBPUtilitiesConstants.EQUAL).append(
	 * nullVal); sb.append(DBPUtilitiesConstants.OR);
	 * sb.append("isWiredRecepient").append(DBPUtilitiesConstants.EQUAL).append("0")
	 * ; sb.append(")"); inputParams.put(DBPUtilitiesConstants.FILTER,
	 * sb.toString()); }
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setPayeeListByUser(Map inputParams, String userId, String organizationId, String offset,
			String limitset) {
		setPayeeListByUser(inputParams, userId, organizationId);
		inputParams.put(DBPUtilitiesConstants.TOP, limitset);
		inputParams.put(DBPUtilitiesConstants.SKIP, offset);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setPayeeListByUser(Map inputParams, String userId, String organizationId) {
		StringBuilder sb = new StringBuilder();
		String nullVal = null;

		sb.append("(");
		sb.append(DBPUtilitiesConstants.P_USER_ID).append(DBPUtilitiesConstants.EQUAL).append(userId);
		if (StringUtils.isNotBlank(organizationId)) {
			sb.append(DBPUtilitiesConstants.OR).append("organizationId").append(DBPUtilitiesConstants.EQUAL)
					.append(organizationId);
		}
		sb.append(")");
		sb.append(DBPUtilitiesConstants.AND).append("softDelete").append(DBPUtilitiesConstants.EQUAL).append("0");
		sb.append(DBPUtilitiesConstants.AND).append("(");
		sb.append("isWiredRecepient").append(DBPUtilitiesConstants.EQUAL).append(nullVal);
		sb.append(DBPUtilitiesConstants.OR);
		sb.append("isWiredRecepient").append(DBPUtilitiesConstants.EQUAL).append("0");
		sb.append(")");
		inputParams.put(DBPUtilitiesConstants.FILTER, sb.toString());
		inputParams.put(DBPUtilitiesConstants.ORDERBY, "nickName asc");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setPayeeById(Map inputParams, String id) {
		String filter = "Id" + DBPUtilitiesConstants.EQUAL + id;
		inputParams.put(DBPUtilitiesConstants.FILTER, filter);
	}

	private String getQuery(Map<String, String> inputParams, DataControllerRequest dcRequest, String userId,
			String organizationId) throws ApplicationException {
		String searchString = inputParams.get("searchString");
		if (StringUtils.isNotBlank(searchString) && !StringUtils.isAlphanumericSpace(searchString)) {
			// Security Fix to block special characters
			throw new ApplicationException(ErrorCodeEnum.ERR_10014);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("select * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest) + ".payee where ");
		sb.append("(");
		sb.append("User_Id = '" + userId + "'");
		if (StringUtils.isNotBlank(organizationId)) {
			sb.append(" or ");
			sb.append("organizationId = '" + organizationId + "'");
		}
		sb.append(")");
		sb.append(" and ");
		sb.append("softDelete = '0'");
		sb.append(" and ");
		sb.append("isWiredRecepient = '0'");
		if (StringUtils.isNotBlank(searchString)) {
			searchString = "'%" + searchString + "%'";
			sb.append(" and (");
			sb.append("nickName like " + searchString);
			sb.append(" or ");
			sb.append("firstName like " + searchString);
			sb.append(" or ");
			sb.append("lastName like " + searchString);
			sb.append(" or ");
			sb.append("name like " + searchString);
			sb.append(")");
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
}