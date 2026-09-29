package com.kony.logservices.service;

import java.util.Date;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.kony.adminconsole.commons.utils.CommonUtilities;
import com.kony.adminconsole.commons.utils.DateUtils;
import com.kony.logservices.core.AbstractLogJavaService;
import com.kony.logservices.dao.LogDAO;
import com.kony.logservices.dto.TransactionValueVolumeTypeDTO;
import com.kony.logservices.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class TransactionValueVolTypeGetService extends AbstractLogJavaService {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    @Override
    public Object execute(String methodID, Object[] inputArray, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        String startDate = null;
        String endDate = null;

        try {
            startDate = requestInstance.getParameter("startDate");
            endDate = requestInstance.getParameter("endDate");

            if ((StringUtils.isNotBlank(startDate) && StringUtils.isBlank(endDate))
                    || (StringUtils.isBlank(startDate) && StringUtils.isNotBlank(endDate))) {
                ErrorCodeEnum.ERR_29007.setErrorCode(result);
                return result;
            }

            Date startOfDate = null;
            Date endOfDate = null;
            if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
                startOfDate = DateUtils.parseToStartOfDay(startDate, DateUtils.PATTERN_MM_DD_YYYY);
                endOfDate = DateUtils.parseToEndOfDay(endDate, DateUtils.PATTERN_MM_DD_YYYY);
            }

            List<TransactionValueVolumeTypeDTO> transactionValueVolumeTypes = LogDAO
                    .getTransactionValueLogsQuery(startOfDate, endOfDate);
            JSONObject json = new JSONObject();
            json.put("records", new JSONArray(transactionValueVolumeTypes));

            JSONObject resultJSONObject = new JSONObject(json.toString());
            return CommonUtilities.constructResultFromJSONObject(resultJSONObject);
        } catch (Exception e) {
            alert.prepareError("Failed while executing get TransactionValueVolTypeGetService logs", e).log();
            ErrorCodeEnum.ERR_29003.setErrorCode(result);
        }
        return result;
    }
}
