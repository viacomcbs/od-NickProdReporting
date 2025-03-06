package com.ppc.mm.nickprodmetacomparing.dao;

import com.ppc.mm.nickprodmetacomparing.entity.NickMetadataCompare;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MetadataDao {
    List<NickMetadataCompare> getFilesForMetadataCompare();

    String getUoiId(String filename, Long productionId);

    List<Object[]> getUoiForMerge();

    void updateUois(String uoiId, String logicalUoiId, String version, String isLatestVersion);

    void addToIndexer(String uoiId);

    void updateUoisNick(String uoiId);

    List<NickProdMetadataUpdateMarch25> getMetaDumpData();

    void saveObject(NickProdMetadataUpdateMarch25 nickObject);

    Integer getId(String condition, String tableName, String columnSelect, String searchCol);

}
