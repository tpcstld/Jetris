package com.tpcstld.jetris;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Settings extends Activity {

	@SuppressLint("NewApi")
	public void openDefaultGravityDialog(View view) {
		DialogFragment fragment = new GameOptionsDialog();
		Bundle args = new Bundle();
		args.putString("option", "defaultGravity");
		args.putInt("message", R.string.default_grav_message);
		args.putInt("title", R.string.default_grav_title);
		fragment.setArguments(args);
		fragment.show(getFragmentManager(), " ");
	}
	
	@SuppressLint("NewApi")
	public void openFPSDialog(View view) {
		DialogFragment fragment = new GameOptionsDialog();
		Bundle args = new Bundle();
		args.putString("option", "FPS");
		args.putInt("message", R.string.FPS_message);
		args.putInt("title", R.string.FPS_title);
		fragment.setArguments(args);
		fragment.show(getFragmentManager(), " ");
	}
	
	@SuppressLint("NewApi")
	public void openFlickSensitivity(View view) {
		DialogFragment fragment = new GameOptionsDialog();
		Bundle args = new Bundle();
		args.putString("option", "flickSensitivity");
		args.putInt("message", R.string.flick_sensitivity_message);
		args.putInt("title", R.string.flick_sensitivity_title);
		fragment.setArguments(args);
		fragment.show(getFragmentManager(), " ");
	}
	@SuppressLint("NewApi")
	public void openSlackLength(View view) {
		DialogFragment fragment = new GameOptionsDialog();
		Bundle args = new Bundle();
		args.putString("option", "slackLength");
		args.putInt("message", R.string.slack_length_message);
		args.putInt("title", R.string.slack_length_title);
		fragment.setArguments(args);
		fragment.show(getFragmentManager(), " ");
	}
	
	@SuppressLint("NewApi")
	public void openSoftDropMultiplier(View view) {
		DialogFragment fragment = new GameOptionsDialog();
		Bundle args = new Bundle();
		args.putString("option", "softDropMultiplier");
		args.putInt("message", R.string.soft_drop_multiplier_message);
		args.putInt("title", R.string.soft_drop_multiplier_title);
		fragment.setArguments(args);
		fragment.show(getFragmentManager(), " ");
	}
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// Show the Up button in the action bar.
		setupActionBar();
		
		
		/*getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new GameOptionsFragment())
				.commit();*/
		
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
		getMenuInflater().inflate(R.menu.settings, menu);
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
