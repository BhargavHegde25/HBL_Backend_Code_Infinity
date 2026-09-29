package com.temenos.dbx.product.approvalservices.dto;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class ApprovalRequestDTOTest {
	@Test
	public void testInvoke() throws Exception {

		JSONObject approvalRequestDTOJSONObj = new JSONObject(new String(Files.readAllBytes(Paths.get(new File(
				getClass().getClassLoader().getResource("ApprovalRequestDTO.json").getFile()).getPath()))));

		ApprovalRequestDTO approvalRequestDTO = JSONUtils
				.parse(approvalRequestDTOJSONObj.toString(), ApprovalRequestDTO.class);
		assertEquals("SENT_BY_NAME_001", approvalRequestDTO.getSentByName());
        assertEquals("SENT_BY_USERNAME_001", approvalRequestDTO.getSentByUserName());
        assertEquals("2024-02-08", approvalRequestDTO.getCreatedts());
	}
}
