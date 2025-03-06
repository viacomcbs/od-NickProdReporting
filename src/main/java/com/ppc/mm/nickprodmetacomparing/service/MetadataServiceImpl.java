package com.ppc.mm.nickprodmetacomparing.service;

import com.ppc.mm.nickprodmetacomparing.dao.MetadataDao;
import com.ppc.mm.nickprodmetacomparing.entity.NickMetadataCompare;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetadataServiceImpl implements MetadataService {

    @Autowired
    MetadataDao metadataDao;

    @Override
    public List<NickMetadataCompare> getFilesForMetadataCompare() {
        return metadataDao.getFilesForMetadataCompare();
    }

    @Override
    public String getUoiId(String filename, Long productionId) {
        return metadataDao.getUoiId(filename, productionId);
    }

    @Override
    public List<Object[]> getUoiForMerge() {
        return metadataDao.getUoiForMerge();
    }

    @Override
    public void updateUois(String uoiId, String logicalUoiId, String version, String isLatestVersion) {
        metadataDao.updateUois(uoiId, logicalUoiId, version, isLatestVersion);
    }

    @Override
    public void addToIndexer(String uoiId) {
        metadataDao.addToIndexer(uoiId);
    }

    @Override
    public void updateUoisNick(String uoiId) {
        metadataDao.updateUoisNick(uoiId);
    }

    @Override
    public List<NickProdMetadataUpdateMarch25> getMetaDumpData() {
        return metadataDao.getMetaDumpData();
    }

    @Override
    public void saveObject(NickProdMetadataUpdateMarch25 nickObject) {
        metadataDao.saveObject(nickObject);
    }

    public Integer getId(String condition, String tableName, String columnSelect, String searchCol) {
        return metadataDao.getId(condition, tableName, columnSelect, searchCol);
    }
}
