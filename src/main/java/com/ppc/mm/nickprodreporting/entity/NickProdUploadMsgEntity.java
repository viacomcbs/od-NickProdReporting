package com.ppc.mm.nickprodreporting.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table(name = "NICKPRODUPLOADMSGINBOUNDENTITY")
@Component
@Scope(value = "prototype")
public class NickProdUploadMsgEntity {

    private Integer id;
    private Integer queueId;
    private String messageType;
    private Integer fmQueueId;
    private String filePath;
    private String otFolder;
    private String otid;
    private String category;
    private String condition;
    private String conditionNotes;
    private String material;	//------------------------1
    private String appraisedAt;
    private String appraisedBy;
    private String appraisedValue;
    private String artists;
    private String assetName;
    private String barcode;
    private String boxID;
    private String contextualNote;
    private String creationDate;
    private String dateImported;
    private String eicAccess;
    private String eicComments;
    private String eicReview;
    private String eicStatus;
    private String fmid;
    private String filename;
    private String firstSeason;
    private String firstShow;
    private String hasDuplicates;
    private String height;
    private String importedBy;
    private String keywords;	//-----------------2
    private String length;
    private String ncrAccess;
    private String physicalArchive;
    private String physicalType;
    private String prodDescription;
    private String prodID;
    private String production;
    private String productionName;
    private String provenance;
    private String releaseDate;
    private String sgid;
    private String status;
    private String territory;
    private String type;
    private String width;
    private String yearbook;
    private String location;
    private String postEpisodeID;	//------------------3
    private String prodShowID;	//----------------------4
    private String assetDescription;
    private String retiredStatus;
    private String rigName;
    private Integer priority;
    private String stage;
    private String fileNameCompare;
    private String forValidation;

    @Id
    @SequenceGenerator(name="NP_INBOUND_ENTITY_SEQ", sequenceName="NP_INBOUND_ENTITY_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="NP_INBOUND_ENTITY_SEQ")
    @Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    @Column(name = "MESSAGETYPE")
    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    @Column(name = "FMQUEUEID")
    public Integer getFmQueueId() {
        return fmQueueId;
    }

    public void setFmQueueId(Integer fmQueueId) {
        this.fmQueueId = fmQueueId;
    }

    @Column(name = "FILEPATH")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Column(name = "OTFOLDER")
    public String getOtFolder() {
        return otFolder;
    }

    public void setOtFolder(String otFolder) {
        this.otFolder = otFolder;
    }

    @Column(name = "OTID")
    public String getOtid() {
        return otid;
    }

    public void setOtid(String otid) {
        this.otid = otid;
    }

    @Column(name = "CATEGORY")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "CONDITION")
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Column(name = "CONDITIONNOTES")
    public String getConditionNotes() {
        return conditionNotes;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }

    @Column(name = "MATERIAL")
    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Column(name = "APPRAISEDAT")
    public String getAppraisedAt() {
        return appraisedAt;
    }

    public void setAppraisedAt(String appraisedAt) {
        this.appraisedAt = appraisedAt;
    }

    @Column(name = "APPRAISEDBY")
    public String getAppraisedBy() {
        return appraisedBy;
    }

    public void setAppraisedBy(String appraisedBy) {
        this.appraisedBy = appraisedBy;
    }

    @Column(name = "APPRAISEDVALUE")
    public String getAppraisedValue() {
        return appraisedValue;
    }

    public void setAppraisedValue(String appraisedValue) {
        this.appraisedValue = appraisedValue;
    }

    @Column(name = "ARTISTS")
    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    @Column(name = "ASSETNAME")
    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    @Column(name = "BARCODE")
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Column(name = "BOXID")
    public String getBoxID() {
        return boxID;
    }

    public void setBoxID(String boxID) {
        this.boxID = boxID;
    }

    @Column(name = "CONTEXTUALNOTE")
    public String getContextualNote() {
        return contextualNote;
    }

    public void setContextualNote(String contextualNote) {
        this.contextualNote = contextualNote;
    }

    @Column(name = "CREATIONDATE")
    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    @Column(name = "DATEIMPORTED")
    public String getDateImported() {
        return dateImported;
    }

    public void setDateImported(String dateImported) {
        this.dateImported = dateImported;
    }

    @Column(name = "EICACCESS")
    public String getEicAccess() {
        return eicAccess;
    }

    public void setEicAccess(String eicAccess) {
        this.eicAccess = eicAccess;
    }

    @Column(name = "EICCOMMENTS")
    public String getEicComments() {
        return eicComments;
    }

    public void setEicComments(String eicComments) {
        this.eicComments = eicComments;
    }

    @Column(name = "EICREVIEW")
    public String getEicReview() {
        return eicReview;
    }

