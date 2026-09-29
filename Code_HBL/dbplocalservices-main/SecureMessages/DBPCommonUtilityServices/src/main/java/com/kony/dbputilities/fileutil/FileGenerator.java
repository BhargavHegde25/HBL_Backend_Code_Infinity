package com.kony.dbputilities.fileutil;

import java.io.IOException;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;

public interface FileGenerator {

    byte[] generateFile(JsonArray data, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, Map<String, Object> otherData, String filters) throws IOException;

    String getContentType();

	byte[] generateLoanFile(JsonArray data, String title, String generatedBy, Map<String, String> fieldList,
			Map<String, Object> otherData, String filters, String installmentType, Map<String, String> summaryDetails, String paymentDateFormat) throws IOException;
	
	byte[] generateCombinedStatementsFile(JsonArray dataObject, String title, String generatedBy, String startDate,
            String endDate, Map<String, String> fieldList, String bankName, String currencyCode, String paymentDateFormat) throws Exception;
}