package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.temenos.dbx.product.utils.DTOConstants;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class PartyDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2463014208669106527L;
	private String partyId;
	private String dateOfBirth;
	private String dateOfDeath;
	private String notificationOfDeath;
	private String cityOfBirth;
	private String gender;
	private String maritalStatus;
	private String countryOfBirth;
	private String partyType;
	private String defaultLanguage;
	private String noOfDependents;
	private String reasonForNoCitizenship;
	private String partyStatus;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String nameStartDate;
	private String nickName;
	private String suffix;
	private String alias;
	private String entityName;
	private String organisationLegalType;
	private String incorporationCountry;
	private String dateOfIncorporation;
	private String nameOfIncorporationAuthority;
	private String numberOfEmployees;
	private String legalForm;
	private List<String> nationalities;
	private List<String> partyLanguages;
	private List<Citizenship> citizenships;
	private List<Employment> employments;
	private List<PartyAddress> addresses;
	private List<ContactReference> contactReferences;
	private List<TaxDetails> taxDetails;
	private List<PartyIdentifier> partyIdentifiers;
	private List<Residence> residences;
	private List<Occupation> occupations;
	private List<AlternateIdentity> alternateIdentities;
	private List<PartyClassification> classification;
	private Map<String, Object> extensionData;

	public PartyDTO() {
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the maritalStatus
	 */
	public String getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the defaultLanguage
	 */
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * @param defaultLanguage the defaultLanguage to set
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * @return the noOfDependents
	 */
	public String getNoOfDependents() {
		return noOfDependents;
	}

	/**
	 * @param noOfDependents the noOfDependents to set
	 */
	public void setNoOfDependents(String noOfDependents) {
		this.noOfDependents = noOfDependents;
	}

	/**
	 * @return the reasonForNoCitizenship
	 */
	public String getReasonForNoCitizenship() {
		return reasonForNoCitizenship;
	}

	/**
	 * @param reasonForNoCitizenship the reasonForNoCitizenship to set
	 */
	public void setReasonForNoCitizenship(String reasonForNoCitizenship) {
		this.reasonForNoCitizenship = reasonForNoCitizenship;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
     * @return the nameStartDate
     */
    public String getNameStartDate() {
        return nameStartDate;
    }

    /**
     * @param nameStartDate the nameStartDate to set
     */
    public void setNameStartDate(String nameStartDate) {
        this.nameStartDate = nameStartDate;
    }

    /**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the organisationLegalType
	 */
	public String getOrganisationLegalType() {
		return organisationLegalType;
	}

	/**
	 * @param organisationLegalType the organisationLegalType to set
	 */
	public void setOrganisationLegalType(String organisationLegalType) {
		this.organisationLegalType = organisationLegalType;
	}

	/**
	 * @return the incorporationCountry
	 */
	public String getIncorporationCountry() {
		return incorporationCountry;
	}

	/**
	 * @param incorporationCountry the incorporationCountry to set
	 */
	public void setIncorporationCountry(String incorporationCountry) {
		this.incorporationCountry = incorporationCountry;
	}

	/**
	 * @return the dateOfIncorporation
	 */
	public String getDateOfIncorporation() {
		return dateOfIncorporation;
	}

	/**
	 * @param dateOfIncorporation the dateOfIncorporation to set
	 */
	public void setDateOfIncorporation(String dateOfIncorporation) {
		this.dateOfIncorporation = dateOfIncorporation;
	}

	/**
	 * @return the nameOfIncorporationAuthority
	 */
	public String getNameOfIncorporationAuthority() {
		return nameOfIncorporationAuthority;
	}

	/**
	 * @param nameOfIncorporationAuthority the nameOfIncorporationAuthority to set
	 */
	public void setNameOfIncorporationAuthority(String nameOfIncorporationAuthority) {
		this.nameOfIncorporationAuthority = nameOfIncorporationAuthority;
	}

	/**
	 * @return the numberOfEmployees
	 */
	public String getNumberOfEmployees() {
		return numberOfEmployees;
	}

	/**
	 * @param numberOfEmployees the numberOfEmployees to set
	 */
	public void setNumberOfEmployees(String numberOfEmployees) {
		this.numberOfEmployees = numberOfEmployees;
	}

	/**
	 * @return the nationalities
	 */
	public List<String> getNationalities() {
		return nationalities;
	}

	/**
	 * @param nationalities the nationalities to set
	 */
	public void setNationalities(List<String> nationalities) {
		this.nationalities = nationalities;
	}

	/**
	 * @return the partyLanguages
	 */
	public List<String> getPartyLanguages() {
		return partyLanguages;
	}

	/**
	 * @param partyLanguages the partyLanguages to set
	 */
	public void setPartyLanguages(List<String> partyLanguages) {
		this.partyLanguages = partyLanguages;
	}

	/**
	 * @return the citizenships
	 */
	public List<Citizenship> getCitizenships() {
		return citizenships;
	}

	/**
	 * @param citizenships the citizenships to set
	 */
	public void setCitizenships(List<Citizenship> citizenships) {
		this.citizenships = citizenships;
	}

	/**
	 * @param employments the employments to set
	 */
	public void setEmployments(List<Employment> employments) {
		this.employments = employments;
	}

	/**
	 * @param PartyAddress the PartyAddress to set
	 */
	public void setPartyAddress(List<PartyAddress> addresses) {
		this.addresses = addresses;
	}

	/**
	 * @param taxDetails the taxDetails to set
	 */
	public void setTaxDetails(List<TaxDetails> taxDetails) {
		this.taxDetails = taxDetails;
	}

	/**
	 * @param partyIdentifier the partyIdentifier to set
	 */
	public void setPartyIdentifier(List<PartyIdentifier> partyIdentifiers) {
		this.partyIdentifiers = partyIdentifiers;
	}

	/**
	 * @return the dateOfBirth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
     * @return the dateOfDeath
     */
    public String getDateOfDeath() {
        return dateOfDeath;
    }

    /**
     * @param dateOfDeath the dateOfDeath to set
     */
    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    /**
	 * @return the notificationOfDeath
	 */
	public String getNotificationOfDeath() {
		return notificationOfDeath;
	}

	/**
	 * @param notificationOfDeath the notificationOfDeath to set
	 */
	public void setNotificationOfDeath(String notificationOfDeath) {
		this.notificationOfDeath = notificationOfDeath;
	}

	/**
	 * @return the cityOfBirth
	 */
	public String getCityOfBirth() {
		return cityOfBirth;
	}

	/**
	 * @param cityOfBirth the cityOfBirth to set
	 */
	public void setCityOfBirth(String cityOfBirth) {
		this.cityOfBirth = cityOfBirth;
	}

	/**
	 * @return the countryOfBirth
	 */
	public String getCountryOfBirth() {
		return countryOfBirth;
	}

	/**
	 * @param countryOfBirth the countryOfBirth to set
	 */
	public void setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}


	/**
	 * @return the partyType
	 */
	public String getPartyType() {
		return partyType;
	}

	/**
	 * @param partyType the partyType to set
	 */
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	/**
	 * @return the partyStatus
	 */
	public String getPartyStatus() {
		return partyStatus;
	}

	/**
	 * @param partyStatus the partyStatus to set
	 */
	public void setPartyStatus(String partyStatus) {
		this.partyStatus = partyStatus;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}

	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	/**
	 * @return the employments
	 */
	public List<Employment> getEmployments() {
		return employments;
	}

	/**
	 * @param employments the employments to set
	 */
	public void setEmployment(Employment employment) {
		if(this.employments == null) {
			this.employments = new ArrayList<Employment>();
		}

		this.employments.add(employment);
	}

	/**
     * @return the legalForm
     */
    public String getLegalForm() {
        return legalForm;
    }

    /**
     * @param legalForm the legalForm to set
     */
    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    /**
	 * @return the PartyAddress
	 */
	public List<PartyAddress> getPartyAddress() {
		return addresses;
	}

	/**
	 * @param PartyAddress the PartyAddress to set
	 */
	public void setPartyAddress(PartyAddress address) {
		if(this.addresses == null){
			this.addresses = new ArrayList<PartyAddress>();
		}

		this.addresses.add(address);
	}

	/**
     * @return the contactReferences
     */
    public List<ContactReference> getContactReferences() {
        return contactReferences;
    }

    /**
     * @param contactReferences the contactReferences to set
     */
    public void setContactReferences(List<ContactReference> contactReferences) {
        this.contactReferences = contactReferences;
    }

    /**
	 * @return the taxDetails
	 */
	public List<TaxDetails> getTaxDetails() {
		return taxDetails;
	}

	/**
	 * @param taxDetails the taxDetails to set
	 */
	public void setTaxDetails(TaxDetails taxDetails) {

		if(this.taxDetails == null){
			this.taxDetails = new ArrayList<TaxDetails>();
		}

		this.taxDetails.add(taxDetails);
	}

	/**
	 * @return the partyIdentifier
	 */
	public List<PartyIdentifier> getPartyIdentifier() {
		return partyIdentifiers;
	}

	/**
	 * @param partyIdentifier the partyIdentifier to set
	 */
	public void setPartyIdentifier(PartyIdentifier partyIdentifier) {
		
		if(this.partyIdentifiers == null){
			this.partyIdentifiers = new ArrayList<PartyIdentifier>();
		}

		this.partyIdentifiers.add(partyIdentifier);
	}
	
	/**
	 * @return the occupations
	 */
	public List<Occupation> getOccupations() {
		return occupations;
	}

	/**
	 * @param occupations the occupations to set
	 */
	public void setOccupations(List<Occupation> occupations) {
		this.occupations = occupations;
	}
	
	/**
	 * @return the residences
	 */
	public List<Residence> getResidences() {
		return residences;
	}

	/**
	 * @param residences the residences to set
	 */
	public void setResidences(List<Residence> residences) {
		this.residences = residences;
	}
	
	
	public JsonObject toStringJson() {
		JsonObject jsonObject = DTOUtils.getJsonObjectFromObject(this);
		if(employments != null && employments.size() >0) {
			JsonArray jsonArray = new JsonArray();
			for(int i=0; i<employments.size(); i++) {
				jsonArray.add(employments.get(i).toStringJson());
			}

			jsonObject.add(DTOConstants.EMPLOYMENTS, jsonArray);
		}

		if(addresses != null && addresses.size() >0) {
			JsonArray jsonArray = new JsonArray();
			for(int i=0; i<addresses.size(); i++) {
				jsonArray.add(addresses.get(i).toStringJson());
			}

			jsonObject.add(DTOConstants.PARTYADDRESS, jsonArray);
		}
		
		if(partyIdentifiers != null && partyIdentifiers.size() >0) {
			JsonArray jsonArray = new JsonArray();
			for(int i=0; i<partyIdentifiers.size(); i++) {
				jsonArray.add(partyIdentifiers.get(i).toStringJson());
			}

			jsonObject.add(DTOConstants.PARTYIDENTIFIERS, jsonArray);
		}
		
		if(taxDetails != null && taxDetails.size() >0) {
			JsonArray jsonArray = new JsonArray();
			for(int i=0; i<taxDetails.size(); i++) {
				jsonArray.add(taxDetails.get(i).toStringJson());
			}

			jsonObject.add(DTOConstants.TAXDETAILS, jsonArray);
		}
		
		if(alternateIdentities != null && alternateIdentities.size() >0) {
            JsonArray jsonArray = new JsonArray();
            for(int i=0; i<alternateIdentities.size(); i++) {
                jsonArray.add(alternateIdentities.get(i).toStringJson());
            }

            jsonObject.add(DTOConstants.ALTERNATEIDENTITIES, jsonArray);
        }
		
		if(extensionData != null && !extensionData.isEmpty()) {
			Gson gson = new Gson();
			jsonObject.add("extensionData", gson.toJsonTree(extensionData));
		}


		return jsonObject;

	}


	public void loadFromJson(JsonObject jsonObject) {

		DTOUtils.loadDTOFromJson(this, jsonObject);
		
		if (jsonObject.has(DTOConstants.EXTENSION_DATA) && !jsonObject.get(DTOConstants.EXTENSION_DATA).isJsonNull()) {
			this.extensionData = new Gson().fromJson(jsonObject.getAsJsonObject(DTOConstants.EXTENSION_DATA),
					new TypeToken<HashMap<String, Object>>() {
					}.getType());
		}

		if(jsonObject.has(DTOConstants.EMPLOYMENTS) && !jsonObject.get(DTOConstants.EMPLOYMENTS).isJsonNull() && jsonObject.get(DTOConstants.EMPLOYMENTS).getAsJsonArray().size() >0) {
			JsonArray employments = jsonObject.get(DTOConstants.EMPLOYMENTS).getAsJsonArray();
			this.employments = Employment.loadFromJsonArray(employments);

		}

		if(jsonObject.has(DTOConstants.PARTYADDRESS) && !jsonObject.get(DTOConstants.PARTYADDRESS).isJsonNull() && jsonObject.get(DTOConstants.PARTYADDRESS).getAsJsonArray().size() >0) {
			JsonArray PartyAddresss = jsonObject.get(DTOConstants.PARTYADDRESS).getAsJsonArray();
			this.addresses = PartyAddress.loadFromJsonArray(PartyAddresss);

		}
		
		
		if(jsonObject.has(DTOConstants.PARTYIDENTIFIERS) && !jsonObject.get(DTOConstants.PARTYIDENTIFIERS).isJsonNull() && jsonObject.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray().size() >0) {
			JsonArray partyIdentifiers = jsonObject.get(DTOConstants.PARTYIDENTIFIERS).getAsJsonArray();
			this.partyIdentifiers = PartyIdentifier.loadFromJsonArray(partyIdentifiers);
		}
		
		if(jsonObject.has(DTOConstants.TAXDETAILS) && !jsonObject.get(DTOConstants.TAXDETAILS).isJsonNull() && jsonObject.get(DTOConstants.TAXDETAILS).getAsJsonArray().size() >0) {
			JsonArray taxDetails = jsonObject.get(DTOConstants.TAXDETAILS).getAsJsonArray();
			this.taxDetails = TaxDetails.loadFromJsonArray(taxDetails);
		}
		
		if(jsonObject.has(DTOConstants.ALTERNATEIDENTITIES) && !jsonObject.get(DTOConstants.ALTERNATEIDENTITIES).isJsonNull() && jsonObject.get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray().size() >0) {
            JsonArray alternateIdentities = jsonObject.get(DTOConstants.ALTERNATEIDENTITIES).getAsJsonArray();
            this.alternateIdentities = AlternateIdentity.loadFromJsonArray(alternateIdentities);
        }
		
	}

	public static void main(String[] args) {
		String party = "{\"partyId\":\"2010035502\",\"dateOfBirth\":\"2020-03-16\",\"dateOfDeath\":\"2020-03-16\",\"notificationOfDeath\":\"2020-03-16\",\"cityOfBirth\":\"Atlanta\",\"countryOfBirth\":\"US\",\"gender\":\"Male\",\"maritalStatus\":\"Unmarried\",\"nationalitys\":[],\"noOfDependents\":1,\"reasonForNoCitizenship\":\"Test\",\"partyType\":\"Individual\",\"partyStatus\":\"Active\",\"extensionData\":{},\"partyLanguages\":[],\"citizenships\":[],\"partyNamess\":[{\"partyId\":\"2010035502\",\"title\":\"Mr\",\"firstName\":\"John\",\"middleName\":\"Rolf\",\"lastName\":\"Gerling\",\"nickName\":\"Rolf\",\"suffix\":\"B.E.\",\"alias\":\"Temenos\",\"entityName\":\"Temenos Group\",\"startDate\":\"2012-05-24\",\"endDate\":\"2020-05-24\",\"extensionData\":{\"key\":\"value\"}}],\"partyAssessments\":[],\"partyIdentifiers\":[],\"personPositions\":[],\"partyLifeCycles\":[],\"occupations\":[],\"employments\":[{\"partyId\":\"2010035502\",\"type\":\"Self-Employed\",\"country\":\"US\",\"jobTitle\":\"Product Manager\",\"employerName\":\"ALfred\",\"startDate\":\"2010-03-26\",\"endDate\":\"2020-03-26\",\"employerSegment\":\"\",\"employerOfficePhoneIdd\":\"91\",\"employerOfficePhone\":\"8574963214\",\"employerOfficeEmail\":\"alfred@temenos.com\",\"salary\":10000,\"salaryInCurrency\":\"Dollar\",\"salaryFrequency\":\"Monthly\",\"extensionData\":{}}],\"otherRiskIndicators\":[],\"residences\":[],\"vulnerabilitys\":[],\"taxDetailss\":[],\"contactPoints\":[{\"partyId\":\"2010035502\",\"contactPointType\":\"Electronic Address\",\"startDate\":\"2000-03-26\",\"endDate\":\"2020-03-26\",\"electronicAddress\":{\"electronicAddressType\":\"Email\",\"electronicAddress\":\"mail@abc.co.uk\",\"electronicCommunicationType\":\"Primary\"},\"phoneAddress\":{},\"extensionData\":{}},{\"partyId\":\"2010035502\",\"contactPointType\":\"Phone Address\",\"startDate\":\"2020-03-26\",\"endDate\":\"2020-03-26\",\"electronicAddress\":{},\"phoneAddress\":{\"phoneAddressType\":\"Home Number\",\"internationalPhoneNumber\":\"+14155552671\",\"iddPrefixPhone\":\"0010\",\"nationalPhoneNumber\":\"4155552671\"},\"extensionData\":{}}],\"PartyAddresss\":[{\"partyId\":\"2010035502\",\"physicalAddressType\":\"Office\",\"startDate\":\"2020-03-26\",\"reliabilityType\":\"Confirmed\",\"contactName\":\"Rolf\",\"endDate\":\"2020-03-26\",\"countryCode\":\"US\",\"flatNumber\":\"151 or 7/3\",\"floor\":\"2\",\"buildingNumber\":\"3\",\"buildingName\":\"c/o Copenagen Fintech Labs\",\"streetName\":\"Grand Avenue\",\"town\":\"San leandro community center\",\"countrySubdivision\":\"Oakland\",\"postalOrZipCode\":\"510-286-4444\",\"validatedBy\":\"\",\"postBoxNumber\":\"A3700\",\"usePurpose\":\"Home Address\",\"regionCode\":\"\",\"district\":\"\",\"department\":\"\",\"subDepartment\":\"\",\"landmark\":\"\",\"extensionData\":{}}]}";

		//        String party1 = "{\"partyId\":\"2010035502\",\"dateOfBirth\":\"2020-03-16\",\"notificationOfDeath\":\"2020-03-16\",\"cityOfBirth\":\"Atlanta\",\"countryOfBirth\":\"US\",\"partyType\":\"Individual\",\"partyStatus\":\"Active\",\"partyNames\":[{\"startDate\":\"2012-05-24\",\"title\":\"Mr\",\"firstName\":\"John\",\"middleName\":\"Rolf\",\"lastName\":\"Gerling\",\"suffix\":\"B.E.\",\"alias\":\"Temenos\",\"entityName\":\"Temenos Group\",\"extensionData\":{\"key\":\"value\"}}],\"employments\":[{\"type\":\"Self-Employed\",\"country\":\"US\",\"jobTitle\":\"Product Manager\",\"employerName\":\"ALfred\",\"startDate\":\"2010-03-26\",\"employerSegment\":\"\",\"employerOfficePhoneIdd\":\"91\",\"employerOfficePhone\":\"8574963214\",\"employerOfficeEmail\":\"alfred@temenos.com\",\"salary\":0,\"salaryInCurrency\":\"Dollar\"}],\"contactPoints\":[{\"contactPointType\":\"Electronic Address\",\"startDate\":\"2000-03-26\",\"electronicAddress\":{\"electronicAddressType\":\"Email\",\"electronicAddress\":\"mail@abc.co.uk\",\"electronicCommunicationType\":\"Primary\"},\"phoneAddress\":{}},{\"contactPointType\":\"Phone Address\",\"startDate\":\"2020-03-26\",\"electronicAddress\":{},\"phoneAddress\":{\"phoneAddressType\":\"Home Number\",\"internationalPhoneNumber\":\"+14155552671\",\"iddPrefixPhone\":\"0010\",\"nationalPhoneNumber\":\"4155552671\"}}],\"PartyAddress\":[{\"physicalAddressType\":\"Office\",\"startDate\":\"2020-03-26\",\"reliabilityType\":\"Confirmed\",\"contactName\":\"Rolf\",\"countryCode\":\"US\",\"flatNumber\":\"151 or 7/3\",\"floor\":\"2\",\"buildingNumber\":\"3\",\"buildingName\":\"c/o Copenagen Fintech Labs\",\"streetName\":\"Grand Avenue\",\"town\":\"San leandro community center\",\"countrySubdivision\":\"Oakland\",\"postalOrZipCode\":\"510-286-4444\",\"validatedBy\":\"\",\"postBoxNumber\":\"A3700\",\"usePurpose\":\"Home Address\",\"regionCode\":\"\",\"district\":\"\",\"department\":\"\",\"subDepartment\":\"\",\"landmark\":\"\"}]}";

		PartyDTO partyDTO = new PartyDTO();
		JsonObject  jsonObject = new JsonParser().parse(party).getAsJsonObject();

		partyDTO.loadFromJson(jsonObject);

		try {
			party = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(partyDTO);
			System.out.println(party);
		} catch (JsonProcessingException e) {
			System.out.println("Caught exception while printing Party: "+e);
		}
		//        
		//        jsonObject = new JsonParser().parse(party).getAsJsonObject();

		System.out.println(partyDTO.toStringJson().toString());




	}

    /**
     * @return the alternateIdentities
     */
    public List<AlternateIdentity> getAlternateIdentities() {
        return alternateIdentities;
    }

    /**
     * @param alternateIdentities the alternateIdentities to set
     */
    public void setAlternateIdentities(AlternateIdentity alternateIdentity) {
        
        if(alternateIdentities == null) {
            alternateIdentities = new ArrayList<AlternateIdentity>();
        }
        
        this.alternateIdentities.add(alternateIdentity);
    }

	public List<PartyClassification> getClassification() {
		return classification;
	}

	public void setClassification(List<PartyClassification> classification) {
		if (this.classification == null) {
			this.classification = new ArrayList<PartyClassification>();
		}
		this.classification = classification;
	}

	public Map<String, Object> getExtensionData() {
		return extensionData;
	}

	public void setExtensionData(Map<String, Object> extensionData) {
		this.extensionData = extensionData;
	}
    
    
}
