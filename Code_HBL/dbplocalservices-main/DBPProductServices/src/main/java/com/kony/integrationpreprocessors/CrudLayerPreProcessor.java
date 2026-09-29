package com.kony.integrationpreprocessors;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.CryptoText;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class CrudLayerPreProcessor implements DataPreProcessor2 {
    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
        Set<String> keys = new HashSet<>();
        keys.add("Ssn");
        keys.add("DrivingLicenseNumber");
        String filter = (String) inputMap.get(DBPUtilitiesConstants.FILTER);
        if (StringUtils.isNotBlank(filter)) {
            String[] strings = filter.split(DBPUtilitiesConstants.AND);
            String andFilter = "";
            for (String string : strings) {
                String[] strings1 = string.split(DBPUtilitiesConstants.OR);
                String orFitler = "";
                for (String string1 : strings1) {
                    if (!orFitler.isEmpty()) {
                        orFitler += DBPUtilitiesConstants.OR;
                    }
                    orFitler += processString(string1, keys);
                }
                if (!andFilter.isEmpty()) {
                    andFilter += DBPUtilitiesConstants.AND;
                }
                andFilter += orFitler;
            }
            filter = andFilter;
            inputMap.put(DBPUtilitiesConstants.FILTER, filter);
        }
        for (String key : keys) {
            if (inputMap.containsKey(key)) {
                String value = CryptoText.encrypt((String) inputMap.get(key));
                inputMap.put(key, value);
            }
        }
        return true;
    }

    private static String processString(String string, Set<String> keys) throws Exception {
        String[] strings1 = string.split(DBPUtilitiesConstants.EQUAL);
        if (keys.contains(strings1[0])) {
            strings1[1] = "'" + CryptoText.encrypt(strings1[1].trim()) + "'";
        }
        return strings1[0] + DBPUtilitiesConstants.EQUAL + strings1[1];
    }
}
