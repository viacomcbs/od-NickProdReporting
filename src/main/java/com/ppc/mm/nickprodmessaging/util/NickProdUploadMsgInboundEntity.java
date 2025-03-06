package com.ppc.mm.nickprodmessaging.util;

public class NickProdUploadMsgInboundEntity {

	private String messageType;
	private String fmQueueId;
	private String filePath;
	private String otFolder;
	private String otid;
	private String category;
	private String condition;
	private String conditionNotes;
	private Material material;	//------------------------1
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
	private Keyword keywords;	//-----------------2
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
	private PostEpisode postEpisodeID;	//------------------3
	private ProdShow prodShowID;	//----------------------4
	private String assetDescription;
	private String retiredStatus;
	private String rigName;
	private Integer priority;
    private String stage;
    private String fileNameCompare;
    
	public String getRigName() {
		return rigName;
	}
	public void setRigName(String rigName) {
		this.rigName = rigName;
	}
	public String getRetiredStatus() {
		return retiredStatus;
	}
	public void setRetiredStatus(String retiredStatus) {
		this.retiredStatus = retiredStatus;
	}	
	public String getOtFolder() {
		return otFolder;
	}
	public void setOtFolder(String otFolder) {
		this.otFolder = otFolder;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public PostEpisode getPostEpisodeID() {
		return postEpisodeID;
	}
	public void setPostEpisodeID(PostEpisode postEpisodeID) {
		this.postEpisodeID = postEpisodeID;
	}
	public ProdShow getProdShowID() {
		return prodShowID;
	}
	public void setProdShowID(ProdShow prodShowID) {
		this.prodShowID = prodShowID;
	}
	public String getAppraisedAt() {
		return appraisedAt;
	}
	public void setAppraisedAt(String appraisedAt) {
		this.appraisedAt = appraisedAt;
	}
	public String getAppraisedBy() {
		return appraisedBy;
	}
	public void setAppraisedBy(String appraisedBy) {
		this.appraisedBy = appraisedBy;
	}
	public String getAppraisedValue() {
		return appraisedValue;
	}
	public void setAppraisedValue(String appraisedValue) {
		this.appraisedValue = appraisedValue;
	}
	public String getArtists() {
		return artists;
	}
	public void setArtists(String artists) {
		this.artists = artists;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getBoxID() {
		return boxID;
	}
	public void setBoxID(String boxID) {
		this.boxID = boxID;
	}
	public String getContextualNote() {
		return contextualNote;
	}
	public void setContextualNote(String contextualNote) {
		this.contextualNote = contextualNote;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getDateImported() {
		return dateImported;
	}
	public void setDateImported(String dateImported) {
		this.dateImported = dateImported;
	}
	public String getEicAccess() {
		return eicAccess;
	}
	public void setEicAccess(String eicAccess) {
		this.eicAccess = eicAccess;
	}
	public String getEicComments() {
		return eicComments;
	}
	public void setEicComments(String eicComments) {
		this.eicComments = eicComments;
	}
	public String getEicReview() {
		return eicReview;
	}
	public void setEicReview(String eicReview) {
		this.eicReview = eicReview;
	}
	public String getEicStatus() {
		return eicStatus;
	}
	public void setEicStatus(String eicStatus) {
		this.eicStatus = eicStatus;
	}
	public String getFmid() {
		return fmid;
	}
	public void setFmid(String fmid) {
		this.fmid = fmid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFirstSeason() {
		return firstSeason;
	}
	public void setFirstSeason(String firstSeason) {
		this.firstSeason = firstSeason;
	}
	public String getFirstShow() {
		return firstShow;
	}
	public void setFirstShow(String firstShow) {
		this.firstShow = firstShow;
	}
	public String getHasDuplicates() {
		return hasDuplicates;
	}
	public void setHasDuplicates(String hasDuplicates) {
		this.hasDuplicates = hasDuplicates;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getImportedBy() {
		return importedBy;
	}
	public void setImportedBy(String importedBy) {
		this.importedBy = importedBy;
	}
	public Keyword getKeywords() {
		return keywords;
	}
	public void setKeywords(Keyword keywords) {
		this.keywords = keywords;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getNcrAccess() {
		return ncrAccess;
	}
	public void setNcrAccess(String ncrAccess) {
		this.ncrAccess = ncrAccess;
	}
	public String getPhysicalArchive() {
		return physicalArchive;
	}
	public void setPhysicalArchive(String physicalArchive) {
		this.physicalArchive = physicalArchive;
	}
	public String getPhysicalType() {
		return physicalType;
	}
	public void setPhysicalType(String physicalType) {
		this.physicalType = physicalType;
	}
	public String getProdDescription() {
		return prodDescription;
	}
	public void setProdDescription(String prodDescription) {
		this.prodDescription = prodDescription;
	}
	public String getProdID() {
		return prodID;
	}
	public void setProdID(String prodID) {
		this.prodID = prodID;
	}
	public String getProduction() {
		return production;
	}
	public void setProduction(String production) {
		this.production = production;
	}
	public String getProductionName() {
		return productionName;
	}
	public void setProductionName(String productionName) {
		this.productionName = productionName;
	}
	public String getProvenance() {
		return provenance;
	}
	public void setProvenance(String provenance) {
		this.provenance = provenance;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getSgid() {
		return sgid;
	}
	public void setSgid(String sgid) {
		this.sgid = sgid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTerritory() {
		return territory;
	}
	public void setTerritory(String territory) {
		this.territory = territory;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getYearbook() {
		return yearbook;
	}
	public void setYearbook(String yearbook) {
		this.yearbook = yearbook;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getFmQueueId() {
		return fmQueueId;
	}
	public void setFmQueueId(String fmQueueId) {
		this.fmQueueId = fmQueueId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getOtid() {
		return otid;
	}
	public void setOtid(String otid) {
		this.otid = otid;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getConditionNotes() {
		return conditionNotes;
	}
	public void setConditionNotes(String conditionNotes) {
		this.conditionNotes = conditionNotes;
	}
	public Material getMaterial() {
		return material;
	}
	public void setMaterial(Material material) {
		this.material = material;
	}
	public String getAssetDescription() {
		return assetDescription;
	}
	public void setAssetDescription(String assetDescription) {
		this.assetDescription = assetDescription;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public String getFileNameCompare() {
	    return this.fileNameCompare;
	}
	  
	public void setFileNameCompare(String fileNameCompare) {
	    this.fileNameCompare = fileNameCompare;
	}
	  
	@Override
	public String toString() {
		return "NickProdUploadMsgInboundEntity [messageType=" + messageType + ", fmQueueId=" + fmQueueId + ", filePath="
				+ filePath + ", otFolder=" + otFolder + ", otid=" + otid + ", category=" + category + ", condition="
				+ condition + ", conditionNotes=" + conditionNotes + ", material=" + material + ", appraisedAt="
				+ appraisedAt + ", appraisedBy=" + appraisedBy + ", appraisedValue=" + appraisedValue + ", artists="
				+ artists + ", assetName=" + assetName + ", barcode=" + barcode + ", boxID=" + boxID
				+ ", contextualNote=" + contextualNote + ", creationDate=" + creationDate + ", dateImported="
				+ dateImported + ", eicAccess=" + eicAccess + ", eicComments=" + eicComments + ", eicReview="
				+ eicReview + ", eicStatus=" + eicStatus + ", fmid=" + fmid + ", filename=" + filename
				+ ", firstSeason=" + firstSeason + ", firstShow=" + firstShow + ", hasDuplicates=" + hasDuplicates
				+ ", height=" + height + ", importedBy=" + importedBy + ", keywords=" + keywords + ", length=" + length
				+ ", ncrAccess=" + ncrAccess + ", physicalArchive=" + physicalArchive + ", physicalType=" + physicalType
				+ ", prodDescription=" + prodDescription + ", prodID=" + prodID + ", production=" + production
				+ ", productionName=" + productionName + ", provenance=" + provenance + ", releaseDate=" + releaseDate
				+ ", sgid=" + sgid + ", status=" + status + ", territory=" + territory + ", type=" + type + ", width="
				+ width + ", yearbook=" + yearbook + ", location=" + location + ", postEpisodeID=" + postEpisodeID
				+ ", prodShowID=" + prodShowID + ", assetDescription=" + assetDescription + ", retiredStatus="
				+ retiredStatus + ", rigName=" + rigName + ", priority=" + priority + ", stage=" + stage + "]";
	}
	
	
}
