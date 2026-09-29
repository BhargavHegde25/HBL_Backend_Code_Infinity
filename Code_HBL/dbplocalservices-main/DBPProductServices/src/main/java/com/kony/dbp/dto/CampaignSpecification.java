package com.kony.dbp.dto;

public class CampaignSpecification {

    private String campaignId;
    private String campaignPlaceholderId;
    private String imageIndex;
    private String imageURL;
    private String destinationURL;

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignPlaceholderId() {
        return campaignPlaceholderId;
    }

    public void setCampaignPlaceholderId(String campaignPlaceholderId) {
        this.campaignPlaceholderId = campaignPlaceholderId;
    }

    public String getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(String imageIndex) {
        this.imageIndex = imageIndex;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDestinationURL() {
        return destinationURL;
    }

    public void setDestinationURL(String destinationURL) {
        this.destinationURL = destinationURL;
    }
}
