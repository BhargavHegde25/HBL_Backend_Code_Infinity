package com.kony.dbputilities.customersecurityservices;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CheckIfOrgMemberExists implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		result.addParam(new Param("status", "true", MWConstants.STRING));

		String lastname = dcRequest.getParameter("LastName");
		String dateOfBirth = dcRequest.getParameter("DateOfBirth");
		String ssn = dcRequest.getParameter("Ssn");
		String orgId = dcRequest.getParameter("Organization_Id");
		if (!isValidDOB(dateOfBirth)) {
			ErrorCodeEnum.ERR_10165.setErrorCode(result);
		}
		Result customer = getCustomer(dcRequest, lastname, ssn, dateOfBirth);
		Result emp = new Result();
		if (HelperMethods.hasRecords(customer)) {
			Dataset customers = customer.getAllDatasets().get(0);
			for (Record record : customers.getAllRecords()) {
				String customerId = HelperMethods.getFieldValue(record, "id");
				emp = getOrgEmployee(dcRequest, orgId, customerId);
				if (HelperMethods.hasRecords(emp)) {
					break;
				}
			}
		}

		if (HelperMethods.hasRecords(emp)) {
			result.addParam(new Param("status", "false", MWConstants.STRING));
			ErrorCodeEnum.ERR_10046.setErrorCode(result);
		} else {
			Result orgOwner = getOrgOwner(dcRequest, lastname, ssn, dateOfBirth, orgId);
			if (!HelperMethods.hasRecords(orgOwner)) {
				result.addParam(new Param("status", "false", MWConstants.STRING));
				ErrorCodeEnum.ERR_10047.setErrorCode(result);
			}
		}
		return result;
	}

	private boolean isValidDOB(String dateOfBirth) {

		if (StringUtils.isNotBlank(dateOfBirth)) {
			Date createdts = HelperMethods.getFormattedTimeStamp(dateOfBirth);
			Calendar generatedCal = Calendar.getInstance();
			generatedCal.setTime(createdts);

			Date verifyDate = new Date();
			Calendar verifyingCal = Calendar.getInstance();
			verifyingCal.setTime(verifyDate);

			long GeneratedMilliSeconds = generatedCal.getTimeInMillis();
			long verifyingMilliSeconds = verifyingCal.getTimeInMillis();

			if (GeneratedMilliSeconds > verifyingMilliSeconds) {
				return false;
			}
		}
		return true;
	}

	private Result getOrgEmployee(DataControllerRequest dcRequest, String organizationId, String customerId) {
		try {
			String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + organizationId + DBPUtilitiesConstants.AND
					+ "Customer_id" + DBPUtilitiesConstants.EQUAL + customerId;
			return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATIONEMPLOYEE_GET);
		} catch (HttpCallException e) {
			return new Result();
		}
	}

	private Result getCustomer(DataControllerRequest dcRequest, String lastName, String ssn, String dob) {
		try {
			String filter = "LastName" + DBPUtilitiesConstants.EQUAL + lastName + DBPUtilitiesConstants.AND + "Ssn"
					+ DBPUtilitiesConstants.EQUAL + ssn + DBPUtilitiesConstants.AND + "DateOfBirth"
					+ DBPUtilitiesConstants.EQUAL + dob;
			return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.CUSTOMERVERIFY_GET);
		} catch (HttpCallException e) {
			return new Result();
		}
	}

	private Result getOrgOwner(DataControllerRequest dcRequest, String lastName, String ssn, String dob, String orgId) {
		try {
			String filter = "LastName" + DBPUtilitiesConstants.EQUAL + lastName + DBPUtilitiesConstants.AND + "Ssn"
					+ DBPUtilitiesConstants.EQUAL + ssn + DBPUtilitiesConstants.AND + "DateOfBirth"
					+ DBPUtilitiesConstants.EQUAL + dob + DBPUtilitiesConstants.AND + "Organization_id"
					+ DBPUtilitiesConstants.EQUAL + orgId;

			return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
					URLConstants.ORGANISATION_OWNER_GET);
		} catch (HttpCallException e) {
			return new Result();
		}

	}
}