package helpers;

import java.util.ArrayList;

import jsonentities.Kpi;
import jsonentities.KpiGroup;
import jsonentities.User;
import android.app.Application;

public class App extends Application {
	private String email;
	private User user;
	private ArrayList<KpiGroup> kpiGroups;
	private KpiGroup kpiGroup;
	private Kpi kpi;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public User getUser() {
		return user;
	}
	public ArrayList<KpiGroup> getKpiGroups() {
		return kpiGroups;
	}
	public void setKpiGroups(ArrayList<KpiGroup> kpiGroups) {
		this.kpiGroups = kpiGroups;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public KpiGroup getKpiGroup() {
		return kpiGroup;
	}
	public void setKpiGroup(KpiGroup kpiGroup) {
		this.kpiGroup = kpiGroup;
	}
	public Kpi getKpi() {
		return kpi;
	}
	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}
}
