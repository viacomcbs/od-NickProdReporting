package com.ppc.mm.common.util;

import com.ppc.mm.nickprodreporting.entity.MMInboundReport;
import com.ppc.mm.nickprodreporting.entity.NickObjects;
import com.ppc.mm.nickprodreporting.service.ReportingService;
import com.ppc.mm.nickprodreporting.util.ProcessDuplicateAssets;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PullMessagesFromInboundQueue {
	
	/**
	 * Logger instance
	 */
	private static final Logger LOG = LoggerFactory.getLogger(PullMessagesFromInboundQueue.class);
	
	@Autowired
	private ReportingService reportingService;

	@Autowired
	ProcessDuplicateAssets processDuplicateAssets;

	//@Autowired
	//Environment environment;
/*
	@Scheduled(fixedDelay = 300*1000)
	@Async
	public void getReport() {

	List<MMInboundReport> mmInboundReports =	reportingService.getMMInboundReports(Constants.NO);

		if (!CollectionUtils.isEmpty(mmInboundReports)){
			LOG.info("in getReport");
			processDuplicateAssets.getReports(mmInboundReports);
		}
	}
*/
/*
	@Scheduled(fixedDelay = 300*1000)
	@Async
	public void validateReport() {
		LOG.info("in validateReport --------->");
		List<MMInboundReport> mmInboundReports =	reportingService.getMMInboundReports(Constants.YES);
		LOG.info("size in scheduler {}",mmInboundReports.size());
		if (!CollectionUtils.isEmpty(mmInboundReports)){
			//LOG.info("in validateReport");
			processDuplicateAssets.validateReports(mmInboundReports);
		}
		LOG.info("in validateReport <-----------");

	}
*/
/*
	@Scheduled(fixedDelay = 200*1000)
	@Async
	public void validateReportBatchTwo() {
		LOG.info("in validateReportBatchTwo --------->");
		String errorMsg = environment.getProperty("errorMsg");
		//List<MMInboundReport> mmInboundReports =	reportingService.getFailedReports(Constants.INVALID_RESPONSE);
		List<MMInboundReport> mmInboundReports =	reportingService.getFailedReports(errorMsg);
//		List<MMInboundReport> mmInboundReports =	reportingService.getFailedReports(Constants.NO_UNIQUE);

		LOG.info("size {}",mmInboundReports.size());
		if (!CollectionUtils.isEmpty(mmInboundReports)){
			//LOG.info("in validateReport");
			processDuplicateAssets.validateFailedReports(mmInboundReports);
		}
		LOG.info("in validateReportBatchTwo <-----------");

	}
*/
/*
	@Scheduled(fixedDelay = 200*1000)
	@Async
	public void validateReportBatchThree() {
		LOG.info("in validateReportBatchThree --------->");
		String errorMsg = environment.getProperty("errorMsg");
		//List<MMInboundReport> mmInboundReports =	reportingService.getFailedReports(Constants.INVALID_RESPONSE);
		List<MMInboundReport> mmInboundReports =	reportingService.getFailedReports(errorMsg);
//		List<MMInboundReport> mmInboundReports =	reportingService.getFailedReports(Constants.NO_UNIQUE);

		LOG.info("size {}",mmInboundReports.size());
		if (!CollectionUtils.isEmpty(mmInboundReports)){
			//LOG.info("in validateReport");
			processDuplicateAssets.validateFailedReports(mmInboundReports);
		}
		LOG.info("in validateReportBatchThree <-----------");

	}
*/
/*
	@Scheduled(fixedDelay = 300*1000)
	@Async
	public void validateNickObjects() {
		LOG.info("in validateNickObjects --------->");

			List<NickObjects> nickObjects =	reportingService.getNickObjects(Constants.NO);
			LOG.info("size in scheduler {}",nickObjects.size());
			if (!CollectionUtils.isEmpty(nickObjects)){

					LOG.info("in validateNickObjects");
					processDuplicateAssets.validateNickObjects(nickObjects);
			}

		LOG.info("in validateNickObjects <-----------");

	}
*/
/*
	@Scheduled(fixedDelay = 300*1000)
	@Async
	public void validateProcessedNickObjects() {
		LOG.info("in validateProcessedNickObjects --------->");

		List<NickObjects> nickObjects =	reportingService.getNickObjects(Constants.YES);
		LOG.info("size in scheduler {}",nickObjects.size());
		if (!CollectionUtils.isEmpty(nickObjects)){

			LOG.info("in validateNickObjects");
			processDuplicateAssets.validateProcessedNickObjects(nickObjects);
		}

		LOG.info("in validateProcessedNickObjects <-----------");

	}

	@Scheduled(fixedDelay = 300*1000)
	@Async
	public void reprocessUnversionedNickObjects() {
		LOG.info("in reprocessUnversionedNickObjects --------->");

		List<NickObjects> nickObjects =	reportingService.getNickObjectsForReprocess(Constants.YES);
		LOG.info("size in scheduler {}",nickObjects.size());
		if (!CollectionUtils.isEmpty(nickObjects)){

			LOG.info("in validateNickObjects");
			processDuplicateAssets.reprocessUnversionedNickObjects(nickObjects);
		}

		LOG.info("in reprocessUnversionedNickObjects <-----------");

	}
	*/

/*
	@Scheduled(fixedDelay = 300*1000)
	@Async
	public void reprocessNonDuplicateNickObjects() {
		LOG.info("in reprocessNonDuplicateNickObjects --------->");

		List<NickObjects> nickObjects =	reportingService.getNickObjects("NonDup");
		LOG.info("size in scheduler {}",nickObjects.size());
		if (!CollectionUtils.isEmpty(nickObjects)){

			LOG.info("in reprocessNonDuplicateNickObjects");
			processDuplicateAssets.reprocessNonDuplicateNickObjects(nickObjects);
		}

		LOG.info("in reprocessNonDuplicateNickObjects <-----------");

	}
*/
}