    public void setEicReview(String eicReview) {
        this.eicReview = eicReview;
    }

    @Column(name = "EICSTATUS")
    public String getEicStatus() {
        return eicStatus;
    }

    public void setEicStatus(String eicStatus) {
        this.eicStatus = eicStatus;
    }

    @Column(name = "FMID")
    public String getFmid() {
        return fmid;
    }

    public void setFmid(String fmid) {
        this.fmid = fmid;
    }

    @Column(name = "FILENAME")
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Column(name = "FIRSTSEASON")
    public String getFirstSeason() {
        return firstSeason;
    }

    public void setFirstSeason(String firstSeason) {
        this.firstSeason = firstSeason;
    }

    @Column(name = "FIRSTSHOW")
    public String getFirstShow() {
        return firstShow;
    }

    public void setFirstShow(String firstShow) {
        this.firstShow = firstShow;
    }

    @Column(name = "HASDUPLICATES")
    public String getHasDuplicates() {
        return hasDuplicates;
    }

    public void setHasDuplicates(String hasDuplicates) {
        this.hasDuplicates = hasDuplicates;
    }

    @Column(name = "HEIGHT")
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Column(name = "IMPORTEDBY")
    public String getImportedBy() {
        return importedBy;
    }

    public void setImportedBy(String importedBy) {
        this.importedBy = importedBy;
    }

    @Column(name = "KEYWORDS")
    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Column(name = "LENGTH")
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Column(name = "NCRACCESS")
    public String getNcrAccess() {
        return ncrAccess;
    }

    public void setNcrAccess(String ncrAccess) {
        this.ncrAccess = ncrAccess;
    }

    @Column(name = "PHYSICALARCHIVE")
    public String getPhysicalArchive() {
        return physicalArchive;
    }

    public void setPhysicalArchive(String physicalArchive) {
        this.physicalArchive = physicalArchive;
    }

    @Column(name = "PHYSICALTYPE")
    public String getPhysicalType() {
        return physicalType;
    }

    public void setPhysicalType(String physicalType) {
        this.physicalType = physicalType;
    }

    @Column(name = "PRODDESCRIPTION")
    public String getProdDescription() {
        return prodDescription;
    }

    public void setProdDescription(String prodDescription) {
        this.prodDescription = prodDescription;
    }

    @Column(name = "PRODID")
    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    @Column(name = "PRODUCTION")
    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    @Column(name = "PRODUCTIONNAME")
    public String getProductionName() {
        return productionName;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }


    @Column(name = "PROVENANCE")
    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    @Column(name = "RELEASEDATE")
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column(name = "SGID")
    public String getSgid() {
        return sgid;
    }

    public void setSgid(String sgid) {
        this.sgid = sgid;
    }

    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "TERRITORY")
    public String getTerritory() {
        return territory;
    }

    public void setTerritory(String territory) {
        this.territory = territory;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "WIDTH")
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Column(name = "YEARBOOK")
    public String getYearbook() {
        return yearbook;
    }

    public void setYearbook(String yearbook) {
        this.yearbook = yearbook;
    }


    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "POSTEPISODEID")
    public String getPostEpisodeID() {
        return postEpisodeID;
    }

    public void setPostEpisodeID(String postEpisodeID) {
        this.postEpisodeID = postEpisodeID;
    }

    @Column(name = "PRODSHOWID")
    public String getProdShowID() {
        return prodShowID;
    }

    public void setProdShowID(String prodShowID) {
        this.prodShowID = prodShowID;
    }

    @Column(name = "ASSETDESCRIPTION")
    public String getAssetDescription() {
        return assetDescription;
    }

    public void setAssetDescription(String assetDescription) {
        this.assetDescription = assetDescription;
    }

    @Column(name = "RETIREDSTATUS")
    public String getRetiredStatus() {
        return retiredStatus;
    }

    public void setRetiredStatus(String retiredStatus) {
        this.retiredStatus = retiredStatus;
    }

    @Column(name = "RIGNAME")
    public String getRigName() {
        return rigName;
    }

    public void setRigName(String rigName) {
        this.rigName = rigName;
    }

    @Column(name = "PRIORITY")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "STAGE")
    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    @Column(name = "FILENAMECOMPARE")
    public String getFileNameCompare() {
        return fileNameCompare;
    }

    public void setFileNameCompare(String fileNameCompare) {
        this.fileNameCompare = fileNameCompare;
    }

    @Column(name = "QUEUEID")
    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    @Column(name = "FOR_VALIDATION")
    public String getForValidation() {
        return forValidation;
    }

    public void setForValidation(String forValidation) {
        this.forValidation = forValidation;
    }
}
