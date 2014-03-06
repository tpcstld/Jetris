package com.tpcstld.jetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;

public class MarathonGame extends MainGame {

	static long linesCleared = 0; // The total number of lines cleared
	static long linesClearedFloor = 0;
	static double gravityAddPerLevel = Constants.GRAVITY_ADD_PER_LEVEL_DEFAULT;
	static double gravityMultiplyPerLevel = Constants.GRAVITY_MULTIPLY_PER_LEVEL_DEFAULT;
	static int linesPerLevel = Constants.LINES_PER_LEVEL_DEFAULT;
	static String dropSpeedMode = Constants.DROP_MODE_DEFAULT;
	static int startingLevel = 0;
	
	public MarathonGame(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick() {
	}

	@Override
	public void printAuxText(Canvas canvas) {
		String tempText = auxText + " ("
				+ (linesPerLevel - linesCleared + linesClearedFloor) + ")";
		canvas.drawText("Level:", auxInfoXStarting + mainFieldShiftX,
				auxInfoYStarting + mainFieldShiftY, paint);
		changePaintSettings("info");
		canvas.drawText(tempText, numSquaresX * squareSide, auxInfoYStarting
				+ mainFieldShiftY, paint);
		changePaintSettings("normal");
	}

	@Override
	public long getHighScore(SharedPreferences settings) {
		return getLongFromSettings(highScore, Constants.MARATHON_SCORE, settings);
	}

	@Override
	public void onShapeLocked() {
		System.out.println(gravity + gravityAdd);
		changeGravity();
	}

	public void changeGravity() {
		while (linesCleared >= linesClearedFloor + linesPerLevel) {
			if (level != Integer.MAX_VALUE - 1) {
				level = level + 1;
			}
			linesClearedFloor = linesClearedFloor + linesPerLevel;
			if (gravityAdd < 20) {
				modifyGravityAdd();
			}
		}
		auxText = "" + (level + 1);
	}
	
	public void modifyGravityAdd() {
		if (dropSpeedMode.equals(Constants.GEOMETRIC_DROP_MODE)) {
			gravityAdd = defaultGravity
					* (Math.pow(gravityMultiplyPerLevel, level) - 1);
		} else if (dropSpeedMode.equals(Constants.LINEAR_DROP_MODE)) {
			gravityAdd = level * gravityAddPerLevel;
		}
		if (gravityAdd > 20) {
			gravityAdd = 20;
		}
	}

	@Override
	public void updateHighScore() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		editHighScore(settings, Constants.MARATHON_SCORE);
	}

	@Override
	public void onNewGame() {
		linesCleared = 0;
		linesClearedFloor = 0;
		level = startingLevel;
		auxText = "" + (level + 1);
		modifyGravityAdd();
	}

	@Override
	public void onScore(int currentDrop) {
		linesCleared = linesCleared + currentDrop;
	}

	@Override
	public void onGetSettings(SharedPreferences settings) {
		dropSpeedMode = settings.getString("dropSpeedMode", dropSpeedMode);
		if (dropSpeedMode.equals(Constants.GEOMETRIC_DROP_MODE)) {
			gravityMultiplyPerLevel = getDoubleFromSettings(
					gravityMultiplyPerLevel, "gravityMultiplyPerLevel",
					settings);
		} else if (dropSpeedMode.equals(Constants.LINEAR_DROP_MODE)) {
			gravityAddPerLevel = getDoubleFromSettings(gravityAddPerLevel,
					"gravityAddPerLevel", settings);
		}
		linesPerLevel = getIntFromSettings(linesPerLevel, "linesPerLevel",
				settings);
		startingLevel = getIntFromSettings(startingLevel, "startingLevel", settings) - 1;
		startingLevel = Math.max(0, startingLevel);
		if (linesPerLevel < 1) {
			linesPerLevel = 1;
		}
	}

}
