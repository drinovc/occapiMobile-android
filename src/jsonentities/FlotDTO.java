package jsonentities;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlotDTO {
	private List<Series> seriesSet;
	private Object settings;
	
	public List<Series> getSeriesSet() {
		return seriesSet;
	}
	public void setSeriesSet(List<Series> seriesSet) {
		this.seriesSet = seriesSet;
	}
	public Object getSettings() {
		return settings;
	}
	public void setSettings(Object settings) {
		this.settings = settings;
	}
}
