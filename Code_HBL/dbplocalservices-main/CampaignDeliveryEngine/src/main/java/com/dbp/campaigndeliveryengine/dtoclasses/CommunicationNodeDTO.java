package com.dbp.campaigndeliveryengine.dtoclasses;

public class CommunicationNodeDTO {
	private String mobile;
	private String email;

	public CommunicationNodeDTO(String mobile, String email) {
		this.mobile = mobile;
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		if (mobile != null)
			mobile = mobile.replace("-", "");
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
