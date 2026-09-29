package com.kony.adminconsole.dto;

import com.konylabs.middleware.dataobject.Result;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DashboardCountsBean {
    private Map<String, Map<String, Map<String, String>>> _groupedCountsMap;  // counts map, categorised by the model and feature under the approvalrequests table
    // module => feature => category => count
    private Map<String, String> _consolidatedCountsMap;


    public Map<String, Map<String, Map<String, String>>> getGroupedCountsMap(){
        return _groupedCountsMap;
    }

    public Map<String, String> getConsolidatedCountsMap() {
        return _consolidatedCountsMap;
    }

    public Result getGroupedCountsMapAsResultObject(){
        Result result = new Result();

        return result;
    }

    public Result getConsolidatedCountsMapAsResultObject() {
        Result countsResult = new Result();
        for(String countKey : this.getConsolidatedCountsMap().keySet()){
            countsResult.addParam(countKey, (String) this.getConsolidatedCountsMap().get(countKey));
        }
        return countsResult;
    }

    public void setGroupedCountsMap(Map<String, Map<String, Map<String, String>>> _groupedCountsMap){
        this._groupedCountsMap = _groupedCountsMap;
    }

    public void setConsolidatedCountsMap(Map<String, String> _consolidatedCountsMap) {
        this._consolidatedCountsMap = _consolidatedCountsMap;
    }

//    public void setGroupedCountsMapFromDBResponse(String dbGroupedResponse){
//        Map<String, Map<String, Map<String, String>>> groupedCounts = new HashMap<>();
//
//        dbGroupedResponse = "{\n" +
//                "    \"records\": [\n" +
//                "      {\n" +
//                "        \"feature\": \"Role\",\n" +
//                "        \"counts\": \"1\",\n" +
//                "        \"module\": \"Customer Management\",\n" +
//                "        \"category\": \"pendingRequests\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"User\",\n" +
//                "        \"counts\": \"2\",\n" +
//                "        \"module\": \"Customer Management\",\n" +
//                "        \"category\": \"pendingRequests\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"Role\",\n" +
//                "        \"counts\": \"2\",\n" +
//                "        \"module\": \"Employee Management\",\n" +
//                "        \"category\": \"pendingRequests\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"User\",\n" +
//                "        \"counts\": \"1\",\n" +
//                "        \"module\": \"Employee Management\",\n" +
//                "        \"category\": \"pendingRequests\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"Role\",\n" +
//                "        \"counts\": \"2\",\n" +
//                "        \"module\": \"Customer Management\",\n" +
//                "        \"category\": \"requestHistory\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"User\",\n" +
//                "        \"counts\": \"2\",\n" +
//                "        \"module\": \"Customer Management\",\n" +
//                "        \"category\": \"requestHistory\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"Role\",\n" +
//                "        \"counts\": \"2\",\n" +
//                "        \"module\": \"Employee Management\",\n" +
//                "        \"category\": \"requestHistory\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"User\",\n" +
//                "        \"counts\": \"1\",\n" +
//                "        \"module\": \"Employee Management\",\n" +
//                "        \"category\": \"requestHistory\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"Role\",\n" +
//                "        \"counts\": \"1\",\n" +
//                "        \"module\": \"Customer Management\",\n" +
//                "        \"category\": \"pendingApprovals\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"Role\",\n" +
//                "        \"counts\": \"3\",\n" +
//                "        \"module\": \"Employee Management\",\n" +
//                "        \"category\": \"pendingApprovals\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"User\",\n" +
//                "        \"counts\": \"1\",\n" +
//                "        \"module\": \"Employee Management\",\n" +
//                "        \"category\": \"pendingApprovals\"\n" +
//                "      },\n" +
//                "      {\n" +
//                "        \"feature\": \"User\",\n" +
//                "        \"counts\": \"1\",\n" +
//                "        \"module\": \"Employee Management\",\n" +
//                "        \"category\": \"approvalHistory\"\n" +
//                "      }\n" +
//                "    ],\n" +
//                "    \"opstatus\": 0,\n" +
//                "    \"httpStatusCode\": 0\n" +
//                "  }";
//        JSONObject groupedCountsObj = new JSONObject(dbGroupedResponse);
//        if(groupedCountsObj.has("records")){
//            JSONArray countsArray = groupedCountsObj.getJSONArray("records");
//            if(countsArray.length() != 0){
//                for(Object obj: countsArray){
//                    JSONObject countObject = (JSONObject) obj;
//                    if(!groupedCounts.containsKey(countObject.get("module"))){
//                        groupedCounts.put((String) countObject.get("module"), new HashMap<String, Map<String, String>>());
//                    }
//                    if(!groupedCounts.get((String) countObject.get("module")).containsKey(countObject.get("feature"))){
//                        groupedCounts.get((String) countObject.get("module")).put((String) countObject.get("feature"), new HashMap<String, String>());
//                    }
//                    if(!groupedCounts.get((String) countObject.get("module")).get((String) countObject.get("feature")).containsKey(countObject.get("category"))){
//                        groupedCounts.get((String) countObject.get("module")).get((String) countObject.get("feature")).put((String) countObject.get("category"), (String) countObject.get("counts"));
//                    }
//                }
//            }
//        }
//        System.out.println(groupedCounts);
//        this.setGroupedCountsMap(groupedCounts);
//    }

    public void setConsolidatedCountsMapFromDBResponse(String dbConsolidatedResponse){
        JSONObject countsObj = new JSONObject(dbConsolidatedResponse);
        Map<String, String> countsResult = new HashMap<>();
        if (countsObj.has("records")){
            JSONArray countsArray = countsObj.getJSONArray("records");

            if(countsArray.length() != 0) {
                for (Object obj : countsArray) {
                    JSONObject countItem = (JSONObject) obj;
                    countsResult.put((String) countItem.get("category"), (String) countItem.get("counts"));
                }
            }
        }
        this.setConsolidatedCountsMap(countsResult);
    }

    public void setDashboardCountsDataFromDBRecords(JSONArray records){
        Map<String, String> countsResult = new HashMap<>();
        if(records.length() != 0) {
            for (Object obj : records) {
                JSONObject countItem = (JSONObject) obj;
                countsResult.put((String) countItem.get("category"), (String) countItem.get("counts"));
            }
        }
        this.setConsolidatedCountsMap(countsResult);
    }
}
