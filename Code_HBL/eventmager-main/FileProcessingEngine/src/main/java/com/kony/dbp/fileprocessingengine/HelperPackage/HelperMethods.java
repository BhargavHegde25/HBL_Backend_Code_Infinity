package com.kony.dbp.fileprocessingengine.HelperPackage;

import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;

public class HelperMethods {

	public static JsonObject mergeJsonObjects(JsonObject src, JsonObject dest) {
		Set<Entry<String, JsonElement>> s = src.entrySet();
		for (Entry<String, JsonElement> elem : s) {
			dest.add(elem.getKey(), elem.getValue());
		}
		return dest;
	}
	
	public static String deriveToken(ServicesManager servicesManager, String events) {
		ConfigurableParametersHelper configHelper = servicesManager.getConfigurableParametersHelper();
		String secret = configHelper.getServerProperty("QUEUEMASTER_SHARED_SECRET");
		if (secret == null || secret.length() == 0) {
			throw new RuntimeException("QueueMaster shared secret has not been configured!");
		}
		String eventsHash = Hashing.sha512().hashString(events, Charsets.UTF_8).toString(); // Hashing using Guava lib
		String saltedSecret = eventsHash + secret;
		return Hashing.sha512().hashString(saltedSecret, Charsets.UTF_8).toString();
	}
	
	public static Result getErrorResult(int errorNumber, String errorMsg) {
		Result result = new Result();
		result.addParam(new Param("errornumber", Integer.toString(errorNumber)));
		result.addParam(new Param("errormsg", errorMsg));
		result.addParam(new Param("success", "false"));
		return result;
	}
	
	public static Result getErrorResult(Throwable ex) {
		String errorMsg = ex.getMessage();
		if (errorMsg == null) {
			StackTraceElement ste = ex.getStackTrace()[0];
			errorMsg = ex.getClass().getName() + "' thrown in " + ste.getClassName() + "." + ste.getMethodName();
		}
		return getErrorResult(7777, errorMsg);
	}

	public static void main(String[] args) {
		JsonObject obj1 = new JsonObject();
		JsonObject obj2 = new JsonObject();

		obj1.addProperty("hello", "val1");
		obj2.addProperty("key2", "val2");

		System.out.println(mergeJsonObjects(obj1, obj2));
	}

}
