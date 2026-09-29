/**
 * 
 */
package com.temenos.infinity.wealth.tap.processor.pre;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
import com.temenos.infinity.api.wealthservices.tap.preandpostprocessors.TAPTokenGenPreProcessor;

/**
 * @author muthukumarv
 *
 */
public class UpdateEntityTAPPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
			diagnostic.prepareDebug("==========> UpdateEntityTAPPreProcessor TAP - Entered ").log();
			if (request.getParameter(TemenosConstants.WEALTH_CORE) != null
					&& (request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP,Refinitiv")
							|| request.getParameter(TemenosConstants.WEALTH_CORE).equalsIgnoreCase("TAP"))) {
			TAPTokenGenPreProcessor obj = new TAPTokenGenPreProcessor();
			obj.execute(inputMap, request, response, result);
			String businessEntityId = LegalEntityUtil.getLegalEntityIdFromSessionOrCache(request);
			//String businessEntityId = "GB0010001";
			if(businessEntityId == null || StringUtils.isEmpty(businessEntityId)) {
				return false;
			}else {
				inputMap.put("businessEntityId", businessEntityId);
				request.addRequestParam_("businessEntityId", businessEntityId);
			}
			diagnostic.prepareDebug("==========>  UpdateEntityTAPPreProcessor TAP - Token Generation Succeeded").log();
			return true;
			}
			else {
				result.addOpstatusParam("0");
				result.addHttpStatusCodeParam("200");
				result.addParam(TemenosConstants.STATUS, TemenosConstants.SUCCESS);
				diagnostic.prepareDebug("==========>  UpdateEntityTAPPreProcessor TAP - Exiting without Token Generation").log();
				return false;
			}
		} catch (Exception e) {
			alert.prepareError("==========>  UpdateEntityTAPPreProcessor TAP - Error: " + e.getMessage()).log();
			e.getMessage();
		}
		return false;
	}

}
