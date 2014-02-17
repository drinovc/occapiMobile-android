package com.drinovc.occapimobile;

import helpers.App;
import helpers.C;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import jsonentities.Alert;
import jsonentities.Alerts;
import jsonentities.Kpi;
import jsonentities.User;

import com.fasterxml.jackson.databind.ObjectMapper;

import adapters.AdapterAlertsList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AlertsActivity extends Activity {
		
	private ListView mList;
	private AdapterAlertsList mAdapter;
	private ArrayList<Alert> mAlerts = new ArrayList<Alert>();
	
	private View mAlertsView;
	private View mLoadingAlertsStatusView;
	private TextView mLoadingAlertsStatusMessageView;
	
	private String mKpiName;
	private int mRefreshTimeout = 5000;
	
	private GetAlertsAsync getAlertsAsync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alerts);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(((App)getApplicationContext()).getKpi().getKpiCaption());
		
		mAlertsView = findViewById(R.id.alertsView);
		mLoadingAlertsStatusView = findViewById(R.id.loading_alerts_status);
		mLoadingAlertsStatusMessageView = (TextView) findViewById(R.id.loading_alerts_status_message);
		
		mAdapter = new AdapterAlertsList(AlertsActivity.this, mAlerts);
		
		mList = (ListView)findViewById(R.id.alertsList);
		mList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mList.setAdapter(mAdapter);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		mRefreshTimeout = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_REFRESH_CHARTS_TIMEOUT, "5")) * 1000;
		
		mKpiName = ((App)getApplicationContext()).getKpi().getKpiName();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alerts, menu);
		return true;
	}
	
	public boolean onMenuItemSelected(int id, MenuItem item) {
		int itemId = item.getItemId();
		switch(itemId) {
		case android.R.id.home:
			finish();
			overridePendingTransition(R.anim.left_to_right, R.anim.left_to_right_exit);
			break;
		}		
		return true;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    /*
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	    }
	    */
	}
	
	@Override
	public void onStart() {
		super.onStart();
		loadAlerts(true);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		((App)getApplicationContext()).setKpi(null);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right, R.anim.left_to_right_exit);
	}
	
	/**
	 * Shows the progress UI and hides the alerts list.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoadingAlertsStatusView.setVisibility(View.VISIBLE);
			mLoadingAlertsStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoadingAlertsStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mAlertsView.setVisibility(View.VISIBLE);
			mAlertsView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mAlertsView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} 
		else {
			mLoadingAlertsStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mAlertsView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	public void loadAlerts(boolean showProgress) {
		Kpi kpi = ((App)getApplicationContext()).getKpi();
		User user = ((App)getApplicationContext()).getUser();
		
		if(user != null && kpi != null && kpi.getKpiName().equals(mKpiName)) {
				if(getAlertsAsync != null) {
				System.out.println("Alerts alrealy loading");
			}
			else {
				System.out.println("load alerts");
				showProgress(showProgress);
				getAlertsAsync = new GetAlertsAsync();
				getAlertsAsync.execute(((App)getApplicationContext()).getUser().getToken());
			}
			
				Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				 @ Override
				public void run() {
					loadAlerts(false); 
				}
			}, mRefreshTimeout);
		}
		else {
//			Toast.makeText(getApplicationContext(), "Refreshing alerts " + mKpiName + " stopped.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public class GetAlertsAsync extends AsyncTask<String, String, Alerts> {
		private String kpiName;
		private String kpiCaption;
		
		@Override
		protected Alerts doInBackground(String... params) {
			String token = params[0];
			Kpi kpi = ((App)getApplicationContext()).getKpi();
			kpiName = kpi.getKpiName();
			kpiCaption = kpi.getKpiCaption();
			
			try {
				URL url = new URL(C.apiURL + "alerts/all_alerts/token/" + token);
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(new InputStreamReader(url.openStream()), Alerts.class);
			} 
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(final Alerts result) {
			if (result != null) {	
				if(result.getAlerts() != null && result.getAlerts().size() > 0) {
					ArrayList<Alert> alerts = new ArrayList<Alert>();
					for(Alert alert : result.getAlerts()) {
						if(alert.getKpiName().equals(kpiName)) {
							alerts.add(alert);
						}
					}
					if(alerts.size() > 0) {
						mAdapter.updateAlerts(alerts);
					}
					else {
						Toast.makeText(getApplicationContext(), "No alerts for " + kpiCaption, Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "No alerts", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(getApplicationContext(), "Result empty", Toast.LENGTH_SHORT).show();
			}
			showProgress(false);
			getAlertsAsync = null;
			System.out.println("alerts loaded");
		}

		@Override
		protected void onCancelled() {
			mLoadingAlertsStatusMessageView.setText("Loading canceled");
			getAlertsAsync = null;
		}
	}

}
