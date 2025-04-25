package com.ppc.mm.nickprodmetacomparing.service;

import com.ppc.mm.nickprodmetacomparing.entity.NPMetaCompareTabularDataMar25;
import com.ppc.mm.nickprodmetacomparing.entity.NickMetadataCompare;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetaCompareEntityMar25;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MetadataService {
    List<NickMetadataCompare> getFilesForMetadataCompare();

    String getUoiId(String filename, Long productionId);

    List<Object[]> getUoiForMerge();

    void updateUois(String uoiId, String logicalUoiId, String version, String isLatestVersion);

    void addToIndexer(String uoiId);

    void updateUoisNick(String uoiId);

    List<NickProdMetadataUpdateMarch25> getMetaDumpData();

    void saveObject(NickProdMetadataUpdateMarch25 nickObject);

    Integer getId(String condition, String tableName, String columnSelect, String searchCol);

    List<NickProdMetadataUpdateMarch25> getMetaDumpDataForComparing();

    Long saveEntity(NickProdMetaCompareEntityMar25 uploadMsgEntity);

    void saveTabular(List<NPMetaCompareTabularDataMar25> tabularReports);
}
