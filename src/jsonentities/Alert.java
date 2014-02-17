package jsonentities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Alert {
	
	private long timestamp;
	private String what;
	private String reason;
	private String level;
	private String kpiName;
	private float longiture;
	private float latitude;
	private String fingerprint;
	private String markerType;
	private float radius;
	private String color;
	private String kpiCaption;
	private String levelCaption;
	
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public float getLongiture() {
		return longiture;
	}
	public void setLongiture(float longiture) {
		this.longiture = longiture;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public String getFingerprint() {
		return fingerprint;
	}
	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}
	public String getMarkerType() {
		return markerType;
	}
	public void setMarkerType(String markerType) {
		this.markerType = markerType;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getKpiCaption() {
		return kpiCaption;
	}
	public void setKpiCaption(String kpiCaption) {
		this.kpiCaption = kpiCaption;
	}
	public String getLevelCaption() {
		return levelCaption;
	}
	public void setLevelCaption(String levelCaption) {
		this.levelCaption = levelCaption;
	}
}