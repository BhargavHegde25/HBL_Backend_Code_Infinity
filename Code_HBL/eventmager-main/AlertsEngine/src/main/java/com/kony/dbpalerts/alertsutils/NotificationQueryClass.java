package com.kony.dbpalerts.alertsutils;

import java.util.Map;

public class NotificationQueryClass {
	private Map<String, Object> notifyquery;
	private Map<String, Object> usernotifyquery;
	private Map<String, Object> alerthistoryquery;

	public NotificationQueryClass(Map<String, Object> notifyquery, Map<String, Object> usernotifyquery) {
		this.notifyquery = notifyquery;
		this.usernotifyquery = usernotifyquery;
	}

	public void setAlertHistoryQuery(Map<String, Object> query) {
		this.alerthistoryquery = query;
	}

	public Map<String, Object> getNotifyQuery() {
		return this.notifyquery;
	}

	public Map<String, Object> getUserNotifyquery() {
		return this.usernotifyquery;
	}

	public Map<String, Object> getAlertHistoryQuery() {
		return this.alerthistoryquery;
	}
}
