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

		String[] key = { "defaultGravity", "softDropSpeed", "slackLength",
				"flickSensitivity", "dragSensitivity", "countDownTime",
				"linesPerLevel", "gravityAddPerLevel" };
		String[] def = { "0.05", "0.45", "1000", "30", "60", "120", "10",
				"0.025" };

		setSummaries(key, def, sharedprefs);
	}

	public void setSummaries(String[] key, String[] def,
			SharedPreferences sharedprefs) {
		for (int xx = 0; xx < key.length; xx++) {
			Preference pref = findPreference(key[xx]);
			pref.setSummary(sharedprefs.getString(key[xx], def[xx]));
		}
	}
}
