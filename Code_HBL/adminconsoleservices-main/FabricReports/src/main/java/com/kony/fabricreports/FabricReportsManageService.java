package com.kony.fabricreports;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kony.fabricreports.util.HelperMethods;
import com.kony.fabricreports.util.ReportsConstants;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class FabricReportsManageService {

	private FabricReportsManageService() {

	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	private static String baseUrl;
	private static String baseUrlforwhoami;
	private static String url;
	private static String accountsUrl;
	private static String whoamiUrl;
	private static String accountId;
	private static String mode = "shared";
	private static boolean isCloud;
	private static final String CLAIMS_TOKEN_KEY = "claims_token";
	private static String fabricClaimsToken;
	private static String awselbSession;
	private static String jSessionId;

	public static JSONArray processReportsAction(int pageno, String methodId, String... input) throws Exception {
		setUrls();
		Result processedResult;
		RestTemplate restTemplate = new RestTemplate();
		if (fabricClaimsToken == null)
			fabricClaimsToken = getFabricClaimsToken();
		boolean validSession = true;
		HttpHeaders headers = new HttpHeaders();
		headers.set(ReportsConstants.X_KONY_AUTH, fabricClaimsToken);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HttpEntity entity = new HttpEntity(headers);
		ResponseEntity whoamiResponseEntity = null;
		whoamiResponseEntity = checkSession(restTemplate, entity, whoamiResponseEntity);
		if (whoamiResponseEntity == null) {
			fabricClaimsToken = getFabricClaimsToken();
			headers = new HttpHeaders();
			headers.set(ReportsConstants.X_KONY_AUTH, fabricClaimsToken);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			HttpEntity entity1 = new HttpEntity(headers);
			whoamiResponseEntity = checkSession(restTemplate, entity1, whoamiResponseEntity);
			validSession = false;
		}
		if (whoamiResponseEntity != null) {
			processedResult = getAccountDetails(headers, restTemplate, whoamiResponseEntity, validSession);
			Map<String, Object> queryParams = new HashMap<>();

			if (processedResult.getParamByName(ReportsConstants.VALIDATIONERROR) == null) {

				String session = processedResult.getParamByName(ReportsConstants.JASPERSESSION).getValue();
				String awselbSession = processedResult.getParamByName(ReportsConstants.AWSELBSESSION).getValue();
				String accountGuid = processedResult.getParamByName(ReportsConstants.ACCOUNTSGUID).getValue();

				if (methodId.equals(ReportsConstants.GET_LIST_OF_REPORTS))
					return getReportsList(session, awselbSession, accountGuid, headers, restTemplate, queryParams);
				if (methodId.equals(ReportsConstants.GET_FILTERS_FOR_REPORT))
					return getFiltersForReport(session, awselbSession, accountGuid, headers, restTemplate, input[0],
							queryParams);
				if (methodId.equals(ReportsConstants.VIEW_REPORT)) {
					HashMap<String, Object> inputs = new HashMap<>();
					processInputParameters(inputs, input[1]);
					String extension = ReportsConstants.HTML;
					String queryParamVal = "view";
					return writeToHTMlFile(viewReport(session, awselbSession, accountGuid, headers, restTemplate,
							input[0], queryParams, inputs, extension, pageno, queryParamVal));
				}
				if (methodId.equals(ReportsConstants.EXECUTIONS)) {
					HashMap<String, Object> inputs = new HashMap<>();
					processInputParameters(inputs, input[1]);
					String extension = ReportsConstants.HTML;
					String queryParamVal = "executions";
					ResponseEntity res = viewReport(session, awselbSession, accountGuid, headers, restTemplate,
							input[0], queryParams, inputs, extension, pageno, queryParamVal);
					return getTotalPages(res);
				}
				if (methodId.equals(ReportsConstants.DOWNLOAD_REPORT)) {
					String fileType = input[2];
					HashMap<String, Object> inputs = new HashMap<>();
					processInputParameters(inputs, input[1]);
					String queryParamVal = "view";
					return downloadReport(viewReport(session, awselbSession, accountGuid, headers, restTemplate,
							input[0], queryParams, inputs, fileType, pageno, queryParamVal), fileType);
				}
				alert.prepareError("Invalid methodId.").log();
			}

		}
		return null;
	}

	private static ResponseEntity checkSession(RestTemplate restTemplate, HttpEntity entity,
			ResponseEntity whoamiResponseEntity) {
		try {
			whoamiResponseEntity = restTemplate.exchange(whoamiUrl, HttpMethod.GET, entity, String.class);
		} catch (Exception e) {
			alert.prepareError("invalid session ", e).log();
			return null;

		}
		return whoamiResponseEntity;
	}

	private static JSONArray getTotalPages(ResponseEntity res) {
		JSONArray js = new JSONArray();
		try {
			JsonElement jj = new JsonParser().parse(res.getBody().toString());
			JsonElement totalPages = null;
			if (jj.isJsonObject())
				totalPages = jj.getAsJsonObject().get("totalPages");
			if (totalPages != null) {
				JSONObject newobj = new JSONObject();
				newobj.put("totalPages", totalPages.getAsString());
				js.put(newobj);
			}
		} catch (Exception e) {
			alert.prepareError("Error in fetching total records", e).log();
		}
		return js;
	}

	private static JSONArray writeToHTMlFile(ResponseEntity downloadResponse) {
		if (downloadResponse.getBody() != null) {
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject(downloadResponse.getBody().toString());
			array.put(json);
			return array;
		}
		return null;

	}

	private static JSONArray downloadReport(ResponseEntity downloadResponse, String fileType) {
		if (downloadResponse.getBody() != null) {
			JSONArray array = new JSONArray();
			JSONObject json = new JSONObject();
			json.put(fileType, downloadResponse.getBody().toString());
			array.put(json);
			return array;
		}
		return null;

	}

	private static void processInputParameters(HashMap<String, Object> inputs, String filters) {
		if (filters != null && !StringUtils.isBlank(filters)) {
			String[] parts = filters.split(",");
			for (String part : parts) {
				String[] values = part.split("=");
				inputs.put(values[0], values[1]);
			}
		}
	}

	private static JSONArray getFiltersForReport(String session, String awselbSession, String accountGuid,
			HttpHeaders headers, RestTemplate restTemplate, String reportName, Map<String, Object> queryParams)
			throws Exception {
		@SuppressWarnings("unchecked")
		HttpEntity entity = new HttpEntity(headers);
		String url = getUrl(accountGuid);
		String path = ReportsConstants.SHARED + reportName;
		getQueryparams(queryParams, session, awselbSession);
		queryParams.put("resourceUri", path);
		UriComponentsBuilder inputcontrols = buildParams(url + "inputControls", queryParams);
		ResponseEntity inputControlResponse = restTemplate.exchange(inputcontrols.build().toUri(), HttpMethod.GET,
				entity, String.class);
		if (inputControlResponse.getBody() != null) {
			return new JSONArray(inputControlResponse.getBody().toString());
		}

		return new JSONArray();
	}

	private static void setUrls() {
		if (baseUrl == null || url == null) {
			String fabricUrl = HelperMethods.getEnvConfigValue("REPORTS_FABRIC_URL");
			if (fabricUrl != null && fabricUrl.indexOf("manage.") > -1) {
				String pattern = "(.*)manage.(.*)(kony|temenos-cloud).com";
				String replace = "$1api.$2$3.com";
				String replaceAuth = "";
				
				if(fabricUrl.contains("kony"))
					replaceAuth = "$1accounts.auth.$2konycloud.com/login";
				else if(fabricUrl.contains("temenos-cloud"))
					replaceAuth = "$1accounts.auth.$2temenos-cloud.net/login";
				
				Pattern p = Pattern.compile(pattern);
				Matcher m = p.matcher(fabricUrl);
				String accUrl = m.replaceAll(replace);
				String authUrl = m.replaceAll(replaceAuth);
				baseUrl = accUrl + "/api/v1_1/";
				baseUrlforwhoami = accUrl + "/api/v1_0/";
				url = authUrl;
				isCloud = true;
			} else {
				isCloud = false;
				baseUrl = fabricUrl + "/accounts/api/v1_0/";
				url = fabricUrl + "/authService/accounts/login";
				baseUrlforwhoami = fabricUrl + "/accounts/api/v1_0/";
			}
			accountsUrl = baseUrl + ReportsConstants.ACCOUNTS;
			whoamiUrl = baseUrlforwhoami + ReportsConstants.WHOAMI;
		}

	}

	public static String getFabricClaimsToken() throws Exception {
		String[] loginKeys = authenticateToFabric();
		return loginKeys[0];
	}

	private static String[] authenticateToFabric() throws Exception {
		diagnostic.prepareDebug("Connecting to the Reports Portal...").log();
		String[] loginKeys = new String[2];
		MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();

		bodyMap.add("userid", HelperMethods.getEnvConfigValue("REPORTS_FABRIC_LOGIN_USERNAME"));
		bodyMap.add("password", HelperMethods.getEnvConfigValue("REPORTS_FABRIC_LOGIN_PASSWORD"));

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response1 = restTemplate.postForEntity(url, bodyMap, String.class);
		if (response1 != null) {
			JSONObject fabricLoginResponseJSON = new JSONObject(response1.getBody().toString());
			if (fabricLoginResponseJSON.has(CLAIMS_TOKEN_KEY)
					&& StringUtils.isNotBlank(fabricLoginResponseJSON.optString(CLAIMS_TOKEN_KEY))) {
				diagnostic.prepareDebug("Fabric Login Successful").log();
				loginKeys[0] = (String) fabricLoginResponseJSON.getJSONObject(CLAIMS_TOKEN_KEY)
						.get(ReportsConstants.VALUE);
			} else {
				alert.prepareError("Failed to authenticate to Fabric. No Tokens found on response.").log();
				throw new Exception();
			}
			return loginKeys;
		}
		return loginKeys;
	}

	public static UriComponentsBuilder buildParams(String url, Map<String, Object> params) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		for (Map.Entry<String, Object> control : params.entrySet()) {
			builder.queryParam(control.getKey(), control.getValue());
		}

		return builder;
	}

	public static JSONArray getReportsList(String session, String awselbSession, String accountGuid,
			HttpHeaders headers, RestTemplate restTemplate, Map<String, Object> queryParams) throws Exception {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		HttpEntity entity = new HttpEntity(headers);
		String url = getUrl(accountGuid);
		getQueryparams(queryParams, session, awselbSession);
		queryParams.put("viewType", mode);
		UriComponentsBuilder inputcontrols = buildParams(url + ReportsConstants.ADHOC, queryParams);
		ResponseEntity inputControlResponse = restTemplate.exchange(inputcontrols.build().toUri(), HttpMethod.GET,
				entity, String.class);

		JSONArray reportsList = new JSONArray();
		if (inputControlResponse.getBody().toString().equals("No data to display"))
			return reportsList;
		JSONObject responseAsJSON = new JSONObject(inputControlResponse.getBody().toString());
		JSONArray customReports = responseAsJSON.getJSONArray("resourceLookup");

		if (customReports != null && customReports.length() != 0) {
			for (int i = 0; i < customReports.length(); i++) {
				JSONObject reportseAsJSON = customReports.getJSONObject(i);
				String resourceType = reportseAsJSON.optString("resourceType");
				if (resourceType.equalsIgnoreCase("reportUnit")) {
					JSONObject report = new JSONObject();
					String reportDataSourceURI = reportseAsJSON.optString("uri");
					int index = reportDataSourceURI.lastIndexOf(ReportsConstants.SHARED);
					if (index == -1) {
						return reportsList;
					}
					report.put("id", reportDataSourceURI.substring(9));
					report.put("description", reportseAsJSON.getString("description"));
					report.put("label", reportseAsJSON.getString("label"));
					reportsList.put(report);
				}
			}

		}
		return reportsList;

	}

	private static Map<String, Object> getQueryparams(Map<String, Object> queryParams, String session,
			String awselbSession) {
		queryParams.put(ReportsConstants.JASPERSESSIONFORPROCESS, session);
		if (isCloud) {
			queryParams.put("awselbSession", awselbSession);
		}
		return queryParams;

	}

	private static String getUrl(String accountGuid) {
		return accountsUrl + "/" + accountGuid + "/reports/";
	}

	@SuppressWarnings("rawtypes")
	public static Result getAccountDetails(HttpHeaders headers, RestTemplate restTemplate,
			ResponseEntity whoamiResponseEntity, boolean validSession) {
		if (whoamiResponseEntity != null) {
			@SuppressWarnings("unchecked")
			HttpEntity entity = new HttpEntity(headers);
			Result processedResult = new Result();
			String accountGuid = null;
			JSONObject whoami = new JSONObject(whoamiResponseEntity.getBody().toString());
			String userGuid = whoami.getString(ReportsConstants.USERGUID);
			if (accountId == null) {
				accountId = HelperMethods.getEnvConfigValue("REPORTS_ACCOUNT_ID");
			}
			JSONArray accounts = whoami.getJSONArray(ReportsConstants.ACCOUNTS);
			for (int i = 0; i < accounts.length(); i++) {
				JSONObject account = accounts.getJSONObject(i);
				if (accountId.equals(account.get("account_id"))) {
					accountGuid = account.get("account_guid").toString();
					break;
				}
			}
			if (accountGuid == null) {
				String errorMessage = "There is no account with accountId :" + accountId + " for the given user.";
				alert.prepareError("error " + errorMessage).log();
				processedResult
						.addParam(new Param(ReportsConstants.VALIDATIONERROR, errorMessage, ReportsConstants.STRING));
				return processedResult;
			}
			String jasperLoginUrl = getUrl(accountGuid) + "login";
			if (!validSession || jSessionId == null || awselbSession == null) {
				Map<String, Object> queryParams = new HashMap<>();
				queryParams.put(ReportsConstants.USERGUID, userGuid);
				UriComponentsBuilder jasperLogin = buildParams(jasperLoginUrl, queryParams);
				ResponseEntity reportsResponseEntity = restTemplate.exchange(jasperLogin.build().toUri(),
						HttpMethod.GET, entity, String.class);
				JSONObject reportsConfig = new JSONObject(reportsResponseEntity.getBody().toString());
				awselbSession = "";
				jSessionId = reportsConfig.getString(ReportsConstants.JASPERSESSION);
				if (isCloud && reportsConfig.has(ReportsConstants.AWSELBSESSION)) {
					awselbSession = reportsConfig.getString(ReportsConstants.AWSELBSESSION);
				}

			}
			processedResult.addParam(new Param(ReportsConstants.JASPERSESSION, jSessionId, ReportsConstants.STRING));
			processedResult.addParam(new Param(ReportsConstants.AWSELBSESSION, awselbSession, ReportsConstants.STRING));
			processedResult.addParam(new Param(ReportsConstants.ACCOUNTSGUID, accountGuid, ReportsConstants.STRING));
			return processedResult;
		}
		return new Result();
	}

	public static ResponseEntity viewReport(String session, String awselbSession, String accountGuid,
			HttpHeaders headers, RestTemplate restTemplate, String reportId, Map<String, Object> queryParams,
			Map<String, Object> inputs, String extension, int pagenum, String queryParamVal) throws Exception {
		@SuppressWarnings("unchecked")
		HttpEntity entity = new HttpEntity(headers);
		JSONArray filtersArray = getFiltersForReport(session, awselbSession, accountGuid, headers, restTemplate,
				reportId, queryParams);
		String filters = "";
		if (filtersArray.length() != 0) {
			Map<String, Object> controls;
			controls = getStandardReportsControls(filtersArray);
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
		if (filters.equals("")) {
			alert.prepareError("no filters available").log();
		}
		queryParams.put("filters", filters + "startyear,startmonth,endyear,endmonth");
		queryParams.put("file_extension", extension);
		queryParams.put("type", extension);
		queryParams.put("page", pagenum);
		if (pagenum == 0)
			queryParams.put("export", true);
		UriComponentsBuilder builder = buildParams(getUrl(accountGuid) + queryParamVal, queryParams);
		return restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);
	}

	public static Map<String, Object> getStandardReportsControls(JSONArray inputControlsArray) throws JSONException {
		Map<String, Object> controls = new HashMap<>();

		for (int i = 0; i < inputControlsArray.length(); i++) {
			JSONObject inputControl = inputControlsArray.getJSONObject(i);
			try {
				JSONArray values = inputControl.getJSONObject("state").getJSONArray("options");
				byte index = 0;
				if (values.length() != 0) {
					if (inputControl.getString("id").equals("eid")) {
						index = 1;
					}
					controls.put(inputControl.getString("id"),
							values.getJSONObject(index).getString(ReportsConstants.VALUE));
				}
			} catch (Exception exception) {
				String value = inputControl.getJSONObject("state").getString(ReportsConstants.VALUE);
				controls.put(inputControl.getString("id"), value);
			}
		}
		return controls;
	}
}