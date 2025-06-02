package com.ppc.mm.nickprodmetacomparing.util;

import com.artesia.asset.Asset;
import com.artesia.asset.AssetIdentifier;
import com.artesia.asset.metadata.services.AssetMetadataServices;
import com.artesia.asset.services.AssetServices;
import com.artesia.common.exception.BaseTeamsException;
import com.artesia.entity.TeamsIdentifier;
import com.artesia.metadata.MetadataCollection;
import com.artesia.metadata.MetadataElement;
import com.artesia.metadata.MetadataField;
import com.artesia.metadata.MetadataTableField;
import com.artesia.security.SecuritySession;
import com.artesia.security.session.services.AuthenticationServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppc.mm.nickprodmessaging.util.*;
import com.ppc.mm.nickprodmetacomparing.entity.NickMetadataCompare;
import com.ppc.mm.nickprodmetacomparing.entity.NickProdMetadataUpdateMarch25;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.ppc.mm.common.util.Constants.REMOVE_VALUE;

@Component
public class MetadataProcessor {

	private static final Logger log = LoggerFactory.getLogger(MetadataProcessor.class);

	MetadataService metadataService;
	ReportingService reportingService;

	private MetadataCollection mc = null;

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

			log.info("merge uoi {} ", obj[0].toString());

			// for (Object o : obj) {
			// log.info("in obj uoi {} ",o.toString());
			// }
			String uoiId = obj[0].toString();
			String logicalUoiId = obj[1].toString();
			String version = obj[2].toString();
			String isLatestVersion = obj[3].toString();

			// update uois
			metadataService.updateUois(uoiId, logicalUoiId, version, isLatestVersion);
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
		// log.info("in update metadata ");

		SecuritySession securitySession = null;

