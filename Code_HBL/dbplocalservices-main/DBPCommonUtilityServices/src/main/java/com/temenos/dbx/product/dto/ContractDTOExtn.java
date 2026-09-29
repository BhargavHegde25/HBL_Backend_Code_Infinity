package com.temenos.dbx.product.dto;

import java.util.List;

import org.json.JSONPropertyIgnore;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContractDTOExtn extends ContractDTO implements DBPDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7435421748955984151L;
	private String id;
    private String servicedefinitionId;
    private String serviceType;
    private String name;
    private String description;
    private String statusId;
    private String faxId;
    private String createdby;
    private String createdts;
    private String lastmodifiedts;
    private String rejectedby;
    private String rejectedts;
    private String rejectedReason;
    private List<AddressDTO> address;
    private List<ContractCommunicationDTO> communication;
    private List<ContractCoreCustomersDTO> contractCustomers;
    private String servicedefinitionName;
    private String companyLegalUnit;
    private String channelAccess;
    private String requestedChannelAccess;
    private String submittedFrom;
    private String isConsentProvided;

    public String getServicedefinitionName() {
        return servicedefinitionName;
    }

    public void setServicedefinitionName(String servicedefinitionName) {
        this.servicedefinitionName = servicedefinitionName;
    }

    public String getServicedefinitionId() {
        return servicedefinitionId;
    }

    public void setServicedefinitionId(String servicedefinitionId) {
        this.servicedefinitionId = servicedefinitionId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public List<AddressDTO> getAddress() {
        return address;
    }

    public void setAddress(List<AddressDTO> address) {
        this.address = address;
    }

    public List<ContractCommunicationDTO> getCommunication() {
        return communication;
    }

    public void setCommunication(List<ContractCommunicationDTO> communication) {
        this.communication = communication;
    }

    public List<ContractCoreCustomersDTO> getContractCustomers() {
        return contractCustomers;
    }

    public void setContractCustomers(List<ContractCoreCustomersDTO> contractCustomers) {
        this.contractCustomers = contractCustomers;
    }
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

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

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getFaxId() {
        return faxId;
    }

    public void setFaxId(String faxId) {
        this.faxId = faxId;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
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

    public String getRejectedby() {
        return rejectedby;
    }

    public void setRejectedby(String rejectedby) {
        this.rejectedby = rejectedby;
    }

    public String getRejectedts() {
        return rejectedts;
    }

    public void setRejectedts(String rejectedts) {
        this.rejectedts = rejectedts;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

	public String getChannelAccess() {
		return channelAccess;
	}

	public void setChannelAccess(String channelAccess) {
		this.channelAccess = channelAccess;
	}

	public String getIsConsentProvided() {
		return isConsentProvided;
	}

	public void setIsConsentProvided(String isConsentProvided) {
		this.isConsentProvided = isConsentProvided;
	}

	public String getRequestedChannelAccess() {
		return requestedChannelAccess;
	}

	public void setRequestedChannelAccess(String requestedChannelAccess) {
		this.requestedChannelAccess = requestedChannelAccess;
	}

	public String getSubmittedFrom() {
		return submittedFrom;
	}

	public void setSubmittedFrom(String submittedFrom) {
		this.submittedFrom = submittedFrom;
	}
	
	
    

}
