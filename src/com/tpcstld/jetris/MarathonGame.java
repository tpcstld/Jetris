package com.tpcstld.jetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;

public class MarathonGame extends MainGame {

	int linesCleared = 0; // The total number of lines cleared
	int linesClearedFloor = 0;
	
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
	public int getHighScore(SharedPreferences settings) {
		return settings.getInt(Constants.MARATHON_SCORE, 0);
	}

	@Override
	public void onShapeLocked() {
		changeGravity();
	}

	public void changeGravity() {
		while (linesCleared >= linesClearedFloor + linesPerLevel) {
			level = level + 1;
			linesClearedFloor = linesClearedFloor + linesPerLevel;
			if (gravityAdd < 20) {
				gravityAdd = level * gravityAddPerLevel;
			}
		}
		auxText = "" + (level + 1);
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
		auxText = "" + (level + 1);
	}

	@Override
	public void onScore(int currentDrop) {
		linesCleared = linesCleared + currentDrop;
	}

}
