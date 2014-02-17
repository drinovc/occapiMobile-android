package adapters;

import java.util.List;
import jsonentities.Kpi;

import com.drinovc.occapimobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterKpisList extends BaseAdapter {

	private Activity context;
	private List<Kpi> kpis;
	
	public AdapterKpisList(Activity context, List<Kpi> kpis) {
		super();
		this.context = context;
		this.kpis = kpis;
	}
	
	@Override
    public int getCount() {
        try {
            int size = kpis.size();
            return size;
        } catch(NullPointerException ex) {
            return 0;
        }
    }
	
    @Override
    public Kpi getItem(int i) {
        return kpis.get(i);
    }
    
    @Override
	public long getItemId(int position) {
		return 0; // TODO
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.kpi_list_item, null, true);
		Kpi kpi = kpis.get(position);
		
		// set list item caption
		TextView caption = (TextView) rowView.findViewById(R.id.kpiCaption);
		caption.setText(kpi.getKpiCaption());
		
		// set list item image
		ImageView image = (ImageView) rowView.findViewById(R.id.kpiImage);
		if (kpi.getMonitorName().equals("alert_on_list")) {
			image.setImageResource(R.drawable.alert_on_list);
		}
		else if (kpi.getMonitorName().equals("line_chart")) {
			image.setImageResource(R.drawable.line_chart);
		}		
				
		return rowView;
	}
	
	public void updateKPIs(List<Kpi> kpis) {
		this.kpis.clear();
		this.kpis.addAll(kpis);
		this.notifyDataSetChanged();
	}	
}
