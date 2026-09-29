package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.DashboardBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.temenos.infinity.tradefinanceservices.dto.DashboardDTO;
import com.temenos.infinity.tradefinanceservices.resource.api.DashboardResource;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.temenos.infinity.tradefinanceservices.utils.TradeFinanceCommonUtils.filterByTimeFrameForDashboard;

/**
 * @author naveen.yerra
 */
public class DashboardResourceImpl implements DashboardResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    DashboardBusinessDelegate dashboardBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(DashboardBusinessDelegate.class);

    public Result fetchReceivables(Map<String, Object> inputParams, DataControllerRequest request) {
        List<DashboardDTO> list = dashboardBusinessDelegate.fetchReceivables(inputParams, request);
        if(list == null)
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result(), "Error occurred while fetching receivables");
        try {
            FilterDTO filterDto = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
            if(StringUtils.isNotBlank(filterDto.get_timeParam()) && StringUtils.isNotBlank(filterDto.get_timeValue())) {
                list = filterByTimeFrameForDashboard(list, filterDto.get_timeParam(), filterDto.get_timeValue());
                filterDto.set_timeParam("");
                filterDto.set_timeValue("");
            }
            list = filterDto.filter(list);
        } catch (IOException e) {
            alert.prepareError("Error while parsing the filter").log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result(), "Error occurred while fetching payables");
        }
        return JSONToResult.convert(
                new JSONObject().put("Receivables",list).toString());
    }

    @Override
    public Result fetchPayables(Map<String, Object> inputParams, DataControllerRequest request) {
        List<DashboardDTO> list = dashboardBusinessDelegate.fetchPayables(inputParams, request);
        if(list == null)
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result(), "Error occurred while fetching payables");

        try {
            FilterDTO filterDto = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
            if(StringUtils.isNotBlank(filterDto.get_timeParam()) && StringUtils.isNotBlank(filterDto.get_timeValue())) {
                list = filterByTimeFrameForDashboard(list, filterDto.get_timeParam(), filterDto.get_timeValue());
                filterDto.set_timeParam("");
                filterDto.set_timeValue("");
            }
            list = filterDto.filter(list);
        } catch (IOException e) {
            alert.prepareError("Error while parsing the filter").log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result(), "Error occurred while fetching payables");
        }
        return JSONToResult.convert(new JSONObject().put("Payables",list).toString());
    }

    public Result fetchAllDetails(Map<String, Object> inputParams, DataControllerRequest request) {

        List<DashboardDTO> list = dashboardBusinessDelegate.fetchAllDetails(inputParams, request);
        if(list == null)
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result(), "Error occurred while fetching trade details");
        try {
            FilterDTO filterDto = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
            list = filterDto.filter(list);
        } catch (IOException e) {
            alert.prepareError("Error while parsing the filter").log();
            return ErrorCodeEnum.ERR_12000.setErrorCode(new Result(), "Error occurred while fetching trade details");
        }
        return JSONToResult.convert(new JSONObject().put("AllTradeDetails",list).toString());
    }

    public Result fetchTradeFinanceConfiguration(Map<String, Object> inputParams, DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        JSONObject configuration = dashboardBusinessDelegate.fetchTradeDashboardConfiguration(customerId);
        if(configuration == null) {
            configuration = new JSONObject();
            configuration.put("customConfig", false);
        }
        else {
            configuration.put("customConfig", true);
        }
        return JSONToResult.convert(configuration.toString());
    }

    public Result createTradeFinanceConfiguration(Map<String, Object> inputParams, DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        inputParams.put("userId", customerId);
        boolean response = dashboardBusinessDelegate.createTradeDashboardConfiguration(inputParams);

        Result result = new Result();
        result.addParam("message", response ? "Success" : "Failed");
        return  result;
    }

    public Result updateTradeFinanceConfiguration(Map<String, Object> inputParams, DataControllerRequest request) {
        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        inputParams.put("userId", customerId);
        boolean response = dashboardBusinessDelegate.updateTradeDashboardConfiguration(inputParams);
        Result result = new Result();
        result.addParam("message", response ? "Success" : "Failed");
        return  result;
    }
}
