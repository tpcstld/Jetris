package com.tpcstld.jetris;

import java.util.TimerTask;

public class Slack extends TimerTask {

	public Slack() {
	}

	@Override
	public void run() {
		MainGame.slack = false;              
	}

}
