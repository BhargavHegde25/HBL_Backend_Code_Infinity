package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author KH2301
 * @version 1.0
 * implements {@link DBPDTO}
 * 
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class BulkWireFileFormatTypeDTO implements DBPDTO{

	/** BulkWireTransactionsDTO is a serializable class, to serialize the getting and setting.
	 */
	private static final long serialVersionUID = 6520933483895049319L;

	/**
	 * These are fields in bulkWireFileFormatType Table
	 * Generate  Constructor from Superclass , Constructor using Fields ,Getters and Setters for the following fields
	 */
	private String bulkWiresFileFormatTypeCode;
	private String bulkWiresFileFormatTypeName;

	public String getBulkWiresFileFormatTypeCode() {
		return bulkWiresFileFormatTypeCode;
	}
	public void setBulkWiresFileFormatTypeCode(String bulkWiresFileFormatTypeCode) {
		this.bulkWiresFileFormatTypeCode = bulkWiresFileFormatTypeCode;
	}
	public String getBulkWiresFileFormatTypeName() {
		return bulkWiresFileFormatTypeName;
	}
	public void setBulkWiresFileFormatTypeName(String bulkWiresFileFormatTypeName) {
		this.bulkWiresFileFormatTypeName = bulkWiresFileFormatTypeName;
	}

	public BulkWireFileFormatTypeDTO(String bulkWiresFileFormatTypeCode, String bulkWiresFileFormatTypeName) {
		super();
		this.bulkWiresFileFormatTypeCode = bulkWiresFileFormatTypeCode;
		this.bulkWiresFileFormatTypeName = bulkWiresFileFormatTypeName;
	}

	public BulkWireFileFormatTypeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bulkWiresFileFormatTypeCode == null) ? 0 : bulkWiresFileFormatTypeCode.hashCode());
		result = prime * result + ((bulkWiresFileFormatTypeName == null) ? 0 : bulkWiresFileFormatTypeName.hashCode());
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
		BulkWireFileFormatTypeDTO other = (BulkWireFileFormatTypeDTO) obj;
		if (bulkWiresFileFormatTypeCode == null) {
			if (other.bulkWiresFileFormatTypeCode != null)
				return false;
		} else if (!bulkWiresFileFormatTypeCode.equals(other.bulkWiresFileFormatTypeCode))
			return false;
		if (bulkWiresFileFormatTypeName == null) {
			if (other.bulkWiresFileFormatTypeName != null)
				return false;
		} else if (!bulkWiresFileFormatTypeName.equals(other.bulkWiresFileFormatTypeName))
			return false;
		return true;
	}
}