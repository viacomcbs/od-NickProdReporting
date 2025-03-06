package com.ppc.mm.nickprodreporting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppc.mm.common.util.Constants;
import com.ppc.mm.nickprodmessaging.entity.AssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.MMInboundQueue;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata2;
import com.ppc.mm.nickprodmessaging.util.NickProdUploadMsgInboundEntity;
import com.ppc.mm.nickprodmessaging.util.NickProdUploadMsgOutboundEntity;
import com.ppc.mm.nickprodreporting.entity.*;
import com.ppc.mm.nickprodreporting.service.ReportingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author tiwarira
 * */

@Component
public class ProcessDuplicateAssets {

    @Autowired
    UtilityForReports utility;

    @Autowired
    PropertyResolver environment;

    @Autowired
    ReportingService reportingService;
    
    private ObjectMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(ProcessDuplicateAssets.class);


    public void getReports(List<MMInboundReport> mmInboundReports) {
        log.info("in getReports process");
        for (MMInboundReport report : mmInboundReports) {
            MMInboundQueue mmInboundQueue = (MMInboundQueue) reportingService.getById(report.getId(), MMInboundQueue.class);
            log.info("mmInboundQueue {}",mmInboundQueue.getId());
            NickProdUploadMsgInboundEntity entity = mapJsonToEntity(mmInboundQueue);
            if (entity != null){
                log.info("entity not null");
                UploadEntityDto dto = utility.getInboundEntity(entity, mmInboundQueue.getId());
                saveInboundEntity(dto);
            }
            report.setMigrated(Constants.YES);
            reportingService.saveReport(report);
            validateResponse(mmInboundQueue);
        }
    }

    private void validateResponse(MMInboundQueue mmInboundQueue) {

        MMInboundResponse inboundResponse = new MMInboundResponse();
        inboundResponse.setMmQueueId(mmInboundQueue.getId());

        if (StringUtils.isBlank(mmInboundQueue.getResponse())){
            inboundResponse.setValidResponse(Constants.NO);
            inboundResponse.setMigrated(Constants.YES);
        } else{
            NickProdUploadMsgOutboundEntity uploadMsgOutbound =
                    mapResponseJsonToEntity(mmInboundQueue.getResponse());
            if (uploadMsgOutbound == null) {
                inboundResponse.setMigrated(Constants.NO);
            } else {
                inboundResponse.setMigrated(Constants.YES);
                updateResponseFromEntity(inboundResponse, uploadMsgOutbound);
            }
        }

        reportingService.saveResponse(inboundResponse);

    }

    private void updateResponseFromEntity(MMInboundResponse inboundResponse, NickProdUploadMsgOutboundEntity uploadMsgOutbound) {

        inboundResponse.setMessageType(uploadMsgOutbound.getMessageType());
        inboundResponse.setFmQueueId(uploadMsgOutbound.getFmQueueId());
        inboundResponse.setStatus(uploadMsgOutbound.getStatus());
        inboundResponse.setOtid(uploadMsgOutbound.getOtid());
        inboundResponse.setErrorMessage(uploadMsgOutbound.getErrorMessage());
        inboundResponse.setErrorCode(uploadMsgOutbound.getErrorCode());

    }

    private NickProdUploadMsgOutboundEntity mapResponseJsonToEntity(String response) {
        NickProdUploadMsgOutboundEntity messageEntity = null;
        try {
            if(mapper == null){
                mapper = new ObjectMapper();
            }
            messageEntity = mapper.readValue(response, NickProdUploadMsgOutboundEntity.class);
            //Object o = new ObjectMapper().createObjectNode()
        } catch (JsonProcessingException e) {
            log.error("Error while parsing mapResponseJsonToEntity data {}",e.toString());
            //return e.getMessage();
        }
        return messageEntity;

    }

    private void saveInboundEntity(UploadEntityDto dto) {
        log.info("in saveInboundEntity");
        if (!CollectionUtils.isEmpty(dto.getTabulerReports())){
            reportingService.saveTabular(dto.getTabulerReports());
        }
        if (dto.getEntity().getQueueId() != null) {
            log.info("before saveEntity");
            reportingService.saveEntity(dto.getEntity());
        }
    }

