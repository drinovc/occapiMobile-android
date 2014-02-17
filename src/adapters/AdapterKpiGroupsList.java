package adapters;

import java.util.List;
import jsonentities.KpiGroup;

import com.drinovc.occapimobile.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterKpiGroupsList extends BaseAdapter {

	private Activity context;
	private List<KpiGroup> kpiGroups;
	
	public AdapterKpiGroupsList(Activity context, List<KpiGroup> kpiGroups) {
		super();
		this.context = context;
		this.kpiGroups = kpiGroups;
	}
	
	@Override
    public int getCount() {
        try {
            int size = kpiGroups.size();
            return size;
        } catch(NullPointerException ex) {
            return 0;
        }
    }
	
    @Override
    public KpiGroup getItem(int i) {
        return kpiGroups.get(i);
    }
    
    @Override
	public long getItemId(int position) {
		return 0; // TODO
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.kpi_group_list_item, null, true);
		
		TextView caption = (TextView) rowView.findViewById(R.id.kpiGroupCaption);
		caption.setText(kpiGroups.get(position).getKpiGroupCaption());
		
		return rowView;
	}
	
	public void updateKPIs(List<KpiGroup> kpiGroups) {
		this.kpiGroups.clear();
		this.kpiGroups.addAll(kpiGroups);
		this.notifyDataSetChanged();
	}	
}
