package com.tpcstld.jetris;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedprefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		addPreferencesFromResource(R.xml.preferences);

		setSummaries(Constants.settingName, Constants.defaultValue, sharedprefs);
	}

	public void setSummaries(String[] key, String[] def,
			SharedPreferences sharedprefs) {
		for (int xx = 0; xx < key.length; xx++) {
			Preference pref = findPreference(key[xx]);
			pref.setSummary(sharedprefs.getString(key[xx], def[xx]));
		}
	}
}
