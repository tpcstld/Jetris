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
	MenuItem mMenuItem;
	public static double defaultGravity = 0.05; // The default gravity of the
												// game
	// value)
	public static int flickSensitivity = 30; // How sensitive is the flick
												// gesture
	public static int slackLength = 1000; // How long the stack goes on for in
	public static String gameMode = "";
	public static double softDropSpeed = 0.45; // How fast soft dropping is
	public static int dragSensitivity = 60;
	public static long countDownTime = 120;
	public static int textColor = Color.BLACK;
	public static int linesPerLevel = 10;
	public static double gravityAddPerLevel = 0.025;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		setTheme(Constants.getTheme(settings));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jetris_main);
		// Show the Up button in the action bar.
		setupActionBar();

		// Start a new game if the appropriate button is pressed.
		Intent intent = getIntent();
		boolean startNewGame = intent.getBooleanExtra("startNewGame", true);
		MainGame.startNewGame = startNewGame;
		if (startNewGame) {
			gameMode = intent.getStringExtra("gameMode");
			MainGame.gameMode = gameMode;
		}

		// Change the pause/unpause text
		mainView = new MainGame(this);
		if (Constants.getTheme(settings) == R.style.DarkTheme
				&& settings.getBoolean("darkBackground", true)) {
			mainView.setBackgroundColor(Color.BLACK);
		}
		setContentView(mainView);
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
		mMenuItem = menu.findItem(R.id.pause);
		updatePauseMessage();
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
		updatePauseMessage();
	}

	public void newGame(MenuItem item) {
		MainGame.newGame();
		updatePauseMessage();
	}

	protected void onPause() {
		super.onPause();
		MainGame.pauseGame(true);
		updatePauseMessage();
	}

	protected void onResume() {
		super.onResume();
	}

	public void updatePauseMessage() {
		mMenuItem.setTitle(getPauseMenuMessage(MainGame.pause));
	}

	public int getPauseMenuMessage(boolean pause) {
		if (pause) {
			return R.string.action_unpause;
		} else {
			return R.string.action_pause;
		}
	}
}
