package com.kony.externalalerts.resource.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.externalalerts.businessdelegate.api.ExternalAlertsBusinessDelegate;
import com.kony.externalalerts.resource.api.ExternalAlertsResource;
import com.kony.externalalerts.util.ExternalAlertsConstants;
import com.kony.externalalerts.util.ExternalAlertsEnum;
import com.kony.externalalerts.util.HelperMethods;
import com.kony.externalalerts.util.StaticDataHolder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public class ExternalAlertsResourceImpl implements ExternalAlertsResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "EVENTMANAGER");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "EVENTMANAGER");

	@SuppressWarnings("unchecked")
	@Override
	public Result triggerExternalAlerts(DataControllerRequest request) {
		if (request == null)
			return HelperMethods.returnResult(false, ExternalAlertsEnum.ERROR_INVALID);
		StaticDataHolder.initializestaticdata();
		List<Map<String, Object>> inputlist = new ArrayList<>();
		try {
			inputlist = JSONUtils.parse(request.getParameter(ExternalAlertsConstants.INPUTPARAMS), List.class);
			diagnostic.prepareDebug("inputlist " + inputlist).log();
		} catch (IOException e) {
			alert.prepareError("Exception occured ",e).log();
			return HelperMethods.returnResult(false, ExternalAlertsEnum.ERROR_EXCEPTION);
		}
		if (inputlist.isEmpty())
			return HelperMethods.returnResult(false, ExternalAlertsEnum.ERROR_EMPTYINPUT);
		try {
			String customerId = inputlist.get(0).get(ExternalAlertsConstants.CUSTOMER_ID).toString();
			diagnostic.prepareDebug("customerId "+customerId).log();
			if (StringUtils.isEmpty(customerId))
				return HelperMethods.returnResult(false, ExternalAlertsEnum.ERROR_CUSTOMERID);
			ExternalAlertsBusinessDelegate externalAlertsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
					.getFactoryInstance(BusinessDelegateFactory.class)
					.getBusinessDelegate(ExternalAlertsBusinessDelegate.class);
			String accountId = externalAlertsBusinessDelegate.getSFAccountId(customerId);
			diagnostic.prepareDebug("SfaccountId "+accountId).log();
			if (StringUtils.isBlank(accountId))
				return HelperMethods.returnResult(false, ExternalAlertsEnum.ERROR_SFID);
			externalAlertsBusinessDelegate.pushToSf(inputlist, accountId);
		} catch (Exception e) {
			alert.prepareError("Exception occured ",e).log();
			return HelperMethods.returnResult(false, ExternalAlertsEnum.ERROR_EXCEPTION);

		}

		return new Result();
	}

}
