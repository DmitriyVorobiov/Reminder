package com.example.reminder;

import java.util.Calendar;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class MainActivity extends ActionBarActivity implements TimePickerDialogFragment.OnCompleteListener, DatePickerDialogFragment.OnCompleteListener,
		OnClickListener {

	protected static final String YEAR = "year";
	protected static final String MONTH = "month";
	protected static final String DAY = "day";
	protected static final String HOUR = "hour";
	protected static final String MINUTE = "minute";
	protected static final String DESCRIPTION = "description";
	protected static final String TITLE = "title";
	protected static final String PREFERENCES_NAME = "title";
	private TimePickerDialogFragment timeDialog;
	private DatePickerDialogFragment dateDialog;
	private final String DATE_DIALOG_TAG = "dateDialog";
	private final String TIME_DIALOG_TAG = "timeDialog";
	private final int DEFAULT_VALUE = 0;
	private RelativeLayout mainLayout;
	private EditText editTime;
	private EditText editTitle;
	private EditText editDescription;
	private EditText editDate;
	private Button button;
	private int year, month, day;
	private int hour, minute;
	private SharedPreferences myPreferences;
	private String description;
	private String title;

	// TODO validation, saveState, toast
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViewComponents();

		if (savedInstanceState != null) {
			initDialogs();
		}

		loadData();

		this.getWindow().addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		this.getWindow().addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		this.getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	private void loadData() {
		myPreferences = getPreferences(MODE_PRIVATE);
		year = myPreferences.getInt(YEAR, DEFAULT_VALUE);
		month = myPreferences.getInt(MONTH, DEFAULT_VALUE);
		day = myPreferences.getInt(DAY, DEFAULT_VALUE);
		hour = myPreferences.getInt(HOUR, DEFAULT_VALUE);
		minute = myPreferences.getInt(MINUTE, DEFAULT_VALUE);
		description = myPreferences.getString(DESCRIPTION, "");
		title = myPreferences.getString(TITLE, "");

		updateUI();

	}

	private void saveData() {	
		title = editTitle.getText().toString();
		description = editDescription.getText().toString();
		myPreferences = getPreferences(MODE_PRIVATE);		
		Editor saveEditor = myPreferences.edit();		
		saveEditor.putInt(YEAR, year)
		.putInt(MONTH, month)
		.putInt(DAY, day)
		.putInt(HOUR, hour)
		.putInt(MINUTE, minute)
		.putString(TITLE, title)
		.putString(DESCRIPTION, description);
		saveEditor.commit();
		Log.d("MainActivity", myPreferences.getString(TITLE, "dfdf"));
		
	}

	private void initViewComponents() {
		editTime = (EditText) findViewById(R.id.editTime);
		editTime.setOnClickListener(this);
		editTitle = (EditText) findViewById(R.id.editTitle);
		editTitle.setOnClickListener(this);
		editDescription = (EditText) findViewById(R.id.editDescription);
		editDescription.setOnClickListener(this);
		editDate = (EditText) findViewById(R.id.editDate);
		editDate.setOnClickListener(this);
		button = (Button) findViewById(R.id.btnSave);
		button.setOnClickListener(this);
	}

	private void initDialogs() {
		timeDialog = (TimePickerDialogFragment) getFragmentManager().findFragmentByTag(TIME_DIALOG_TAG);
		dateDialog = (DatePickerDialogFragment) getFragmentManager().findFragmentByTag(DATE_DIALOG_TAG);
		if (timeDialog != null) {
			timeDialog.setListener((TimePickerDialogFragment.OnCompleteListener) this);
		}
		if (dateDialog != null) {
			dateDialog.setListener((DatePickerDialogFragment.OnCompleteListener) this);
		}
	}

	@Override
	public void onTimePicked(int newHour, int newMinute) {
		hour = newHour;
		minute = newMinute;
		updateUI();
	}

	private void updateUI() {
		editTitle.setText(title);
		editDescription.setText(description);
		editDate.setText(year + "." + month + "." + day);
		editTime.setText(hour + ":" + minute);
	}

	private Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

	@Override
	public void onDatePicked(int newYear, int newMonth, int newDay) {
		year = newYear;
		month = newMonth;
		day = newDay;
		updateUI();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.mainLayout): {
			mainLayout.requestFocus();
			Log.d("MainActivity", "focus");
			return;
		}

		case (R.id.editDate): {
			showInputDialog(DATE_DIALOG_TAG);
			return;
		}

		case (R.id.editTime): {
			showInputDialog(TIME_DIALOG_TAG);
			return;
		}
		case (R.id.btnSave): {
			saveData();
			return;
		}
		}
	}

	private void showInputDialog(String type) {
		switch (type) {
		case (DATE_DIALOG_TAG): {
			dateDialog = new DatePickerDialogFragment();
			dateDialog.setListener((DatePickerDialogFragment.OnCompleteListener) this);
			dateDialog.show(getFragmentManager(), DATE_DIALOG_TAG);
			return;
		}
		case (TIME_DIALOG_TAG): {
			timeDialog = new TimePickerDialogFragment();
			timeDialog.setListener((TimePickerDialogFragment.OnCompleteListener) this);
			timeDialog.show(getFragmentManager(), TIME_DIALOG_TAG);
			return;
		}
		}
	}

	private void initByToday() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
	}


}
