package com.tpcstld.jetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.preference.PreferenceManager;

public class TimeAttackGame extends MainGame {
	
	public static long countDownTime = 120;
	static long currentCountDownTime = countDownTime * 1000000000;
	public TimeAttackGame(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onTick() {
		countDown();
	}

	@Override
	public void printAuxText(Canvas canvas) {
		// Drawing aux text box
		canvas.drawText("Time:", auxInfoXStarting + mainFieldShiftX,
				auxInfoYStarting + mainFieldShiftY, paint);
		changePaintSettings("info");
		canvas.drawText(auxText, numSquaresX * squareSide, auxInfoYStarting
				+ mainFieldShiftY, paint);
		changePaintSettings("normal");
	}

	@Override
	public int getHighScore(SharedPreferences settings) {
		return settings.getInt(Constants.TIME_ATTACK_SCORE, 0);
	}

	@Override
	public void onShapeLocked() {
	}

	@Override
	public void updateHighScore() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		editHighScore(settings, Constants.TIME_ATTACK_SCORE);
	}

	@Override
	public void onNewGame() {
		currentCountDownTime = countDownTime * 1000000000 + FPS;
		countDown();
	}
	
	public void countDown() {
		currentCountDownTime = currentCountDownTime - FPS;
		long minutes = currentCountDownTime / 60000 / 1000000;
		long seconds = (currentCountDownTime / 1000000) % 60000 / 1000;
		MainGame.auxText = minutes + ":" + String.format("%02d", seconds);
		if (currentCountDownTime < 0) {
			win = true;
			auxText = 0 + ":" + String.format("%02d", 0);
			updateHighScore();
		}
	}

	@Override
	public void onScore(int currentDrop) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onGetSettings(SharedPreferences settings) {
		countDownTime = getIntFromSettings((int) countDownTime,
				"countDownTime", settings);
	}

}
