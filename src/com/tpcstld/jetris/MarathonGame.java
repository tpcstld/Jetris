package com.tpcstld.jetris;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;

public class MarathonGame extends MainGame {

	public MarathonGame(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void extraTick() {
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

}
