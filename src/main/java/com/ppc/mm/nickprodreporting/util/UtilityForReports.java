package com.ppc.mm.nickprodreporting.util;

import com.ppc.mm.common.util.Constants;
import com.ppc.mm.nickprodmessaging.entity.AssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata2;
import com.ppc.mm.nickprodmessaging.util.*;
import com.ppc.mm.nickprodreporting.entity.MMInboundTabularReport;
import com.ppc.mm.nickprodreporting.entity.NickProdUploadMsgEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import java.util.*;

@Component
public class UtilityForReports {

    private static final Logger log = LoggerFactory.getLogger(UtilityForReports.class);

    public UploadEntityDto getInboundEntity(NickProdUploadMsgInboundEntity entity, Integer id) {
        log.info("In saveInboundEntity for id {} ",id);
        NickProdUploadMsgEntity uploadMsgEntity = new NickProdUploadMsgEntity();
        uploadMsgEntity.setQueueId(id);//----Int
        uploadMsgEntity.setMessageType(entity.getMessageType());
        uploadMsgEntity.setFmQueueId(Integer.valueOf(entity.getFmQueueId()));//------Int
        uploadMsgEntity.setFilePath(entity.getFilePath());
        uploadMsgEntity.setOtFolder(entity.getOtFolder());
        uploadMsgEntity.setOtid(entity.getOtid());
        uploadMsgEntity.setCategory(entity.getCategory());
        uploadMsgEntity.setCondition(entity.getCondition());
        uploadMsgEntity.setConditionNotes(entity.getConditionNotes());
        uploadMsgEntity.setAppraisedAt(entity.getAppraisedAt());
        uploadMsgEntity.setAppraisedBy(entity.getAppraisedBy());
        uploadMsgEntity.setAppraisedValue(entity.getAppraisedValue());
        uploadMsgEntity.setArtists(entity.getArtists());
        uploadMsgEntity.setAssetName(entity.getAssetName());
        uploadMsgEntity.setBarcode(entity.getBarcode());
        uploadMsgEntity.setBoxID(entity.getBoxID());
        uploadMsgEntity.setContextualNote(entity.getContextualNote());
        uploadMsgEntity.setCreationDate(entity.getCreationDate());
        uploadMsgEntity.setDateImported(entity.getDateImported());
        uploadMsgEntity.setEicAccess(entity.getEicAccess());
        uploadMsgEntity.setEicComments(entity.getEicComments());
        uploadMsgEntity.setEicReview(entity.getEicReview());
        uploadMsgEntity.setEicStatus(entity.getEicStatus());
        uploadMsgEntity.setFmid(entity.getFmid());
        uploadMsgEntity.setFilename(entity.getFilename());
        uploadMsgEntity.setFirstSeason(entity.getFirstSeason());
        uploadMsgEntity.setFirstShow(entity.getFirstShow());
        uploadMsgEntity.setHasDuplicates(entity.getHasDuplicates());
        uploadMsgEntity.setHeight(entity.getHeight());
        uploadMsgEntity.setImportedBy(entity.getImportedBy());
        uploadMsgEntity.setLength(entity.getLength());
        uploadMsgEntity.setNcrAccess(entity.getNcrAccess());
        uploadMsgEntity.setPhysicalArchive(entity.getPhysicalArchive());
        uploadMsgEntity.setPhysicalType(entity.getPhysicalType());
        uploadMsgEntity.setProdDescription(entity.getProdDescription());
        uploadMsgEntity.setProdID(entity.getProdID());
        uploadMsgEntity.setProduction(entity.getProduction());
        uploadMsgEntity.setProductionName(entity.getProductionName());
        uploadMsgEntity.setProvenance(entity.getProvenance());
        uploadMsgEntity.setReleaseDate(entity.getReleaseDate());
        uploadMsgEntity.setSgid(entity.getSgid());
        uploadMsgEntity.setStatus(entity.getStatus());
        uploadMsgEntity.setTerritory(entity.getTerritory());
        uploadMsgEntity.setType(entity.getType());
        uploadMsgEntity.setWidth(entity.getWidth());
        uploadMsgEntity.setYearbook(entity.getYearbook());
        uploadMsgEntity.setLocation(entity.getLocation());
        uploadMsgEntity.setAssetDescription(entity.getAssetDescription());
        uploadMsgEntity.setRetiredStatus(entity.getRetiredStatus());
        uploadMsgEntity.setRigName(entity.getRigName());
        uploadMsgEntity.setPriority(entity.getPriority());//-----Int
        uploadMsgEntity.setStage(entity.getStage());
        uploadMsgEntity.setFileNameCompare(entity.getFileNameCompare());

        //private String material;	    //------------------------1
        //private String keywords;	    //------------------------2
        //private String postEpisodeID;	//------------------------3
        //private String prodShowID;	//------------------------4

        Optional<Material> materialOptional = Optional.ofNullable(entity.getMaterial());
        List<MMInboundTabularReport> tabularReports = new ArrayList<>();
        if(materialOptional.isPresent()) {
            log.info("Materials present");

            uploadMsgEntity.setMaterial(Constants.YES);
            if(Constants.YES.equals(entity.getMaterial().getRemoveAll())) {
                MMInboundTabularReport report = new MMInboundTabularReport();
                report.setQueueId(id);
                report.setMessageType(entity.getMessageType());
                report.setFieldType(Constants.MATERIAL);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);
            } else {
                if(!CollectionUtils.isEmpty(entity.getMaterial().getRemove())) {
                    for (String rmValue : entity.getMaterial().getRemove()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.MATERIAL);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getMaterial().getAdd())) {
                    for (String addVal : entity.getMaterial().getAdd()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.MATERIAL);
                        report.setAddValue(addVal);
                        tabularReports.add(report);                    }
                }
            }
        }

        Optional<PostEpisode> postEpsOptional = Optional.ofNullable(entity.getPostEpisodeID());
        if(postEpsOptional.isPresent()) {
            log.info("POst Episode present");

            uploadMsgEntity.setPostEpisodeID(Constants.YES);
            if(Constants.YES.equals(entity.getPostEpisodeID().getRemoveAll())) {
                MMInboundTabularReport report = new MMInboundTabularReport();
                report.setQueueId(id);
                report.setMessageType(entity.getMessageType());
                report.setFieldType(Constants.POST_EPISODE);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);

            } else {

                if(!CollectionUtils.isEmpty(entity.getPostEpisodeID().getRemove())) {

                    for (String rmValue : entity.getPostEpisodeID().getRemove()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.POST_EPISODE);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getPostEpisodeID().getAdd())) {
                    for (String addVal : entity.getPostEpisodeID().getAdd()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.POST_EPISODE);
                        report.setAddValue(addVal);
                        tabularReports.add(report);
                    }
                }
            }
        }

        Optional<ProdShow> prodShowOptional = Optional.ofNullable(entity.getProdShowID());
        if(prodShowOptional.isPresent()) {
            log.info("ProdShow present");

            uploadMsgEntity.setProdShowID(Constants.YES);
            if(Constants.YES.equals(entity.getProdShowID().getRemoveAll())) {
                MMInboundTabularReport report = new MMInboundTabularReport();
                report.setQueueId(id);
                report.setMessageType(entity.getMessageType());
                report.setFieldType(Constants.PROD_SHOW);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);
            } else {
                if(!CollectionUtils.isEmpty(entity.getProdShowID().getRemove())) {

                    for (String rmValue : entity.getProdShowID().getRemove()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.PROD_SHOW);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getProdShowID().getAdd())) {
                    for (String addVal : entity.getProdShowID().getAdd()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.PROD_SHOW);
                        report.setAddValue(addVal);
                        tabularReports.add(report);
                    }
                }
            }
        }

        Optional<Keyword> keywordwOptional = Optional.ofNullable(entity.getKeywords());
        if(keywordwOptional.isPresent()) {
            log.info("Keywords present");

            uploadMsgEntity.setKeywords(Constants.YES);
            if(Constants.YES.equals(entity.getKeywords().getRemoveAll())) {
                MMInboundTabularReport report = new MMInboundTabularReport();
                report.setQueueId(id);
                report.setMessageType(entity.getMessageType());
                report.setFieldType(Constants.KEYWORDS);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);
            } else {
                if(!CollectionUtils.isEmpty(entity.getKeywords().getRemove())) {

                    for (String rmValue : entity.getKeywords().getRemove()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.KEYWORDS);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getKeywords().getAdd())) {
                    for (String addVal : entity.getKeywords().getAdd()) {
                        MMInboundTabularReport report = new MMInboundTabularReport();
                        report.setQueueId(id);
                        report.setMessageType(entity.getMessageType());
                        report.setFieldType(Constants.KEYWORDS);
                        report.setAddValue(addVal);
                        tabularReports.add(report);
                    }
                }
            }
        }

        UploadEntityDto dto = new UploadEntityDto();
        dto.setEntity(uploadMsgEntity);
        dto.setTabulerReports(tabularReports);

        return dto;

    }

    public String convertWindowToLinuxPath(String path) {
        log.info("In convertWindowToLinuxPath real path - "+path);

        path = path.replaceAll("\\\\", "/");

        String windowLocation1 = "//nickwest/nick/anim";
        String windowLocation2 = "//nasBurbank/NAS_Production";
        String windowLocation3 = "//ql-us-laqum02-smb/nick";
        String linuxLocation1 = "/wip/NICK";
        String linuxLocation2 = "/wip/NAS";
        String linuxLocation3 = "/wip/NICK-WRAPPED";

        if (path.toLowerCase().contains(windowLocation1.toLowerCase())) {
            path = path.replaceAll("(?i)" +windowLocation1,linuxLocation1);
        } else if (path.toLowerCase().contains(windowLocation2.toLowerCase())) {
            path = path.replaceAll("(?i)" +windowLocation2,linuxLocation2);
        }else if (path.toLowerCase().contains(windowLocation3.toLowerCase())) {
            path = path.replaceAll("(?i)" +windowLocation3,linuxLocation3);
        }
        log.info("In convertWindowToLinuxPath Converted path = "+path);
        return path;

    }

    public String compareMetadata(NickProdUploadMsgEntity entity, AssetMetadata assetMetadata) {
        StringBuilder message = new StringBuilder("");

        if (StringUtils.isNotBlank(entity.getFmQueueId().toString())){
            if (!entity.getFmQueueId().toString().equals(assetMetadata.getFmQueueId())){
                message.append("fmQueueId ");
            }
        }

        if (isValueNotNull(entity.getCategory())){
            if (!entity.getCategory().equals(assetMetadata.getCategory())){
                message.append("category ");
            }
        }

        //private String condition;//3/23/2018 4:30:45 PM
        //private String conditionNotes;//3/20/2018 1:15:43 PM
        //private String material;	//------------------------1 never used
        //private String appraisedAt; never used
        //private String appraisedBy; never used
        //private String appraisedValue; never used

        if (isValueNotNull(entity.getArtists())){
            if (!entity.getArtists().equals(assetMetadata.getArtists())){
                message.append("artists ");
            }
        }
        if (isValueNotNull(entity.getAssetName())){
            if (!entity.getAssetName().equals(assetMetadata.getAssetName())){
                message.append("assetName ");
            }
        }

        if (isValueNotNull(entity.getBarcode())){
            if (!entity.getBarcode().equals(assetMetadata.getBarcode())){
                message.append("barcode ");
            }
        }

        if (isValueNotNull(entity.getBoxID())){
            if(Long.parseLong(entity.getBoxID()) != assetMetadata.getBoxId()){
                message.append("boxID ");
            }
        }

        //private String contextualNote; last used 6/4/2019 2:25:57 PM
        //private String creationDate; last used 9/17/2021 1:29:10 PM
        //private String dateImported; never used

        if (isValueNotNull(entity.getEicAccess())){
            String access = entity.getEicAccess().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getEicAccess())){
                message.append("eicAccess ");
            }
        }

        if (isValueNotNull(entity.getEicComments())){
            if (!entity.getEicComments().equals(assetMetadata.getEicComments())){
                message.append("eicComments ");
            }
        }

        //private String eicReview; last used 5/5/2022 4:29:23 PM
        //private String eicStatus; last used 10/23/2017 2:27:31 PM

        if (isValueNotNull(entity.getFmid())){
            if(Long.parseLong(entity.getFmid()) != assetMetadata.getFmId()){
                message.append("fmid ");
            }
        }
        if (isValueNotNull(entity.getFilename())){
            if (!entity.getFilename().equals(assetMetadata.getFilename())){
                message.append("filename ");
            }
        }
        if (isValueNotNull(entity.getFirstSeason())){
            if (!entity.getFirstSeason().equals(assetMetadata.getSeason())){
                message.append("firstSeason ");
            }
        }
        if (isValueNotNull(entity.getFirstShow())){
            if(Long.parseLong(entity.getFirstShow()) != assetMetadata.getFirstShow()){
                message.append("firstShow ");
            }
        }

        //private String hasDuplicates; not used in requests
        //private String height; last used 9/17/2021 1:29:10 PM
        //private String importedBy; never used
        //private String length; last used 6/10/2019 9:40:53 AM

        if (isValueNotNull(entity.getNcrAccess())){
            String access = entity.getNcrAccess().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getNcrAccess())){
                message.append("ncrAccess ");
            }
        }

        //private String physicalArchive; never used
        //private String physicalType; never used
        //private String prodDescription; never used
        if (isValueNotNull(entity.getProdID())){
            if (!entity.getProdID().equals(assetMetadata.getProdId())){
                message.append("prodID ");
            }
        }
        if (isValueNotNull(entity.getProductionName())){
            if (!entity.getProductionName().equals(assetMetadata.getProductionName())){
                message.append("productionName ");
            }
        }

        //private String production; never used
        if (isValueNotNull(entity.getProvenance())){
            if (!entity.getProvenance().equals(assetMetadata.getProvenance())){
                message.append("provenance ");
            }
        }
        //private String releaseDate; never used in rabbit
        if (isValueNotNull(entity.getSgid())){
            if(Long.parseLong(entity.getSgid()) != assetMetadata.getSgid()){
                message.append("sgid ");
            }
        }

        //private String status; never used in rabbit
        //private String territory; never used in rabbit
        if (isValueNotNull(entity.getType())){
            if (!entity.getType().equals(assetMetadata.getType())){
                message.append("type ");
            }
        }
        if (isValueNotNull(entity.getWidth())){
            if(Integer.parseInt(entity.getWidth()) != assetMetadata.getWidth()){
                message.append("width ");
            }
        }
        if (isValueNotNull(entity.getYearbook())){
            if (!entity.getYearbook().equals(assetMetadata.getYearbook())){
                message.append("yearbook ");
            }
        }
