package com.kony.adminconsole.service.termandcondition.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TermAndConditionTextDTO implements DBPDTO{

	private static final long serialVersionUID = 6472269167837613801L;

	private String id;
	private String TermAndConditionId;
	private String LanguageCode;
	private String Version_Id;
	private String Description;	
	private String Content;
	private String ContentType_id;	
	private String ContentModifiedBy;
	private String ContentModifiedOn;	
	private String Status_id;	
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String softDeleteFlag;

	public TermAndConditionTextDTO() {
		super();
	}

	public TermAndConditionTextDTO(String id, String TermAndConditionId, String LanguageCode, String Version_Id, String Description, 
			String Content, String ContentType_id, String ContentModifiedBy, String ContentModifiedOn, String Status_id, String createdby,
			String modifiedby, String createdts, String lastmodifiedts, String synctimestamp, String softDeleteFlag) {
		super();

		this.id = id;
		this.TermAndConditionId = TermAndConditionId;
		this.LanguageCode = LanguageCode;
		this.Version_Id = Version_Id;
		this.Description = Description;	
		this.Content = Content;
		this.ContentType_id = ContentType_id;	
		this.ContentModifiedBy = ContentModifiedBy;
		this.ContentModifiedOn = ContentModifiedOn;	
		this.Status_id = Status_id;	
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softDeleteFlag = softDeleteFlag;

	}
	public String getLanguageCode() {
		return LanguageCode;
	}

	public void setLanguageCode(String languageCode) {
		LanguageCode = languageCode;
	}

	public String getVersion_Id() {
		return Version_Id;
	}

	public void setVersion_Id(String version_Id) {
		Version_Id = version_Id;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getContentType_id() {
		return ContentType_id;
	}

	public void setContentType_id(String contentType_id) {
		ContentType_id = contentType_id;
	}

	public String getContentModifiedBy() {
		return ContentModifiedBy;
	}

	public void setContentModifiedBy(String contentModifiedBy) {
		ContentModifiedBy = contentModifiedBy;
	}

	public String getContentModifiedOn() {
		return ContentModifiedOn;
	}

	public void setContentModifiedOn(String contentModifiedOn) {
		ContentModifiedOn = contentModifiedOn;
	}

	public String getStatus_id() {
		return Status_id;
	}

	public void setStatus_id(String status_id) {
		Status_id = status_id;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTermAndConditionId() {
		return TermAndConditionId;
	}

	public void setTermAndConditionId(String termAndConditionId) {
		TermAndConditionId = termAndConditionId;
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

	public String getCreatedBy() {
		return createdby;
	}

	public void setCreatedBy(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedBy() {
		return modifiedby;
	}

	public void setModifiedBy(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getCreatedts() {
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getModifiedts() {
		return lastmodifiedts;
	}

	public void setModifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}

	public String getSoftDeleteFlag() {
		return softDeleteFlag;
	}

	public void setSoftDeleteFlag(String softDeleteFlag) {
		this.softDeleteFlag = softDeleteFlag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((TermAndConditionId == null) ? 0 : TermAndConditionId.hashCode());
		result = prime * result + ((LanguageCode == null) ? 0 : LanguageCode.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((Version_Id == null) ? 0 : Version_Id.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((softDeleteFlag == null) ? 0 : softDeleteFlag.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((Description == null) ? 0 : Description.hashCode());
		result = prime * result + ((Content == null) ? 0 : Content.hashCode());
		result = prime * result + ((ContentType_id == null) ? 0 : ContentType_id.hashCode());
		result = prime * result + ((ContentModifiedBy == null) ? 0 : ContentModifiedBy.hashCode());
		result = prime * result + ((ContentModifiedOn == null) ? 0 : ContentModifiedOn.hashCode());
		result = prime * result + ((Status_id == null) ? 0 : Status_id.hashCode());		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TermAndConditionTextDTO other = (TermAndConditionTextDTO) obj;
		if (LanguageCode == null) {
			if (other.LanguageCode != null)
				return false;
		} else if (!LanguageCode.equals(other.LanguageCode))
			return false;
		if (TermAndConditionId == null) {
			if (other.TermAndConditionId != null)
				return false;
		} else if (!TermAndConditionId.equals(other.TermAndConditionId))
			return false;
		if (ContentType_id == null) {
			if (other.ContentType_id != null)
				return false;
		} else if (!ContentType_id.equals(other.ContentType_id))
			return false;
		return true;
	}

	



}
