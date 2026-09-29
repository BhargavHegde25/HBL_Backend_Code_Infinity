/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradesupplyfinance.javaservices;

import com.kony.dbputilities.util.Log4j2Configurator;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradesupplyfinance.constants.ErrorCodeEnum;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.dbp.core.constants.DBPConstants.DBP_ERROR_MESSAGE_KEY;
import static com.temenos.infinity.tradesupplyfinance.constants.TradeSupplyFinanceConstants.DISPLAY_DATE_FORMAT_INVOICE;

/**
 * @author k.meiyazhagan
 */
public class ParseBulkUploadedXlsxOperation implements JavaService2 {
    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
        Log4j2Configurator.getInstance();

        try {
            String base64Xlsx = request.getParameter("base64Input");
            if (StringUtils.isBlank(base64Xlsx)) {
                alert.prepareError("Mandatory fields are missing").log();
                return ErrorCodeEnum.ERR_30004.setErrorCode(new Result());
            }

            // Bytes to XSSFWorkbook
            byte[] decodedBytes = Base64.decodeBase64(base64Xlsx);
            try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(decodedBytes))) {
                JSONObject jsonObject = new JSONObject().put("BulkInvoices", convertExcelToJson(workbook));
                return JSONToResult.convert(String.valueOf(jsonObject));
            }
        } catch (Exception e) {
            alert.prepareError("Error while parsing the sample file", e).log();
            Result result = ErrorCodeEnum.ERR_30016.setErrorCode(new Result());
            result.addParam(DBP_ERROR_MESSAGE_KEY, e.toString());
            return result;
        }
    }

    private static JSONArray convertExcelToJson(Workbook workbook) {
        JSONArray allRowData = new JSONArray();

        // First Sheet
        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(0);
        int lastColumn = headerRow.getLastCellNum();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row currentRow = sheet.getRow(rowIndex);
            JSONObject rowData = new JSONObject();
            for (int colIndex = 0; colIndex < lastColumn; colIndex++) {
                XSSFCell currentCell = (XSSFCell) currentRow.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String columnName = getColumnName(headerRow.getCell(colIndex));
                rowData.put(columnName, getColumnValue(currentCell, columnName));
            }
            allRowData.put(rowData);
        }

        return allRowData;
    }

    private static String getColumnValue(XSSFCell currentCell, String columnName) {
        switch (columnName) {
            case "supplierId":
            case "buyerId":
            case "invoiceAmount":
                return currentCell.getRawValue();
            case "billReference":
                return currentCell.getCellType() == CellType.NUMERIC ? currentCell.getRawValue() : currentCell.getRichStringCellValue().getString();
            case "billType":
            case "invoiceCurrency":
                return currentCell.getStringCellValue();
            case "issueDate":
            case "maturityDate":
                Date date = currentCell.getDateCellValue();
                return new SimpleDateFormat(DISPLAY_DATE_FORMAT_INVOICE).format(date);
            default:
                return null;
        }
    }

    private static String getColumnName(Cell cell) {
        switch (cell.getStringCellValue()) {
            case "Bill Reference":
                return "billReference";
            case "Bill Type":
                return "billType";
            case "Supplier ID":
                return "supplierId";
            case "Buyer ID":
                return "buyerId";
            case "Issue Date":
                return "issueDate";
            case "Maturity Date":
                return "maturityDate";
            case "Currency":
                return "invoiceCurrency";
            case "Amount":
                return "invoiceAmount";
            default:
                return cell.getStringCellValue();
        }
    }
}