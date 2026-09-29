package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

public class CredentialCheckerDTO implements DBPDTOEXT {

    /**
     * 
     */
    private static final long serialVersionUID = 8034633732574011187L;
    private String id;
    private String userName;
    private String linktype;
    private String createdts;
    private boolean isNew;
    private boolean isDeleted;
    private String retryCount;
    private String companyLegalUnit;

    /**
     * @param isNew
     *            the isNew to set
     */
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    /**
     * @return the linktype
     */
    public String getLinktype() {
        return linktype;
    }

    /**
     * @param linktype
     *            the linktype to set
     */
    public void setLinktype(String linktype) {
        this.linktype = linktype;
    }

    /**
     * @return the createdts
     */
    public String getCreatedts() {
        return createdts;
    }

    /**
     * @param createdts
     *            the createdts to set
     */
    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }

    /**
     * @param isNew
     *            the isNew to set
     */
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @return the isNew
     */
    public Boolean getIsNew() {
        return isNew;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
        if (isNew) {
            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CREDENTIAL_CHECKER_CREATE)
                    .has("errmsg");
        } else {
            return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CREDENTIAL_CHECKER_UPDATE)
                    .has("errmsg");
        }
    }

    @Override
    public Object loadDTO(String id) {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        if (StringUtils.isNotBlank(linktype)) {
            if (StringUtils.isNotBlank(filter))
                filter += " ";
            filter += "linktype" + DBPUtilitiesConstants.EQUAL + linktype;
        }

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CREDENTIAL_CHECKER_GET, true, false);

        if (exts != null && exts.size() > 0) {
            return exts.get(exts.size() - 1);
        }

        return null;
    }

    @Override
    public Object loadDTO() {

        if (StringUtils.isNotBlank(id)) {
            return loadDTO(id);
        }
        if (StringUtils.isNotBlank(userName)) {
            String filter = "UserName" + DBPUtilitiesConstants.EQUAL + userName;
            List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

            if (StringUtils.isNotBlank(linktype)) {
                filter += DBPUtilitiesConstants.AND + "linktype" + DBPUtilitiesConstants.EQUAL + linktype;
            }

            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CREDENTIAL_CHECKER_GET, true, false);

            if (!exts.isEmpty()) {
                return exts.get(0);
            }
        }
        return null;
    }

    /**
     * @return the isDeleted
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted
     *            the isDeleted to set
     */
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(String retryCount) {
        this.retryCount = retryCount;
    }
}
