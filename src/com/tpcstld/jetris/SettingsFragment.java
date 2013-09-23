package com.tpcstld.jetris;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.tpcstld.jetris.R;

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
		}
	}
}