    private NickProdUploadMsgInboundEntity mapJsonToEntity(MMInboundQueue queue) {
        NickProdUploadMsgInboundEntity messageEntity = null;
        try {
            if(mapper == null){
                mapper = new ObjectMapper();
            }
            messageEntity = mapper.readValue(queue.getRequest(), NickProdUploadMsgInboundEntity.class);
            //Object o = new ObjectMapper().createObjectNode()
        } catch (JsonProcessingException e) {
            log.error("Error while parsing JSON data {}",e.getMessage());
            //return e.getMessage();
        }
        return messageEntity;
    }

    public void validateReports(List<MMInboundReport> mmInboundReports) {
        //log.info("size in validateReports{}",mmInboundReports.size());

        for (MMInboundReport report : mmInboundReports) {
            validateEntity(report);
        }

    }

    private void validateEntity(MMInboundReport report) {

        try {

            log.info("processing report {}", report.getId());
            NickProdUploadMsgEntity entity = reportingService.getByQueueId(report.getId());

            if(entity != null) {
                log.info("entity found {}", entity.getId());
                boolean validResponse = false;
                String errorMsg = null;

                entity.setForValidation("Y");
                reportingService.saveEntity(entity);
                log.info("Entity updated");
                List<String> uoids;
                if (entity.getMessageType().equals(Constants.METADATA_UPDATE)) {
                     uoids = reportingService.isFileNameExist(entity.getFileNameCompare(),entity.getProdID());
                } else {

                    String fileName = entity.getFilePath().substring(entity.getFilePath().lastIndexOf('\\') + 1);
                    log.info("fileName {}", fileName);
                    uoids = reportingService.getUoiId(fileName);
                }
                if (CollectionUtils.isEmpty(uoids)) {
                    report.setStatus(Constants.FAILED);
                    report.setErrorMsg(Constants.NO_ASSET_FOUND);
                    log.info("No asset found");
                } else {

                //    if (uoids.size() == 1) {
                        String uoiId = uoids.get(0).trim();
                        report.setOtIdByFileName(uoiId);
                        log.info("uoi_id not null size 1 {} ", uoiId);

                        //MMInboundResponse response = getUniqueResponse(report.getId());
                //    List<MMInboundResponse> responseList = reportingService.getMMInboundResponseList(report.getId());

                    //validate response
                        //if (response != null) {
 /*                   if (!CollectionUtils.isEmpty(responseList)) {
                        if (responseList.size() == 1){
                            MMInboundResponse response = responseList.get(0);
                                    log.info("response found {}", response.getId());

                                if (StringUtils.isNotBlank(response.getOtid()) && response.getOtid().equals(uoiId)) {
                                    response.setValidResponse(Constants.YES);

                                } else {
                                    response.setValidResponse(Constants.NO);
                                    report.setErrorMsg("INVALID RESPONSE");
                                }

                                reportingService.saveResponse(response);
                        } else {
                            report.setErrorMsg("DUPLICATE RESPONSE");

                                }
                            log.info("Response updated");
                    } else {
                            report.setErrorMsg("NO RESPONSE SENT");
                            report.setStatus(Constants.FAILED);
                    }

*/
                        //validate fields
                        //List<MMInboundTabularReport> tabularReports = reportingService.getMMTabularReports(report.getId());
                        //validateTabularFields(tabularReports, uoiId);
                        errorMsg = validateUploadMessageEntity(entity, uoiId);

                        if (StringUtils.isNotBlank(errorMsg)) {
                            //    log.info("Error msg not null");
                            report.setFieldsValid("N");
                            report.setStatus(Constants.FAILED);
                            if (StringUtils.isNotBlank(report.getErrorMsg())) {
                                report.setErrorMsg(report.getErrorMsg() + " " + errorMsg);
                            } else {
                                report.setErrorMsg(errorMsg);
                            }

                        } else {
                            report.setFieldsValid("Y");
                            report.setStatus(Constants.SUCCESS);
                        }

                /*    } else {
                        report.setStatus(Constants.FAILED);
                        report.setErrorMsg(Constants.NO_UNIQUE);
                    }*/
                }
            } else {
                report.setStatus(Constants.FAILED);
                report.setErrorMsg(Constants.NO_ENTITY);
            }
            report.setValidationDate(new Date());
            reportingService.saveReport(report);
        //    log.info("Report updated");
        }catch (RuntimeException e) {
            log.error("Error while parsing report {}",e.toString());
        }
    }

