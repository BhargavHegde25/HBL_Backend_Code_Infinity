package com.bct.javaservices;

import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import com.kony.dbputilities.memorymanagement.MemoryManager;

public class TestCIPSToken implements JavaService2{

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest dcRequest, DataControllerResponse dcResponse)
			throws Exception {
		Result result = new Result();
		String refreshToken = (String) MemoryManager.getDataFromCache(dcRequest, "CIPS_REFRESH_TOKEN");
		result.addParam(new Param("refreshToken", refreshToken));
		String accessToken = (String) MemoryManager.getDataFromCache(dcRequest, "CIPS_ACCESS_TOKEN");
		result.addParam(new Param("accessToken", accessToken));
		return result;
	}

}
