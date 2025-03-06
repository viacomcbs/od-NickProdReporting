package com.ppc.mm.nickprodmetacomparing.entity;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NICK_METADATA_COMPARE", schema = "TOWNER")
@Component
public class NickMetadataCompare {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;  // NUMBER(10)

    @Column(name = "FILENAME", length = 500)
    private String filename;  // VARCHAR2(500 BYTE)

    @Column(name = "PRODUCTIONID", nullable = false)
    private Long productionId;  // NUMBER(10)

    @Column(name = "CLEANEDMESSAGE", length = 4000)
    private String cleanedMessage;  // VARCHAR2(4000 BYTE)

    @Column(name = "SHEETNAME", length = 100)
    private String sheetName;  // VARCHAR2(100 BYTE)

    @Column(name = "PROCESSED", length = 10)
    private String processed;  // VARCHAR2(10 BYTE)

    @Column(name = "PROCESSED_DATE")
    @Temporal(TemporalType.DATE)
    private Date processedDate;  // DATE

    @Column(name = "FIELDS_MISSING", length = 2000)
    private String fieldsMissing;  // VARCHAR2(2000 BYTE)

    @Column(name = "FIELDS_ERRORED", length = 2000)
    private String fieldsErrored;  // VARCHAR2(2000 BYTE)

    @Column(name = "ERROR_MSG", length = 2000)
    private String errorMsg;  // VARCHAR2(2000 BYTE)

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getProductionId() {
        return productionId;
    }

    public void setProductionId(Long productionId) {
        this.productionId = productionId;
    }

    public String getCleanedMessage() {
        return cleanedMessage;
    }

    public void setCleanedMessage(String cleanedMessage) {
        this.cleanedMessage = cleanedMessage;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
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

    public String getFieldsMissing() {
        return fieldsMissing;
    }

    public void setFieldsMissing(String fieldsMissing) {
        this.fieldsMissing = fieldsMissing;
    }

    public String getFieldsErrored() {
        return fieldsErrored;
    }

    public void setFieldsErrored(String fieldsErrored) {
        this.fieldsErrored = fieldsErrored;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String toString() {
        return "NickMetadataCompare{" +
                "id=" + id +
                ", filename='" + filename + '\'' +
                ", productionId=" + productionId +
                ", cleanedMessage='" + cleanedMessage + '\'' +
                ", sheetName='" + sheetName + '\'' +
                ", processed='" + processed + '\'' +
                ", processedDate=" + processedDate +
                ", fieldsMissing='" + fieldsMissing + '\'' +
                ", fieldsErrored='" + fieldsErrored + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