    private MMInboundResponse getUniqueResponse(Integer id) {

        List<MMInboundResponse> responseList = reportingService.getMMInboundResponseList(id);
        if (!CollectionUtils.isEmpty(responseList) && responseList.size() > 1) {

            for (int i = 1; i < responseList.size(); i++) {
                deleteDuplicateResponse(responseList.get(i));
            }
        }
       return  responseList.get(0);
    }

    private void deleteDuplicateResponse(MMInboundResponse mmInboundResponse) {
        reportingService.removeObject(mmInboundResponse);
    }

    private String validateUploadMessageEntity(NickProdUploadMsgEntity entity, String uoiId) {
        String errorMsg = null;
        String env = environment.getRequiredProperty("env");
        if (env.equals("PROD")){
        //    log.info("env {}",env);
            NickProdAssetMetadata assetMetadata = reportingService.getNickProdAssetMetadata(uoiId);
            NickProdAssetMetadata2 nickProdAssetMetadata2 = reportingService.getNickProdAdditionalMetadata(uoiId);
            if (assetMetadata != null && nickProdAssetMetadata2 != null){
                errorMsg = utility.compareProdMetadata(entity, assetMetadata, nickProdAssetMetadata2);
            } else {
                errorMsg = "Metadata Record Missing";
            }
            log.info("Error Msg null {}",StringUtils.isBlank(errorMsg));
        } else {
            log.info("In Uat {}",env);
            AssetMetadata assetMetadata = reportingService.getAssetMetadata(uoiId);
            if (assetMetadata != null){
                //log.info(assetMetadata.toString());
                errorMsg = utility.compareMetadata(entity, assetMetadata);

            }
        }

        return errorMsg;
    }

    private void validateTabularFields(List<MMInboundTabularReport> tabularReports, String uoiId) {

        for (MMInboundTabularReport tabularReport : tabularReports) {

            switch (tabularReport.getFieldType()) {
                case Constants.MATERIAL:
                    log.info("Material Present");
                    break;
                case Constants.POST_EPISODE: {
                    //select * from NICK_PROD_POST_EPISODE
                    String column = "POST_EPS_ID";
                    String table = "NICK_PROD_POST_EPISODE";
                    if (StringUtils.isNotBlank(tabularReport.getAddValue()) && !tabularReport.getAddValue().equals("?")) {
                        int count = reportingService.checkCountTabValue(column, tabularReport.getAddValue(), uoiId, table);
                        if (count != 0) {
                            tabularReport.setValid(Constants.YES);
                        }else {
                            tabularReport.setValid(Constants.NO);
                        }
                    }

                    break;
                }
                case Constants.PROD_SHOW: {

                    String column = "REUSE_PROD_SHOW_ID";
                    String table = "NICK_PROD_REUSE_PROD_SHOW";
                    if (StringUtils.isNotBlank(tabularReport.getAddValue()) && !tabularReport.getAddValue().equals("?")) {
                        int count = reportingService.checkCountTabValue(column, tabularReport.getAddValue(), uoiId, table);
                        if (count != 0) {
                            tabularReport.setValid(Constants.YES);
                        }else {
                            tabularReport.setValid(Constants.NO);
                        }
                    }
                    break;
                }
                case Constants.KEYWORDS: {
                    String column = "KEYWORD";
                    String table = "MM_ASSET_KEYWORD";
                    if (StringUtils.isNotBlank(tabularReport.getAddValue()) && !tabularReport.getAddValue().equals("?")) {
                        int count = reportingService.checkCountTabValue(column, tabularReport.getAddValue(), uoiId, table);
                        if (count != 0) {
                            tabularReport.setValid(Constants.YES);
                        }else {
                            tabularReport.setValid(Constants.NO);
                        }
                    }
                    break;
                }
            }

        }

        reportingService.saveTabular(tabularReports);
    }

