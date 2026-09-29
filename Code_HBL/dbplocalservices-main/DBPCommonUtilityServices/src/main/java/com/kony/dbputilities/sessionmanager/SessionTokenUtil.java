package com.kony.dbputilities.sessionmanager;

import java.util.UUID;

/**
 * The Class SessionTokenUtil.
 */
public class SessionTokenUtil {

    /**
     * Gets the new session token.
     *
     * @return the new session token
     */
    public static String getNewSessionToken() {
        String token = UUID.randomUUID().toString();
        return token;
    }

}
