package com.kony.dbputilities.demoservices;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class YAWS_USER {

    String APPID;
    String USERID;
    String PASSWORD;
    String COUNTRY;
    String RETENTION;
    String INTUSER;
    String EMAIL;
    String FIRSTNAME;
    String LASTNAME;
    String JOBTITLE;
    String STATE;
    String MOBILE;
    String EXTENSION;
    String MAIL_TRIGGERED;
    String NOTIF_DATA_CRTD;
    String OR_DATA_CRTD;
    String ERROR_IND;
    String ERROR_MSG;
    String DESCRIPTION;
    String INT_SERV;
    String TIMESTAMP;
    String EXTRACT_TSTAMP;
    String DELETE_IND;
    String COMPANY;
    String utm_source;
    String utm_campaign;
    String utm_medium;
    String Lead_source;
    String Lead_source_Desc;
    String Ad_group;
    String Campaign;

    public String getLead_source() {
        return Lead_source;
    }

    public void setLead_source(String lead_source) {
        Lead_source = lead_source;
    }

    public String getLead_source_Desc() {
        return Lead_source_Desc;
    }

    public void setLead_source_Desc(String lead_source_Desc) {
        Lead_source_Desc = lead_source_Desc;
    }

    public String getAd_group() {
        return Ad_group;
    }

    public void setAd_group(String ad_group) {
        Ad_group = ad_group;
    }

    public String getCampaign() {
        return Campaign;
    }

    public void setCampaign(String campaign) {
        Campaign = campaign;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    String keyword;

    public String getUtm_medium() {
        return utm_medium;
    }

    public void setUtm_medium(String utm_medium) {
        this.utm_medium = utm_medium;
    }

    public String getUtm_source() {
        return utm_source;
    }

    public void setUtm_source(String utm_source) {
        this.utm_source = utm_source;
    }

    public String getUtm_campaign() {
        return utm_campaign;
    }

    public void setUtm_campaign(String utm_campaign) {
        this.utm_campaign = utm_campaign;
    }

    public String getUtm_content() {
        return utm_content;
    }

    public void setUtm_content(String utm_content) {
        this.utm_content = utm_content;
    }

    public String getUtm_term() {
        return utm_term;
    }

    public void setUtm_term(String utm_term) {
        this.utm_term = utm_term;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    String utm_content;
    String utm_term;
    String medium;

    public String getEXTRACT_TSTAMP() {
        return EXTRACT_TSTAMP;
    }

    @XmlElement
    public void setEXTRACT_TSTAMP(String eXTRACT_TSTAMP) {
        EXTRACT_TSTAMP = eXTRACT_TSTAMP;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String cOMPANY) {
        COMPANY = cOMPANY;
    }

    public String getAPPID() {
        return APPID;
    }

    @XmlElement
    public void setAPPID(String aPPID) {
        APPID = aPPID;
    }

    public String getUSERID() {
        return USERID;
    }

    @XmlElement
    public void setUSERID(String uSERID) {
        USERID = uSERID;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    @XmlElement
    public void setPASSWORD(String pASSWORD) {
        PASSWORD = pASSWORD;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    @XmlElement
    public void setCOUNTRY(String cOUNTRY) {
        COUNTRY = cOUNTRY;
    }

    public String getRETENTION() {
        return RETENTION;
    }

    @XmlElement
    public void setRETENTION(String rETENTION) {
        RETENTION = rETENTION;
    }

    public String getINTUSER() {
        return INTUSER;
    }

    @XmlElement
    public void setINTUSER(String iNTUSER) {
        INTUSER = iNTUSER;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    @XmlElement
    public void setEMAIL(String eMAIL) {
        EMAIL = eMAIL;
    }

    public String getFIRSTNAME() {
        return FIRSTNAME;
    }

    @XmlElement
    public void setFIRSTNAME(String fIRSTNAME) {
        FIRSTNAME = fIRSTNAME;
    }

    public String getLASTNAME() {
        return LASTNAME;
    }

    @XmlElement
    public void setLASTNAME(String lASTNAME) {
        LASTNAME = lASTNAME;
    }

    public String getJOBTITLE() {
        return JOBTITLE;
    }

    @XmlElement
    public void setJOBTITLE(String jOBTITLE) {
        JOBTITLE = jOBTITLE;
    }

    public String getSTATE() {
        return STATE;
    }

    @XmlElement
    public void setSTATE(String sTATE) {
        STATE = sTATE;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    @XmlElement
    public void setMOBILE(String mOBILE) {
        MOBILE = mOBILE;
    }

    public String getEXTENSION() {
        return EXTENSION;
    }

    @XmlElement
    public void setEXTENSION(String eXTENSION) {
        EXTENSION = eXTENSION;
    }

    public String getMAIL_TRIGGERED() {
        return MAIL_TRIGGERED;
    }

    @XmlElement
    public void setMAIL_TRIGGERED(String mAIL_TRIGGERED) {
        MAIL_TRIGGERED = mAIL_TRIGGERED;
    }

    public String getNOTIF_DATA_CRTD() {
        return NOTIF_DATA_CRTD;
    }

    @XmlElement
    public void setNOTIF_DATA_CRTD(String nOTIF_DATA_CRTD) {
        NOTIF_DATA_CRTD = nOTIF_DATA_CRTD;
    }

    public String getOR_DATA_CRTD() {
        return OR_DATA_CRTD;
    }

    @XmlElement
    public void setOR_DATA_CRTD(String oR_DATA_CRTD) {
        OR_DATA_CRTD = oR_DATA_CRTD;
    }

    public String getERROR_IND() {
        return ERROR_IND;
    }

    @XmlElement
    public void setERROR_IND(String eRROR_IND) {
        ERROR_IND = eRROR_IND;
    }

    public String getERROR_MSG() {
        return ERROR_MSG;
    }

    @XmlElement
    public void setERROR_MSG(String eRROR_MSG) {
        ERROR_MSG = eRROR_MSG;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    @XmlElement
    public void setDESCRIPTION(String dESCRIPTION) {
        DESCRIPTION = dESCRIPTION;
    }

    public String getINT_SERV() {
        return INT_SERV;
    }

    @XmlElement
    public void setINT_SERV(String iNT_SERV) {
        INT_SERV = iNT_SERV;
    }

    public String getTIMESTAMP() {
        /*
         * String trDate=TIMESTAMP.substring(0, Math.min(TIMESTAMP.length(), 8)); String krwtrDate=""; Date tradeDate;
         * try { tradeDate = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse(trDate); krwtrDate = new
         * SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(tradeDate); } catch (ParseException e) {
         * 
         * LOG.error(e.getMessage()); }
         */

        return TIMESTAMP;
    }

    @XmlElement
    public void setTIMESTAMP(String tIMESTAMP) {
        TIMESTAMP = tIMESTAMP;
    }

    public String getDELETE_IND() {
        return DELETE_IND;
    }

    @XmlElement
    public void setDELETE_IND(String dELETE_IND) {
        DELETE_IND = dELETE_IND;
    }

    @Override
    public String toString() {

        return "Appid: " + APPID + " Userid: " + USERID + " Password: " + PASSWORD + "Country: " + COUNTRY
                + " Retention: " + RETENTION + " Intuser: " + INTUSER + " Email: " + EMAIL + " Extension: " + EXTENSION
                + "MailTriggered: " + MAIL_TRIGGERED + " NotifDataCreated: " + NOTIF_DATA_CRTD + " OrDataCreated: "
                + OR_DATA_CRTD + "ErrorIndicator: " + ERROR_IND + " ErrorMsg: " + ERROR_MSG + " Description: "
                + DESCRIPTION + " TimeStamp: " + TIMESTAMP + " DeleteIndicator: " + DELETE_IND;
    }

}
