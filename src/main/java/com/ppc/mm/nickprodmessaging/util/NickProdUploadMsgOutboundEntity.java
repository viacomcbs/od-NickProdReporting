package com.ppc.mm.nickprodmessaging.util;

public class NickProdUploadMsgOutboundEntity {

	private String messageType;
	private Integer fmQueueId;
	private String status;
	private String otid;
	private String errorMessage;
	private String errorCode;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public Integer getFmQueueId() {
		return fmQueueId;
	}
	public void setFmQueueId(Integer fmQueueId) {
		this.fmQueueId = fmQueueId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOtid() {
		return otid;
	}
	public void setOtid(String otid) {
		this.otid = otid;
	}
	@Override
	public String toString() {
		return "NickProdUploadMsgOutboundEntity [messageType=" + messageType + ", fmQueueId=" + fmQueueId + ", status="
				+ status + ", otid=" + otid + ", errorMessage=" + errorMessage + ", errorCode=" + errorCode + "]";
	}
}
