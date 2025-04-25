package com.ppc.mm.nickprodmetacomparing.util;

import com.artesia.asset.Asset;
import com.artesia.asset.AssetIdentifier;
import com.artesia.asset.metadata.services.AssetMetadataServices;
import com.artesia.asset.services.AssetServices;
import com.artesia.common.exception.BaseTeamsException;
import com.artesia.entity.TeamsIdentifier;
import com.artesia.metadata.MetadataField;
import com.artesia.metadata.MetadataTableField;
import com.artesia.security.SecuritySession;
import com.artesia.security.session.services.AuthenticationServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppc.mm.common.util.Constants;
import com.ppc.mm.nickprodmessaging.util.*;
import com.ppc.mm.nickprodmetacomparing.entity.*;
import com.ppc.mm.nickprodmetacomparing.service.MetadataService;
import com.ppc.mm.nickprodreporting.service.ReportingService;
import com.ppc.mm.nickprodreporting.service.ReportingServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.ppc.mm.common.util.Constants.REMOVE_VALUE;

@Component
public class MetadataProcessor {

    private static final Logger log = LoggerFactory.getLogger(MetadataProcessor.class);

    MetadataService metadataService;
    ReportingService reportingService;

    private ObjectMapper mapper;
    @Autowired
    private Environment environment;

