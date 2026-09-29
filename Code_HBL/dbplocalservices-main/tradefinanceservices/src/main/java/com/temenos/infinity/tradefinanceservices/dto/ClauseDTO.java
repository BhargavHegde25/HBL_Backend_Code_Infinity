/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClauseDTO implements DBPDTO {
    private static final long serialVersionUID = 689763772320897233L;

    private String clauseType;
    private String clauseDescription;
    private String clauseTitle;
    private String clauseId;

    public String getClauseId() {
        return clauseId;
    }

    public void setClauseId(String clauseId) {
        this.clauseId = clauseId;
    }

    public String getClauseType() {
        return clauseType;
    }

    public void setClauseType(String clauseType) {
        this.clauseType = clauseType;
    }

    public String getClauseDescription() {
        return clauseDescription;
    }

    public void setClauseDescription(String clauseDescription) {
        this.clauseDescription = clauseDescription;
    }

    public String getClauseTitle() {
        return clauseTitle;
    }

    public void setClauseTitle(String clauseTitle) {
        this.clauseTitle = clauseTitle;
    }

    public ClauseDTO() {
    }

    public ClauseDTO(String clauseType, String clauseDescription, String clauseTitle, String clauseId) {
        this.clauseType = clauseType;
        this.clauseDescription = clauseDescription;
        this.clauseTitle = clauseTitle;
        this.clauseId = clauseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClauseDTO)) return false;
        ClauseDTO clauseDTO = (ClauseDTO) o;
        return Objects.equals(getClauseType(), clauseDTO.getClauseType()) && Objects.equals(getClauseDescription(), clauseDTO.getClauseDescription()) && Objects.equals(getClauseTitle(), clauseDTO.getClauseTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClauseType(), getClauseDescription(), getClauseTitle());
    }
}
