package com.floridaseabee.wodjournal;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Utilities_Second_Display_Fragment extends Fragment  {
	private static final String TYPE = "Type_To_Display";
	private int type_to_display;
	private Utilities_Send_Task_Listener listener;
	private Boolean sdOkToWrite = false;
	private String DATASUBDIRECTORY;

	public static Utilities_Second_Display_Fragment newInstance(int type_to_display) {

		Utilities_Second_Display_Fragment f = new Utilities_Second_Display_Fragment();
		Bundle args = new Bundle();
		args.putInt(TYPE, type_to_display);
		f.setArguments(args);

		return f;

	}

	public void setListener(Utilities_Send_Task_Listener listener) {
		this.listener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setRetainInstance(true);
		type_to_display = getArguments().getInt(TYPE, 0);
		String[] title;
		String[] summary;

		if (type_to_display == 0) {
			// display backup text
			title = new String[] { getResources().getString(R.string.utilities_backup_title1), getResources().getString(R.string.utilities_backup_title2),
					getResources().getString(R.string.utilities_backup_title3) };
			summary = new String[] { getResources().getString(R.string.utilities_backup_summary1),
					getResources().getString(R.string.utilities_backup_summary2), getResources().getString(R.string.utilities_backup_summary3) };

		} else {
			title = new String[] { getResources().getString(R.string.utilities_restore_title1), getResources().getString(R.string.utilities_restore_title2) };
			summary = new String[] { getResources().getString(R.string.utilities_restore_summary1),
					getResources().getString(R.string.utilities_restore_summary2) };
		}
		View result = inflater.inflate(R.layout.utilities_main, container, false);
		ListView first_lv = (ListView) result.findViewById(R.id.utilities_listview);
		Utilities_List_Adapter adapter = new Utilities_List_Adapter(getActivity(), title, summary);
		first_lv.setAdapter(adapter);
		first_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (type_to_display) {

				case 0:
					// backup
					if (position == 0) {
						// start to backup to external
						Log.v("second fragment", "displaying dialog");
						String sdTest = Environment.getExternalStorageState();
						if (sdTest.equals(Environment.MEDIA_MOUNTED)) {
							Log.v("Database helper for .xml dump", "Media is mounted");
							sdOkToWrite = true;
							// for now lets get the external storage directory and append WODJOURNAL
							DATASUBDIRECTORY = Environment.getExternalStorageDirectory() + "/WODJournal/";
							// work on adding the dialog now
							Log.v("second display fragment", "backup to filename heading back to utilities activity");
							listener.sent_task(0, 0, DATASUBDIRECTORY);

						} else {
							//media is not good to write, so kick out and tell user.
							sdOkToWrite = false;
						}
						
					} else if (position == 1) {
						// start to backup to .xml
					
						Log.v("second fragment", "displaying dialog");
						String sdTest = Environment.getExternalStorageState();
						if (sdTest.equals(Environment.MEDIA_MOUNTED)) {
							Log.v("Database helper for .xml dump", "Media is mounted");
							sdOkToWrite = true;
							// for now lets get the external storage directory and append WODJOURNAL
							DATASUBDIRECTORY = Environment.getExternalStorageDirectory() + "/WODJournal/";
							// work on adding the dialog now
							listener.sent_task(0,1, DATASUBDIRECTORY);

						} else {
							//media is not good to write, so kick out and tell user.
							sdOkToWrite = false;
						}

					} else {
						// start to backup to .xls
						listener.sent_task(0, 2, "");
					}

					break;

				case 1:
					// restore
					if (position == 0) {
						// restore from external
						listener.sent_task(1, 0, "");
					} else
						// restore from .xml
						listener.sent_task(1, 1, "");
					break;
				}

			}
		});

		
		return result;
	}

	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.v("Utilties Second Fragment", " on Save instance state");
		
	}
	
}
