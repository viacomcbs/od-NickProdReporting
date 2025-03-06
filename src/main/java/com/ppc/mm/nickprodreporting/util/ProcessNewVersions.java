package com.ppc.mm.nickprodreporting.util;

import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata;
import com.ppc.mm.nickprodmessaging.entity.NickProdAssetMetadata2;
import com.ppc.mm.nickprodreporting.entity.NpNewVersionReport;
import com.ppc.mm.nickprodreporting.service.NewVersionService;
import com.ppc.mm.nickprodreporting.service.NewVersionServiceImpl;
import com.ppc.mm.nickprodreporting.service.ReportingService;
import com.ppc.mm.nickprodreporting.service.ReportingServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProcessNewVersions {

    private static final Logger log = LoggerFactory.getLogger(ProcessNewVersions.class);
    NewVersionService newVersionService;

    ReportingService reportingService;


    @Autowired
    public void setNewVersionService(NewVersionServiceImpl service) {
        newVersionService = service;
    }

    @Autowired
    public void setReportingService(ReportingServiceImpl service) {
        reportingService = service;
    }

    public void validateAssets(NpNewVersionReport mmInboundReport, String oldUoiId) {

        // get previous version from logical uoi id
        //oldUoiId
        String errorMsg = null;
        NickProdAssetMetadata latest = reportingService.getNickProdAssetMetadata(mmInboundReport.getUoiId());
        NickProdAssetMetadata previous = reportingService.getNickProdAssetMetadata(oldUoiId);
        NickProdAssetMetadata2 latestAdditional = reportingService.getNickProdAdditionalMetadata(mmInboundReport.getUoiId());
        NickProdAssetMetadata2 previousAdditional = reportingService.getNickProdAdditionalMetadata(oldUoiId);
        errorMsg = compareAndUpdate(latest, previous, latestAdditional, previousAdditional);

        log.info("After compare ");

        if (StringUtils.isNotBlank(errorMsg)) {
            log.info("errorMsg not null ");

            mmInboundReport.setErrorMsg(errorMsg);
            mmInboundReport.setStatus("FAILED");
            newVersionService.saveNewVersionReport(mmInboundReport);
        }
        else {
            log.info("errorMsg null ");

            mmInboundReport.setStatus("SUCCESS");
            newVersionService.saveNewVersionReport(mmInboundReport);
        }
        //match asset metadata
    }

    private String compareAndUpdate(NickProdAssetMetadata latest, NickProdAssetMetadata previous, NickProdAssetMetadata2 latestAdditional, NickProdAssetMetadata2 previousAdditional) {

        //log.info("In compareAndUpdate");
        //StringBuilder message = new StringBuilder();

        String errorMsg = "";
        if (previous != null) {

            //log.info("checking fmqueueId");
            if (!Objects.equals(latest.getFmQueueId(), previous.getFmQueueId())) {
                //message.append("fmQueueId ");
                errorMsg = "fmQueueId ";
            }
            //log.info("checking Category");
            if (!Objects.equals(latest.getCategory(), previous.getCategory())) {
                //message.append("category ");
                errorMsg += "category ";

            }


            //private String condition;//3/23/2018 4:30:45 PM
            //private String conditionNotes;//3/20/2018 1:15:43 PM
            //private String material;	//------------------------1 never used
            //private String appraisedAt; never used
            //private String appraisedBy; never used
            //private String appraisedValue; never used
            //log.info("checking getArtists");
            if (!Objects.equals(latest.getArtists(), previous.getArtists())) {
                //message.append("artists ");
                errorMsg += "artists ";

            }

            //log.info("checking getAssetName");

            if (!Objects.equals(latest.getAssetName(), previous.getAssetName())) {
                //message.append("assetName ");
                errorMsg += "assetName ";

            }
            //log.info("checking getBarcode");

            if (!Objects.equals(latest.getBarcode(), previous.getBarcode())) {
                //message.append("barcode ");
                errorMsg += "barcode ";

            }
            //log.info("checking getBoxId");

            if (!Objects.equals(latest.getBoxId(), previous.getBoxId())) {
                //message.append("boxID ");
                errorMsg += "boxID ";

            }


            //private String contextualNote; last used 6/4/2019 2:25:57 PM
            //private String creationDate; last used 9/17/2021 1:29:10 PM
            //private String dateImported; never used
            //log.info("checking getEicAccess");

            if (!Objects.equals(latest.getEicAccess(), previous.getEicAccess())) {
                //message.append("eicAccess ");
                errorMsg += "eicAccess ";

            }
            //log.info("checking getEicComments");

            if (!Objects.equals(latest.getEicComments(), previous.getEicComments())) {
                //message.append("eicComments ");
                errorMsg += "eicComments ";

            }


            //private String eicReview; last used 5/5/2022 4:29:23 PM
            //private String eicStatus; last used 10/23/2017 2:27:31 PM
            //log.info("checking getFmId");

            if (!Objects.equals(latest.getFmId(), previous.getFmId())) {
                //message.append("fmid ");
                errorMsg += "fmid ";

            }
            //log.info("checking getFilename");

            if (!Objects.equals(latest.getFilename(), previous.getFilename())) {
                //message.append("filename ");
                errorMsg += "filename ";

            }
            //log.info("checking getSeason");

            if (!Objects.equals(latest.getSeason(), previous.getSeason())) {
                //message.append("firstSeason ");
                errorMsg += "firstSeason ";

            }
            //log.info("checking getFirstShow");

            if (!Objects.equals(latest.getFirstShow(), previous.getFirstShow())) {
                //message.append("firstShow ");
                errorMsg += "firstShow ";

            }


            //private String hasDuplicates; not used in requests
            //private String height; last used 9/17/2021 1:29:10 PM
            //private String importedBy; never used
            //private String length; last used 6/10/2019 9:40:53 AM
            //log.info("checking getNcrAccess");

            if (!Objects.equals(latest.getNcrAccess(), previous.getNcrAccess())) {
                //message.append("ncrAccess ");
                errorMsg += "ncrAccess ";

            }


            //private String physicalArchive; never used
            //private String physicalType; never used
            //private String prodDescription; never used
            //log.info("checking getProdId");

            if (!Objects.equals(latest.getProdId(), previous.getProdId())) {
                //message.append("prodID ");
                errorMsg += "prodID ";

            }

            //log.info("checking getProductionName");

            if (!Objects.equals(latest.getProductionName(), previous.getProductionName())) {
                //message.append("productionName ");
                errorMsg += "productionName ";

            }
            //log.info("checking getProvenance");

            if (!Objects.equals(latest.getProvenance(), previous.getProvenance())) {
                //message.append("provenance ");
                errorMsg += "provenance ";

            }
            //log.info("checking getSgid");

            if (!Objects.equals(latest.getSgid(), previous.getSgid())) {
                // message.append("sgid ");
                errorMsg += "sgid ";

            }


            //private String status; never used in rabbit
            //private String territory; never used in rabbit
            //log.info("checking getType");

            if (!Objects.equals(latest.getType(), previous.getType())) {
                //message.append("type ");
                errorMsg += "type ";

            }

            //log.info("checking getWidth");

            if (!Objects.equals(latest.getWidth(), previous.getWidth())) {
                //message.append("width ");
                errorMsg += "width ";

            }

            // log.info("checking getYearbook");

            if (!Objects.equals(latest.getYearbook(), previous.getYearbook())) {
                //message.append("yearbook ");
                errorMsg += "yearbook ";

            }

//todo: change in column for PROD db

            if (latestAdditional != null && previousAdditional != null) {
                log.info("checking getImageLocation");

                if (!Objects.equals(latestAdditional.getImageLocation(), previousAdditional.getImageLocation())) {
                    //message.append("location ");
                    errorMsg += "location ";

                }

//todo: change in column in PROD db
                log.info("checking getAssetDescription");

                if (!Objects.equals(latestAdditional.getAssetDescription(), previousAdditional.getAssetDescription())) {
                    //message.append("assetDescription ");
                    errorMsg += "assetDescription ";

                }
            }
            // log.info("checking getIsRetired");

            if (!Objects.equals(latest.getIsRetired(), previous.getIsRetired())) {
                //message.append("retiredStatus ");
                errorMsg += "retiredStatus ";

            }

            //log.info("checking getRigName");

            if (!Objects.equals(latest.getRigName(), previous.getRigName())) {
                //message.append("rigName ");
                errorMsg += "rigName ";

            }

            //log.info("checking getStage");

            if (!Objects.equals(latest.getStage(), previous.getStage())) {
                //message.append("stage ");
                errorMsg += "stage ";

            }

            //log.info("checking getFileNameCompare");

            if (!Objects.equals(latest.getFileNameCompare(), previous.getFileNameCompare())) {
                //message.append("fileNameCompare ");
                errorMsg += "fileNameCompare ";

            }

            //log.info("checking getFileSize "+message.length());
        } else {
            errorMsg += "no previous version available ";
        }
        //return message.toString();
        return errorMsg;

    }

    public void processFileNameCompares(String nickObject) {
        log.info("processFileNameCompares");
        //newVersionService.executeProcedure(nickObjects);

        List<String> uoiIds = newVersionService.getUoiIds(nickObject);

        int counter = 1;
        String isLatest = "N";
        String original = uoiIds.get(0);
        log.info("original: {}" , original);
        for (String id : uoiIds) {
            log.info("updating uoiId {}" , id);

            if (counter == uoiIds.size()) {
                isLatest = "Y";
            }
            newVersionService.updateUois(id, counter, isLatest, original);
            log.info("inserting updated uoid");
            newVersionService.insertFileCompareObjects(id);
            counter++;
        }
        log.info("updating filename objects");
        newVersionService.updateFileNameObjects(nickObject);

    }
}