//todo: change in column for PROD db
        if (isValueNotNull(entity.getLocation())){
            if (!entity.getLocation().equals(assetMetadata.getImageLocation())){
                message.append("location ");
            }
        }
//todo: change in column in PROD db
        if (isValueNotNull(entity.getAssetDescription())){
            if (!entity.getAssetDescription().equals(assetMetadata.getAssetDescription())){
                message.append("assetDescription ");
            }
        }
        if (isValueNotNull(entity.getRetiredStatus())){
            String access = entity.getRetiredStatus().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getIsRetired())){
                message.append("retiredStatus ");
            }
        }

        if (isValueNotNull(entity.getRigName())){
            if (!entity.getRigName().equals(assetMetadata.getRigName())){
                message.append("rigName ");
            }
        }
        if (isValueNotNull(entity.getStage())){
            if (!entity.getStage().equals(assetMetadata.getStage())){
                message.append("stage ");
            }
        }
        if (isValueNotNull(entity.getFileNameCompare())){
            if (!entity.getFileNameCompare().equals(assetMetadata.getFileNameCompare())){
                message.append("fileNameCompare ");
            }
        }

        return message.toString();
    }

    private boolean isValueNotNull(String value) {
        return StringUtils.isNotBlank(value) && !value.equals(Constants.REMOVE_VALUE);
    }

    public String compareProdMetadata(NickProdUploadMsgEntity entity, NickProdAssetMetadata assetMetadata, NickProdAssetMetadata2 nickProdAssetMetadata2) {
    //    log.info("In compareProdMetadata");

        StringBuilder message = new StringBuilder();
/*
        if (StringUtils.isNotBlank(entity.getFmQueueId().toString())){
    //        log.info("fmQueueId present");
            if (!entity.getFmQueueId().toString().equals(assetMetadata.getFmQueueId())){
                message.append("fmQueueId ");
            }
        }

        if (isValueNotNull(entity.getCategory())){
      //      log.info("category present");

            if (!entity.getCategory().equals(assetMetadata.getCategory())){
                message.append("category ");
            }
        }

        //private String condition;//3/23/2018 4:30:45 PM
        //private String conditionNotes;//3/20/2018 1:15:43 PM
        //private String material;	//------------------------1 never used
        //private String appraisedAt; never used
        //private String appraisedBy; never used
        //private String appraisedValue; never used

        if (isValueNotNull(entity.getArtists())){
        //    log.info("artists present");

            if (!entity.getArtists().equals(assetMetadata.getArtists())){
                message.append("artists ");
            }
        }
        if (isValueNotNull(entity.getAssetName())){
          //  log.info("assetName present");

            if (!entity.getAssetName().equals(assetMetadata.getAssetName())){
                message.append("assetName ");
            }
        }

        if (isValueNotNull(entity.getBarcode())){
        //    log.info("barcode present");

            if (!entity.getBarcode().equals(assetMetadata.getBarcode())){
                message.append("barcode ");
            }
        }

        if (isValueNotNull(entity.getBoxID())){
          //  log.info("boxID present");
            if(!Objects.equals(Long.parseLong(entity.getBoxID()),assetMetadata.getBoxId()))
            {
                message.append("boxID ");
            }

        }

        //private String contextualNote; last used 6/4/2019 2:25:57 PM
        //private String creationDate; last used 9/17/2021 1:29:10 PM
        //private String dateImported; never used

        if (isValueNotNull(entity.getEicAccess())){
            //log.info("eicAccess present");

            String access = entity.getEicAccess().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getEicAccess())){
                message.append("eicAccess ");
            }
        }

        if (isValueNotNull(entity.getEicComments())){
            //log.info("eicComments present");

            if (!entity.getEicComments().equals(assetMetadata.getEicComments())){
                message.append("eicComments ");
            }
        }

        //private String eicReview; last used 5/5/2022 4:29:23 PM
        //private String eicStatus; last used 10/23/2017 2:27:31 PM

        if (isValueNotNull(entity.getFmid())){
            //log.info("fmid present");
            if(!Objects.equals(Long.parseLong(entity.getFmid()),assetMetadata.getFmId()))
            {
                message.append("fmid ");
            }
        }
        if (isValueNotNull(entity.getFilename())){
            //log.info("filename present");

            if (!entity.getFilename().equals(assetMetadata.getFilename())){
                message.append("filename ");
            }
        }
        if (isValueNotNull(entity.getFirstSeason())){
            //log.info("firstSeason present");

            if (!entity.getFirstSeason().equals(assetMetadata.getSeason())){
                message.append("firstSeason ");
            }
        }
        if (isValueNotNull(entity.getFirstShow())){
            //log.info("firstShow present");

            if(!Objects.equals(Long.parseLong(entity.getFirstShow()),assetMetadata.getFirstShow()))
            {
                message.append("firstShow ");
            }
        }

        //private String hasDuplicates; not used in requests
        //private String height; last used 9/17/2021 1:29:10 PM
        //private String importedBy; never used
        //private String length; last used 6/10/2019 9:40:53 AM

        if (isValueNotNull(entity.getNcrAccess())){
            //log.info("ncrAccess present");

            String access = entity.getNcrAccess().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getNcrAccess())){
                message.append("ncrAccess ");
            }
        }

        //private String physicalArchive; never used
        //private String physicalType; never used
        //private String prodDescription; never used
        if (isValueNotNull(entity.getProdID())){
            //log.info("prodID present");

            if (!entity.getProdID().equals(assetMetadata.getProdId())){
                message.append("prodID ");
            }
        }
        if (isValueNotNull(entity.getProductionName())){
            //log.info("productionName present");

            if (!entity.getProductionName().equals(assetMetadata.getProductionName())){
                message.append("productionName ");
            }
        }

        //private String production; never used
        if (isValueNotNull(entity.getProvenance())){
            //log.info("provenance present");

            if (!entity.getProvenance().equals(assetMetadata.getProvenance())){
                message.append("provenance ");
            }
        }
        //private String releaseDate; never used in rabbit
        if (isValueNotNull(entity.getSgid())){
            //log.info("sgid present");
            if(!Objects.equals(Long.parseLong(entity.getSgid()),assetMetadata.getSgid()))
            {
                message.append("sgid ");
            }

        }

        //private String status; never used in rabbit
        //private String territory; never used in rabbit
        if (isValueNotNull(entity.getType())){
            //log.info("type present");

            if (!entity.getType().equals(assetMetadata.getType())){
                message.append("type ");
            }
        }
        if (isValueNotNull(entity.getWidth())){
            //log.info("width present");
            if(!Objects.equals(Integer.parseInt(entity.getWidth()),assetMetadata.getWidth()))
            {
                message.append("width ");
            }
        }
        if (isValueNotNull(entity.getYearbook())){
            //log.info("yearbook present");

            if (!entity.getYearbook().equals(assetMetadata.getYearbook())){
                message.append("yearbook ");
            }
        }
//todo: change in column for PROD db
        if (isValueNotNull(entity.getLocation())){
            //log.info("location present");

            if (!entity.getLocation().equals(nickProdAssetMetadata2.getImageLocation())){
                message.append("location ");
            }
        }
//todo: change in column in PROD db
        if (isValueNotNull(entity.getAssetDescription())){
            //log.info("assetDescription present");

            if (!entity.getAssetDescription().equals(nickProdAssetMetadata2.getAssetDescription())){
                message.append("assetDescription ");
            }
        }
        if (isValueNotNull(entity.getRetiredStatus())){
            //log.info("retiredStatus present");

            String access = entity.getRetiredStatus().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getIsRetired())){
                message.append("retiredStatus ");
            }
        }

        if (isValueNotNull(entity.getRigName())){
            //log.info("rigName present");

            if (!entity.getRigName().equals(assetMetadata.getRigName())){
                message.append("rigName ");
            }
        }
        if (isValueNotNull(entity.getStage())){
            //log.info("stage present");

            if (!entity.getStage().equals(assetMetadata.getStage())){
                message.append("stage ");
            }
        }
        if (isValueNotNull(entity.getFileNameCompare())){
            //log.info("fileNameCompare present");

            if (!entity.getFileNameCompare().equals(assetMetadata.getFileNameCompare())){
                message.append("fileNameCompare ");
            }
        }
*/

        if (StringUtils.isNotBlank(entity.getFmQueueId().toString())){
            if (!entity.getFmQueueId().toString().equals(assetMetadata.getFmQueueId())){
                message.append("fmQueueId ");
            }
        }

        if (isValueNotNull(entity.getCategory())){
            if (!entity.getCategory().equals(assetMetadata.getCategory())){
                message.append("category ");
            }
        }

        //private String condition;//3/23/2018 4:30:45 PM
        //private String conditionNotes;//3/20/2018 1:15:43 PM
        //private String material;	//------------------------1 never used
        //private String appraisedAt; never used
        //private String appraisedBy; never used
        //private String appraisedValue; never used

        if (isValueNotNull(entity.getArtists())){
            if (!entity.getArtists().equals(assetMetadata.getArtists())){
                message.append("artists ");
            }
        }
        if (isValueNotNull(entity.getAssetName())){
            if (!entity.getAssetName().equals(assetMetadata.getAssetName())){
                message.append("assetName ");
            }
        }

        if (isValueNotNull(entity.getBarcode())){
            if (!entity.getBarcode().equals(assetMetadata.getBarcode())){
                message.append("barcode ");
            }
        }

        if (isValueNotNull(entity.getBoxID())){
            if (assetMetadata.getBoxId() != null) {
                if (Long.parseLong(entity.getBoxID()) != assetMetadata.getBoxId()) {
                    message.append("boxID ");
                }
            } else {
                message.append("boxID ");
            }
        }

        //private String contextualNote; last used 6/4/2019 2:25:57 PM
        //private String creationDate; last used 9/17/2021 1:29:10 PM
        //private String dateImported; never used

        if (isValueNotNull(entity.getEicAccess())){
            String access = entity.getEicAccess().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getEicAccess())){
                message.append("eicAccess ");
            }
        }

        if (isValueNotNull(entity.getEicComments())){
            if (!entity.getEicComments().equals(assetMetadata.getEicComments())){
                message.append("eicComments ");
            }
        }

        //private String eicReview; last used 5/5/2022 4:29:23 PM
        //private String eicStatus; last used 10/23/2017 2:27:31 PM

        if (isValueNotNull(entity.getFmid())){
            if (assetMetadata.getFmId() != null) {
                if(Long.parseLong(entity.getFmid()) != assetMetadata.getFmId()){
                    message.append("fmid ");
                }
            } else {
                message.append("fmid ");
            }

        }
        if (isValueNotNull(entity.getFilename())){
            if (!entity.getFilename().equals(assetMetadata.getFilename())){
                message.append("filename ");
            }
        }
        if (isValueNotNull(entity.getFirstSeason())){
            if (!entity.getFirstSeason().equals(assetMetadata.getSeason())){
                message.append("firstSeason ");
            }
        }
        if (isValueNotNull(entity.getFirstShow())){
            if (assetMetadata.getFirstShow() != null) {
                if(Long.parseLong(entity.getFirstShow()) != assetMetadata.getFirstShow()){
                    message.append("firstShow ");
                }
            } else {
                message.append("firstShow ");
            }

        }

        //private String hasDuplicates; not used in requests
        //private String height; last used 9/17/2021 1:29:10 PM
        //private String importedBy; never used
        //private String length; last used 6/10/2019 9:40:53 AM

        if (isValueNotNull(entity.getNcrAccess())){
            String access = entity.getNcrAccess().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getNcrAccess())){
                message.append("ncrAccess ");
            }
        }

        //private String physicalArchive; never used
        //private String physicalType; never used
        //private String prodDescription; never used
        if (isValueNotNull(entity.getProdID())){
            if (!entity.getProdID().equals(assetMetadata.getProdId())){
                message.append("prodID ");
            }
        }
        if (isValueNotNull(entity.getProductionName())){
            if (!entity.getProductionName().equals(assetMetadata.getProductionName())){
                message.append("productionName ");
            }
        }

        //private String production; never used
        if (isValueNotNull(entity.getProvenance())){
            if (!entity.getProvenance().equals(assetMetadata.getProvenance())){
                message.append("provenance ");
            }
        }
        //private String releaseDate; never used in rabbit
        if (isValueNotNull(entity.getSgid())){
            if (assetMetadata.getSgid() != null) {
                if(Long.parseLong(entity.getSgid()) != assetMetadata.getSgid()){
                    message.append("sgid ");
                }
            } else {
                message.append("sgid ");
            }

        }

        //private String status; never used in rabbit
        //private String territory; never used in rabbit
        if (isValueNotNull(entity.getType())){
            if (!entity.getType().equals(assetMetadata.getType())){
                message.append("type ");
            }
        }
        if (isValueNotNull(entity.getWidth())){
            if (assetMetadata.getWidth() != null) {
                if(Integer.parseInt(entity.getWidth()) != assetMetadata.getWidth()){
                    message.append("width ");
                }
            } else {
                message.append("width ");
            }

        }
        if (isValueNotNull(entity.getYearbook())){
            if (!entity.getYearbook().equals(assetMetadata.getYearbook())){
                message.append("yearbook ");
            }
        }
