package com.bct.javaservices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.utilities.Utils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class GoogleAuthPairService implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GoogleAuthPairService.class);
	@Override
	public Object invoke(String arg0, Object[] arg1, DataControllerRequest request, DataControllerResponse response)
			throws Exception {
		Result result = new Result();
		try {
			ServicesManager sm = request.getServicesManager();
			ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();

			// SecretCode is the key configured under Server Properties tab in App Services
			// String SecretCode = paramHelper.getServerProperty("GOOGLEAUTH_SECRETCODE");
			String AppName = paramHelper.getServerProperty("APPNAME");
			String userName = request.getParameter("userName");
			String SecretCode = Utils.generateEncryptedKey(userName);

			String barCodeUrl = Utils.getGoogleAuthenticatorBarCode(SecretCode, userName, AppName);
			System.out.println("barCodeUrl >> " + barCodeUrl);
			String qrCode = Utils.returnQRCode(barCodeUrl, 400, 400);

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
			result.setParam(new Param("qrcode", qrCode));

		} catch (Exception e) {
			LOG.error("Exception occured in GoogleAuthPairService:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

}
