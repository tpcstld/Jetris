package com.tpcstld.jetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

public class SettingsFragment extends PreferenceFragment {

	public static Preference[] prefList;
	public static Preference[] booleanPrefList;
	public static final String prefListName = "prefList";
	public static final String booleanPrefListName = "booleanPrefList";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedprefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		addPreferencesFromResource(R.xml.preferences);

		getPreferences(Constants.defaultSettings, prefListName);
		getPreferences(Constants.defaultBooleanSettings, booleanPrefListName);
		setSummariesString(Constants.defaultSettings, sharedprefs);
		setBooleanStatus(Constants.defaultBooleanSettings, sharedprefs);
	}

	public void getPreferences(Map<String, ?> map, String addTo) {
		Preference[] temp = new Preference[map.size()];
		int counter = 0;
		ArrayList<String> settingNames = new ArrayList<String>(map.keySet());
		Collections.sort(settingNames);
		for (String settingName : settingNames) {
			temp[counter] = findPreference(settingName);
			counter++;
		}
		if (addTo.equals(prefListName)) {
			prefList = temp;
		} else if (addTo.equals(booleanPrefListName)) {
			booleanPrefList = temp;
		}
	}

	public static void setSummariesString(Map<String, String> map,
			SharedPreferences sharedprefs) {
		int counter = 0;
		ArrayList<String> settingNames = new ArrayList<String>(map.keySet());
		Collections.sort(settingNames);
		for (String key : settingNames) {
			try {
				Double.parseDouble(map.get(key));
				prefList[counter].setSummary(sharedprefs.getString(key,
						map.get(key)));
			} catch (Exception e) {
				((ListPreference) prefList[counter]).setValue(sharedprefs
						.getString(key, map.get(key)));
			}
			counter++;
		}
	}

	public static void setBooleanStatus(Map<String, Boolean> map,
			SharedPreferences sharedprefs) {
		int counter = 0;
		ArrayList<String> settingNames = new ArrayList<String>(map.keySet());
		Collections.sort(settingNames);
		for (String key : settingNames) {
			((CheckBoxPreference) booleanPrefList[counter])
					.setChecked(sharedprefs.getBoolean(key, map.get(key)));
			counter++;
		}
	}
}
