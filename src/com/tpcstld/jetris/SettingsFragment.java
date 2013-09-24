package com.tpcstld.jetris;

import android.os.Bundle;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String settings = getArguments().getString("settings");
		if (settings.equals("Speed Variables")) {
			addPreferencesFromResource(R.xml.speeds_preferences);
		} else if (settings.equals("Timer Variables")) {
			addPreferencesFromResource(R.xml.timers_preferences);
		} else if (settings.equals("Sensitivity Variables")) {
			addPreferencesFromResource(R.xml.sensitivities_preferences);
		} else if (settings.equals("Misc")) {
			addPreferencesFromResource(R.xml.misc_preferences);
		} else if (settings.equals("Time Attack Variables")) {
			addPreferencesFromResource(R.xml.time_attack_preferences);
		} else if (settings.equals("Marathon Variables")) {
			addPreferencesFromResource(R.xml.marathon_preferences);
		}
	}
}
