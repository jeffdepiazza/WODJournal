package com.floridaseabee.wodjournal;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.List;

public class Preferences_Edit extends PreferenceActivity {
	@Override
	public void onBuildHeaders(List<Header> target) {
		Log.v("Preference Activity", "loading headers");
		loadHeadersFromResource(R.xml.preference_headers, target);
	}

	@Override
	protected boolean isValidFragment(String fragmentName) {
        return First.class.getName().equals(fragmentName) || Second.class.getName().equals(fragmentName);
    }

	public static class First extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference1);
		}
	}

	public static class Second extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preference2);
		}
	}
}
