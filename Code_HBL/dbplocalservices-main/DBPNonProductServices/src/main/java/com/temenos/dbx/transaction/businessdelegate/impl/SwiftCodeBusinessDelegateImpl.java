package com.temenos.dbx.transaction.businessdelegate.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.fabric.extn.DBPServiceInvocationWrapper;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.dbx.transaction.businessdelegate.api.SwiftCodeBusinessDelegate;
import com.temenos.dbx.transaction.dto.SwiftCodeDTO;

public class SwiftCodeBusinessDelegateImpl implements SwiftCodeBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	public static final String serviceName = "dbpRbLocalServicesdb";
	public static final String operationName = EnvironmentConfigurationsHandler.getValue("DBX_SCHEMA_NAME")
			+ "_swiftcode_get";

	@Override
	public List<SwiftCodeDTO> getSwiftBICcode(String bankName, String city, String country, String branchName) {

		List<SwiftCodeDTO> dtos = null;

		Map<String, Object> requestParams = new HashMap<String, Object>();

		StringBuilder filter = new StringBuilder();
		filter.append("substringof('bankName','" + bankName + "') eq true and ");
		filter.append("substringof('city','" + city + "') eq true and ");
		filter.append("substringof('country','" + country + "') eq true and ");
		filter.append("substringof('branchName','" + branchName + "') eq true");

		requestParams.put("$filter", filter.toString());
		requestParams.put("$orderby", "id");
		requestParams.put("$top", "50");
		requestParams.put("$select", "bankName, bic, city, country, countryRegion, branchName, bankAddress, zipcode");

		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray countJsonArray = jsonRsponse.getJSONArray("swiftcode");
			dtos = JSONUtils.parseAsList(countJsonArray.toString(), SwiftCodeDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Exception occured while fetching", exp).log();
			return null;
		}

		return dtos;
	}
	@Override
	public List<SwiftCodeDTO> getSwiftBICcode(String iban) {

		List<SwiftCodeDTO> dtos = null;

		Map<String, Object> requestParams = new HashMap<String, Object>();

		requestParams.put("$filter", "countryCode eq '"+iban.substring(0, 2) +"' and countryRegion eq DOMESTIC");
		requestParams.put("$orderby", "id");
		requestParams.put("$top", "1");
		requestParams.put("$select", "bankName, bic, city, country, countryRegion, branchName, bankAddress, zipcode");
		

		try {
			String fetchResponse = DBPServiceExecutorBuilder.builder().
					withServiceId(serviceName).
					withObjectId(null).
					withOperationId(operationName).
					withRequestParameters(requestParams).
					build().getResponse();

			JSONObject jsonRsponse = new JSONObject(fetchResponse);
			JSONArray countJsonArray = jsonRsponse.getJSONArray("swiftcode");
			dtos = JSONUtils.parseAsList(countJsonArray.toString(), SwiftCodeDTO.class);
		}

		catch (JSONException jsonExp) {
			alert.prepareError("JSONExcpetion occured while fetching", jsonExp).log();
			return null;
		} catch (Exception exp) {
			alert.prepareError("Exception occured while fetching", exp).log();
			return null;
		}

		return dtos;
	}

}
