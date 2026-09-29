package com.bct.custom.dto;

import java.util.Objects;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentAggregatorDTO implements DBPDTO
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	@JsonProperty("isActive")
	private Boolean isActive;
	private String id;
	private String createdby;
	private String modifiedby;
	public PaymentAggregatorDTO() {
		super();
	}
	public PaymentAggregatorDTO(String name, Boolean isActive, String id, String createdby, String modifiedby) {
		super();
		this.name = name;
		this.isActive = isActive;
		this.id = id;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean isActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public String getModifiedby() {
		return modifiedby;
	}
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "PaymentAggregatorDTO [name=" + name + ", isActive=" + isActive + ", id=" + id + ", createdby="
				+ createdby + ", modifiedby=" + modifiedby + "]";
	}
	
	
	
	
}
