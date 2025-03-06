package com.ppc.mm.nickprodreporting.dao;

import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodreporting.entity.NpNewVersionReport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NewVersionDAO {


    List<NpNewVersionReport> getNewVersionReports();

    String getOldUoiId(String logicalUoiId, String version);

    NickProdAssetMetadata getAssetMetadata(String uoiId);

    void saveNewVersionReport(NpNewVersionReport mmInboundReport);

    List<String> getFileNamesForProcess(Integer prodId);

    void executeProcedure(List<String> nickObjects);

    List<String> getUoiIds(String nickObject);

    void updateUois(String id, int counter, String isLatest, String original);

    void insertFileCompareObjects(String id);

    void updateFileNameObjects(String nickObject);
}
