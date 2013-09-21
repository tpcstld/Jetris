package com.tpcstld.jetris;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.EditText;

@SuppressLint("NewApi")
public class GameOptionsDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		//Initialize variables
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final String currentOption = getArguments().getString("option");
		String defaultValue = "";
		final EditText input = new EditText(getActivity());

		//Get the default value, just in case
		Resources resource = getActivity().getResources();
		final AssetManager assetManager = resource.getAssets();
		final Properties configFile = new Properties();

		try {
			InputStream is = assetManager.open("config.properties");
			configFile.load(is);
			defaultValue = configFile.getProperty(currentOption);
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Missing config.properties");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Get the settings preference object
		final SharedPreferences settings = getActivity().getSharedPreferences(
				"settings", 0);

		//Get the current value of the variable
		String value = settings.getString(currentOption, defaultValue);

		//Make the dialog
		builder.setView(input)
				//Set the message of the dialog
				.setMessage(R.string.default_grav_message)
				//Set what the OK button does
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//Update the settings
								SharedPreferences.Editor editor = settings
										.edit();
								editor.putString(currentOption, input.getText()
										.toString());
								editor.commit();
							}
						})
				//Set what the cancel button does
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						}).setTitle(R.string.default_grav_title);
		// Create the AlertDialog object and return it
		input.setText(value);
		input.setSelection(value.length());
		return builder.create();
	}

}
