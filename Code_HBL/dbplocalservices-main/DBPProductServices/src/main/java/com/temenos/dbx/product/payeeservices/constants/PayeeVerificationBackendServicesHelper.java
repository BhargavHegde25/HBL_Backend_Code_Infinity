package com.temenos.dbx.product.payeeservices.constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.temenos.dbx.product.constants.PayeeVerificationBackendURLFinder;
import com.dbp.core.constants.DBPConstants;
import com.infinity.dbx.dbp.jwt.auth.utils.CommonUtils;
import com.infinity.dbx.temenos.constants.TemenosConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class PayeeVerificationBackendServicesHelper {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    public static final String ERROR_MESSAGE_KEY = DBPConstants.DBP_ERROR_MESSAGE_KEY;
    public static final String OPSTATUS_CODE = DBPConstants.FABRIC_OPSTATUS_KEY;
    public static final String HTTPSTATUS_CODE = DBPConstants.FABRIC_HTTP_STATUS_CODE_KEY;
	public static String getCountry(Map<String, Object> params) {
		
		String country = "";
		try {
			String bic = (String) params.get("swiftCode");
			String clearingSysId = (String) params.get("clearingIdentifierCode");
			String bankCountryName = (String) params.get("bankCountryName");
			if (StringUtils.isNotEmpty(bic))
				country = bic.substring(4, 6);
			else if (StringUtils.isEmpty(country) && StringUtils.isNotEmpty(clearingSysId))
				country = clearingSysId.substring(0, 2);
			else if (StringUtils.isEmpty(country) && StringUtils.isNotEmpty(bankCountryName) && bankCountryName.length() == 2)
				country = bankCountryName;
			if (StringUtils.isEmpty(country))
				country = "default";
		} catch (Exception e) {
			alert.prepareError("Exception occurred while invoking payee verification backend service : " + e).log();
			return "";
		}
		return country;
	}
	public static Result fetchVerifyPayeeResponse(DataControllerRequest request, Map<String, Object> params) {
		Result verifyPayeeResult = new Result();
		String serviceName = "";
		String operationName = "";
		String serviceDetails = "";
		try {
			String verifyPayeeBackend = EnvironmentConfigurationsHandler.getValue("VERIFY_PAYEE_BACKEND", request);
			if (StringUtils.isBlank(verifyPayeeBackend))
				verifyPayeeBackend = "Country";
			if (verifyPayeeBackend.equalsIgnoreCase("Country")) {
				serviceDetails = PayeeVerificationBackendURLFinder.getCountryBackendURL(PayeeVerificationBackendServicesHelper.getCountry(params));
			} else {
				serviceDetails = PayeeVerificationBackendURLFinder.getPaymentTypeBackendURL("WithinBank");
				if (serviceDetails.isEmpty())
					serviceDetails = PayeeVerificationBackendURLFinder.getPaymentTypeBackendURL("default");
			}
			serviceName = serviceDetails.split("\\.")[0];
			operationName = serviceDetails.split("\\.")[1];

			HashMap<String, Object> serviceHeaders = new HashMap<String, Object>();
			if("PayeeVerificationJsonServices".equals(serviceName)) {
				verifyPayeeResult = CommonUtils.callIntegrationService(request, new HashMap<String, Object>(), new HashMap<String, Object>(), serviceName,
						operationName, true);
				serviceName="PayeeVerificationJavaServices";
			}
			verifyPayeeResult = CommonUtils.callIntegrationService(request, params, serviceHeaders, serviceName,
					operationName, false);
		} catch (Exception e) {
			String excep = "ioexception";
			if(e.toString().toLowerCase().contains(excep)) {
				verifyPayeeResult.addParam(new Param(ERROR_MESSAGE_KEY, "Time out exception", MWConstants.STRING));
				verifyPayeeResult.addParam(new Param(OPSTATUS_CODE, "9001", MWConstants.INT));
				verifyPayeeResult.addParam(new Param(HTTPSTATUS_CODE, "-1", MWConstants.INT));
				verifyPayeeResult.addParam(new Param("payeeVerificationStatus", "Failure"));
				verifyPayeeResult.addParam(new Param("payeeVerificationErrMsg", "TimedOut"));
				verifyPayeeResult.addParam(new Param("payeeVerification",""));
		        return verifyPayeeResult;
			}else {
				alert.prepareError("Exception occurred while invoking payee verification backend service : " + e).log();
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
		}
		return verifyPayeeResult;
	}
}