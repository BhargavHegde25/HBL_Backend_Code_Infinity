package com.bct.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.NCHLNpixTransactionModel.TokenResponse;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.HelperMethods;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.convertions.ResultToJSON;
import com.konylabs.middleware.dataobject.Result;
import com.neovisionaries.i18n.CountryCode;
import com.temenos.dbx.product.utils.CustomerSessionsUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NCHLNpixTransactionUAT {

    //private static final String NPIX_AUTH_URL = "http://uat.connectips.com:9062/oauth/token";
    //private static final String NPIX_URL = "http://uat.connectips.com:9062/npix/v1/api/";
    //private static final String NPIX_AUTH_URL = "https://devnpix.connectips.com/oauth/token";
    //private static final String NPIX_URL = "https://devnpix.connectips.com/npix/v1/api/";
    //private static final String PFX_FILE_PASS = "NPIX@123";
    private static final String wsLogUrl = "http://192.168.214.79:82/tcmbapi/NotifyUser.asmx";

    private static final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
    private static final Gson gson = new Gson();
    private static final Logger logger = LogManager.getLogger(NCHLNpixTransactionUAT.class);
    
   public static void main(String[] args) throws Exception {
	   
    	System.out.println("Testing!!");
    	String base64Credentials = "SEJMTlBJWDpBYmNkQDEyMw==";
    	String username = "HBLNPIX@999";
    	String password = "123Abcd@123";
    	
    	String NPIX_AUTH_URL = "https://devnpix.connectips.com/oauth/token";
    	String NPIX_URL = "https://devnpix.connectips.com/npix/v1/api/";
    	String PFX_FILE_PASS = "NPIX@123";
    	String PFX_FILE_PATH = "C:\\HBL-Certificate\\Certificate\\NPIX.pfx";
    	//String PFX_FILE_PATH = EnvironmentConfigurationsHandler.getServerProperty("NPIX_CERT_PATH"); // /data/infinity/certificates/NPIX.pfx
    	Date date = new Date();
    	String modifiedDate= new SimpleDateFormat("yyyy/MM/dd").format(date); 
    	System.out.println("startDate :"+ modifiedDate);
    	String text = "00";
        String regex = "^(?!0{2,3}$)\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String URL = "http://192.168.206.18/eNotifyHBL/Pages/SMSOutBox.aspx?contact=8124442352&body=test message";
        
        // Find and display matches
        while (matcher.find()) {
            System.out.println("Found match at index " + matcher.start() + ": " + matcher.group());
        }
		
        CountryCode code = CountryCode.getByCode("NP");

        System.out.println("ISO 3166-1 numeric code = " + code.getNumeric());
        Double latitude = 18.29;
        Double longitude = 83.89;
       
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        Date date1 = cal.getTime();
        
        System.out.println("currentDate:"+ date1);
       
        //System.out.println("pastDate   :" + pastDate);
        
        String countryCode = "NP"; // Example: United States
        Locale locale = new Locale("", countryCode);

            System.out.println("Country: " + locale.getDisplayCountry());
            
        
        System.out.println("latitude :  "+latitude+" longitude: "+longitude);
            String[] addressArray = new String[2];
            try
            {
                LatLng latLng = new LatLng();
                latLng.setLat(BigDecimal.valueOf(latitude));
                latLng.setLng(BigDecimal.valueOf(longitude));
                final Geocoder geocoder = new Geocoder();
                GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(latLng).getGeocoderRequest();
                GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
                List<GeocoderResult> results = geocoderResponse.getResults();
                //This will print geographical information        
                System.out.println("results :  "+results);
                List<GeocoderAddressComponent> geList= results.get(0).getAddressComponents();
                for(int i =0; i < geList.size(); i++)
                {
                    if(geList.get(i).getTypes().get(0).equalsIgnoreCase("locality"))
                    {
                        addressArray[0] = geList.get(i).getLongName();
                    }
                }
                addressArray[1] = results.get(0).getFormattedAddress();
                System.out.println("addressArray :  " + addressArray);
            }catch (Exception e) {
            	System.out.println(e.toString());
            	System.out.println(e.getLocalizedMessage());
            }
		
    	String refreshToken = getRefreshToken(NPIX_AUTH_URL, base64Credentials, username, password);
    	System.out.println("refreshToken:"+ refreshToken);
    	
    	String accessToken = getAccessToken(NPIX_AUTH_URL, base64Credentials, refreshToken);
    	System.out.println("accessToken:"+ accessToken);
    	
    	String rawbody = "{\"payerDetail\":{\"address\":{\"country\":\"Nepal\",\"city\":\"BHAKTAPUR Bhaktapur-Municipality 12 GAHITI\",\"geoCode\":\"27.7151,85.3278\",\"location\":\"LALITPUR Lalitpur-Metropolitan 25 BHAISEPATI\"},\"accountDetail\":{\"branchCode\":\"1\",\"bankCode\":\"0701\",\"vpa\":\"37010199010\",\"accountType\":\"Savings\",\"accountNumber\":\"11001019900019\"},\"deviceInformation\":{\"os\":\"web\",\"ip\":\"192.168.1.111\",\"mobile\":\"9841257125\",\"geoCode\":\"13.0777088,77.5716864\",\"location\":\"LALITPUR Lalitpur-Metropolitan 25 BHAISEPATI\",\"teleCom\":\"NTC\"},\"accountType\":\"VPA\",\"name\":\"Lokeswara\",\"identificationNumber\":\"\",\"identificationType\":\"\",\"type\":\"PERSON\"},\"amount\":\"21.00\",\"purpose\":\"S1301\",\"countryCode\":\"IND\",\"chargeAmount\":\"5\",\"currency\":\"NPR\",\"txnType\":\"CROSS_BORDER\",\"payeeDetail\":{\"accountDetail\":{\"vpa\":\"yogeshwanira@sbi\"},\"accountType\":\"VPA\",\"name\":\"yogesh\",\"type\":\"PERSON\",\"payeeRelationShip\":\"R1004\"},\"participantCode\":\"HBL\",\"participantService\":\"VAL_CUST\",\"remarks\":\"testat\",\"requestUniqueId\":\"HBLNPIXTRANSACT23337620250721172131\"}";
    	String signature = getSignature(PFX_FILE_PASS,PFX_FILE_PATH, rawbody);
    	System.out.println("signature:"+ signature);
    	
    	
    	//Limit check
    	String limtRawBody = "{\"amount\":\"24000.00\",\"vpa\":\"86085045802\",\"service\":\"XP2P_UPI\"}";
    	String limitSignature = getSignature(PFX_FILE_PASS,PFX_FILE_PATH, limtRawBody);
    	System.out.println("limitSignature:"+ limitSignature);
    	//String purpose = com.bct.utilities.NCHLNpixTransactionUAT.updateConsent(PFX_FILE_PASS,NPIX_URL,accessToken, PFX_FILE_PATH,"", limtRawBody);
    	//System.out.println("purpose Res:"+ purpose);
    }
   
   public static String getCurrentTimeStamp() {
       DateFormat dateFormat = new SimpleDateFormat("ddMMyyhhmmsss");
       Calendar calendar = Calendar.getInstance();

       return dateFormat.format(calendar.getTime());
   }

    /* METHOD FOR TOKEN & SIGNATURES */
    public static String getRefreshToken(String NPIX_AUTH_URL, String base64Credentials,
            String username, String password) throws Exception {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String requestBody = "username=" + username + "&password=" + password + "&grant_type=password";
        RequestBody body = RequestBody.create(requestBody,mediaType);

        String base64AuthHeader = "Basic " + base64Credentials;

        Request request = new Request.Builder()
                .url(NPIX_AUTH_URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", base64AuthHeader)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        TokenResponse tokenResponse = gson.fromJson(responseBody, TokenResponse.class);
        if (response.isSuccessful()) {

            return tokenResponse.getRefreshToken();
        } else {
            String responseCode = tokenResponse.getError();
            String responseMessage = tokenResponse.getErrorDescription();
            int statusCode = response.code();
            return "Failed to obtain access token. Response Code: " + statusCode
                    + ", Error Code: " + responseCode
                    + ", Error Description: " + responseMessage;
        }
    }
    
    
    public static String getPurpose(String PFX_FILE_PASS, String NPIX_URL, String bearerToken, String pathToPfxFile,String requestBody) throws Exception {
    	MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody);
        String messageSignature = getSignature(PFX_FILE_PASS, pathToPfxFile, requestBody);

        //String messageSignature = "pjdXGL+ypWp09suLAc9S17NdO0/prgOZIi/2LDZBzHlsGJcGbcuufY+LU+pt2asuoyvWJroi7ALAFxBcm5OH3xd6p95sDTZjR49Bu4dqIRUlj6BxetRoWa8NIkka+ll6MlVyw7NuKvj9H0BZgMFyRT600PFJJB3VCyfgORA8J78xatE77Eb9wluBtNbS98Xik+CDxvm2f4cpfMM41pLp1GDZgGzokYY/grfn2X3Fr3dq9MVAAd00LkOj7gxBgdM3L5rZL9PBWLFRLk95uCuPvxLClvFJ9I3LKZVHCLxLBUcWIX28yX20hv8BOCvaTh7EL2B5Zrl3INJ2luq/qpEAXA==";
        Request request = new Request.Builder()
        		.url(NPIX_URL + "transaction-purpose")
        		.method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + bearerToken)
                .addHeader("Message-Signature", messageSignature)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(" responseBody purpose:"+ responseBody);
        if (response.isSuccessful()) {
            return responseBody;
        } else {
            int statusCode = response.code();
            return "Failed to obtain access token. Response Code: " + statusCode
                   ;
        }
    }

    
    public static String getRelationship(String PFX_FILE_PASS, String NPIX_URL, String bearerToken, String pathToPfxFile,String requestBody) throws Exception {
    	MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody);
        String messageSignature = getSignature(PFX_FILE_PASS, pathToPfxFile, requestBody);

        //String messageSignature = "pjdXGL+ypWp09suLAc9S17NdO0/prgOZIi/2LDZBzHlsGJcGbcuufY+LU+pt2asuoyvWJroi7ALAFxBcm5OH3xd6p95sDTZjR49Bu4dqIRUlj6BxetRoWa8NIkka+ll6MlVyw7NuKvj9H0BZgMFyRT600PFJJB3VCyfgORA8J78xatE77Eb9wluBtNbS98Xik+CDxvm2f4cpfMM41pLp1GDZgGzokYY/grfn2X3Fr3dq9MVAAd00LkOj7gxBgdM3L5rZL9PBWLFRLk95uCuPvxLClvFJ9I3LKZVHCLxLBUcWIX28yX20hv8BOCvaTh7EL2B5Zrl3INJ2luq/qpEAXA==";
        Request request = new Request.Builder()
        		.url(NPIX_URL + "relationships")
        		.method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + bearerToken)
                .addHeader("Message-Signature", messageSignature)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(" responseBody purpose:"+ responseBody);
        if (response.isSuccessful()) {
            return responseBody;
        } else {
            int statusCode = response.code();
            return "Failed to obtain access token. Response Code: " + statusCode
                   ;
        }
    }
    
    public static String getAccessToken(String NPIX_AUTH_URL, String base64Credentials,
            String refresh_token) throws Exception {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        String requestBody = "grant_type=refresh_token&refresh_token=" + refresh_token;
        RequestBody body = RequestBody.create(mediaType, requestBody);

        Request request = new Request.Builder()
                .url(NPIX_AUTH_URL)
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + base64Credentials)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        TokenResponse tokenResponse = gson.fromJson(responseBody, TokenResponse.class);
        if (response.isSuccessful()) {
            return tokenResponse.getAccessToken();
        } else {
            String responseCode = tokenResponse.getError();
            String responseMessage = tokenResponse.getErrorDescription();
            int statusCode = response.code();
            return "Failed to obtain access token. Response Code: " + statusCode
                    + ", Error Code: " + responseCode
                    + ", Error Description: " + responseMessage;
        }
    }

    public static String getSignature(String PFX_FILE_PASS, String pathToPfxFile,
            String contentToEncode) throws Exception {
        try {
            String passToPfx = PFX_FILE_PASS;
            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(new FileInputStream(pathToPfxFile), passToPfx.toCharArray());
            Enumeration<String> aliases = ks.aliases();
            String alias = "";
            boolean isAliasWithPrivateKey = false;
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
                if (isAliasWithPrivateKey = ks.isKeyEntry(alias)) {
                    break;
                }
            }
            if (!isAliasWithPrivateKey) {
                throw new Exception("Private key not found for certificate on this server.");
            }

            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(passToPfx.toCharArray()));

            PrivateKey privateKey = pkEntry.getPrivateKey();

            String token = signToken(privateKey, contentToEncode);
            return token;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public static String signToken(PrivateKey privKey, String content) throws Exception {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privKey);
            signature.update(content.getBytes("UTF-8"));
            byte[] signed = signature.sign();
            String signedText = Base64.getEncoder().encodeToString(signed);
            return signedText;
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | SignatureException e) {
            throw new Exception("Couldn't generate token at the moment.", e);
        }
    }

    /* METHOD FOR CONSENT API */
	public static String createConsent(DataControllerRequest dc_request, String PFX_FILE_PASS, String NPIX_URL,
			String bearerToken, String pathToPfxFile, String Vpa, String requestBody) throws Exception {
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, requestBody);
		String messageSignature = getSignature(PFX_FILE_PASS, pathToPfxFile, requestBody);
		String status = "";
		// Parse the requestBody to get the direction value
		JsonObject requestBodyJson = gson.fromJson(requestBody, JsonObject.class);
		String direction = requestBodyJson.get("direction").getAsString();

		Request request = new Request.Builder().url(NPIX_URL + "consent/create").method("POST", body)
				.addHeader("Content-Type", "application/json").addHeader("Authorization", "Bearer " + bearerToken)
				.addHeader("Message-Signature", messageSignature).build();

		JsonObject returnJson = new JsonObject();

		try (Response response = client.newCall(request).execute()) {
			String responseBody = response.body().string();
			// String logresponse = wsNpixApiAuditLog(Vpa, requestBody, messageSignature,
			// responseBody, "create_consent");
			JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

			returnJson.addProperty("statusCode", response.code());
			returnJson.addProperty("responseCode", responseJson.get("responseCode").getAsString());
			returnJson.addProperty("responseMessage", responseJson.get("responseMessage").getAsString());

			status = (response.isSuccessful()) ? "success" : "failed";

			recordCrossBorderAPILog(dc_request, Vpa, messageSignature, requestBody, responseBody, status,
					"create_consent");

			if (response.isSuccessful()) {

				JsonObject responseData = responseJson.getAsJsonObject("responseData");

				JsonObject parsedResponseData = new JsonObject();
				parsedResponseData.addProperty("vpaId", responseData.get("vpaId").getAsString());
				parsedResponseData.addProperty("direction", responseData.get("direction").getAsString());
				parsedResponseData.addProperty("instrument", responseData.get("instrument").getAsString());
				parsedResponseData.addProperty("consent", responseData.get("consent").getAsString());
				parsedResponseData.addProperty("uniqueTransactingId",
						responseData.get("uniqueTransactingId").getAsString());
				// String updateresponse =
				// wsNpixUpdateVPAStatus(Vpa,responseData.get("consent").getAsString() ,
				// "CONSENT_PENDING",direction);
				returnJson.addProperty("consent", responseData.get("consent").getAsString());
				returnJson.add("responseData", parsedResponseData);
				JsonArray emptyArray = new JsonArray();
				returnJson.add("responseErrors", emptyArray);
			} else {
				// Handle error response
				JsonArray emptyArray = new JsonArray();
				JsonObject parsedResponseData = new JsonObject();
				parsedResponseData.addProperty("vpaId", "");
				parsedResponseData.addProperty("direction", "");
				parsedResponseData.addProperty("instrument", "");
				parsedResponseData.addProperty("consent", "");
				parsedResponseData.addProperty("uniqueTransactingId", "");
				returnJson.add("responseErrors", emptyArray);
			}
		} catch (IOException e) {
			// Handle exceptions
			returnJson.addProperty("statusCode", 0); // Set your custom error code here
			returnJson.addProperty("responseCode", -1); // Set your custom error code here
			returnJson.addProperty("responseMessage", "An error occurred."); // Set your custom error message hereM
			JsonObject parsedResponseData = new JsonObject();
			parsedResponseData.addProperty("vpaId", "");
			parsedResponseData.addProperty("direction", "");
			parsedResponseData.addProperty("instrument", "");
			parsedResponseData.addProperty("consent", "");
			parsedResponseData.addProperty("uniqueTransactingId", "");
			returnJson.add("responseErrors", new JsonArray());
		}

		// Return the JSON string
		return returnJson.toString();
	}

    
    public static String updateConsent(DataControllerRequest dc_request, String PFX_FILE_PASS, String NPIX_URL, String bearerToken, String pathToPfxFile,String Vpa, String requestBody) throws Exception {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody);
        String status = "";
        System.out.println("In side updateConsent:");
        String messageSignature = getSignature(PFX_FILE_PASS, pathToPfxFile, requestBody);
        System.out.println("messageSignature:"+ messageSignature);
        //String messageSignature  = "WhRba+vyCBW/nfrva+6sqFsPXf2hv+UEDqYLenjThsSryR4O1BLJSi1fuuQFyLokzINd8lhbm69ekrKFmd+3V6Ao0boMeVYePHRha1VH3IyyA6j10cfA2RBug3aWAjceKqTwHQUWE60Ap1jXnW3CgEGOgbqmE/U4iP5dJ1oj/RXHXba+B6YPCEjb/kqAjLxY61dMJwrVfYuDxMrAanE5d7K+RMYlyEYjVGcE2mMaICWtGy7IqcpTI0hUmhxNFI4pqcBeNi/GWSFJOOgC4+94l40hShZnK9ZJpipj9/e79t+4uldjXoL3xwFHD2EeS0hludGnWGfg0DsivEegdrzGfw==";
             // Parse the requestBody to get the direction value
        JsonObject requestBodyJson = gson.fromJson(requestBody, JsonObject.class);
        String direction = requestBodyJson.get("direction").getAsString();

        Request request = new Request.Builder()
                .url(NPIX_URL + "consent/update")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + bearerToken)
                .addHeader("Message-Signature", messageSignature)
                .build();

        JsonObject returnJson = new JsonObject();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
           // String logresponse = wsNpixApiAuditLog(Vpa, requestBody, messageSignature, responseBody, "update_consent");
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            returnJson.addProperty("statusCode", response.code());
            returnJson.addProperty("responseCode", responseJson.get("responseCode").getAsString());
            returnJson.addProperty("responseMessage", responseJson.get("responseMessage").getAsString());
            
            status = (response.isSuccessful()) ? "success" : "failed";

			recordCrossBorderAPILog(dc_request, Vpa, messageSignature, requestBody, responseBody, status,
					"update_consent");

            if (response.isSuccessful()) {
                JsonObject responseData = responseJson.getAsJsonObject("responseData");

                JsonObject parsedResponseData = new JsonObject();
                parsedResponseData.addProperty("vpaId", responseData.get("vpaId").getAsString());
                parsedResponseData.addProperty("direction", responseData.get("direction").getAsString());
                parsedResponseData.addProperty("instrument", responseData.get("instrument").getAsString());
                parsedResponseData.addProperty("consent", responseData.get("consent").getAsString());
                parsedResponseData.addProperty("uniqueTransactingId", responseData.get("uniqueTransactingId").getAsString());
                //String updateresponse = wsNpixUpdateVPAStatus(Vpa,responseData.get("consent").getAsString() , "CONSENT_UPDATE",direction);
                returnJson.addProperty("consent", responseData.get("consent").getAsString());
                returnJson.add("responseData", parsedResponseData);
                JsonArray emptyArray = new JsonArray();
                returnJson.add("responseErrors", emptyArray);
            } else {
                // Handle error response
                JsonArray emptyArray = new JsonArray();
                JsonObject parsedResponseData = new JsonObject();
                parsedResponseData.addProperty("vpaId", "");
                parsedResponseData.addProperty("direction", "");
                parsedResponseData.addProperty("instrument", "");
                parsedResponseData.addProperty("consent", "");
                parsedResponseData.addProperty("uniqueTransactingId", "");
                returnJson.add("responseErrors", emptyArray);
            }
        } catch (IOException e) {
            // Handle exceptions
            returnJson.addProperty("statusCode", 0); // Set your custom error code here
            returnJson.addProperty("responseCode", -1); // Set your custom error code here
            returnJson.addProperty("responseMessage", "An error occurred."); // Set your custom error message hereM
            JsonObject parsedResponseData = new JsonObject();
            parsedResponseData.addProperty("vpaId", "");
            parsedResponseData.addProperty("direction", "");
            parsedResponseData.addProperty("instrument", "");
            parsedResponseData.addProperty("consent", "");
            parsedResponseData.addProperty("uniqueTransactingId", "");
            returnJson.add("responseErrors", new JsonArray());
        }

        // Return the JSON string
        return returnJson.toString();
    }

    /* METHOD FOR TRANSACTIONAL API */
    public static String limitCheck(DataControllerRequest dc_request, String PFX_FILE_PASS, String NPIX_URL, String bearerToken, String pathToPfxFile,
            String Vpa, String limitCheckInfo) throws Exception {
        MediaType mediaType = MediaType.parse("application/json");

        String requestBody = limitCheckInfo;
        String status = "";
        RequestBody body = RequestBody.create(mediaType, requestBody);
        String messageSignature = getSignature(PFX_FILE_PASS, pathToPfxFile, requestBody);
        
        //String messageSignature = "q+Kb3kPl+DnK0Z61c4zakzjrhZv9hSxJVUCjV6dlKnNQG4r8KKxJ1vJ14h+1ucwMga4h92HEVMPoGHlMUp1rfhmD7AtAghMtWxBHTk7k229emh+Ik+aX/XhoSoglQJ1WUHEIYXREUvGlp6mXOkaZzikZ3KakpppB5LADz9R7hJfuQOfZnoqAOhxzq1isjMRnK4DwH5+JJt57tzZvrW7bHRtQ1m72cBUdZw0Hg4gDZWwUDtmb+BigN6SwEazBHxBfyoBcDxM/eL+KL19wLrMyEaYhRH5BxH5NR/TWhOIrZJN7mBov7Z3inbSCN8slL90LVOjdJ5S2ymtzCSAXG357ng==";

        Request request = new Request.Builder()
                .url(NPIX_URL + "check-limit")
                .method("POST", body)
                .addHeader("Message-Signature", messageSignature)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        logger.debug("HBL responseBody###"+responseBody);
        JsonObject tokenResponse = gson.fromJson(responseBody, JsonObject.class);
       
        //String logresponse = wsNpixApiAuditLog(Vpa, requestBody, messageSignature, responseBody, "limit_check");
        status = (response.isSuccessful()) ? "success" : "failed";

		recordCrossBorderAPILog(dc_request, Vpa, messageSignature, requestBody, responseBody, status,
				"limit_check");

        if (response.isSuccessful()) {
        	logger.debug("HBL if ###"+tokenResponse.toString());
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);
            JsonObject chargeData = null;
            JsonObject limitData = null;
            JsonObject returnJson = new JsonObject();
            JsonObject responseData = responseJson.getAsJsonObject("responseData");
            returnJson.addProperty("statusCode", response.code());
            returnJson.addProperty("responseCode", responseJson.get("responseCode").getAsString());
            returnJson.addProperty("responseMessage", responseJson.get("responseMessage").getAsString());
            logger.debug("HBL responseMessage ###"+responseJson.get("responseMessage").getAsString());
            logger.debug("HBL responseCode ###"+responseJson.get("responseCode").getAsString());
            logger.debug("HBL charge ###"+responseData.get("charge"));

            
			if (!responseData.get("charge").isJsonNull() && !responseData.get("limit").isJsonNull()) {
				chargeData = responseData.getAsJsonObject("charge");
				limitData = responseData.getAsJsonObject("limit");

				returnJson.add("chargeDetails", chargeData.getAsJsonArray("chargeDetails"));
				returnJson.addProperty("calculatedCharge", chargeData.get("calculatedCharge").getAsDouble());
				returnJson.addProperty("dailyAvailableLimitCount",
						limitData.getAsJsonObject("dailyAvailableLimitDetails").get("count").getAsInt());
				returnJson.addProperty("dailyAvailableLimitAmount",
						limitData.getAsJsonObject("dailyAvailableLimitDetails").get("amount").getAsDouble());
				returnJson.addProperty("dailyConsumedLimitCount",
						limitData.getAsJsonObject("dailyConsumedLimitDetails").get("count").getAsInt());
				returnJson.addProperty("dailyConsumedLimitAmount",
						limitData.getAsJsonObject("dailyConsumedLimitDetails").get("amount").getAsDouble());
				returnJson.addProperty("limitExceeded", limitData.get("limitExceeded").getAsBoolean());
				returnJson.addProperty("exemptedTxn", limitData.get("exemptedTxn").getAsBoolean());
			}
            //System.out.println("Response JSON: " + returnJson.toString());
            return returnJson.toString();
        } else {
        	logger.debug("HBL else ###"+tokenResponse.toString());
            String responseCode = tokenResponse.get("responseCode").getAsString();
            String responseMessage = tokenResponse.get("responseMessage").getAsString();
            int statusCode = response.code();
            JsonArray emptyArray = new JsonArray();

            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("statusCode", statusCode);
            errorJson.addProperty("responseCode", responseCode);
            errorJson.addProperty("responseMessage", responseMessage);
            errorJson.add("chargeDetails", emptyArray);
            errorJson.addProperty("calculatedCharge", "");
            errorJson.addProperty("dailyAvailableLimitCount", "");
            errorJson.addProperty("dailyAvailableLimitAmount", "");
            errorJson.addProperty("dailyConsumedLimitCount", "");
            errorJson.addProperty("dailyConsumedLimitAmount", "");
            errorJson.addProperty("limitExceeded", "");
            errorJson.addProperty("exemptedTxn", "");

            //System.out.println("Error JSON: " + errorJson.toString());
            return errorJson.toString();
        }
    }

    public static String validateCustomer(DataControllerRequest dc_request, String PFX_FILE_PASS, String NPIX_URL, String bearerToken,
            String pathToPfxFile, String vpa, String transactionInfo) throws Exception {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, transactionInfo);
        String status = "";
        String messageSignature = getSignature(PFX_FILE_PASS, pathToPfxFile, transactionInfo);
        logger.debug("messageSignature##:"+ messageSignature);
        //String messageSignature = "qmfkhVf+i+2Slip1CxCDgzhQ2RiShsIgTm1cUdPk4RktcqAfTRGnUOLvrcu3Sh4YQnl7pV3BUwtRLJ/Ok35UGpEb0dPp+b3A9s8YeffiDkpYekHuad2XnBboKQOnVZhMvdQNWVofOv8lsP1uIrC3iimQ5I6TXmTGdxEWHuQ3kMtyzi3mw8Rs/Ht4bVlRe5sEZMBFdCvs56Hk8WACGs4xUCogyt2LK5Jud2AEUIuottjjjmdYE7rScjfbzxIyEnxoFEt05Tahxwf3MO1bQzor5yPaaDztfR/QN/QO8QolqgQmsXL209M4q+2vkKo45GA4Djpkyf+fI9DTcsnmPIUwYA==";

        Request request = new Request.Builder()
                .url(NPIX_URL + "validate-customer")
                .method("POST", body)
                .addHeader("Message-Signature", messageSignature)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + bearerToken)
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        
        logger.debug("responseBody##:"+ responseBody);
        JsonObject tokenResponse = gson.fromJson(responseBody, JsonObject.class);

        //String logresponse = wsNpixApiAuditLog(vpa, requestBody, messageSignature, responseBody, "validate_customer");
        status = (response.isSuccessful()) ? "success" : "failed";

		recordCrossBorderAPILog(dc_request, vpa, messageSignature, transactionInfo, responseBody, status,
				"validate_customer");

        if (response.isSuccessful()) {
            JsonObject responseData = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("responseData");

            String orgRequestUniqueId = responseData.get("orgRequestUniqueId").getAsString();
            String endToEndId = responseData.get("endToEndId").getAsString();

            double amount = responseData.get("amount").getAsDouble();
            double chargeAmount = responseData.get("chargeAmount").getAsDouble();

            JsonObject returnJson = new JsonObject();
            returnJson.addProperty("statusCode", response.code());
            returnJson.addProperty("responseCode", tokenResponse.get("responseCode").getAsString());
            returnJson.addProperty("responseMessage", tokenResponse.get("responseMessage").getAsString());
            returnJson.addProperty("orgRequestUniqueId", orgRequestUniqueId);
            returnJson.addProperty("endToEndId", endToEndId);
            returnJson.addProperty("amount", amount);
            returnJson.addProperty("chargeAmount", chargeAmount);
            return returnJson.toString();
        } else {
        	String responseMessage = "";
            String responseCode = tokenResponse.get("responseCode").getAsString();
            
            if(tokenResponse.has("responseMessage") && !tokenResponse.get("responseMessage").isJsonNull()){
            	responseMessage = tokenResponse.get("responseMessage").getAsString();
            }else if(tokenResponse.has("fieldErrors")) {
            	logger.debug("fieldErrors##:"+ tokenResponse.get("fieldErrors").getAsJsonArray());
            	String message = null;
                JsonArray lineItems = tokenResponse.getAsJsonArray("fieldErrors");
                for (int i = 0; i < lineItems.size(); ++i) {
                    JsonElement rec = lineItems.get(i);
                    logger.debug("JsonElement ##"+ rec.toString());
                    message = rec.toString();
                }
                responseMessage = message;
            }
            
            
            
            int statusCode = response.code();

            JsonObject errorJson = new JsonObject();
            errorJson.addProperty("statusCode", statusCode); // Include the status code
            errorJson.addProperty("responseCode", responseCode);
            errorJson.addProperty("responseMessage", responseMessage);
            errorJson.addProperty("orgRequestUniqueId", "");
            errorJson.addProperty("endToEndId", "");
            errorJson.addProperty("amount", "");
            errorJson.addProperty("chargeAmount", "");
            return errorJson.toString();
        }
    }
    

    public static String makePaymentRequest(DataControllerRequest dc_request, String PFX_FILE_PASS, String NPIX_URL, String bearerToken,
            String pathToPfxFile, String paymentRequest) throws IOException {

        MediaType mediaType = MediaType.parse("application/json");
        String jsonBody = paymentRequest;
        String status = "";
        RequestBody body = RequestBody.create(mediaType, jsonBody);

        try {
            String messageSignature = getSignature(PFX_FILE_PASS, pathToPfxFile, jsonBody);

            Request request = new Request.Builder()
                    .url(NPIX_URL + "payment")
                    .method("POST", body)
                    .addHeader("Message-Signature", messageSignature)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + bearerToken)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                String responseBody = response.body().string();
                JsonObject tokenResponse = gson.fromJson(responseBody, JsonObject.class);
                int statusCode = response.code();
                //String logresponse = wsNpixApiAuditLog(vpa, jsonBody, messageSignature, responseBody, "paymeent_request");
                status = (response.isSuccessful()) ? "success" : "failed";

        		recordCrossBorderAPILog(dc_request, " ", messageSignature, paymentRequest, responseBody, status,
        				"paymeent_request");

                if (response.isSuccessful()) {
                    JsonObject responseData = gson.fromJson(responseBody, JsonObject.class).getAsJsonObject("responseData");
                    JsonObject payerDetail = responseData.getAsJsonObject("payerDetail");
                    JsonObject payeeDetail = responseData.getAsJsonObject("payeeDetail");
                    JsonObject payeraccountDetail = payerDetail.getAsJsonObject("accountDetail");
                    JsonObject payeeaccountDetail = payeeDetail.getAsJsonObject("accountDetail");

                    String responseCode = tokenResponse.get("responseCode").getAsString();
                    String responseMessage = tokenResponse.get("responseMessage").getAsString();
                    String orgRequestUniqueId = responseData.get("orgRequestUniqueId").getAsString();
                    String endToEndId = responseData.get("endToEndId").getAsString();
                    BigDecimal amount = responseData.get("amount").getAsBigDecimal();
                    String paymentStatus = responseData.get("paymentStatus").getAsString();
                    String debitStatus = responseData.get("debitStatus") != null && !responseData.get("debitStatus").isJsonNull() ? responseData.get("debitStatus").getAsString() : "";
                    String creditStatus = responseData.get("creditStatus") != null && !responseData.get("creditStatus").isJsonNull() ? responseData.get("creditStatus").getAsString() : "";
                    BigDecimal chargeAmount = responseData.get("chargeAmount").getAsBigDecimal();
                    BigDecimal conversionRate = responseData.get("conversionRate").getAsBigDecimal();
                    String purpose = responseData.get("purpose").getAsString();
                    String remarks = responseData.get("remarks").getAsString();
                    String currency = responseData.get("currency").getAsString();
                    String payerName = payerDetail.get("name") != null && !payerDetail.get("name").isJsonNull() ? payerDetail.get("name").getAsString() : "";
                    String payerVPA = payeraccountDetail.get("vpa") != null && !payeraccountDetail.get("vpa").isJsonNull() ? payeraccountDetail.get("vpa").getAsString() : "";
                    String payerAccountNumber = payeraccountDetail.get("accountNumber") != null && !payeraccountDetail.get("accountNumber").isJsonNull() ? payeraccountDetail.get("accountNumber").getAsString() : "";
                    String payerAccountType = payeraccountDetail.get("accountType") != null && !payeraccountDetail.get("accountType").isJsonNull() ? payerDetail.get("accountType").getAsString() : "";
                    String payeeName = payeeDetail.get("name") != null && !payeeDetail.get("name").isJsonNull() ? payeeDetail.get("name").getAsString() : "";
                    String payeeVPA = payeeaccountDetail.get("vpa") != null && !payeeaccountDetail.get("vpa").isJsonNull() ? payeeaccountDetail.get("vpa").getAsString() : "";
                    String payeeAccountNumber = payeeaccountDetail.get("accountNumber") != null && !payeeaccountDetail.get("accountNumber").isJsonNull() ? payeeaccountDetail.get("accountNumber").getAsString() : "";
                    String rpsTransactionId = responseData.get("rpsTransactionId") != null && !responseData.get("rpsTransactionId").isJsonNull() ? responseData.get("rpsTransactionId").getAsString() : "";

                    JsonObject returnJson = new JsonObject();
                    returnJson.addProperty("statusCode", statusCode);
                    returnJson.addProperty("responseCode", responseCode);
                    returnJson.addProperty("responseMessage", responseMessage);
                    returnJson.addProperty("orgRequestUniqueId", orgRequestUniqueId);
                    returnJson.addProperty("endToEndId", endToEndId);
                    returnJson.addProperty("amount", amount);
                    returnJson.addProperty("chargeAmount", chargeAmount);
                    returnJson.addProperty("debitStatus", debitStatus);
                    returnJson.addProperty("creditStatus", creditStatus);

                    //String transactionlog = wsNpixTransactionLog(responseCode, responseMessage, orgRequestUniqueId, endToEndId, amount, paymentStatus, debitStatus, creditStatus, chargeAmount, conversionRate, purpose, remarks, currency, payerName, payerVPA, payerAccountNumber, payerAccountType, payeeName, payeeVPA, payeeAccountNumber, rpsTransactionId);

                    return returnJson.toString();
                } else {
                    String responseCode = tokenResponse.get("responseCode").getAsString();
                    String responseMessage = tokenResponse.get("responseMessage").getAsString();
                    JsonObject errorJson = new JsonObject();
                    errorJson.addProperty("statusCode", statusCode);
                    errorJson.addProperty("responseCode", responseCode);
                    errorJson.addProperty("responseMessage", responseMessage);
                    errorJson.addProperty("orgRequestUniqueId", "");
                    errorJson.addProperty("endToEndId", "");
                    errorJson.addProperty("amount", "");
                    errorJson.addProperty("chargeAmount", "");
                    errorJson.addProperty("debitStatus", "");
                    errorJson.addProperty("creditStatus", "");
                    return errorJson.toString();
                }
            }
        } catch (Exception e) {
            // Handle the exception here (e.g., log it or take appropriate action)
            e.printStackTrace();
            throw new IOException("An error occurred while generating the message signature.", e);
        }
    }

    /* METHOD FOR LOG API */
    private static String wsNpixApiAuditLog(String vpa, String request,
            String signature, String response, String apitype) {

        MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String soapRequest = String.format(
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:s=\"http://www.w3.org/2001/XMLSchema\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<Npix_ApiAuditLog xmlns=\"http://tempuri.org/\">"
                + "<vpa>%s</vpa>"
                + "<request>%s</request>"
                + "<signature>%s</signature>"
                + "<response>%s</response>"
                + "<apitype>%s</apitype>"
                + "<responsedate>%s</responsedate>"
                + "</Npix_ApiAuditLog>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>",
                vpa, request, signature, response, apitype, currentDateTime
        );

        RequestBody body = RequestBody.create(mediaType, soapRequest);

        Request requestObj = new Request.Builder()
                .url(wsLogUrl)
                .method("POST", body)
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .addHeader("SOAPAction", "http://tempuri.org/Npix_ApiAuditLog")
                .build();

        // Log the request details including the request body
    /*String requestDetails = String.format(
         "Request Details:\nMethod: %s\nURL: %s\nHeaders: %s\nBody: %s",
         requestObj.method(),
         requestObj.url(),
         requestObj.headers(),
         soapRequest  // Include the request body
         );
         System.out.println(requestDetails);
         */
        try {
            Response responseObj = client.newCall(requestObj).execute();

            if (responseObj.isSuccessful()) {
                String responseBody = responseObj.body().string();
                return responseBody;
            } else {
                return "Request failed with code: " + responseObj;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    private static String wsNpixTransactionLog(String responseCode,
            String responseMessage, String orgRequestUniqueId, String endToEndId,
            BigDecimal amount, String paymentStatus, String debitStatus,
            String creditStatus, BigDecimal chargeAmount,
            BigDecimal conversionRate, String purpose, String remarks,
            String currency, String payerName, String payerVPA,
            String payerAccountNumber, String payerAccountType, String payeeName,
            String payeeVPA, String payeeAccountNumber, String rpsTransactionId) {

        MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");

        String soapRequest = String.format(
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:s=\"http://www.w3.org/2001/XMLSchema\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<Npix_TransactionLog xmlns=\"http://tempuri.org/\">"
                + "<responseCode>%s</responseCode>"
                + "<responseMessage>%s</responseMessage>"
                + "<orgRequestUniqueId>%s</orgRequestUniqueId>"
                + "<endToEndId>%s</endToEndId>"
                + "<amount>%s</amount>"
                + "<paymentStatus>%s</paymentStatus>"
                + "<debitStatus>%s</debitStatus>"
                + "<creditStatus>%s</creditStatus>"
                + "<chargeAmount>%s</chargeAmount>"
                + "<conversionRate>%s</conversionRate>"
                + "<purpose>%s</purpose>"
                + "<remarks>%s</remarks>"
                + "<currency>%s</currency>"
                + "<payerName>%s</payerName>"
                + "<payerVPA>%s</payerVPA>"
                + "<payerAccountNumber>%s</payerAccountNumber>"
                + "<payerAccountType>%s</payerAccountType>"
                + "<payeeName>%s</payeeName>"
                + "<payeeVPA>%s</payeeVPA>"
                + "<payeeAccountNumber>%s</payeeAccountNumber>"
                + "<rpsTransactionId>%s</rpsTransactionId>"
                + "</Npix_TransactionLog>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>",
                responseCode, responseMessage, orgRequestUniqueId, endToEndId, amount, paymentStatus, debitStatus, creditStatus, chargeAmount, conversionRate, purpose, remarks, currency, payerName, payerVPA, payerAccountNumber, payerAccountType, payeeName, payeeVPA, payeeAccountNumber, rpsTransactionId
        );

        RequestBody body = RequestBody.create(mediaType, soapRequest);

        Request requestObj = new Request.Builder()
                .url(wsLogUrl)
                .method("POST", body)
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .addHeader("SOAPAction", "http://tempuri.org/Npix_TransactionLog")
                .build();

        try {
            Response responseObj = client.newCall(requestObj).execute();

            if (responseObj.isSuccessful()) {
                String responseBody = responseObj.body().string();
                return responseBody;
            } else {
                return "Request failed with code: " + responseObj;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
    
    
    
    public static String wsNpixUpdateVPAStatus(String vpa, String consent, String currentStatus,String direction) {
        MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");

        // Construct the SOAP request
        String soapRequest = String.format(
            "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://tempuri.org/\">"
            + "<soapenv:Header/>"
            + "<soapenv:Body>"
            + "<web:Npix_UpdateVPAStatus>"
            + "<web:vpa>%s</web:vpa>"
            + "<web:consent>%s</web:consent>"
            + "<web:currentStatus>%s</web:currentStatus>"
            + "<web:direction>%s</web:direction>"
            + "</web:Npix_UpdateVPAStatus>"
            + "</soapenv:Body>"
            + "</soapenv:Envelope>",
            vpa, consent, currentStatus,direction
        );

        RequestBody body = RequestBody.create(mediaType, soapRequest);

        Request requestObj = new Request.Builder()
            .url(wsLogUrl)
            .method("POST", body)
            .addHeader("Content-Type", "text/xml; charset=utf-8")
            .addHeader("SOAPAction", "http://tempuri.org/Npix_UpdateVPAStatus")
            .build();

        try {
            Response responseObj = client.newCall(requestObj).execute();

            if (responseObj.isSuccessful()) {
                String responseBody = responseObj.body().string();
                return responseBody;
            } else {
                return "Request failed with code: " + responseObj;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }
    
    public static boolean recordCrossBorderAPILog(DataControllerRequest request, String vpa, String signature, String requestbody,
    		String responsebody, String status, String servicetype)
			throws HttpCallException {
		boolean isinvalidattemptEntryMade = false;
		try {
			ServicesManager servicesManager;
			servicesManager = request.getServicesManager();
			long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
			String id = "REQ" + String.valueOf(number);
			Map<String, Object> inputParams = new HashMap<>();
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			String customerID = (String) servicesManager.getIdentityHandler().getUserAttributes().get("customer_id");
			String userSignOnName = (String) CustomerSessionsUtil.getLoggedInUserAttributesMap(request).get("UserName");

			logger.debug("recordCrossBorderAPILog vpa" + vpa);
			logger.debug("recordCrossBorderAPILog signature" + signature);
			logger.debug("recordCrossBorderAPILog requestbody" + requestbody);
			logger.debug("recordCrossBorderAPILog responsebody" + responsebody);
			logger.debug("recordCrossBorderAPILog status" + status);
			logger.debug("recordCrossBorderAPILog servicetype" + servicetype);

			inputParams.put("id", id);
			inputParams.put("Customer_id", customerID);
			inputParams.put("username", userSignOnName);
			inputParams.put("core_identifier", coreIdentifier);
			inputParams.put("vpa", vpa);
			inputParams.put("messageSignature", signature);
			inputParams.put("requestBody", requestbody);
			inputParams.put("responseBody", responsebody);
			inputParams.put("status", status);
			inputParams.put("serviceType", servicetype);
			inputParams.put("createdby", getTimestamp());
			inputParams.put("modifiedby", coreIdentifier);
			inputParams.put("createdts", getTimestamp());
			inputParams.put("lastmodifiedts", getTimestamp());
			inputParams.put("synctimestamp", getTimestamp());
			inputParams.put("softdeleteflag", "");

			logger.debug("BCT::recordCrossBorderAPILog: inputParams:" + inputParams.toString());

			String dbResponse = DBPServiceExecutorBuilder.builder().withOperationId(HBLURLConstants.CROSSBORDER_API_LOG_CREATE)
					.withRequestParameters(inputParams).withServiceId(HBLURLConstants.TRANSACTIONPIN_SERVICE)
					.withRequestHeaders(request.getHeaderMap()).build().getResponse();
			logger.debug("BCT::recordCrossBorderAPILog: response:" + dbResponse);
			JSONObject responseJSON = new JSONObject(dbResponse);
			if (responseJSON.has("errmsg")) {
				isinvalidattemptEntryMade = false;
				logger.debug("BCT::recordCrossBorderAPILog failure:");
			} else {
				isinvalidattemptEntryMade = true;
				logger.debug("BCT::recordCrossBorderAPILog success:");
			}
		} catch (Exception e) {
			logger.debug("Couldn't create recordCrossBorderAPILog");
			return false;
		}

		return isinvalidattemptEntryMade;
	}

    public static String getTimestamp() {
		String localDateTime;
		if (LocalDateTime.now().getSecond() == 0) {
			localDateTime = LocalDateTime.now().plusSeconds(1).withNano(0).toString();
		} else {
			localDateTime = LocalDateTime.now().withNano(0).toString();
		}
		return localDateTime;
	}
}
