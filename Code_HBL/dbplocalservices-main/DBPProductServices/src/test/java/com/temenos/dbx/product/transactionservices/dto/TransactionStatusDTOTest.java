package com.temenos.dbx.product.transactionservices.dto;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;
import com.temenos.dbx.product.commons.dto.TransactionStatusDTO;

public class TransactionStatusDTOTest {
	@Test
	public void testInvoke() throws Exception {

		JSONObject transactionStatusDTOObj = new JSONObject(new String(Files.readAllBytes(Paths.get(new File(
				getClass().getClassLoader().getResource("TransactionStatusDTO.json").getFile()).getPath()))));

		TransactionStatusDTO transactionStatusDTO = JSONUtils
				.parse(transactionStatusDTOObj.toString(), TransactionStatusDTO.class);
		assertEquals("18ca792d-2121-444c-884e-e5bfd233de78,29d52604-4dab-46a4-85f0-aa70b1964485", transactionStatusDTO.getNewSignatories());
        assertEquals("18ca792d-2121-444c-884e-e5bfd233de78,29d52604-4dab-46a4-85f0-aa70b1964488", transactionStatusDTO.getOldSignatories());
        assertEquals(false, transactionStatusDTO.getIsNewFlow());
        assertEquals("29d52604-4dab-46a4-85f0-aa70b1964485,GsxVkrMuAf,GsxVkrMuAf,3358932067", transactionStatusDTO.getSignatoryGroupValues());
        assertEquals("18ca792d-2121-444c-884e-e5bfd233de78,29d52604-4dab-46a4-85f0-aa70b1964485,3358932067", transactionStatusDTO.getNewSigValues());
        assertEquals("18ca792d-2121-444c-884e-e5bfd233de78,29d52604-4dab-46a4-85f0-aa70b1964488", transactionStatusDTO.getDeleteSigValues());
        assertEquals("[1389540560,1389540561,1389540562]", transactionStatusDTO.getAccountIds());
        assertEquals("NON_MONETARY_LIMIT", transactionStatusDTO.getLimitTypeId());
        assertEquals("[{\"lowerlimit\":\"-1\",\"upperlimit\":\"-1\",\"approvalruleId\":\"ANY_ONE\",\"approvers\":[{\"approverId\":\"1389540560\"}]}]", transactionStatusDTO.getLimits());
        assertEquals("INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT", transactionStatusDTO.getApprovalMatrixFeatureActionID());
	}
}