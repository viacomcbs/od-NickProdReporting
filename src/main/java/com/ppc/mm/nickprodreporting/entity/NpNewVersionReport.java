package com.ppc.mm.nickprodreporting.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "NP_NEW_VERSION_REPORT")
@Component
@Scope(value = "prototype")
public class NpNewVersionReport {

    private Integer id;
    private String uoiId;
    private String logicalUoiId;
    private String version;
    private String name;
    private String status;
    private String migratedFromMQ;
    private String fieldsValid;
    private String tabularValid;
    private String errorMsg;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "UOI_ID")
    public String getUoiId() {
        return uoiId;
    }

    public void setUoiId(String uoiId) {
        this.uoiId = uoiId;
    }
    @Column(name = "LOGICAL_UOI_ID")
    public String getLogicalUoiId() {
        return logicalUoiId;
    }

    public void setLogicalUoiId(String logicalUoiId) {
        this.logicalUoiId = logicalUoiId;
    }
    @Column(name = "VERSION")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "MIG_FROM_MQ")
    public String getMigratedFromMQ() {
        return migratedFromMQ;
    }

    public void setMigratedFromMQ(String migratedFromMQ) {
        this.migratedFromMQ = migratedFromMQ;
    }
    @Column(name = "FIELDS_VALID")
    public String getFieldsValid() {
        return fieldsValid;
    }

    public void setFieldsValid(String fieldsValid) {
        this.fieldsValid = fieldsValid;
    }
    @Column(name = "TABULAR_VALID")
    public String getTabularValid() {
        return tabularValid;
    }

    public void setTabularValid(String tabularValid) {
        this.tabularValid = tabularValid;
    }
    @Column(name = "ERROR_MSG")
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
