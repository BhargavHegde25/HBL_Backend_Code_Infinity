package com.temenos.infinity.api.wealth.tap.preandpostprocessors;

import java.util.HashMap;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.wealthservices.constants.TemenosConstants;
/**
 * 
 * (INFO) Prepares the input for the TAP service in the desired format. 
 * 
 * @author s.swapna
 *
 */

public class GetAccountActivityPreProcessor implements DataPreProcessor2{
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		String dateFrom = request.getParameter(TemenosConstants.DATEFROM).toString();
		String dateTo = request.getParameter(TemenosConstants.DATETO).toString();
		String startDate = dateFrom.substring(0,4) + "-" + dateFrom.substring(4,6) + "-" + dateFrom.substring(6,8);
		String endDate = dateTo.substring(0,4) + "-" + dateTo.substring(4,6) + "-" + dateTo.substring(6,8);
        inputMap.put(TemenosConstants.DATEFROM, startDate);
        inputMap.put(TemenosConstants.DATETO, endDate);
		//inputMap.put(TemenosConstants.DATEFROM, "2021-05-01");
        //inputMap.put(TemenosConstants.DATETO, "2021-05-31");
        String orderBy = inputMap.get(TemenosConstants.ORDERBY).toString();
		String orderType = inputMap.get(TemenosConstants.SORTTYPE).toString().toLowerCase();
		orderBy = orderBy.concat("%20").concat(orderType);
		inputMap.put(TemenosConstants.ORDERBY,orderBy);
        
        return true;
	}
	

}
