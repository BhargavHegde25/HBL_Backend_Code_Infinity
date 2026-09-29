package com.temenos.dbx.mfa.utils;

import java.util.ArrayList;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.BCrypt;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.temenos.dbx.product.dto.CustomerSecurityQuestionsViewDTO;
import com.temenos.dbx.product.utils.DTOUtils;

public class SecurityQuestionsUtil {

    private List<CustomerSecurityQuestionsViewDTO> customerSecurityQuestions; 

    public boolean verifySecurityQuestionsFromDB(JsonArray jsonArray) {

        boolean status = false;

        if (customerSecurityQuestions != null) {
            for (CustomerSecurityQuestionsViewDTO record : customerSecurityQuestions) {
                for (JsonElement element : jsonArray) {
                    JsonObject question = element.getAsJsonObject();
                    if (!question.has("questionId") || question.get("questionId").isJsonNull()) {
                        return false;
                    } else if (question.get("questionId").getAsString()
                            .equals(record.getSecurityQuestion_id())) {
                        if (!question.has("customerAnswer") || question.get("customerAnswer").isJsonNull()) {
                            return false;
                        } else if (BCrypt.checkpw(question.get("customerAnswer").getAsString(),
                                record.getCustomerAnswer())) {
                            status = true;

                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        return status;
    }

    public void loadCustomerSecurityQuestions(DataControllerRequest dcRequest, String user_id) {
    	 String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + user_id;
    	
        List<DBPDTO> dbpdtos = DTOUtils.getDTOListfromDB(dcRequest, filter, URLConstants.CUSTOMERSECURITYQUESTION_VIEW, true, true);
        if(dbpdtos.size() >0) {
            customerSecurityQuestions = new ArrayList<CustomerSecurityQuestionsViewDTO>();
            for(DBPDTO dbpdto : dbpdtos) {
                customerSecurityQuestions.add((CustomerSecurityQuestionsViewDTO) (dbpdto));
            }
        }
    }

    public void loadCustomerSecurityQuestions(FabricRequestManager requestManager, String user_id) {
    	 String filter = "Customer_id" + DBPUtilitiesConstants.EQUAL + user_id;
        List<DBPDTO> dbpdtos =  DTOUtils.getDTOListfromDB(requestManager, filter, URLConstants.CUSTOMERSECURITYQUESTION_VIEW, true, true);
        if(dbpdtos.size() >0) {
            customerSecurityQuestions = new ArrayList<CustomerSecurityQuestionsViewDTO>();
            for(DBPDTO dbpdto : dbpdtos) {
                customerSecurityQuestions.add((CustomerSecurityQuestionsViewDTO) (dbpdto));
            }
        }
    }

    public boolean areSecurityQuestionsPresent() {
        return customerSecurityQuestions != null && customerSecurityQuestions.size() > 0;
    }

    public JsonArray getSecurityQuestionsAttributesJsonArray(int questionSize) {

        JsonArray questions = new JsonArray();

        int randomNumber = (int) (Math.random() * customerSecurityQuestions.size() - 1);

        if (customerSecurityQuestions.size() == 0) {
            return questions;

        }
        CustomerSecurityQuestionsViewDTO question;

        JsonObject jsonObject = new JsonObject();

        for (int i = 0; i < questionSize; i++, randomNumber++) {
            question = customerSecurityQuestions.get(randomNumber % customerSecurityQuestions.size());
            jsonObject = new JsonObject();
            jsonObject.addProperty("Question", question.getQuestion());
            jsonObject.addProperty("SecurityQuestion_id", question.getSecurityQuestion_id());
            questions.add(jsonObject);
        }

        return questions;
    }

    public Dataset getSecurityQuestionsAttributesDataset(int questionSize) {


        Dataset questions = new Dataset();


        int randomNumber = HelperMethods.generateRandomWithRange(0, customerSecurityQuestions.size() - 1);

        if (customerSecurityQuestions.size() == 0) {
            return questions;

        }
        CustomerSecurityQuestionsViewDTO question;

        Record record = new Record();
        for (int i = 0; i < questionSize; i++, randomNumber++) {
            record = new Record();
            question = customerSecurityQuestions.get(randomNumber % customerSecurityQuestions.size());
            record.addParam("Question", question.getQuestion());
            record.addParam("SecurityQuestion_id", question.getSecurityQuestion_id());
            questions.addRecord(record);
        }

        return questions;
    }



}
