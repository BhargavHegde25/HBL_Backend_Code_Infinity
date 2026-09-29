package com.temenos.infinity.product.bulkpaymentservices.javaservices;

import java.io.StringReader;
/*import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;*/
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.Log4j2Configurator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;*/

import com.kony.dbputilities.util.TokenUtils;
import com.temenos.infinity.api.commons.config.EnvironmentConfigurationsHandler;
import com.temenos.infinity.product.bulkpaymentservices.dto.BulkPaymentFileDTO;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.apache.poi.util.StringUtil;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import org.w3c.dom.Document;
/*import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.kony.dbx.util.EnvironmentConfigurationsHandler;*/
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.tocf.tcc.TCCFactory;
import com.temenos.tocf.tcc.TCClientException;
import com.temenos.tocf.tcc.TCConnection;
import com.temenos.tocf.tcc.TCOutputStream;

import static com.kony.scaintegration.helper.Helper.getCurrentTimeStamp;

public class StoreBulkPaymentFileOperation implements JavaService2{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	//private static final String CHANNEL_XML  = System.getProperty("middleware.home") +"//middleware//middleware-bootconfig//customlib//default//conf//channels.xml";
	private static final String CHANNEL_NAME = "FileUploadDownloadChannel";
	//private static final String PORT_XPATH = "/CHANNELS/CHANNEL/ADAPTER/PORT";
	//private static final String HOST_XPATH = "/CHANNELS/CHANNEL/ADAPTER/SUPPLIER/INITIATOR/HOSTNAME";
	//private static final String T24_STORE_HOST_AND_PORT = "T24_STORE_API_HOST_URL";
	private static final String UPLOAD_FOLDER_CSV = "BULK.PAYMENTS.CSV";
	private static final String UPLOAD_ID_CSV = "BULK.PAYMENT.CSV";
	private static final String UPLOAD_FOLDER_XML = "BULK.PAYMENTS.XML";
	private static final String UPLOAD_ID_XML = "BULK.PAYMENT.PAIN001";
	private static final String UPLOAD_ID_XML_V9 = "BULK.PAYMENT.PAIN001V9";
	private static final String UPLOAD_ID_XML_DESCRIPTION = "pain.001.001.03";
	private static final String ServiceName = "T24BulkFileUpload";
	private static final String OperationName = "uploadFile";
	private static final String BULK_PAYMENT_PRE_AUTH_USER = "BULK_PAYMENT_PRE_AUTH_USER";
	private static final String FILE_TYPE_CSV = "text/csv";
	private static final String FILE_TYPE_XML = "text/xml";

	@Override
	public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
						 DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();
		Result result = new Result();
		try {
			result =  new Result();
			Map<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];
			BulkPaymentFileDTO bulkFile = new BulkPaymentFileDTO();
			String defaultUserName = EnvironmentConfigurationsHandler.getServerAppProperty(BULK_PAYMENT_PRE_AUTH_USER);
			bulkFile.setFileName(inputParams.get("fileName") != null ? inputParams.get("fileName").toString() : null);
			bulkFile.setContent(inputParams.get("content") != null ? inputParams.get("content").toString() : null);
			bulkFile.setDescription(inputParams.get("description") != null ? inputParams.get("description").toString() : null);
			String fileBaseName = FilenameUtils.getBaseName(bulkFile.getFileName()) + getCurrentTimeStamp();
			String fileExtension = FilenameUtils.getExtension(bulkFile.getFileName());
			bulkFile.setSysGeneratedFileName(fileBaseName + "." + fileExtension);
			bulkFile.setUploadedBy(StringUtils.isNotBlank(defaultUserName) ? defaultUserName : "AUTHORISER");
			byte[] bulkPaymentFileContent = Base64.getDecoder().decode(new String(bulkFile.getContent()).getBytes("UTF-8"));
			bulkFile.setFileSize(String.valueOf(bulkPaymentFileContent.length));
			if("csv".equalsIgnoreCase(fileExtension)) {
				bulkFile.setUploadTypeId(UPLOAD_ID_CSV);
				bulkFile.setFileType(FILE_TYPE_CSV);
			}
			else if("xml".equalsIgnoreCase(fileExtension)) {
				String utf = new String(bulkPaymentFileContent, "UTF-8");
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				factory.setValidating(true);
				factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
				factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
				DocumentBuilder builder = factory.newDocumentBuilder();
				String DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
				factory.setFeature(DOCTYPE_DECL, true);
				InputSource is = new InputSource(new StringReader(utf));
				Document doc = builder.parse(is);
				if (doc != null && doc.getFirstChild() != null && doc.getFirstChild().getNamespaceURI() != null
						&& doc.getFirstChild().getNamespaceURI().contains(UPLOAD_ID_XML_DESCRIPTION)) {
					bulkFile.setUploadTypeId(UPLOAD_ID_XML);
				} else {
					bulkFile.setUploadTypeId(UPLOAD_ID_XML_V9);
				}
				bulkFile.setFileType(FILE_TYPE_XML);
			}

