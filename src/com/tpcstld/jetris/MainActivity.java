package com.tpcstld.jetris;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		intent.putExtra("gameMode", "Marathon");
		startActivity(intent);
	}
	
	public void newTimeAttackGame(View view) {
		
		Intent intent = new Intent(this, StartGameActivity.class);
		intent.putExtra("startNewGame", true);
		intent.putExtra("gameMode", "Time Attack");
		startActivity(intent);
	}
	
	public void loadGame(View view) {
		Intent intent = new Intent(this, StartGameActivity.class);
		intent.putExtra("startNewGame", false);
		startActivity(intent);
	}
	
	// Opens the Settings activity when the settings button is pressed
	public void openSettings(View view) {
		Intent intent = new Intent(this, Settings.class);
		startActivity(intent);
	}
	
	public void openInstructions(View view) {
		Intent intent = new Intent(this, Instructions.class);
		startActivity(intent);
	}
}
