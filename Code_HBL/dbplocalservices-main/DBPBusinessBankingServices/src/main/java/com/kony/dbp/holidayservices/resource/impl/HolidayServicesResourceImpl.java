package com.kony.dbp.holidayservices.resource.impl;
import java.util.List;
import java.util.Map;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.holidayservices.businessdelegate.api.HolidayServicesBusinessDelegate;
import com.kony.dbp.holidayservices.dto.HolidaysDTO;
import com.kony.dbp.holidayservices.resource.api.HolidayServicesResource;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
public class HolidayServicesResourceImpl implements HolidayServicesResource{
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

	@Override
	public Result getHolidays(String methodID, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) {

		Result result = new Result();

		// Initialization of business Delegate Class
		HolidayServicesBusinessDelegate holidaysBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
				.getFactoryInstance(BusinessDelegateFactory.class)
				.getBusinessDelegate(HolidayServicesBusinessDelegate.class);

		try {

			List<HolidaysDTO> holidaysDTO = holidaysBusinessDelegate.getHolidays();

			if (holidaysDTO == null) {
				return ErrorCodeEnum.ERR_12000.setErrorCode(new Result());
			}
			JSONArray rulesJSONArr = new JSONArray(holidaysDTO);
			JSONObject responseObj = new JSONObject();
			responseObj.put("holidays", rulesJSONArr);
			result = JSONToResult.convert(responseObj.toString());
		} catch (Exception e) {
			alert.prepareError("Caught exception at getHolidays method: " + e).log();
			return ErrorCodeEnum.ERR_12000.setErrorCode(result);
		}

		return result;
	}

}
