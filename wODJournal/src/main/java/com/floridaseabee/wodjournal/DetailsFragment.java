package com.floridaseabee.wodjournal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View result = inflater.inflate(R.layout.calendar_details,
				container, false);
	Log.v("Details Fragment", "inflating calendar_details view.xml and passing to calling function");
		return(result);
	}
}
