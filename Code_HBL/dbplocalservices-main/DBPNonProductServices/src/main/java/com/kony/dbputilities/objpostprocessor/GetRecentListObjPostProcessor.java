package com.kony.dbputilities.objpostprocessor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;
import com.konylabs.middleware.common.objectservice.ObjectServicePostProcessor;

public class GetRecentListObjPostProcessor implements ObjectServicePostProcessor {

    @Override
    public void execute(FabricRequestManager requestManager, FabricResponseManager responseManager) throws Exception {
		Log4j2Configurator.getInstance();

        JsonObject jsonObject = responseManager.getPayloadHandler().getPayloadAsJson().getAsJsonObject();
        JsonArray transactionArray = new JsonArray();
        JsonArray paypersonArray = new JsonArray();
        if (jsonObject.has("accounttransaction") && jsonObject.get("accounttransaction").isJsonArray()) {
            transactionArray = jsonObject.get("accounttransaction").getAsJsonArray();
        }
        if (jsonObject.has("paypersontransaction") && jsonObject.get("paypersontransaction").isJsonArray()) {
            paypersonArray = jsonObject.get("paypersontransaction").getAsJsonArray();
        }

        for (int i = 0; i < paypersonArray.size(); i++) {
            transactionArray.add(paypersonArray.get(i).getAsJsonObject());
        }

        JsonArray sortedJsonArray = new JsonArray();

        List<JsonObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < transactionArray.size(); i++) {
            jsonValues.add(transactionArray.get(i).getAsJsonObject());
        }
        if (jsonValues.size() > 1) {
            Collections.sort(jsonValues, new Comparator<JsonObject>() {
                private static final String KEY_NAME = "transactionDate";

                @Override
                public int compare(JsonObject a, JsonObject b) {
                    String valA = new String();
                    String valB = new String();

                    try {
                        valA = a.get(KEY_NAME).getAsString();
                        valB = b.get(KEY_NAME).getAsString();
                    } catch (Exception e) {

                    }

                    Date dateA = HelperMethods.getFormattedTimeStamp(valA);
                    Calendar calenderA = Calendar.getInstance();
                    calenderA.setTime(dateA);

                    Date dateB = HelperMethods.getFormattedTimeStamp(valB);
                    Calendar calenderB = Calendar.getInstance();
                    calenderB.setTime(dateB);

                    long millisA = calenderA.getTimeInMillis();
                    long millisB = calenderB.getTimeInMillis();

                    return (int) (millisA - millisB);
                }
            });
        }

        for (int i = 0; i < jsonValues.size() && i < 6; i++) {
            sortedJsonArray.add(jsonValues.get(i));
        }

        JsonObject recentTransactions = new JsonObject();
        recentTransactions.add("recentTransactions", sortedJsonArray);
        responseManager.getPayloadHandler().updatePayloadAsJson(recentTransactions);

    }
}
