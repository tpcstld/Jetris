package com.tpcstld.jetris;

import com.tpcstld.jetris.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class ClassicModeActivity extends Activity {

	MainGame mainView;
	static double defaultGravity = 0.09; // The default gravity of the game
	static int FPS = 1000 / 30; // Frames per second of the game (1000/given
								// value)
	static int flickSensitivity = 30; // How sensitive is the flick gesture
	static int slackLength = 1000; // How long the stack goes on for in
	static double softDropMultipler = 9; // How fast soft dropping is

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jetris_main);
		// Show the Up button in the action bar.
		setupActionBar();

		System.out.println("");
		// Set up and display the main game screen

		// Get the settings
		SharedPreferences settings = getSharedPreferences("settings", 0);
		
		defaultGravity = Double.parseDouble(settings.getString(
				"defaultGravity", String.valueOf(defaultGravity)));
		
		FPS = 1000 / Integer.parseInt(settings.getString("FPS",
				String.valueOf(FPS)));

		flickSensitivity = Integer.parseInt(settings.getString("flickSensitivity",
				String.valueOf(flickSensitivity)));
		
		slackLength = settings.getInt("slackLength", 1000);
		softDropMultipler = settings.getFloat("softDropMultipler", (float) 9);

		mainView = new MainGame(this);
		mainView.setBackgroundColor(Color.WHITE);
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
		getMenuInflater().inflate(R.menu.jetris_main, menu);
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

}
