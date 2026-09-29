package com.kony.adminconsole.dto.campaign;

public class DataContext {
	
	private String profileConditionId;
	private String conditionExpression;
	private String dataContextId;
	private String endPointURL;	
	
	public DataContext(String profileConditionId) {
		super();
		this.profileConditionId = profileConditionId;
	}
	public DataContext(String profileConditionId,  String dataContextId, String endPointURL, 
			String conditionExpression) {
		super();
		this.profileConditionId = profileConditionId;
		this.conditionExpression = conditionExpression;
		this.dataContextId = dataContextId;
		this.endPointURL = endPointURL;
	}
	public String getProfileConditionId() {
		return profileConditionId;
	}
	public void setProfileConditionId(String profileConditionId) {
		this.profileConditionId = profileConditionId;
	}
	public String getConditionExpression() {
		return conditionExpression;
	}
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	public String getDataContextId() {
		return dataContextId;
	}
	public void setDataContextId(String dataContextId) {
		this.dataContextId = dataContextId;
	}
	public String getEndPointURL() {
		return endPointURL;
	}
	public void setEndPointURL(String endPointURL) {
		this.endPointURL = endPointURL;
	}
		
}
