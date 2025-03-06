/**
 * 
 */
package com.ppc.mm.nickprodmessaging.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author gujarach
 * Inbound queue staging table
 */
@Entity
@Table(name = "MM_INBOUND_QUEUE_ASSETS")
@Component
@Scope(value = "prototype")
public class MMInboundQueueAssets {

	private Integer Id;
	private String status;
	private Integer importId;
	private String assetJson;
	private String assetMessage;
	private MMInboundQueue mmInboundQueue;
	
	@Id
	@SequenceGenerator(name="mm_inbound_queue_assets_seq", sequenceName="MM_INBOUND_QUEUE_ASSETs_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="mm_inbound_queue_assets_seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 12, scale = 0)
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	
	@Column(name = "STATUS", nullable = false, length = 10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "IMPORT_ID", nullable = true, length = 12)
	public Integer getImportId() {
		return importId;
	}
	public void setImportId(Integer importId) {
		this.importId = importId;
	}
	
	@Column(name = "ASSET_JSON", nullable = false, length = 4000)
	public String getAssetJson() {
		return assetJson;
	}
	public void setAssetJson(String assetJson) {
		this.assetJson = assetJson;
	}
	
	@Column(name = "ASSET_MESSAGE", nullable = true, length = 4000)
	public String getAssetMessage() {
		return assetMessage;
	}
	public void setAssetMessage(String assetMessage) {
		this.assetMessage = assetMessage;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "INBOUND_QUEUE_ID", nullable = false)
	public MMInboundQueue getMmInboundQueue() {
		return mmInboundQueue;
	}
	public void setMmInboundQueue(MMInboundQueue mmInboundQueue) {
		this.mmInboundQueue = mmInboundQueue;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetJson == null) ? 0 : assetJson.hashCode());
		result = prime * result + ((assetMessage == null) ? 0 : assetMessage.hashCode());
		result = prime * result + ((importId == null) ? 0 : importId.hashCode());
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
		MMInboundQueueAssets other = (MMInboundQueueAssets) obj;
		if (assetJson == null) {
			if (other.assetJson != null)
				return false;
		} else if (!assetJson.equals(other.assetJson))
			return false;
		if (assetMessage == null) {
			if (other.assetMessage != null)
				return false;
		} else if (!assetMessage.equals(other.assetMessage))
			return false;
		if (importId == null) {
			if (other.importId != null)
				return false;
		} else if (!importId.equals(other.importId))
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
		return "MMInboundQueueAssets [Id=" + Id + ", status=" + status + ", importId=" + importId + ", assetJson="
				+ assetJson + ", assetMessage=" + assetMessage + "]";
	}
}
