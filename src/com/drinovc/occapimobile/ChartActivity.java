package com.drinovc.occapimobile;

import helpers.App;
import helpers.C;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import jsonentities.Kpi;
import jsonentities.LineChart;
import jsonentities.Series;
import jsonentities.User;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChartActivity extends Activity {

	private GraphicalView mChartView;
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	
	private String mKpiName;
	private int mRefreshTimeout = 5000;
	
	private GetChartAsync getChartAsync;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		 
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(((App)getApplicationContext()).getKpi().getKpiCaption());
		
		// styling chart
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.WHITE);
		mRenderer.setAxisTitleTextSize(dpToPx(15));
		mRenderer.setLabelsTextSize(dpToPx(14));
		mRenderer.setLabelsColor(Color.rgb(152, 153, 152));
		mRenderer.setMarginsColor(Color.WHITE);
		mRenderer.setMargins(new int[] { dpToPx(0), dpToPx(30), dpToPx(30), dpToPx(0) } ); // top, left, bottom, right
		mRenderer.setGridColor(Color.rgb(166, 150, 150));
		mRenderer.setShowGridX(true);
		mRenderer.setYLabelsAlign(Align.CENTER);
		mRenderer.setYLabelsVerticalPadding(dpToPx(2));
		mRenderer.setPointSize(dpToPx(4));
		mRenderer.setLegendHeight(dpToPx(40));
		mRenderer.setLegendTextSize(dpToPx(15));
		mRenderer.setYLabels(10);
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		mRefreshTimeout = Integer.parseInt(sharedPref.getString(SettingsActivity.KEY_PREF_REFRESH_CHARTS_TIMEOUT, "5")) * 1000;
		
		mKpiName = ((App)getApplicationContext()).getKpi().getKpiName();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chart, menu);
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
		loadChart();
	}
	
	@Override
	public void onResume() {
		super.onResume();		
		if(mChartView == null) {
			//mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
			mChartView = ChartFactory.getTimeChartView(this, mDataset, mRenderer, "HH:mm:ss");
			mChartView.setZoomRate(0.9f);
			LinearLayout chart = (LinearLayout)findViewById(R.id.lineChart);
			chart.addView(mChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		else {
			mChartView.repaint();
		}
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
	
	public int dpToPx(float dp) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
		return (int)px;
	}
	
	public void loadChart() {
		Kpi kpi = ((App)getApplicationContext()).getKpi();
		User user = ((App)getApplicationContext()).getUser();
		
		if(user != null && kpi != null && kpi.getKpiName().equals(mKpiName)) {
			if(getChartAsync != null) {
				System.out.println("Chart alrealy loading");
			}
			else {
				System.out.println("load chart");
				getChartAsync = new GetChartAsync();
				getChartAsync.execute(kpi.getKpiName(), kpi.getMonitorName(), user.getToken());
			}
			
			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				 @ Override
				public void run() {
					loadChart(); 
				}
			}, mRefreshTimeout);
		}
		else {
//			Toast.makeText(getApplicationContext(), "Refreshing chart " + mKpiName + " stopped.", Toast.LENGTH_SHORT).show();
		}
	}
	
	public class GetChartAsync extends AsyncTask<String, String, LineChart> {
		
		@Override
		protected LineChart doInBackground(String... params) {
			String kpiName = params[0];
			String monitor = params[1];
			String token = params[2];
			
			try {
				URL url = new URL(C.apiURL + "chart/kpi/" + kpiName + "/monitor/" + monitor + "/token/" + token);
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(new InputStreamReader(url.openStream()), LineChart.class);
			} 
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		protected void onPostExecute(final LineChart result) {
			if (result != null && result.getFlotDTO() != null && result.getFlotDTO().getSeriesSet() != null) {	
				
				if(result.getFlotDTO().getSeriesSet().size() > 0) {
					List<Series> seriesSet = result.getFlotDTO().getSeriesSet();
					int[] colors = new int[] { Color.rgb(54,68,82), Color.rgb(248,153,29), Color.rgb(52,147,244)};
					
					long minX = Long.MAX_VALUE;
					long maxX = Long.MIN_VALUE;
					double minY = Double.MAX_VALUE;
					double maxY = Double.MIN_VALUE;
					
					mDataset.clear();
					mRenderer.removeAllRenderers();
					
					for(int s = 0; s < seriesSet.size(); s++) {
						Series series = seriesSet.get(s);
						
						//XYSeries xySeries = new XYSeries(series.getLabel());
						TimeSeries xySeries = new TimeSeries(series.getLabel());
						mDataset.addSeries(xySeries);
						XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
						
						seriesRenderer.setLineWidth(dpToPx(1));
						seriesRenderer.setPointStyle(PointStyle.CIRCLE);
						seriesRenderer.setPointStrokeWidth(dpToPx(1));
						seriesRenderer.setFillPoints(false);
						seriesRenderer.setColor(colors[s]);
						
						mRenderer.addSeriesRenderer(seriesRenderer);
						
						for(int p = 0; p < series.getData().size(); p++) {
							double[] point = series.getData().get(p);
							long timestamp = (long)(point[0]);
							Date time = new Date(timestamp);
							double y = point[1];
							
							minX = timestamp < minX ? timestamp : minX;
							maxX = timestamp > maxX ? timestamp : maxX;
							minY = y < minY ? y : minY;
							maxY = y > maxY ? y : maxY;
							
							//xySeries.add(point[0], point[1]);
							xySeries.add(time, y);
						}
					}
					long dX = maxX - minX;
					double dY = maxY - minY;
					double margin = 0.1f;
					
					mRenderer.setXAxisMin(minX - dX * margin);
					mRenderer.setXAxisMax(maxX + dX * margin);
					mRenderer.setYAxisMin(minY - dY * margin);
					mRenderer.setYAxisMax(maxY + dY * margin);
					mChartView.repaint();
					
//					System.out.println("xMin: " + mRenderer.getXAxisMin());
//					System.out.println("xMax: " + mRenderer.getXAxisMax());
				}
				else {
					Toast.makeText(getApplicationContext(), "No data to plot", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(getApplicationContext(), "Result empty", Toast.LENGTH_SHORT).show();
			}
			getChartAsync = null;
			System.out.println("chart loaded");
		}

		@Override
		protected void onCancelled() {
			getChartAsync = null;
		}
	}

}
