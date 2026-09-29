package com.kony.adminconsole.service.productmanagement.businessdelegate.api;

import java.util.Map;

import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.error.DBPApplicationException;

public interface ProductBusinessDelegate extends BusinessDelegate {
	
	public JSONObject createProduct(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject updateProduct(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject getProducts(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;

	public JSONObject createProductFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject updateProductFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public JSONObject deleteProductFacility(Map<String, Object> postParametersMap, String backendToken)
            throws DBPApplicationException;
	
	public String loadProductLines() throws DBPApplicationException;

	public String loadProductGroups() throws DBPApplicationException;

	public String loadProducts() throws DBPApplicationException;

	public String clearMarketingData() throws DBPApplicationException;

	public String loadProductFacilities() throws DBPApplicationException;
	
	public JSONObject getAllProductGroupsCampaign(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
            throws DBPApplicationException;
	
	public JSONObject getProductsByProductGroup(Map<String, Object> postParametersMap, Map<String, Object> headerMap)
            throws DBPApplicationException;

}
