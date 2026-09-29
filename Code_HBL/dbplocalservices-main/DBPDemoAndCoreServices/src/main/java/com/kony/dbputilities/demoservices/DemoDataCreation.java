package com.kony.dbputilities.demoservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.http.HttpConnector;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;

public class DemoDataCreation {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DEMODATA = "demodata";
    static final String DBPACCAGG = "dbpaccountsaggregation";

    public DemoDataCreation(DataControllerRequest dcRequest, String mainUser) {

        Connection demoDataConn = null;
        Connection dbpAccAggConn = null;

        Statement demoDataStatment = null;

        Map<String, String> data = new HashMap<>();

        try {
            Class.forName(JDBC_DRIVER);

            demoDataConn = DriverManager.getConnection(
                    URLFinder.getPathUrl(URLConstants.AG_DB_URL, dcRequest) + DEMODATA,
                    URLFinder.getPathUrl(URLConstants.AG_USER, dcRequest),
                    URLFinder.getPathUrl(URLConstants.AG_PASS, dcRequest));
            dbpAccAggConn = DriverManager.getConnection(
                    URLFinder.getPathUrl(URLConstants.AG_DB_URL, dcRequest) + DBPACCAGG,
                    URLFinder.getPathUrl(URLConstants.AG_USER, dcRequest),
                    URLFinder.getPathUrl(URLConstants.AG_PASS, dcRequest));

            demoDataStatment = demoDataConn.createStatement();
            dbpAccAggConn.setAutoCommit(false);

            String loginInsertStatement = "INSERT INTO externalbankidentity " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";

            String accountsInsertStatement = "INSERT INTO accounts (Account_id,AccountName,UserName,"
                    + "ExternalBankIdentity_id,CurrencyCode,AvailableBalance,AccountHolder,Address,Scheme,"
                    + "Number,error,Type_id,NickName,FavouriteStatus,LastUpdated,InternalAccount)"
                    + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            if (!isNewUser(dbpAccAggConn, mainUser, dcRequest)) {
                populateLoginData(dcRequest, dbpAccAggConn, loginInsertStatement, demoDataStatment, data, mainUser);
                populateAccountsData(dcRequest, dbpAccAggConn, demoDataStatment, data, accountsInsertStatement,
                        mainUser);

                demoDataStatment.close();
                demoDataConn.close();
                dbpAccAggConn.close();
            }

        } catch (SQLException se) {
            alert.prepareError(se.getMessage()).log();
        } catch (Throwable e) {
            alert.prepareError(e.getMessage()).log();
        } finally {
            try {
                if (demoDataStatment != null) {
                    demoDataStatment.close();
                }
            } catch (SQLException se2) {
            }

            try {
                if (demoDataConn != null) {
                    demoDataConn.close();
                }
                if (dbpAccAggConn != null) {
                    dbpAccAggConn.close();
                }
            } catch (Throwable se) {
            }

        }
    }

    private static boolean isNewUser(Connection dbpAccAggConn, String mainUser, DataControllerRequest dcRequest)
            throws SQLException {

        boolean flag = false;
        String externalBankIdentityReadStatement = "SELECT * FROM "
                + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
                + ".externalbankidentity WHERE MainUser_id=?";
        ResultSet res = null;
        try (PreparedStatement dbpPreparedStatement = dbpAccAggConn
                .prepareStatement(externalBankIdentityReadStatement)) {
            dbpPreparedStatement.setString(1, mainUser);
            res = dbpPreparedStatement.executeQuery();
            while (res.next()) {
                flag = true;
            }
            return flag;
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        } finally {
            if (null != res) {
                res.close();
            }
        }
        return flag;
    }