    @Autowired
    public void setMetadataService(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @Autowired
    public void setReportingService(ReportingServiceImpl service) {
        reportingService = service;
    }


    public List<NickMetadataCompare> getFilesForMetadataCompare() {
        return metadataService.getFilesForMetadataCompare();
    }

    public void processFileForMetaCompare(List<NickMetadataCompare> nickObjects) {

        for (NickMetadataCompare nickObject : nickObjects) {
            processObjectForMetaCompare(nickObject);
        }
    }

    private void processObjectForMetaCompare(NickMetadataCompare nickObject) {

        String uoiId = metadataService.getUoiId(nickObject.getFilename(), nickObject.getProductionId());
    }

    public List<Object[]> getUoiForMerge() {
        return metadataService.getUoiForMerge();
    }

    public void mergeUoi(List<Object[]> nickObjects) {
        for (Object[] obj : nickObjects) {

            log.info("merge uoi {} ",obj[0].toString());

            //for (Object o : obj) {
            //    log.info("in obj uoi {} ",o.toString());
            //}
            String uoiId = obj[0].toString();
            String logicalUoiId = obj[1].toString();
            String version = obj[2].toString();
            String isLatestVersion = obj[3].toString();

            //update uois
            metadataService.updateUois(uoiId,logicalUoiId,version,isLatestVersion);
            // insert indexer
            metadataService.addToIndexer(uoiId);
            // update uois nick
            metadataService.updateUoisNick(uoiId);
        }

    }

    public List<NickProdMetadataUpdateMarch25> getMetaDumpData() {
        return metadataService.getMetaDumpData();
    }

    public void updateMetadata(List<NickProdMetadataUpdateMarch25> nickObjects) throws BaseTeamsException {
        log.info("in update metadata ");

        SecuritySession securitySession = null;

        try {
            String userName = environment.getRequiredProperty("nickprodAdminUserName");
            String password = environment.getRequiredProperty("nickprodAdminPassword");

//            log.info("userName : {}",userName);
//            log.info("password : {}",password);
            securitySession = AuthenticationServices.getInstance().login(userName, password);

            for (NickProdMetadataUpdateMarch25 nickObject : nickObjects) {
                log.info("updating metadata {} ",nickObject.getOtid());
                mapToJson(nickObject, securitySession);
            }

        } catch (BaseTeamsException e) {
            log.error("Error in creating session {}",e.getDebugMessage());
        } finally {
            if (securitySession != null) {
                AuthenticationServices.getInstance().logout(securitySession);
            }
        }


    }

    private void mapToJson(NickProdMetadataUpdateMarch25 nickObject, SecuritySession securitySession) {
        //log.info("mapToJson {} ",nickObject.getOtid());
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
            NickProdUploadMsgInboundEntity entity = mapper.readValue(nickObject.getMetadata(), NickProdUploadMsgInboundEntity.class);
            //log.info("NickProdUploadMsgInboundEntity {} ",entity.toString());
            //NickProdAssetMetadata latest = reportingService.getNickProdAssetMetadata(nickObject.getOtid());
            //NickProdAssetMetadata2 latestAdditional = reportingService.getNickProdAdditionalMetadata(nickObject.getOtid());
            nickObject.setProcessed("Y");
            nickObject.setProcessedDate(new Date());
            entity.setOtid(nickObject.getOtid());
            String message = updateMetadata(entity,securitySession);

            if (StringUtils.isNotBlank(message)){
                nickObject.setProcessed("N");
                nickObject.setErrorMsg(message);
            }

        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);

            nickObject.setErrorMsg(e.getMessage());
        }
        metadataService.saveObject(nickObject);

    }

    private String updateMetadata(NickProdUploadMsgInboundEntity messageEntity, SecuritySession securitySession) {

        log.info("in updateMetadata {} ",messageEntity.getOtid());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String conditionCol = "";
        String searchCol = "";
        String message = "";

        List<MetadataField> metadataFields = new ArrayList<>();
        AssetIdentifier[] assetIdentifiers = new AssetIdentifier[1];

        try{

            Asset asset = new Asset(new AssetIdentifier(messageEntity.getOtid()));
            log.info("Asset created {}",asset.getAssetId().asString());
            assetIdentifiers[0] = asset.getAssetId();
            if (asset.isLocked()){
                message = "Asset is locked";
                log.info(asset.getContentLockStateUserName());

            } else {

                log.info("Before locking asset ");
                AssetServices.getInstance().lockAsset(asset.getAssetId(), securitySession);
                log.info("Asset Locked {}", asset.getAssetId());
                AssetMetadataServices metadataServices = AssetMetadataServices.getInstance();

                MetadataField fmQueueField = new MetadataField(new TeamsIdentifier("CUSTOM.NICKPROD.FMQUEUEID"));

                if (StringUtils.isNotBlank(messageEntity.getFmQueueId())) {
                    log.info("FMQUEUEID {}", messageEntity.getFmQueueId());
                    fmQueueField.setValue(messageEntity.getFmQueueId());
                    metadataFields.add(fmQueueField);
                } else {
                    if (fmQueueField.getValue() != null) {
                        fmQueueField.setValue(null);
                        metadataFields.add(fmQueueField);
                    }
                }

                MetadataField categoryField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD CATEGORY"));

                if (StringUtils.isNotBlank(messageEntity.getCategory())
                        && !REMOVE_VALUE.equals(messageEntity.getCategory())) {
                    log.info("Category {}", messageEntity.getCategory());
                    categoryField.setValue(null);
                    categoryField.setValue(messageEntity.getCategory());

                    metadataFields.add(categoryField);
                } else {
                    if (categoryField.getValue() != null) {
                        categoryField.setValue(null);
                        metadataFields.add(categoryField);
                    }
                }
//todo: check in each batch if metadata is present in table
  /*          if(StringUtils.isNotBlank(messageEntity.getCondition())) {
                Integer conditionId = nickProdMessagingService.getId(messageEntity.getCondition().toUpperCase(), "NICK_PROD_LKP_CONDITION", "ID", "VALUE");
                TeamsIdentifier condition = new TeamsIdentifier("CUSTOM.NICK PROD CONDITION");
                MetadataField conditionField = new MetadataField(condition);
                if(REMOVE_VALUE.equals(messageEntity.getCondition())) {
                    conditionField.setValue(null);
                } else {
                    conditionField.setValue(new BigDecimal(conditionId));
                }
                metadataFields.add(conditionField);
            }

            if(StringUtils.isNotBlank(messageEntity.getConditionNotes())) {
                TeamsIdentifier conditionNotes = new TeamsIdentifier("CUSTOM.NICK PROD CONDITION NOTES");
                MetadataField conditionNotesField = new MetadataField(conditionNotes);
                if(REMOVE_VALUE.equals(messageEntity.getConditionNotes())) {
                    conditionNotesField.setValue(null);
                } else {
                    conditionNotesField.setValue(messageEntity.getConditionNotes());
                }
                metadataFields.add(conditionNotesField);
            }

            if(StringUtils.isNotBlank(messageEntity.getAppraisedAt())) {
                TeamsIdentifier appraisedAt = new TeamsIdentifier("CUSTOM.NICK PROD APPRAISED AT");
                MetadataField appraisedAtField = new MetadataField(appraisedAt);
                if(REMOVE_VALUE.equals(messageEntity.getAppraisedAt())) {
                    appraisedAtField.setValue(null);
                } else {
                    appraisedAtField.setValue(formatter.parse(messageEntity.getAppraisedAt()));
                }
                metadataFields.add(appraisedAtField);
            }

            if(StringUtils.isNotBlank(messageEntity.getAppraisedBy())) {
                TeamsIdentifier appraisedBy = new TeamsIdentifier("CUSTOM.NICK PROD APPRAISED BY");
                MetadataField appraisedByField = new MetadataField(appraisedBy);
                if(REMOVE_VALUE.equals(messageEntity.getAppraisedBy())) {
                    appraisedByField.setValue(null);
                } else {
                    appraisedByField.setValue(messageEntity.getAppraisedBy());
                }
                metadataFields.add(appraisedByField);
            }

            if(StringUtils.isNotBlank(messageEntity.getAppraisedValue())) {
                TeamsIdentifier appraisedVal = new TeamsIdentifier("CUSTOM.NICK PROD APPRAISED VALUE");
                MetadataField appraisedValField = new MetadataField(appraisedVal);
                if(REMOVE_VALUE.equals(messageEntity.getAppraisedValue())) {
                    appraisedValField.setValue(null);
                } else {
                    appraisedValField.setValue(new BigDecimal(messageEntity.getAppraisedValue()));
                }
                metadataFields.add(appraisedValField);
            }
*/
                MetadataField artistValField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD ARTISTS"));

                if (StringUtils.isNotBlank(messageEntity.getArtists()) && !REMOVE_VALUE.equals(messageEntity.getArtists())) {
                    log.info("Artists {}", messageEntity.getArtists());
                    artistValField.setValue(messageEntity.getArtists());
                    metadataFields.add(artistValField);
                } else {
                    if (artistValField.getValue() != null) {
                        artistValField.setValue(null);
                        metadataFields.add(artistValField);
                    }
                }

                if (StringUtils.isNotBlank(messageEntity.getAssetName())) {
                    log.info("AssetName {}", messageEntity.getAssetName());
                    TeamsIdentifier assetNameVal = new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD ASSET NAME");
                    MetadataField assetNameValField = new MetadataField(assetNameVal);
                    if (REMOVE_VALUE.equals(messageEntity.getAssetName())) {
                        assetNameValField.setValue(null);
                    } else {
                        assetNameValField.setValue(messageEntity.getAssetName());
                    }
                    metadataFields.add(assetNameValField);
                }

                /***
                 * Not updating embedded fields
                 */

//            MetadataField assetDescField = new MetadataField(new TeamsIdentifier("ARTESIA.FIELD.ASSET DESCRIPTION"));//embedded
                MetadataField assetDescField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK_PROD_ASSETDESCRIPTION")); //custom

                if (StringUtils.isNotBlank(messageEntity.getAssetDescription())
                        && !REMOVE_VALUE.equals(messageEntity.getAssetDescription())) {
                    log.info("AssetDescription {}", messageEntity.getAssetDescription());
                    assetDescField.setValue(messageEntity.getAssetDescription());
                    metadataFields.add(assetDescField);
                } else {
                    if (assetDescField.getValue() != null) {
                        assetDescField.setValue(null);
                        metadataFields.add(assetDescField);
                    }
                }
/*
            if(StringUtils.isNotBlank(messageEntity.getBarcode())) {
                TeamsIdentifier barcodeVal = new TeamsIdentifier("CUSTOM.NICK PROD BARCODE");
                MetadataField barcodeValField = new MetadataField(barcodeVal);
                if(REMOVE_VALUE.equals(messageEntity.getBarcode())) {
                    barcodeValField.setValue(null);
                } else {
                    barcodeValField.setValue(messageEntity.getBarcode());
                }
                metadataFields.add(barcodeValField);
            }

            if(StringUtils.isNotBlank(messageEntity.getBoxID())) {
                TeamsIdentifier boxIdVal = new TeamsIdentifier("CUSTOM.NICK PROD BOX ID");
                MetadataField boxIdValField = new MetadataField(boxIdVal);
                if(REMOVE_VALUE.equals(messageEntity.getBoxID())) {
                    boxIdValField.setValue(null);
                } else {
                    boxIdValField.setValue(new BigDecimal(messageEntity.getBoxID()));
                }
                metadataFields.add(boxIdValField);
            }

            if(StringUtils.isNotBlank(messageEntity.getContextualNote())) {
                TeamsIdentifier contextualNotesVal = new TeamsIdentifier("CUSTOM.NICK PROD CONTEXTUAL NOTE");
                MetadataField contextualNotesValField = new MetadataField(contextualNotesVal);
                if(REMOVE_VALUE.equals(messageEntity.getContextualNote())) {
                    contextualNotesValField.setValue(null);
                } else {
                    contextualNotesValField.setValue(messageEntity.getContextualNote());
                }
                metadataFields.add(contextualNotesValField);
            }
*/
                MetadataField creationDateField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD.CREATION DATE"));

                if (StringUtils.isNotBlank(messageEntity.getCreationDate())
                        && !REMOVE_VALUE.equals(messageEntity.getCreationDate())) {
                    log.info("CreationDate {}", messageEntity.getCreationDate());
                    creationDateField.setValue(formatter.parse(messageEntity.getCreationDate()));
                    metadataFields.add(creationDateField);
                } else {
                    if (creationDateField.getValue() != null) {
                        creationDateField.setValue(null);
                        metadataFields.add(creationDateField);
                    }
                }

                MetadataField eicAccessField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD EIC ACCESS"));

                if (StringUtils.isNotBlank(messageEntity.getEicAccess())
                        && !REMOVE_VALUE.equals(messageEntity.getEicAccess())) {
                    log.info("EicAccess {}", messageEntity.getEicAccess());
                    String eicAccess = null;
                    if (messageEntity.getEicAccess().equalsIgnoreCase("Yes")){
                        eicAccess = "Y";
                    } else {
                        eicAccess = "N";
                    }
                    eicAccessField.setValue(eicAccess);
                    metadataFields.add(eicAccessField);
                } else {
                    if (eicAccessField.getValue() != null) {
                        eicAccessField.setValue(null);
                        metadataFields.add(eicAccessField);
                    }
                }

                MetadataField eicCommentsField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD EIC COMMENTS"));

                if (StringUtils.isNotBlank(messageEntity.getEicComments())
                        && !REMOVE_VALUE.equals(messageEntity.getEicComments())) {
                    log.info("EicComments {}", messageEntity.getEicComments());
                    eicCommentsField.setValue(messageEntity.getEicComments());
                    metadataFields.add(eicCommentsField);
                } else {
                    if (eicCommentsField.getValue() != null) {
                        eicCommentsField.setValue(null);
                        metadataFields.add(eicCommentsField);
                    }
                }
/*
            if(StringUtils.isNotBlank(messageEntity.getEicReview())) {
                TeamsIdentifier eicReviewVal = new TeamsIdentifier("CUSTOM.NICK PROD EIC REVIEW");
                MetadataField eicReviewField = new MetadataField(eicReviewVal);
                if(REMOVE_VALUE.equals(messageEntity.getEicReview())) {
                    eicReviewField.setValue(null);
                } else {
                    eicReviewField.setValue(formatter.parse(messageEntity.getEicReview()));
                }
                metadataFields.add(eicReviewField);
            }

            if(StringUtils.isNotBlank(messageEntity.getEicStatus())) {
                TeamsIdentifier eicStatusVal = new TeamsIdentifier("CUSTOM.NICK PROD EIC STATUS");
                MetadataField eicStatusField = new MetadataField(eicStatusVal);
                if(REMOVE_VALUE.equals(messageEntity.getEicStatus())) {
                    eicStatusField.setValue(null);
                } else {
                    Integer eicStatus = nickProdMessagingService.getId(messageEntity.getEicStatus().toUpperCase(), "NICK_PROD_LKP_EIC_STATUS", "id", "value");
                    eicStatusField.setValue(new BigDecimal(eicStatus));
                }
                metadataFields.add(eicStatusField);
            }
*/
                MetadataField fmidField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD FMID"));

                if (StringUtils.isNotBlank(messageEntity.getFmid()) && !REMOVE_VALUE.equals(messageEntity.getFmid())) {

                    if (NumberUtils.isCreatable(messageEntity.getFmid())) {
                        log.info("FMID {}", messageEntity.getFmid());
                        fmidField.setValue(new BigDecimal(messageEntity.getFmid()));
                        metadataFields.add(fmidField);
                    } else {
                        return "FM Id is not number";
                    }
                } else {
                    if (fmidField.getValue() != null) {
                        fmidField.setValue(null);
                        metadataFields.add(fmidField);
                    }
                }

                MetadataField firstSeasonField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD SEASON"));

                if (StringUtils.isNotBlank(messageEntity.getFirstSeason())
                        && !REMOVE_VALUE.equals(messageEntity.getFirstSeason())) {
                log.info("FirstSeason {}", messageEntity.getFirstSeason());
                    firstSeasonField.setValue(messageEntity.getFirstSeason());
                    metadataFields.add(firstSeasonField);
                } else {
                    if (firstSeasonField.getValue() != null) {
                        firstSeasonField.setValue(null);
                        metadataFields.add(firstSeasonField);
                    }
                }
                MetadataField firstShowField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD FIRST SHOW"));

                if (StringUtils.isNotBlank(messageEntity.getFirstShow())
                        && !REMOVE_VALUE.equals(messageEntity.getFirstShow())) {
                    if (NumberUtils.isCreatable(messageEntity.getFirstShow())) {
                        log.info("FirstShow {}", messageEntity.getFirstShow());
                        firstShowField.setValue(new BigDecimal(messageEntity.getFirstShow()));
                        metadataFields.add(firstShowField);
                    } else {
                        return "First Show is not number";
                    }
                } else {
                    if (firstShowField.getValue() != null) {
                        firstShowField.setValue(null);
                        metadataFields.add(firstShowField);
                    }
                }
/*
            if(StringUtils.isNotBlank(messageEntity.getHasDuplicates())) {
                TeamsIdentifier hasDuplicateVal = new TeamsIdentifier("CUSTOM.NICK PROD HAS DUPLICATES");
                MetadataField hasDuplicateField = new MetadataField(hasDuplicateVal);
                if(REMOVE_VALUE.equals(messageEntity.getHasDuplicates())) {
                    hasDuplicateField.setValue(null);
                } else {
                    hasDuplicateField.setValue(messageEntity.getHasDuplicates());
                }
                metadataFields.add(hasDuplicateField);
            }

            if(StringUtils.isNotBlank(messageEntity.getHeight())) {
                TeamsIdentifier heightVal = new TeamsIdentifier("CUSTOM.NICK PROD HEIGHT");
                MetadataField heightField = new MetadataField(heightVal);
                if(REMOVE_VALUE.equals(messageEntity.getHeight())) {
                    heightField.setValue(null);
                } else {
                    heightField.setValue(new BigDecimal(messageEntity.getHeight()));
                }
                metadataFields.add(heightField);
            }

            if(StringUtils.isNotBlank(messageEntity.getLength())) {
                TeamsIdentifier lengthVal = new TeamsIdentifier("CUSTOM.NICK PROD LENGTH");
                MetadataField lengthField = new MetadataField(lengthVal);
                if(REMOVE_VALUE.equals(messageEntity.getLength())) {
                    lengthField.setValue(null);
                } else {
                    lengthField.setValue(new BigDecimal(messageEntity.getLength()));
                }
                metadataFields.add(lengthField);
            }
*/
                MetadataField ncrAccessField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD NCR ACCESS"));

                if (StringUtils.isNotBlank(messageEntity.getNcrAccess())
                        && !REMOVE_VALUE.equals(messageEntity.getNcrAccess())) {
                    log.info("NcrAccess {}", messageEntity.getNcrAccess());
                    ncrAccessField.setValue(messageEntity.getNcrAccess());
                    metadataFields.add(ncrAccessField);
                } else {
                    if (ncrAccessField.getValue() != null) {
                        ncrAccessField.setValue(null);
                        metadataFields.add(ncrAccessField);
                    }
                }
/*
            if(StringUtils.isNotBlank(messageEntity.getPhysicalArchive())) {
                TeamsIdentifier physicalAccessVal = new TeamsIdentifier("CUSTOM.NICK PROD PHYSICAL ARCHIVE");
                MetadataField physicalAccessField = new MetadataField(physicalAccessVal);
                if(REMOVE_VALUE.equals(messageEntity.getPhysicalArchive())) {
                    physicalAccessField.setValue(null);
                } else {
                    physicalAccessField.setValue(messageEntity.getPhysicalArchive());
                }
                metadataFields.add(physicalAccessField);
            }

            if(StringUtils.isNotBlank(messageEntity.getPhysicalType())) {
                TeamsIdentifier physicalTypeVal = new TeamsIdentifier("CUSTOM.NICK PROD.PHYSICAL TYPE");
                MetadataField physicalTypeField = new MetadataField(physicalTypeVal);
                if(REMOVE_VALUE.equals(messageEntity.getPhysicalType())) {
                    physicalTypeField.setValue(null);
                } else {
                    Integer physicalType = nickProdMessagingService.getId(messageEntity.getPhysicalType().toUpperCase(), "NICK_PROD_LKP_PHYSICAL_TYPE", "ID", "VALUE");
                    physicalTypeField.setValue(new BigDecimal(physicalType));
                }
                metadataFields.add(physicalTypeField);
            }

            if(StringUtils.isNotBlank(messageEntity.getProdDescription())) {
                TeamsIdentifier prodDescVal = new TeamsIdentifier("CUSTOM.NICK PROD DESCRIPTION");
                MetadataField prodDescField = new MetadataField(prodDescVal);
                if(REMOVE_VALUE.equals(messageEntity.getProdDescription())) {
                    prodDescField.setValue(null);
                } else {
                    prodDescField.setValue(messageEntity.getProdDescription());
                }
                metadataFields.add(prodDescField);
            }
*/
		/*if(StringUtils.isNotBlank(messageEntity.getProdID()) && !messageEntity.getProdID().trim().equals("0")) {
			TeamsIdentifier prodIdVal = new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD PRODID");
			MetadataField prodIdField = new MetadataField(prodIdVal);
			if(REMOVE_VALUE.equals(messageEntity.getProdID())) {
				prodIdField.setValue(null);
			} else {
				prodIdField.setValue(messageEntity.getProdID());
				conditionCol = messageEntity.getProdID().toUpperCase();
				searchCol = "PRODUCTION_ID";
			}
			metadataFields.add(prodIdField);
		}*/

                if (StringUtils.isNotBlank(messageEntity.getProductionName())) {
                    TeamsIdentifier prodNameVal = new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD PRODUCTION NAME");
                    MetadataField prodNameField = new MetadataField(prodNameVal);
                    log.info("ProductionName {}", messageEntity.getProductionName());
                    if (REMOVE_VALUE.equals(messageEntity.getProductionName())) {
                        prodNameField.setValue(null);
                    } else {
                        prodNameField.setValue(messageEntity.getProductionName());
                        conditionCol = messageEntity.getProductionName().toUpperCase();
                        searchCol = "PRODUCTION_NAME";
                    }
                    metadataFields.add(prodNameField);
                }

                if (StringUtils.isNotBlank(conditionCol)) {
                    Integer production = metadataService.getId(conditionCol, "NICK_PROD_PRODUCTION_NAME", "ID", searchCol);
                    if (production == null) {
                        message = "Production name not found";
                        return message;
                    }
                    TeamsIdentifier productionVal = new TeamsIdentifier("CUSTOM.NICK PROD PRODUCTION");
                    MetadataField productionField = new MetadataField(productionVal);
                    productionField.setValue(new BigDecimal(production));
                    metadataFields.add(productionField);
                }
/*
            if(StringUtils.isNotBlank(messageEntity.getProvenance())) {
                TeamsIdentifier provenanceVal = new TeamsIdentifier("CUSTOM.NICK PROD PROVENANCE");
                MetadataField provenanceField = new MetadataField(provenanceVal);
                if(REMOVE_VALUE.equals(messageEntity.getProductionName())) {
                    provenanceField.setValue(null);
                } else {
                    provenanceField.setValue(messageEntity.getProvenance());
                }
                metadataFields.add(provenanceField);
            }

            if(StringUtils.isNotBlank(messageEntity.getReleaseDate())) {
                TeamsIdentifier relaseDateVal = new TeamsIdentifier("CUSTOM.NICK PROD RELEASE DATE");
                MetadataField relaseDateField = new MetadataField(relaseDateVal);
                if(REMOVE_VALUE.equals(messageEntity.getReleaseDate())) {
                    relaseDateField.setValue(null);
                } else {
                    relaseDateField.setValue(formatter.parse(messageEntity.getReleaseDate()));
                }
                metadataFields.add(relaseDateField);
            }

            if(StringUtils.isNotBlank(messageEntity.getSgid())) {
                TeamsIdentifier sgidVal = new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD SGID");
                MetadataField sgidField = new MetadataField(sgidVal);
                if(REMOVE_VALUE.equals(messageEntity.getSgid())) {
                    sgidField.setValue(null);
                } else {
                    sgidField.setValue(new BigDecimal(messageEntity.getSgid()));
                }
                metadataFields.add(sgidField);
            }

            if(StringUtils.isNotBlank(messageEntity.getStatus())) {
                TeamsIdentifier statusVal = new TeamsIdentifier("CUSTOM.NICK PROD STATUS");
                MetadataField statusField = new MetadataField(statusVal);
                if(REMOVE_VALUE.equals(messageEntity.getStatus())) {
                    statusField.setValue(null);
                } else {
                    Integer status = nickProdMessagingService.getId(messageEntity.getStatus().toUpperCase(), "NICK_PROD_LKP_STATUS", "ID", "VALUE");
                    statusField.setValue(new BigDecimal(status));
                }
                metadataFields.add(statusField);
            }

            if(StringUtils.isNotBlank(messageEntity.getTerritory())) {
                TeamsIdentifier territoryVal = new TeamsIdentifier("CUSTOM.NICK PROD TERRITORY");
                MetadataField territoryField = new MetadataField(territoryVal);
                if(REMOVE_VALUE.equals(messageEntity.getTerritory())) {
                    territoryField.setValue(null);
                } else {
                    Integer territory = nickProdMessagingService.getId(messageEntity.getTerritory().toUpperCase(), "NICK_PROD_LKP_TERRITORY", "ID", "VALUE");
                    territoryField.setValue(new BigDecimal(territory));
                }
                metadataFields.add(territoryField);
            }
*/
                MetadataField stageField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD STAGE"));

                if (StringUtils.isNotBlank(messageEntity.getStage())
                        && !REMOVE_VALUE.equals(messageEntity.getStage())) {
                    log.info("Stage {}", messageEntity.getStage());
                    stageField.setValue(messageEntity.getStage());
                    metadataFields.add(stageField);
                } else {
                    if (stageField.getValue() != null) {
                        stageField.setValue(null);
                        metadataFields.add(stageField);
                    }
                }


                if (StringUtils.isNotBlank(messageEntity.getType())) {
                    log.info("Type {}", messageEntity.getType());
                    TeamsIdentifier typeVal = new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD TYPE");
                    MetadataField typeField = new MetadataField(typeVal);
                    if (REMOVE_VALUE.equals(messageEntity.getType())) {
                        typeField.setValue(null);
                    } else {
                        typeField.setValue(messageEntity.getType());
                    }
                    metadataFields.add(typeField);
                }
/*
            if(StringUtils.isNotBlank(messageEntity.getWidth())) {
                TeamsIdentifier widthVal = new TeamsIdentifier("CUSTOM.NICK PROD WIDTH");
                MetadataField widthField = new MetadataField(widthVal);
                if(REMOVE_VALUE.equals(messageEntity.getWidth())) {
                    widthField.setValue(null);
                } else {
                    widthField.setValue(new BigDecimal(messageEntity.getWidth()));
                }
                metadataFields.add(widthField);
            }
*/
                MetadataField yearBookField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD YEARBOOK"));

                if (StringUtils.isNotBlank(messageEntity.getYearbook()) && !REMOVE_VALUE.equals(messageEntity.getYearbook())) {
                    log.info("Yearbook {}", messageEntity.getYearbook());
                    yearBookField.setValue(messageEntity.getYearbook());
                    metadataFields.add(yearBookField);
                } else {
                    if (yearBookField.getValue() != null) {
                        yearBookField.setValue(null);
                        metadataFields.add(yearBookField);
                    }
                }

                MetadataField locationField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD IMAGELOCATION"));

                if (StringUtils.isNotBlank(messageEntity.getLocation()) && !REMOVE_VALUE.equals(messageEntity.getLocation())) {
                    log.info("Location {}", messageEntity.getLocation());
                    locationField.setValue(messageEntity.getLocation());
                    metadataFields.add(locationField);

                } else {
                    if (locationField.getValue() != null) {
                        locationField.setValue(null);
                        metadataFields.add(locationField);
                    }
                }

                log.info("Tabular fields check");
                Optional<Material> materialOptional = Optional.ofNullable(messageEntity.getMaterial());

                MetadataTableField materialField = (MetadataTableField) metadataServices.retrieveMetadataFieldForAsset(asset.getAssetId(),
                        new TeamsIdentifier("CUSTOM.NICK PROD MATERIAL ID"), securitySession);

                materialField.clearValues();

                if (materialOptional.isPresent()) {
                    log.info("materials present");
                    if (!CollectionUtils.isEmpty(messageEntity.getMaterial().getAdd())) {
                        for (String addVal : messageEntity.getMaterial().getAdd()) {
                            materialField.addValue(addVal);
                        }
                    }

                }
                metadataFields.add(materialField);

                Optional<PostEpisode> postEpsOptional = Optional.ofNullable(messageEntity.getPostEpisodeID());

                MetadataTableField postEpisodeField = (MetadataTableField) metadataServices
                        .retrieveMetadataFieldForAsset(asset.getAssetId(),
                                new TeamsIdentifier("CUSTOM.NICK PROD POST EPISODE.POST EPISODE ID"), securitySession);

                postEpisodeField.clearValues();

                if (postEpsOptional.isPresent()) {
                    log.info("POst Episode present");

                    if (!CollectionUtils.isEmpty(messageEntity.getPostEpisodeID().getAdd())) {
                        for (String addVal : messageEntity.getPostEpisodeID().getAdd()) {
                            postEpisodeField.addValue(addVal);
                        }
                    }

                }
                metadataFields.add(postEpisodeField);

                Optional<ProdShow> prodShowOptional = Optional.ofNullable(messageEntity.getProdShowID());

                MetadataTableField prodShowField = (MetadataTableField) metadataServices
                        .retrieveMetadataFieldForAsset(asset.getAssetId(),
                                new TeamsIdentifier("CUSTOM.NICK PROD REUSE PROD SHOW.PROD SHOW ID"), securitySession);

                prodShowField.clearValues();

                if (prodShowOptional.isPresent()) {
                    log.info("PROD show present");
                    if (!CollectionUtils.isEmpty(messageEntity.getProdShowID().getAdd())) {
                        for (String addVal : messageEntity.getProdShowID().getAdd()) {
                            prodShowField.addValue(addVal);
                        }
                    }
                }
                metadataFields.add(prodShowField);

                Optional<Keyword> keywordwOptional = Optional.ofNullable(messageEntity.getKeywords());

                MetadataTableField keywordField = (MetadataTableField) metadataServices
                        .retrieveMetadataFieldForAsset(asset.getAssetId(),
                                new TeamsIdentifier("KEYWORD"), securitySession);
                keywordField.clearValues();

                if (keywordwOptional.isPresent()) {
                    log.info("Keywords present");
                    if (!CollectionUtils.isEmpty(messageEntity.getKeywords().getAdd())) {
                        for (String addVal : messageEntity.getKeywords().getAdd()) {
                            keywordField.addValue(addVal);
                        }
                    }
                }
                metadataFields.add(keywordField);

/*
            if(StringUtils.isNotBlank(messageEntity.getRetiredStatus())) {
                TeamsIdentifier isRetiredVal = new TeamsIdentifier("CUSTOM.NICK PROD RETIRED ASSET");
                MetadataField isRetiredField = new MetadataField(isRetiredVal);
                if(REMOVE_VALUE.equals(messageEntity.getRetiredStatus())) {
                    isRetiredField.setValue(null);
                } else {
                    if(messageEntity.getRetiredStatus().equalsIgnoreCase("No")){
                        isRetiredField.setValue("N");
                    } else if (messageEntity.getRetiredStatus().equalsIgnoreCase("Yes")) {
                        isRetiredField.setValue("Y");
                    }else {
                        isRetiredField.setValue("N");
                    }
//                    isRetiredField.setValue(messageEntity.getRetiredStatus());
                }
                metadataFields.add(isRetiredField);
            }

            if(StringUtils.isNotBlank(messageEntity.getRigName())) {
                TeamsIdentifier isRigVal = new TeamsIdentifier("CUSTOM.NICK PROD.RIG NAME");
                MetadataField isRigNameField = new MetadataField(isRigVal);
                if(REMOVE_VALUE.equals(messageEntity.getRigName())) {
                    isRigNameField.setValue(null);
                } else {
                    isRigNameField.setValue(messageEntity.getRigName());
                }
                metadataFields.add(isRigNameField);
            }
*/
                MetadataField fileNameCompareField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD FILENAME COMPARE"));

                if (StringUtils.isNotBlank(messageEntity.getFileNameCompare()) &&
                        !REMOVE_VALUE.equals(messageEntity.getFileNameCompare())) {
                    log.info("fileNameCompare {}", messageEntity.getFileNameCompare());
                    fileNameCompareField.setValue(messageEntity.getFileNameCompare());
                    metadataFields.add(fileNameCompareField);
                } else {
                    if (fileNameCompareField.getValue() != null) {
                        fileNameCompareField.setValue(null);
                        metadataFields.add(fileNameCompareField);
                    }
                }


                metadataServices.saveMetadataForAssets(assetIdentifiers,
                        metadataFields.toArray(new MetadataField[0]), securitySession);

                AssetServices.getInstance().unlockAsset(asset.getAssetId(), securitySession);
                log.info("unlock asset");
            }
        }catch(BaseTeamsException e){

            log.error("Error occurred at saving Metadata - {}", e.getDebugMessage());
            message = e.getDebugMessage();
        }
        return message;
    }

    public List<NickProdMetaValidation25> getValidationReport() {

        return reportingService.getValidationReport();
    }

    public void validateAndUpdate(List<NickProdMetaValidation25> nickObjects) {
            log.info("validating and updating {}" , nickObjects.size() );
        for (NickProdMetaValidation25 nickObject : nickObjects) {
            mapJsonToObject(nickObject);
        }
    }

    private void mapJsonToObject(NickProdMetaValidation25 nickObject) {
        log.info("mapJsonToObject {}" , nickObject.getOtid());
        if (mapper == null) {
            mapper = new ObjectMapper();
        }

        try {
        //    log.info("In try");
//            ResultKey key = mapper.readValue(nickObject.getVeriResult(), ResultKey.class);
            Map<String, Object> key = mapper.readValue(nickObject.getVeriResult(), Map.class);

            if (key.size() > 1){
        //        log.info("Size > 1 : {}", key.size());
                String erroredAt = "";
                List<String> keys = new ArrayList<>();

                for (Object keyObj : key.values()) {
        //            log.info("keyObj {}", keyObj);
                //    log.info(String.valueOf(keyObj instanceof String));
//                    Map<String, Object> veriResult = mapper.readValue(keyObj.toString(), Map.class);
                    Map<String, Object> veriResult = (Map<String, Object>)keyObj;

                    keys.add(veriResult.get("Key").toString());
                    //reprocessObjects(veriResult);
                }
                erroredAt = String.join(",", keys);
                nickObject.setErrored(erroredAt);
                //nickObject.setProcessed("Multi"); location
                nickObject.setProcessed("P");

            } else {
                Map<String, Object> resultValue = (Map<String, Object>) key.get("0");
                /*String processed = reprocessObjects(resultValue, nickObject.getOtid());
                if (processed.equals("Y")) {
                    nickObject.setProcessed("Done");
                } else {
                    nickObject.setProcessed("N");
                }
                */
                nickObject.setProcessed("P");

                nickObject.setErrored(resultValue.get("Key").toString());

            }
            reportingService.saveObject(nickObject);

            //log.info(key.toString());
            //log.info("result {}", key.get("0").toString());

            //Map<String, Object> resultValue = (Map<String, Object>) key.get("0");

        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            nickObject.setProcessed("Error");
            reportingService.saveObject(nickObject);
        }
    }

    private String reprocessObjects(Map<String, Object> veriResult, String otid) {

        String processed = "N";
        if (StringUtils.isNotBlank(veriResult.get("Key").toString())) {

            if (veriResult.get("Key").toString().equals("firstSeason")) {
                String column = "SEASON";
                if (StringUtils.isNotBlank(veriResult.get("Sent").toString())) {
                    reportingService.updateMetadataFromReprocess(veriResult.get("Sent").toString(), otid, column);
                } else {
                    reportingService.updateMetadataFromReprocess(null, otid, column);
                }
                processed = "Y";
            }
            if (veriResult.get("Key").toString().equals("artists")) {
                String column = "ARTISTS";
                if (StringUtils.isNotBlank(veriResult.get("Sent").toString())) {
                    reportingService.updateMetadataFromReprocess(veriResult.get("Sent").toString(), otid, column);
                } else {
                    reportingService.updateMetadataFromReprocess(null, otid, column);
                }
                processed = "Y";
            }
            if (veriResult.get("Key").toString().equals("assetDescription")) {
                String column = "ASSET_DESCRIPTION";
                if (StringUtils.isNotBlank(veriResult.get("Sent").toString())) {
                    reportingService.updateMetadata2FromReprocess(veriResult.get("Sent").toString(), otid, column);
                } else {
                    reportingService.updateMetadata2FromReprocess(null, otid, column);
                }
                processed = "Y";
            }
            if (veriResult.get("Key").toString().equals("fileNameCompare")) {
                String column = "FILE_NAME_COMPARE";
                if (StringUtils.isNotBlank(veriResult.get("Sent").toString())) {
                    reportingService.updateMetadataFromReprocess(veriResult.get("Sent").toString(), otid, column);
                } else {
                    reportingService.updateMetadataFromReprocess(null, otid, column);
                }
                processed = "Y";
            }
            if (veriResult.get("Key").toString().equals("keywords")) {
                try {
                    NickProdMetadataUpdateMarch25 nickObject = reportingService.getMetadataFromDump(otid);

                    NickProdUploadMsgInboundEntity entity = mapper.readValue(nickObject.getMetadata(), NickProdUploadMsgInboundEntity.class);

                    Keyword keywords = entity.getKeywords();

                    //reportingService.updateKeywords(keywords);
                    for (String s : keywords.getAdd()){
                        log.info(s);
                    }

                    if (StringUtils.isNotBlank(veriResult.get("Sent").toString())){
                        log.info("sent {}", veriResult.get("Sent").toString());
                    }
                } catch (Exception e) {
                    //throw new RuntimeException(e);
                    log.error("Error at keywords - {}", e.getMessage());
                    return "N";
                }
                processed = "Y";
            }
        }
        return processed;
    }

    public List<NickProdMetadataUpdateMarch25> getMetaDumpDataForComparing() {
        return metadataService.getMetaDumpDataForComparing();
    }

    public void stageMetadata(List<NickProdMetadataUpdateMarch25> nickObjects) {

        log.info("stage metadata {}", nickObjects.size());
        for (NickProdMetadataUpdateMarch25 nickObject : nickObjects) {
            NickProdUploadMsgInboundEntity entity = mapJsonToMetaObject(nickObject);

            if (entity != null) {
                log.info("Entity is not null");
                Long entityId = saveMetadataEntity(entity, nickObject.getId(), nickObject.getOtid());
                if (entityId != null) {
                    nickObject.setEntityId(String.valueOf(entityId));
                } else {
                    nickObject.setEntityId("STAGING FAILED");
                }
            } else {
                nickObject.setEntityId("MAPPING FAILED");
            }

            metadataService.saveObject(nickObject);
        }
    }

    private Long saveMetadataEntity(NickProdUploadMsgInboundEntity entity, Long id, String otid) {

        log.info("In saveInboundEntity for id {} ",id);
        NickProdMetaCompareEntityMar25 uploadMsgEntity = new NickProdMetaCompareEntityMar25();
        uploadMsgEntity.setQueueId(Math.toIntExact(id));//----Int

        if (StringUtils.isNotBlank(entity.getFmQueueId())){
            uploadMsgEntity.setFmQueueId(Integer.valueOf(entity.getFmQueueId()));//------Int
        }

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


        Long entityId = metadataService.saveEntity(uploadMsgEntity);


        Optional<Material> materialOptional = Optional.ofNullable(entity.getMaterial());
        List<NPMetaCompareTabularDataMar25> tabularReports = new ArrayList<>();
        if(materialOptional.isPresent()) {
            log.info("Materials present");

            uploadMsgEntity.setMaterial(Constants.YES);
            if(Constants.YES.equals(entity.getMaterial().getRemoveAll())) {
                NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                report.setQueueId(Math.toIntExact(id));
                report.setUoiId(otid);
                report.setFieldType(Constants.MATERIAL);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);
            } else {
                if(!CollectionUtils.isEmpty(entity.getMaterial().getRemove())) {
                    for (String rmValue : entity.getMaterial().getRemove()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setQueueId(Math.toIntExact(id));
                        report.setUoiId(otid);
                        report.setFieldType(Constants.MATERIAL);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getMaterial().getAdd())) {
                    for (String addVal : entity.getMaterial().getAdd()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setQueueId(Math.toIntExact(id));
                        report.setUoiId(otid);
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
                NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                report.setQueueId(Math.toIntExact(id));
                report.setUoiId(otid);
                report.setFieldType(Constants.POST_EPISODE);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);

            } else {

                if(!CollectionUtils.isEmpty(entity.getPostEpisodeID().getRemove())) {

                    for (String rmValue : entity.getPostEpisodeID().getRemove()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setUoiId(otid);
                        report.setQueueId(Math.toIntExact(id));
                        report.setFieldType(Constants.POST_EPISODE);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getPostEpisodeID().getAdd())) {
                    for (String addVal : entity.getPostEpisodeID().getAdd()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setQueueId(Math.toIntExact(id));
                        report.setUoiId(otid);
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
                NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                report.setQueueId(Math.toIntExact(id));
                report.setUoiId(otid);
                report.setFieldType(Constants.PROD_SHOW);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);
            } else {
                if(!CollectionUtils.isEmpty(entity.getProdShowID().getRemove())) {

                    for (String rmValue : entity.getProdShowID().getRemove()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setQueueId(Math.toIntExact(id));
                        report.setUoiId(otid);
                        report.setFieldType(Constants.PROD_SHOW);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getProdShowID().getAdd())) {
                    for (String addVal : entity.getProdShowID().getAdd()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setQueueId(Math.toIntExact(id));
                        report.setUoiId(otid);
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
                NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                report.setQueueId(Math.toIntExact(id));
                report.setUoiId(otid);
                report.setFieldType(Constants.KEYWORDS);
                report.setRemoveAllValue(Constants.YES);
                tabularReports.add(report);
            } else {
                if(!CollectionUtils.isEmpty(entity.getKeywords().getRemove())) {

                    for (String rmValue : entity.getKeywords().getRemove()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setQueueId(Math.toIntExact(id));
                        report.setUoiId(otid);
                        report.setFieldType(Constants.KEYWORDS);
                        report.setRemoveValue(rmValue);
                        tabularReports.add(report);
                    }
                }
                if(!CollectionUtils.isEmpty(entity.getKeywords().getAdd())) {
                    for (String addVal : entity.getKeywords().getAdd()) {
                        NPMetaCompareTabularDataMar25 report = new NPMetaCompareTabularDataMar25();
                        report.setQueueId(Math.toIntExact(id));
                        report.setUoiId(otid);
                        report.setFieldType(Constants.KEYWORDS);
                        report.setAddValue(addVal);
                        tabularReports.add(report);
                    }
                }
            }
        }

        metadataService.saveTabular(tabularReports);

        return entityId;
    }

    private NickProdUploadMsgInboundEntity mapJsonToMetaObject(NickProdMetadataUpdateMarch25 nickObject) {

        log.info("MapJsonToMetaObject instance for id {} ",nickObject.getId());
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        NickProdUploadMsgInboundEntity entity = null;
        try {
            entity = mapper.readValue(nickObject.getMetadata(), NickProdUploadMsgInboundEntity.class);
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
            return null;
        }

        return entity;
    }
}
