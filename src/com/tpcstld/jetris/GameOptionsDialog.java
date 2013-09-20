package com.tpcstld.jetris;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.EditText;

@SuppressLint("NewApi")
public class GameOptionsDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final String currentOption = getArguments().getString("option");
		
		Resources resource = getActivity().getResources();
		final AssetManager assetManager = resource.getAssets();
		final Properties configFile = new Properties();
		String value = "";
		try {
			InputStream is = assetManager.open("config.properties");
			configFile.load(is);
			value = configFile.getProperty(currentOption);
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("Missing config.properties");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final EditText input = new EditText(getActivity());
		
		
		builder.setView(input)
				.setMessage(R.string.default_grav_message)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								System.out.println(input.getText().toString());
								InputStream is;
								try {
									is = assetManager.open("config.properties");
									configFile.load(is);
									configFile.setProperty(currentOption, input.getText().toString());
									is.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// FIRE ZE MISSILES!
							}
						})
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
