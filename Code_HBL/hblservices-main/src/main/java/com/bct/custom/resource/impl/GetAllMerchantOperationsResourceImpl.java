package com.bct.custom.resource.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bct.custom.businessdeligate.api.GetAllMerchantOperationsBusinessDeligate;
import com.bct.custom.businessdeligate.api.MerchantChargesBusinessDelegate;
import com.bct.custom.businessdeligate.api.MerchantFieldsBusinessDeligate;
import com.bct.custom.businessdeligate.api.PaymentAggregatorBusinessDeligate;
import com.bct.custom.constants.HBLConstants;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.bct.custom.resource.api.GetAllMerchantOperationsResource;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.constants.DBPConstants;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class GetAllMerchantOperationsResourceImpl implements GetAllMerchantOperationsResource {
	private static final Logger LOG = LogManager.getLogger(GetAllMerchantOperationsResourceImpl.class);
	//private static final String staticLogoURL="https://www.connectips.com/cdn/CONNECTIPS/connectipsweb/images/dashboard/";
	private static final String staticLogoURL="";
	static MerchantChargesBusinessDelegate chargesBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(MerchantChargesBusinessDelegate.class);
	static MerchantFieldsBusinessDeligate fieldsBusinessDelegate = DBPAPIAbstractFactoryImpl
			.getBusinessDelegate(MerchantFieldsBusinessDeligate.class);

	@Override
	public Result getAllMerchantCategories(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		if(methodID.equalsIgnoreCase("getMerchantCategoriesByCode")) {
		result=getMerchantCategoriesByMerchantCode(methodID, inputArray, dcRequest, dcResponse);
		}else {
		result=getMerchnatCategoriesOrMerchantDetails(methodID, inputArray, dcRequest, dcResponse);
		}
		return result;
	}

	@Override
	public Result getMerchantSubCategoriesByCategoryName(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String category = inputParams.get("category");
		LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantSubCategoriesByCategoryName:category:"
				+ category.toString());
		Result result = new Result();
		if (StringUtils.isBlank(category)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		try {
			GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
			JSONArray merchantCategories = businessDelegate.getMerchantSubCategoriesByCategoryName(category,
					dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantSubCategoriesByCategoryName:"
					+ merchantCategories.toString());
			if(category.equalsIgnoreCase("ALL")) {
				JSONObject merchantCategoriesForAdmin = new JSONObject();
				JSONArray newMerchantCategories=merchantCategoriesForAdmin(merchantCategories);
				merchantCategoriesForAdmin.put("categories", newMerchantCategories);
                result = JSONToResult.convert(merchantCategoriesForAdmin.toString());
			}
			else {
			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray1(merchantCategories);
			ds.setId("subCategories");
			result.addDataset(ds);
			}
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		return result;
	}

	public JSONArray merchantCategoriesForAdmin(JSONArray merchantCategories) {
		JSONArray newMerchantCategories = new JSONArray();
		for(int i=0;i<merchantCategories.length();i++) {
			JSONObject obj = merchantCategories.getJSONObject(i);//new JSONObject();
			//obj.put("labelText", merchantCategories.getJSONObject(i).getString("labelText"));
			//obj.put("code", merchantCategories.getJSONObject(i).getString("code"));
			//obj.put("logoUrl", merchantCategories.getJSONObject(i).getString("logoUrl"));
			
			newMerchantCategories.put(obj);
		}
		return newMerchantCategories;
	}

	public Result getAllMerchants(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		Result result = new Result();
		if(methodID.equalsIgnoreCase("GetAllMerchants")){
		try {
			GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
			JSONArray merchantCategories = businessDelegate.getAllMerchants(dcRequest.getHeaderMap());
			Dataset ds = new Dataset();
			ds = constructMerchantsDatasetFromJSONArray(merchantCategories);
			ds.setId("merchants");
			result.addDataset(ds);
			LOG.debug("Result categories:" + ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		}else if(methodID.equalsIgnoreCase("GetMerchantDetails")){
			String code = inputParams.get("merchantCode");
			if (StringUtils.isBlank(code)) {
				throw new ApplicationException(ErrorCodeEnum.ERR_10755);
			}
			try {
				GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
						.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
				JSONArray merchantdetails = businessDelegate.getMerchantDetails(code,
						dcRequest.getHeaderMap());
				LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantDetailsByCode:"
						+ merchantdetails.toString());
				JSONObject merchantDetailsResponse = constructDataForManualMerchantDetails(code,merchantdetails,dcRequest);
				result = JSONToResult.convert(merchantDetailsResponse.toString());
				result.setParam(new Param("opstatus", "0"));
				result.setParam(new Param("httpStatusCode", "200"));
			} catch (Exception e) {
				// TODO: handle exception
				result.addParam(new Param("dbpErrCode", e.toString()));
				result.addParam(new Param("dbpErrMsg", "10002"));
			}
		}
		return result;
	}

	@Override
	public Result getMerchantsByCategory(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String category = inputParams.get("category");
		Result result = new Result();
		if (StringUtils.isBlank(category)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		try {
			GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
			JSONArray merchantCategories = businessDelegate.getMerchantDetailsByCategory(category,
					dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantDetailsByCategory:"
					+ merchantCategories.toString());

			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray1(merchantCategories);
			ds.setId("merchants");
			result.addDataset(ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		return result;
	}

	@Override
	public Result getMerchantFields(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String merchantCode = inputParams.get("appCode");
		Result result = new Result();
		if (StringUtils.isBlank(merchantCode)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		try {
			dcRequest.setAttribute("appCode", merchantCode);
			GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
			JSONArray merchantFields = businessDelegate.getMerchantFields(merchantCode, dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantFields:" + merchantFields.toString());
			JSONObject obj= categorizeFields(merchantFields);
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantFieldsResponse:" + obj);
			JSONObject resultObj= new JSONObject();
			resultObj.put("merchantFields", obj);
			if(obj.has("requiredFields") && obj.getJSONArray("requiredFields").length()>0) {
				JSONObject merchantObj = getMerchantDetails(merchantCode, dcRequest);
				String outageMessage=merchantObj.has("outageMessage")?merchantObj.getString("outageMessage"):"";
				String disclimerMessage=merchantObj.has("disclimerMessage")?merchantObj.getString("disclimerMessage"):"";
				String paymentAggregator=merchantObj.has("paymentAggregator")?merchantObj.getString("paymentAggregator"):"";
				resultObj.put("outageMessage", outageMessage);
				resultObj.put("disclimerMessage", disclimerMessage);
				resultObj.put("paymentAggregator", paymentAggregator);
			}
            result = JSONToResult.convert(resultObj.toString());
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("success", "true"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
			result.setParam(new Param("success", "false"));
		}
		return result;
	}
	public static JSONObject categorizeFields(JSONArray JSONArray) {
		JSONArray requestFields = new JSONArray();
		JSONArray responseFields = new JSONArray();
		JSONObject responseObj= new JSONObject();
		for (int count = 0; count < JSONArray.length(); count++) {
			JSONObject obj= JSONArray.getJSONObject(count);
			String fieldType = obj.get("fieldtype").toString();
			String fieldName = obj.get("fieldname").toString();
			fieldName = StringUtils.capitalize(fieldName);
			String id = obj.get("id").toString();
			String fieldId = null;
			if (fieldType.equals("textbox")) {
				fieldId = "tbx" + fieldName + id;
			} else if (fieldType.equals("label")) {
				fieldId = "lbl" + fieldName + id;
			} else if (fieldType.equals("listbox")) {
				fieldId = "lbx" + fieldName + id;
			}
			if (fieldId != null)
				obj.put("fieldId", fieldId);
			
			if(obj.has("fieldcategory") && obj.getString("fieldcategory").equalsIgnoreCase("Request")) {
				requestFields.put(obj);
			}
			
			else if(obj.has("fieldcategory") && obj.getString("fieldcategory").equalsIgnoreCase("Response")) {
				responseFields.put(obj);
			}
		}
		responseObj.put("requiredFields",sortJsonArray(requestFields,"fieldindex"));
		responseObj.put("responseFieldMapping",sortJsonArray(responseFields,"fieldindex"));
		return responseObj;
	}
	@Override
	public Result getAllPaymentAggregators(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		PaymentAggregatorBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PaymentAggregatorBusinessDeligate.class);
		try {
			JSONArray merchantCategories = businessDelegate.getAllPaymentAggregator(dcRequest.getHeaderMap());
			LOG.debug("BCT::PaymentAggregatorResourceImpl::paymentAggregators: " + merchantCategories.toString());
			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray2(merchantCategories);
			ds.setId("paymentaggregator");
			result.addDataset(ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.getLocalizedMessage()));
			result.addParam(new Param("dbpErrMsg", "10001"));
		}
		return result;
	}
	
	@Override
	public Result getPaymentAggregator(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		PaymentAggregatorBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(PaymentAggregatorBusinessDeligate.class);
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String name = inputParams.get("aggregatorName");
		if (StringUtils.isBlank(name)) {
			result = getAllPaymentAggregators(methodID, inputArray, dcRequest, dcResponse);
			return result;
		}
		try {
			JSONArray paymentAggregators = businessDelegate.getPaymentAggregator(name, dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getPaymentAggregator:" + paymentAggregators.toString());

			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray2(paymentAggregators);
			ds.setId("paymentaggregator");
			result.addDataset(ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		return result;
	}
	@Override
	public Result getMerchantDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String code = inputParams.get("code");
		Result result = new Result();
		if (StringUtils.isBlank(code)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		try {
			GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
			JSONArray merchantCategories = businessDelegate.getMerchantDetails(code,
					dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantDetailsByCode:"
					+ merchantCategories.toString());

			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray1(merchantCategories);
			ds.setId("merchants");
			result.addDataset(ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		return result;
	}

	public static Dataset constructDatasetFromJSONArray2(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			dataset.addRecord(record);
		}
		return dataset;
	}
	public static Dataset constructDatasetFromJSONArray1(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			String logo=record.getParamValueByName("logoUrl").toString();
			String category=record.getParamValueByName("category").toString();
			/*if(category.equals("APP")) {
				record.removeParamByName("category");
				record.addStringParam("category", "CATEGORY");
			}
			*/
			record.removeParamByName("logoUrl");
			record.addStringParam("logoUrl", logo);
			String merchantType=record.getParamValueByName("merchantType");
			if(merchantType!=null && merchantType.equalsIgnoreCase("Automatic")) {
				record.addStringParam("logoUrl", staticLogoURL+logo);
			}
			dataset.addRecord(record);
		}
		return dataset;
	}
	
	public static Dataset constructMerchantsDatasetFromJSONArray(JSONArray JSONArray) {
		Dataset dataset = new Dataset();
		for (int count = 0; count < JSONArray.length(); count++) {
			Record record = constructRecordFromJSONObject((JSONObject) JSONArray.get(count));
			record.removeParamByName("category");
			record.addStringParam("category", "APP");
			String logo=record.getParamValueByName("logoUrl").toString();
			record.removeParamByName("logoUrl");
			record.addStringParam("logoUrl", logo);
			String merchantType=record.getParamValueByName("merchantType");
			if(merchantType!=null && merchantType.equalsIgnoreCase("Automatic")) {
				record.addStringParam("logoUrl", staticLogoURL+logo);
			}
			record.addStringParam("paymentaggregator", record.getParamValueByName("paymentaggregator"));
			dataset.addRecord(record);
		}
		return dataset;
	}

	public static JSONArray postProcessFieldsResponse(JSONArray response) {
		try {
			for (int i = 0; i < response.length(); i++) {
				JSONObject obj = (JSONObject) response.get(i);
				String fieldType = obj.get("fieldtype").toString();
				String fieldName = obj.get("fieldname").toString();
				fieldName = StringUtils.capitalize(fieldName);
				String id = obj.get("id").toString();
				// response.getJSONObject(i).getString(fieldType);
				String fieldId = null;
				if (fieldType.equals("textbox")) {
					fieldId = "tbx" + fieldName + id;
				} else if (fieldType.equals("label")) {
					fieldId = "lbl" + fieldName + id;
				} else if (fieldType.equals("listbox")) {
					fieldId = "lbx" + fieldName + id;
				}
				if (fieldId != null)
					obj.put("fieldId", fieldId);
			}
		} catch (Exception e) {
			LOG.error("HBL::Exception in processFieldsResponse:" + e.toString());
		}
		return response;

	}

	public static Record constructRecordFromJSONObject(JSONObject JSONObject) {
		Record response = new Record();
		if (JSONObject == null || JSONObject.length() == 0) {
			return response;
		}
		Iterator<String> keys = JSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();
			if (JSONObject.get(key) instanceof String) {
				Param param = new Param(key, JSONObject.getString(key), DBPConstants.FABRIC_STRING_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Integer) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_INT_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof Boolean) {
				Param param = new Param(key, JSONObject.get(key).toString(), DBPConstants.FABRIC_BOOLEAN_CONSTANT_KEY);
				response.addParam(param);

			} else if (JSONObject.get(key) instanceof JSONArray) {
				Dataset dataset = constructDatasetFromJSONArray2(JSONObject.getJSONArray(key));
				dataset.setId(key);
				response.addDataset(dataset);
			}
		}

		return response;
	}
	public static JSONObject constructDataForManualMerchantDetails(String code, JSONArray response, DataControllerRequest dcRequest) {
		Map<String, Object> inputMap = new HashMap<String, Object>();
		JSONArray chargesResponse = new JSONArray();
		JSONArray merchantFieldsResponse= new JSONArray();
		JSONObject merchantDetailsResponse = new JSONObject();
		var count=0;
			JSONObject merchants = response.getJSONObject(count);
			String fieldSetup=merchants.has("fieldSetup")?merchants.get("fieldSetup").toString():"";
			//if(fieldSetup.equalsIgnoreCase("Manual")) {
				try {
					 chargesResponse = chargesBusinessDelegate.getMerchantCharges(code, inputMap, dcRequest.getHeaderMap());
					  merchantFieldsResponse = fieldsBusinessDelegate.getMerchantFileds(code, inputMap, dcRequest.getHeaderMap());
				}catch (Exception e) {
					LOG.error("HBL::Exception in fetching Merchant Charges and Fields:" + e.getLocalizedMessage());
				}
			//}
			JSONObject merchantFieldsObj = processRequestResponseMerchantFields(merchantFieldsResponse);
			merchants.put("limitsAndCharges", chargesResponse);
			merchants.put(HBLConstants.REQUEST_MERCHANT_FIELDS, merchantFieldsObj.get(HBLConstants.REQUEST_MERCHANT_FIELDS));
			merchants.put(HBLConstants.RESPONSE_MERCHANT_FIELDS, merchantFieldsObj.get(HBLConstants.RESPONSE_MERCHANT_FIELDS));
			String logo=merchants.has("logoUrl")?merchants.get("logoUrl").toString():"";
			merchants.put("logoUrl", logo);
			merchantDetailsResponse.put("merchants", merchants);
			LOG.debug("HBL::constructDataForManualMerchantDetails getMerchantDetails  final response:" + merchantDetailsResponse.toString());
		return merchantDetailsResponse;
	}
	public static JSONObject processRequestResponseMerchantFields(JSONArray merchantFieldsResponse) {
		JSONArray reqFieldsResponse = new JSONArray();
		JSONArray resFieldsResponse = new JSONArray();
		JSONObject merchantFields = new JSONObject();
		for(int i=0;i<merchantFieldsResponse.length();i++) {
			JSONObject merchantFieldsObj= merchantFieldsResponse.getJSONObject(i); 
			if(merchantFieldsObj.getString("fieldcategory").equalsIgnoreCase("Request")) {
				reqFieldsResponse.put(merchantFieldsObj);
			}
			if(merchantFieldsObj.getString("fieldcategory").equalsIgnoreCase("Response")) {
				resFieldsResponse.put(merchantFieldsObj);
			}
		}
		merchantFields.put(HBLConstants.REQUEST_MERCHANT_FIELDS, reqFieldsResponse);
		merchantFields.put(HBLConstants.RESPONSE_MERCHANT_FIELDS, resFieldsResponse);
		return merchantFields;
		
	}
	@Override
	public Result getMerchantSubCategoriesForAdmin(String methodID, Object[] inputArray,
			DataControllerRequest dcRequest, DataControllerResponse dcResponse) throws ApplicationException {
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String category = inputParams.get("category");
		LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantSubCategoriesForAdmin:category:"
				+ category.toString());
		Result result = new Result();
		if (StringUtils.isBlank(category)) {
			throw new ApplicationException(ErrorCodeEnum.ERR_10755);
		}
		try {
			GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
			JSONArray merchantCategories = businessDelegate.getMerchantSubCategoriesForAdmin(category,
					dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantSubCategoriesForAdmin:"
					+ merchantCategories.toString());
			if(category.equalsIgnoreCase("ALL")) {
				JSONObject merchantCategoriesForAdmin = new JSONObject();
				JSONArray newMerchantCategories=merchantCategoriesForAdmin(merchantCategories);
				merchantCategoriesForAdmin.put("categories", newMerchantCategories);
                result = JSONToResult.convert(merchantCategoriesForAdmin.toString());
			}
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.toString()));
			result.addParam(new Param("dbpErrMsg", "10002"));
		}
		return result;
	}
	public JSONObject getMerchantDetails(String merchantCode, DataControllerRequest dcRequest ) throws com.temenos.infinity.api.commons.exception.ApplicationException {
		GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
		JSONArray merchantdetails = businessDelegate.getMerchantDetails(merchantCode,
				dcRequest.getHeaderMap());
		LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantFields:merchantdetails:"
				+ merchantdetails.toString());
		JSONObject jsonObj= new JSONObject();
		for(int i=0;i<merchantdetails.length();i++) {
			jsonObj.put("outageMessage", merchantdetails.getJSONObject(i).getString("outageMessage"));
			jsonObj.put("disclimerMessage", merchantdetails.getJSONObject(i).getString("disclimerMessage"));
			jsonObj.put("paymentAggregator", merchantdetails.getJSONObject(i).getString("paymentaggregator"));
		}
		return jsonObj;
		
	}
	public Result getMerchnatCategoriesOrMerchantDetails(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String code = inputParams.get("code");
		MerchantCategoriesDTO merchantCategoriesDTO = new MerchantCategoriesDTO();
		GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
		try {
			JSONArray merchantCategories = businessDelegate.getAllMerchantCategories(code, dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::merchantCategories: " + merchantCategories.toString());
			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray1(merchantCategories);
			
			if(merchantCategories.isEmpty()) {
				JSONArray merchants = businessDelegate.getMerchantDetailsByCategory(code,
						dcRequest.getHeaderMap());
				ds = constructMerchantsDatasetFromJSONArray(merchants);
			}
			ds.setId("categories");
			result.addDataset(ds);
			LOG.debug("Result categories:" + ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.getLocalizedMessage()));
			result.addParam(new Param("dbpErrMsg", "10001"));
		}
		return result;
	}
	@Override
	public Result getMerchantCategoriesByMerchantCode(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
			DataControllerResponse dcResponse) throws ApplicationException {
		Result result = new Result();
		Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
		String code = inputParams.get("code");
		MerchantCategoriesDTO merchantCategoriesDTO = new MerchantCategoriesDTO();
		GetAllMerchantOperationsBusinessDeligate businessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(GetAllMerchantOperationsBusinessDeligate.class);
		try {
			dcRequest.getHeaderMap().put("filterType", "merchantCode");
			JSONArray merchantCategories = businessDelegate.getAllMerchantCategories(code, dcRequest.getHeaderMap());
			LOG.debug("BCT::MerchantCategoriesResourceImpl::getMerchantCategoriesByMerchantCode: " + merchantCategories.toString());
			Dataset ds = new Dataset();
			ds = constructDatasetFromJSONArray1(merchantCategories);
			ds.setId("categories");
			result.addDataset(ds);
			LOG.debug("Result categories:" + ds);
			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			// TODO: handle exception
			result.addParam(new Param("dbpErrCode", e.getLocalizedMessage()));
			result.addParam(new Param("dbpErrMsg", "10001"));
		}
		return result;
	}
	public static JSONArray sortJsonArray(JSONArray array, String sortKey) {
		JSONArray sortedJsonArray = new JSONArray();
		List list=array.toList();
		System.out.println(list);
		 Collections.sort(list,new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				 HashMap a = (HashMap) o1;
				 HashMap b = (HashMap) o2;
				String str1 = new String();
	            String str2 = new String();
	            int i= 0;
	            int j=0;
	            try {
	               str1 = (String)a.get(sortKey);
	               str2 = (String)b.get(sortKey);
	            } catch(JSONException e) {
	               e.printStackTrace();
	            }
	            return str1.compareTo(str2);
			}
	      });
		 
		 for(int i = 0; i < list.size(); i++) {
			 sortedJsonArray.put(list.get(i));
	      }
		 return sortedJsonArray;
	}

}
