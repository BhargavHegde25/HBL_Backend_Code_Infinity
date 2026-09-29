/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.javaservices;

import com.kony.dbputilities.util.Log4j2Configurator;
import com.kony.dbputilities.util.MWConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.*;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getBundleConfigAsJson;
import static com.temenos.infinity.tradesupplyfinance.utils.TradeSupplyFinanceCommonUtils.getCoreCustomerId;

/**
 * @author k.meiyazhagan
 */
public class DownloadSampleUploadXlsxOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private static final int TOTAL_ROWS = 50;

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
        Log4j2Configurator.getInstance();

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             Workbook workbook = new SXSSFWorkbook()) {
            // Bill types and currency data from DBP bundles
            JSONObject uploadConfig = getBundleConfigAsJson(request, PARAM_INVOICE_UPLOAD_KEY);
            // Customer IDs: Logged in customer-id first
            String customerIds[] = new String[]{getCoreCustomerId(request), "100100", "100110", "100112", "100114", "100115"};

            Sheet sheet = workbook.createSheet("Upload Bulk Invoices");
            // Header Row
            addHeaderRow(sheet);
            // Dropdown Columns
            addDropdownColumns(sheet, 1, uploadConfig.getJSONArray(PARAM_BILL_TYPE).toList().toArray(new String[0]));
            addDropdownColumns(sheet, 2, customerIds);
            addDropdownColumns(sheet, 3, customerIds);
            addDropdownColumns(sheet, 6, uploadConfig.getJSONArray(PARAM_CURRENCY).toList().toArray(new String[0]));

            // Closing workbook
            workbook.write(outputStream);
            workbook.close();

            // constructing response
            byte[] workbookBytes = outputStream.toByteArray();
            response.getHeaders().putAll(getAllResponseHeaders());
            response.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON, new BufferedHttpEntity(new ByteArrayEntity(workbookBytes)));
            response.setStatusCode(HttpStatus.SC_OK);
            return new Result();
        } catch (Exception e) {
            alert.prepareError("Error while downloading the sample file", e).log();
            return ErrorCodeEnum.ERR_30009.setErrorCode(new Result());
        }
    }

    private void addHeaderRow(Sheet sheet) {
        Row row = sheet.createRow(0);
        Cell cell;
        CellStyle headerStyle = getHeaderStyle(sheet.getWorkbook());
        for (int i = 0; i < BULK_XLSX_HEADER_DATA.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(BULK_XLSX_HEADER_DATA[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private static CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle headerCellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerCellStyle.setFont(font);
        return headerCellStyle;
    }

    private static void addDropdownColumns(Sheet sheet, int column, String[] data) {
        DataValidationHelper dvHelper = sheet.getDataValidationHelper();
        CellRangeAddressList addressList = new CellRangeAddressList(1, TOTAL_ROWS, column, column);
        DataValidationConstraint dvConstraint = dvHelper.createExplicitListConstraint(data);
        DataValidation validation = dvHelper.createValidation(dvConstraint, addressList);
        validation.setEmptyCellAllowed(true);
        validation.createErrorBox("Invalid Entry", "Please select a value from the dropdown list.");
        sheet.addValidationData(validation);
    }

    private static Map<String, String> getAllResponseHeaders() {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        customHeaders.put(HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, HTTP_HEADER_CONTENT_DISPOSITION);
        customHeaders.put(HTTP_HEADER_CONTENT_DISPOSITION, "attachment; filename=\"Sample Document.xlsx\"");
        return customHeaders;
    }
}