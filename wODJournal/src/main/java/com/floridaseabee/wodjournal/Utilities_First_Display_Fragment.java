package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Utilities_First_Display_Fragment extends Fragment {

	// setting this as we are using this display fragment to take care of both
	// the backup and restore and will need to ensure we not only display the
	// right list strings to the user, but also to launch the appropriate tasks.

	private Utilities_Send_Task_Listener listener;

	public static Utilities_First_Display_Fragment newInstance() {
		Utilities_First_Display_Fragment f = new Utilities_First_Display_Fragment();
		return f;
	}

	public void setListener(Utilities_Send_Task_Listener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		String[] title;
		String[] summary;

		title = new String[] { getResources().getString(R.string.utilities_headers_title1),
				getResources().getString(R.string.utilities_headers_title2) };
		summary = new String[] { getResources().getString(R.string.utilities_headers_summary1),
				getResources().getString(R.string.utilities_headers_summary2) };

		View result = inflater.inflate(R.layout.utilities_main, container, false);

        ListView first_lv = result.findViewById(R.id.utilities_listview);
		Utilities_List_Adapter adapter = new Utilities_List_Adapter(getActivity(), title, summary);
		first_lv.setAdapter(adapter);
		first_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					listener.spawn_second_fragment(position);

			}
		});

		return result;
	}
}
