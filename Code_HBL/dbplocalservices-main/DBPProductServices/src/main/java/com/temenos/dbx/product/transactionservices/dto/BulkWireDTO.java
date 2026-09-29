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
import com.kony.dbputilities.util.HelperMethods;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BulkWireDTO implements DBPDTO {

	/**
	 * BulkWireDTO is a serializable class, to serialize the getting and setting.
	 */
	private static final long serialVersionUID = -1974351165510782596L;


	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    /**
     * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
     */
    
    private String bulkWireID;
	private String bulkWireName;
	private String bulkWireTemplateID;
	private String bulkWireTemplateName;
	private String company_id;
    private String noOfTransactions;
    private String noOfDomesticTransactions;
    private String noOfInternationalTransactions;
    private String createdts;
    private String lastmodifiedts; 
    private String lastExecutedOn;
    private String defaultFromAccount;
    private String defaultCurrency;
    private String bulkWireCategory;
    private String firstname;
    private String lastname;
    
    public BulkWireDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public BulkWireDTO(String bulkWireID, String bulkWireName, String bulkWireTemplateID, String bulkWireTemplateName, String company_id, String noOfTransactions, String noOfDomesticTransactions,
			String noOfInternationalTransactions, String createdts, String lastmodifiedts, String lastExecutedOn,
			String defaultFromAccount, String defaultCurrency, String bulkWireCategory, String firstname,
			String lastname) {
		super();
		this.bulkWireID = bulkWireID;
		this.bulkWireName = bulkWireName;
		this.bulkWireTemplateID = bulkWireTemplateID;
		this.bulkWireTemplateName = bulkWireTemplateName;
		this.company_id = company_id;
		this.noOfTransactions = noOfTransactions;
		this.noOfDomesticTransactions = noOfDomesticTransactions;
		this.noOfInternationalTransactions = noOfInternationalTransactions;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.lastExecutedOn = lastExecutedOn;
		this.defaultFromAccount = defaultFromAccount;
		this.defaultCurrency = defaultCurrency;
		this.bulkWireCategory = bulkWireCategory;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	
	public String getBulkWireID() {
		return bulkWireID;
	}

	public void setBulkWireID(String bulkWireID) {
		this.bulkWireID = bulkWireID;
	}

	public String getBulkWireName() {
		return bulkWireName;
	}

	public void setBulkWireName(String bulkWireName) {
		this.bulkWireName = bulkWireName;
	}

	public String getBulkWireTemplateID() {
		return bulkWireTemplateID;
	}

	public void setBulkWireTemplateID(String bulkWireTemplateID) {
		this.bulkWireTemplateID = bulkWireTemplateID;
	}

	public String getBulkWireTemplateName() {
		return bulkWireTemplateName;
	}

	public void setBulkWireTemplateName(String bulkWireTemplateName) {
		this.bulkWireTemplateName = bulkWireTemplateName;
	}

	public String getCompany_id() {
		return company_id;
	}

	public void setCompany_id(String company_id) {
		this.company_id = company_id;
	}

	public String getNoOfTransactions() {
		return noOfTransactions;
	}

	public void setNoOfTransactions(String noOfTransactions) {
		this.noOfTransactions = noOfTransactions;
	}

	public String getNoOfDomesticTransactions() {
		return noOfDomesticTransactions;
	}

	public void setNoOfDomesticTransactions(String noOfDomesticTransactions) {
		this.noOfDomesticTransactions = noOfDomesticTransactions;
	}

	public String getNoOfInternationalTransactions() {
		return noOfInternationalTransactions;
	}

	public void setNoOfInternationalTransactions(String noOfInternationalTransactions) {
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

	public String getDefaultFromAccount() {
		return defaultFromAccount;
	}

	public void setDefaultFromAccount(String defaultFromAccount) {
		this.defaultFromAccount = defaultFromAccount;
	}

	public String getDefaultCurrency() {
		return defaultCurrency;
	}

	public void setDefaultCurrency(String defaultCurrency) {
		this.defaultCurrency = defaultCurrency;
	}

	public String getBulkWireCategory() {
		return bulkWireCategory;
	}

	public void setBulkWireCategory(String bulkWireCategory) {
		this.bulkWireCategory = bulkWireCategory;
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
		result = prime * result + ((bulkWireCategory == null) ? 0 : bulkWireCategory.hashCode());
		result = prime * result + ((bulkWireID == null) ? 0 : bulkWireID.hashCode());
		result = prime * result + ((bulkWireName == null) ? 0 : bulkWireName.hashCode());
		result = prime * result + ((bulkWireTemplateID == null) ? 0 : bulkWireTemplateID.hashCode());
		result = prime * result + ((bulkWireTemplateName == null) ? 0 : bulkWireTemplateName.hashCode());
		result = prime * result + ((company_id == null) ? 0 : company_id.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((defaultCurrency == null) ? 0 : defaultCurrency.hashCode());
		result = prime * result + ((defaultFromAccount == null) ? 0 : defaultFromAccount.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((lastExecutedOn == null) ? 0 : lastExecutedOn.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((noOfDomesticTransactions == null) ? 0 : noOfDomesticTransactions.hashCode());
		result = prime * result
				+ ((noOfInternationalTransactions == null) ? 0 : noOfInternationalTransactions.hashCode());
		result = prime * result + ((noOfTransactions == null) ? 0 : noOfTransactions.hashCode());
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
		BulkWireDTO other = (BulkWireDTO) obj;
		if (bulkWireCategory == null) {
			if (other.bulkWireCategory != null)
				return false;
		} else if (!bulkWireCategory.equals(other.bulkWireCategory))
			return false;
		if (bulkWireID == null) {
			if (other.bulkWireID != null)
				return false;
		} else if (!bulkWireID.equals(other.bulkWireID))
			return false;
		if (bulkWireName == null) {
			if (other.bulkWireName != null)
				return false;
		} else if (!bulkWireName.equals(other.bulkWireName))
			return false;
		
		if (bulkWireTemplateID == null) {
			if (other.bulkWireTemplateID != null)
				return false;
		} else if (!bulkWireTemplateID.equals(other.bulkWireTemplateID))
			return false;
		if (bulkWireTemplateName == null) {
			if (other.bulkWireTemplateName != null)
				return false;
		} else if (!bulkWireTemplateName.equals(other.bulkWireTemplateName))
			return false;
		if (company_id == null) {
			if (other.company_id != null)
				return false;
		} else if (!company_id.equals(other.company_id))
			return false;
		
		if (createdts == null) {
			if (other.createdts != null)
				return false;
		} else if (!createdts.equals(other.createdts))
			return false;
		if (defaultCurrency == null) {
			if (other.defaultCurrency != null)
				return false;
		} else if (!defaultCurrency.equals(other.defaultCurrency))
			return false;
		if (defaultFromAccount == null) {
			if (other.defaultFromAccount != null)
				return false;
		} else if (!defaultFromAccount.equals(other.defaultFromAccount))
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
		if (noOfDomesticTransactions == null) {
			if (other.noOfDomesticTransactions != null)
				return false;
		} else if (!noOfDomesticTransactions.equals(other.noOfDomesticTransactions))
			return false;
		if (noOfInternationalTransactions == null) {
			if (other.noOfInternationalTransactions != null)
				return false;
		} else if (!noOfInternationalTransactions.equals(other.noOfInternationalTransactions))
			return false;
		if (noOfTransactions == null) {
			if (other.noOfTransactions != null)
				return false;
		} else if (!noOfTransactions.equals(other.noOfTransactions))
			return false;
		return true;
	}

}
