package com.kony.adminconsole.preprocessor;

import com.konylabs.middleware.common.DataPreProcessor2;
import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.jwt.auth.utils.CommonUtilsC360;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class ProductsAuthorizationPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		Log4j2Configurator.getInstance();
		try {
			String coreInfo = (String) params.get(TemenosConstantsC360.CORE_IDENTIFIER);
			if (StringUtils.isNotBlank(coreInfo)) {
				String userID = CommonUtilsC360.getBackendId(coreInfo, TemenosConstantsC360.CONSTANT_TEMPLATE_NAME);
				if (StringUtils.isNotBlank(userID)) {
					params.put(TemenosConstantsC360.USER_ID, userID);
				}
			}
			String branchRef = EnvironmentConfiguration.BRANCH_ID_REFERENCE.getValue(request);
			if (StringUtils.isNotBlank(branchRef)) {
				request.addRequestParam_("branchRef", branchRef);
				params.put("branchRef", branchRef);
			} else {
				request.addRequestParam_("branchRef", "GB0010001");
				params.put("branchRef", "GB0010001");
			}
				
			String productGrpName = request.getParameter("productGroupName");
			productGrpName = StringEscapeUtils.escapeHtml(productGrpName);

			String detailedDesc = request.getParameter("detailedDesc");
			detailedDesc = StringEscapeUtils.escapeHtml(detailedDesc);
			
			params.put("productGroupName", productGrpName);
			params.put("detailedDesc", detailedDesc);
			
			request.updateOriginalRequest(params);
			
			try {
			CommonUtilsC360.setCloudAuthenticationHeaders(request,TemenosConstantsC360.MARKETING_CATALOGUE_DEPLOYMENT_PLATFORM,TemenosConstantsC360.MARKETING_CATALOGUE_AUTHORIZATION_KEY);
			CommonUtilsC360.setAuthenticationHeader(request,
					TemenosConstantsC360.POST_LOGIN_FLOW, "ms");
			} catch (Exception e) {
				alert.prepareError("Exception in generating JWT authToken for Marketing catalog", e).log();
			}
			
			 String authToken = request.getParameter(TemenosConstantsC360.AUTHORIZATION);
			 if(StringUtils.isBlank(authToken)) {
				String authTokenFromParam = request.getHeader("Authorization"); 
				request.addRequestParam_(TemenosConstantsC360.AUTHORIZATION, authTokenFromParam);
			 }			
			return true;
		} catch (Exception e) {
			alert.prepareError("Exception in generating JWT authToken for Marketing catalog", e).log();
			return Boolean.FALSE;
		}

	}

	
}
