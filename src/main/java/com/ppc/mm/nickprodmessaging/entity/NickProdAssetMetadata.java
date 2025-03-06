package com.ppc.mm.nickprodmessaging.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.Date;

@Entity
@Table(name = "NICK_PROD_ASSET_METADATA", schema = "TOWNER")
public class NickProdAssetMetadata {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "UOI_ID")
    private String uoiId;

    @Column(name = "EIC_COMMENTS")
    private String eicComments;

    @Column(name = "EIC_REVIEW")
    private Date eicReview;

    @Column(name = "RELEASE_DATE")
    private Date releaseDate;

    @Column(name = "FILENAME")
    private String filename;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "BOXID")
    private Long boxId;

    @Column(name = "BARCODE")
    private String barcode;

    @Column(name = "CONDITION_ID")
    private Integer conditionId;

    @Column(name = "CONDITION_NOTES")
    private String conditionNotes;

    @Column(name = "MATERIALS")
    private String materials;

    @Column(name = "LENGTH")
    private Integer length;

    @Column(name = "WIDTH")
    private Integer width;

    @Column(name = "HEIGHT")
    private Integer height;

    @Column(name = "APPRAISED_BY")
    private String appraisedBy;

    @Column(name = "APPRAISED_AT")
    private Date appraisedAt;

    @Column(name = "APPRAISED_VALUE")
    private Long appraisedValue;

    @Column(name = "PROVENANCE")
    private String provenance;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CONTEXTUAL_NOTE")
    private String contextualNote;

    @Column(name = "TERRITORY_ID")
    private Integer territoryId;

    @Column(name = "EIC_STATUS")
    private Integer eicStatus;

    @Column(name = "PHYSICAL_ARCHIVE", length = 5)
    private String physicalArchive;

    @Column(name = "FM_ID")
    private Long fmId;

    @Column(name = "PROD_ID")
    private String prodId;

    @Column(name = "FIRST_SHOW")
    private Long firstShow;

    @Column(name = "SEASON")
    private String season;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "PRODUCTION_NAME")
    private String productionName;

    @Column(name = "ARTISTS")
    private String artists;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "ASSET_NAME")
    private String assetName;

    @Column(name = "PROD_NAME_PK")
    private Integer prodNamePk;

    @Column(name = "HAS_DUPLICATES")
    private String hasDuplicates;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "PHYSICAL_TYPE_ID")
    private Integer physicalTypeId;

    @Column(name = "YEARBOOK")
    private String yearbook;

    @Column(name = "IS_MIGRATED")
    private String isMigrated;

    @Column(name = "MIGRATION_ID")
    private Integer migrationId;

    @Column(name = "SGID")
    private Long sgid;

    @Column(name = "EIC_ACCESS")
    private String eicAccess;

    @Column(name = "NCR_ACCESS")
    private String ncrAccess;

    @Column(name = "PREVIOUS_EIC_STATUS")
    private Integer previousEicStatus;

    @Column(name = "PREVIOUS_EIC_COMMENTS")
    private String previousEicComments;

    @Column(name = "PREVIOUS_EIC_ACCESS")
    private String previousEicAccess;

    @Column(name = "PREVIOUS_NCR_ACCESS")
    private String previousNcrAccess;

    @Column(name = "IS_RETIRED")
    private String isRetired;

    @Column(name = "RIG_NAME")
    private String rigName;

    @Column(name = "PRODUCTION_STATUS")
    private Integer productionStatus;

    @Column(name = "PRODUCTION_COMMENT")
    private String productionComment;

    @Column(name = "STAGE")
    private String stage;

    @Column(name = "KEYWORD")
    private String keyword;

    @Column(name = "ASSET_SOURCE")
    private String assetSource;

    @Column(name = "IS_ELIGIBLE_WATERMARK")
    private String isEligibleWatermark;

    @Column(name = "PRODUCTION_THUMBNAIL")
    private String productionThumbnail;

    @Column(name = "SEASON_THUMBNAIL")
    private String seasonThumbnail;

    @Column(name = "SHOW_DESCRIPTION")
    private String showDescription;

    @Column(name = "SHOW_TITLE")
    private String showTitle;

    @Column(name = "PRESERVATION_NOTES")
    private String preservationNotes;

    @Column(name = "CL_SHARED")
    private String clShared;

    @Column(name = "PROJECT_TITLE")
    private String projectTitle;

    @Column(name = "EPISODE_TITLE")
    private String episodeTitle;

    @Column(name = "GCG_ACCESS")
    private String gcgAccess;

    @Column(name = "FILE_NAME_COMPARE")
    private String fileNameCompare;

    @Column(name = "FM_QUEUE_ID")
    private String fmQueueId;

    // Getters and Setters
    public String getUoiId() {
        return uoiId;
    }

    public void setUoiId(String uoiId) {
        this.uoiId = uoiId;
    }

    public String getEicComments() {
        return eicComments;
    }

    public void setEicComments(String eicComments) {
        this.eicComments = eicComments;
    }

    public Date getEicReview() {
        return eicReview;
    }

    public void setEicReview(Date eicReview) {
        this.eicReview = eicReview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBoxId() {
        return boxId;
    }

    public void setBoxId(Long boxId) {
        this.boxId = boxId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getConditionId() {
        return conditionId;
    }

    public void setConditionId(Integer conditionId) {
        this.conditionId = conditionId;
    }

    public String getConditionNotes() {
        return conditionNotes;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getAppraisedBy() {
        return appraisedBy;
    }

    public void setAppraisedBy(String appraisedBy) {
        this.appraisedBy = appraisedBy;
    }

    public Date getAppraisedAt() {
        return appraisedAt;
    }

    public void setAppraisedAt(Date appraisedAt) {
        this.appraisedAt = appraisedAt;
    }

    public Long getAppraisedValue() {
        return appraisedValue;
    }

    public void setAppraisedValue(Long appraisedValue) {
        this.appraisedValue = appraisedValue;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContextualNote() {
        return contextualNote;
    }

    public void setContextualNote(String contextualNote) {
        this.contextualNote = contextualNote;
    }

    public Integer getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(Integer territoryId) {
        this.territoryId = territoryId;
    }

    public Integer getEicStatus() {
        return eicStatus;
    }

    public void setEicStatus(Integer eicStatus) {
        this.eicStatus = eicStatus;
    }

    public String getPhysicalArchive() {
        return physicalArchive;
    }

    public void setPhysicalArchive(String physicalArchive) {
        this.physicalArchive = physicalArchive;
    }

    public Long getFmId() {
        return fmId;
    }

    public void setFmId(Long fmId) {
        this.fmId = fmId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public Long getFirstShow() {
        return firstShow;
    }

    public void setFirstShow(Long firstShow) {
        this.firstShow = firstShow;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductionName() {
        return productionName;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Integer getProdNamePk() {
        return prodNamePk;
    }

    public void setProdNamePk(Integer prodNamePk) {
        this.prodNamePk = prodNamePk;
    }

    public String getHasDuplicates() {
        return hasDuplicates;
    }

    public void setHasDuplicates(String hasDuplicates) {
        this.hasDuplicates = hasDuplicates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getPhysicalTypeId() {
        return physicalTypeId;
    }

    public void setPhysicalTypeId(Integer physicalTypeId) {
        this.physicalTypeId = physicalTypeId;
    }

    public String getYearbook() {
        return yearbook;
    }

    public void setYearbook(String yearbook) {
        this.yearbook = yearbook;
    }

    public String getIsMigrated() {
        return isMigrated;
    }

    public void setIsMigrated(String isMigrated) {
        this.isMigrated = isMigrated;
    }

    public Integer getMigrationId() {
        return migrationId;
    }

    public void setMigrationId(Integer migrationId) {
        this.migrationId = migrationId;
    }

    public Long getSgid() {
        return sgid;
    }

    public void setSgid(Long sgid) {
        this.sgid = sgid;
    }

    public String getEicAccess() {
        return eicAccess;
    }

    public void setEicAccess(String eicAccess) {
        this.eicAccess = eicAccess;
    }

    public String getNcrAccess() {
        return ncrAccess;
    }

    public void setNcrAccess(String ncrAccess) {
        this.ncrAccess = ncrAccess;
    }

    public Integer getPreviousEicStatus() {
        return previousEicStatus;
    }

    public void setPreviousEicStatus(Integer previousEicStatus) {
        this.previousEicStatus = previousEicStatus;
    }

    public String getPreviousEicComments() {
        return previousEicComments;
    }

    public void setPreviousEicComments(String previousEicComments) {
        this.previousEicComments = previousEicComments;
    }

    public String getPreviousEicAccess() {
        return previousEicAccess;
    }

    public void setPreviousEicAccess(String previousEicAccess) {
        this.previousEicAccess = previousEicAccess;
    }

    public String getPreviousNcrAccess() {
        return previousNcrAccess;
    }

    public void setPreviousNcrAccess(String previousNcrAccess) {
        this.previousNcrAccess = previousNcrAccess;
    }

    public String getIsRetired() {
        return isRetired;
    }

    public void setIsRetired(String isRetired) {
        this.isRetired = isRetired;
    }

    public String getRigName() {
        return rigName;
    }

    public void setRigName(String rigName) {
        this.rigName = rigName;
    }

    public Integer getProductionStatus() {
        return productionStatus;
    }

    public void setProductionStatus(Integer productionStatus) {
        this.productionStatus = productionStatus;
    }

    public String getProductionComment() {
        return productionComment;
    }

    public void setProductionComment(String productionComment) {
        this.productionComment = productionComment;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAssetSource() {
        return assetSource;
    }

    public void setAssetSource(String assetSource) {
        this.assetSource = assetSource;
    }

    public String getIsEligibleWatermark() {
        return isEligibleWatermark;
    }

    public void setIsEligibleWatermark(String isEligibleWatermark) {
        this.isEligibleWatermark = isEligibleWatermark;
    }

    public String getProductionThumbnail() {
        return productionThumbnail;
    }

    public void setProductionThumbnail(String productionThumbnail) {
        this.productionThumbnail = productionThumbnail;
    }

    public String getSeasonThumbnail() {
        return seasonThumbnail;
    }

    public void setSeasonThumbnail(String seasonThumbnail) {
        this.seasonThumbnail = seasonThumbnail;
    }

    public String getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(String showDescription) {
        this.showDescription = showDescription;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public void setShowTitle(String showTitle) {
        this.showTitle = showTitle;
    }

    public String getPreservationNotes() {
        return preservationNotes;
    }

    public void setPreservationNotes(String preservationNotes) {
        this.preservationNotes = preservationNotes;
    }

    public String getClShared() {
        return clShared;
    }

    public void setClShared(String clShared) {
        this.clShared = clShared;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }

    public String getGcgAccess() {
        return gcgAccess;
    }

    public void setGcgAccess(String gcgAccess) {
        this.gcgAccess = gcgAccess;
    }

    public String getFileNameCompare() {
        return fileNameCompare;
    }

    public void setFileNameCompare(String fileNameCompare) {
        this.fileNameCompare = fileNameCompare;
    }

    public String getFmQueueId() {
        return fmQueueId;
    }

    public void setFmQueueId(String fmQueueId) {
        this.fmQueueId = fmQueueId;
    }
}
