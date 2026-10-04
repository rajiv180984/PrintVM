package com.first.test.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/*@Entity
@Table(name = "MachinePaperInkCountforRefile")
@NamedQueries({
		@NamedQuery(name = "notifyPaperCareTaker", query = "SELECT machineId FROM MachinePaperInkCountforRefile where papercount>=refilPaperThresold"),
		@NamedQuery(name = "notifyInkCareTaker", query = "SELECT machineId FROM MachinePaperInkCountforRefile WHERE bpid =:bpid"),
		@NamedQuery(name = "machinePaperCount", query = "SELECT * FROM MachinePaperIntCountforRefile WHERE machineId =:machineId")})
		
		
*/

@Document(collection = "MachinePaperIntCountforRefile")
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect
public class MachinePaperInkCountforRefile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4285107343810780800L;
	
	private String machineId;
	
	private int papercount;
	private  int papercountForInk;
	private  int refilPaperThresold;
	private  int refilIntThresold;

	private long lastRefilePaperTimestamp;
	
	private long lastRefileInkTimestamp;
	
	private long lastnotificationTime;
	
	public String getMachineId() {
		return machineId;
	}

	public  void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public  int getPapercount() {
		return papercount;
	}

	public  void setPapercount(int papercount) {
		this.papercount = papercount;
	}

	public  int getPapercountForInk() {
		return papercountForInk;
	}

	public  void setPapercountForInk(int papercountForInk) {
		this.papercountForInk = papercountForInk;
	}

	public  int getRefilPaperThresold() {
		return refilPaperThresold;
	}

	public  void setRefilPaperThresold(int refilPaperThresold) {
		this.refilPaperThresold = refilPaperThresold;
	}

	public  int getRefilInkThresold() {
		return refilIntThresold;
	}

	public  void setRefilIntThresold(int refilIntThresold) {
		this.refilIntThresold = refilIntThresold;
	}

	
	public long getLastRefilePaperTimestamp() {
		return lastRefilePaperTimestamp;
	}

	public void setLastRefilePaperTimestamp(long lastRefilePaperTimestamp) {
		this.lastRefilePaperTimestamp = lastRefilePaperTimestamp;
	}

	public long getLastRefileInkTimestamp() {
		return lastRefileInkTimestamp;
	}

	public void setLastRefileInkTimestamp(long lastRefileInkTimestamp) {
		this.lastRefileInkTimestamp = lastRefileInkTimestamp;
	}

	public long getLastnotificationTime() {
		return lastnotificationTime;
	}

	public void setLastnotificationTime(long lastnotificationTime) {
		this.lastnotificationTime = lastnotificationTime;
	}

	@Override
	public String toString() {
		return "MachinePaperInkCountforRefile [serialVersionUID=" + serialVersionUID + ", machineId=" + machineId
				+ ", papercount=" + papercount + ", papercountForInk=" + papercountForInk + ", refilPaperThresold="
				+ refilPaperThresold + ", refilIntThresold=" + refilIntThresold + ", lastRefilePaperTimestamp="
				 + "]";
	}

	
}
