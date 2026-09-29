package com.bct.preprocessor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class getAccountActivityPreprocessor implements DataPreProcessor2 {
	private static final Logger LOG = LogManager.getLogger(getAccountActivityPreprocessor.class);
	@SuppressWarnings("unchecked")
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		String accountId = request.getParameter("accountId");
		String date =  new SimpleDateFormat("yyyyMMdd").format(new Date());
		LOG.debug("accountId:"+ accountId);
		SimpleDateFormat toDate = new SimpleDateFormat("yyyyMMdd"); 
		Calendar fromDate = Calendar.getInstance();
		fromDate.add(Calendar.DATE, -30);
		Date d = fromDate.getTime(); // get a Date object
		String yesDate = toDate.format(d); 
		
		LOG.debug("Current date:"+ date);
		LOG.debug("fromDate:"+ yesDate);
		
		
		params.put("toDate", date);
		
		/** This section has to delete post testing 
		 * Start
		 */
		//params.put("fromDate", yesDate);
		params.put("fromDate", "20230301");
		params.put("toDate", date);
		
		/** End
		 * 
		 */
		
		return true;
	}
}
