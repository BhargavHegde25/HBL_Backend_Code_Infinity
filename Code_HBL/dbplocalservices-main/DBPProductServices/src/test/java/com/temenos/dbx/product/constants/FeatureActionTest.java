package com.temenos.dbx.product.constants;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FeatureActionTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	
	@AfterClass
	public static void cleanup() throws Exception {
	}
	
	@Before 
	public void setUp() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals("APPROVAL_MATRIX_MANAGE", FeatureAction.APPROVAL_MATRIX_MANAGE);
		assertEquals("APPORVAL_MATRIX_MANAGE_APPROVAL", FeatureAction.APPROVAL_MATRIX_MANAGE_APPROVAL);
		assertEquals("APPORVAL_MATRIX_MANAGE_SELF_APPROVAL", FeatureAction.APPROVAL_MATRIX_MANAGE_SELF_APPROVAL);
		assertEquals("APPROVAL_MATRIX_VIEW", FeatureAction.APPROVAL_MATRIX_VIEW);
	}

}
