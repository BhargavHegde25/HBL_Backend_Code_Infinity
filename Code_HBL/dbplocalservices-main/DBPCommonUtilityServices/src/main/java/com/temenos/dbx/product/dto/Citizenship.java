package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Citizenship implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1354487193184268367L;
	private String countryOfCitizenship;
	private String endDate;

	public Citizenship() {
	}

	/**
	 * @return the countryOfCitizenship
	 */
	public String getCountryOfCitizenship() {
		return countryOfCitizenship;
	}

	/**
	 * @param countryOfCitizenship the countryOfCitizenship to set
	 */
	public void setCountryOfCitizenship(String countryOfCitizenship) {
		this.countryOfCitizenship = countryOfCitizenship;
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

}
