package com.temenos.dbx.product.approvalservices.dto;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;
import org.junit.Test;

import com.dbp.core.util.JSONUtils;

public class BBRequestDTOTest {
	@Test
	public void testInvoke() throws Exception {

		JSONObject bbRequestDTOObj = new JSONObject(new String(Files.readAllBytes(Paths.get(new File(
				getClass().getClassLoader().getResource("BBRequestDTO.json").getFile()).getPath()))));

		BBRequestDTO bbRequestDTO = JSONUtils
				.parse(bbRequestDTOObj.toString(), BBRequestDTO.class);
		assertEquals("SENT_BY_NAME_001", bbRequestDTO.getSentByName());
        assertEquals("SENT_BY_USERNAME_001", bbRequestDTO.getSentByUserName());
        assertEquals("2024-02-08", bbRequestDTO.getCreatedts());
	}
}
