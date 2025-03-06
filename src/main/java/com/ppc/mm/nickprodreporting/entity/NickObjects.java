package com.ppc.mm.nickprodreporting.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NICK_OBJECTS_NEW", schema = "TOWNER")
public class NickObjects {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "CONTENT_CHECKSUM")
    private String objectId;

    @Column(name = "UOI_COUNT")
    private Integer uoiCount;

    @Column(name = "PROCESSED")
    private String processed;

    @Column(name = "PROCESSED_DATE")
    private Date processedDate;

    @Column(name = "UPDATED_ID")
    private String updatedIds;

    @Column(name = "IS_DUPLICATE")
    private String isDuplicate;

    @Column(name = "VERSIONED")
    private String versioned;

    @Column(name = "ERROR_MSG")
    private String errorMsg;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getUoiCount() {
        return uoiCount;
    }

    public void setUoiCount(Integer uoiCount) {
        this.uoiCount = uoiCount;
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

    public String getUpdatedIds() {
        return updatedIds;
    }

    public void setUpdatedIds(String updatedIds) {
        this.updatedIds = updatedIds;
    }

    public String getIsDuplicate() {
        return isDuplicate;
    }

    public void setIsDuplicate(String isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    public String getVersioned() {
        return versioned;
    }

    public void setVersioned(String versioned) {
        this.versioned = versioned;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
