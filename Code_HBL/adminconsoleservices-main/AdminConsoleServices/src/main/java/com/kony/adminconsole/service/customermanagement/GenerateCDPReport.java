package com.kony.adminconsole.service.customermanagement;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutor;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kony.adminconsole.commons.utils.AuthenticationC360;
import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.constants.TemenosConstantsC360;
import com.kony.adminconsole.exception.ApplicationException;
import com.kony.adminconsole.service.customermanagement.dto.CDPReport;
import com.kony.adminconsole.service.customermanagement.dto.CDPReportDTO;
import com.kony.adminconsole.utilities.ACConstants;
import com.kony.adminconsole.utilities.ErrorCodeEnum;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;

public class GenerateCDPReport implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "SPOTLIGHT");
	
	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {

		Result result = new Result();

		Map<String, Object> requestParameters = new HashMap<String, Object>();

		JSONObject payload = CommonUtilities.getJSONFromRequest(request);
		
		String prospectId = (String) payload.get("prospectId");
		String applicationId = (String) payload.get("applicationId");
		String applicationType = (String) payload.get("applicationType");
		requestParameters.put("applicationId", applicationId);
		requestParameters.put("applicationType", applicationType);
		requestParameters.put("prospectId", prospectId);
		
		if (StringUtils.isAnyBlank(applicationId, applicationType, prospectId)) {
			ErrorCodeEnum.ERR_22085.setErrorCode(result);
			return result;
		}
		Map<String, Object> cdpReport = getDataForCDPReport(requestParameters);
		JSONObject cdpReportObj = new JSONObject(cdpReport);
		
		JSONArray entityItems = new JSONArray(cdpReportObj.get("entityItems").toString());
		JSONArray customerData = new JSONArray(cdpReportObj.get("customerData").toString());
		JSONArray customerCommunicationData = new JSONArray(cdpReportObj.get("customerCommunication").toString());
		JSONArray leadAppDetailsList = new JSONArray();
		String leadList = (String) cdpReportObj.get("leadDetails");
		leadAppDetailsList = new JSONArray(leadList);
		JSONArray odmsEntityList = payloadForODMSEntities(entityItems, prospectId);
		JSONArray leadReportList = payloadForLeadEntities(leadAppDetailsList, prospectId,odmsEntityList);
		JSONArray docEntityList = payloadForDocumentEntities(entityItems, prospectId,leadReportList);
		JSONArray customerDataList = payloadForCustomerDetails(customerData, prospectId,docEntityList);
		JSONArray customerReportList = payloadForCustomerCommunicationDetails(customerCommunicationData, prospectId,customerDataList);

		
		Map<String, String> documentInfo = generateCDPDocument(customerReportList, request, prospectId);

		if (documentInfo != null && documentInfo.get("documentName") != null) {
			result = JSONToResult.convert(JSONUtils.stringify(documentInfo));
			result.addOpstatusParam(0);
			result.addHttpStatusCodeParam(200);
			result.addStringParam("message", "Document Generated successfully");
		} else {
			result = new Result();
			result.addOpstatusParam(-1);
			result.addHttpStatusCodeParam(500);
			result.addStringParam("message", "Document Generation failed");
		}
		return result;
	}

	public Map<String, Object> getDataForCDPReport(Map<String, Object> requestParameters)
			throws DBPApplicationException, Exception {
		Map<String, Object> cdpReportDataResp = null;
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder()
					.withServiceId("CDPReportOrchService").withOperationId("getDataForCDPReport")
					.withRequestParameters(requestParameters).build();
			Result result = serviceExecutor.getResult();
			if (CommonUtilities.isBackendResponseSuccess(result, "httpStatusCode")) {
				ObjectMapper objectMapper = new ObjectMapper();
				cdpReportDataResp = new HashMap<String, Object>();
				cdpReportDataResp = objectMapper.readValue(ResultToJSON.convert(result), Map.class);
			}

		} catch (JSONException | DBPApplicationException de) {
			alert.prepareError("Error in GenerateCDPReport : getDataForCDPReport" + de.getMessage()).log();
			throw new ApplicationException(ErrorCodeEnum.ERR_22219);
		}
		return cdpReportDataResp;

	}

	//
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, String> generateCDPDocument(JSONArray odmsEntityList, DataControllerRequest request,
			String prospectId) {

		diagnostic.prepareTrace("In generateCDPDocument of GenerateCDPReport").log();
		Map<String, String> documentResult = new HashMap<String, String>();
		Map<String, Object> documentMap = new HashMap<String, Object>();
		try {
			SimpleDateFormat sysDate = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			documentMap.put("date", sysDate.format(date));

			CDPReportDTO cdpReportDTO = new CDPReportDTO();
			List<CDPReport> CDPReportList = new ArrayList<CDPReport>();
			for (int i = 0; i < odmsEntityList.length(); i++) {
				JSONObject reportObj = (JSONObject) odmsEntityList.opt(i);
				CDPReport cdpReport = new CDPReport();
				cdpReport.setStoragePoint(reportObj.optString("storagePoint"));
				cdpReport.setCompany(reportObj.optString("company"));
				cdpReport.setEntityName(reportObj.optString("entityName"));
				cdpReport.setFieldName(reportObj.optString("fieldName"));
				cdpReport.setFieldContent(reportObj.optString("fieldContent"));

				CDPReportList.add(cdpReport);
			}
			cdpReportDTO.setCDPReportDataDto(CDPReportList);
			String cdpTableXmlData = cdpReportDTO.marshallFacilitiesData(CDPReportList);

			documentMap.put("prospectId", prospectId);
			documentMap.put("requestType", "SAR");
			documentMap.put("time", DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now()));

			Map<String, Object> documentPayLoad = getDocumentPayLoadForCDPReport(documentMap, cdpTableXmlData);
			String authToken = AuthenticationC360.getDMSAuthToken(request, "");
			documentPayLoad.put("Authorization", authToken);
			
			Map<String, Object> backendResponse = generateDocumentOnline(documentPayLoad);

			if (backendResponse.get("documentName").toString() != null) {
				documentResult.put("documentName", documentMap.get("prospectId").toString() + "-CDPReport.pdf");
				documentResult.put("content", backendResponse.get("content").toString());
			}

		} catch (Exception e) {
			alert.prepareError("Exception occured while generating CDP Document:" + e.getMessage()).log();
		}
		return documentResult;
	}

	//
	private Map<String, Object> getDocumentPayLoadForCDPReport(Map<String, Object> documentInput,
			String cdpTableXmlData) {

		diagnostic.prepareTrace("In getDocumentPayLoadForCDPReport of GenerateCDPReport").log();
		Map<String, Object> documentInfo = null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		InputStream inputStream = null;
		DocumentBuilder documentBuilder;

		try {
			documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
			documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
			documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			inputStream = GenerateCDPReport.class.getClassLoader().getResourceAsStream("CDPReport.xml");

			Document xmlDoc = documentBuilder.parse(inputStream);

			Map<String, String> mappingValues = new HashMap<String, String>();
			mappingValues.put("PROSPECT_ID", (String) documentInput.get("prospectId"));
			mappingValues.put("DATE", (String) documentInput.get("date"));
			mappingValues.put("REQUEST_TYPE", (String) documentInput.get("requestType"));
			mappingValues.put("TIME", (String) documentInput.get("time"));
			mappingValues.put("GDPR_TABLE", cdpTableXmlData);
			String encodedXml = getEncodedDataForCDPReport(xmlDoc, mappingValues);

			JSONObject generationDetails = new JSONObject();
			generationDetails.put("templateName", ACConstants.SPOTLIGHT_PROSPECT_GDPR);
			generationDetails.put("data", encodedXml);

			JSONObject payload = new JSONObject();
			payload.put("generationDetails", generationDetails);

			documentInfo = new HashMap<String, Object>();
			documentInfo.put("payload", payload);

		} catch (Exception e) {
			alert.prepareError("Exception Occured while getting DocPayload" + e.getMessage()).log();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					alert.prepareError("Error in GenerateCDPReport : getDocumentPayLoadForCDPReport"
							+ e.getMessage()).log();
				}
			}
		}
		return documentInfo;
	}

	//
	private String getEncodedDataForCDPReport(Document xmlDoc, Map<String, String> mappingValues) {
		String encodedXml = "";
		DOMSource domSource = new DOMSource(xmlDoc);
		Transformer transformer;
		try {
			TransformerFactory transFact = TransformerFactory.newInstance();
			transFact.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			transFact.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
			transformer = transFact.newTransformer(domSource);
			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);

			transformer.transform(domSource, sr);
			String xmlString = sw.toString();

			StrSubstitutor sub = new StrSubstitutor(mappingValues, "{", "}");
			String formattedXMLString = sub.replace(xmlString);
			encodedXml = Base64.getEncoder().encodeToString(formattedXMLString.getBytes());
		} catch (TransformerException e) {
			alert.prepareError("Error in GenerateCDPReport : getEncodedDataForCDPReport" + e.getMessage()).log();
		}
		return encodedXml;
	}

	//
	private JSONArray payloadForODMSEntities(JSONArray entityItems, String digitalProfileId) {
		JSONArray odmsEntityList = new JSONArray();
		JSONObject json = new JSONObject();
		JSONObject applicantMetaData = new JSONObject();
		String applicantEntityName = "ApplicantMetaData_" + digitalProfileId;
		applicantMetaData = CommonUtilities.getEntityItemEntry("ApplicantMetaData", entityItems, applicantEntityName);
		String branchReference = "";
		if ((applicantMetaData != null) && applicantMetaData.has("entry")) {
			JSONObject coApplicantMetaDataEntry = new JSONObject(applicantMetaData.optString("entry"));
			if (coApplicantMetaDataEntry != null) {
				branchReference = coApplicantMetaDataEntry.optString("BranchReference");
			}
		}
		for (int i = 0; i < entityItems.length(); i++) {
			JSONObject entityItem = (JSONObject) entityItems.opt(i);
			String entityItemDef = entityItem.optString("entityItemDefinitionName");
			if ("PersonalInfo".equals(entityItemDef)) {
				if (entityItem.has("entry")) {
					String entry = entityItem.optString("entry");
					JSONObject entryObj = new JSONObject(entry);
					Iterator<?> keys = entryObj.keys();
					while (keys.hasNext()) {
						json = new JSONObject();
						String key = (String) keys.next();
						String fieldContent = entryObj.optString(key);
						if (StringUtils.isNotBlank(fieldContent)) {
							json.put("storagePoint", "ODMS");
							json.put("company", branchReference);
							json.put("entityName", entityItemDef);
							json.put("fieldName", key);
							json.put("fieldContent", fieldContent);
							odmsEntityList.put(json);
						}
					}
				}
			}
			if ("AddressInfo".equals(entityItemDef)) {
				if (entityItem.has("entry")) {
					String entry = entityItem.optString("entry");
					JSONObject entryObj = new JSONObject(entry);
					Iterator<?> keys = entryObj.keys();
					while (keys.hasNext()) {
						json = new JSONObject();
						String key = (String) keys.next();
						String fieldContent = entryObj.optString(key);
						if (StringUtils.isNotBlank(fieldContent)) {
							json.put("storagePoint", "ODMS");
							json.put("company", branchReference);
							json.put("entityName", entityItemDef);
							json.put("fieldName", key);
							json.put("fieldContent", fieldContent);
							odmsEntityList.put(json);
						}
					}
				}
			}
			if ("IdentityInfo".equals(entityItemDef)) {
				if (entityItem.has("entry")) {
					String entry = entityItem.optString("entry");
					JSONObject entryObj = new JSONObject(entry);
					Iterator<?> keys = entryObj.keys();
					while (keys.hasNext()) {
						json = new JSONObject();
						String key = (String) keys.next();
						String fieldContent = entryObj.optString(key);
						if (StringUtils.isNotBlank(fieldContent)) {
							json.put("storagePoint", "ODMS");
							json.put("company", branchReference);
							json.put("entityName", entityItemDef);
							json.put("fieldName", key);
							json.put("fieldContent", fieldContent);
							odmsEntityList.put(json);
						}
					}
				}
			}
			if ("ProductSelection".equals(entityItemDef)) {
				if (entityItem.has("entry")) {
					String entry = entityItem.optString("entry");
					JSONObject entryObj = new JSONObject(entry);
					JSONArray products = entryObj.optJSONArray("Products");
					for(int lp=0; lp< products.length(); lp++) {
						json = new JSONObject();
						JSONObject productData = products.getJSONObject(lp);
					    String productName = productData.optString("ProductName");
						if (StringUtils.isNotEmpty(productName)) {
							json.put("storagePoint", "ODMS");
							json.put("company", branchReference);
							json.put("entityName", entityItemDef);
							json.put("fieldName", "ProductName");
							json.put("fieldContent", productName);
							odmsEntityList.put(json);
						}
					}
				}
			}
			if ("IncomeEmployment".equals(entityItemDef)) {
				boolean arrayFlag;
				if (entityItem.has("entry")) {
					String entry = entityItem.optString("entry");
					JSONObject entryObj = new JSONObject(entry);
					Iterator<?> keys = entryObj.keys();
					while (keys.hasNext()) {
						json = new JSONObject();
						String key = (String) keys.next();
						String fieldContent = entryObj.optString(key);
						arrayFlag = CommonUtilities.isValidJsonArray(fieldContent);
						if (arrayFlag == false) {
							if (StringUtils.isNotBlank(fieldContent)) {
								json.put("storagePoint", "ODMS");
								json.put("company", branchReference);
								json.put("entityName", entityItemDef);
								json.put("fieldName", key);
								json.put("fieldContent", fieldContent);
								odmsEntityList.put(json);
							}
						} else if (arrayFlag) {
							String entityDetails = entryObj.optString(key);
							JSONArray entityData = new JSONArray(entityDetails.toString());
							for (int loop = 0; loop < entityData.length(); loop++) {
								JSONObject employmentData = entityData.getJSONObject(loop);
								Iterator<?> employmentDataKeys = employmentData.keys();
								while (employmentDataKeys.hasNext()) {
									json = new JSONObject();
									String employmentDataKeysKey = (String) employmentDataKeys.next();
									String fieldData = employmentData.optString(employmentDataKeysKey);
									if (StringUtils.isNotBlank(fieldData)) {
										json.put("storagePoint", "ODMS");
										json.put("company", branchReference);
										json.put("entityName", entityItemDef);
										json.put("fieldName", employmentDataKeysKey);
										json.put("fieldContent", fieldData);
										odmsEntityList.put(json);
									}
								}
							}
						}
					}
				}
			}
		}
		
		return odmsEntityList;
	}
	//
	//
	private JSONArray payloadForLeadEntities(JSONArray entityItemsList, String digitalProfileId,JSONArray reportEntityList) {
		JSONObject json = new JSONObject();
		for (int lp = 0; lp < entityItemsList.length(); lp++) {
			JSONArray arr = entityItemsList.optJSONArray(lp);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject entityItem = (JSONObject) arr.opt(i);
				String entityItemDef = entityItem.optString("entityItemDefinitionName");
				if ("Address".equals(entityItemDef)) {
					if (entityItem.has("entry")) {
						String entry = entityItem.optString("entry");
						JSONObject entryObj = new JSONObject(entry);
						Iterator<?> keys = entryObj.keys();
						while (keys.hasNext()) {
							json = new JSONObject();
							String key = (String) keys.next();
							String fieldContent = entryObj.optString(key);
							if (StringUtils.isNotBlank(fieldContent)) {
								json.put("storagePoint", "ODMS");
								json.put("company", "");
								json.put("entityName", entityItemDef);
								json.put("fieldName", key);
								json.put("fieldContent", fieldContent);
								reportEntityList.put(json);
							}
						}
					}
				}
				if ("Lead".equals(entityItemDef)) {
					if (entityItem.has("entry")) {
						String entry = entityItem.optString("entry");
						JSONObject entryObj = new JSONObject(entry);
						Iterator<?> keys = entryObj.keys();
						while (keys.hasNext()) {
							json = new JSONObject();
							String key = (String) keys.next();
							String fieldContent = entryObj.optString(key);
							if (StringUtils.isNotBlank(fieldContent)) {
								json.put("storagePoint", "ODMS");
								json.put("company", "");
								json.put("entityName", entityItemDef);
								json.put("fieldName", key);
								json.put("fieldContent", fieldContent);
								reportEntityList.put(json);
							}
						}
					}
				}
				if ("AlternateIdentity".equals(entityItemDef)) {
					if (entityItem.has("entry")) {
						String entry = entityItem.optString("entry");
						JSONObject entryObj = new JSONObject(entry);
						Iterator<?> keys = entryObj.keys();
						while (keys.hasNext()) {
							json = new JSONObject();
							String key = (String) keys.next();
							String fieldContent = entryObj.optString(key);
							if (StringUtils.isNotBlank(fieldContent)) {
								json.put("storagePoint", "ODMS");
								json.put("company", "");
								json.put("entityName", entityItemDef);
								json.put("fieldName", key);
								json.put("fieldContent", fieldContent);
								reportEntityList.put(json);
							}
						}
					}
				}
				if ("Role".equals(entityItemDef)) {
					if (entityItem.has("entry")) {
						String entry = entityItem.optString("entry");
						JSONObject entryObj = new JSONObject(entry);
						Iterator<?> keys = entryObj.keys();
						while (keys.hasNext()) {
							json = new JSONObject();
							String key = (String) keys.next();
							String fieldContent = entryObj.optString(key);
							if (StringUtils.isNotBlank(fieldContent)) {
								json.put("storagePoint", "ODMS");
								json.put("company", "");
								json.put("entityName", entityItemDef);
								json.put("fieldName", key);
								json.put("fieldContent", fieldContent);
								reportEntityList.put(json);
							}
						}
					}
				}
			}
		}
		
		return reportEntityList;
	}
	//
	private JSONArray payloadForDocumentEntities(JSONArray entityItems, String digitalProfileId, JSONArray reportEntityList) {
		JSONObject json = new JSONObject();
		for (int i = 0; i < entityItems.length(); i++) {
			JSONObject entityItem = (JSONObject) entityItems.opt(i);
			String type = entityItem.optString("type");
			
			if("DOCUMENT".equalsIgnoreCase(type)) {
				if (entityItem.has("entry")) {
					String entry = entityItem.optString("entry");
					String[] documentList = {"documentId","systemId","documentName","documentType"}; 
					JSONObject entryObj = new JSONObject(entry);
					Iterator<?> keys = entryObj.keys();
					while (keys.hasNext()) {
						json = new JSONObject();
						String key = (String) keys.next();
						String fieldContent = entryObj.optString(key);
						for (int index = 0; index < documentList.length; index++) {
							if (key.equalsIgnoreCase(documentList[index])) {
								if (StringUtils.isNotBlank(fieldContent)) {
									json.put("storagePoint", "DOC MS");
									json.put("company", "");
									json.put("entityName", "Document");
									json.put("fieldName", key);
									json.put("fieldContent", fieldContent);
									reportEntityList.put(json);
								}
							}
						}
					}
				}
			}
		}
		return reportEntityList;
	}
	//
	//
	private JSONArray payloadForCustomerDetails(JSONArray customerData, String digitalProfileId,
			JSONArray reportEntityList) {
		JSONObject json = new JSONObject();
		for (int i = 0; i < customerData.length(); i++) {
			JSONObject customerDetails = (JSONObject) customerData.opt(i);
			String[] customerList = { "FirstName", "LastName", "DateOfBirth", "taxid", "IDState", "IDCountry",
					"CountryCode", "IDIssueDate", "Ssn", "IDExpiryDate", "IDType_id", "IDValue" };
			Iterator<?> keys = customerDetails.keys();
			while (keys.hasNext()) {
				json = new JSONObject();
				String key = (String) keys.next();
				String fieldContent = customerDetails.optString(key);
				for (int index = 0; index < customerList.length; index++) {
					if (key.equalsIgnoreCase(customerList[index])) {
						if (StringUtils.isNotBlank(fieldContent)) {
							json.put("storagePoint", "DBXDB");
							json.put("company", "");
							json.put("entityName", "Customer");
							json.put("fieldName", key);
							json.put("fieldContent", fieldContent);
							reportEntityList.put(json);
						}
					}
				}
			}
		}
		return reportEntityList;
	}
	//
	//
	private JSONArray payloadForCustomerCommunicationDetails(JSONArray customerData, String digitalProfileId,
			JSONArray reportEntityList) {
		JSONObject json = new JSONObject();
		for (int i = 0; i < customerData.length(); i++) {
			JSONObject customerDetails = (JSONObject) customerData.opt(i);
			String[] custCommunicationList = { "Type_id", "Value" };
			Iterator<?> keys = customerDetails.keys();
			while (keys.hasNext()) {
				json = new JSONObject();
				String key = (String) keys.next();
				String fieldContent = customerDetails.optString(key);
				for (int index = 0; index < custCommunicationList.length; index++) {
					if (key.equalsIgnoreCase(custCommunicationList[index])) {
						if (StringUtils.isNotBlank(fieldContent)) {
							json.put("storagePoint", "DBXDB");
							json.put("company", "");
							json.put("entityName", "CustomerCommunication");
							json.put("fieldName", key);
							json.put("fieldContent", fieldContent);
							reportEntityList.put(json);
						}
					}
				}
			}
		}
		return reportEntityList;
	}

	//
	public Map<String, Object> generateDocumentOnline(Map<String, Object> receiptMap) {
		Map<String, Object> receiptRespMap = null;
		try {
			DBPServiceExecutor serviceExecutor = DBPServiceExecutorBuilder.builder().withServiceId("SpotlightDocAPIs")
					.withFabricAuthToken((String) receiptMap.get("xKonyAuthorization"))
					.withOperationId("generateDocumentOnline").withRequestParameters(receiptMap).build();
			Result result = serviceExecutor.getResult();
			if (CommonUtilities.isBackendResponseSuccess(result, "status")) {
				receiptRespMap = new HashMap<String, Object>();
				receiptRespMap.put("documentName", result.getParamValueByName("documentName"));
				receiptRespMap.put("content", result.getParamValueByName("content"));
			}

		} catch (DBPApplicationException de) {
			alert.prepareError("Error in GenerateCDPReport : generateDocumentOnline" + de.getMessage()).log();
		}

		return receiptRespMap;
	}
}
