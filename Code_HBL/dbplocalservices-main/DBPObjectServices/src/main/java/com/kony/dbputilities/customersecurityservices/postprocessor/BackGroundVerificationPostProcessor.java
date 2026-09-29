package com.kony.dbputilities.customersecurityservices.postprocessor;

import java.util.Arrays;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.DataPostProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class BackGroundVerificationPostProcessor implements DataPostProcessor2 {

	@Override
	public Object execute(Result result, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Log4j2Configurator.getInstance();
		Result retResult = new Result();
		boolean emailStatus = verifyEmail(retResult, result);
		boolean usernameStatus = verifyUserName(retResult, result);
		boolean paStatus = verifyPatriotAct(retResult, result);
		boolean locationStatus = verifyLocation(retResult, result);

		if (emailStatus && usernameStatus && locationStatus && paStatus) {
			HelperMethods.setSuccessMsg("PASS", retResult);
		} else {
			ErrorCodeEnum.ERR_11029.setErrorCode(retResult);
		}

		return retResult;
	}

	private boolean verifyPatriotAct(Result retResult, Result result) {
		Record expectIDPA = result.getRecordById("ExpectIDPA");
		String successValue = "global.watch.list.no.match";
		if (null != expectIDPA) {
			String paStatus = HelperMethods.getParamValue(expectIDPA.getParamByName("restriction_key"));

			if (StringUtils.isNotBlank(paStatus) && !successValue.equalsIgnoreCase(paStatus)) {
				retResult.addParam(new Param("paStatus",
						HelperMethods.getParamValue(expectIDPA.getParamByName("restriction_message")),
						MWConstants.STRING));
				return false;
			}
		}

		return true;
	}

	private boolean verifyLocation(Result retResult, Result result) {
		String successValue = "id.success";
		Record expectIDGeotrace = result.getRecordById("ExpectIDGeotrace");
		Record results = null;
		String locationStatus = null;
		if (null != expectIDGeotrace) {
			results = expectIDGeotrace.getRecordById("summary_result");
		}
		if (null != results) {
			locationStatus = HelperMethods.getParamValue(results.getParamByName("key"));
			if (!successValue.equalsIgnoreCase(locationStatus)) {
				retResult.addParam(new Param("locationStatus",
						HelperMethods.getParamValue(results.getParamByName("message")), MWConstants.STRING));
				return false;
			}
		}
		return true;
	}

	private boolean verifyUserName(Result retResult, Result result) {
		String successValue = "result.match";
		Record expectIDIQ = result.getRecordById("ExpectIDIQ");

		if (null != expectIDIQ) {
			String usernameStatus = HelperMethods.getParamValue(expectIDIQ.getParamByName("results_key"));
			if (!successValue.equalsIgnoreCase(usernameStatus)) {
				retResult.addParam(new Param("usernameStatus",
						HelperMethods.getParamValue(expectIDIQ.getParamByName("results_message")), MWConstants.STRING));
				return false;
			}
		}

		return true;
	}

	private boolean verifyEmail(Result retResult, Result result) {
		List<String> resultcodes = Arrays
				.asList(new String[] { "resultcode.email.no.hit", "resultcode.email.service.not.available" });
		String emailStatus = HelperMethods.getParamValue(result.getParamByName("qualifier_key"));
		if (!resultcodes.contains(emailStatus)) {
			retResult.addParam(new Param("emailStatus",
					HelperMethods.getParamValue(result.getParamByName("qualifier_message")), MWConstants.STRING));
			return false;
		}
		return true;
	}

}
