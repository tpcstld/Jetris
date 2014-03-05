package com.tpcstld.jetris;

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

		getPreferences(Constants.settingName, prefListName);
		getPreferences(Constants.booleanSettingName, booleanPrefListName);
		setSummariesString(Constants.settingName, Constants.defaultValue,
				sharedprefs);
		setBooleanStatus(Constants.booleanSettingName,
				Constants.booleanSettingDefault, sharedprefs);
	}

	public void getPreferences(String[] key, String addTo) {
		Preference[] temp = new Preference[key.length];
		for (int xx = 0; xx < key.length; xx++) {
			temp[xx] = findPreference(key[xx]);
		}
		if (addTo.equals(prefListName)) {
			prefList = temp;
		} else if (addTo.equals(booleanPrefListName)) {
			booleanPrefList = temp;
		}
	}

	public static void setSummariesString(String[] key, String[] def,
			SharedPreferences sharedprefs) {
		for (int xx = 0; xx < key.length; xx++) {
			try {
				Double.parseDouble(def[xx]);
				prefList[xx].setSummary(sharedprefs.getString(key[xx], def[xx]));
			} catch (Exception e) {
				((ListPreference) prefList[xx]).setValue(sharedprefs.getString(key[xx], def[xx]));
			}
		}
	}

	public static void setBooleanStatus(String[] key, boolean[] def,
			SharedPreferences sharedprefs) {
		for (int xx = 0; xx < key.length; xx++) {
			((CheckBoxPreference) booleanPrefList[xx]).setChecked(sharedprefs
					.getBoolean(key[xx], def[xx]));
		}
	}
}
