package adapters;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.drinovc.occapimobile.R;

import jsonentities.Alert;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterAlertsList extends BaseAdapter {

	private Activity context;
	private List<Alert> alerts;
	
	public Comparator<Alert> comparator = new Comparator<Alert>() {
		public int compare(Alert lhs, Alert rhs) {
	    	long t1 = lhs.getTimestamp(); 
	    	long t2 = rhs.getTimestamp();
			return t1 > t2 ? -1 : (t1 == t2 ? 0 : 1);
		}
	};
		
	public AdapterAlertsList(Activity context, List<Alert> alerts) {
		super();
		this.context = context;		
		this.alerts = alerts;
		Collections.sort(this.alerts, comparator);
	}
	
	@Override
    public int getCount() {
        try {
            int size = alerts.size();
            return size;
        } catch(NullPointerException ex) {
            return 0;
        }
    }
	
    @Override
    public Alert getItem(int i) {
        return alerts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return alerts.get(i).getTimestamp();
    }
    
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.alert_list_item, null, true);
		Alert alert = alerts.get(position);
		
		// color item by level
		if(alert.getLevel().equals("LEVEL0")) {
			rowView.setBackgroundResource(R.color.alertLevelNormal);
		}
		else if(alert.getLevel().equals("LEVEL1")) {
			rowView.setBackgroundResource(R.color.alertLevelSerious);
		}
		
		// set item caption
		TextView caption = (TextView) rowView.findViewById(R.id.alertCaption);
		caption.setText(alert.getReason());
		
		Date dateTime = new Date(alert.getTimestamp());
		String dateTimeS = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(dateTime);
		
		TextView subtitle = (TextView) rowView.findViewById(R.id.alertSubtitle);
		subtitle.setText(dateTimeS + " - " + alert.getLevelCaption());
		
		return rowView;
	}
	
	public void updateAlerts(List<Alert> alerts) {
		this.alerts.clear();
		this.alerts.addAll(alerts);
		Collections.sort(this.alerts, this.comparator);
		this.notifyDataSetChanged();
	}
}