//todo: change in column for PROD db
        if (isValueNotNull(entity.getLocation())){
            if (!entity.getLocation().equals(nickProdAssetMetadata2.getImageLocation())){
                message.append("location ");
            }
        }
//todo: change in column in PROD db
        if (isValueNotNull(entity.getAssetDescription())){
            if (!entity.getAssetDescription().equals(nickProdAssetMetadata2.getAssetDescription())){
                message.append("assetDescription ");
            }
        }
        if (isValueNotNull(entity.getRetiredStatus())){
            String access = entity.getRetiredStatus().equalsIgnoreCase("Yes") ? Constants.YES : Constants.NO;
            if (!access.equals(assetMetadata.getIsRetired())){
                message.append("retiredStatus ");
            }
        }

        if (isValueNotNull(entity.getRigName())){
            if (!entity.getRigName().equals(assetMetadata.getRigName())){
                message.append("rigName ");
            }
        }
        if (isValueNotNull(entity.getStage())){
            if (!entity.getStage().equals(assetMetadata.getStage())){
                message.append("stage ");
            }
        }
        if (isValueNotNull(entity.getFileNameCompare())){
            if (!entity.getFileNameCompare().equals(assetMetadata.getFileNameCompare())){
                message.append("fileNameCompare ");
            }
        }

        return message.toString();
    }

    public boolean checkDupilateMetadata(NickProdAssetMetadata assetMetadata, NickProdAssetMetadata assetMetadata1) {
log.info("In checkDupilateMetadata");

        if (StringUtils.isBlank(assetMetadata.getProdId())
                || StringUtils.isBlank(assetMetadata1.getProdId())) {
            return false;
        } else {
            log.info("Prod Id present");
            if (assetMetadata.getProdId().equals(assetMetadata1.getProdId())){
                log.info("Prod id equal");
                if (StringUtils.isBlank(assetMetadata.getFileNameCompare())
                        || StringUtils.isBlank(assetMetadata1.getFileNameCompare())){
                    return true;
                } else {
                    return assetMetadata.getFileNameCompare().equals(assetMetadata1.getFileNameCompare());
                }
            }else {
                return false;
            }
        }
        /*
        if (StringUtils.isBlank(assetMetadata.getProdId())
                || StringUtils.isBlank(assetMetadata1.getProdId())
                || StringUtils.isBlank(assetMetadata.getFileNameCompare())
                || StringUtils.isBlank(assetMetadata1.getFileNameCompare())){
            return false;

        } else {
            return assetMetadata.getProdId().equals(assetMetadata1.getProdId())
                    && assetMetadata.getFileNameCompare().equals(assetMetadata1.getFileNameCompare());
        }*/

    }
}
