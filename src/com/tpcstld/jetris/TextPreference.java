package com.tpcstld.jetris;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class TextPreference extends DialogPreference {
	
	String mNewValue;
	String mCurrentValue;

	public TextPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}
	
	public void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			persistString(mNewValue);
		}
	}
	
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
	    if (restorePersistedValue) {
	        // Restore existing state
	        mCurrentValue = this.getPersistedString("ERROR");
	    } else {
	        // Set default state from the XML attribute
	        mCurrentValue = (String) defaultValue;
	        persistString(mCurrentValue);
	    }
	}
	
	protected Object onGetDefaultValue(TypedArray a, int index) {
	    return a.getString(index);
	}

}
