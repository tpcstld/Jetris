package com.tpcstld.jetris;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		setTheme(Constants.getTheme(settings));
		setContentView(R.layout.activity_main);
		removeText();
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
		if (!MainGame.gameMode.equals("")) {
			Intent intent = new Intent(this, StartGameActivity.class);
			intent.putExtra("startNewGame", false);
			startActivity(intent);
		}
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
	
	protected void onResume()
	{
		super.onResume();
		Button button = (Button)findViewById(R.id.loadgame);
		if (MainGame.gameMode.equals("")) {
			button.setEnabled(false);
		} else {
			button.setEnabled(true);
		}
	}
	
	public void removeText() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String resetVersion = settings.getString("resetVersion", "0.0.0");
		if (resetVersion.equals(Constants.CURRENT_VERSION)) {
			TextView textBox = (TextView) this.findViewById(R.id.updateInfo);
			textBox.setText("");
		}
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("resetVersion", Constants.CURRENT_VERSION);
		editor.commit();
	}
}