    private static void populateAccountsData(DataControllerRequest dcRequest, Connection dbpAccAggConn,
            Statement demoDataStatment, Map<String, String> data, String accountsInsertStatement, String mainUser)
            throws SQLException, HttpCallException {

        String accountsdataQuery = "SELECT * FROM " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
                + ".accounts";
        try (PreparedStatement dbpPreparedStatement = dbpAccAggConn.prepareStatement(accountsInsertStatement);
                ResultSet accountsResult = demoDataStatment.executeQuery(accountsdataQuery)) {

            addAccountsDataToBatch(dcRequest, data, dbpPreparedStatement, accountsResult, mainUser);
            dbpPreparedStatement.executeBatch();
            accountsResult.close();
            dbpAccAggConn.commit();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
    }

    private static void addAccountsDataToBatch(DataControllerRequest dcRequest, Map<String, String> data,
            PreparedStatement dbpPreparedStatement, ResultSet accountsResult, String mainUser)
            throws SQLException, HttpCallException {
        while (accountsResult.next()) {

            String id = getNextNumber(dcRequest, "ACCOUNTID");
            String Account_id = id.toString();
            String AccountName = accountsResult.getString("AccountName");
            String UserName = mainUser;
            String BankName = accountsResult.getString("BankId");
            String username = accountsResult.getString("UserName");
            String tokenId = getToken(username, BankName, data);
            String CurrencyCode = accountsResult.getString("CurrencyCode");
            String AvailableBalance = accountsResult.getString("AvailableBalance");
            String AccountHolder = accountsResult.getString("AccountHolder");
            String Scheme = accountsResult.getString("Scheme");
            String Address = accountsResult.getString("Address");
            String Number = accountsResult.getString("Number");
            String error = null;
            String Type_id = accountsResult.getString("Type_id");
            String NickName = accountsResult.getString("Nickname");
            boolean FavouriteStatus = accountsResult.getBoolean("FavouriteStatus");
            String LastUpdated = getLastUpdatedTime();
            boolean InternalAccount = false;

            dbpPreparedStatement.setString(1, Account_id);
            dbpPreparedStatement.setString(2, AccountName);
            dbpPreparedStatement.setString(3, UserName);
            dbpPreparedStatement.setString(4, tokenId);
            dbpPreparedStatement.setString(5, CurrencyCode);
            dbpPreparedStatement.setString(6, AvailableBalance);
            dbpPreparedStatement.setString(7, AccountHolder);
            dbpPreparedStatement.setString(8, Address);
            dbpPreparedStatement.setString(9, Scheme);
            dbpPreparedStatement.setString(10, Number);
            dbpPreparedStatement.setString(11, error);
            dbpPreparedStatement.setString(12, Type_id);
            dbpPreparedStatement.setString(13, NickName);
            dbpPreparedStatement.setBoolean(14, FavouriteStatus);
            dbpPreparedStatement.setString(15, LastUpdated);
            dbpPreparedStatement.setBoolean(16, InternalAccount);
            dbpPreparedStatement.addBatch();
        }
    }

    private static String getToken(String userName, String bankName, Map<String, String> data) {
        String key = userName + "_" + bankName;
        return data.get(key);
    }

    private static void populateLoginData(DataControllerRequest dcRequest, Connection dbpAccAggConn,
            String loginInsertStatement, Statement demoDataStatment, Map<String, String> data, String mainUser)
            throws Exception {
        try (PreparedStatement dbpPreparedStatement = dbpAccAggConn.prepareStatement(loginInsertStatement)) {

            String loginDataQuery = "SELECT * from " + URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest)
                    + ".login";
            ResultSet loginResult = demoDataStatment.executeQuery(loginDataQuery);

            addLoginDataToBatch(dcRequest, data, dbpPreparedStatement, loginResult, mainUser);

            dbpPreparedStatement.executeBatch();

            loginResult.close();
            dbpAccAggConn.commit();
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        }
    }

