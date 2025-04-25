package com.ppc.mm.nickprodmetacomparing.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NICK_PROD_METADATA_UPDATE_MARCH25", schema = "TOWNER")
public class NickProdMetadataUpdateMarch25 {

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "OTID", length = 200)
    private String otid;

    @Lob
    @Column(name = "METADATA")
    private String metadata;

    @Column(name = "PROCESSED", length = 100)
    private String processed;

    @Temporal(TemporalType.DATE)
    @Column(name = "PROCESSED_DATE")
    private Date processedDate;

    @Column(name = "ENTITY_ID", length = 1000)
    private String entityId;

    @Column(name = "ERROR_MSG", length = 2000)
    private String errorMsg;

    @Column(name = "BATCH")
    private String batch;
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOtid() {
        return otid;
    }

    public void setOtid(String otid) {
        this.otid = otid;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    @Override
    public String toString() {
        return "NickProdMetadataUpdateMarch25{" +
                "id=" + id +
                ", otid='" + otid + '\'' +
                ", metadata='" + metadata + '\'' +
                ", processed='" + processed + '\'' +
                ", processedDate=" + processedDate +
                ", entityId='" + entityId + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", batch='" + batch + '\'' +
                '}';
    }
}
