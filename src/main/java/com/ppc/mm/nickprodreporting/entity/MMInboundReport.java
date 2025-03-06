package com.ppc.mm.nickprodreporting.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "MM_INBOUND_REPORT")
@Component
@Scope(value = "prototype")
public class MMInboundReport {

    private Integer id;

    private String status;
    private String migrated;
    private String errorMsg;
    private String fieldsValid;
    private String tabularValid;
    private String otIdByFileName;
    private Date validationDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "MIGRATED")
    public String getMigrated() {
        return migrated;
    }

    public void setMigrated(String migrated) {
        this.migrated = migrated;
    }
    @Column(name = "ERROR_MSG")
    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
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

    @Column(name = "OTID_BY_FILENAME")
    public String getOtIdByFileName() {
        return otIdByFileName;
    }

    public void setOtIdByFileName(String otIdByFileName) {
        this.otIdByFileName = otIdByFileName;
    }

    @Column(name = "VALIDATED_DATE")
    public Date getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(Date validationDate) {
        this.validationDate = validationDate;
    }
}
