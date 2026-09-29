package com.temenos.dbx.product.dto;

import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Occupation implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2067848625182706144L;
	private String occupationType;
	private String occupationDescription;
	private JSONObject extensionData;
	
	public Occupation() {
	}
	
	/**
	 * @return the occupationType
	 */
	public String getOccupationType() {
		return occupationType;
	}
	/**
	 * @param occupationType the occupationType to set
	 */
	public void setOccupationType(String occupationType) {
		this.occupationType = occupationType;
	}
	/**
	 * @return the occupationDescription
	 */
	public String getOccupationDescription() {
		return occupationDescription;
	}
	/**
	 * @param occupationDescription the occupationDescription to set
	 */
	public void setOccupationDescription(String occupationDescription) {
		this.occupationDescription = occupationDescription;
	}
	/**
	 * @return the extensionData
	 */
	public JSONObject getExtensionData() {
		return extensionData;
	}
	/**
	 * @param extensionData the extensionData to set
	 */
	public void setExtensionData(JSONObject extensionData) {
		this.extensionData = extensionData;
	}

}