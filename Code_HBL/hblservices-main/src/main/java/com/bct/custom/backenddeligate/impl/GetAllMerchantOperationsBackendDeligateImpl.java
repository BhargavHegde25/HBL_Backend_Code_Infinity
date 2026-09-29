package com.bct.custom.backenddeligate.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bct.custom.backenddeligate.api.GetAllMerchantOperationsBackendDeligate;
import com.bct.custom.constants.HBLURLConstants;
import com.bct.custom.dto.MerchantCategoriesDTO;
import com.dbp.core.error.DBPApplicationException;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.commons.exception.ApplicationException;

public class GetAllMerchantOperationsBackendDeligateImpl implements GetAllMerchantOperationsBackendDeligate {
	private static final Logger logger = LogManager.getLogger(GetAllMerchantOperationsBackendDeligateImpl.class);
	public static final String DBPERRMSG = "dbpErrMsg";
	public static final String DBPERRCODE = "dbpErrCode";
	String serviceId = "HBLCustomCRUDService";
	String operationid = "dbxdb_merchantcategory_get";

	private static Result callDBService(String serviceid, String operationid, Map<String, Object> inputmap,
			Map<String, Object> headers) {
		Result res = new Result();
		try {
			res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
					.withServiceId(serviceid).withRequestHeaders(headers).build().getResult();
		} catch (Exception e) {
			logger.debug("Error occured in callDBService", e.toString());
			res.addParam(new Param(DBPERRMSG, e.getMessage(), "String"));
			return res;

		}
		return res;
	}

	private static String callInternalServiceAndGetString(String serviceid, String operationid,
			Map<String, Object> inputmap, Map<String, Object> headers) throws DBPApplicationException {
		String res = DBPServiceExecutorBuilder.builder().withOperationId(operationid).withRequestParameters(inputmap)
				.withServiceId(serviceid).withRequestHeaders(headers).build().getResponse();
		logger.debug("HBL::MerchantCategoriesBackendDeligateImpl: callInternalServiceAndGetString: response:" + res);

		return res;
	}

	@Override
	public JSONArray getAllMerchantCategories(String code, Map<String, Object> headerMap) throws ApplicationException {
		MerchantCategoriesDTO merchantsDTO = new MerchantCategoriesDTO();
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "";
		if(StringUtils.isNotBlank(code)) {
		filter = "subcategoryof eq '" + code + "'";
		if(headerMap.get("filterType")!=null && headerMap.get("filterType").toString().equalsIgnoreCase("merchantCode") ) {
		filter = "code eq '" + code + "'";
		}
		inputmap.put(HBLURLConstants.FILTER, filter);
		}
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl:getAllMerchantCategories: inputmap:"+
				inputmap.toString());
		JSONArray dbCategories = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_ALL_MERCHANT_CATEGORIES_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getAllMerchantCategories response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			dbCategories = responseJSON.getJSONArray("merchantcategory");
			/*if(dbCategories.isEmpty()) {
				inputmap = new HashMap<>();
				inputmap.put("category",category);
				String connectIPSResp = DBPServiceExecutorBuilder.builder()
						.withOperationId(HBLURLConstants.GET_MERCHANT_DETAILS_OPERATION)
						.withRequestParameters(inputmap).withServiceId(HBLURLConstants.SERVICEID)
						.withRequestHeaders(headerMap).build().getResponse();
				 responseJSON = new JSONObject(connectIPSResp);
				 dbCategories = responseJSON.getJSONArray("merchantdetails");
			}
			*/
			
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant categories:" +e.toString());
			
		}
		return dbCategories;
	}

	@Override
	public JSONArray getMerchantSubCategoriesByCategoryName(String categoryName,
			Map<String, Object> headerMap)throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "subcategoryof eq '" + categoryName + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantSubCategoriesByCategoryName inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_ALL_MERCHANT_CATEGORIES_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantSubCategoriesByCategoryName response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantcategory");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant subcategories:" +e.toString());
			
		}
		return types;
	}
	@Override
	public JSONArray getAllMerchants(Map<String, Object> headerMap)throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, Object> inputmap = new HashMap<>();
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getAllMerchants inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getAllMerchants response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantdetails");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant details:" +e.toString());
			
		}
		return types;
	}
	@Override
	public JSONArray getMerchantDetailsByCategory(String code,
			Map<String, Object> headerMap)throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "categoryof eq '" + code + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantDetailsByCategory inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantDetailsByCategory response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantdetails");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant details:" +e.toString());
			
		}
		return types;
	}
	@Override
	public JSONArray getMerchantFields(String merchantCode,
			Map<String, Object> headerMap)throws ApplicationException {
		// TODO Auto-generated method stub
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "merchantCode eq '" + merchantCode + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantFields inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_FIELDS_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.HBL_OLB_CRUD_OPERATION_SERVICE)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantFields response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantfields");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant fileds:" +e.toString());
			
		}
		return types;
	}
	@Override
	public JSONArray getMerchantDetails(String code,
			Map<String, Object> headerMap)throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "code eq '" + code + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantDetails inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MERCHANT_DETAILS_OPERATION)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantDetails response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("merchantdetails");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant details:" +e.toString());
			
		}
		return types;
	}
	
	@Override
	public JSONArray getMerchantSubCategoriesForAdmin(String categoryName,
			Map<String, Object> headerMap)throws ApplicationException {
		Map<String, Object> inputmap = new HashMap<>();
		String filter = "subcategoryof eq '" + categoryName + "'";
		inputmap.put(HBLURLConstants.FILTER, filter);
		logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantSubCategoriesByCategoryName inputmap:"+
				inputmap.toString());
		JSONArray types = new JSONArray();
		try {
			String response = DBPServiceExecutorBuilder.builder()
					.withOperationId(HBLURLConstants.GET_MASTER_MERCHANTS)
					.withRequestParameters(inputmap).withServiceId(HBLURLConstants.C360_SERVICEID)
					.withRequestHeaders(headerMap).build().getResponse();
			logger.debug("BCT::MerchantCategoriesBackendDeligateImpl: getMerchantSubCategoriesByCategoryName response:"+response);
			JSONObject responseJSON = new JSONObject(response);
			types = responseJSON.getJSONArray("mastermerchants");
		}catch (Exception e) {
			logger.error("Exception caught while fetching merchant subcategories:" +e.toString());
			
		}
		return types;
	}

	
	

}
