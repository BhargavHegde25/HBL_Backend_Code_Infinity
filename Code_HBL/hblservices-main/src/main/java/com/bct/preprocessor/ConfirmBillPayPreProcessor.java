package com.bct.preprocessor;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.constants.HBLConstants;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.logger.LoggerUtil;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.session.Session;

public class ConfirmBillPayPreProcessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(ConfirmBillPayPreProcessor.class);
	String debtorName;
	String debtorAccount;
	String bankCode;
	String branchCode;
	String fromAccountCurrency;
	BigInteger id;
	String instructionId;
	String endToEndId;
	String refId;
	String appId;
	BigDecimal amount;
	HashMap cipsBatchDetailsObj= null;
	HashMap cipsTransactionDetailsObj= null;
	HashMap debitInfoObj= null;
	JSONArray errorArray = new JSONArray();
	boolean isValidFields = false;
	public static LoggerUtil logger = new LoggerUtil(ConfirmBillPayPreProcessor.class);

	public boolean execute1(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor: inputMap:" + inputMap);
		errorArray = new JSONArray();
		isValidFields = false;
		//try {
			String transactionDetailsString = inputMap.get("transactionDetails").toString();
			transactionDetailsString=StringEscapeUtils.unescapeJava(transactionDetailsString);
			JSONArray transactionDetailsArray= new JSONArray(transactionDetailsString);
			logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:transactionDetailsArray :" + transactionDetailsArray);
			JSONObject transObj = transactionDetailsArray.getJSONObject(0);
			logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:transObj :" + transObj);
			JSONObject cipsTransactionDetail =null;
			JSONObject cipsBatchDetail = null;
			JSONObject debitInformation = null;
			if (transObj.has("cipsTransactionDetail")) {
				cipsTransactionDetail = transObj.getJSONObject("cipsTransactionDetail");
				id = cipsTransactionDetail.has("id") ? cipsTransactionDetail.getBigInteger("id") : null;
				instructionId = cipsTransactionDetail.has("instructionId")
						? cipsTransactionDetail.getString("instructionId")
						: "";
				endToEndId = cipsTransactionDetail.has("endToEndId") ? cipsTransactionDetail.getString("endToEndId")
						: "";
				appId = cipsTransactionDetail.has("appId") ? cipsTransactionDetail.getString("appId") : "";
				refId = cipsTransactionDetail.has("refId") ? cipsTransactionDetail.getString("refId") : "";
				amount = cipsTransactionDetail.has("amount") ? cipsTransactionDetail.getBigDecimal("amount") : null;

			}
			if (transObj.has("cipsBatchDetail")) {
				cipsBatchDetail = transObj.getJSONObject("cipsBatchDetail");
				removeEmptyAndNullFields(cipsBatchDetail);
				if (transObj.has("debitInformation")) {
					 debitInformation = transObj.getJSONObject("debitInformation");
					 cipsBatchDetail.put("debtorAccount", debitInformation.get("debtorAccount"));
					 cipsBatchDetail.put("debtorBranch", debitInformation.get("debtorBranch"));
					 cipsBatchDetail.put("debtorAgent", debitInformation.get("debtorAgent"));
					 cipsBatchDetail.put("debtorName", debitInformation.get("debtorName"));
				}
				cipsBatchDetailsObj = convertJSONToHashMap(cipsBatchDetail);
				logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:cipsBatchDetailsObj :" + cipsBatchDetailsObj);
				inputMap.putAll(cipsBatchDetailsObj);
			}
			
			/*if (transObj.has("debitInformation")) {
				debitInformation = transObj.getJSONObject("debitInformation");
				debitInformation.put("batchAmount", amount.toString());
				removeEmptyAndNullFields(debitInformation);
				debitInfoObj = convertJSONToHashMap(debitInformation);
				logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:debitInfoObj :" + debitInfoObj);
				inputMap.putAll(debitInfoObj);
			}
			*/
			//validatePayload(inputMap);
			String token=generateTokenNew(inputMap, request);
			inputMap.clear();
			JSONArray array = new JSONArray();
			array.put(cipsTransactionDetail);
			inputMap.put("cipsTransactionDetail", array);
			inputMap.put("cipsBatchDetail", cipsBatchDetail);
			inputMap.put("token", token);
			isValidFields=true;
			/*if (isValidFields == true) {
				generateToken(inputMap, request);
			} else {
				logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:errorArray:" + errorArray);
				Dataset ds = constructDatasetFromJSONArray(errorArray);
				ds.setId("errorObj");
				result.addDataset(ds);
				isValidFields = false;
			}*/
			
		/*} catch (Exception e) {
			logger.error("Exception occured while generating HBLTxnToken:" + e.getLocalizedMessage());
			JSONObject errorObject = new JSONObject();
			errorObject.put("dbpErrMsg", e.getLocalizedMessage());
			errorObject.put("dbpErrCode", e.toString());
			errorArray.put(errorObject);
			Dataset ds = constructDatasetFromJSONArray(errorArray);
			ds.setId("errorObj");
			result.addDataset(ds);
			isValidFields = false;
		}
		*/
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:Final input params:" + inputMap);
		return isValidFields;
	}
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor: inputMap:" + inputMap);
		errorArray = new JSONArray();
		isValidFields = true;
		try {
			String transactionDetailsString = inputMap.get("transactionDetails").toString();
			transactionDetailsString=StringEscapeUtils.unescapeJava(transactionDetailsString);
			JSONArray transactionDetailsArray= new JSONArray(transactionDetailsString);
			logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:transactionDetailsArray :" + transactionDetailsArray);
			JSONObject transObj = transactionDetailsArray.getJSONObject(0);
			logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:transObj :" + transObj);
			if (transObj.has("cipsTransactionDetail")) {
				JSONObject cipsTransactionDetail = transObj.getJSONObject("cipsTransactionDetail");
				removeEmptyAndNullFields(cipsTransactionDetail);
				cipsTransactionDetailsObj = convertJSONToHashMap(cipsTransactionDetail);
				logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:cipsTransactionDetailsObj :"
						+ cipsTransactionDetailsObj);
				inputMap.clear();
				inputMap.putAll(cipsTransactionDetailsObj);
				id = cipsTransactionDetail.has("id") ? cipsTransactionDetail.getBigInteger("id") : null;
				instructionId = cipsTransactionDetail.has("instructionId")
						? cipsTransactionDetail.getString("instructionId")
						: "";
				endToEndId = cipsTransactionDetail.has("endToEndId") ? cipsTransactionDetail.getString("endToEndId")
						: "";
				appId = cipsTransactionDetail.has("appId") ? cipsTransactionDetail.getString("appId") : "";
				refId = cipsTransactionDetail.has("refId") ? cipsTransactionDetail.getString("refId") : "";
				amount = cipsTransactionDetail.has("amount") ? cipsTransactionDetail.getBigDecimal("amount") : null;

			}
			if (transObj.has("cipsBatchDetail")) {
				JSONObject cipsBatchDetail = transObj.getJSONObject("cipsBatchDetail");
				removeEmptyAndNullFields(cipsBatchDetail);
				cipsBatchDetailsObj = convertJSONToHashMap(cipsBatchDetail);
				logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:cipsBatchDetailsObj :" + cipsBatchDetailsObj);
				inputMap.putAll(cipsBatchDetailsObj);
			}
			if (transObj.has("debitInformation")) {
				JSONObject debitInformation = transObj.getJSONObject("debitInformation");
				debitInformation.put("batchAmount", amount.toString());
				removeEmptyAndNullFields(debitInformation);
				debitInfoObj = convertJSONToHashMap(debitInformation);
				logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:debitInfoObj :" + debitInfoObj);
				inputMap.putAll(debitInfoObj);
			}
			validatePayload(inputMap);
			if (isValidFields == true) {
				generateToken(inputMap, request);
			} else {
				logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:errorArray:" + errorArray);
				Dataset ds = constructDatasetFromJSONArray(errorArray);
				ds.setId("errorObj");
				result.addDataset(ds);
				isValidFields = false;
			}
		} catch (Exception e) {
			logger.error("Exception occured while generating HBLTxnToken:" + e.getLocalizedMessage());
			JSONObject errorObject = new JSONObject();
			errorObject.put("dbpErrMsg", e.getLocalizedMessage());
			errorObject.put("dbpErrCode", e.toString());
			errorArray.put(errorObject);
			Dataset ds = constructDatasetFromJSONArray(errorArray);
			ds.setId("errorObj");
			result.addDataset(ds);
			isValidFields = false;
		}
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:Final input params:" + inputMap);
		return isValidFields;
	}
	public void validatePayload(HashMap inputMap) {
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:inputMap :" + inputMap);
		String amount = inputMap.get("amount") != null ? inputMap.get("amount").toString() : "";
		String debtorAgent = inputMap.get("debtorAgent") != null ? inputMap.get("debtorAgent").toString() : "";
		String debtorBranch = inputMap.get("debtorBranch") != null ? inputMap.get("debtorBranch").toString() : "";
		String debtorName = inputMap.get("debtorName") != null ? inputMap.get("debtorName").toString() : "";
		String debtorAccount = inputMap.get("debtorAccount") != null ? inputMap.get("debtorAccount").toString() : "";

		if (StringUtils.isBlank(amount)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid amount:" + amount);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_AMOUNT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_AMOUNT);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorAgent)) {
			JSONObject errorObject = new JSONObject();
			logger.error(
					"HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtorAgent:" + debtorAgent);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_AGENT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_AGENT);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorBranch)) {
			JSONObject errorObject = new JSONObject();
			logger.error(
					"HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtorBranch:" + debtorBranch);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_BRANCH);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_BRANCH);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorName)) {
			JSONObject errorObject = new JSONObject();
			logger.error(
					"HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtor name:" + debtorName);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_NAME);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_NAME);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		if (StringUtils.isBlank(debtorAccount)) {
			JSONObject errorObject = new JSONObject();
			logger.error("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:invalid debtorBranch:"
					+ debtorAccount);
			errorObject.put("dbpErrMsg", HBLConstants.INVALID_DEBTOR_ACCOUNT);
			errorObject.put("dbpErrCode", HBLConstants.INVALID_DEBTOR_ACCOUNT);
			errorArray.put(errorObject);
			isValidFields = false;
		}
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:validatePayload:isValidFields:" + isValidFields);
	}
	public HashMap convertJSONToHashMap(JSONObject jsonObj) {
		Map<String, Object> map = jsonObj.toMap();
		HashMap hashMap = (map instanceof HashMap) ? (HashMap) map : new HashMap(map);
		return hashMap;
	}

	public void removeEmptyAndNullFields(Object object) throws Exception {
		if (object instanceof JSONArray) {
			JSONArray array = (JSONArray) object;
			for (int i = 0; i < array.length(); ++i)
				removeEmptyAndNullFields(array.get(i));
		} else if (object instanceof JSONObject) {
			JSONObject json = (JSONObject) object;
			JSONArray names = json.names();
			if (names == null)
				return;
			for (int i = 0; i < names.length(); ++i) {
				String key = names.getString(i);

				if (json.isNull(key) || json.get(key) == "") {
					json.remove(key);
				} else {
					removeEmptyAndNullFields(json.get(key));
				}
			}
		}
	}
	public String generateTokenNew(HashMap inputMap, DataControllerRequest request) throws Exception {
		String batchString = generateBatchString(inputMap);
		String txString = generateTransactionString(inputMap);
		String cipsApiUser = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");// "HBL@999";
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:generateToken:cipsApiUser :" + cipsApiUser);
		String tokenString = generateTokenString(batchString, txString, cipsApiUser);
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:token String:" + tokenString);
		String encodeContent = generateTxnToken(tokenString);
		//inputMap.put("token", encodeContent);
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:token:" + encodeContent);
		return encodeContent;
	}

	public HashMap generateToken(HashMap inputMap, DataControllerRequest request) throws Exception {
		String batchString = generateBatchString(inputMap);
		String txString = generateTransactionString(inputMap);
		String cipsApiUser = EnvironmentConfigurationsHandler.getServerProperty("CONNECTIPS_API_USER");// "HBL@999";
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:generateToken:cipsApiUser :" + cipsApiUser);
		String tokenString = generateTokenString(batchString, txString, cipsApiUser);
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:token String:" + tokenString);
		String encodeContent = generateTxnToken(tokenString);
		inputMap.put("token", encodeContent);
		logger.debug("HBL:ConnectIPSOtherbankTransferPreProcessor:token:" + encodeContent);
		return inputMap;
	}

	private String generateBatchString(HashMap inputMap) {
		return inputMap.get("batchId") + "," + inputMap.get("debtorAgent") + "," + inputMap.get("debtorBranch") + ","
				+ inputMap.get("debtorAccount") + "," + inputMap.get("amount") + ",NPR";
	}

	private String generateTransactionString(HashMap inputMap) {
		return id + "," + instructionId + "," + endToEndId + "," + appId + "," + refId + "," + amount;
	}

	public String generateTokenString(String batchString, String txString, String userId) {
		return batchString + "," + txString + "," + userId;
	}

	public String generateTxnToken(String contentToEncode) throws Exception {
		String cips_cert_path = EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PATH"); // "C:\\HBL-Certificate\\Certificate\\HBL.pfx";
		String cips_cert_password = EnvironmentConfigurationsHandler.getServerProperty("NPI_PFX_FILE_PASSWORD"); // "123";
		String certPath = cips_cert_path;
		byte[] certStore = Files.readAllBytes(Paths.get(certPath));

		KeyStore keyStore = KeyStore.getInstance("PKCS12");

		keyStore.load(new ByteArrayInputStream(certStore), cips_cert_password.toCharArray());

		String alias = keyStore.aliases().nextElement();
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, cips_cert_password.toCharArray());

		byte[] utf8Data = contentToEncode.getBytes("UTF-8");
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initSign(privateKey);
		signature.update(utf8Data);

		byte[] signedData = signature.sign();
		return Base64.getEncoder().encodeToString(signedData);
	}
	public Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}
		return response;
	}

	public Dataset constructDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}
}
