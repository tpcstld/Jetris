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

	public final static String singleText = "Single";
	public final static String doubleText = "Double";
	public final static String tripleText = "Triple";
	public final static String tetrisText = "Tetris";
	public final static String twistText = "Twist";
	public final static String kickText = "Kicked";
	public final static String backToBackText = "Back to Back";

	public final static int[][] kickX = { { 0, -1, -1, 0, -1 },
			{ 0, 1, 1, 0, 1 }, { 0, 1, 1, 0, 1 }, { 0, -1, -1, 0, -1 } };
	public final static int[][] kickY = { { 0, 0, 1, -2, -2 },
			{ 0, 0, -1, 2, 2 }, { 0, 0, 1, -2, -2 }, { 0, 0, -1, 2, 2 } };

	public final static int[][] iBlockkickX = { { 0, -2, 1, -2, 1 },
			{ 0, -1, 2, -1, 2 }, { 0, 2, -1, 2, -1 }, { 0, 1, -2, 1, -2 } };
	public final static int[][] iBlockkickY = { { 0, 0, 0, -1, 2 },
			{ 0, 0, 0, 2, -1 }, { 0, 0, 0, 1, -2 }, { 0, 0, 0, -2, 1 } };
	public final static String[] settingName = { "defaultGravity",
			"softDropSpeed", "slackLength", "flickSensitivity",
			"dragSensitivity", "countDownTime", "linesPerLevel",
			"gravityAddPerLevel" };
	public final static String[] defaultValue = { "0.033", "0.45", "1000",
			"30", "60", "120", "10", "0.025" };
	public final static String[] booleanSettingName = { "tapToHold" };
	public final static boolean[] booleanSettingDefault = { true };

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
