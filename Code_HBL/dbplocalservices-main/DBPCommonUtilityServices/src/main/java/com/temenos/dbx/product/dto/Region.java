package com.temenos.dbx.product.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;

@JsonInclude(Include.NON_NULL)
public class Region implements DBPDTOEXT   {

    /**
     * 
     */
    private static final long serialVersionUID = -3342323516357620470L;
    /**
     * 
     */
    private  String id;
    private  String country_id;
    private  String code;
    private  String name;
    private  String createdby;
    private  String modifiedby;
    private  String createdts;
    private  String lastmodifiedts;
    private  String synctimestamp;
    private  String softdeleteflag;
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the country_id
     */
    public String getCountry_id() {
        return country_id;
    }
    /**
     * @param country_id the country_id to set
     */
    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the createdby
     */
    public String getCreatedby() {
        return createdby;
    }
    /**
     * @param createdby the createdby to set
     */
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
    /**
     * @return the modifiedby
     */
    public String getModifiedby() {
        return modifiedby;
    }
    /**
     * @param modifiedby the modifiedby to set
     */
    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }
    /**
     * @return the createdts
     */
    public String getCreatedts() {
        return createdts;
    }
    /**
     * @param createdts the createdts to set
     */
    public void setCreatedts(String createdts) {
        this.createdts = createdts;
    }
    /**
     * @return the lastmodifiedts
     */
    public String getLastmodifiedts() {
        return lastmodifiedts;
    }
    /**
     * @param lastmodifiedts the lastmodifiedts to set
     */
    public void setLastmodifiedts(String lastmodifiedts) {
        this.lastmodifiedts = lastmodifiedts;
    }
    /**
     * @return the synctimestamp
     */
    public String getSynctimestamp() {
        return synctimestamp;
    }
    /**
     * @param synctimestamp the synctimestamp to set
     */
    public void setSynctimestamp(String synctimestamp) {
        this.synctimestamp = synctimestamp;
    }
    /**
     * @return the softdeleteflag
     */
    public String getSoftdeleteflag() {
        return softdeleteflag;
    }
    /**
     * @param softdeleteflag the softdeleteflag to set
     */
    public void setSoftdeleteflag(String softdeleteflag) {
        this.softdeleteflag = softdeleteflag;
    }
    @Override
    public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public Object loadDTO(String id) {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.REGION_GET, true, true);

        if (exts != null && exts.size() > 0) {
            return exts.get(0);
        }

        return null;
    }
    @Override
    public Object loadDTO() {
        if (StringUtils.isNotBlank(name)) {
            String filter = "Name" + DBPUtilitiesConstants.EQUAL + "'"+name+"'";
            List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.REGION_GET, true, true);

            if (exts != null && exts.size() > 0) {
                return exts.get(0);
            }
        } else if (StringUtils.isNotBlank(id)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + id;
            List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

            DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.REGION_GET    , true, true);

            if (exts != null && exts.size() > 0) {
                return exts.get(0);
            }
        }
        return null;
    }
}
