package jsonentities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Kpi {
	private int kpiId;
	private String kpiName;
	private String kpiParentName;
	private String kpiCaption;
	private String kpiDescription;
	private String kpiGroupName;
	private String kpiSubgroupName;
	private boolean isActive;
	private boolean assignedToUser;
	private String monitorName;
	private String kpiSettingsSer;
	
	public int getKpiId() {
		return kpiId;
	}
	public void setKpiId(int kpiId) {
		this.kpiId = kpiId;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public String getKpiParentName() {
		return kpiParentName;
	}
	public void setKpiParentName(String kpiParentName) {
		this.kpiParentName = kpiParentName;
	}
	public String getKpiCaption() {
		return kpiCaption;
	}
	public void setKpiCaption(String kpiCaption) {
		this.kpiCaption = kpiCaption;
	}
	public String getKpiDescription() {
		return kpiDescription;
	}
	public void setKpiDescription(String kpiDescription) {
		this.kpiDescription = kpiDescription;
	}
	public String getKpiGroupName() {
		return kpiGroupName;
	}
	public void setKpiGroupName(String kpiGroupName) {
		this.kpiGroupName = kpiGroupName;
	}
	public String getKpiSubgroupName() {
		return kpiSubgroupName;
	}
	public void setKpiSubgroupName(String kpiSubgroupName) {
		this.kpiSubgroupName = kpiSubgroupName;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean getAssignedToUser() {
		return assignedToUser;
	}
	public void setAssignedToUser(boolean assignedToUser) {
		this.assignedToUser = assignedToUser;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getKpiSettingsSer() {
		return kpiSettingsSer;
	}
	public void setKpiSettingsSer(String kpiSettingsSer) {
		this.kpiSettingsSer = kpiSettingsSer;
	}
}
