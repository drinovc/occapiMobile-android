package jsonentities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LineChart {
	private FlotDTO flotDTO;
	private Object alerts;
	private Object options;
	
	public FlotDTO getFlotDTO() {
		return flotDTO;
	}
	public void setFlotDTO(FlotDTO flotDTO) {
		this.flotDTO = flotDTO;
	}
	public Object getAlerts() {
		return alerts;
	}
	public void setAlerts(Object alerts) {
		this.alerts = alerts;
	}
	public Object getOptions() {
		return options;
	}
	public void setOptions(Object options) {
		this.options = options;
	}
}
