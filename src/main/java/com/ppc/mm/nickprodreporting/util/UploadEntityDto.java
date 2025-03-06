package com.ppc.mm.nickprodreporting.util;

import com.ppc.mm.nickprodreporting.entity.MMInboundTabularReport;
import com.ppc.mm.nickprodreporting.entity.NickProdUploadMsgEntity;

import java.util.List;

public class UploadEntityDto {

    private NickProdUploadMsgEntity entity;
    private List<MMInboundTabularReport> tabulerReports;

    public NickProdUploadMsgEntity getEntity() {
        return entity;
    }

    public void setEntity(NickProdUploadMsgEntity entity) {
        this.entity = entity;
    }

    public List<MMInboundTabularReport> getTabulerReports() {
        return tabulerReports;
    }

    public void setTabulerReports(List<MMInboundTabularReport> tabulerReports) {
        this.tabulerReports = tabulerReports;
    }
}
