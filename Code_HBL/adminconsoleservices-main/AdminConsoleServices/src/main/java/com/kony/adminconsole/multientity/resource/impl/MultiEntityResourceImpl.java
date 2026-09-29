/**
 * 
 */
package com.kony.adminconsole.multientity.resource.impl;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.handler.ApplicationParametersHandler;
import com.kony.adminconsole.multientity.businessdelegate.api.MultiEntityBusinessDelegate;
import com.kony.adminconsole.multientity.resource.api.MultiEntityResource;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

/**
 * @author abhishek.jain
 *
 */
public class MultiEntityResourceImpl implements MultiEntityResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	
	@Override
	public Result getAllCompanyLegalUnits(DataControllerRequest dcRequest) {
		
		MultiEntityBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(MultiEntityBusinessDelegate.class);

		Result result = new Result();
		boolean singleentity = false;
		Object sessionId = dcRequest.getSession().getId();
		singleentity = Boolean.parseBoolean(ApplicationParametersHandler.fetchIsSingleEntity(dcRequest));
		alert.prepareError("SingleEntity:" + singleentity).log();
		String authToken = CommonUtilities.getAuthToken(dcRequest);
		try {
			Result companyLegalUnits = businessDelegate.getAllCompanyLegalUnits(authToken, singleentity, sessionId );
			return companyLegalUnits;
		} catch (Exception e) 
		    {
			alert.prepareError("Unexepected Error in get company legal units details", e).log();
			result.addParam(new Param("FailureReason", e.getMessage(), FabricConstants.STRING));
			ErrorCodeEnum.ERR_22228.setErrorCode(result);
			return result;
		    }

	}
}