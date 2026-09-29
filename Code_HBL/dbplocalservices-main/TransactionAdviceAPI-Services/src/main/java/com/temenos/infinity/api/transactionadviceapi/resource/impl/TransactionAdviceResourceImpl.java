package com.temenos.infinity.api.transactionadviceapi.resource.impl;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.transactionadviceapi.businessdelegate.api.TransactionAdviceBusinessDelegate;
import com.temenos.infinity.api.transactionadviceapi.config.ServerConfigurations;
import com.temenos.infinity.api.transactionadviceapi.constants.ErrorCodeEnum;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormCookie;
import com.temenos.infinity.api.transactionadviceapi.dto.AutoFormDownload;
import com.temenos.infinity.api.transactionadviceapi.resource.api.TransactionAdviceResource;

public class TransactionAdviceResourceImpl implements TransactionAdviceResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result loginAndDownload(String customerId, String accountId, String transactionRef, String mediaType,
			String transactionType, String page,String operation,String auth_token) {

		Result result = new Result();
		//If no AuthToken is passed to input for download(Public) throw Invalid Input Exception
		 if(operation.equalsIgnoreCase("download")&& StringUtils.isBlank(auth_token))
		{
			diagnostic.prepareDebug("Download Operation performed without Auth Token").log();
			return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
		}
		if (StringUtils.isBlank(customerId) || StringUtils.isBlank(accountId) || StringUtils.isBlank(transactionRef)
				|| StringUtils.isBlank(mediaType) || StringUtils.isBlank(transactionType)) {
			diagnostic.prepareDebug("Missing Params").log();
			return ErrorCodeEnum.ERR_20001.setErrorCode(new Result());
		}
		try {
			if (StringUtils.isBlank(ServerConfigurations.AUTOFORM_USERNAME.getValue())
					|| StringUtils.isBlank(ServerConfigurations.AUTOFORM_PASSWORD.getValue())) {
				diagnostic.prepareDebug("Missing AutoForm Creds, Kindly add it to environment variables").log();
				return ErrorCodeEnum.ERR_20005.setErrorCode(new Result());
			}
		} catch (Exception e) {

			diagnostic.prepareDebug("Missing AutoForm Creds, Kindly add it to environment variables").log();
			return ErrorCodeEnum.ERR_20005.setErrorCode(new Result());
		}
		try {

			TransactionAdviceBusinessDelegate loginAndDownloadBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(TransactionAdviceBusinessDelegate.class);
			// Login Operation
			AutoFormCookie LoginResult = loginAndDownloadBusinessDelegate.login(auth_token);

			String xsrf = LoginResult.getXsrftoken();
			String jsessionid = LoginResult.getJSESSIONID();

			if (StringUtils.isBlank(xsrf) || StringUtils.isBlank(jsessionid)) {
				diagnostic.prepareDebug("Login Failed").log();
				return ErrorCodeEnum.ERR_20004.setErrorCode(new Result());
			}

			// Search Operation
			AutoFormDownload document = loginAndDownloadBusinessDelegate.search(customerId, accountId, transactionRef,
					mediaType, transactionType, page, xsrf, jsessionid,auth_token);

			// Download Operation
			if (document.getDocumentId() != null && document.getRevision() != null) {
				String DownloadResult = loginAndDownloadBusinessDelegate.download(document.getDocumentId(),
						document.getRevision(), xsrf, jsessionid,auth_token);
				result.addParam("base64", DownloadResult);
				diagnostic.prepareDebug("Document Retrieved Successfully").log();
			} else
				{diagnostic.prepareDebug("Document Retrieve Failed").log();
				return ErrorCodeEnum.ERR_20003.setErrorCode(new Result());}

		} catch (Exception e) {
			alert.prepareError(e.toString()).log();
			diagnostic.prepareDebug("Document Retrieve Failed").log();
			return ErrorCodeEnum.ERR_20041.setErrorCode(new Result());
		}
		return result;
	}

}