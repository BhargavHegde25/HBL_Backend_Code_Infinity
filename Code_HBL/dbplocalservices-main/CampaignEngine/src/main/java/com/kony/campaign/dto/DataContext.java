package com.kony.campaign.dto;

public class DataContext {	
	private String name;
	private String endPointURL;
	private String filter;
	
	public DataContext(String dcName) {
		super();
		this.name = dcName;		
	}
		
	public DataContext(String dcName, String filter) {
		super();
		name = dcName;
		this.filter = filter;		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String dcName) {
		name = dcName;
	}	
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public String getEndPointURL() {
		return endPointURL;
	}		
	
	public void setEndPointURL(String endPointURL) {
		this.endPointURL = endPointURL;
	}	
	
}
