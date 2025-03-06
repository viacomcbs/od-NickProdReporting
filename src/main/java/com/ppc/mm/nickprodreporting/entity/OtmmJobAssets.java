package com.ppc.mm.nickprodreporting.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "OTMM_JOB_ASSETS")
@Component
@Scope(value = "prototype")
public class OtmmJobAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "JOB_ID", unique = true, nullable = false, precision = 10, scale = 0)
    private Integer job_id;

    @Column(name = "UOI_ID")
    private String uoiId;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public String getUoiId() {
        return uoiId;
    }

    public void setUoiId(String uoiId) {
        this.uoiId = uoiId;
    }

}
