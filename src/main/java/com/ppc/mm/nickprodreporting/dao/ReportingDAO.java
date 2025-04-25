package com.ppc.mm.nickprodreporting.dao;

import com.ppc.mm.nickprodmessaging.entity.AssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata2;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetaValidation25;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import com.ppc.mm.nickprodreporting.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReportingDAO {

List<MMInboundReport> getInboundReportMsgs(String migrated);

void saveTabular(List<MMInboundTabularReport> tabularReports);

void saveEntity(NickProdUploadMsgEntity entity);

void saveReport(MMInboundReport report);

void saveResponse(MMInboundResponse inboundResponse);

    Object getById(Integer id, Class<?> clazz);

    List<String> getUoiId(String fileName);

    NickProdUploadMsgEntity getByQueueId(Integer id);

    MMInboundResponse getMMInboundResponse(Integer id);

    List<MMInboundTabularReport> getMMTabularReports(Integer id);

    int countTabValue(String column, String addValue, String uoiId, String table);

    AssetMetadata getAssetMetadata(String uoiId);

    NickProdAssetMetadata2 getNickProdAdditionalMetadata(String uoiId);

    NickProdAssetMetadata getNickProdAssetMetadata(String uoiId);

    List<MMInboundReport> getgetFailedReports(String invalidResponse);

    List<MMInboundResponse> getMMInboundResponseList(Integer id);

    void removeObject(MMInboundResponse mmInboundResponse);

    List<String> isFileNameExist(String fileName, String prodID);

    List<NickObjects> getNickObjects(String migrated);

    void saveObject(NickObjects nickObject);

    List<Object[]> getNickObjectList(String objectId);

    void updateUois(String originalAsset, String updatedAsset);

    void addToIndexer(List<String> assets);

    Integer getVersoinedNickObjects(String objectId);

    List<NickObjects> getNickObjectsForReprocess(String yes);

    void updateUoisNew(String uoiId1, String uoiId2);

    List<NickProdMetaValidation25> getValidationReport();

    void saveObject(NickProdMetaValidation25 nickObject);

    void updateMetadata(String sent, String otid, String column);

    void updateMetadata2(String sent, String otid, String column);

    NickProdMetadataUpdateMarch25 getMetadataFromDump(String otid);
}
