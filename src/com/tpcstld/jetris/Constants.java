package com.tpcstld.jetris;

import android.content.SharedPreferences;

public class Constants {

	public final static String LIGHT_THEME = "Light";
	public final static String DARK_THEME = "Dark";
	public final static String MARATHON_MODE = "Marathon";
	public final static String TIME_ATTACK_MODE = "Time Attack";
	
	public static int getTheme(SharedPreferences settings) {
		String theme = settings.getString("theme", Constants.LIGHT_THEME);
		if (theme.equals(Constants.LIGHT_THEME)) {
			return R.style.LightTheme;
		} else if (theme.equals(Constants.DARK_THEME)) {
			return R.style.DarkTheme;
		}
		return R.style.LightTheme;
	}
}
