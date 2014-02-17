package jsonentities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Series {
	private String kpiName;
	private String label;
	private List<double[]> data;
	
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<double[]> getData() {
		return data;
	}
	public void setData(List<double[]> data) {
		this.data = data;
	}	
}
