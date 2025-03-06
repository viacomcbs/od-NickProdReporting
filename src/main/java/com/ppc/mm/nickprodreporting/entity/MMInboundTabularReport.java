package com.ppc.mm.nickprodreporting.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "MM_INBOUND_TABULAR_REPORT")
@Component
@Scope(value = "prototype")
public class MMInboundTabularReport {

    private Integer id;
    private Integer queueId;
    private String messageType;
    private String fieldType;
    private String addValue;
    private String removeValue;
    private String removeAllValue;
    private String uoiId;
    private String valid;

    @Id
    @SequenceGenerator(name="NP_INBOUND_TABULAR_SEQ", sequenceName="NP_INBOUND_TABULAR_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="NP_INBOUND_TABULAR_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "MESSAGE_TYPE")
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    @Column(name = "FIELD_TYPE")
    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    @Column(name = "ADD_VALUE")
    public String getAddValue() {
        return addValue;
    }

    public void setAddValue(String addValue) {
        this.addValue = addValue;
    }

    @Column(name = "REMOVE_VALUE")
    public String getRemoveValue() {
        return removeValue;
    }

    public void setRemoveValue(String removeValue) {
        this.removeValue = removeValue;
    }

    @Column(name = "REMOVE_ALL_VAL")
    public String getRemoveAllValue() {
        return removeAllValue;
    }

    public void setRemoveAllValue(String removeAllValue) {
        this.removeAllValue = removeAllValue;
    }

    @Column(name = "UOI_ID")
    public String getUoiId() {
        return uoiId;
    }

    public void setUoiId(String uoiId) {
        this.uoiId = uoiId;
    }

    @Column(name = "QUEUE_ID")
    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    @Column(name = "VALID")
    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }
}