		try {
			String userName = environment.getRequiredProperty("nickprodAdminUserName");
			String password = environment.getRequiredProperty("nickprodAdminPassword");

//            log.info("userName : {}",userName);
//            log.info("password : {}",password);
			securitySession = AuthenticationServices.getInstance().login(userName, password);
			int count = 0;
			for (NickProdMetadataUpdateMarch25 nickObject : nickObjects) {
				log.info("updating metadata {} {}", nickObject.getOtid(), count++);
				mapToJson(nickObject, securitySession);
			}

		} catch (BaseTeamsException e) {
			log.error("Error in creating session {}", e.getDebugMessage());
		} finally {
			if (securitySession != null) {
				AuthenticationServices.getInstance().logout(securitySession);
			}
		}

	}

	private void mapToJson(NickProdMetadataUpdateMarch25 nickObject, SecuritySession securitySession) {
		// log.info("mapToJson {} ",nickObject.getOtid());
		
		log.info("Inside mapToJson method for OTID: {}"+ nickObject.getOtid());

		if (mapper == null) {
			mapper = new ObjectMapper();
		}

		try {
			
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


			NickProdUploadMsgInboundEntity entity = mapper.readValue(nickObject.getMetadata(),
					NickProdUploadMsgInboundEntity.class);
			// log.info("NickProdUploadMsgInboundEntity {} ",entity.toString());
			// NickProdAssetMetadata latest =
			// reportingService.getNickProdAssetMetadata(nickObject.getOtid());
			// NickProdAssetMetadata2 latestAdditional =
			// reportingService.getNickProdAdditionalMetadata(nickObject.getOtid());
			nickObject.setProcessed("Y");
			nickObject.setProcessedDate(new Date());
			entity.setOtid(nickObject.getOtid());
			String message = updateMetadata(entity, securitySession);

			if (StringUtils.isNotBlank(message)) {
				nickObject.setProcessed("N");
				nickObject.setErrorMsg(message);
			}

		} catch (JsonProcessingException e) {
			// throw new RuntimeException(e);

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

		Asset asset = new Asset(new AssetIdentifier(messageEntity.getOtid()));

		try {
			// deletePreviousMetadata(messageEntity.getOtid());

			 log.info("Asset created {}",asset.getAssetId().asString());
			assetIdentifiers[0] = asset.getAssetId();
			if (asset.isLocked()) {
				message = "Asset is locked";
				log.info(asset.getContentLockStateUserName());

			} else {

				// log.info("Before locking asset ");
				AssetServices.getInstance().lockAsset(asset.getAssetId(), securitySession);
				// log.info("Asset Locked {}", asset.getAssetId());
				AssetMetadataServices metadataServices = AssetMetadataServices.getInstance();

				this.mc = asset.getMetadata();
				if (this.mc == null)
					this.mc = new MetadataCollection();

				MetadataField fmQueueField = new MetadataField(new TeamsIdentifier("CUSTOM.NICKPROD.FMQUEUEID"));

				if (StringUtils.isNotBlank(messageEntity.getFmQueueId())) {
					// log.info("FMQUEUEID {}", messageEntity.getFmQueueId());
					fmQueueField.setValue(messageEntity.getFmQueueId());
					metadataFields.add(fmQueueField);
				} else {
					if (fmQueueField.getValue() != null) {
						fmQueueField.setValue(null);
						metadataFields.add(fmQueueField);
					}
				}

				if (StringUtils.isNotBlank(messageEntity.getCategory())) {

					MetadataField categoryField = new MetadataField(
							new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD CATEGORY"));

					if (!REMOVE_VALUE.equals(messageEntity.getCategory())) {
						log.info("Category {}", messageEntity.getCategory());
						// categoryField.setValue(null);
						categoryField.setValue(messageEntity.getCategory());
						// metadataFields.add(categoryField);
					} else {
						if (categoryField.getValue() != null) {
							categoryField.setValue(null);
							// metadataFields.add(categoryField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) categoryField);
					metadataFields.add(categoryField);
				}

//todo: check in each batch if metadata is present in table
				/*
				 * if(StringUtils.isNotBlank(messageEntity.getCondition())) { Integer
				 * conditionId =
				 * nickProdMessagingService.getId(messageEntity.getCondition().toUpperCase(),
				 * "NICK_PROD_LKP_CONDITION", "ID", "VALUE"); TeamsIdentifier condition = new
				 * TeamsIdentifier("CUSTOM.NICK PROD CONDITION"); MetadataField conditionField =
				 * new MetadataField(condition);
				 * if(REMOVE_VALUE.equals(messageEntity.getCondition())) {
				 * conditionField.setValue(null); } else { conditionField.setValue(new
				 * BigDecimal(conditionId)); } metadataFields.add(conditionField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getConditionNotes())) {
				 * TeamsIdentifier conditionNotes = new
				 * TeamsIdentifier("CUSTOM.NICK PROD CONDITION NOTES"); MetadataField
				 * conditionNotesField = new MetadataField(conditionNotes);
				 * if(REMOVE_VALUE.equals(messageEntity.getConditionNotes())) {
				 * conditionNotesField.setValue(null); } else {
				 * conditionNotesField.setValue(messageEntity.getConditionNotes()); }
				 * metadataFields.add(conditionNotesField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getAppraisedAt())) { TeamsIdentifier
				 * appraisedAt = new TeamsIdentifier("CUSTOM.NICK PROD APPRAISED AT");
				 * MetadataField appraisedAtField = new MetadataField(appraisedAt);
				 * if(REMOVE_VALUE.equals(messageEntity.getAppraisedAt())) {
				 * appraisedAtField.setValue(null); } else {
				 * appraisedAtField.setValue(formatter.parse(messageEntity.getAppraisedAt())); }
				 * metadataFields.add(appraisedAtField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getAppraisedBy())) { TeamsIdentifier
				 * appraisedBy = new TeamsIdentifier("CUSTOM.NICK PROD APPRAISED BY");
				 * MetadataField appraisedByField = new MetadataField(appraisedBy);
				 * if(REMOVE_VALUE.equals(messageEntity.getAppraisedBy())) {
				 * appraisedByField.setValue(null); } else {
				 * appraisedByField.setValue(messageEntity.getAppraisedBy()); }
				 * metadataFields.add(appraisedByField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getAppraisedValue())) {
				 * TeamsIdentifier appraisedVal = new
				 * TeamsIdentifier("CUSTOM.NICK PROD APPRAISED VALUE"); MetadataField
				 * appraisedValField = new MetadataField(appraisedVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getAppraisedValue())) {
				 * appraisedValField.setValue(null); } else { appraisedValField.setValue(new
				 * BigDecimal(messageEntity.getAppraisedValue())); }
				 * metadataFields.add(appraisedValField); }
				 */

				if (StringUtils.isNotBlank(messageEntity.getArtists())) {

					MetadataField artistValField = new MetadataField(
							new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD ARTISTS"));

					if (!REMOVE_VALUE.equals(messageEntity.getArtists())) {
						log.info("Artists {}", messageEntity.getArtists());
						artistValField.setValue(messageEntity.getArtists());
						// metadataFields.add(artistValField);
					} else {
						if (artistValField.getValue() != null) {
							artistValField.setValue(null);
							// metadataFields.add(artistValField);
						}

					}
					this.mc.addMetadataElement((MetadataElement) artistValField);
					metadataFields.add(artistValField);
				}
				
				
				log.info("Asset name>>>>>>>>> {}",messageEntity.getAssetName());

				if (StringUtils.isNotBlank(messageEntity.getAssetName())) {
					
					log.info("AssetName {}", messageEntity.getAssetName());
					TeamsIdentifier assetNameVal = new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD ASSET NAME");
					MetadataField assetNameValField = new MetadataField(assetNameVal);
					if (!REMOVE_VALUE.equals(messageEntity.getAssetName())) {
						log.info("AssetName Inside else");
						assetNameValField.setValue(messageEntity.getAssetName());
					} else {
				
						log.info("AssetName Inside if");

						assetNameValField.setValue(null);
					}
					
					log.info("Before adding to collection.....!");

					this.mc.addMetadataElement((MetadataElement) assetNameValField);

					metadataFields.add(assetNameValField);
				}

				/***
				 * Not updating embedded fields
				 */

//            MetadataField assetDescField = new MetadataField(new TeamsIdentifier("ARTESIA.FIELD.ASSET DESCRIPTION"));//embedded
				MetadataField assetDescField = new MetadataField(
						new TeamsIdentifier("CUSTOM.NICK_PROD_ASSETDESCRIPTION")); // custom

				if (StringUtils.isNotBlank(messageEntity.getAssetDescription())) {
					if (!REMOVE_VALUE.equals(messageEntity.getAssetDescription())) {
						// log.info("AssetDescription {}", messageEntity.getAssetDescription());
						assetDescField.setValue(messageEntity.getAssetDescription());
						// metadataFields.add(assetDescField);
					} else {
						if (assetDescField.getValue() != null) {
							assetDescField.setValue(null);
							// metadataFields.add(assetDescField);
						}

					}
					this.mc.addMetadataElement((MetadataElement) assetDescField);

					metadataFields.add(assetDescField);
				}

				if (StringUtils.isNotBlank(messageEntity.getAssetDescription2())) {
					if (!REMOVE_VALUE.equals(messageEntity.getAssetDescription2())) {
						// log.info("AssetDescription {}", messageEntity.getAssetDescription());
						assetDescField.setValue(messageEntity.getAssetDescription2());
						// metadataFields.add(assetDescField);
					} else {
						if (assetDescField.getValue() != null) {
							assetDescField.setValue(null);
							// metadataFields.add(assetDescField);
						}					
					}
					this.mc.addMetadataElement((MetadataElement) assetDescField);

					metadataFields.add(assetDescField);
				}

				/*
				 * if(StringUtils.isNotBlank(messageEntity.getBarcode())) { TeamsIdentifier
				 * barcodeVal = new TeamsIdentifier("CUSTOM.NICK PROD BARCODE"); MetadataField
				 * barcodeValField = new MetadataField(barcodeVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getBarcode())) {
				 * barcodeValField.setValue(null); } else {
				 * barcodeValField.setValue(messageEntity.getBarcode()); }
				 * metadataFields.add(barcodeValField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getBoxID())) { TeamsIdentifier
				 * boxIdVal = new TeamsIdentifier("CUSTOM.NICK PROD BOX ID"); MetadataField
				 * boxIdValField = new MetadataField(boxIdVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getBoxID())) {
				 * boxIdValField.setValue(null); } else { boxIdValField.setValue(new
				 * BigDecimal(messageEntity.getBoxID())); } metadataFields.add(boxIdValField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getContextualNote())) {
				 * TeamsIdentifier contextualNotesVal = new
				 * TeamsIdentifier("CUSTOM.NICK PROD CONTEXTUAL NOTE"); MetadataField
				 * contextualNotesValField = new MetadataField(contextualNotesVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getContextualNote())) {
				 * contextualNotesValField.setValue(null); } else {
				 * contextualNotesValField.setValue(messageEntity.getContextualNote()); }
				 * metadataFields.add(contextualNotesValField); }
				 */
				if (StringUtils.isNotBlank(messageEntity.getCreationDate())) {
					MetadataField creationDateField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD.CREATION DATE"));

					if (!REMOVE_VALUE.equals(messageEntity.getCreationDate())) {
						// log.info("CreationDate {}", messageEntity.getCreationDate());
						creationDateField.setValue(formatter.parse(messageEntity.getCreationDate()));
						// metadataFields.add(creationDateField);
					} else {
						if (creationDateField.getValue() != null) {
							creationDateField.setValue(null);
							// metadataFields.add(creationDateField);
						}					
					}
					this.mc.addMetadataElement((MetadataElement) creationDateField);
					metadataFields.add(creationDateField);
				}

				if (StringUtils.isNotBlank(messageEntity.getEicAccess())) {
					MetadataField eicAccessField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD EIC ACCESS"));
					log.info("EicAccess {}", messageEntity.getEicAccess());

					if (!REMOVE_VALUE.equals(messageEntity.getEicAccess())) {
						String eicAccess = null;
						if (messageEntity.getEicAccess().equalsIgnoreCase("Yes")
								|| messageEntity.getEicAccess().equalsIgnoreCase("Y")) {
							eicAccess = "Y";
						} else {
							eicAccess = "N";
						}
						eicAccessField.setValue(eicAccess);
						//metadataFields.add(eicAccessField);
					} else {
						if (eicAccessField.getValue() != null) {
							eicAccessField.setValue(null);
							//metadataFields.add(eicAccessField);
						}
					}

					this.mc.addMetadataElement((MetadataElement) eicAccessField);
					metadataFields.add(eicAccessField);
				}

				if (StringUtils.isNotBlank(messageEntity.getEicComments())) {
					MetadataField eicCommentsField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD EIC COMMENTS"));

					if (!REMOVE_VALUE.equals(messageEntity.getEicComments())) {
						// log.info("EicComments {}", messageEntity.getEicComments());
						eicCommentsField.setValue(messageEntity.getEicComments());
						// metadataFields.add(eicCommentsField);
					} else {
						if (eicCommentsField.getValue() != null) {
							eicCommentsField.setValue(null);
							// metadataFields.add(eicCommentsField);
						}
					}

					this.mc.addMetadataElement((MetadataElement) eicCommentsField);
					metadataFields.add(eicCommentsField);
				}
				/*
				 * if(StringUtils.isNotBlank(messageEntity.getEicReview())) { TeamsIdentifier
				 * eicReviewVal = new TeamsIdentifier("CUSTOM.NICK PROD EIC REVIEW");
				 * MetadataField eicReviewField = new MetadataField(eicReviewVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getEicReview())) {
				 * eicReviewField.setValue(null); } else {
				 * eicReviewField.setValue(formatter.parse(messageEntity.getEicReview())); }
				 * metadataFields.add(eicReviewField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getEicStatus())) { TeamsIdentifier
				 * eicStatusVal = new TeamsIdentifier("CUSTOM.NICK PROD EIC STATUS");
				 * MetadataField eicStatusField = new MetadataField(eicStatusVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getEicStatus())) {
				 * eicStatusField.setValue(null); } else { Integer eicStatus =
				 * nickProdMessagingService.getId(messageEntity.getEicStatus().toUpperCase(),
				 * "NICK_PROD_LKP_EIC_STATUS", "id", "value"); eicStatusField.setValue(new
				 * BigDecimal(eicStatus)); } metadataFields.add(eicStatusField); }
				 */
				if (StringUtils.isNotBlank(messageEntity.getFmid())) {

					MetadataField fmidField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD FMID"));

					if (!REMOVE_VALUE.equals(messageEntity.getFmid())) {

						if (NumberUtils.isCreatable(messageEntity.getFmid())) {
							// log.info("FMID {}", messageEntity.getFmid());
							fmidField.setValue(new BigDecimal(messageEntity.getFmid()));
							// metadataFields.add(fmidField);
						} else {
							return "FM Id is not number";
						}
					} else {
						if (fmidField.getValue() != null) {
							fmidField.setValue(null);
							// metadataFields.add(fmidField);
						}
					}

					this.mc.addMetadataElement((MetadataElement) fmidField);
					metadataFields.add(fmidField);
				}

				if (StringUtils.isNotBlank(messageEntity.getFirstSeason())) {
					MetadataField firstSeasonField = new MetadataField(
							new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD SEASON"));

					if (!REMOVE_VALUE.equals(messageEntity.getFirstSeason())) {
						// log.info("FirstSeason {}", messageEntity.getFirstSeason());
						firstSeasonField.setValue(messageEntity.getFirstSeason());
						// metadataFields.add(firstSeasonField);
					} else {
						if (firstSeasonField.getValue() != null) {
							firstSeasonField.setValue(null);
							// metadataFields.add(firstSeasonField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) firstSeasonField);
					metadataFields.add(firstSeasonField);
				}

				if (StringUtils.isNotBlank(messageEntity.getFirstShow())) {

					MetadataField firstShowField = new MetadataField(
							new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD FIRST SHOW"));

					if (!REMOVE_VALUE.equals(messageEntity.getFirstShow())) {
						if (NumberUtils.isCreatable(messageEntity.getFirstShow())) {
							// log.info("FirstShow {}", messageEntity.getFirstShow());
							firstShowField.setValue(new BigDecimal(messageEntity.getFirstShow()));
							// metadataFields.add(firstShowField);
						} else {
							return "First Show is not number";
						}
					} else {
						if (firstShowField.getValue() != null) {
							firstShowField.setValue(null);
							// metadataFields.add(firstShowField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) firstShowField);
					metadataFields.add(firstShowField);
				}
				/*
				 * if(StringUtils.isNotBlank(messageEntity.getHasDuplicates())) {
				 * TeamsIdentifier hasDuplicateVal = new
				 * TeamsIdentifier("CUSTOM.NICK PROD HAS DUPLICATES"); MetadataField
				 * hasDuplicateField = new MetadataField(hasDuplicateVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getHasDuplicates())) {
				 * hasDuplicateField.setValue(null); } else {
				 * hasDuplicateField.setValue(messageEntity.getHasDuplicates()); }
				 * metadataFields.add(hasDuplicateField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getHeight())) { TeamsIdentifier
				 * heightVal = new TeamsIdentifier("CUSTOM.NICK PROD HEIGHT"); MetadataField
				 * heightField = new MetadataField(heightVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getHeight())) {
				 * heightField.setValue(null); } else { heightField.setValue(new
				 * BigDecimal(messageEntity.getHeight())); } metadataFields.add(heightField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getLength())) { TeamsIdentifier
				 * lengthVal = new TeamsIdentifier("CUSTOM.NICK PROD LENGTH"); MetadataField
				 * lengthField = new MetadataField(lengthVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getLength())) {
				 * lengthField.setValue(null); } else { lengthField.setValue(new
				 * BigDecimal(messageEntity.getLength())); } metadataFields.add(lengthField); }
				 * 
				 * 
				 */

				MetadataField ncrAccessField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD NCR ACCESS"));

				if (StringUtils.isNotBlank(messageEntity.getNcrAccess())) {
					if (!REMOVE_VALUE.equals(messageEntity.getNcrAccess())) {
						// log.info("NcrAccess {}", messageEntity.getNcrAccess());
						String NcrAccess = null;
						if (messageEntity.getNcrAccess().equalsIgnoreCase("Yes")
								|| messageEntity.getNcrAccess().equalsIgnoreCase("Y")) {
							NcrAccess = "Y";
						} else {
							NcrAccess = "N";
						}
						ncrAccessField.setValue(NcrAccess);
						// metadataFields.add(ncrAccessField);
					} else {
						if (ncrAccessField.getValue() != null) {
							ncrAccessField.setValue(null);
							// metadataFields.add(ncrAccessField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) ncrAccessField);
					metadataFields.add(ncrAccessField);
				}

				if (StringUtils.isNotBlank(messageEntity.getPhysicalArchive())) {

					MetadataField physicalAccessField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD PHYSICAL ARCHIVE"));

					if (!REMOVE_VALUE.equals(messageEntity.getPhysicalArchive())) {

						String physicalAccess = null;
						if (messageEntity.getPhysicalArchive().equalsIgnoreCase("Yes")
								|| messageEntity.getPhysicalArchive().equalsIgnoreCase("Y")) {
							physicalAccess = "Y";
						} else {
							physicalAccess = "N";
						}
						physicalAccessField.setValue(physicalAccess);
						// metadataFields.add(physicalAccessField);
					} else {
						if (physicalAccessField.getValue() != null) {
							physicalAccessField.setValue(null);
							// metadataFields.add(physicalAccessField);
						}

					}

					this.mc.addMetadataElement((MetadataElement) physicalAccessField);
					metadataFields.add(physicalAccessField);
				}

				if (StringUtils.isNotBlank(messageEntity.getPhysicalType())) {

					MetadataField physicalTypeField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD.PHYSICAL TYPE"));

					if (!REMOVE_VALUE.equals(messageEntity.getPhysicalType())) {
						if (NumberUtils.isCreatable(messageEntity.getPhysicalType())) {
							physicalTypeField.setValue(new BigDecimal(messageEntity.getPhysicalType()));
							// metadataFields.add(physicalTypeField);
						} else {
							return "Physical Type is not number";
						}
					} else {
						if (physicalTypeField.getValue() != null) {
							physicalTypeField.setValue(null);
							// metadataFields.add(physicalTypeField);
						}
					}

					this.mc.addMetadataElement((MetadataElement) physicalTypeField);
					metadataFields.add(physicalTypeField);
				}

				MetadataField prodDescField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD DESCRIPTION"));

				if (StringUtils.isNotBlank(messageEntity.getProdDescription())) {
					if (!REMOVE_VALUE.equals(messageEntity.getProdDescription())) {

						prodDescField.setValue(messageEntity.getProdDescription());
						// metadataFields.add(prodDescField);
					} else {
						if (prodDescField.getValue() != null) {
							prodDescField.setValue(null);
							// metadataFields.add(prodDescField);
						}
					}

					this.mc.addMetadataElement((MetadataElement) prodDescField);
					metadataFields.add(prodDescField);
				}

				if (StringUtils.isNotBlank(messageEntity.getProductionName())) {
					TeamsIdentifier prodNameVal = new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD PRODUCTION NAME");
					MetadataField prodNameField = new MetadataField(prodNameVal);
					// log.info("ProductionName {}", messageEntity.getProductionName());
					if (!REMOVE_VALUE.equals(messageEntity.getProductionName())) {
						prodNameField.setValue(messageEntity.getProductionName());
						conditionCol = messageEntity.getProductionName().toUpperCase();
						searchCol = "PRODUCTION_NAME";
					} else {
						prodNameField.setValue(null);
					}
					this.mc.addMetadataElement((MetadataElement) prodNameField);
					metadataFields.add(prodNameField);
				}

				if (StringUtils.isNotBlank(conditionCol)) {
					Integer production = metadataService.getId(conditionCol, "NICK_PROD_PRODUCTION_NAME", "ID",
							searchCol);
					if (production == null) {
						message = "Production name not found";
						return message;
					}
					TeamsIdentifier productionVal = new TeamsIdentifier("CUSTOM.NICK PROD PRODUCTION");
					MetadataField productionField = new MetadataField(productionVal);
					productionField.setValue(new BigDecimal(production));
					this.mc.addMetadataElement((MetadataElement) productionField);
					metadataFields.add(productionField);
				}

				if (StringUtils.isNotBlank(messageEntity.getProvenance())) {
					MetadataField provenanceField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD PROVENANCE"));

					if (!REMOVE_VALUE.equals(messageEntity.getProvenance())) {

						provenanceField.setValue(messageEntity.getProvenance());
						// metadataFields.add(provenanceField);
					} else {
						if (provenanceField.getValue() != null) {
							provenanceField.setValue(null);
							// metadataFields.add(provenanceField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) provenanceField);
					metadataFields.add(provenanceField);
				}

				if (StringUtils.isNotBlank(messageEntity.getReleaseDate())) {
					MetadataField releaseDateField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD RELEASE DATE"));

					if (!REMOVE_VALUE.equals(messageEntity.getReleaseDate())) {

						releaseDateField.setValue(formatter.parse(messageEntity.getReleaseDate()));
						// metadataFields.add(releaseDateField);
					} else {
						if (releaseDateField.getValue() != null) {
							releaseDateField.setValue(null);
							// metadataFields.add(releaseDateField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) releaseDateField);
					metadataFields.add(releaseDateField);
				}

				if (StringUtils.isNotBlank(messageEntity.getSgid())) {
					MetadataField sgidField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD SGID"));

					if (!REMOVE_VALUE.equals(messageEntity.getSgid())) {
						if (NumberUtils.isCreatable(messageEntity.getSgid())) {
							sgidField.setValue(new BigDecimal(messageEntity.getSgid()));
							// metadataFields.add(sgidField);
						} else {
							return "SGID is not a number";
						}
					} else {
						if (sgidField.getValue() != null) {
							sgidField.setValue(null);
							// metadataFields.add(sgidField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) sgidField);
					metadataFields.add(sgidField);
				}
				/*
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getStatus())) { TeamsIdentifier
				 * statusVal = new TeamsIdentifier("CUSTOM.NICK PROD STATUS"); MetadataField
				 * statusField = new MetadataField(statusVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getStatus())) {
				 * statusField.setValue(null); } else { Integer status =
				 * nickProdMessagingService.getId(messageEntity.getStatus().toUpperCase(),
				 * "NICK_PROD_LKP_STATUS", "ID", "VALUE"); statusField.setValue(new
				 * BigDecimal(status)); } metadataFields.add(statusField); }
				 * 
				 * if(StringUtils.isNotBlank(messageEntity.getTerritory())) { TeamsIdentifier
				 * territoryVal = new TeamsIdentifier("CUSTOM.NICK PROD TERRITORY");
				 * MetadataField territoryField = new MetadataField(territoryVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getTerritory())) {
				 * territoryField.setValue(null); } else { Integer territory =
				 * nickProdMessagingService.getId(messageEntity.getTerritory().toUpperCase(),
				 * "NICK_PROD_LKP_TERRITORY", "ID", "VALUE"); territoryField.setValue(new
				 * BigDecimal(territory)); } metadataFields.add(territoryField); }
				 */

				if (StringUtils.isNotBlank(messageEntity.getStage())) {
					MetadataField stageField = new MetadataField(
							new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD STAGE"));

					if (!REMOVE_VALUE.equals(messageEntity.getStage())) {

						// log.info("Stage {}", messageEntity.getStage());
						stageField.setValue(messageEntity.getStage());
						// metadataFields.add(stageField);
					} else {
						if (stageField.getValue() != null) {
							stageField.setValue(null);
							// metadataFields.add(stageField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) stageField);
					metadataFields.add(stageField);
				}

				MetadataField typeField = new MetadataField(new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD TYPE"));

				if (StringUtils.isNotBlank(messageEntity.getType())) {
					if (!REMOVE_VALUE.equals(messageEntity.getType())) {

						typeField.setValue(messageEntity.getType());
						// metadataFields.add(typeField);
					} else {
						if (typeField.getValue() != null) {
							typeField.setValue(null);
							// metadataFields.add(typeField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) typeField);
					metadataFields.add(typeField);
				}
				/*
				 * if(StringUtils.isNotBlank(messageEntity.getWidth())) { TeamsIdentifier
				 * widthVal = new TeamsIdentifier("CUSTOM.NICK PROD WIDTH"); MetadataField
				 * widthField = new MetadataField(widthVal);
				 * if(REMOVE_VALUE.equals(messageEntity.getWidth())) {
				 * widthField.setValue(null); } else { widthField.setValue(new
				 * BigDecimal(messageEntity.getWidth())); } metadataFields.add(widthField); }
				 */
				MetadataField yearBookField = new MetadataField(
						new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD YEARBOOK"));

				if (StringUtils.isNotBlank(messageEntity.getYearbook())) {
					if (!REMOVE_VALUE.equals(messageEntity.getYearbook())) {
						// log.info("Yearbook {}", messageEntity.getYearbook());
						yearBookField.setValue(messageEntity.getYearbook());
						// metadataFields.add(yearBookField);
					} else {
						if (yearBookField.getValue() != null) {
							yearBookField.setValue(null);
							// metadataFields.add(yearBookField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) yearBookField);
					metadataFields.add(yearBookField);
				}

				MetadataField locationField = new MetadataField(
						new TeamsIdentifier("CUSTOM.EMBEDDED.NICK PROD IMAGELOCATION"));

				if (StringUtils.isNotBlank(messageEntity.getLocation())) {
					if (!REMOVE_VALUE.equals(messageEntity.getLocation())) {

						// log.info("Location {}", messageEntity.getLocation());
						locationField.setValue(messageEntity.getLocation());
						// metadataFields.add(locationField);

					} else {
						if (locationField.getValue() != null) {
							locationField.setValue(null);
							// metadataFields.add(locationField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) locationField);
					metadataFields.add(locationField);
				}

				if (StringUtils.isNotBlank(messageEntity.getImageLocation2())) {
					if (!REMOVE_VALUE.equals(messageEntity.getImageLocation2())) {
						// log.info("Location {}", messageEntity.getLocation());
						locationField.setValue(messageEntity.getImageLocation2());
						//metadataFields.add(locationField);

					} else {
						if (locationField.getValue() != null) {
							locationField.setValue(null);
							//metadataFields.add(locationField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) locationField);
					metadataFields.add(locationField);
				}

				// log.info("Tabular fields check");
				Optional<Material> materialOptional = Optional.ofNullable(messageEntity.getMaterial());

				if (materialOptional.isPresent()) {
					MetadataTableField materialField = new MetadataTableField(
							new TeamsIdentifier("CUSTOM.NICK PROD MATERIAL ID"));

					// log.info("materials present");
					if (!CollectionUtils.isEmpty(messageEntity.getMaterial().getAdd())) {
						for (String addVal : messageEntity.getMaterial().getAdd()) {
							materialField.addValue(addVal);
						}
					}
					this.mc.addMetadataElement((MetadataElement) materialField);

					metadataFields.add(materialField);

				}

				Optional<PostEpisode> postEpsOptional = Optional.ofNullable(messageEntity.getPostEpisodeID());

				if (postEpsOptional.isPresent()) {
					MetadataTableField postEpisodeField = new MetadataTableField(
							new TeamsIdentifier("CUSTOM.NICK PROD POST EPISODE.POST EPISODE ID"));

					// log.info("POst Episode present");

					if (!CollectionUtils.isEmpty(messageEntity.getPostEpisodeID().getAdd())) {
						for (String addVal : messageEntity.getPostEpisodeID().getAdd()) {
							postEpisodeField.addValue(addVal);
						}
					}
					this.mc.addMetadataElement((MetadataElement) postEpisodeField);
					metadataFields.add(postEpisodeField);

				}

				Optional<ProdShow> prodShowOptional = Optional.ofNullable(messageEntity.getProdShowID());

				if (prodShowOptional.isPresent()) {
					MetadataTableField prodShowField = new MetadataTableField(
							new TeamsIdentifier("CUSTOM.NICK PROD REUSE PROD SHOW.PROD SHOW ID"));

					// log.info("PROD show present");
					if (!CollectionUtils.isEmpty(messageEntity.getProdShowID().getAdd())) {
						for (String addVal : messageEntity.getProdShowID().getAdd()) {
							prodShowField.addValue(addVal);
						}
					}
					this.mc.addMetadataElement((MetadataElement) prodShowField);

					metadataFields.add(prodShowField);

				}

				Optional<Keyword> keywordwOptional = Optional.ofNullable(messageEntity.getKeywords());

				if (keywordwOptional.isPresent()) {
					MetadataTableField keywordField = new MetadataTableField(new TeamsIdentifier("KEYWORD"));

					// log.info("Keywords present");
					if (!CollectionUtils.isEmpty(messageEntity.getKeywords().getAdd())) {
						for (String addVal : messageEntity.getKeywords().getAdd()) {
							keywordField.addValue(addVal);
						}
					}
					this.mc.addMetadataElement((MetadataElement) keywordField);

					metadataFields.add(keywordField);

				}

				if (StringUtils.isNotBlank(messageEntity.getRetiredStatus())) {

					MetadataField isRetiredField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD RETIRED ASSET"));

					if (!REMOVE_VALUE.equals(messageEntity.getRetiredStatus())) {
						// log.info("EicAccess {}", messageEntity.getEicAccess());
						String isRetiredStatus = null;
						if (messageEntity.getRetiredStatus().equalsIgnoreCase("Yes")
								|| messageEntity.getRetiredStatus().equalsIgnoreCase("Y")) {
							isRetiredStatus = "Y";
						} else {
							isRetiredStatus = "N";
						}
						isRetiredField.setValue(isRetiredStatus);
						// metadataFields.add(isRetiredField);
					} else {
						if (isRetiredField.getValue() != null) {
							isRetiredField.setValue(null);
							// metadataFields.add(isRetiredField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) isRetiredField);
					metadataFields.add(isRetiredField);

				}

				if (StringUtils.isNotBlank(messageEntity.getRigName())) {
					MetadataField isRigNameField = new MetadataField(new TeamsIdentifier("CUSTOM.NICK PROD.RIG NAME"));

					if (!REMOVE_VALUE.equals(messageEntity.getRigName())) {

						isRigNameField.setValue(messageEntity.getRigName());
						metadataFields.add(isRigNameField);
					} else {
						if (isRigNameField.getValue() != null) {
							isRigNameField.setValue(null);
							metadataFields.add(isRigNameField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) isRigNameField);
					metadataFields.add(isRigNameField);

				}

				if (StringUtils.isNotBlank(messageEntity.getFileNameCompare())) {
					MetadataField fileNameCompareField = new MetadataField(
							new TeamsIdentifier("CUSTOM.NICK PROD FILENAME COMPARE"));

					if (!REMOVE_VALUE.equals(messageEntity.getFileNameCompare())) {
						// log.info("fileNameCompare {}", messageEntity.getFileNameCompare());
						fileNameCompareField.setValue(messageEntity.getFileNameCompare());
						// metadataFields.add(fileNameCompareField);
					} else {
						if (fileNameCompareField.getValue() != null) {
							fileNameCompareField.setValue(null);
							// metadataFields.add(fileNameCompareField);
						}
					}
					this.mc.addMetadataElement((MetadataElement) fileNameCompareField);
					metadataFields.add(fileNameCompareField);

				}

				log.info("Metadata updates completed");
				metadataFields.forEach(m -> this.mc.addMetadataElement((MetadataElement) m));
				asset.setMetadata(this.mc);

				AssetMetadataServices.getInstance().saveMetadataForAssets(assetIdentifiers,
						metadataFields.<MetadataField>toArray(new MetadataField[metadataFields.size()]),
						securitySession);

				AssetServices.getInstance().unlockAsset(asset.getAssetId(), securitySession);
				// log.info("unlock asset");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Inside Catch:", e);
			try {
				message = "Unknow Error Occurred.....!";
				if (!asset.isLocked()) {

					AssetServices.getInstance().unlockAsset(asset.getAssetId(), securitySession);
				}
			} catch (BaseTeamsException e1) {
				// TODO Auto-generated catch block
				log.error("Inside catch BaseTeamsException:", e1);
			}
			e.printStackTrace();
			return message;
		} finally {

			log.info("Inside Finally:");
			try {
				if (!asset.isLocked()) {

					AssetServices.getInstance().unlockAsset(asset.getAssetId(), securitySession);
				}
			} catch (BaseTeamsException e) {
				// TODO Auto-generated catch block
				log.error("Inside finally BaseTeamsException:", e);
			}

		}
		return message;
	}

}