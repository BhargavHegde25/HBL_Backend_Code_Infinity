package com.temenos.infinity.api.wealthservices.vo;

import java.io.Serializable;

/**
 * TokenVO is a POJO object and it holds key data of the JWT Token
 * 
 * @author Rajesh Kappera
 */
public class TokenVO implements Serializable {
	private static final long serialVersionUID = 7407905493131213863L;
	
	private String userId;
	private String userName;
	private String hostURL;
	private String appName;
	private String roleId; // INFINITY.WEALTH
	private Long validityInMinutes;
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @return the hostURL
	 */
	public String getHostURL() {
		return hostURL;
	}
	/**
	 * @return the appName
	 */
	public String getAppName() {
		return appName;
	}
	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}
	/**
	 * @return the validityInMinutes
	 */
	public Long getValidityInMinutes() {
		return validityInMinutes;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @param hostURL the hostURL to set
	 */
	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
	}
	/**
	 * @param appName the appName to set
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	/**
	 * @param validityInMinutes the validityInMinutes to set
	 */
	public void setValidityInMinutes(Long validityInMinutes) {
		this.validityInMinutes = validityInMinutes;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TokenVO [userId=");
		builder.append(userId);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", hostURL=");
		builder.append(hostURL);
		builder.append(", appName=");
		builder.append(appName);
		builder.append(", roleId=");
		builder.append(roleId);
		builder.append(", validityInMinutes=");
		builder.append(validityInMinutes);
		builder.append("]");
		return builder.toString();
	}
}