    public void validateFailedReports(List<MMInboundReport> mmInboundReports) {

        for (MMInboundReport report : mmInboundReports) {
            validateEntityForFailedResponse(report);
        }
    }

    private void validateEntityForFailedResponse(MMInboundReport report) {

        try {

            log.info("processing failed report {}", report.getId());
            NickProdUploadMsgEntity entity = reportingService.getByQueueId(report.getId());
            log.info("entity for failed is found {}",entity.getId());
            boolean validResponse = false;
            String errorMsg = null;


            MMInboundQueue mmInboundQueue = (MMInboundQueue) reportingService.getById(report.getId(), MMInboundQueue.class);
            if (mmInboundQueue.getImportJobId() != null || mmInboundQueue.getImportJobId() != 0){
                log.info("mmInboundQueue jobId {}", mmInboundQueue.getImportJobId());

                OtmmJobAssets jobAssets = (OtmmJobAssets) reportingService.getById(mmInboundQueue.getImportJobId(), OtmmJobAssets.class);
                String uoiId = null;
                if (jobAssets != null && StringUtils.isNotBlank(jobAssets.getUoiId())){
                    log.info("jobAsset not null");
                    uoiId = jobAssets.getUoiId();
                    MMInboundResponse response = reportingService.getMMInboundResponse(report.getId());
                    if (response != null) {
                        log.info("response found {}",response.getId());

                        if (StringUtils.isNotBlank(response.getOtid()) && response.getOtid().equals(jobAssets.getUoiId())) {
                            log.info("uoi_id matched");
                            response.setValidResponse(Constants.YES);
                            validResponse = true;
                        }else {
                            response.setValidResponse(Constants.NO);
                        }
                        reportingService.saveResponse(response);
                    } else {
                        report.setErrorMsg("NO RESPONSE SENT");
                        report.setStatus(Constants.FAILED);
                    }
                } else {
                    log.info("jobAsset null");
                    String newErrorMsg = report.getErrorMsg().concat(" ").concat(Constants.INVALID_JOB_ID);
                    log.info("newErrorMsg {}",newErrorMsg);
                    report.setErrorMsg(newErrorMsg);
                }


                if (validResponse) {
                    log.info("response is valid");
                    List<MMInboundTabularReport> tabularReports = reportingService.getMMTabularReports(report.getId());
                    validateTabularFields(tabularReports, uoiId);
                    errorMsg = validateUploadMessageEntity(entity, uoiId);
                    log.info("error msg {}",errorMsg);
                    report.setStatus(Constants.SUCCESS);

                    if (StringUtils.isNotBlank(errorMsg)) {
                        report.setFieldsValid("N");
                        report.setErrorMsg(errorMsg);
                    } else {
                        log.info("response is valid and error meg is null");
                        report.setFieldsValid("Y");
                        report.setErrorMsg(null);
                    }
                }/* else {
                    report.setStatus(Constants.FAILED);
                    report.setErrorMsg("INVALID RESPONSE");
                }*/
            }

            reportingService.saveReport(report);
        }catch (RuntimeException e) {
            log.error("Error while validateEntityForFailedResponse report {}",e.toString());
        }
    }

    public void validateNickObjects(List<NickObjects> nickObjects) {
        for (NickObjects nickObject : nickObjects) {
            log.info("NickObject {}", nickObject.getObjectId());
            processNickObject(nickObject);
        }
    }

