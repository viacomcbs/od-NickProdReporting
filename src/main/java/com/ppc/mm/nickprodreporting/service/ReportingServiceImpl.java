package com.ppc.mm.nickprodreporting.service;

import com.ppc.mm.nickprodmessaging.entity.AssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata2;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetaValidation25;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import com.ppc.mm.nickprodreporting.dao.ReportingDAO;
import com.ppc.mm.nickprodreporting.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReportingServiceImpl implements ReportingService{

    @Autowired
    private ReportingDAO reportingDAO;

    @Override
    public List<MMInboundReport> getMMInboundReports(String migrated) {
        return reportingDAO.getInboundReportMsgs(migrated);
    }

    @Override
    public Object getById(Integer id, Class<?> clazz) {
        return reportingDAO.getById(id, clazz);

    }

    @Override
    public void saveTabular(List<MMInboundTabularReport> tabularReports) {
        reportingDAO.saveTabular(tabularReports);

    }

    @Override
    public void saveEntity(NickProdUploadMsgEntity entity) {
        reportingDAO.saveEntity(entity);

    }

    @Override
    public void saveReport(MMInboundReport report) {
        reportingDAO.saveReport(report);

    }

    @Override
    public void saveResponse(MMInboundResponse inboundResponse) {
        reportingDAO.saveResponse(inboundResponse);

    }

    @Override
    public List<String> getUoiId(String fileName) {
        return reportingDAO.getUoiId(fileName);
    }

    @Override
    public NickProdUploadMsgEntity getByQueueId(Integer id) {
        return reportingDAO.getByQueueId(id);
    }

    @Override
    public MMInboundResponse getMMInboundResponse(Integer id) {
        return reportingDAO.getMMInboundResponse(id);
    }

    @Override
    public List<MMInboundTabularReport> getMMTabularReports(Integer id) {
        return reportingDAO.getMMTabularReports(id);
    }

    @Override
    public int checkCountTabValue(String column, String addValue, String uoiId, String table) {
        return reportingDAO.countTabValue(column, addValue, uoiId, table);

    }

    @Override
    public AssetMetadata getAssetMetadata(String uoiId) {
        return reportingDAO.getAssetMetadata(uoiId);
    }

    @Override
    public NickProdAssetMetadata getNickProdAssetMetadata(String uoiId) {
        return reportingDAO.getNickProdAssetMetadata(uoiId);
    }

    @Override
    public NickProdAssetMetadata2 getNickProdAdditionalMetadata(String uoiId) {
        return reportingDAO.getNickProdAdditionalMetadata(uoiId);
    }

    @Override
    public List<MMInboundReport> getFailedReports(String invalidResponse) {
        return reportingDAO.getgetFailedReports(invalidResponse);
    }

    @Override
    public List<MMInboundResponse> getMMInboundResponseList(Integer id) {
        return reportingDAO.getMMInboundResponseList(id);
    }

    @Override
    public void removeObject(MMInboundResponse mmInboundResponse) {
        reportingDAO.removeObject(mmInboundResponse);
    }

    @Override
    public List<String> isFileNameExist(String fileName, String prodID) {
        return reportingDAO.isFileNameExist(fileName, prodID);
    }

    @Override
    public List<NickObjects> getNickObjects(String migrated) {
        return reportingDAO.getNickObjects(migrated);
    }

    @Override
    public void saveObject(NickObjects nickObject) {
        reportingDAO.saveObject(nickObject);
    }

    @Override
    public List<Object[]> getNickObjectList(String objectId) {
        return reportingDAO.getNickObjectList(objectId);
    }

    @Override
    public void updateUois(String originalAsset, String updatedAsset) {
        reportingDAO.updateUois(originalAsset, updatedAsset);
    }

    @Override
    public void addToIndexer(List<String> assets) {
        reportingDAO.addToIndexer(assets);
    }

    @Override
    public Integer getVersoinedNickObjects(String objectId) {

        return reportingDAO.getVersoinedNickObjects(objectId);
    }

    @Override
    public List<NickObjects> getNickObjectsForReprocess(String yes) {
        return reportingDAO.getNickObjectsForReprocess(yes);
    }

    @Override
    public void updateUoisNew(String uoiId1, String uoiId2) {
        reportingDAO.updateUoisNew(uoiId1, uoiId2);
    }

    @Override
    public List<NickProdMetaValidation25> getValidationReport() {
        return reportingDAO.getValidationReport();
    }

    @Override
    public void saveObject(NickProdMetaValidation25 nickObject) {
        reportingDAO.saveObject(nickObject);
    }

    @Override
    public void updateMetadataFromReprocess(String sent, String otid, String column) {
        reportingDAO.updateMetadata(sent, otid, column);
    }

    @Override
    public void updateMetadata2FromReprocess(String sent, String otid, String column) {
        reportingDAO.updateMetadata2(sent, otid, column);
    }

    @Override
    public NickProdMetadataUpdateMarch25 getMetadataFromDump(String otid) {
        return reportingDAO.getMetadataFromDump(otid);
    }
}
