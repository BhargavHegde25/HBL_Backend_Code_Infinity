package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author KH2638
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHFileFormatTypeDTO implements DBPDTO{

	private static final long serialVersionUID = 5864449735323075384L;
	
	private int id;
	private String fileType;
	private String fileextension;
	private String mimetype;
	
	public ACHFileFormatTypeDTO() {
		super();
	}

	public ACHFileFormatTypeDTO(int id, String fileType, String fileextension, String mimetype) {
		super();
		this.id = id;
		this.fileType = fileType;
		this.fileextension = fileextension;
		this.mimetype = mimetype;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileextension() {
		return fileextension;
	}

	public void setFileextension(String fileextension) {
		this.fileextension = fileextension;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
		result = prime * result + ((fileextension == null) ? 0 : fileextension.hashCode());
		result = prime * result + id;
		result = prime * result + ((mimetype == null) ? 0 : mimetype.hashCode());
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
		ACHFileFormatTypeDTO other = (ACHFileFormatTypeDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}