    private void processNickObject(NickObjects nickObject) {
    log.info("processNickObject");
    String isDuplicate = null;

        List<String> updatedId = new ArrayList<>();
        nickObject.setProcessedDate(new Date());
        nickObject.setProcessed("Y");

        try {
        List<Object[]> objectList = reportingService.getNickObjectList(nickObject.getObjectId());

            if (!CollectionUtils.isEmpty(objectList)) {
                log.info("Object array not null ");
                for (Object[] objects : objectList) {
                    updatedId.add(objects[0].toString());
                }
                nickObject.setUpdatedIds(String.join(",", updatedId));

                isDuplicate = checkForDuplicate(objectList);
                log.info("After isDuplicate {}", isDuplicate);
                if (StringUtils.isBlank(isDuplicate)){
                    nickObject.setIsDuplicate("Y");
                    boolean versioned = updateVersion(objectList);
                    log.info("versioned {}", versioned);
                    if (versioned) {
                        nickObject.setVersioned("Y");
                    } else {
                        nickObject.setVersioned("N");
                    }
                } else {
                    nickObject.setErrorMsg(isDuplicate);
                    nickObject.setIsDuplicate("N");
                    nickObject.setVersioned("N");
                }

            } else {
                nickObject.setErrorMsg("NO OBJECT FOUND");
                nickObject.setVersioned("N");
            }
        }catch (RuntimeException e) {
            nickObject.setErrorMsg("NPE");
            nickObject.setVersioned("N");
            //reportingService.saveObject(nickObject);
            log.error("Error while processNickObject report {}", e.toString());
        }

        reportingService.saveObject(nickObject);

    }

    private boolean updateVersion(List<Object[]> objectList) {
        log.info("updateVersion");
        boolean versioned = false;
        try {
            String originalAsset = objectList.get(0)[0].toString();
            String updatedAsset = objectList.get(1)[0].toString();

//            reportingService.updateUois(originalAsset, updatedAsset);
            reportingService.updateUois(originalAsset, updatedAsset);

//            reportingService.addToIndexer(Arrays.asList(originalAsset, updatedAsset));
            versioned = true;
            log.info("setting version true");
        }catch (RuntimeException e) {
            log.error("Error while updateVersion report {}", e.toString());
        }
        return versioned;
    }

    private String checkForDuplicate(List<Object[]> objectList) {
        log.info("checkForDuplicate");
        NickProdAssetMetadata assetMetadata = reportingService.getNickProdAssetMetadata(objectList.get(0)[0].toString());
        NickProdAssetMetadata assetMetadata1 = reportingService.getNickProdAssetMetadata(objectList.get(1)[0].toString());

        if (assetMetadata == null || assetMetadata1 == null){
            return "METADATA MISSING";
        }

        boolean result = utility.checkDupilateMetadata(assetMetadata, assetMetadata1);
        log.info("after result = {}",result);
         if (!(objectList.get(0)[1].equals(objectList.get(1)[1]) && result)){
             return "METADATA MISMATCH";
         }
        return null;
    }

    public void validateProcessedNickObjects(List<NickObjects> nickObjects) {

        for (NickObjects nickObject : nickObjects) {
            log.info("NickObject {}", nickObject.getObjectId());
            validateProcessedNickObject(nickObject);
        }

    }

    private void validateProcessedNickObject(NickObjects nickObject) {

        log.info("validateProcessedNickObject");
        Integer versionCount = reportingService.getVersoinedNickObjects(nickObject.getObjectId());

        if (Objects.equals(versionCount, nickObject.getUoiCount())) {

            nickObject.setErrorMsg("Versioned");
        }else {
            nickObject.setErrorMsg("Not Versioned");
        }

        reportingService.saveObject(nickObject);
    }

    public void reprocessUnversionedNickObjects(List<NickObjects> nickObjects) {
        for (NickObjects nickObject : nickObjects) {
            log.info("reprocessUnversionedNickObjects {}", nickObject.getObjectId());

            List<Object[]> objectList = reportingService.getNickObjectList(nickObject.getObjectId());

            String uoi_id_1 = objectList.get(0)[0].toString();
            String uoi_id_2 = objectList.get(1)[0].toString();

            reportingService.updateUoisNew(uoi_id_1, uoi_id_2);

            nickObject.setProcessedDate(new Date());
            nickObject.setErrorMsg(null);
            reportingService.saveObject(nickObject);
        }
    }

    public void reprocessNonDuplicateNickObjects(List<NickObjects> nickObjects) {

        for (NickObjects nickObject : nickObjects) {
            log.info("NickObject in reprocessNonDuplicateNickObjects {}", nickObject.getObjectId());
            processNickObject(nickObject);
        }
    }
}
