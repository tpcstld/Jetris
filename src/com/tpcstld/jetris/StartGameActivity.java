package com.tpcstld.jetris;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.tpcstld.jetris.R;

public class StartGameActivity extends Activity {

	MainGame mainView;
	public static double defaultGravity = 0.05; // The default gravity of the game
	public static int FPS = 1000 / 30; // Frames per second of the game (1000/given
								// value)
	public static int flickSensitivity = 30; // How sensitive is the flick gesture
	public static int slackLength = 1000; // How long the stack goes on for in
	public static String gameMode = "";
	public static double softDropSpeed = 9; // How fast soft dropping is
	public static int dragSensitivity = 60;
	public static long countDownTime = 120;
	public static int textColor = Color.BLACK;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		setTheme(Constants.getTheme(settings));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jetris_main);
		// Show the Up button in the action bar.
		setupActionBar();

		// Set up and display the main game screen

		// Get the settings
		
		try {
			defaultGravity = Double.parseDouble(settings.getString(
				"defaultGravity", String.valueOf(defaultGravity)));
		} catch (Exception e) {
			System.err.println("Error getting defaultGravity. Reverting to default value.");
		}
		try {
			FPS = 1000 / (int) Double.parseDouble(settings.getString("FPS",
				String.valueOf(FPS)));
		} catch (Exception e) {
			System.err.println("Error getting FPS. Reverting to default value.");
		}
		try {
			flickSensitivity = (int) Double.parseDouble(settings.getString("flickSensitivity",
				String.valueOf(flickSensitivity)));
		} catch (Exception e) {
			System.err.println("Error getting flickSenstivity. Reverting to default value.");
		}
		try {
			slackLength = (int) Double.parseDouble(settings.getString("slackLength", String.valueOf(slackLength)));
		} catch (Exception e) {
			System.err.println("Error getting slackLength. Reverting to default value.");
		}
		try {
			softDropSpeed = Double.parseDouble(settings.getString("softDropSpeed", String.valueOf(softDropSpeed)));
		} catch (Exception e) {
			System.err.println("Error getting softDropSpeed. Reverting to default value.");
		}
		try {
			dragSensitivity = Integer.parseInt(settings.getString("dragSensitivity", String.valueOf(dragSensitivity)));
		} catch (Exception e) {
			System.err.println("Error getting dragSensitivity. Reverting to default value.");
		}
		try {
			countDownTime = Long.parseLong(settings.getString("countDownTime", String.valueOf(countDownTime)));
		} catch (Exception e) {
			System.err.println("Error getting countDownTime. Reverting to default value.");
		}
		int theme = Constants.getTheme(settings);
		if (theme == R.style.LightTheme) {
			textColor = Color.BLACK;
		} else if (theme == R.style.DarkTheme) {
			textColor = Color.WHITE;
		}
		//Start a new game if the appropriate button is pressed.
		Intent intent = getIntent();
		boolean startNewGame = intent.getBooleanExtra("startNewGame", true);
		MainGame.startNewGame = startNewGame;
		if (startNewGame) {
			gameMode = intent.getStringExtra("gameMode");
			MainGame.gameMode = gameMode;
		}
		//Change the pause/unpause text
		mainView = new MainGame(this);
		//mainView.setBackgroundColor(Color.WHITE);
		setContentView(mainView);
	}
	
	public int getInt(int variable, String text, SharedPreferences settings) {
		try {
			dragSensitivity = Integer.parseInt(settings.getString("dragSensitivity", String.valueOf(dragSensitivity)));
		} catch (Exception e) {
			System.err.println("Error getting dragSensitivity. Reverting to default value.");
		}
		return 0;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_main, menu);
		MenuItem item = menu.findItem(R.id.pause);
		if (MainGame.pause) {
			item.setTitle(R.string.action_unpause);
		} else if (!MainGame.pause){
			item.setTitle(R.string.action_pause);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void pauseGame(MenuItem item) {
		MainGame.pauseGame(!MainGame.pause);
		if (MainGame.pause) {
			item.setTitle(R.string.action_unpause);
		} else if (!MainGame.pause){
			item.setTitle(R.string.action_pause);
		}
	}
	public void newGame(MenuItem item) {
		MainGame.newGame();
	}
	protected void onPause() {
		MainGame.pauseGame(true);
		super.onPause();
	}
}
