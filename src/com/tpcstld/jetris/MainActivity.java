package com.tpcstld.jetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tpcstld.jetris.R;

public class MainActivity extends Activity {

	final String lightTheme = "light";
	final String darkTheme = "dark";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		setTheme(Constants.getTheme(settings));
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Opens the main game activity when the new game button is pressed
	public void newMarathonGame(View view) {

		Intent intent = new Intent(this, StartGameActivity.class);
		intent.putExtra("startNewGame", true);
		intent.putExtra("gameMode", Constants.MARATHON_MODE);
		startActivity(intent);
	}

	public void newTimeAttackGame(View view) {

		Intent intent = new Intent(this, StartGameActivity.class);
		intent.putExtra("startNewGame", true);
		intent.putExtra("gameMode", Constants.TIME_ATTACK_MODE);
		startActivity(intent);
	}

	public void loadGame(View view) {
		Intent intent = new Intent(this, StartGameActivity.class);
		intent.putExtra("startNewGame", false);
		startActivity(intent);
	}

	// Opens the Settings activity when the settings button is pressed
	public void openSettings(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void openInstructions(View view) {
		Intent intent = new Intent(this, InstructionsActivity.class);
		startActivity(intent);
	}
	
	public void openAbout(View view) {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
}
