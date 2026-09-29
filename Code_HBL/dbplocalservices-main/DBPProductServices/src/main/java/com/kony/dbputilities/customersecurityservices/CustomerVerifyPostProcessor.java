package com.kony.dbputilities.customersecurityservices;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodes;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class CustomerVerifyPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Result retValue = new Result();

		Dataset dataset = result.getDatasetById(DBPUtilitiesConstants.USR_ATTR + 1);

		Record dbxUsrAttr = null;
		if (dataset != null) {
			dbxUsrAttr = dataset.getRecord(0);
		}

		Dataset dataset2 = result.getDatasetById(DBPUtilitiesConstants.CORE_ATTR + 1);

		Record coreUsrAttr = new Record();

		if (dataset2 != null) {
			coreUsrAttr = dataset2.getRecord(0);
		}

		String dbxErrorCode = HelperMethods.getFieldValue(dbxUsrAttr, DBPUtilitiesConstants.ERROR_CODE);
		String coreErrorCode = HelperMethods.getFieldValue(coreUsrAttr, DBPUtilitiesConstants.ERROR_CODE);

		if (StringUtils.isNotBlank(dbxErrorCode) && ErrorCodes.RECORD_FOUND.equals(dbxErrorCode)) {
			for (Param param : dbxUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			dataset = result.getDatasetById(DBPUtilitiesConstants.USR_ATTR);
			retValue.addDataset(dataset);
			return retValue;
		} else if (StringUtils.isNotBlank(dbxErrorCode) && "3402".equals(dbxErrorCode)) {
			for (Param param : dbxUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(coreErrorCode) && ErrorCodes.RECORD_FOUND.equals(coreErrorCode)) {

			dataset = result.getDatasetById(DBPUtilitiesConstants.CORE_ATTR);
			Dataset dataset1 = new Dataset();
			dataset1.setId(DBPUtilitiesConstants.USR_ATTR);
			for (Record record : dataset.getAllRecords()) {
				dataset1.addRecord(updateRecords(record));
			}

			retValue.addDataset(dataset1);

			for (Param param : coreUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(coreErrorCode) && ErrorCodes.RECORD_NOT_FOUND.equals(coreErrorCode)) {
			for (Param param : coreUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(dbxErrorCode)
				&& ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS.equals(dbxErrorCode)) {
			for (Param param : dbxUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(coreErrorCode)
				&& ErrorCodes.ERROR_SEARCHING_RECORD_MANDATORY_INFORMATION_MISS.equals(coreErrorCode)) {
			for (Param param : coreUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(dbxErrorCode) && ErrorCodes.ERROR_SEARCHING_RECORD.equals(dbxErrorCode)) {
			for (Param param : dbxUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else if (StringUtils.isNotBlank(coreErrorCode) && ErrorCodes.ERROR_SEARCHING_RECORD.equals(coreErrorCode)) {
			for (Param param : coreUsrAttr.getAllParams()) {
				retValue.addParam(param);
			}
			return retValue;
		} else {
			HelperMethods.setValidationMsg("User verification failed", retValue);
		}

		return retValue;
	}

	private Record updateRecords(Record coreUsrAttr) {

		Record record = new Record();

		if (coreUsrAttr.getParam("Id") != null) {
			record.addParam(new Param("id", coreUsrAttr.getParam("Id").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("ssn") != null) {
			record.addParam(new Param("Ssn", coreUsrAttr.getParam("ssn").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("userName") != null) {
			record.addParam(new Param("UserName", coreUsrAttr.getParam("userName").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("userFirstName") != null) {
			record.addParam(new Param("FirstName", coreUsrAttr.getParam("userFirstName").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("userLastName") != null) {
			record.addParam(new Param("LastName", coreUsrAttr.getParam("userLastName").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("dateOfBirth") != null) {
			record.addParam(new Param("DateOfBirth", coreUsrAttr.getParam("dateOfBirth").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("phone") != null) {
			record.addParam(new Param("Phone", coreUsrAttr.getParam("phone").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("email") != null) {
			record.addParam(new Param("Email", coreUsrAttr.getParam("email").getValue(), "String"));
		}
		if (coreUsrAttr.getParam("gender") != null) {
			record.addParam(new Param("Gender", coreUsrAttr.getParam("gender").getValue(), "String"));
		}

		return record;
	}

}