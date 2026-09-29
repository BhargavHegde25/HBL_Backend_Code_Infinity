package com.kony.dbputilities.customersecurityservices;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.BCrypt;

public class ReplacePassword {

	public static void main(String[] args) throws HttpCallException {

		System.out.println(BCrypt.hashpw("kony1235", BCrypt.gensalt(11)));

//		HashMap<String, String> hashMap = new  HashMap<>();

//		hashMap.put("X-kony-Authorization", 
//				"eyAidHlwIjogImp3dCIsICJhbGciOiAiUlMyNTYiIH0.eyAiX3ZlciI6ICJ2MS4xIiwgImlzcyI6ICJodHRwczovL2FwcHNhZG1pbmNvbnNvbGUua29ueWxhYnMubmV0Ojg0NDMvYXV0aFNlcnZpY2UvMTAwMDAwMDAyIiwgIl9zY29wZSI6ICJnIiwgIl9pc3NtZXRhIjogIi9tZXRhZGF0YS94RWJwSENBVmlwN3M0cVpic0g0eWN3PT0iLCAiX2FwcCI6ICI5MjIyZWNmMC1mYmE2LTQzOWUtYWUzNC01YjU3ZDhlNTc4YjciLCAiX3Nlc3Npb25faWQiOiAiNjQxNTQxZTktOWZkMC00ZWUwLWE4MWQtMzc4NzhiMzUyNjk2IiwgIl9wdWlkIjogMjU1MDY3NDQwNDcxNjc3ODEyOCwgIl9hdXRoeiI6ICJleUp3WlhKdGFYTnphVzl1Y3lJNmUzMHNJbkp2YkdWeklqcGJYWDAiLCAiX2lkcCI6ICJEYnhVc2VyTG9naW4iLCAiZXhwIjogMTU0NzU2MjUwNSwgImlhdCI6IDE1NDc1NjEzMDUsICJfc2Vzc2lvbl90aWQiOiAiMTAwMDAwMDAyIiwgIl9wcm92X3VzZXJpZCI6ICIxIiwgImp0aSI6ICI5ZWZlMjhjYS1mNzFhLTRiMzgtOTIwMy1hYjNhMTgxZmIyNmQiLCAiX2FjcyI6ICIxMDAwMDAwMDIiLCAiX3Byb3ZpZGVycyI6IFsgIkRieFVzZXJMb2dpbiIgXSB9.Rsp7W3ROP4eNpYrYZaHdNRa290B1tCHqTSD2qJ3jiXc2wcDxQ9bAOEKvvSyDBF3smwuPJCrAEgdZX1lXbTHscpmZxtmIGqsBIFj_AFRLpME-LS7Txr4kAcSMqAwM8VwqA21RxtK8Pws-P-HbrcEswAftan8OEX7M8A7IMu-Z6WmnV_emJnzsfo21lZjuu2CGfjv-HiFqZp9jEa28xOpZFDhG2VwkH43XI0TDVWVGVDMMc_UOAlryWmtrHjhS1nVdx_9EO19atYNhUPwhknWcCn1H7U7gMoVcSyoD9kHemrZ473sekzqn9bF-yQGd2Lhm7eStbQ86TtJrDLAhNzzEcg"
//				);
//		Result result = HelperMethods.callGetApi(null,"", hashMap, URLConstants.CUSTOMER_GET);
//		HashMap<String, String> hashMap2 = new HashMap<>();
//		if(HelperMethods.hasRecords(result)) {
//			List<Record> list = result.getAllDatasets().get(0).getAllRecords();
//			for(Record record : list) {
//				if(HelperMethods.getFieldValue(record, "id").startsWith("SamplePassword")) {
//					hashMap2.put("id", HelperMethods.getFieldValue(record, "id"));
//					if(StringUtils.isNotBlank(HelperMethods.getFieldValue(record, "Password"))) {
//						String salt = BCrypt.gensalt(DBPUtilitiesConstants.SALT_NUMBER_OF_ROUNDS);
//						String hashedPassword = BCrypt.hashpw(HelperMethods.getFieldValue(record, "Password"), salt);
//						hashMap2.put("Password", hashedPassword);
//						result = HelperMethods.callApi(null,hashMap2, hashMap, URLConstants.CUSTOMER_UPDATE);
//					}
//				}
//			}
//		}

	}
}
