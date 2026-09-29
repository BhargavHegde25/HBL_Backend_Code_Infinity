package com.kony.adminconsole.dto.reports;


public class Report {
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReportDataSourceId() {
		return reportDataSourceId;
	}
	public void setReportDataSourceId(String reportDataSourceId) {
		this.reportDataSourceId = reportDataSourceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	private String id;
    private String reportDataSourceId;
    private String name;
    private String description;
    private String createdBy;
}
