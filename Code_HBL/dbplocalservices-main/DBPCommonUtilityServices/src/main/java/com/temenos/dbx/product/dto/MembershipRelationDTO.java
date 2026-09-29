package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class MembershipRelationDTO implements DBPDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 5538819073457139422L;

    private String id;
    private String membershipId;
    private String relatedMebershipId;
    private String relationshipId;
    private String relationshipName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getRelatedMebershipId() {
        return relatedMebershipId;
    }

    public void setRelatedMebershipId(String relatedMebershipId) {
        this.relatedMebershipId = relatedMebershipId;
    }

    public String getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
