package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class Employment implements DBPDTO {
    /**
     * 
     */
    private static final long serialVersionUID = 2113926349075183809L;
    private String employmentReference;
    private String type;
    private String country;
    private String jobTitle;
    private String employerName;
    private String startDate;
    private String endDate;
    private String employerSegment;
    private String employerOfficePhoneIdd;
    private String employerOfficePhone;
    private String employerOfficeEmail;
    private int salary;
    private String salaryInCurrency;
    private String salaryFrequecny;
    private String primaryEmployment;
    private JSONObject extensionData;

    public Employment() {
    }

    /**
     * @return the employmentReference
     */
    public String getEmploymentReference() {
        return employmentReference;
    }

    /**
     * @param employmentReference the employmentReference to set
     */
    public void setEmploymentReference(String employmentReference) {
        this.employmentReference = employmentReference;
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * @param jobTitle the jobTitle to set
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * @return the employerName
     */
    public String getEmployerName() {
        return employerName;
    }

    /**
     * @param employerName the employerName to set
     */
    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the employerSegment
     */
    public String getEmployerSegment() {
        return employerSegment;
    }

    /**
     * @param employerSegment the employerSegment to set
     */
    public void setEmployerSegment(String employerSegment) {
        this.employerSegment = employerSegment;
    }

    /**
     * @return the employerOfficePhoneIdd
     */
    public String getEmployerOfficePhoneIdd() {
        return employerOfficePhoneIdd;
    }

    /**
     * @param employerOfficePhoneIdd the employerOfficePhoneIdd to set
     */
    public void setEmployerOfficePhoneIdd(String employerOfficePhoneIdd) {
        this.employerOfficePhoneIdd = employerOfficePhoneIdd;
    }

    /**
     * @return the employerOfficePhone
     */
    public String getEmployerOfficePhone() {
        return employerOfficePhone;
    }

    /**
     * @param employerOfficePhone the employerOfficePhone to set
     */
    public void setEmployerOfficePhone(String employerOfficePhone) {
        this.employerOfficePhone = employerOfficePhone;
    }

    /**
     * @return the employerOfficeEmail
     */
    public String getEmployerOfficeEmail() {
        return employerOfficeEmail;
    }

    /**
     * @param employerOfficeEmail the employerOfficeEmail to set
     */
    public void setEmployerOfficeEmail(String employerOfficeEmail) {
        this.employerOfficeEmail = employerOfficeEmail;
    }

    /**
     * @return the salary
     */
    public int getSalary() {
        return salary;
    }

    /**
     * @param salary the salary to set
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * @return the salaryInCurrency
     */
    public String getSalaryInCurrency() {
        return salaryInCurrency;
    }

    /**
     * @param salaryInCurrency the salaryInCurrency to set
     */
    public void setSalaryInCurrency(String salaryInCurrency) {
        this.salaryInCurrency = salaryInCurrency;
    }

    /**
     * @return the salaryFrequecny
     */
    public String getSalaryFrequecny() {
        return salaryFrequecny;
    }

    /**
     * @param salaryFrequecny the salaryFrequecny to set
     */
    public void setSalaryFrequecny(String salaryFrequecny) {
        this.salaryFrequecny = salaryFrequecny;
    }
    
    /**
     * @return the primaryEmployment
     */
    public String getPrimaryEmployment() {
        return primaryEmployment;
    }

    /**
     * @param primaryEmployment the primaryEmployment to set
     */
    public void setPrimaryEmployment(String primaryEmployment) {
        this.primaryEmployment = primaryEmployment;
    }
    
    /**
	 * @return the extensionData
	 */
	public JSONObject getExtensionData() {
		return extensionData;
	}
	/**
	 * @param extensionData the extensionData to set
	 */
	public void setExtensionData(JSONObject extensionData) {
		this.extensionData = extensionData;
	}

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void loadFromJson(JsonObject jsonObject) {
    	DTOUtils.loadDTOFromJson(this, jsonObject);
    }
    
    public static List<Employment> loadFromJsonArray(JsonArray jsonArray) {

		List<Employment> list = new ArrayList<Employment>();
		for(int i=0; i<jsonArray.size(); i++) {

			JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
			Employment employment = new Employment();
			employment.loadFromJson(jsonObject);
			list.add(employment);
		}

		return list;
	}

    public JsonObject toStringJson() {
        return DTOUtils.getJsonObjectFromObject(this);
    }
}
