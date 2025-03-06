package com.ppc.mm.common.util;

import com.artesia.common.exception.BaseTeamsException;
import com.ppc.mm.nickprodmetacomparing.entity.NickMetadataCompare;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
import com.ppc.mm.nickprodmetacomparing.util.MetadataProcessor;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PullAssetsForMetaCompare {

    private static final Logger LOG = LoggerFactory.getLogger(PullAssetsForMetaCompare.class);

    MetadataProcessor processor;

    @Autowired
    public void setProcessor(MetadataProcessor processor) {
        this.processor = processor;
    }
/*
    @Scheduled(fixedDelay = 300*1000)
    @Async
    public void processMessageForMetaCompare() {
        LOG.info("in processMessageForMetaCompare --------->");

        List<NickMetadataCompare> nickObjects =	processor.getFilesForMetadataCompare();
        LOG.info("size in scheduler {}",nickObjects.size());
        if (!CollectionUtils.isEmpty(nickObjects)){

            //LOG.info(nickObjects.get(0).toString());
            processor.processFileForMetaCompare(nickObjects);
        }

        LOG.info("in processMessageForMetaCompare <-----------");

    }
*/
/*
    @Scheduled(fixedDelay = 300*1000)
    @Async
    public void performUoiMerge() {
        LOG.info("in performUoiMerge --------->");

        List<Object[]> nickObjects =	processor.getUoiForMerge();
        LOG.info("size in scheduler {}",nickObjects.size());
        if (!CollectionUtils.isEmpty(nickObjects)){

            //LOG.info(nickObjects.get(0).toString());
            processor.mergeUoi(nickObjects);
        }

        LOG.info("in performUoiMerge <-----------");

    }

*/

    @Scheduled(fixedDelay = 900*1000)
    @Async
    public void performMetadataUpdate() throws BaseTeamsException {
        LOG.info("in performMetadataUpdate --------->");

        List<NickProdMetadataUpdateMarch25> nickObjects =	processor.getMetaDumpData();
        LOG.info("size in scheduler {}",nickObjects.size());
        if (!CollectionUtils.isEmpty(nickObjects)){

            //LOG.info(nickObjects.get(0).toString());
            processor.updateMetadata(nickObjects);
        }

        LOG.info("in performMetadataUpdate <-----------");

    }
}
