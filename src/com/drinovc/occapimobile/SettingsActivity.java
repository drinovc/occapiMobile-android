package com.drinovc.occapimobile;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {
    
	public static final String KEY_EMAIL = "email";
	public static final String KEY_PASS = "pass";
			
	public static final String KEY_PREF_REMEMBER_ME = "remember_me";
	public static final String KEY_PREF_AUTO_LOGIN = "auto_login";
	public static final String KEY_PREF_AUTO_REFRESH_ALERTS = "auto_refresh_alerts";
	public static final String KEY_PREF_REFRESH_ALERTS_TIMEOUT = "refresh_alerts_timeout";
	public static final String KEY_PREF_AUTO_REFRESH_CHARTS = "auto_refresh_charts";
	public static final String KEY_PREF_REFRESH_CHARTS_TIMEOUT = "refresh_charts_timeout";
	public static final String KEY_PREF_API_URL = "api_url";
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        
        // defaults
        EditTextPreference editTextPref = (EditTextPreference) findPreference(KEY_PREF_REFRESH_ALERTS_TIMEOUT);
        editTextPref.setSummary(sp.getString(KEY_PREF_REFRESH_ALERTS_TIMEOUT, "10"));
        
        editTextPref = (EditTextPreference) findPreference(KEY_PREF_REFRESH_CHARTS_TIMEOUT);
        editTextPref.setSummary(sp.getString(KEY_PREF_REFRESH_CHARTS_TIMEOUT, "5"));
        
        editTextPref = (EditTextPreference) findPreference(KEY_PREF_API_URL);
        editTextPref.setSummary(sp.getString(KEY_PREF_API_URL, "http://212.235.191.163:8080/inteliui/resources/"));
    }
    
    @Override
    public boolean onMenuItemSelected(int id, MenuItem item) {
		int itemId = item.getItemId();
		switch(itemId) {
		case android.R.id.home:
			finish();
			break;
		}		
		return true;
	}
    
    @SuppressWarnings("deprecation")
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    	Preference pref = findPreference(key);
        if (pref instanceof EditTextPreference) {
            EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
        }
    }
    
    @SuppressWarnings("deprecation")
	@Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
