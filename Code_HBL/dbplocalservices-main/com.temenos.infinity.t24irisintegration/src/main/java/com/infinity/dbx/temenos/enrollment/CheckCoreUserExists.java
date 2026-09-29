/**
 * 
 */
package com.infinity.dbx.temenos.enrollment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author Gopinath Vaddepally - KH2453
 *
 */
public class CheckCoreUserExists implements JavaService2 {

	@Override
	public Object invoke(String methodId, Object[] inputMap, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		TemenosUtils temenosUtils = TemenosUtils.getInstance();
		Result result = new Result();
		boolean isUserExists = false;
		JsonObject customerProfileJson = new JsonObject();
		String dob = request.getParameter(TemenosConstants.KONY_DBX_DATEOFBIRTH);
		dob = CommonUtils.convertDateToYYYYMMDD(dob);
		request.addRequestParam_(TemenosConstants.KONY_DBX_DATEOFBIRTH, dob);
		if (prePrcoess(request)) {
			Result customerResult = (Result)CommonUtils.callInternalService(EnrollmentConstants.SERVICE_ID_USER, EnrollmentConstants.OPERATION_GET_CUSTOMER_DETAILS, null, null, request, 1, true);
			if (temenosUtils.validateResult(customerResult, TemenosConstants.PARAM_DATASET, EnrollmentConstants.PARAM_BODY_DS)) {
				Dataset bodyDataset = customerResult.getDatasetById(EnrollmentConstants.PARAM_BODY_DS);
				Record customerRecord = bodyDataset.getRecord(0);
				if (customerRecord != null) {
					isUserExists = true;
					CreateCustomerProfile customerProfile = CreateCustomerProfile.getInstance();
					customerProfile.addBackendIdentifiersInformation(customerRecord, customerProfileJson);
					customerProfile.addAddressInformation(customerRecord, customerProfileJson);
					customerProfile.addPersonalInformation(customerRecord, customerProfileJson, null);
					customerProfile.addCommunicationInfo(customerRecord, customerProfileJson);
					result.addParam(new Param(EnrollmentConstants.PARAM_CORE_COMMUNICATION, customerProfileJson.get(EnrollmentConstants.PARAM_CORE_COMMUNICATION).toString()));
					result.addParam(new Param(EnrollmentConstants.PARAM_ADDRESS_INFORMATION, customerProfileJson.get(EnrollmentConstants.PARAM_ADDRESS_INFORMATION).toString()));
					result.addParam(new Param(EnrollmentConstants.PARAM_BACKEND_IDENTIFIERS_INFO, customerProfileJson.get(EnrollmentConstants.PARAM_BACKEND_IDENTIFIERS_INFO).toString()));
					result.addParam(new Param(EnrollmentConstants.PARAM_PERSONAL_INFORMATION, customerProfileJson.get(EnrollmentConstants.PARAM_PERSONAL_INFORMATION).toString()));
				}
				else
				{
					result.addParam(new Param(Constants.PARAM_ERR_CODE, EnrollmentConstants.DEF_ERR_CODE,Constants.PARAM_DATATYPE_STRING));
				}
			}
			else
			{
				result.addParam(new Param(Constants.PARAM_ERR_CODE, EnrollmentConstants.DEF_ERR_CODE,Constants.PARAM_DATATYPE_STRING));
			}
			result.addParam(new Param(EnrollmentConstants.PARAM_IS_USER_EXISTS, Boolean.toString(isUserExists)));
		}
		else
		{
			CommonUtils.setErrMsg(result, EnrollmentConstants.ERR_ENROLLMENT_VALID_INPUTS);
			CommonUtils.setOpStatusOk(result);

		}
		return result;
	}

	/*
	 *validate the request
	 */
	public boolean prePrcoess(DataControllerRequest request)
	{
		boolean status = false;
		String lastName = request.getParameter(EnrollmentConstants.PARAM_LAST_NAME);
		String ssn = request.getParameter(EnrollmentConstants.PARAM_SSN);
		String dob = request.getParameter(EnrollmentConstants.PARAM_DOB);
		if (StringUtils.isNotBlank(lastName) && StringUtils.isNotBlank(ssn) && StringUtils.isNotBlank(dob)) {
			/**
			 * convert date to t24 api format
			 */
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(EnrollmentConstants.CONSTANT_T24_DATE_FORMATTER);
			request.addRequestParam_(EnrollmentConstants.PARAM_DOB, LocalDate.parse(dob).format(formatter));
			status = true;
		}
		return status;
	}
}
