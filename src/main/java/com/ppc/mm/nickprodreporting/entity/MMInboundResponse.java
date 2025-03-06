package com.ppc.mm.nickprodreporting.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "MM_INBOUND_RESPONSE")
@Component
@Scope(value = "prototype")
public class MMInboundResponse {

    private Integer id;
    private Integer mmQueueId;
    private String messageType;
    private Integer fmQueueId;
    private String status;
    private String otid;
    private String errorMessage;
    private String errorCode;
    private String migrated;
    private String validResponse;


    @Id
    @SequenceGenerator(name="NP_INBOUND_RESPONSE_SEQ", sequenceName="NP_INBOUND_RESPONSE_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="NP_INBOUND_RESPONSE_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "MM_QUEUE_ID")
    public Integer getMmQueueId() {
        return mmQueueId;
    }

    public void setMmQueueId(Integer mmQueueId) {
        this.mmQueueId = mmQueueId;
    }

    @Column(name = "MESSAGE_TYPE")
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Column(name = "FM_QUEUE_ID")
    public Integer getFmQueueId() {
        return fmQueueId;
    }

    public void setFmQueueId(Integer fmQueueId) {
        this.fmQueueId = fmQueueId;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "OTID")
    public String getOtid() {
        return otid;
    }

    public void setOtid(String otid) {
        this.otid = otid;
    }

    @Column(name = "ERROR_MESSAGE")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Column(name = "ERROR_CODE")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    @Column(name = "MIGRATED")
    public String getMigrated() {
        return migrated;
    }

    public void setMigrated(String migrated) {
        this.migrated = migrated;
    }

    @Column(name = "VALID_RESPONSE")
    public String getValidResponse() {
        return validResponse;
    }

    public void setValidResponse(String validResponse) {
        this.validResponse = validResponse;
    }
}
