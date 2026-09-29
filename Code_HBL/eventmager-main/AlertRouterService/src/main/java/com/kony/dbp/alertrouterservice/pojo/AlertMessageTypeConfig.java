package com.kony.dbp.alertrouterservice.pojo;

public class AlertMessageTypeConfig {
	
	String messagetype ;
	String alerttype;
	String alertsubtype;
	boolean passVariableData;
	
	String subject;
	
	public String getMessagetype() {
		return messagetype;
	}
	public void setMessagetype(String messagetype) {
		this.messagetype = messagetype;
	}
	public String getAlerttype() {
		return alerttype;
	}
	public void setAlerttype(String alerttype) {
		this.alerttype = alerttype;
	}
	public String getAlertsubtype() {
		return alertsubtype;
	}
	public void setAlertsubtype(String alertsubtype) {
		this.alertsubtype = alertsubtype;
	}
	public boolean isPassVariableData() {
		return passVariableData;
	}
	public void setPassVariableData(boolean passVariableData) {
		this.passVariableData = passVariableData;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	@Override
	public String toString() {
		return "AlertMessageTypeConfig [messagetype=" + messagetype + ", alerttype=" + alerttype + ", alertsubtype="
				+ alertsubtype + ", passVariableData=" + passVariableData + ", subject=" + subject + "]";
	}
	
}
