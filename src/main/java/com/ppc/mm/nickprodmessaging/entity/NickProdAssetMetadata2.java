package com.ppc.mm.nickprodmessaging.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "NICK_PROD_ASSET_METATDATA2", schema = "TOWNER")
public class NickProdAssetMetadata2 {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "UOI_ID", length = 40)
    private String uoiId;

    @Column(name = "ASSET_DESCRIPTION", length = 4000)
    private String assetDescription;

    @Column(name = "IMAGE_LOCATION", length = 400)
    private String imageLocation;

    @Column(name = "FM_QUEUE_ID", length = 100)
    private String fmQueueId;

    // Getters and Setters
    public String getUoiId() {
        return uoiId;
    }

    public void setUoiId(String uoiId) {
        this.uoiId = uoiId;
    }

    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getFmQueueId() {
        return fmQueueId;
    }

    public void setFmQueueId(String fmQueueId) {
        this.fmQueueId = fmQueueId;
    }
}
