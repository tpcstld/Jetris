package com.tpcstld.jetris;

import android.content.SharedPreferences;

public class Constants {

	public final static String LIGHT_THEME = "Light";
	public final static String DARK_THEME = "Dark";
	public final static String MARATHON_MODE = "Marathon";
	public final static String TIME_ATTACK_MODE = "Time Attack";
	public final static String MARATHON_SCORE = "Marathon Score";
	public final static String TIME_ATTACK_SCORE = "Time Attack Score";
	public final static String CONFIRM_SETTINGS_MESSAGE = "Are you sure you wish to reset all non-misc settings?";
	public final static String CONFIRM_HIGHSCORE_MESSAGE = "Are you sure you wish to reset your highscores?";

	public final static String[] settingName = { "defaultGravity", "softDropSpeed",
			"slackLength", "flickSensitivity", "dragSensitivity",
			"countDownTime", "linesPerLevel", "gravityAddPerLevel" };
	public final static String[] defaultValue = { "0.05", "0.45", "1000", "30", "60",
			"120", "10", "0.025" };

	public static int getTheme(SharedPreferences settings) {
		String theme = settings.getString("theme", Constants.LIGHT_THEME);
		if (theme.equals(LIGHT_THEME)) {
			return R.style.LightTheme;
		} else if (theme.equals(DARK_THEME)) {
			return R.style.DarkTheme;
		}
		return R.style.LightTheme;
	}
}
