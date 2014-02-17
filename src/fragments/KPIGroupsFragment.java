package fragments;

import java.util.ArrayList;

import helpers.App;
import jsonentities.KpiGroup;

import com.drinovc.occapimobile.KpisActivity;
import com.drinovc.occapimobile.R;

import adapters.AdapterKpiGroupsList;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class KPIGroupsFragment extends ListFragment{
	
	private AdapterKpiGroupsList mAdapter;
	private ArrayList<KpiGroup> mKpiGroups;
	
	public KPIGroupsFragment() {}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mKpiGroups = ((App)getActivity().getApplicationContext()).getKpiGroups();
		mAdapter = new AdapterKpiGroupsList(getActivity(), mKpiGroups);
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setListAdapter(mAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		KpiGroup kpiGroup = mKpiGroups.get(position);
		((App)getActivity().getApplicationContext()).setKpiGroup(kpiGroup);
		
		// show KPIs view
		Intent intent = new Intent(getActivity(), KpisActivity.class);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
	}
}
