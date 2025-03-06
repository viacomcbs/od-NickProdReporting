package com.ppc.mm.nickprodreporting.service;

import com.ppc.mm.nickprodmessaging.entity.AssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata2;
import com.ppc.mm.nickprodreporting.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ReportingService {

    List<MMInboundReport> getMMInboundReports(String migrated);

    Object getById(Integer id, Class<?> clazz);

    void saveTabular(List<MMInboundTabularReport> tabularReports);

    void saveEntity(NickProdUploadMsgEntity entity);

    void saveReport(MMInboundReport report);

    void saveResponse(MMInboundResponse inboundResponse);

    List<String> getUoiId(String fileName);

    NickProdUploadMsgEntity getByQueueId(Integer id);

    MMInboundResponse getMMInboundResponse(Integer id);

    List<MMInboundTabularReport> getMMTabularReports(Integer id);

    int checkCountTabValue(String column, String addValue, String uoiId, String table);

    AssetMetadata getAssetMetadata(String uoiId);

    NickProdAssetMetadata getNickProdAssetMetadata(String uoiId);

    NickProdAssetMetadata2 getNickProdAdditionalMetadata(String uoiId);

    List<MMInboundReport> getFailedReports(String invalidResponse);

    List<MMInboundResponse> getMMInboundResponseList(Integer id);

    void removeObject(MMInboundResponse mmInboundResponse);

    List<String> isFileNameExist(String fileNameCompare, String prodID);

    List<NickObjects> getNickObjects(String no);

    void saveObject(NickObjects nickObject);

    List<Object[]> getNickObjectList(String objectId);

    void updateUois(String originalAsset, String updatedAsset);

    void addToIndexer(List<String> assets);

    Integer getVersoinedNickObjects(String objectId);

    List<NickObjects> getNickObjectsForReprocess(String yes);

    void updateUoisNew(String uoiId1, String uoiId2);
}
