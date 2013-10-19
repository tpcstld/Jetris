package com.tpcstld.jetris;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class ResetHighScoresPreference extends DialogPreference {
	private Context mContext;

	CheckBox marathonCB;
	CheckBox timeAttackCB;

	@SuppressLint("Recycle")
	public ResetHighScoresPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		int[] attrsArray = new int[] { android.R.attr.title, // 0
				android.R.attr.summary, // 1
		};

		TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
		setTitle(ta.getString(0));
		setDialogMessage(Constants.CONFIRM_HIGHSCORE_MESSAGE);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}

	protected View onCreateDialogView() {
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(6, 6, 6, 50);

		marathonCB = new CheckBox(mContext);
		marathonCB.setText(Constants.MARATHON_MODE);
		
		marathonCB.setChecked(false);
		layout.addView(marathonCB);
		timeAttackCB = new CheckBox(mContext);
		timeAttackCB.setText(Constants.TIME_ATTACK_MODE);
		timeAttackCB.setChecked(false);
		layout.addView(timeAttackCB);
		return layout;
	}

	public void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			SharedPreferences.Editor editor = settings.edit();

			if (marathonCB.isChecked()) {
				editor.putInt(Constants.MARATHON_SCORE, 0);
			}
			if (timeAttackCB.isChecked()) {
				editor.putInt(Constants.TIME_ATTACK_SCORE, 0);
			}
			editor.commit();
		}
	}
}
