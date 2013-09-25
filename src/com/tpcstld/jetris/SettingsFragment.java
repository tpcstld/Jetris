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
		String settings = getArguments().getString("settings");
		SharedPreferences sharedprefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		
		if (settings.equals("Speed Variables")) {
			addPreferencesFromResource(R.xml.speeds_preferences);
			
			String[] key = {"defaultGravity", "softDropSpeed"};
			String[] def = {"0.05", "0.45"};
			
			setSummaries(key, def, sharedprefs);
		} else if (settings.equals("Timer Variables")) {
			addPreferencesFromResource(R.xml.timers_preferences);
			
			String[] key = {"slackLength"};
			String[] def = {"1000"};
			
			setSummaries(key, def, sharedprefs);
		} else if (settings.equals("Sensitivity Variables")) {
			addPreferencesFromResource(R.xml.sensitivities_preferences);
			
			String[] key = {"flickSensitivity", "dragSensitivity"};
			String[] def = {"30", "60"};
			
			setSummaries(key, def, sharedprefs);
		} else if (settings.equals("Misc")) {
			addPreferencesFromResource(R.xml.misc_preferences);
		} else if (settings.equals("Time Attack Variables")) {
			addPreferencesFromResource(R.xml.time_attack_preferences);
			
			String[] key = {"countDownTime"};
			String[] def = {"120"};
			
			setSummaries(key, def, sharedprefs);
		} else if (settings.equals("Marathon Variables")) {
			addPreferencesFromResource(R.xml.marathon_preferences);
			
			String[] key = {"linesPerLevel", "gravityAddPerLevel"};
			String[] def = {"10", "0.1"};
			
			setSummaries(key, def, sharedprefs);
		}
	}

	public void setSummaries(String[] key, String[] def, SharedPreferences sharedprefs) {
		for (int xx = 0; xx < key.length; xx++) {
			Preference pref = findPreference(key[xx]);
			pref.setSummary(sharedprefs.getString(key[xx], def[xx]));
		}
	}
}
