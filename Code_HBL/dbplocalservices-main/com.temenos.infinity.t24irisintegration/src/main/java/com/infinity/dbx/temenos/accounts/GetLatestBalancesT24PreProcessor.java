package com.infinity.dbx.temenos.accounts;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.TokenUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetLatestBalancesT24PreProcessor extends TemenosBasePreProcessor {
	
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	
    @SuppressWarnings({ "rawtypes" })
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
		alert.prepareError("input Params for GetLatestbalances T24: "+params).log();
		String companyId = (String) request.getHeader("companyId");
		if(StringUtils.isBlank(companyId)) {
			companyId = LegalEntityUtil.getCurrentLegalEntityIdFromCache(request);
		}
		String authToken = TokenUtils.getT24AuthToken(request);
		request.getHeaderMap().put("Authorization", authToken);
		request.getHeaderMap().put("companyId", companyId);
		alert.prepareError("input Params for GetLatestbalances T24: "+params).log();	
		return Boolean.TRUE;
    }
}
