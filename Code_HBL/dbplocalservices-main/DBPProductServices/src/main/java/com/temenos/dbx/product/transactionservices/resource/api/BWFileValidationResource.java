package com.temenos.dbx.product.transactionservices.resource.api;  

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.BusinessDelegate;
import com.dbp.core.api.Resource;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Result;

public interface BWFileValidationResource extends Resource {
	
	
	/**
	 * validate a bulk wire file for the specified validations 
     * @param fileId
     * @return Map of line items
     * @author KH2144
	 * @param file 
	 * @param requestInstance 
	 * @throws Exception 
     */	
	public Map<String, Object> validateFile(File inputFile, DataControllerRequest requestInstance) throws Exception;
	
	/**
	 * append fileId to bulk wire file contents 
     * @param Map of line items
     * @param fileId
     * @return Map of line items
     * @author KH2144
     */	
	Map<String, Object> appendFileId(Map<String, Object> bWfileMapContents, String fileId);
	
	/**
	 * validate a bulk wire templates file for the specified validations 
     * @param fileId
     * @return JSON of recipients
     * @author KH2144
	 * @param file 
	 * @param requestInstance 
     */	
	public JSONObject validateTemplateFile(File uploadedFile, DataControllerRequest requestInstance);
	
	/**
	 * get the file type id if a valid extension
     * @param fileExtension
     * @return typeId of the file type
     * @author KH2144 
     */	
	public String getFileFormat(String fileExtension);
	
	/**
	 *  
	 * @param fileName 
     * @param fileExtension
     * @return validity of the file name
     * @author KH2144 
     */	
	boolean isValidFileName(String fileName, String fileExtension);

}