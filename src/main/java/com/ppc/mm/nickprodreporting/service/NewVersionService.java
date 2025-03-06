package com.ppc.mm.nickprodreporting.service;

import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodreporting.entity.NpNewVersionReport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NewVersionService {
    List<NpNewVersionReport> getNewVersionReport();

    String getUoiId(NpNewVersionReport mmInboundReport);

    NickProdAssetMetadata getAssetMetadata(String uoiId);

    void saveNewVersionReport(NpNewVersionReport mmInboundReport);

    List<String> getFileNamesForProcess(Integer prodId);

    void executeProcedure(List<String> nickObjects);

    List<String> getUoiIds(String nickObject);

    void updateUois(String id, int counter, String isLatest, String original);

    void insertFileCompareObjects(String id);

    void updateFileNameObjects(String nickObject);
}
