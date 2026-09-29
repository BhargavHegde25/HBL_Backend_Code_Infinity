package com.kony.adminconsole.service.customermanagement.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType (XmlAccessType.FIELD)
public class CDPReport {

	@XmlElement(name="storage-point")
	private String storagePoint ;
	
	@XmlElement(name="company")
	private String company;
	
	@XmlElement(name="entity-name")
	private String entityName;
	
	@XmlElement(name="field-name")
	private String fieldName ;
	
	@XmlElement(name="field-content")
	private String fieldContent;
	
	public CDPReport(String storagePoint, String company, String entityName, String fieldName,
			String fieldContent) {
		super();
		this.storagePoint = storagePoint;
		this.company = company;
		this.entityName = entityName;
		this.fieldName = fieldName;
		this.fieldContent = fieldContent;
	}
	
	public CDPReport() {
		super();
	}

	public String getStoragePoint() {
		return storagePoint;
	}

	public void setStoragePoint(String storagePoint) {
		this.storagePoint = storagePoint;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldContent() {
		return fieldContent;
	}

	public void setFieldContent(String fieldContent) {
		this.fieldContent = fieldContent;
	}
}
