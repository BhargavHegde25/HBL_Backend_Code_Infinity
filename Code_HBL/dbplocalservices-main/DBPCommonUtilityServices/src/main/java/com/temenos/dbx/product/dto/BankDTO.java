package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class BankDTO implements DBPDTOEXT {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3814929216361242631L;

	private String id;
	private String description;
	private String oauth2;
	private String IdentityProvider;



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}



	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}



	/**
	 * @return the oauth2
	 */
	public String getOauth2() {
		return oauth2;
	}



	/**
	 * @param oauth2 the oauth2 to set
	 */
	public void setOauth2(String oauth2) {
		this.oauth2 = oauth2;
	}



	/**
	 * @return the identityProvider
	 */
	public String getIdentityProvider() {
		return IdentityProvider;
	}



	/**
	 * @param identityProvider the identityProvider to set
	 */
	public void setIdentityProvider(String identityProvider) {
		IdentityProvider = identityProvider;
	}



	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	public BankDTO() {
	}



	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public Object loadDTO(String id) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Object loadDTO() {
		String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.BANK_GET, true, true);

		if (exts != null && exts.size() > 0) {
			return exts.get(0);
		}

		return null;
	}

}
