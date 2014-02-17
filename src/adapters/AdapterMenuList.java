package adapters;


import java.util.ArrayList;

import com.drinovc.occapimobile.DrawerMenuItem;
import com.drinovc.occapimobile.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterMenuList extends BaseAdapter{

	private Context context;
	private ArrayList<DrawerMenuItem> menuItems;
	
	public AdapterMenuList(Context context, ArrayList<DrawerMenuItem> menuItems) {
		this.context = context;
		this.menuItems = menuItems;
	}
	
	@Override
	public int getCount() {
		return menuItems.size();
	}

	@Override
	public Object getItem(int position) {
		return menuItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}
		
		ImageView ivIcon = (ImageView) convertView.findViewById(R.id.icon);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.title);
		
		DrawerMenuItem menuItem = menuItems.get(position);
		
		ivIcon.setImageResource(menuItem.getIcon());
		tvTitle.setText(menuItem.getTitle());
		
		return convertView;
	}

}
