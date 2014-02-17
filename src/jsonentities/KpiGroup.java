package jsonentities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KpiGroup {
	private String kpiGroupName;
	private String kpiGroupCaption;
	
	public String getKpiGroupName() {
		return kpiGroupName;
	}
	public void setKpiGroupName(String kpiGroupName) {
		this.kpiGroupName = kpiGroupName;
	}
	public String getKpiGroupCaption() {
		return kpiGroupCaption;
	}
	public void setKpiGroupCaption(String kpiGroupCaption) {
		this.kpiGroupCaption = kpiGroupCaption;
	}
}
