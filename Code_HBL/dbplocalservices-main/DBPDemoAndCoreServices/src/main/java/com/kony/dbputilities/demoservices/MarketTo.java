package com.kony.dbputilities.demoservices;

public class MarketTo {
    /*
     * public static String marketoInstance; public static String marketoBaseURL; public static String clientId; public
     * static String clientSecret;
     * 
     * static{ marketoBaseURL="https://656-WNA-414.mktorest.com"; marketoInstance=marketoBaseURL+
     * "/identity/oauth/token?grant_type=client_credentials"; clientId="9e78cc15-1cbd-497f-a26f-01c0fc1d408e";
     * clientSecret="RJIovd9Nz4iBII1qChhScqM94l3ODtFS";
     * 
     * }
     * 
     * public static void main(String[] args) throws IOException { YAWS_USER user=new YAWS_USER();
     * user.setEMAIL("madhava.challa9@gmail.com"); user.setFIRSTNAME("Madhav"); user.setLASTNAME("Solutionsus1");
     * user.setMOBILE("97891722222"); user.setCOUNTRY("US"); user.setSTATE("NY"); user.setJOBTITLE("Admin");
     * 
     * MarketTo.createLead_NewUser(user);
     * 
     * System.exit(0); }
     * 
     * public static String getToken() { String url = marketoInstance + "&client_id=" + clientId + "&client_secret=" +
     * clientSecret; String result = getData(url); JSONObject resultJson=new JSONObject(result); String
     * access_token=resultJson.getString("access_token"); return access_token; }
     * 
     * public static int createLead_NewUser(YAWS_USER user) throws IOException{
     * 
     * String access_token=getToken();
     * 
     * String action="createOrUpdate"; String lookupField="email"; JSONObject createPayloadJson=constructPayload(action,
     * lookupField, user); System.out.println("PayLoad"+createPayloadJson); String url = marketoBaseURL +
     * "/rest/v1/leads.json"; URL URLobj = new URL(url); HttpURLConnection urlConn = (HttpURLConnection)
     * URLobj.openConnection(); urlConn.setRequestMethod("POST"); urlConn.setAllowUserInteraction(false);
     * urlConn.setDoOutput(true); urlConn.setRequestProperty("Content-type",
     * ContentType.APPLICATION_JSON.getMimeType()); urlConn.setRequestProperty("Authorization",
     * "Bearer "+access_token);//62dbd6e5-d1d3-426e-bc58-357b19738f99:ab
     * 
     * String PayloadString = createPayloadJson.toString();
     * 
     * OutputStream os = urlConn.getOutputStream(); os.write(PayloadString.getBytes()); os.flush();
     * 
     * int responseCode = urlConn.getResponseCode(); if (responseCode == 200) { InputStream inStream=
     * urlConn.getInputStream(); String data = convertStreamToString(inStream); JSONObject responseJson=new
     * JSONObject(data); JSONArray resultArray=responseJson.getJSONArray("result"); for (int i = 0; i <
     * resultArray.length(); i++) { JSONObject resultJson=resultArray.getJSONObject(i); String
     * Status=resultJson.getString("status"); if(Status.equalsIgnoreCase("skipped")){ JSONArray
     * reasonsArray=resultJson.getJSONArray("reasons"); for (int j = 0; j < reasonsArray.length(); j++) { JSONObject
     * reasonJson=reasonsArray.getJSONObject(j); } }else{ } } } else { } return responseCode; }
     * 
     * private static String getData(String endpoint) { String data = null; try { URL url = new URL(endpoint);
     * HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection(); urlConn.setRequestMethod("GET");
     * urlConn.setAllowUserInteraction(false); urlConn.setDoOutput(true); urlConn.setRequestProperty("Content-type",
     * ContentType.APPLICATION_JSON.getMimeType()); urlConn.setRequestProperty("accept",
     * ContentType.APPLICATION_JSON.getMimeType()); int responseCode = urlConn.getResponseCode(); if (responseCode ==
     * 200) { InputStream inStream = urlConn.getInputStream(); data = convertStreamToString(inStream); } else { data =
     * "Status:" + responseCode; } } catch (MalformedURLException e) { } catch (IOException e) {
     * LOG.error(e.getMessage()); } catch(Exception e){ LOG.error(e.getMessage()); }
     * 
     * return data; }
     * 
     * @SuppressWarnings("resource") private static String convertStreamToString(InputStream inputStream) { try { return
     * new Scanner(inputStream).useDelimiter("\\A").next(); } catch (NoSuchElementException e) { return "";
     * }catch(Exception e){ LOG.error(e.getMessage()); return ""; } }
     * 
     * /* 'action' allowed Values : createOrUpdate, createOnly, updateOnly, createDuplicate
     */
    /*
     * private static JSONObject constructPayload(String action, String lookupField, YAWS_USER user) {
     * 
     * JSONObject PayloadJson=new JSONObject(); PayloadJson.put("action", action); PayloadJson.put("lookupField",
     * lookupField);
     * 
     * JSONArray usersJsonArray=new JSONArray(); JSONObject userJson=constructUserJson(user);
     * usersJsonArray.put(0,userJson); PayloadJson.put("input", usersJsonArray);
     * 
     * return PayloadJson; }
     * 
     * private static JSONObject constructUserJson(YAWS_USER user) { JSONObject userJson=new JSONObject();
     * userJson.put("email", user.getEMAIL()); userJson.put("firstName", user.getFIRSTNAME()); userJson.put("lastName",
     * user.getLASTNAME()); userJson.put("phone", user.getMOBILE()); userJson.put("Title", user.getJOBTITLE());
     * userJson.put("country", user.getCOUNTRY()); userJson.put("state", user.getSTATE()); userJson.put("company",
     * user.getCOMPANY()); userJson.put("utmsource", user.getUtm_source()); userJson.put("utmcampaign",
     * user.getUtm_campaign()); userJson.put("utmcontent", user.getUtm_content()); userJson.put("utmmedium",
     * user.getUtm_medium()); userJson.put("utmterm", user.getUtm_term());
     * 
     * userJson.put("leadSource", user.getLead_source()); userJson.put("LeadSourceDescription",
     * user.getLead_source_Desc()); userJson.put("adGroup", user.getAd_group()); userJson.put("campaign",
     * user.getCampaign()); userJson.put("keyword", user.getKeyword());
     * 
     * userJson.put("dms","Retail Banking");
     * 
     * return userJson; }
     */
}
