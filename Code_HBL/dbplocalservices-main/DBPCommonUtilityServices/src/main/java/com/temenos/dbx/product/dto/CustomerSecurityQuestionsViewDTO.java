package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dbp.core.api.DBPDTO;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CustomerSecurityQuestionsViewDTO implements DBPDTOEXT {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2557499095021839023L;
	private String customer_id;
	private String customerAnswer;
	private String securityQuestion_id;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String question;
	private String questionStatus_id;
	private String customerStatus_id;
	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}
	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	/**
	 * @return the customerAnswer
	 */
	public String getCustomerAnswer() {
		return customerAnswer;
	}
	/**
	 * @param customerAnswer the customerAnswer to set
	 */
	public void setCustomerAnswer(String customerAnswer) {
		this.customerAnswer = customerAnswer;
	}
	/**
	 * @return the securityQuestion_id
	 */
	public String getSecurityQuestion_id() {
		return securityQuestion_id;
	}
	/**
	 * @param securityQuestion_id the securityQuestion_id to set
	 */
	public void setSecurityQuestion_id(String securityQuestion_id) {
		this.securityQuestion_id = securityQuestion_id;
	}
	/**
	 * @return the createdby
	 */
	public String getCreatedby() {
		return createdby;
	}
	/**
	 * @param createdby the createdby to set
	 */
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	/**
	 * @return the modifiedby
	 */
	public String getModifiedby() {
		return modifiedby;
	}
	/**
	 * @param modifiedby the modifiedby to set
	 */
	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}
	/**
	 * @return the createdts
	 */
	public String getCreatedts() {
		return createdts;
	}
	/**
	 * @param createdts the createdts to set
	 */
	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}
	/**
	 * @return the lastmodifiedts
	 */
	public String getLastmodifiedts() {
		return lastmodifiedts;
	}
	/**
	 * @param lastmodifiedts the lastmodifiedts to set
	 */
	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	/**
	 * @return the questionStatus_id
	 */
	public String getQuestionStatus_id() {
		return questionStatus_id;
	}
	/**
	 * @param questionStatus_id the questionStatus_id to set
	 */
	public void setQuestionStatus_id(String questionStatus_id) {
		this.questionStatus_id = questionStatus_id;
	}
	/**
	 * @return the customerStatus_id
	 */
	public String getCustomerStatus_id() {
		return customerStatus_id;
	}
	/**
	 * @param customerStatus_id the customerStatus_id to set
	 */
	public void setCustomerStatus_id(String customerStatus_id) {
		this.customerStatus_id = customerStatus_id;
	}

	@Override
	public Object loadDTO(String id) {
		// TODO Auto-generated method stub
		String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

		DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERSECURITYQUESTION_VIEW, true, true);

		return exts;
	}
	@Override
	public Object loadDTO() {
		String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
		List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();
		List<CustomerSecurityQuestionsViewDTO> customerSecurityQuestionsViewDTOs = new ArrayList<CustomerSecurityQuestionsViewDTO>();
		DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERSECURITYQUESTION_VIEW, true, true);
		if(exts != null && exts.size() >0) {
			for(int i=0; i<exts.size(); i++) {
				CustomerSecurityQuestionsViewDTO customerSecurityQuestionsViewDTO = (CustomerSecurityQuestionsViewDTO)exts.get(i);
				customerSecurityQuestionsViewDTOs.add(customerSecurityQuestionsViewDTO);
			}
		}
		
		
		return customerSecurityQuestionsViewDTOs;
	}
	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		return true;
	}


}
