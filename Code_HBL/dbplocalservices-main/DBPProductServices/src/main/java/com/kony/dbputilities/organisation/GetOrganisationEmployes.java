package com.kony.dbputilities.organisation;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetOrganisationEmployes implements JavaService2 {

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws Exception {
		Result result = new Result();
		Result retResult = new Result();
		Dataset orgEmploye = new Dataset();
		orgEmploye.setId("OrganizationEmploye");
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		result = preProcess(inputParams, dcRequest, result);
		if (HelperMethods.hasRecords(result) && !HelperMethods.hasError(result)) {
			Dataset ds = result.getAllDatasets().get(0);
			for (Record record : ds.getAllRecords()) {
				appendEmployee(record, dcRequest, orgEmploye);
			}
			retResult.addDataset(orgEmploye);
			HelperMethods.setSuccessMsg(DBPUtilitiesConstants.USER_EXISTS_IN_DBX, retResult);
		} else if (HelperMethods.hasError(result)) {
			ErrorCodeEnum.ERR_11012.setErrorCode(retResult);
		} else {
			ErrorCodeEnum.ERR_11013.setErrorCode(retResult);
		}

		return retResult;
	}

	private void appendEmployee(Record record, DataControllerRequest dcRequest, Dataset orgEmploye)
			throws HttpCallException {
		String employeId = record.getParam("Customer_id").getValue();
		Record empRecord = new Record();
		addEmployeCommunication(employeId, dcRequest, empRecord);
		addEmployeDetails(employeId, dcRequest, empRecord);
		orgEmploye.addRecord(empRecord);

	}

	private void addEmployeDetails(String employeId, DataControllerRequest dcRequest, Record empRecord)
			throws HttpCallException {
		String filter = "id" + DBPUtilitiesConstants.EQUAL + employeId;
		StringBuilder sb = new StringBuilder();

		Result customer = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_GET);
		if (HelperMethods.hasRecords(customer)) {
			String firstName = HelperMethods.getFieldValue(customer, "FirstName");
			String middleName = HelperMethods.getFieldValue(customer, "MiddleName");
			String lastName = HelperMethods.getFieldValue(customer, "LastName");
			if (StringUtils.isNotBlank(firstName)) {
				sb.append(firstName);
			}
			if (StringUtils.isNotBlank(middleName)) {
				sb.append(middleName);
			}
			if (StringUtils.isNotBlank(lastName)) {
				sb.append(lastName);
			}
			empRecord.addParam(
					new Param("id", HelperMethods.getFieldValue(customer, "id"), DBPUtilitiesConstants.STRING_TYPE));
			empRecord.addParam(new Param("UserName", HelperMethods.getFieldValue(customer, "UserName"),
					DBPUtilitiesConstants.STRING_TYPE));
			empRecord.addParam(new Param("Name", sb.toString(), DBPUtilitiesConstants.STRING_TYPE));
			empRecord.addParam(customer.getParamByName("isEagreementSigned"));
		}

	}

	private void addEmployeCommunication(String employeId, DataControllerRequest dcRequest, Record empRecord)
			throws HttpCallException {
		String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + employeId;

		Result customerCommunication = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMERCOMMUNICATION_GET);
		if (HelperMethods.hasRecords(customerCommunication)) {
			Dataset ds = customerCommunication.getAllDatasets().get(0);
			for (Record record : ds.getAllRecords()) {
				addEmailAndPhoneToEmp(record, empRecord);
			}

		}

	}

	private void addEmailAndPhoneToEmp(Record record, Record empRecord) {
		if (record.getParam("Type_id").getValue().equals("COMM_TYPE_EMAIL")) {
			empRecord.addParam(
					new Param("Email", record.getParam("Value").getValue(), DBPUtilitiesConstants.STRING_TYPE));
		} else {
			empRecord.addParam(
					new Param("Phone", record.getParam("Value").getValue(), DBPUtilitiesConstants.STRING_TYPE));

		}
	}

	private Result preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
			throws HttpCallException {
		String orgId = inputParams.get("Organization_id");

		String filter = "Organization_id" + DBPUtilitiesConstants.EQUAL + orgId;
		Result organisationEmploye = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
				URLConstants.ORGANISATIONEMPLOYEE_GET);

		return organisationEmploye;

	}

}
