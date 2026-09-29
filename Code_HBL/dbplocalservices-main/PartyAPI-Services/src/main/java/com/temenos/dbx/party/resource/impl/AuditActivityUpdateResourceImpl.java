package com.temenos.dbx.party.resource.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.party.businessdelegate.api.AuditActivityPartyCustomerInformationUpdateBusinessDelegate;
import com.temenos.dbx.party.resource.api.AuditActivityUpdateResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

/**
 * 
 * @author KH2627
 * @version 1.0 Extends the {@link AuditActivityUpdateResource}
 */

public class AuditActivityUpdateResourceImpl implements AuditActivityUpdateResource {
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	@Override
	public Result updatePartyAuditLogsWithCustomerInformation(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) {

		LoggerUtil logger = new LoggerUtil(AuditActivityUpdateResourceImpl.class);

		Result result = new Result();

		Map<String, Object> inputMap = HelperMethods.getInputParamObjectMap(inputArray);
		boolean responseFlag = false;

		if (preProcess(inputMap)) {
			try {
				AuditActivityPartyCustomerInformationUpdateBusinessDelegate auditActivityPartyCustomerInformationUpdateBusinessDelegate = DBPAPIAbstractFactoryImpl
						.getInstance().getFactoryInstance(BusinessDelegateFactory.class)
						.getBusinessDelegate(AuditActivityPartyCustomerInformationUpdateBusinessDelegate.class);

				responseFlag = auditActivityPartyCustomerInformationUpdateBusinessDelegate
						.updateAuditLogsWithCustomerInformation(inputMap, getHeaders(dcRequest),
								URLConstants.AUDITLOGS_UPDATE);

			} catch (Exception e) {
				alert.prepareError(
						"Exception occured while calling AuditActivityPartyCustomerInformationUpdateBusinessDelegate").log();
			}
		}
		postProcess(responseFlag, result);

		return result;
	}

	private void postProcess(boolean responseFlag, Result result) {

		result.addStringParam("isUpdateSuccess", String.valueOf(responseFlag));
		
		if(!responseFlag) {
			ErrorCodeEnum.ERR_10717.setErrorCode(result);
		}
	}

	private boolean preProcess(Map<String, Object> inputMap) {

		if (!inputMap.isEmpty() && inputMap.containsKey("partyID") && inputMap.get("partyID") != null) {
			return true;
		}
		return false;
	}

	public static Map<String, Object> getHeaders(DataControllerRequest dcRequest) {
		Map<String, Object> headerMap = new HashMap<>();
		headerMap.put(DBPUtilitiesConstants.X_KONY_AUTHORIZATION,
				dcRequest.getHeader(DBPUtilitiesConstants.X_KONY_AUTHORIZATION));
		headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		return headerMap;
	}

}
