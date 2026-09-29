package com.kony.adminconsole.utilities;

/**
 * @author Aditya Mankal
 *
 */
public enum ObjectServiceURLEnum {

    DBXOBJECTS_MEDIA_CREATE("dbxdbObjects", "media", "POST"), //
    DBXOBJECTS_MEDIA_GET("dbxdbObjects", "media", "GET");

    private String serviceName;
    private String objectName;
    private String verbName;

    ObjectServiceURLEnum(String serviceName, String objectName, String verbName) {
        this.serviceName = serviceName;
        this.objectName = objectName;
        this.verbName = verbName;
    }

    /**
     * @return the objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @param objectName
     *            the objectName to set
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * @return the verbName
     */
    public String getVerbName() {
        return verbName;
    }

    /**
     * @param verbName
     *            the verbName to set
     */
    public void setVerbName(String verbName) {
        this.verbName = verbName;
    }

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName
     *            the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
