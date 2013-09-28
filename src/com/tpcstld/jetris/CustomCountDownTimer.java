package com.tpcstld.jetris;

import android.os.CountDownTimer;

public class CustomCountDownTimer extends CountDownTimer {

	public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	@Override
	public void onFinish() {
		MainGame.win = true;
		MainGame.auxText = "Time Left: " + 0 + ":"
				+ String.format("%02d", 0);
		MainGame.updateHighScore();
	}

	@Override
	public void onTick(long timeLeft) {
		MainGame.currentCountDownTime = timeLeft;
		int minutes = (int) timeLeft / 60000;
		int seconds = (int) timeLeft % 60000 / 1000;
		MainGame.auxText = "Time Left: " + minutes + ":"
				+ String.format("%02d", seconds);
	}

}
