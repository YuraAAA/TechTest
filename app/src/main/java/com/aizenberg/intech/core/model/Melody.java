package com.aizenberg.intech.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public class Melody extends CommonIdsModel {

    private int artistId;
    private String artist;
    private String code;
    private String smsNumber;
    private String price;
    private float fPrice;
    private String prolongationPrice;
    private float fProlongationPrice;
    private String purchasePeriod;
    @JsonProperty("picUrl")
    private String pictureUrl;
    private String demoUrl;
    private boolean active;
    private int relevance;
    private String melodyId;
    private List<Tag> tags;

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public float getFprice() {
        return fPrice;
    }

    public void setFprice(float fprice) {
        this.fPrice = fprice;
    }

    public String getProlongationPrice() {
        return prolongationPrice;
    }

    public void setProlongationPrice(String prolongationPrice) {
        this.prolongationPrice = prolongationPrice;
    }

    public float getfProlongationPrice() {
        return fProlongationPrice;
    }

    public void setfProlongationPrice(float fProlongationPrice) {
        this.fProlongationPrice = fProlongationPrice;
    }

    public String getPurchasePeriod() {
        return purchasePeriod;
    }

    public void setPurchasePeriod(String purchasePeriod) {
        this.purchasePeriod = purchasePeriod;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public String getMelodyId() {
        return melodyId;
    }

    public void setMelodyId(String melodyId) {
        this.melodyId = melodyId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public static class Tag extends CommonIdsModel {

        private boolean limitedVisibility;
        private boolean isFullCatalogEnabled;
        private int topMelodiesCount;
        private boolean isBlockDisplayMode;

        public boolean isLimitedVisibility() {
            return limitedVisibility;
        }

        public void setLimitedVisibility(boolean limitedVisibility) {
            this.limitedVisibility = limitedVisibility;
        }

        public boolean isFullCatalogEnabled() {
            return isFullCatalogEnabled;
        }

        public void setFullCatalogEnabled(boolean fullCatalogEnabled) {
            isFullCatalogEnabled = fullCatalogEnabled;
        }

        public int getTopMelodiesCount() {
            return topMelodiesCount;
        }

        public void setTopMelodiesCount(int topMelodiesCount) {
            this.topMelodiesCount = topMelodiesCount;
        }

        public boolean isBlockDisplayMode() {
            return isBlockDisplayMode;
        }

        public void setBlockDisplayMode(boolean blockDisplayMode) {
            isBlockDisplayMode = blockDisplayMode;
        }
    }

}
