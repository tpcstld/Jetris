package com.tpcstld.jetris;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class CustomListPreference extends ListPreference {

	public CustomListPreference(Context context) {
		super(context);
	}
	
	public CustomListPreference(Context context, AttributeSet attrs) {
		super (context, attrs);
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
		setSummary(value);
	}
	
	public void setSummary(String summary) {
		super.setValue(summary);
		notifyChanged();
		super.setSummary(getValue());
	}
	
}
