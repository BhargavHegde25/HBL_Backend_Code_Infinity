package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;

public class GetDashboardRecentActivityPreProcessor implements DataPreProcessor2 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		
		int no_of_days=Integer.parseInt(EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_RCNT_ACTY_DAYS,request).toString().trim() == null ? "7" : EnvironmentConfigurationsHandler.getValue(TemenosConstants.INF_WLTH_RCNT_ACTY_DAYS,request).toString().trim());
		Calendar sdt = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String toDate=sdf.format(sdt.getTime());
		Calendar edt = Calendar.getInstance();
		edt.add(Calendar.DATE, -no_of_days);
		String fromDate=sdf.format(edt.getTime());
		inputMap.put(TemenosConstants.DATEFROM, fromDate);
		inputMap.put(TemenosConstants.DATETO, toDate);
		return true;
	}

}
