package com.temenos.dbx.transaction.businessdelegate.impl;

import java.util.ArrayList;
import java.util.List;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.transaction.businessdelegate.api.DirectDebitsBusinessDelegate;
import com.temenos.dbx.transaction.dto.DirectDebitDTO;

public class DirectDebitsBusinessDelegateImpl implements DirectDebitsBusinessDelegate {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

	/**
	 * @author sribarani.vasthan resourceImpl getDirectDebits: returns list of Direct debits
	 */
	public List<DirectDebitDTO> getDirectDebits(DirectDebitDTO inputDTO) {

		List<DirectDebitDTO> directDebitsList = new ArrayList<>();

		String accountId = inputDTO.getAccountID();
		DirectDebitDTO record1 = new DirectDebitDTO();

		record1.setAccountID(accountId);
		record1.setBeneficiaryName("ROLFGERLING");
		record1.setDirectDebitId(accountId+".1");
		record1.setFromAccountName("John Day");
		record1.setDirectDebitStatus("ACTIVE");
		record1.setSigningDate("2020-04-17");
		record1.setLastPaymentDate("2020-06-17");
		record1.setMandateReference("RB20200414150783707200");

		directDebitsList.add(record1);
		DirectDebitDTO record2 = new DirectDebitDTO();

		record2.setAccountID(accountId);
		record2.setBeneficiaryName("ANTONY");
		record2.setDirectDebitId(accountId+".2");
		record2.setFromAccountName("William Day");
		record2.setDirectDebitStatus("ACTIVE");
		record2.setSigningDate("2020-04-17");
		record2.setLastPaymentDate("2020-07-22");
		record2.setMandateReference("RB20200414150783708938");
		directDebitsList.add(record2);
		
		DirectDebitDTO record3 = new DirectDebitDTO();

		record3.setAccountID(accountId);
		record3.setBeneficiaryName("ERIC");
		record3.setDirectDebitId(accountId+".3");
		record3.setFromAccountName("Aaron Fleming");
		record3.setDirectDebitStatus("ACTIVE");
		record3.setSigningDate("2020-04-17");
		record3.setLastPaymentDate("2020-06-19");
		record3.setMandateReference("RB20200414150783700192");
        directDebitsList.add(record3);
        
        DirectDebitDTO record4 = new DirectDebitDTO();

        record4.setAccountID("1425363642");
        record4.setBeneficiaryName("ALEX");
        record4.setDirectDebitId("1425363642.1");
        record4.setFromAccountName("Umar Akmal");
        record4.setDirectDebitStatus("ACTIVE");
        record4.setSigningDate("2020-04-17");
        record4.setLastPaymentDate("2021-06-17");
        record4.setMandateReference("RB20200414150783709152");
        directDebitsList.add(record4);
        
        DirectDebitDTO record5 = new DirectDebitDTO();

        record5.setAccountID("1425363642");
        record5.setBeneficiaryName("ALTON");
        record5.setDirectDebitId("1425363642.2");
        record5.setFromAccountName("Barry Allen");
        record5.setDirectDebitStatus("ACTIVE");
        record5.setSigningDate("2020-04-17");
        record5.setLastPaymentDate("2021-01-11");
        record5.setMandateReference("RB20200414150783767352");
        directDebitsList.add(record5);
        
        DirectDebitDTO record6 = new DirectDebitDTO();

        record6.setAccountID("1425363642");
        record6.setBeneficiaryName("ANGEL");
        record6.setDirectDebitId("1425363642.6");
        record6.setFromAccountName("Andy Grimm");
        record6.setDirectDebitStatus("ACTIVE");
        record6.setSigningDate("2020-04-17");
        record6.setLastPaymentDate("2020-10-10");
        record6.setMandateReference("RB20200414150783783652");
        directDebitsList.add(record6);
        
        DirectDebitDTO record7 = new DirectDebitDTO();

        record7.setAccountID(accountId);
        record7.setBeneficiaryName("CADBURY");
        record7.setDirectDebitId(accountId+".7");
        record7.setFromAccountName("Earl Cadogan");
        record7.setDirectDebitStatus("ACTIVE");
        record7.setSigningDate("2020-04-17");
        record7.setLastPaymentDate("2020-12-12");
        record7.setMandateReference("RB20200414150783799999");
        directDebitsList.add(record7);
        
        DirectDebitDTO record8 = new DirectDebitDTO();

        record8.setAccountID(accountId);
        record8.setBeneficiaryName("BUFFET");
        record8.setDirectDebitId(accountId+".8");
        record8.setFromAccountName("Sumitomo Tokyo");
        record8.setDirectDebitStatus("ACTIVE");
        record8.setSigningDate("2020-04-17");
        record8.setLastPaymentDate("2020-06-17");
        record8.setMandateReference("RB20200414150783707000");
        directDebitsList.add(record8);
        
        DirectDebitDTO record9 = new DirectDebitDTO();

        record9.setAccountID(accountId);
        record9.setBeneficiaryName("CISCO");
        record9.setDirectDebitId(accountId+".9");
        record9.setFromAccountName("Citi Group");
        record9.setDirectDebitStatus("ACTIVE");
        record9.setSigningDate("2020-04-17");
        record9.setLastPaymentDate("2020-07-07");
        record9.setMandateReference("RB20200414150783766666");
        directDebitsList.add(record9);
        
        DirectDebitDTO record10 = new DirectDebitDTO();

        record10.setAccountID(accountId);
        record10.setBeneficiaryName("ANDREW");
        record10.setDirectDebitId(accountId+".10");
        record10.setFromAccountName("Diana Cooper");
        record10.setDirectDebitStatus("ACTIVE");
        record10.setSigningDate("2020-04-17");
        record10.setLastPaymentDate("2020-11-09");
        record10.setMandateReference("RB20200414150783708888");
        directDebitsList.add(record10);
        
        DirectDebitDTO record11 = new DirectDebitDTO();

        record11.setAccountID(accountId);
        record11.setBeneficiaryName("DAVID");
        record11.setDirectDebitId(accountId+".11");
        record11.setFromAccountName("Danske Bank");
        record11.setDirectDebitStatus("ACTIVE");
        record11.setSigningDate("2020-04-17");
        record11.setLastPaymentDate("2020-11-17");
        record11.setMandateReference("RB20200414150783723451");
        directDebitsList.add(record11);
        
        DirectDebitDTO record12 = new DirectDebitDTO();

        record12.setAccountID(accountId);
        record12.setBeneficiaryName("INFINITY");
        record12.setDirectDebitId(accountId+".12");
        record12.setFromAccountName("Harry Crispp");
        record12.setDirectDebitStatus("ACTIVE");
        record12.setSigningDate("2020-04-17");
        record12.setLastPaymentDate("2020-09-29");
        record12.setMandateReference("RB20200414150783701111");
        directDebitsList.add(record12);
        
        DirectDebitDTO record13 = new DirectDebitDTO();

        record13.setAccountID(accountId);
        record13.setBeneficiaryName("CHARLES");
        record13.setDirectDebitId(accountId+".13");
        record13.setFromAccountName("David Brown");
        record13.setDirectDebitStatus("ACTIVE");
        record13.setSigningDate("2020-04-17");
        record13.setLastPaymentDate("2020-10-27");
        record13.setMandateReference("RB202004141507837090371");
        directDebitsList.add(record13);
        
        DirectDebitDTO record14 = new DirectDebitDTO();

        record14.setAccountID(accountId);
        record14.setBeneficiaryName("FAKHIR");
        record14.setDirectDebitId(accountId+".14");
        record14.setFromAccountName("Emma Wilson");
        record14.setDirectDebitStatus("ACTIVE");
        record14.setSigningDate("2020-04-17"); 
        record14.setLastPaymentDate("2020-12-14");
        record14.setMandateReference("RB202004141455666453201");
        directDebitsList.add(record14);
        
        DirectDebitDTO record15 = new DirectDebitDTO();

        record15.setAccountID(accountId);
        record15.setBeneficiaryName("GHOLAM");
        record15.setDirectDebitId(accountId+".15");
        record15.setFromAccountName("Henry Gibbins");
        record15.setDirectDebitStatus("ACTIVE");
        record15.setSigningDate("2020-04-17");
        record15.setLastPaymentDate("2020-01-28");
        record15.setMandateReference("RB20200414150723344201");
        directDebitsList.add(record15);
		
		return directDebitsList;
	}

	/**
	 * @author sribarani.vasthan businessdelegateImpl cancelDirectDebit: removed
	 *         directDebit from mock directDebitsList
	 */
	public DirectDebitDTO cancelDirectDebit(String directDebitId, String directDebitStatus) {
		JSONObject jsonResponse = new JSONObject();
		DirectDebitDTO directDebitDTO = null;

		try {
			jsonResponse.put("message", "Service Request created successfully");
			jsonResponse.put("orderId", directDebitId);
			jsonResponse.put("status", "SUCCESS");
			directDebitDTO = JSONUtils.parse(jsonResponse.toString(), DirectDebitDTO.class);
		} catch (Exception e) {
			alert.prepareError("Caught exception in cancelDirectDebit : " + e).log();
		} 
		return directDebitDTO;
	}
}
