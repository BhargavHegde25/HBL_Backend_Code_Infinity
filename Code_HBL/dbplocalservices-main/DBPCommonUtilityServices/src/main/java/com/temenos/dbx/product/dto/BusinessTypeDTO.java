package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

/**
 * 
 * @author sowmya.vandanapu
 * @version 1.0
 *
 */
public class BusinessTypeDTO implements DBPDTO {

	private static final long serialVersionUID = -6745866791548488621L;

	private String id;
	private String name;
	private String minAuthSignatory;
	private String maxAuthSignatory;
	private String sigtype;

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

	public String getMinAuthSignatory() {
		return minAuthSignatory;
	}

	public void setMinAuthSignatory(String minAuthSignatory) {
		this.minAuthSignatory = minAuthSignatory;
	}

	public String getMaxAuthSignatory() {
		return maxAuthSignatory;
	}

	public void setMaxAuthSignatory(String maxAuthSignatory) {
		this.maxAuthSignatory = maxAuthSignatory;
	}

	public String getSigtype() {
		return sigtype;
	}

	public void setSigtype(String sigtype) {
		this.sigtype = sigtype;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
