package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BulkWireSampleFileDTO implements DBPDTO {


	/**
	 *  BulkWireSampleFileDTO is a serializable class, to serialize the getting and setting.
     */
	private static final long serialVersionUID = 9135484067283424605L;

    /**
     * These are fields in bulkWireFiles Table
     * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
     */
    private String bulkWireSampleFileID;
    private String bulkWireSampleFileName;
    private String bulkWireSampleFileFormatCode;
    private String createdby;
    private String modifiedby;
    private String createdts;
    private String lastmodifiedts;
    private String synctimestamp;
    private String softdeleteflag;
    private String sampleFileContents;
    
	public BulkWireSampleFileDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public BulkWireSampleFileDTO(String bulkWireSampleFileID, String bulkWireSampleFileName,
			String bulkWireSampleFileFormatCode, String createdby, String modifiedby, String createdts,
			String lastmodifiedts, String synctimestamp, String softdeleteflag, String sampleFileContents) {
		super();
		this.bulkWireSampleFileID = bulkWireSampleFileID;
		this.bulkWireSampleFileName = bulkWireSampleFileName;
		this.bulkWireSampleFileFormatCode = bulkWireSampleFileFormatCode;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
		this.sampleFileContents = sampleFileContents;
	}
	
	
	public String getBulkWireSampleFileID() {
		return bulkWireSampleFileID;
	}
	public void setBulkWireSampleFileID(String bulkWireSampleFileID) {
		this.bulkWireSampleFileID = bulkWireSampleFileID;
	}
	public String getBulkWireSampleFileName() {
		return bulkWireSampleFileName;
	}
	public void setBulkWireSampleFileName(String bulkWireSampleFileName) {
		this.bulkWireSampleFileName = bulkWireSampleFileName;
	}
	public String getBulkWireSampleFileFormatCode() {
		return bulkWireSampleFileFormatCode;
	}
	public void setBulkWireSampleFileFormatCode(String bulkWireSampleFileFormatId) {
		this.bulkWireSampleFileFormatCode = bulkWireSampleFileFormatId;
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
	public String getSampleFileContents() {
		return sampleFileContents;
	}
	public void setSampleFileContents(String sampleFileContents) {
		this.sampleFileContents = sampleFileContents;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bulkWireSampleFileFormatCode == null) ? 0 : bulkWireSampleFileFormatCode.hashCode());
		result = prime * result + ((bulkWireSampleFileID == null) ? 0 : bulkWireSampleFileID.hashCode());
		result = prime * result + ((bulkWireSampleFileName == null) ? 0 : bulkWireSampleFileName.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((sampleFileContents == null) ? 0 : sampleFileContents.hashCode());
		result = prime * result + ((softdeleteflag == null) ? 0 : softdeleteflag.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
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
		BulkWireSampleFileDTO other = (BulkWireSampleFileDTO) obj;
		if (bulkWireSampleFileFormatCode == null) {
			if (other.bulkWireSampleFileFormatCode != null)
				return false;
		} else if (!bulkWireSampleFileFormatCode.equals(other.bulkWireSampleFileFormatCode))
			return false;
		if (bulkWireSampleFileID == null) {
			if (other.bulkWireSampleFileID != null)
				return false;
		} else if (!bulkWireSampleFileID.equals(other.bulkWireSampleFileID))
			return false;
		if (bulkWireSampleFileName == null) {
			if (other.bulkWireSampleFileName != null)
				return false;
		} else if (!bulkWireSampleFileName.equals(other.bulkWireSampleFileName))
			return false;
		if (createdby == null) {
			if (other.createdby != null)
				return false;
		} else if (!createdby.equals(other.createdby))
			return false;
		if (createdts == null) {
			if (other.createdts != null)
				return false;
		} else if (!createdts.equals(other.createdts))
			return false;
		if (lastmodifiedts == null) {
			if (other.lastmodifiedts != null)
				return false;
		} else if (!lastmodifiedts.equals(other.lastmodifiedts))
			return false;
		if (modifiedby == null) {
			if (other.modifiedby != null)
				return false;
		} else if (!modifiedby.equals(other.modifiedby))
			return false;
		if (sampleFileContents == null) {
			if (other.sampleFileContents != null)
				return false;
		} else if (!sampleFileContents.equals(other.sampleFileContents))
			return false;
		if (softdeleteflag == null) {
			if (other.softdeleteflag != null)
				return false;
		} else if (!softdeleteflag.equals(other.softdeleteflag))
			return false;
		if (synctimestamp == null) {
			if (other.synctimestamp != null)
				return false;
		} else if (!synctimestamp.equals(other.synctimestamp))
			return false;
		return true;
	}
    
}
