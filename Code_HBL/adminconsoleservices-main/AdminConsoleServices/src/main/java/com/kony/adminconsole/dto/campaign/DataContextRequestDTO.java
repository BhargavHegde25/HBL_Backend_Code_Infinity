package com.kony.adminconsole.dto.campaign;

import java.util.HashMap;
import java.util.Map;

public class DataContextRequestDTO {
	
	private String requesterID;
	
	private String endPointURL;
	
	private String filter;
	
	private String dataContextID;
	
	private Map<String,Object> headerMap = new HashMap<>();
	
	public DataContextRequestDTO(String requesterID, String endPointURL,
			String filter, String dataContextID) {
		super();
		this.requesterID = requesterID;
		this.endPointURL = endPointURL;
		this.filter = filter;
		this.dataContextID = dataContextID;
	}
	
	public String getRequesterID() {
		return requesterID;
	}

	public void setRequesterID(String requesterID) {
		this.requesterID = requesterID;
	}
	
	public String getEndPointURL() {
		return endPointURL;
	}
	
	public void setEndPointURL(String endPointURL) {
		this.endPointURL = endPointURL;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getDataContextID() {
		return dataContextID;
	}

	public void setDataContextID(String dataContextID) {
		this.dataContextID = dataContextID;
	}

	public Map<String, Object> getHeaderMap() {
		return headerMap;
	}	
}