package com.tpcstld.jetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class NumberPickerPreference extends DialogPreference {

	String mNewValue;
	String mCurrentValue;
	String mMessage;
	String currentOption;
	int inputType;
	NumberPicker np;
	private Context mContext;

	@SuppressLint("Recycle")
	public NumberPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		int[] attrsArray = new int[] { android.R.attr.title, // 0
				android.R.attr.summary, // 1
				android.R.attr.key, // 2
				android.R.attr.inputType // 3
		};

		TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
		setTitle(ta.getString(0));
		mMessage = ta.getString(1);
		currentOption = ta.getString(2);
		inputType = ta.getInt(3, InputType.TYPE_CLASS_TEXT);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}

	protected View onCreateDialogView() {
	    LinearLayout layout = new LinearLayout(mContext);
	    layout.setOrientation(LinearLayout.VERTICAL);
	    layout.setPadding(6,6,6,6);
	    
	    TextView message = new TextView(mContext);
	    message.setText(mMessage);
	    message.setTextSize(16);
	    layout.addView(message);
	    
	    np = new NumberPicker(mContext);
		np.setMaxValue(Integer.MAX_VALUE);
		np.setMinValue(0);
		np.setWrapSelectorWheel(false);
		np.setValue(Integer.parseInt(mCurrentValue));
		layout.addView(np);
		return layout;
	}

	public void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			np.clearFocus();
			mNewValue = Integer.toString(np.getValue());
			persistString(mNewValue);
			setSummary(mNewValue);
		}
	}

	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		if (restorePersistedValue) {
			// Restore existing state
			mCurrentValue = this.getPersistedString(currentOption);
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
