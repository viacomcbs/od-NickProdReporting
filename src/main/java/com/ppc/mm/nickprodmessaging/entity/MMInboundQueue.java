/**
 * 
 */
package com.ppc.mm.nickprodmessaging.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author gujarach
 * Inbound queue staging table
 */
@Entity
@Table(name = "MM_INBOUND_QUEUE")
@Component
@Scope(value = "prototype")
public class MMInboundQueue {

	private Integer id;
	private String notificationType;
	private String actionSource;
	private Date notificationDate;
	private String request;
	private String status;
	private Integer priority;
	

	private Date processedDate;
	private Integer importJobId;
	private Integer recordCount;
	private String response;
	private List<MMInboundQueueAssets> mmInboundQueueAssets;
	private String emailAddr;
	private String processorNode;
	private String isEligible;

	
	@Id
	@SequenceGenerator(name="mm_inbound_queue_seq", sequenceName="MM_INBOUND_QUEUE_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="mm_inbound_queue_seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "NOTIFICATION_TYPE", nullable = false)
	public String getNotificationType() {
		return notificationType;
	}
	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}
	
	@Column(name = "ACTION_SOURCE", nullable = false)
	public String getActionSource() {
		return actionSource;
	}
	public void setActionSource(String actionSource) {
		this.actionSource = actionSource;
	}
	
	@Column(name = "NOTIFICATION_DATE", nullable = false)
	public Date getNotificationDate() {
		return notificationDate;
	}
	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}
	
	@Column(name = "JSON_DATA", nullable = false)
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	
	@Column(name = "STATUS", nullable = false)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "PRIORITY", nullable = false)
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	@Column(name = "PROCESSED_DATE", nullable = true)
	public Date getProcessedDate() {
		return processedDate;
	}
	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}
	
	@Column(name = "IMPORT_JOB_ID", nullable = true)
	public Integer getImportJobId() {
		return importJobId;
	}
	public void setImportJobId(Integer importJobId) {
		this.importJobId = importJobId;
	}
	
	@Column(name = "RECORD_COUNT", nullable = true)
	public Integer getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}
	
	@Column(name = "RESPONSE", nullable = true)
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "mmInboundQueue", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<MMInboundQueueAssets> getMmInboundQueueAssets() {
		return mmInboundQueueAssets;
	}
	public void setMmInboundQueueAssets(List<MMInboundQueueAssets> mmInboundQueueAssets) {
		this.mmInboundQueueAssets = mmInboundQueueAssets;
	}
	
	@Column(name = "EMAIL_ADDR", nullable = true)
	public String getEmailAddr() {
		return emailAddr;
	}
	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}
	
	@Column(name = "PROCESSOR_NODE")
	public String getProcessorNode() {
		return processorNode;
	}
	public void setProcessorNode(String processorNode) {
		this.processorNode = processorNode; 
	}
	
	@Column(name = "IS_ELIGIBLE")
	public String getIsEligible() {
		return isEligible;
	}
	public void setIsEligible(String isEligible) {
		this.isEligible = isEligible;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionSource == null) ? 0 : actionSource.hashCode());
		result = prime * result + ((emailAddr == null) ? 0 : emailAddr.hashCode());
		result = prime * result + ((importJobId == null) ? 0 : importJobId.hashCode());
		result = prime * result + ((mmInboundQueueAssets == null) ? 0 : mmInboundQueueAssets.hashCode());
		result = prime * result + ((notificationDate == null) ? 0 : notificationDate.hashCode());
		result = prime * result + ((notificationType == null) ? 0 : notificationType.hashCode());
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((processedDate == null) ? 0 : processedDate.hashCode());
		result = prime * result + ((recordCount == null) ? 0 : recordCount.hashCode());
		result = prime * result + ((request == null) ? 0 : request.hashCode());
		result = prime * result + ((response == null) ? 0 : response.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MMInboundQueue other = (MMInboundQueue) obj;
		if (actionSource == null) {
			if (other.actionSource != null)
				return false;
		} else if (!actionSource.equals(other.actionSource))
			return false;
		if (emailAddr == null) {
			if (other.emailAddr != null)
				return false;
		} else if (!emailAddr.equals(other.emailAddr))
			return false;
		if (importJobId == null) {
			if (other.importJobId != null)
				return false;
		} else if (!importJobId.equals(other.importJobId))
			return false;
		if (mmInboundQueueAssets == null) {
			if (other.mmInboundQueueAssets != null)
				return false;
		} else if (!mmInboundQueueAssets.equals(other.mmInboundQueueAssets))
			return false;
		if (notificationDate == null) {
			if (other.notificationDate != null)
				return false;
		} else if (!notificationDate.equals(other.notificationDate))
			return false;
		if (notificationType == null) {
			if (other.notificationType != null)
				return false;
		} else if (!notificationType.equals(other.notificationType))
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (processedDate == null) {
			if (other.processedDate != null)
				return false;
		} else if (!processedDate.equals(other.processedDate))
			return false;
		if (recordCount == null) {
			if (other.recordCount != null)
				return false;
		} else if (!recordCount.equals(other.recordCount))
			return false;
		if (request == null) {
			if (other.request != null)
				return false;
		} else if (!request.equals(other.request))
			return false;
		if (response == null) {
			if (other.response != null)
				return false;
		} else if (!response.equals(other.response))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "MMInboundQueue [id=" + id + ", notificationType=" + notificationType + ", actionSource=" + actionSource
				+ ", notificationDate=" + notificationDate + ", request=" + request + ", status=" + status
				+ ", priority=" + priority + ", processedDate=" + processedDate + ", importJobId=" + importJobId
				+ ", recordCount=" + recordCount + ", response=" + response + ", mmInboundQueueAssets="
				+ mmInboundQueueAssets + ", emailAddr=" + emailAddr + "]";
	}
}
