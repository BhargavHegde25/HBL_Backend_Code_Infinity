package com.infinity.dbx.temenos.paylater;


import static com.infinity.dbx.temenos.transactions.TransactionConstants.PARAM_OFFSET;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.infinity.dbx.temenos.TemenosBasePreProcessor;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CreateInstallmentPreProcessor extends TemenosBasePreProcessor {
	@Override
	public boolean execute(HashMap params, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {

		
		String override = CommonUtils.getParamValue(params, PayLaterConstants.PARAM_OVERRIDE);
		
		if(StringUtils.isNotBlank(override))
		{
			override=StringEscapeUtils.unescapeJson(override);
			params.put( PayLaterConstants.PARAM_OVERRIDE, override);
		}
		
		return Boolean.TRUE;
	}
	
}
