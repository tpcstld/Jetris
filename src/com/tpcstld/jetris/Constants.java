package com.tpcstld.jetris;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;

public class Constants {

	public final static String CURRENT_VERSION = "1.0.4";
	public final static String LIGHT_THEME = "Light";
	public final static String DARK_THEME = "Dark";
	public final static String MARATHON_MODE = "Marathon";
	public final static String TIME_ATTACK_MODE = "Time Attack";
	public final static String MARATHON_SCORE = "Marathon Score";
	public final static String TIME_ATTACK_SCORE = "Time Attack Score";
	public final static String CORNER_TSPIN = "3-corner T";
	public final static String IMMOBILE = "Immobile";
	public final static String GEOMETRIC_DROP_MODE = "Exponential";
	public final static String LINEAR_DROP_MODE = "Linear";
	public final static String CONFIRM_SETTINGS_MESSAGE = "Are you sure you wish to reset all non-misc settings?";
	public final static String CONFIRM_HIGHSCORE_MESSAGE = "Are you sure you wish to reset your highscores?";

	public final static String singleText = "Single";
	public final static String doubleText = "Double";
	public final static String tripleText = "Triple";
	public final static String tetrisText = "Tetris";
	public final static String twistText = "Twist";
	public final static String kickText = "Kicked";
	public final static String backToBackText = "Back to Back";

	public final static String TSPIN_MODE_DEFAULT = CORNER_TSPIN;
	public final static String DROP_MODE_DEFAULT = GEOMETRIC_DROP_MODE;
	
	public final static int SLACK_LENGTH_DEFAULT = 1000;
	public final static int FLICK_SENSITIVITY_DEFAULT = 30;
	public final static int DRAG_SENSITIVITY_DEFAULT = 60;
	public final static int COUNTDOWN_TIME_DEFAULT = 120;
	public final static int LINES_PER_LEVEL_DEFAULT = 10;
	public final static int STARTING_LEVEL_DEFAULT = 1;

	public final static double DEFAULT_GRAVITY_DEFAULT = 0.033;
	public final static double SOFT_DROP_SPEED_DEFAULT = 0.45;
	public final static double GRAVITY_ADD_PER_LEVEL_DEFAULT = 0.025;
	public final static double GRAVITY_MULTIPLY_PER_LEVEL_DEFAULT = 1.38;

	public final static boolean TAP_TO_HOLD_DEFAULT = true;
	
	public final static double doubleCap = 20.0;

	public final static int[][] kickX = { { 0, -1, -1, 0, -1 },
			{ 0, 1, 1, 0, 1 }, { 0, 1, 1, 0, 1 }, { 0, -1, -1, 0, -1 } };
	public final static int[][] kickY = { { 0, 0, 1, -2, -2 },
			{ 0, 0, -1, 2, 2 }, { 0, 0, 1, -2, -2 }, { 0, 0, -1, 2, 2 } };

	public final static int[][] iBlockkickX = { { 0, -2, 1, -2, 1 },
			{ 0, -1, 2, -1, 2 }, { 0, 2, -1, 2, -1 }, { 0, 1, -2, 1, -2 } };
	public final static int[][] iBlockkickY = { { 0, 0, 0, -1, 2 },
			{ 0, 0, 0, 2, -1 }, { 0, 0, 0, 1, -2 }, { 0, 0, 0, -2, 1 } };
	public static Map <String, String> defaultSettings = createSettings();
	public static Map <String, Boolean> defaultBooleanSettings = createBooleanSettings();
	
	private static Map <String, String> createSettings() {
		Map<String, String> answer = new HashMap<String, String>();
		answer.put("tSpinMode", TSPIN_MODE_DEFAULT);
		answer.put("defaultGravity", "" + DEFAULT_GRAVITY_DEFAULT);
		answer.put("softDropSpeed", "" + SOFT_DROP_SPEED_DEFAULT);
		answer.put("slackLength", "" + SLACK_LENGTH_DEFAULT);
		answer.put("flickSensitivity", "" + FLICK_SENSITIVITY_DEFAULT);
		answer.put("dragSensitivity", "" + DRAG_SENSITIVITY_DEFAULT);
		answer.put("countDownTime", "" + COUNTDOWN_TIME_DEFAULT);
		answer.put("linesPerLevel", "" + LINES_PER_LEVEL_DEFAULT);
		answer.put("dropSpeedMode", DROP_MODE_DEFAULT);
		answer.put("gravityAddPerLevel", "" + GRAVITY_ADD_PER_LEVEL_DEFAULT);
		answer.put("gravityMultiplyPerLevel", "" + GRAVITY_MULTIPLY_PER_LEVEL_DEFAULT);
		answer.put("startingLevel", "" + STARTING_LEVEL_DEFAULT);
		
		return answer;
	}
	
	private static Map <String, Boolean> createBooleanSettings() {
		Map<String, Boolean> answer = new HashMap<String, Boolean>();
		answer.put("tapToHold", TAP_TO_HOLD_DEFAULT);
		return answer;
	}

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

/* Implementing a new setting:
 * Make a preference
 * Add to default value and settingName
 * Implement
 * Changing a setting's default value:
 * Remember to change both the value in Constants and the value in preferences!
 * */
