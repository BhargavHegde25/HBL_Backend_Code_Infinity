package com.kony.adminconsole.utilities;

public class DBXResult {

    private String dbpErrCode;
    private String dbpErrMsg;
    private Object response;

    public DBXResult() {
    }

    /**
     * @return the dbpErrCode
     */
    public String getDbpErrCode() {
        return dbpErrCode;
    }

    /**
     * @param dbpErrCode
     *            the dbpErrCode to set
     */
    public void setDbpErrCode(String dbpErrCode) {
        this.dbpErrCode = dbpErrCode;
    }

    /**
     * @return the dbpErrMsg
     */
    public String getDbpErrMsg() {
        return dbpErrMsg;
    }

    /**
     * @param dbpErrMsg
     *            the dbpErrMsg to set
     */
    public void setDbpErrMsg(String dbpErrMsg) {
        this.dbpErrMsg = dbpErrMsg;
    }

    /**
     * @return the response
     */
    public Object getResponse() {
        return response;
    }

    /**
     * @param response
     *            the response to set
     */
    public void setResponse(Object response) {
        this.response = response;
    }

    public void setError(ErrorCodeEnum enumObject) {
        this.dbpErrCode = enumObject.getErrorCodeAsString();
        this.dbpErrMsg = enumObject.getMessage();

    }

}
