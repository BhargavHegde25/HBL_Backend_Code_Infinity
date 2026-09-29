
/**
 * @author kh2304
 * @version 1.0
 */
package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BBTemplateTypeDTO implements DBPDTO {

	private static final long serialVersionUID = -9207881155092552914L;

	@JsonProperty("templateType_id")
	private long templateType_id;

	@JsonProperty("templateTypeName")
	private String templateTypeName;

	public BBTemplateTypeDTO() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((templateTypeName == null) ? 0 : templateTypeName.hashCode());
		result = prime * result + (int) (templateType_id ^ (templateType_id >>> 32));
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
		BBTemplateTypeDTO other = (BBTemplateTypeDTO) obj;
		if (templateType_id != other.templateType_id)
			return false;
		return true;
	}

	public BBTemplateTypeDTO(long templateType_id, String templateTypeName) {
		this.settemplateType_id(templateType_id);
		this.settemplateTypeName(templateTypeName);
	}

	public long gettemplateType_id() {
		return templateType_id;
	}

	public void settemplateType_id(long templateType_id) {
		this.templateType_id = templateType_id;
	}

	public String gettemplateTypeName() {
		return templateTypeName;
	}

	public void settemplateTypeName(String templateTypeName) {
		this.templateTypeName = templateTypeName;
	}

}
