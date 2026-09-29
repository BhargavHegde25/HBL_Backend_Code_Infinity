package com.kony.dbputilities.sessionmanager;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.kony.dbputilities.util.HelperMethods;

/**
 * The Class SessionManager.
 */
@SuppressWarnings({ "rawtypes", "unused" })
public class SessionManager {

    /** The all sessions. */
    private static Map<String, SessionData> allSessions = new ConcurrentHashMap<>();

    /** The session timeout. */
    private static long sessionTimeout = 30;

    /**
     * Sets the non static session timeout. This is a hack to initialize static attributes. Currently Spring does not
     * allow initializing static attributes through properties.
     * 
     * @param nonStaticSessionTimeout
     *            the new non static session timeout
     * 
     */
    public void setNonStaticSessionTimeout(long nonStaticSessionTimeout) {
        SessionManager.sessionTimeout = nonStaticSessionTimeout;
    }

    /**
     * Sets the session.
     *
     * @param token
     *            the new session
     */
    public static void setSession(String token, String userId) {
        SessionData sessionData;
        if (isValidSession(token)) {
            sessionData = allSessions.get(token);
        } else {
            sessionData = new SessionData();
            sessionData.setUserId(userId);
        }
        sessionData.setDate(HelperMethods.incrementDateInMinutes(sessionTimeout));
        allSessions.put(token, sessionData);
    }

    /**
     * Checks if is valid session.
     *
     * @param token
     *            the token
     * @return true, if is valid session
     */
    public static boolean isValidSession(String token) {
        boolean isValidSession = false;
        synchronized (allSessions) {
            if (allSessions.containsKey(token)) {
                SessionData sessionData = allSessions.get(token);
                isValidSession = (sessionData.getDate()).after(new Date());
            }
        }
        return isValidSession;
    }

    /**
     * Removes the session token.
     *
     * @param token
     *            the token
     */
    public static void removeSessionToken(String token) {
        allSessions.remove(token);
    }

    /**
     * Gets the new token.
     *
     * @return the new token
     */
    public static String createSession(String userId) {
        String token = SessionTokenUtil.getNewSessionToken();
        setSession(token, userId);
        return token;
    }
    /**
     * Gets the new token.
     *
     * @return the new token
     */
    /*
     * public static String getNewToken() { String token = SessionTokenUtil.getNewSessionToken(); return token; }
     */

    /**
     * Gets the session data.
     *
     * @param token
     *            the token
     * @return the session data
     */
    public static SessionData getSessionData(String token) {
        return allSessions.get(token);
    }

    public static String getUserIDfromSession(String token) {
        return getSessionData(token).getUserId();
    }

    /**
     * Cleanup sessions.fixed delay in milliseconds.
     */
    public static void cleanupSessions() {
        Iterator it = allSessions.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry obj = (Map.Entry) it.next();
            SessionData sessionData = (SessionData) obj.getValue();
            if ((sessionData.getDate()).before(new Date())) {
                it.remove();
            }
        }
    }

    static {
        Timer time = new Timer();
        time.schedule(new TimerTask() {

            @Override
            public void run() {
                cleanupSessions();
            }

        }, 60000);
    }

}