package com.kony.adminconsole.campaign.resource.impl;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.campaign.businessdelegate.api.DataStorageBusinessDelegate;
import com.kony.adminconsole.campaign.resource.ApplicationResource;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;


public class ApplicationResourceImpl implements ApplicationResource{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	@Override
	public String getApplicationBankReference(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws ApplicationException {
		try {
			String applicationId = request.getParameter("ApplicationId");
			String applicationType = request.getParameter("ApplicationType");
			String entityDefinitionCode = applicationType.equalsIgnoreCase("SME")
					? EnvironmentConfiguration.SME_ONBOARDING_ENTITY_DEFINTION.getValue(request)
					: EnvironmentConfiguration.ONBOARDING_ENTITY_DEFINTION.getValue(request);

			if (StringUtils.isAnyBlank(applicationId, entityDefinitionCode))
				throw new ApplicationException(ErrorCodeEnum.ERR_22085);

			DataStorageBusinessDelegate storageBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(DataStorageBusinessDelegate.class);

			JSONObject metaDataEntry = storageBusinessDelegate.getEntityItemByKeyNameVersionType(entityDefinitionCode,
					applicationId, "MetaData", "", "JSON");
			return metaDataEntry.optString("BankReference");
		}
		catch (ApplicationException ae) {
			throw new ApplicationException(ae.getErrorCodeEnum());
		} catch (Exception e) {
			alert.prepareError("Exception in getApplicationBankReference -> " + e.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22086);
		}
	}

}
