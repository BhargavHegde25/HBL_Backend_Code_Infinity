package com.kony.adminconsole.multientity.businessdelegate.impl;

import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.dbp.core.api.factory.BackendDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.error.DBPApplicationException;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.multientity.backenddelegate.api.MultiEntityBackendDelegate;
import com.kony.adminconsole.multientity.businessdelegate.api.MultiEntityBusinessDelegate;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;



public class MultiEntityBusinessDelegateImpl implements MultiEntityBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	MultiEntityBackendDelegate backendDelegate = DBPAPIAbstractFactoryImpl.getInstance()
			.getFactoryInstance(BackendDelegateFactory.class).getBackendDelegate(MultiEntityBackendDelegate.class);
	@Override
	public Result getAllCompanyLegalUnits(String authToken,boolean singleentity, Object sessionId) throws DBPApplicationException
	{
		Result result = new Result();
		try
		{
			JSONObject records = null;
			String Orgrefbackend = EnvironmentConfigurationsHandler.getServerAppProperty("ORGREF_BACKEND");
			
			if(StringUtils.isBlank(Orgrefbackend) || "DBXDB".equalsIgnoreCase(Orgrefbackend)) {
				records = backendDelegate.getAllCompanyLegalUnitsFromDB(singleentity);
			} else {
				records = backendDelegate.getAllCompanyLegalUnits(authToken,singleentity, sessionId );
			}
			
			if(records ==null)
               {
				ErrorCodeEnum.ERR_22229.setErrorCode(result);
				alert.prepareError("Error while getting records for legalEntities.").log();
				result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else if (records.has("dbpErrMsg")) {
				alert.prepareError("DBP error occured").log();
				result = CommonUtilities.constructResultFromJSONObject(records);
            	result.addParam(new Param("status", "Failure", FabricConstants.STRING));
				return result;
			} else {
				result = CommonUtilities.constructResultFromJSONObject(records);
			}
			return result;
		}
	 catch (DBPApplicationException e) {
		 	alert.prepareError("Unexepected Error in get company legal units details", e).log();
	 	} catch (Exception e) {
	 		alert.prepareError("Unexepected Error in get company legal units details", e).log();
	 	}
		result.addParam(new Param("FailureReason", "Failed to fetch company legal units details", FabricConstants.STRING));
		ErrorCodeEnum.ERR_22228.setErrorCode(result);
		return null;
		
	}
}
