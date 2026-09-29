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
import com.temenos.dbx.product.transactionservices.resource.impl.BulkWireTransactionsResourceImpl;
import com.kony.dbputilities.util.HelperMethods;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BulkWireFileTransactionsDetailDTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 998379566900551989L;

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	/**
	 * These are fields in bulkWireFileTransactdetails Table
	 * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
	 */	
	private int bulkWireTransactionID;
	private String bulkWireFileID;
	private String initiatedBy;
	private String createdts;
	private String lastmodifiedts; 
	private String transactionDate;
	private int totalCountOfTransactions;
	private int totalCountOfDomesticTransactions;
	private int totalCountOfInternationalTransactions;
	private String createdby;
	private String modifiedby;
	private String synctimestamp;
	
	private String firstname;
	private String lastname;

	public BulkWireFileTransactionsDetailDTO(){
		super();
	}

	public BulkWireFileTransactionsDetailDTO(String bulkWireFileID, int bulkWireTransactionID, String initiatedBy, int totalCountOfTransactions,
			int totalCountOfDomesticTransactions, int totalCountOfInternationalTransactions, String createdby, String modifiedby,
			String createdts, String lastmodifiedts, String transactionDate, String synctimestamp, String firstname, String lastname) {
		super();
		this.bulkWireFileID = bulkWireFileID;
		this.bulkWireTransactionID = bulkWireTransactionID;
		this.initiatedBy = initiatedBy;
		this.totalCountOfTransactions = totalCountOfTransactions;
		this.totalCountOfDomesticTransactions = totalCountOfDomesticTransactions;
		this.totalCountOfInternationalTransactions = totalCountOfInternationalTransactions;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.transactionDate = transactionDate;
		this.synctimestamp = synctimestamp;
		this.firstname = firstname;
		this.lastname = lastname;
	}


	public int getBulkWireTransactionID() {
		return bulkWireTransactionID;
	}

	public void setBulkWireTransactionID(int bulkWireTransactionID) {
		this.bulkWireTransactionID = bulkWireTransactionID;
	}

	public String getBulkWireFileID() {
		return bulkWireFileID;
	}

	public void setBulkWireFileID(String bulkWireFileID) {
		this.bulkWireFileID = bulkWireFileID;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
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

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		try {
			if (StringUtils.isNotBlank(transactionDate))
				this.transactionDate = HelperMethods.convertDateFormat(transactionDate, Constants.TIMESTAMP_FORMAT);
			else {
				alert.prepareError("createdts : Is Empty").log();
				this.transactionDate = transactionDate;
			}
		} catch (Exception e) {
			alert.prepareError("Error while converting timeStamp to required format : " + e).log();
			this.transactionDate = transactionDate;
		}
	}

	public int getTotalCountOfTransactions() {
		return totalCountOfTransactions;
	}

	public void setTotalCountOfTransactions(int totalCountOfTransactions) {
		this.totalCountOfTransactions = totalCountOfTransactions;
	}

	public int getTotalCountOfDomesticTransactions() {
		return totalCountOfDomesticTransactions;
	}

	public void setTotalCountOfDomesticTransactions(int totalCountOfDomesticTransactions) {
		this.totalCountOfDomesticTransactions = totalCountOfDomesticTransactions;
	}

	public int getTotalCountOfInternationalTransactions() {
		return totalCountOfInternationalTransactions;
	}

	public void setTotalCountOfInternationalTransactions(int totalCountOfInternationalTransactions) {
		this.totalCountOfInternationalTransactions = totalCountOfInternationalTransactions;
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

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		try {
			if (StringUtils.isNotBlank(synctimestamp))
				this.synctimestamp = HelperMethods.convertDateFormat(synctimestamp, Constants.TIMESTAMP_FORMAT);
			else {
				alert.prepareError("synctimestamp : Is Empty").log();
				this.synctimestamp = synctimestamp;
			}
		} catch (Exception e) {
			alert.prepareError("Error while converting timeStamp to required format : " + e).log();
			this.synctimestamp = synctimestamp;
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
		result = prime * result + ((bulkWireFileID == null) ? 0 : bulkWireFileID.hashCode());
		result = prime * result + bulkWireTransactionID;
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		result = prime * result + ((initiatedBy == null) ? 0 : initiatedBy.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + totalCountOfDomesticTransactions;
		result = prime * result + totalCountOfInternationalTransactions;
		result = prime * result + totalCountOfTransactions;
		result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
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
		BulkWireFileTransactionsDetailDTO other = (BulkWireFileTransactionsDetailDTO) obj;
		if (bulkWireFileID != other.bulkWireFileID)
			return false;
		if (bulkWireTransactionID != other.bulkWireTransactionID)
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
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		if (initiatedBy == null) {
			if (other.initiatedBy != null)
				return false;
		} else if (!initiatedBy.equals(other.initiatedBy))
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
		if (modifiedby == null) {
			if (other.modifiedby != null)
				return false;
		} else if (!modifiedby.equals(other.modifiedby))
			return false;
		if (synctimestamp == null) {
			if (other.synctimestamp != null)
				return false;
		} else if (!synctimestamp.equals(other.synctimestamp))
			return false;
		if (totalCountOfDomesticTransactions != other.totalCountOfDomesticTransactions)
			return false;
		if (totalCountOfInternationalTransactions != other.totalCountOfInternationalTransactions)
			return false;
		if (totalCountOfTransactions != other.totalCountOfTransactions)
			return false;
		if (transactionDate == null) {
			if (other.transactionDate != null)
				return false;
		} else if (!transactionDate.equals(other.transactionDate))
			return false;
		return true;
	}



}
