package com.tpcstld.jetris;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {

	public static Preference[] prefList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedprefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		addPreferencesFromResource(R.xml.preferences);

		getPreferences(Constants.settingName);
		setSummaries(Constants.settingName, Constants.defaultValue, sharedprefs);
	}

	public void getPreferences(String[] key) {
		Preference[] temp = new Preference[key.length];
		for (int xx = 0; xx < key.length; xx++) {
			temp[xx] = findPreference(key[xx]);

		}
		prefList = temp;
	}

	public static void setSummaries(String[] key, String[] def,
			SharedPreferences sharedprefs) {
		Preference[] temp = prefList;
		for (int xx = 0; xx < key.length; xx++) {
			Preference pref = temp[xx];
			pref.setSummary(sharedprefs.getString(key[xx], def[xx]));
		}

	}
}