    private static void addLoginDataToBatch(DataControllerRequest dcRequest, Map<String, String> data,
            PreparedStatement dbpPreparedStatement, ResultSet loginResult, String mainUser) throws Exception {
        while (loginResult.next()) {
            String id = getNextNumber(dcRequest, "ExternalBankIdentity");
            Integer ExternalBank_id = loginResult.getInt("bankId");
            String MainUser_id = mainUser;
            String User_id = loginResult.getString("username");
            String Password = loginResult.getString("password");
            String SessionToken = CryptoText.encrypt(getTokenByLogin(User_id, Password));
            String createdby = "";
            String modifiedby = "";
            Timestamp createdts = getTimeStamp();
            Timestamp lastmodifiedts = getTimeStamp();
            Timestamp synctimestamp = getTimeStamp();
            boolean softdeleteflag = false;
            String key = User_id + "_" + ExternalBank_id;

            data.put(key, id.toString());

            dbpPreparedStatement.setString(1, id);
            dbpPreparedStatement.setInt(2, ExternalBank_id);
            dbpPreparedStatement.setString(3, MainUser_id);
            dbpPreparedStatement.setString(4, User_id);
            dbpPreparedStatement.setString(5, CryptoText.encrypt(Password));
            dbpPreparedStatement.setString(6, SessionToken);
            dbpPreparedStatement.setString(7, createdby);
            dbpPreparedStatement.setString(8, modifiedby);
            dbpPreparedStatement.setTimestamp(9, createdts);
            dbpPreparedStatement.setTimestamp(10, lastmodifiedts);
            dbpPreparedStatement.setTimestamp(11, synctimestamp);
            dbpPreparedStatement.setBoolean(12, softdeleteflag);
            dbpPreparedStatement.addBatch();
        }
    }

    private static String getTokenByLogin(String user_id, String password) throws HttpCallException {
        HttpConnector conn = new HttpConnector();
        JsonObject response = new JsonObject();
        String url = "https://apisandbox.openbankproject.com/my/logins/direct";
        Map<String, String> inputParams = new HashMap<>();
        Map<String, String> headerParams = new HashMap<>();
        headerParams.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        String auth = "DirectLogin username=\"" + user_id + "\"" + "," + "password" + "=" + "\"" + password + "\"" + ","
                + "consumer_key" + "=" + "\"" + "5u3huxx2ti4il5yvldxas3jq5pvyvdfg4s3ztd5z" + "\"";
        headerParams.put("Authorization", auth);
        response = conn.invokeHttpPost(url, inputParams, headerParams);
        String token = response.getAsJsonObject().get("token").getAsString();
        return token;
    }

    private static String getNextNumber(DataControllerRequest dcRequest, String ObjectID) throws HttpCallException {
        JsonObject response = new JsonObject();
        Map<String, String> inputParams = new HashMap<>();
        Map<String, String> headerParams = new HashMap<>();
        inputParams.put("ObjectId", ObjectID);
        headerParams.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        response = HelperMethods.callApiJson(dcRequest, inputParams, headerParams, URLConstants.SET_NEXT_NUMBER);
        JsonElement id = response.getAsJsonArray("numberrange").get(0);
        ObjectID = id.getAsJsonObject().get("CurrentValue").toString();
        ObjectID = ObjectID.substring(1, ObjectID.length() - 1);
        return ObjectID;
    }

    private static Timestamp getTimeStamp() {
        long time = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(time);
        return timestamp;
    }

    private static String getLastUpdatedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");

        return formatter.format(new Date());
    }

    public Map<String, String> getAccountData(Map<String, String> inputParams) {
        inputParams.put("Account_id", inputParams.get("id"));
        inputParams.remove("id");
        String accHolderName = "{\"fullname\": \"" + inputParams.get("firstName") + " " + inputParams.get("lastName")
                + "\"}";
        inputParams.put("AccountHolder", accHolderName);
        Map<String, String> inputParam = new HashMap<>();
        inputParam.put("Name", "Savings");
        inputParam.put("OpeningDate", new Date().toString());
        inputParam.put("StatusDesc", "Active");
        inputParam.put("FavouriteStatus", "");
        inputParam.put("PendingDeposit", "500");
        inputParam.put("PendingWithdrawal", "0");
        inputParam.put("ShowTransactions", "1");
        inputParam.put("SupportBillPay", "1");
        inputParam.put("SupportDeposit", "1");
        inputParam.put("SupportTransferFrom", "1");
        inputParam.put("SupportTransferTo", "1");
        inputParam.put("SupportCardlessCash", "1");
        inputParam.put("TransactionLimit", "500");
        inputParam.put("CurrencyCode", "EUR");
        return inputParam;
    }
}