			result = uploadFile(request,bulkFile);
		}
		catch(Exception e) {
			alert.prepareError("Error occured while invoking uploadBulkPaymentFile: ", e).log();
			return ErrorCodeEnum.ERR_21225.setErrorCode(new Result());
		}

		return result;
	}

	private Result uploadFile(DataControllerRequest request, BulkPaymentFileDTO bulkFile) throws DBPApplicationException {
		Result result = new Result();
		Map<String, Object> requestParameters = null;
		String errorMessage = "";
		//Token For TB_SERVER War
		_addBulkPaymentHeaders(request,bulkFile);
		//Request Params for Upload File Service
		try {
			bulkFile.setContent("data:"+bulkFile.getFileType()+";base64,"+bulkFile.getContent());
			requestParameters = JSONUtils.parseAsMap(new JSONObject(bulkFile).toString(), String.class,
					Object.class);
		} catch (Exception e) {
			alert.prepareError("Error occured while fetching the input params", e).log();
			return null;
		}

		String uploadResponse = DBPServiceExecutorBuilder.
				builder().
				withServiceId(ServiceName).
				withObjectId(null).
				withOperationId(OperationName).
				withRequestParameters(requestParameters).
				withRequestHeaders(request.getHeaderMap()).
				withDataControllerRequest(request).
				build().getResponse();
		JSONObject uploadResponseJSON = new JSONObject(uploadResponse),responseBody = null;
		//Success Response in status as "success"
		if(uploadResponseJSON.has("body")) {
			responseBody = uploadResponseJSON.getJSONObject("body");
			if (responseBody.has("status") && responseBody.getString("status").equalsIgnoreCase("success")) {
				result.addParam(new Param("fileName", responseBody.getString("fileId")));
				result.addParam(new Param("uploadSize", bulkFile.getFileSize() + ""));
				result.addParam(new Param("uploadTypeId", bulkFile.getUploadTypeId()));
				result.addParam(new Param("displayName", bulkFile.getDescription()));
				result.addParam(new Param("sysGeneratedFileName", responseBody.getString("fileId").split("\\|\\|")[0]));
				return result;
			}
		}
		if (uploadResponseJSON.has("error")) {
			//Error Response - From TB-Server war
			responseBody = uploadResponseJSON.getJSONObject("error");
			if (responseBody.has("errorMessage")) {
				errorMessage = responseBody.getString("errorMessage");

			}
		}
		//Error Response - Fabric Error
		if (uploadResponseJSON.has("errmsg")) {
			errorMessage = responseBody.getString("errmsg");
		}
		alert.prepareError("Error occured while invoking uploadBulkPaymentFile to TB_SERVER : "+errorMessage).log();
		return ErrorCodeEnum.ERR_21225.setErrorCode(new Result(),errorMessage);
	}
	private boolean _addBulkPaymentHeaders(DataControllerRequest request, BulkPaymentFileDTO bulkFile){
		request.addRequestParam_("issuer","TB_SERVER");
		String Authorization = TokenUtils.getT24AuthToken(request);
		request.addRequestParam_("Authorization","Bearer "+Authorization);
		request.getHeaderMap().put("Authorization","Bearer "+Authorization);
		request.addRequestParam_("DefaultUserName",bulkFile.getUploadedBy());
		request.getHeaderMap().put("DefaultUserName",bulkFile.getUploadedBy());
		return true;
	}

    private boolean openConnectionAndUploadFile(byte[] bytes, String fileName, String uploadDir) throws TCClientException {
    	
    	TCConnection connection = null; 
    	TCOutputStream connOutStream = null;
    	boolean result = true;
    	
    	try
    	{
    		TCCFactory tcf = TCCFactory.getInstance();
    		
    		connection = tcf.createTCConnection(CHANNEL_NAME);
    		connOutStream = connection.getOutputStream();
    		
    		if (StringUtils.isNotBlank(fileName))
    			connOutStream.setFileName(fileName);
    		
    		if (StringUtils.isNotBlank(uploadDir))
    			connOutStream.setFilePath(uploadDir);

    		connOutStream.send(bytes);
    	}
    	catch (Exception e) {
    		alert.prepareError("Error occured while creating the connection with remote and uploading the file",e).log();
    		result = false;
    	} 
    	finally {
    		if(connection != null)
    			connection.close();
    	}

    	return result;
    }
    
    private String getCurrentTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyhhmmsss");
        Calendar calendar = Calendar.getInstance();

        return dateFormat.format(calendar.getTime());
    }

    
}