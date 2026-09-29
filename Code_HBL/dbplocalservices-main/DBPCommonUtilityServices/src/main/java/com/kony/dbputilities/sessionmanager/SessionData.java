package com.kony.dbputilities.sessionmanager;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Class SessionData.
 */
public class SessionData {

    /** The date. */
    private Date date;
    /** The userId. */
    private String userId;
    /** The table offset. */
    private Map<String, String> tableOffset = new ConcurrentHashMap<>();

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
        return new Date(date.getTime());
    }

    /**
     * Sets the date.
     *
     * @param date
     *            the new date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the table offset.
     *
     * @return the table offset
     */
    public Map<String, String> getTableOffset() {
        return tableOffset;
    }

    /**
     * Sets the table offset.
     *
     * @param tableOffset
     *            the table offset
     */
    public void setTableOffset(Map<String, String> tableOffset) {
        this.tableOffset = tableOffset;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *            the new user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}