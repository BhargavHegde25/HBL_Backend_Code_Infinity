package com.kony.dbputilities.fileutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.kony.dbputilities.qfxbeans.AvailableBalance;
import com.kony.dbputilities.qfxbeans.BankAccountFrom;
import com.kony.dbputilities.qfxbeans.BankMessageService;
import com.kony.dbputilities.qfxbeans.BankMessageSet;
import com.kony.dbputilities.qfxbeans.BankMessageSetV;
import com.kony.dbputilities.qfxbeans.BankTransactionList;
import com.kony.dbputilities.qfxbeans.FinancialInstitution;
import com.kony.dbputilities.qfxbeans.LedgerBalance;
import com.kony.dbputilities.qfxbeans.OFX;
import com.kony.dbputilities.qfxbeans.QFXHelper;
import com.kony.dbputilities.qfxbeans.SignON;
import com.kony.dbputilities.qfxbeans.SignonMessageService;
import com.kony.dbputilities.qfxbeans.Statement;
import com.kony.dbputilities.qfxbeans.StatementWraper;
import com.kony.dbputilities.qfxbeans.Status;
import com.kony.dbputilities.qfxbeans.Transaction;
import com.kony.dbputilities.util.HelperMethods;

public class QFXGenerator implements FileGenerator {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private long intuitBuid;
    private String userName;
    private long financeId;
    private String orgName;
    private Map<String, String> account;
    private Map<String, String> transactionTypes;
    private Map<String, String> accountTypes;

    private ArrayList<Transaction> getTransactions(JsonArray transactionsData) throws ParseException {
        ArrayList<Transaction> allTransactions = new ArrayList<>();
        for (int i = 0; i < transactionsData.size(); i++) {
            JsonObject currTransaction = transactionsData.get(i).getAsJsonObject();
            allTransactions.add(getTransaction(currTransaction));
        }
        return allTransactions;
    }

    private BankAccountFrom getBankAccountFrom() {
        BankAccountFrom bankAccount = new BankAccountFrom();
        bankAccount.setAccountId(this.account.get(QFXHelper.ACCT_ID));
        bankAccount.setAccountType(this.accountTypes.get((this.account.get(QFXHelper.ACCT_TYPE))));
        bankAccount.setBankId(convertToLong(this.account.get(QFXHelper.BANK_ID)));
        return bankAccount;
    }

    private AvailableBalance getAvailableBalance(Date today) {
        AvailableBalance balance = new AvailableBalance();
        balance.setBalanceAmount(convertToDouble(this.account.get(QFXHelper.AVAILABLE_BAL)));
        balance.setBalanceDate(HelperMethods.toQFXDateTime(today));
        return balance;
    }

    private LedgerBalance getLedgerBalance(Date today) {
        LedgerBalance lbalance = new LedgerBalance();
        lbalance.setLedgerBalanceAmount(convertToDouble(this.account.get(QFXHelper.CURR_BAL)));
        lbalance.setLedgerBalanceDate(HelperMethods.toQFXDateTime(today));
        return lbalance;
    }

    private String getQFXTransactionType(String dbxTransactionType) {
        String transactionType = "DEP";
        if (StringUtils.isNotBlank(transactionTypes.get(dbxTransactionType))) {
            transactionType = transactionTypes.get(dbxTransactionType);
        }
        return transactionType;
    }

    private Transaction getTransaction(JsonObject currObject) throws ParseException {
        Transaction transaction = new Transaction();
        transaction.setAmount(currObject.get(QFXHelper.AMOUNT).getAsDouble());
        transaction.setTransactionId(currObject.get(QFXHelper.TRANS_ID).getAsString());
        transaction.setTransactionPostedDate(HelperMethods.toOFXDateTime(HelperMethods
                .convertDateFormat(currObject.get(QFXHelper.TRANS_DATE).getAsString(), "yyyy-MM-dd HH:mm:ss")));
        transaction.setUserInitiatedTransactionDate(transaction.getTransactionPostedDate());
        transaction.setTransactionType(getQFXTransactionType(currObject.get(QFXHelper.TRANS_TYPE).getAsString()));
        if (currObject.has(QFXHelper.TO_ACCT_NAME)) {
            transaction.setName(currObject.get(QFXHelper.TO_ACCT_NAME).getAsString());
        } else if (currObject.has(QFXHelper.PAYEE_NAME)) {
            transaction.setName(currObject.get(QFXHelper.PAYEE_NAME).getAsString());
        }
        if (null != currObject.get(QFXHelper.DESCRIPTION)) {
            transaction.setMemo(currObject.get(QFXHelper.DESCRIPTION).getAsString());
        }
        if (null != currObject.get(QFXHelper.CHK_NUM)) {
            transaction.setCheckNum(currObject.get(QFXHelper.CHK_NUM).getAsString());
        }
        return transaction;
    }

