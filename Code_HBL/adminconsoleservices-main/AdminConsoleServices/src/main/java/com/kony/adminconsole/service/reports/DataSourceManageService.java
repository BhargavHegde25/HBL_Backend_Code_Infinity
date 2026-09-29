package com.kony.adminconsole.service.reports;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.FabricConstants;
import com.kony.adminconsole.commons.utils.ODataQueryConstants;
import com.kony.adminconsole.core.config.EnvironmentConfiguration;
import com.kony.adminconsole.core.security.LoggedInUserHandler;
import com.kony.adminconsole.core.security.UserDetailsBean;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.kony.adminconsole.utilities.Executor;
import com.kony.adminconsole.utilities.ServiceURLEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class DataSourceManageService implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	List<String> reportsNameList = new ArrayList<String>();
	private String baseUrl = "https://api.kony.com/api/v1_1/";
	private static String url = "https://accounts.auth.konycloud.com/login";
	private String accountsUrl = baseUrl + "accounts";
	private String whoamiUrl = baseUrl + "whoami";
	private String accountId;
	private String userGuid;
	private String reportName;
	private String extension = "html";
	private String path;
	private String mode = "shared";
	private String reportType = "custom";
	private boolean isCloud = false;
	private boolean hasFilter = false;
	private static final String GET_LIST_OF_DATASOURCES = "getListOfDataSources";
	private static final String GET_DATASOURCE_ADDED_REPORTS = "getDataSourceAddedReports";
	private static final String GET_DATASOURCE_ALL_REPORTS = "getDataSourceAllReports";
	private static final String GENERATE_REPORT_OPERATION_NAME = "generateReport";
	private static final String DOWNLOAD_REPORT_OPERATION_NAME = "downloadReport";
	private static final String CLAIMS_TOKEN_KEY = "claims_token";
	private HashMap<String, Object> inputs = new HashMap<String, Object>();

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
			DataControllerResponse responseInstance) throws Exception {
		try {
			String authToken = requestInstance.getHeader(FabricConstants.X_KONY_AUTHORIZATION_HEADER);

			UserDetailsBean userDetailsBeanInstance = LoggedInUserHandler.getUserDetails(requestInstance);
			String userID = userDetailsBeanInstance.getId();
			if (methodID.equalsIgnoreCase(GET_LIST_OF_DATASOURCES)) {
				return getListOfDataSources(requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(GET_DATASOURCE_ADDED_REPORTS)) {
				String datasource = requestInstance.getParameter("datasource");
				return getDataSourceAddedReports(userID, datasource, requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(GET_DATASOURCE_ALL_REPORTS)) {
				String datasource = requestInstance.getParameter("datasource");
				return getDataSourceAllReports(userID, datasource, requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(GENERATE_REPORT_OPERATION_NAME)) {
				String reportId = requestInstance.getParameter("reportId");
				// return generateReport(userID, reportId, requestInstance, authToken);
			} else if (methodID.equalsIgnoreCase(DOWNLOAD_REPORT_OPERATION_NAME)) {
				String reportId = requestInstance.getParameter("reportId");
				// return downloadReport(userID, reportId, requestInstance, authToken);
			}
			return new Result();
		} catch (Exception e) {
			Result errorResult = new Result();
			diagnostic.prepareDebug("Runtime Exception.Exception Trace:", e).log();
			ErrorCodeEnum.ERR_20001.setErrorCode(errorResult);
			return errorResult;
		}
	}

	private Result getDataSourceAllReports(String userID, String datasource, DataControllerRequest requestInstance,
			String authToken) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		Result processedResult = new Result();
		HttpHeaders headers = new HttpHeaders();
		String reportType = "custom";
		String fabricClaimsToken = getFabricClaimsToken(reportType, requestInstance);
		headers.set("X-Kony-Authorization", fabricClaimsToken);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HttpEntity entity = new HttpEntity(headers);
		processedResult = getAccountDetails(entity, restTemplate, reportType);
		String session = processedResult.getParamByName("jasper_session").toString();
		String awselbSession = processedResult.getParamByName("awselb_session").toString();
		String accountGuid = processedResult.getParamByName("accountGuid").toString();
		List<String> reportsList = getReportsList(session, awselbSession, accountGuid, entity, restTemplate,
				reportType);
		Dataset dataset = new Dataset();
		dataset.setId("records");
		for (String report : reportsList) {
			Record currRecord = new Record();
			currRecord.addParam(new Param("id", report, FabricConstants.STRING));
			currRecord.addParam(new Param("description", "default description", FabricConstants.STRING));
			dataset.addRecord(currRecord);
		}

		// Add current Dataset to Result Object
		processedResult.addDataset(dataset);
		return processedResult;
	}

	private Result getDataSourceAddedReports(String userID, String datasource, DataControllerRequest requestInstance,
			String authToken) throws Exception {
		Result processedResult = new Result();
		Dataset reportsDataset = new Dataset();
		reportsDataset.setId("reports");
		Map<String, String> postParametersMap = new HashMap<String, String>();
		postParametersMap.clear();
		postParametersMap.put(ODataQueryConstants.FILTER, "(reportDataSourceId eq '" + datasource + "')");
		String readDataSourceResponse = Executor.invokeService(ServiceURLEnum.REPORT_READ, postParametersMap, null,
				requestInstance);
		JSONObject readReportsResponseJSON = CommonUtilities.getStringAsJSONObject(readDataSourceResponse);
		if (readReportsResponseJSON != null && readReportsResponseJSON.has(FabricConstants.OPSTATUS)
				&& readReportsResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readReportsResponseJSON.has("report")) {
			JSONArray reportRecordsJSONArray = readReportsResponseJSON.getJSONArray("report");
			for (int indexVar = 0; indexVar < reportRecordsJSONArray.length(); indexVar++) {
				JSONObject currJSONObject = reportRecordsJSONArray.getJSONObject(indexVar);

				Record currRecord = new Record();
				currRecord.addParam(new Param("id", currJSONObject.optString("id"), FabricConstants.STRING));
				currRecord.addParam(new Param("name", currJSONObject.optString("name"), FabricConstants.STRING));
				reportsDataset.addRecord(currRecord);
			}
		}

		processedResult.addDataset(reportsDataset);
		return processedResult;
	}

	private Result getListOfDataSources(DataControllerRequest requestInstance, String authToken) throws Exception {
		Result processedResult = new Result();
		Dataset datasourcessDataset = new Dataset();
		datasourcessDataset.setId("datasources");
		Map<String, String> postParametersMap = new HashMap<String, String>();
		// TODO Auto-generated method stub
		postParametersMap.clear();
		String readDataSourceResponse = Executor.invokeService(ServiceURLEnum.DATASOURCE_READ, postParametersMap, null,
				requestInstance);

		JSONObject readDataSourceResponseJSON = CommonUtilities.getStringAsJSONObject(readDataSourceResponse);
		if (readDataSourceResponseJSON != null && readDataSourceResponseJSON.has(FabricConstants.OPSTATUS)
				&& readDataSourceResponseJSON.getInt(FabricConstants.OPSTATUS) == 0
				&& readDataSourceResponseJSON.has("datasource")) {
			JSONArray reportDataSourceRecordsJSONArray = readDataSourceResponseJSON.getJSONArray("datasource");

			for (int indexVar = 0; indexVar < reportDataSourceRecordsJSONArray.length(); indexVar++) {
				JSONObject currJSONObject = reportDataSourceRecordsJSONArray.getJSONObject(indexVar);
				Record currRecord = new Record();
				currRecord.addParam(new Param("id", currJSONObject.optString("id"), FabricConstants.STRING));
				currRecord.addParam(
						new Param("type", currJSONObject.optString("dataSourceTypeId"), FabricConstants.STRING));
				currRecord.addParam(new Param("name", currJSONObject.optString("name"), FabricConstants.STRING));
				currRecord.addParam(new Param("schema", currJSONObject.optString("schema"), FabricConstants.STRING));
				datasourcessDataset.addRecord(currRecord);

			}
		}
		processedResult.addDataset(datasourcessDataset);
		return processedResult;
	}

	public static String getFabricClaimsToken(String reportType, DataControllerRequest dataControllerRequest)
			throws Exception {
		String[] loginKeys = authenticateToFabric(reportType, dataControllerRequest);
		return loginKeys[0];
	}

	private static String[] authenticateToFabric(String reportType, DataControllerRequest dataControllerRequest)
			throws Exception {
		System.out.println("Connecting to the Reports Portal...");
		String[] loginKeys = new String[2];
		MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();
		bodyMap.add("userid", EnvironmentConfiguration.AC_FABRIC_LOGIN_USERNAME.getValue(dataControllerRequest));
		bodyMap.add("password", EnvironmentConfiguration.AC_FABRIC_LOGIN_PASSWORD.getValue(dataControllerRequest));
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response1 = restTemplate.postForEntity(url, bodyMap, String.class);
		JSONObject fabricLoginResponseJSON = new JSONObject(response1.getBody().toString());

		if (fabricLoginResponseJSON != null && fabricLoginResponseJSON.has(CLAIMS_TOKEN_KEY)
				&& StringUtils.isNotBlank(fabricLoginResponseJSON.optString(CLAIMS_TOKEN_KEY))) {
			diagnostic.prepareDebug("Fabric Login Successful").log();
			loginKeys[0] = (String) fabricLoginResponseJSON.getJSONObject("claims_token").get("value");
		} else {
			alert.prepareError("Failed to authenticate to Fabric. No Tokens found on response.").log();
			throw new Exception();
		}
		return loginKeys;
	}

	public UriComponentsBuilder buildParams(String url, Map<String, Object> params) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		for (Map.Entry<String, Object> control : params.entrySet()) {
			builder.queryParam(control.getKey(), control.getValue());
		}

		return builder;
	}

	public void writeToHTMLFile(ResponseEntity downloadResponse, String repName) throws IOException, JSONException {
		if (downloadResponse.getBody() != null) {

			JSONObject json = new JSONObject(downloadResponse.getBody().toString());
			String str = (String) json.get("html");
			FileWriter fw = new FileWriter(repName + "." + extension);
			fw.write(str);
			fw.close();

		} else {

			System.out.println("There is No data");
		}

	}

	// Writing the report data into a csv file
	public void writeToFile(ResponseEntity downloadResponse, String repName) throws IOException {
		if (downloadResponse.getBody() != null) {
			InputStream stream = new ByteArrayInputStream(downloadResponse.getBody().toString().getBytes("UTF-8"));
			FileOutputStream outputStream = new FileOutputStream(repName + "." + extension);
			int bytesread = -1;
			byte[] buffer = new byte[4096];
			try {
				while ((bytesread = stream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesread);
				}
			} finally {
				stream.close();
				outputStream.close();

			}
		} else {

			System.out.println("There is No data");
		}

	}

	public Map<String, Object> getStandardReportsControls(JSONArray inputControlsArray) throws JSONException {
		Map<String, Object> controls = new HashMap<String, Object>();

		for (int i = 0; i < inputControlsArray.length(); i++) {
			JSONObject inputControl = inputControlsArray.getJSONObject(i);
			try {
				JSONArray values = inputControl.getJSONObject("state").getJSONArray("options");
				byte index = 0;
				if (values.length() != 0) {
					if (inputControl.getString("id").equals("eid")) {
						index = 1;
					}
					controls.put(inputControl.getString("id"), values.getJSONObject(index).getString("value"));
				}
			} catch (Exception exception) {
				String value = inputControl.getJSONObject("state").getString("value");
				controls.put(inputControl.getString("id"), value);
			}
		}
		return controls;
	}

//    Helper function to extract the input control key value pairs
	public Map<String, Object> getControls(JSONArray inputControlsArray) throws JSONException {
		Map<String, Object> controls = new HashMap<String, Object>();

		for (int i = 0; i < inputControlsArray.length(); i++) {
			JSONObject inputControl = inputControlsArray.getJSONObject(i);
			JSONArray values = inputControl.getJSONObject("state").getJSONArray("options");
			for (int j = 0; j < values.length(); j++) {
				if ((inputs.containsKey("appName")
						&& values.getJSONObject(j).getString("value").equals(inputs.get("appName")))
						|| (inputs.containsKey("channel")
								&& values.getJSONObject(j).getString("value").equals(inputs.get("channel")))) {
					controls.put(inputControl.getString("id"), values.getJSONObject(j).getString("value"));
					break;
				}
			}
		}
		return controls;
	}

	public List<String> getReportsList(String session, String awselbSession, String accountGuid, HttpEntity entity,
			RestTemplate restTemplate, String reportType) throws IOException, JSONException {
		if (!("list".equals(reportName) || "filters".equals(reportName))) {
			reportsNameList.add(reportName);
			return reportsNameList;
		}
		String url = accountsUrl + "/" + accountGuid + "/reports/";
		if ("private".equals(mode)) {
			path = String.format(path, userGuid, accountId);
		}
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("jasperSession", session);
		if (isCloud) {
			queryParams.put("awselbSession", awselbSession);
		}
		queryParams.put("viewType", mode);
		UriComponentsBuilder inputcontrols = buildParams(url + "adhoc", queryParams);
		ResponseEntity inputControlResponse = restTemplate.exchange(inputcontrols.build().toUri(), HttpMethod.GET,
				entity, String.class);
		JSONObject responseAsJSON = new JSONObject(inputControlResponse.getBody().toString());

		JSONArray customReports = responseAsJSON.getJSONArray("resourceLookup");

		if (customReports != null && customReports.length() != 0) {
			for (int i = 0; i < customReports.length(); i++) {
				JSONObject reportseAsJSON = customReports.getJSONObject(i);
				String resourceType = reportseAsJSON.optString("resourceType");
				if (resourceType.equalsIgnoreCase("reportUnit")) {
					String reportDataSourceURI = reportseAsJSON.optString("uri");
					int index = reportDataSourceURI.lastIndexOf("/shared_/");
					if (index == -1) {
						return reportsNameList;
					}
					System.out.println(reportDataSourceURI.substring(9));
					reportsNameList.add(reportDataSourceURI.substring(9));
				}
			}

		}
		return reportsNameList;

	}

	public void getAndDownloadReport(String session, String awselbSession, String accountGuid, HttpEntity entity,
			RestTemplate restTemplate, String reportType) throws IOException, JSONException {
		if ("list".equals(reportName)) {
			return;
		}
		String url = accountsUrl + "/" + accountGuid + "/reports/";
		for (int i = 0; i < reportsNameList.size(); i++) {
			try {
				if ("private".equals(mode)) {
					path = String.format(path, userGuid, accountId);
				} else {
					path = "/shared_/" + reportsNameList.get(i);
				}
				Map<String, Object> queryParams = new HashMap<String, Object>();
				queryParams.put("jasperSession", session);
				if (isCloud) {
					queryParams.put("awselbSession", awselbSession);
				}
				queryParams.put("resourceUri", path);
				UriComponentsBuilder inputcontrols = buildParams(url + "inputControls", queryParams);

				ResponseEntity inputControlResponse = restTemplate.exchange(inputcontrols.build().toUri(),
						HttpMethod.GET, entity, String.class);
				String filters = "";
				if (inputControlResponse.getBody() != null) {
					JSONArray jsonArray = new JSONArray(inputControlResponse.getBody().toString());
					if (hasFilter) {
						System.out.println("filters responce: " + inputControlResponse.getBody().toString());
						return;
					}
					if (jsonArray.length() != 0) {
						Map<String, Object> controls;
						controls = getStandardReportsControls(jsonArray);
						queryParams.remove("inputControls");
						queryParams.put("inputControlValues", true);

						for (Map.Entry<String, Object> control : controls.entrySet()) {
							if (inputs.containsKey(control.getKey())) {
								control.setValue(inputs.get(control.getKey()));
							}
							if (!control.getKey().contains("report_table")) {
								filters += control.getKey() + ",";
							}
							queryParams.put(control.getKey(), control.getValue());
						}

					}
					if (hasFilter && filters.equals("")) {
						System.out.println("No filters available");
					}
					// UriComponentsBuilder inputcontrolvalues = buildParams(url+
					// "inputControlValues", queryParams);
					// restTemplate.exchange(inputcontrolvalues.build().encode().toUri(),
					// HttpMethod.GET, entity, String.class);

					queryParams.remove("inputControlValues");
					queryParams.put("filters", filters);
					System.out.println("Applied filters: " + filters);
				}
				queryParams.put("filters", filters + "startyear,startmonth,endyear,endmonth");
				queryParams.put("export", true);
				queryParams.put("file_extension", extension);
				queryParams.put("type", "html");
				UriComponentsBuilder builder = buildParams(url + "view", queryParams);
				System.out.println(queryParams.toString());
				ResponseEntity downloadResponse = restTemplate.exchange(builder.build().encode().toUri(),
						HttpMethod.GET, entity, String.class);
				writeToHTMLFile(downloadResponse, reportsNameList.get(i));
				System.out.println("Done");
			}

			catch (Exception e) {
				System.out.println(e);
				System.out.println(
						"Data Could not be retrieved because it might have exceeded the limit. Please apply appropriate filters and try again");

			}
		}
	}

	public void writeToFile(ResponseEntity downloadResponse) throws IOException {
		if (downloadResponse.getBody() != null) {
			InputStream stream = new ByteArrayInputStream(downloadResponse.getBody().toString().getBytes("UTF-8"));
			FileOutputStream outputStream = new FileOutputStream(reportName + "." + extension);
			int bytesread = -1;
			byte[] buffer = new byte[4096];
			try {
				while ((bytesread = stream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesread);
				}
			} finally {
				stream.close();
				outputStream.close();

			}
		} else {

			System.out.println("There is No data");
		}

	}

	@SuppressWarnings("rawtypes")
	public Result getAccountDetails(HttpEntity entity, RestTemplate restTemplate, String reportType)
			throws JSONException, IOException {
		Result processedResult = new Result();
		String accountGuid = null;

		ResponseEntity whoamiResponseEntity = restTemplate.exchange(whoamiUrl, HttpMethod.GET, entity, String.class);
		JSONObject whoami = new JSONObject(whoamiResponseEntity.getBody().toString());
		userGuid = whoami.getString("user_guid");
		if (accountId == null) {
			accountId = whoami.getString("last_selected_account_id");
		}
		JSONArray accounts = whoami.getJSONArray("accounts");
		for (int i = 0; i < accounts.length(); i++) {
			JSONObject account = accounts.getJSONObject(i);
			if (accountId.equals(account.get("account_id"))) {
				accountGuid = account.get("account_guid").toString();
				break;
			}
		}
		if (accountGuid == null) {
			String errorMessage = "There is no account with accountId :" + accountId + " for the given user.";
			processedResult.addParam(new Param("validationError", errorMessage, FabricConstants.STRING));
			return processedResult;
		}
		String jasperLoginUrl = accountsUrl + "/" + accountGuid + "/reports/login";
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("user_guid", userGuid);
		UriComponentsBuilder jasperLogin = buildParams(jasperLoginUrl, queryParams);
		ResponseEntity reportsResponseEntity = restTemplate.exchange(jasperLogin.build().toUri(), HttpMethod.GET,
				entity, String.class);
		JSONObject reportsConfig = new JSONObject(reportsResponseEntity.getBody().toString());
		String JSessionId = reportsConfig.getString("jasper_session");
		String awselbSession = reportsConfig.getString("awselb_session");

		processedResult.addParam(new Param("jasper_session", JSessionId, FabricConstants.STRING));
		processedResult.addParam(new Param("awselb_session", awselbSession, FabricConstants.STRING));
		processedResult.addParam(new Param("accountGuid", accountGuid, FabricConstants.STRING));
		return processedResult;

	}

}