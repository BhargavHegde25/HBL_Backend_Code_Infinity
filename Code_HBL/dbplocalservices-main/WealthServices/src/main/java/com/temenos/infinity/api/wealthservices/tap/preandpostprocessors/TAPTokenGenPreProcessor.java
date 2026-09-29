/**
 * 
 */
package com.temenos.infinity.api.wealthservices.tap.preandpostprocessors;

import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.vo.TokenVO;
import com.temenos.infinity.api.wealthservices.util.JWTTokenUtil;


/**
 * @author himaja.sridhar
 *
 *         (INFO) Generates TAP Token. Needs to be included for all TAP
 *         services.
 *
 */
public class TAPTokenGenPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "rawtypes", "static-access" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		try {
				
				JWTTokenUtil jWTTokenUtil = new JWTTokenUtil();
				
				TokenVO tokenVO = jWTTokenUtil.setParamsForToken(request);
				String backendToken = jWTTokenUtil.computeJWTToken(tokenVO,request);
				
				if (StringUtils.isBlank(backendToken)) {
					return false;
				}
				
				backendToken = "Bearer ".concat(backendToken);
				
				//PortfolioWealthUtils.saveTokenIntoSession(backendToken, companyIdValue);
				request.addRequestParam_("Authorization", backendToken);
				request.addRequestParam_("x-channel", "PCK_TCIB_PM_DESKTOP");
				return true;
		} catch (Exception e) {
			e.getMessage();
		}
		return false;
	}
	


}
