package com.ppc.mm.common.util;

import com.ppc.mm.nickprodreporting.entity.NickObjects;
import com.ppc.mm.nickprodreporting.entity.NpNewVersionReport;
import com.ppc.mm.nickprodreporting.service.NewVersionService;
import com.ppc.mm.nickprodreporting.service.NewVersionServiceImpl;
import com.ppc.mm.nickprodreporting.util.ProcessNewVersions;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PullNewVersionMessages {

    private static final Logger LOG = LoggerFactory.getLogger(PullNewVersionMessages.class);

    NewVersionService newVersionService;

	ProcessNewVersions processNewVersions;

    @Autowired
    PullNewVersionMessages(NewVersionServiceImpl service) {
        newVersionService = service;
    }

	@Autowired
	public void setProcessNewVersions(ProcessNewVersions processNewVersions) {
		this.processNewVersions = processNewVersions;
	}
/*
	@Scheduled(fixedDelay = 400*1000)
	@Async
	public void validateNewVersionReport() {
		LOG.info("in validateNewVersionReport --------->");
		List<NpNewVersionReport> mmInboundReports =	newVersionService.getNewVersionReport();
		LOG.info("size {}",mmInboundReports.size());
		if (!CollectionUtils.isEmpty(mmInboundReports)){

			for (NpNewVersionReport mmInboundReport : mmInboundReports){
				//LOG.info(mmInboundReport.getUoiId());
				String oldUoiId = newVersionService.getUoiId(mmInboundReport);
				LOG.info(mmInboundReport.getUoiId() +" "+ oldUoiId);
				processNewVersions.validateAssets(mmInboundReport, oldUoiId);
			}
		}
		LOG.info("in validateNewVersionReport <-----------");

	}*/
/*
    @Scheduled(fixedDelay = 300*1000)
    @Async
    public void processNewVersionsByFileNameCompare() {
        LOG.info("in processNewVersionsByFileNameCompare --------->");

        Integer prodId = 233;
        List<String> nickObjects =	newVersionService.getFileNamesForProcess(prodId);
        LOG.info("size in scheduler {}",nickObjects.size());
        if (!CollectionUtils.isEmpty(nickObjects)){

            //LOG.info("in processNewVersionsByFileNameCompare");

            for (String nickObject : nickObjects) {
                LOG.info("processNewVersionsByFileNameCompare nickObject {}", nickObject);
                processNewVersions.processFileNameCompares(nickObject);

            }
        }

        LOG.info("in processNewVersionsByFileNameCompare <-----------");

    }*/
}
