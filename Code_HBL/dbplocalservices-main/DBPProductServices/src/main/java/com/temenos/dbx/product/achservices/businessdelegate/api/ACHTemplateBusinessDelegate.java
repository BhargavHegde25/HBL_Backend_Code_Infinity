package com.temenos.dbx.product.achservices.businessdelegate.api;

import java.util.List;

import com.dbp.core.api.BusinessDelegate;
import com.temenos.dbx.product.achservices.dto.BBTemplateDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateSubRecordDTO;
import com.temenos.dbx.product.achservices.dto.BBTemplateTypeDTO;
import com.temenos.dbx.product.commons.dto.FilterDTO;

public interface ACHTemplateBusinessDelegate extends BusinessDelegate{

	/**
	 *Method to create template and its records, sub-records.
	 *@author KH2626
	 *@version 1.0
	 *@param templateDTO contains data for to create template
	 *@param records contains data to create records and sub-records
	 **/
	public BBTemplateDTO createTemplate(BBTemplateDTO templateDTO);
	
	/**
	 * interface to fetch TemplateTypes
	 * @author kh2304
	 * @version 1.0
	 * @return List of Template Details.
	 */
	public List<BBTemplateTypeDTO> getACHTemplateType();

	/**
	 *@author KH2626 
	 *@version 1.0
	 *@param filters contains filter data to fetch all templates
	 **/
	public List<BBTemplateDTO> fetchAllACHTemplates(String customerId, FilterDTO filters, String templateID);

	 /**
	 * Method to fetch template details for given template_id
	 * @author KH2624
	 * @param templateDTO contains templateDTO
	 * @return template Details for specified template_Id
	 *
	 */
	public List<BBTemplateDTO> fetchTemplate(BBTemplateDTO templateDTO);

	/**
	 * Method to fetch template records for given template_id
	 * @author KH2624
	 * @param templateDTO contains template details.
	 * @return list of template record details.
	 *
	 */
	public List<BBTemplateRecordDTO> fetchTemplateRecords(BBTemplateDTO templateDTO);

	/**
	 * Method to fetch template sub records for given templateRecord_id
	 * @author KH2624
	 * @param bbTemplateRecordDTO contains record details.
	 * @return list of template sub records
	 */
	public List<BBTemplateSubRecordDTO> fetchTemplateSubRecord(BBTemplateRecordDTO bbTemplateRecordDTO);

	/**
	 * Method update template details for given template_id
	 * @author KH2624
	 * @param bbTemplateDTO updates details of templateDTO
	 */
	public void updateTemplate(BBTemplateDTO bbTemplateDTO);
	
	/**
	 * @author sharath.prabhala
	 * @param templateId
	 * @return
	 */
	public BBTemplateDTO deleteTemplate(Long templateId);
	
	/**
	 * @author poondla.hemanth
	 * @param templateId
	 * @return
	 */
	public boolean softDeleteTemplate(Long templateId);
	
}