package com.hbl.infinity.accounts.perf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class T24AccountsSnapshotTest {

    private static final String ACCOUNT = "{\"accountId\":\"100001\",\"isNew\":\"false\",\"availableBalance\":\"10.00\"}";

    @Test
    public void cleanResponseIsStorable() {
        assertTrue(T24AccountsSnapshot.isStorable("{\"Accounts\":[" + ACCOUNT + "],\"opstatus\":0,\"httpStatusCode\":200}"));
        assertTrue(T24AccountsSnapshot.isStorable("{\"Accounts\":[" + ACCOUNT + "],\"opstatus\":\"0\"}"));
    }

    @Test
    public void responseWithANewAccountIsNotStorable() {
        String newAccount = "{\"accountId\":\"100002\",\"isNew\":\"true\"}";
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[" + ACCOUNT + "," + newAccount + "],\"opstatus\":0}"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[{\"isNew\":true}],\"opstatus\":0}"));
    }

    @Test
    public void errorsAndEmptyResponsesAreNotStorable() {
        assertFalse(T24AccountsSnapshot.isStorable(null));
        assertFalse(T24AccountsSnapshot.isStorable(" "));
        assertFalse(T24AccountsSnapshot.isStorable("not json"));
        assertFalse(T24AccountsSnapshot.isStorable("[]"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[],\"opstatus\":0}"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"opstatus\":0}"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[" + ACCOUNT + "]}"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[" + ACCOUNT + "],\"opstatus\":8009}"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[" + ACCOUNT + "],\"opstatus\":0,\"dbpErrCode\":\"20041\"}"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[" + ACCOUNT + "],\"opstatus\":0,\"errmsg\":\"x\"}"));
        assertFalse(T24AccountsSnapshot.isStorable("{\"Accounts\":[1],\"opstatus\":0}"));
    }
}
