package com.infinity.dbx.temenos.accounts;

import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.TemenosPreLoginBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAccountsByOrganisationIdPreProcessor extends TemenosPreLoginBasePreProcessor {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		try {
			super.execute(params, request, response, result);
			String organizationId = CommonUtils.getParamValue(params, AccountsConstants.PARAM_ORGANISATION_ID);
			
			if (StringUtils.isBlank(organizationId)) {
				organizationId = CommonUtils.getUserAttributeFromIdentity(request, AccountsConstants.PARAM_ORGANISATION_ID);
			}

			if (StringUtils.isNotBlank(organizationId)) {

				HashMap<String, Object> inputParams = new HashMap<String, Object>();
				inputParams.put(AccountsConstants.FILTER, AccountsConstants.ORGANISATION_FILTER + organizationId);
				request.addRequestParam_(AccountsConstants.FILTER, AccountsConstants.ORGANISATION_FILTER + organizationId);
				Result userAccounts = CommonUtils.callIntegrationService(request, inputParams, null,
						SERVICE_BACKEND_CERTIFICATE, OP_ACCOUNTS_GET, true);
				String accounts = "";
				Dataset customerAccounts = userAccounts.getDatasetById(AccountsConstants.DS_DB_ACCOUNTS);
				if(customerAccounts !=null && customerAccounts.getAllRecords().size() > 0)
				{
					for (Record rec : customerAccounts.getAllRecords()) {
						accounts += rec.getParamValueByName(AccountsConstants.DB_ACCOUNTID);
						accounts += " ";
					}
				}
				params.put(AccountsConstants.DB_ACCOUNTID,
						URLEncoder.encode(accounts.substring(0, accounts.length() - 1), "UTF-8"));
				alert.prepareError("Final Params " + params.toString()).log();

				return Boolean.TRUE;

			}

			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		} catch (Exception e) {
			alert.prepareError("Exception Occured while getting Organization accounts").log();
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			return Boolean.FALSE;
		}
	}
}
