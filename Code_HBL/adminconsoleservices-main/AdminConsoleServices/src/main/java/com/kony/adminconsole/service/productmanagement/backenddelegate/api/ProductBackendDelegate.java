package com.kony.adminconsole.service.productmanagement.backenddelegate.api;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.BackendDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface ProductBackendDelegate extends BackendDelegate {

	public JSONObject createProduct(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject createProductFacilities(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject createProductFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject createProductImages(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject updateProduct(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getProduct(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getProductList(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject createProductFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject updateProductFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject deleteProductFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getProductFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject editProductFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject deleteProductFeatures(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getProductImages(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject updateProductImages(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject deleteProductImages(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getAccountLevelFeatureDetails(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;

	public JSONObject getProductLines() throws DBPApplicationException;

	public JSONObject getProductGroups() throws DBPApplicationException;

	public JSONObject getProducts() throws DBPApplicationException;

	public String createProductLines(JSONArray productLines) throws DBPApplicationException;
	public String createProductGroups(JSONArray productLines) throws DBPApplicationException;
	public String createProducts(JSONArray productLines) throws DBPApplicationException;
	public String clearMarketingData() throws DBPApplicationException;
	public JSONObject getProductConditions() throws DBPApplicationException;
	public String createProductfacilities(JSONArray responseObject) throws DBPApplicationException;
	
	public JSONObject getAllProductGroupsCampaign(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
            throws DBPApplicationException;
	
	public JSONObject getProductsByProductGroup(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
            throws DBPApplicationException;
}
