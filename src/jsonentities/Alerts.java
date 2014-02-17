package jsonentities;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Alerts {
	private List<Alert> alerts;
	private Object flotDTO;
	private Object options;
	
	public List<Alert> getAlerts() {
		return alerts;
	}
	public void setAlerts(List<Alert> alerts) {
		this.alerts = alerts;
	}
	public Object getFlotDTO() {
		return flotDTO;
	}
	public void setFlotDTO(Object flotDTO) {
		this.flotDTO = flotDTO;
	}
	public Object getOptions() {
		return options;
	}
	public void setOptions(Object options) {
		this.options = options;
	}
}
