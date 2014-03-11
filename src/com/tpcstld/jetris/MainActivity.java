package com.tpcstld.jetris;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		setTheme(Constants.getTheme(settings));
		setContentView(R.layout.activity_main);
		mContext = this;
		achieveParityWithNewVersion();
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

	protected void onResume() {
		super.onResume();
		Button button = (Button) findViewById(R.id.loadgame);
		if (MainGame.gameMode.equals("")) {
			button.setEnabled(false);
		} else {
			button.setEnabled(true);
		}
	}

	public void achieveParityWithNewVersion() {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String resetVersion = settings.getString("resetVersion", "0.0.0");
		if (!resetVersion.equals(Constants.CURRENT_VERSION)) {
			new AlertDialog.Builder(this)
					.setTitle("For Returning Players")
					.setMessage(
							"Due to the new update, it is highly recommended that you reset your settings even if you've never changed them. \nDo you wish to reset them now?")
					.setPositiveButton(android.R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									SharedPreferences settings = PreferenceManager
											.getDefaultSharedPreferences(mContext);
									SharedPreferences.Editor editor = settings
											.edit();
									// Changing hold to tap
									for (String key : Constants.defaultBooleanSettings
											.keySet()) {
										editor.putBoolean(
												key,
												Constants.defaultBooleanSettings
														.get(key));
									}
									for (String settingName : Constants.defaultSettings
											.keySet()) {
										editor.putString(settingName,
												Constants.defaultSettings
														.get(settingName));
									}
									editor.commit();
									Toast.makeText(getApplicationContext(),
											"Settings reset", Toast.LENGTH_LONG)
											.show();
								}
							})
					.setNegativeButton(android.R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do nothing
								}
							}).show();
		}
		
		try {
			settings.getString(Constants.MARATHON_SCORE, "-1");
		} catch (Exception e) {
			String marathon = String.valueOf(settings.getInt(
					Constants.MARATHON_SCORE, 0));
			String timeAttack = String.valueOf(settings.getInt(
					Constants.TIME_ATTACK_SCORE, 0));
			SharedPreferences.Editor editor = settings.edit();
			editor.remove(Constants.MARATHON_SCORE);
			editor.remove(Constants.TIME_ATTACK_SCORE);
			editor.commit();
			editor.putString(Constants.MARATHON_SCORE, marathon);
			editor.putString(Constants.TIME_ATTACK_SCORE, timeAttack);
			editor.commit();
		}
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("resetVersion", Constants.CURRENT_VERSION);
		editor.commit();
	}
}
