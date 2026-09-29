package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

/**
 * 
 * @author KH2627
 * @version 1.0 implements {@link DBPDTO}
 * 
 *
 */

public class CustomerImageDTO implements DBPDTO {

	private static final long serialVersionUID = 4865903039190150223L;

	private String Customer_id;
	private String UserImage;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String softdeleteflag;
	private String id;
	private String legalEntityId;


	public String getCustomer_id() {
		return Customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.Customer_id = customer_id;
	}

	public String getUserImage() {
		return UserImage;
	}

	public void setUserImage(String userImage) {
		this.UserImage = userImage;
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

	public String getCreatedts() {
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}

	public String getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(String softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
		}
	
	public String getid() {
		return id;
	}

	public void setid(String id) {
		this.id = id;
	}
	
	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}
}
