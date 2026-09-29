package com.kony.adminconsole.service.authmodule;

import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class UserAuthenticationPreProcessor implements DataPreProcessor2 {

	private static final String USERNAME_PARAM_FIELD_NAME = "userid";
    private static final String PASSWORD_PARAM_FIELD_NAME = "inputPassword";
    
	@Override
	public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
			Result result) throws Exception {
		
		String username = request.getParameter(USERNAME_PARAM_FIELD_NAME);
        String password = request.getParameter(PASSWORD_PARAM_FIELD_NAME);
		
        if(StringUtils.isNotBlank(username)) {
        	username = URLEncoder.encode( username, "UTF-8");
        	inputMap.put(USERNAME_PARAM_FIELD_NAME, username);
        	request.addRequestParam_(USERNAME_PARAM_FIELD_NAME, username);
		}
        if(StringUtils.isNotBlank(password)) {
        	password = URLEncoder.encode( password, "UTF-8");
        	inputMap.put(PASSWORD_PARAM_FIELD_NAME, password);
        	request.addRequestParam_(PASSWORD_PARAM_FIELD_NAME, password);
		}

		return true;
	}

}
