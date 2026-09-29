package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FeatureDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 310246338637800265L;

	private String id;
	private String name;
	private String description;
	private String Type_id;
	private String Status_id;
	private String DisplaySequence;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getType_id() {
		return Type_id;
	}

	public void setType_id(String type_id) {
		this.Type_id = type_id;
	}

	public String getStatus_id() {
		return Status_id;
	}

	public void setStatus_id(String status_id) {
		this.Status_id = status_id;
	}

	public String getDisplaySequence() {
		return DisplaySequence;
	}

	public void setDisplaySequence(String displaySequence) {
		this.DisplaySequence = displaySequence;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
