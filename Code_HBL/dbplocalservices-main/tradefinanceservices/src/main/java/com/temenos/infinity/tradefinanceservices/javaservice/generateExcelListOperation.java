/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.javaservice;

import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.memorymanagement.MemoryManager;
import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.infinity.api.commons.exception.ApplicationException;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.utils.ExcelBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.utils.ExcelEnum;
import com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONObject;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.filterByTimeFrameForDashboard;

public class generateExcelListOperation implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws Exception {
		Log4j2Configurator.getInstance();

        HashMap<String, Object> inputParams = (HashMap<String, Object>) inputArray[1];

        String prefix = (String) inputParams.get("prefix");
        String sheetName = (String) inputParams.get("sheetName");
        methodID = methodID.toUpperCase();
        String[] headersList = ExcelEnum.valueOf(methodID).getHeadersList();
        String[] fieldsList = ExcelEnum.valueOf(methodID).getFieldsList();

        LinkedHashMap<Integer, ArrayList<Object>> headerValuesMap;
        try {
            headerValuesMap = getHeaderValuesMap(methodID, fieldsList, inputParams, request, ExcelEnum.valueOf(methodID).getBusinessDelegateImplName(), ExcelEnum.valueOf(methodID).isDashboard());
            return generateExcelList(prefix, sheetName, headersList, headerValuesMap, request);
        } catch (Exception e) {
            alert.prepareError("Error occurred while generating the trade finance file. Error: ", e).log();
        }
        return ErrorCodeEnum.ERRTF_29055.setErrorCode(new Result());
    }

    public Result generateExcelList(String prefix, String sheetName, String[] headerList, LinkedHashMap<Integer, ArrayList<Object>> headerValuesMap, DataControllerRequest request)
            throws IOException {

        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(sheetName);

        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Font font = sheet.getWorkbook().createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.BLUE_GREY.getIndex());
        font.setFontName("Segoe UI");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);

        Cell cell;
        Row row;
        int r = 0;
        Row rowHead = sheet.createRow(0);
        rowHead.setHeight((short) 500);
        if (headerList != null) {
            for (String header : headerList) {
                cell = rowHead.createCell(r);
                cell.setCellValue(header);
                cell.setCellStyle(style);
                r++;
            }
        }
        int i = 1;
        for (Integer key : headerValuesMap.keySet()) {
            ArrayList<Object> arrayList = headerValuesMap.get(key);
            row = sheet.createRow(i);
            int j = 0;
            for (Object obj : arrayList) {
                if (obj instanceof Integer)
                    row.createCell(j).setCellValue((int) obj);
                else
                    row.createCell(j).setCellValue(obj != null && StringUtils.isNotBlank(String.valueOf(obj)) ? String.valueOf(obj) : "N/A");
                j++;
            }
            i++;
        }
        for (int j = 0; j < headerList.length; j++) {
            int width = ((int) (headerList[j].length() * 1.14388)) * 300;
            sheet.setColumnWidth(j, width);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } finally {
            bos.close();
            workbook.close();
        }
        byte[] bytes = bos.toByteArray();
        Result result = new Result();
        String fileId = TradeFinanceCommonUtils.generateTradeFinanceFileID(prefix);
        MemoryManager.saveIntoCache(fileId + new CorporateManager(request).getSessionId(), Base64.encodeBase64String(bytes), 120);
        result.addParam("fileId", fileId);
        return result;
    }

    public <T> LinkedHashMap<Integer, ArrayList<Object>> getHeaderValuesMap(String methodId, String[] headerDTOList, HashMap<String, Object> inputParams,
                                                                            DataControllerRequest request, Class<? extends ExcelBusinessDelegate> businessDelegate, boolean isDashboard) throws IOException, ApplicationException, InstantiationException, IllegalAccessException {
        diagnostic.prepareDebug("Generating Header Values Map").log();
        LinkedHashMap<Integer, ArrayList<Object>> headerValuesMap = new LinkedHashMap<>();
        Integer i = 1;
        FilterDTO filterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
        List<T> filteredListDto;
        diagnostic.prepareDebug("Filtering the header values map").log();
        List<T> responseListDto = getSpecificDtoList(request, businessDelegate);
        if (responseListDto != null)
            responseListDto.removeAll(Collections.singletonList(null));
        if(!isDashboard)
            filteredListDto = filterDTO.filter(responseListDto);
        else {
            filteredListDto = responseListDto;
            if(StringUtils.isNotBlank(filterDTO.get_timeParam()) && StringUtils.isNotBlank(filterDTO.get_timeValue())) {
                filteredListDto = filterByTimeFrameForDashboard(filteredListDto, filterDTO.get_timeParam(), filterDTO.get_timeValue());
                filterDTO.set_timeParam("");
                filterDTO.set_timeValue("");
            }
            filteredListDto = filterDTO.filter(filteredListDto);
        }
        Map<String, Object> dtoMap;
        for (T responseDto : filteredListDto) {
            dtoMap = JSONUtils.parseAsMap(new JSONObject(responseDto).toString(), String.class, Object.class);
            ArrayList<Object> res = new ArrayList<>();
            for (String val : headerDTOList) {
                res.add(dtoMap.get(val));
            }
            headerValuesMap.put(i, res);
            i++;
        }
        alert.prepareError(methodId + " Filtered Header values map generated successfully for excel : " + headerValuesMap).log();
        return headerValuesMap;
    }

    private <T> List<T> getSpecificDtoList(DataControllerRequest request, Class<? extends ExcelBusinessDelegate> businessDelegate) throws InstantiationException, IllegalAccessException, ApplicationException {
        return businessDelegate.newInstance().getList(request);
    }
}
