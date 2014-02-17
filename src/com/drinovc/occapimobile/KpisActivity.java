package com.drinovc.occapimobile;

import helpers.App;
import helpers.C;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import jsonentities.Kpi;

import com.fasterxml.jackson.databind.ObjectMapper;

import adapters.AdapterKpisList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class KpisActivity extends Activity {
	ListView list;
	AdapterKpisList adapter;
	ArrayList<Kpi> kpis;
	
	GetKPIsAsync getKPIsAsync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kpis);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(((App)getApplicationContext()).getKpiGroup().getKpiGroupCaption());
		
		
		kpis = new ArrayList<Kpi>();
		adapter = new AdapterKpisList(KpisActivity.this, kpis);
		list = (ListView) findViewById(R.id.kpisList);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Kpi kpi = kpis.get(position);
				((App)getApplicationContext()).setKpi(kpi);
				Intent intent = null;
				
				// show Alerts view
				if(kpi.getMonitorName().equals("alert_on_list")) {
					intent = new Intent(KpisActivity.this, AlertsActivity.class);
				}
				// show Line Chart view
				else if (kpi.getMonitorName().equals("line_chart")) {
					intent = new Intent(KpisActivity.this, ChartActivity.class);
				}
				else {
					Toast.makeText(getApplicationContext(), "Monitor " + kpi.getMonitorName() + " not supported", Toast.LENGTH_SHORT).show();
					return;
				}
				startActivity(intent);
				overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
			}
		});
		
		// load kpis
		String kpiGroupName = ((App)getApplicationContext()).getKpiGroup().getKpiGroupName();	
		String token = ((App)getApplicationContext()).getUser().getToken();
		getKPIsAsync = new GetKPIsAsync();
		getKPIsAsync.execute(kpiGroupName, token);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kpis, menu);
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
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.left_to_right, R.anim.left_to_right_exit);
	}
		
	public class GetKPIsAsync extends AsyncTask<String, String, Kpi[]> {
		
		@Override
		protected Kpi[] doInBackground(String... params) {
			String groupName = params[0];
			String token = params[1];
			
			try {
				URL url = new URL(C.apiURL + "kpi/group/" + groupName + "/user/" + token);
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(new InputStreamReader(url.openStream()), Kpi[].class);			
			} 
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(final Kpi[] result) {
			if (result != null && result.length > 0) { 
				kpis = new ArrayList<Kpi>();
				for(Kpi kpi : result) {
					kpis.add(kpi);
				}
				adapter.updateKPIs(kpis);
			}
			else {
				Toast.makeText(getApplicationContext(), "Result empty", Toast.LENGTH_SHORT).show();
			}
			getKPIsAsync = null;
		}

		@Override
		protected void onCancelled() {
			getKPIsAsync = null;
		}
		
	}
}