    private Map<String, String> getAccountsMap(JsonArray accounts) {
        if (null != accounts && accounts.size() > 0) {
            return jsonToMap(accounts.get(0).getAsJsonObject());
        }
        return new HashMap<>();
    }

    private Map<String, String> jsonToMap(JsonObject jobject) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jobject.entrySet()) {
            if (null != entry.getValue() && !(entry.getValue().isJsonObject() || entry.getValue().isJsonArray())) {
                map.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return map;
    }

    private OFX generateOFXObject(JsonArray transactions, String startDate, String endDate) throws ParseException {
        Date trxStartDate = null;
        Date trxEndDate = null;
        if (StringUtils.isNotBlank(startDate)) {
            trxStartDate = HelperMethods.getFormattedTimeStamp(startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            trxEndDate = HelperMethods.getFormattedTimeStamp(endDate);
        }
        OFX ofx = new OFX();
        Status serverStatus = getServerStatus();
        SignON signon = getSignOn(serverStatus);
        SignonMessageService signOnMsgSrvc = getSignOnMsgService(signon);
        ofx.setSignonMessageService(signOnMsgSrvc);
        if (isAccountTypeValid()) {
            BankMessageService bankMsgSrvc = getBankMsgService(transactions, trxStartDate, trxEndDate, serverStatus);
            ofx.setBankingMessageService(bankMsgSrvc);
        } else {
            BankMessageSetV msgSetV = new BankMessageSetV();
            msgSetV.setMessageStore("Invalid account type :" + account.get(QFXHelper.ACCT_TYPE));
            BankMessageSet msgSet = new BankMessageSet();
            msgSet.setBankMessageSetV1(msgSetV);
            ofx.setBankingMessageSet(msgSet);
        }
        return ofx;
    }

    private boolean isAccountTypeValid() {
        return StringUtils.isNotBlank(this.accountTypes.get((account.get(QFXHelper.ACCT_TYPE))));
    }

    private BankMessageService getBankMsgService(JsonArray transactions, Date trxStartDate, Date trxEndDate,
            Status serverStatus) throws ParseException {
    	Date today = HelperMethods.getUTCdate();
        ArrayList<Transaction> allTransactions = getTransactions(transactions);
        BankMessageService bankMsgSrvc = new BankMessageService();
        BankTransactionList bankTransactionList = new BankTransactionList();
        bankTransactionList.setBankTransactionList(allTransactions);
        if (null != trxStartDate) {
            bankTransactionList.setTransactionStartDate(HelperMethods.toQFXDateTime(trxStartDate));
        }
        if (null != trxEndDate) {
            bankTransactionList.setTransactionEndDate(HelperMethods.toQFXDateTime(trxEndDate));
        }
        Statement stmt = new Statement();
        stmt.setCurrency("USD");
        stmt.setBankTransactionList(bankTransactionList);
        stmt.setBankAccountForm(getBankAccountFrom());
        stmt.setAvailableBalance(getAvailableBalance(today));
        stmt.setLedgerBalance(getLedgerBalance(today));

        StatementWraper stmtWrpr = new StatementWraper();
        stmtWrpr.setStatement(stmt);
        stmtWrpr.setStatus(serverStatus);
        stmtWrpr.setTransactionUid(0L);

        bankMsgSrvc.addStatementwrapper(stmtWrpr);
        return bankMsgSrvc;
    }

    private SignonMessageService getSignOnMsgService(SignON signon) {
        SignonMessageService signOnMsgSrvc = new SignonMessageService();
        signOnMsgSrvc.setSignon(signon);
        return signOnMsgSrvc;
    }

    private SignON getSignOn(Status serverStatus) throws ParseException {
        FinancialInstitution fi = new FinancialInstitution();
        fi.setFinancialId(this.financeId);
        fi.setOrg(this.orgName);
        SignON signon = new SignON();
        signon.setIntuitBuid(this.intuitBuid);
        signon.setIntuitUserId(this.userName);
        signon.setLanguage("ENG");
        signon.setServerDate(HelperMethods.toQFXDateTime(HelperMethods.getUTCdate()));
        signon.setServerStatus(serverStatus);
        signon.setFi(fi);
        return signon;
    }

    private Status getServerStatus() {
        Status serverStatus = new Status();
        serverStatus.setCode("0");
        serverStatus.setMessage("OK");
        serverStatus.setSeverity("INFO");
        return serverStatus;
    }

    private void addHeader(Writer file) throws IOException {
        String header =
                "OFXHEADER:100\r\nDATA:OFXSGML\r\nVERSION:102\r\nSECURITY:NONE\r\nENCODING:USASCII\r\nCHARSET:1252\r\nCOMPRESSION:NONE\r\nOLDFILEUID:NONE\r\nNEWFILEUID:NONE\r\n\n";
        file.write(header);
    }

    public File addOFXToFile(OFX ofx, File file) throws IOException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { OFX.class });
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            jaxbMarshaller.marshal(ofx, file);
            return file;
        } catch (JAXBException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

    @Override
    public byte[] generateFile(JsonArray data, String title, String generatedBy, String startDate, String endDate,
            Map<String, String> fieldList, Map<String, Object> otherData, String filters) throws IOException {
        File file = null;
        try {
            init(otherData);
            file = File.createTempFile("ofxobject", ".tmp");
            OFX ofx = generateOFXObject(data, startDate, endDate);
            addOFXToFile(ofx, file);
            return generateQFXFile(file);
        } catch (Exception e) {
            alert.prepareError(e.getMessage()).log();
        } finally {
            if (null != file) {
                file.deleteOnExit();
            }
        }
        return new byte[0];
    }

    private void init(Map<String, Object> otherData) {
        QFXHelper helper = new QFXHelper();
        this.transactionTypes = helper.getTransactionTypesMap();
        this.accountTypes = helper.getAccountTypesMap();
        this.intuitBuid = convertToLong(otherData.get("intuitBuid"));
        this.financeId = convertToLong(otherData.get("financeId"));
        this.orgName = (String) otherData.get("userName");
        this.userName = (String) otherData.get("userName");
        if (null != otherData.get("accounts")) {
            this.account = getAccountsMap((JsonArray) otherData.get("accounts"));
        }
    }

    private Long convertToLong(Object longVal) {
        try {
            return Long.parseLong((String) longVal);
        } catch (Exception e) {
            return Long.valueOf(0);
        }
    }

    private Double convertToDouble(Object longVal) {
        try {
            return Double.parseDouble((String) longVal);
        } catch (Exception e) {
            return Double.valueOf(0);
        }
    }

    private byte[] generateQFXFile(File ofxFile) throws IOException {
        File qfxFile = null;
        FileReader reader = null;
        FileReader qfxreader = null;
        BufferedReader bufferedReader = null;
        FileWriter writer = null;
        try {
            qfxFile = File.createTempFile("qfx", ".tmp");
            writer = new FileWriter(qfxFile);
            addHeader(writer);
            reader = new FileReader(ofxFile);
            bufferedReader = new BufferedReader(reader);
            String s = bufferedReader.readLine();
            while ((s = bufferedReader.readLine()) != null) {
                writer.write(s);
            }
            writer.close();
            qfxreader = new FileReader(qfxFile);
            return IOUtils.toByteArray(qfxreader);
        } catch (Exception e) {
        } finally {
            if (null != qfxreader) {
                qfxreader.close();
            }
            if (null != bufferedReader) {
                bufferedReader.close();
            }
            if (null != reader) {
                reader.close();
            }
            if (null != qfxFile) {
                qfxFile.deleteOnExit();
            }
        }
        return new byte[0];
    }

    @Override
    public String getContentType() {
        return "application/vnd.intu.qfx";
    }
    
    @Override
	public byte[] generateLoanFile(JsonArray data, String title, String generatedBy, Map<String, String> fieldList,
			Map<String, Object> otherData, String filters,String installmentType,Map<String, String> summaryDetails,String paymentDateFormat) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] generateCombinedStatementsFile(JsonArray dataObject, String title, String generatedBy,
			String startDate, String endDate, Map<String, String> fieldList, String bankName, String currencyCode,
			String paymentDateFormat) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}