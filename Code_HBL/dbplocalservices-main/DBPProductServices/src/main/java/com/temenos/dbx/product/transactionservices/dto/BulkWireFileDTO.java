package com.temenos.dbx.product.transactionservices.dto;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.constants.Constants;
import com.temenos.dbx.product.transactionservices.resource.impl.BulkWireFileResourceImpl;
import com.kony.dbputilities.util.HelperMethods;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BulkWireFileDTO implements DBPDTO {

	/** BulkWireTransactionsDTO is a serializable class, to serialize the getting and setting.
     */
	private static final long serialVersionUID = -9049554083121583392L;


	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");


    /**
     * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
     */
    
    /**
     * These are fields in bulkWireFiles Table
     */
    private String bulkWireFileID;
	private String bulkWireFileName;
    private int noOfTransactions;
    private int noOfDomesticTransactions;
    private int noOfInternationalTransactions;
    private String createdts;
    private String lastmodifiedts;
    private String bulkWireFileContents;
    private String lastExecutedOn;
    private int fileFormatId;
    private String companyId;
    private String createdBy;
    

	/**
     * These are fields in Customer Table
     */
    private String firstname;
    private String lastname;
    

    public BulkWireFileDTO() {
        super();
    }

    public BulkWireFileDTO(String bulkWireFileID, String bulkWireFileName, int noOfTransactions,
			int noOfDomesticTransactions, int noOfInternationalTransactions,
			String createdts, String lastmodifiedts, String bulkWireFileContents, String lastExecutedOn, String firstname,
			String lastname,int fileFormatId,String companyId, String createdBy) {
		super();
		this.bulkWireFileID = bulkWireFileID;
		this.bulkWireFileName = bulkWireFileName;
		this.noOfTransactions = noOfTransactions;
		this.noOfDomesticTransactions = noOfDomesticTransactions;
		this.noOfInternationalTransactions = noOfInternationalTransactions;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.bulkWireFileContents = bulkWireFileContents;
		this.lastExecutedOn = lastExecutedOn;
		this.firstname = firstname;
		this.lastname = lastname;
		this.fileFormatId = fileFormatId;
		this.companyId = companyId;
		this.createdBy = createdBy;
	}
    
    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
    public String getBulkWireFileID() {
        return bulkWireFileID;
    }

    public void setBulkWireFileID(String bulkWireFileID) {
        this.bulkWireFileID = bulkWireFileID;
    }
    
     public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

    public String getBulkWireFileName() {
        return bulkWireFileName;
    }

    public void setBulkWireFileName(String bulkWireFileName) {
        this.bulkWireFileName = bulkWireFileName;
    }

    public int getFileFormatId() {
		return fileFormatId;
	}

	public void setFileFormatId(int fileFormatId) {
		this.fileFormatId = fileFormatId;
	}

	public int getNoOfTransactions() {
        return noOfTransactions;
    }

    public void setNoOfTransactions(int noOfTransactions) {
        this.noOfTransactions = noOfTransactions;
    }

    public int getNoOfDomesticTransactions() {
        return noOfDomesticTransactions;
    }

    public void setNoOfDomesticTransactions(int noOfDomesticTransactions) {
        this.noOfDomesticTransactions = noOfDomesticTransactions;
    }

    public int getNoOfInternationalTransactions() {
        return noOfInternationalTransactions;
    }

    public void setNoOfInternationalTransactions(int noOfInternationalTransactions) {
        this.noOfInternationalTransactions = noOfInternationalTransactions;
    }

    public String getCreatedts() {
        return createdts;
    }

    public void setCreatedts(String createdts) {
        try {
            if (StringUtils.isNotBlank(createdts))
                this.createdts = HelperMethods.convertDateFormat(createdts, Constants.TIMESTAMP_FORMAT);
            else {
                alert.prepareError("createdts : Is Empty").log();
                this.createdts = createdts;
            }
        } catch (Exception e) {
            alert.prepareError("Error while converting timeStamp to required format : " + e).log();
            this.createdts = createdts;
        }
    }

    public String getLastmodifiedts() {
        return lastmodifiedts;
    }

    public void setLastmodifiedts(String lastmodifiedts) {
        try {
            if (StringUtils.isNotBlank(lastmodifiedts))
                this.lastmodifiedts = HelperMethods.convertDateFormat(lastmodifiedts, Constants.TIMESTAMP_FORMAT);
            else {
                alert.prepareError("lastmodifiedts : Is Empty").log();
                this.lastmodifiedts = lastmodifiedts;
            }
        } catch (Exception e) {
            alert.prepareError("Error while converting timeStamp to required format : " + e).log();
            this.lastmodifiedts = lastmodifiedts;
        }
    }

    public String getBulkWireFileContents() {
		return bulkWireFileContents;
	}

	public void setBulkWireFileContents(String bulkWireFileContents) {
		this.bulkWireFileContents = bulkWireFileContents;
	}

	public String getLastExecutedOn() {
		return lastExecutedOn;
	}

	public void setLastExecutedOn(String lastExecutedOn) {
		 try {
	            if (StringUtils.isNotBlank(lastExecutedOn))
	                this.lastExecutedOn = HelperMethods.convertDateFormat(lastExecutedOn, Constants.TIMESTAMP_FORMAT);
	            else {
	                alert.prepareError("lastExecutedOn : Is Empty").log();
	                this.lastExecutedOn = lastExecutedOn;
	            }
	        } catch (Exception e) {
	            alert.prepareError("Error while converting timeStamp to required format : " + e).log();
	            this.lastExecutedOn = lastExecutedOn;
	        }
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bulkWireFileContents == null) ? 0 : bulkWireFileContents.hashCode());
		result = prime * result + ((bulkWireFileID == null) ? 0 : bulkWireFileID.hashCode());
		result = prime * result + ((bulkWireFileName == null) ? 0 : bulkWireFileName.hashCode());
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + fileFormatId;
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastExecutedOn == null) ? 0 : lastExecutedOn.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + noOfDomesticTransactions;
		result = prime * result + noOfInternationalTransactions;
		result = prime * result + noOfTransactions;
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
		BulkWireFileDTO other = (BulkWireFileDTO) obj;
		if (bulkWireFileContents == null) {
			if (other.bulkWireFileContents != null)
				return false;
		} else if (!bulkWireFileContents.equals(other.bulkWireFileContents))
			return false;
		if (bulkWireFileID == null) {
			if (other.bulkWireFileID != null)
				return false;
		} else if (!bulkWireFileID.equals(other.bulkWireFileID))
			return false;
		if (bulkWireFileName == null) {
			if (other.bulkWireFileName != null)
				return false;
		} else if (!bulkWireFileName.equals(other.bulkWireFileName))
			return false;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdts == null) {
			if (other.createdts != null)
				return false;
		} else if (!createdts.equals(other.createdts))
			return false;
		if (fileFormatId != other.fileFormatId)
			return false;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (lastExecutedOn == null) {
			if (other.lastExecutedOn != null)
				return false;
		} else if (!lastExecutedOn.equals(other.lastExecutedOn))
			return false;
		if (lastmodifiedts == null) {
			if (other.lastmodifiedts != null)
				return false;
		} else if (!lastmodifiedts.equals(other.lastmodifiedts))
			return false;
		if (lastname == null) {
			if (other.lastname != null)
				return false;
		} else if (!lastname.equals(other.lastname))
			return false;
		if (noOfDomesticTransactions != other.noOfDomesticTransactions)
			return false;
		if (noOfInternationalTransactions != other.noOfInternationalTransactions)
			return false;
		if (noOfTransactions != other.noOfTransactions)
			return false;
		return true;
	}
 
}