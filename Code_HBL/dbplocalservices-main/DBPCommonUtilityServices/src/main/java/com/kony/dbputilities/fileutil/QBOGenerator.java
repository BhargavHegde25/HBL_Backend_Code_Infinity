package com.kony.dbputilities.fileutil;

import java.io.IOException;
import java.util.Map;

import com.google.gson.JsonArray;

public class QBOGenerator implements FileGenerator {

    @Override
    public byte[] generateFile(JsonArray data, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, Map<String, Object> otherData, String filters,String downloadType,Map<String, String> inputParams) throws IOException {
        return FileGeneratorFactory.getFileGenerator("qfx").generateFile(data, title, generatedBy, startDate, endDate,
                fieldList, otherData, filters,downloadType,inputParams);
    }
    
    @Override
    public byte[] generateBlockedFile(JsonArray data, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, Map<String, Object> otherData, String filters) {
        return null;
    }

    @Override
    public String getContentType() {
        return "application/vnd.intu.qbo";
    }

	@Override
	public byte[] generateLoanFile(JsonArray data, String title, String generatedBy, Map<String, String> fieldList,
			Map<String, Object> otherData, String filters, String installmentType,Map<String, String> summaryDetails,String paymentDateFormat) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] generateCombinedStatementsFile(JsonArray dataObject, String title, String generatedBy,
			String startDate, String endDate, Map<String, String> fieldList, String bankName, String currencyCode,
			String paymentDateFormat,Map<String, Object> otherData) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}