package com.example.reminder;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements TimePickerDialogFragment.OnCompleteListener,
		DatePickerDialogFragment.OnCompleteListener, OnClickListener {

	protected static final String YEAR = "year";
	protected static final String MONTH = "month";
	protected static final String DAY = "day";
	protected static final String HOUR = "hour";
	protected static final String MINUTE = "minute";
	protected static final String DESCRIPTION = "description";
	protected static final String TITLE = "title";
	protected static final String PREFERENCES_NAME = "title";
	private final String DATE_DIALOG_TAG = "dateDialog";
	private final String TIME_DIALOG_TAG = "timeDialog";
	private final int DEFAULT_VALUE = -1;
	private int year, month, day;
	private int hour, minute;
	private TimePickerDialogFragment timeDialog;
	private DatePickerDialogFragment dateDialog;
	private RelativeLayout mainLayout;
	private EditText editTime;
	private EditText editTitle;
	private EditText editDescription;
	private EditText editDate;
	private Button button;
	private SharedPreferences preferences;
	private String description;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViewComponents();

		if (savedInstanceState != null) {
			initDialogs();
		}

		loadData();

		editWindowManagerLayoutParams();

	}

	private void editWindowManagerLayoutParams() {
		this.getWindow().addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
		this.getWindow().addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		this.getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
	}

	private void loadData() {
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		year = preferences.getInt(YEAR, DEFAULT_VALUE);
		month = preferences.getInt(MONTH, DEFAULT_VALUE);
		day = preferences.getInt(DAY, DEFAULT_VALUE);
		hour = preferences.getInt(HOUR, DEFAULT_VALUE);
		minute = preferences.getInt(MINUTE, DEFAULT_VALUE);
		description = preferences.getString(DESCRIPTION, "");
		title = preferences.getString(TITLE, "");

		if (year == DEFAULT_VALUE || hour == DEFAULT_VALUE) {
			initByToday();
		}
		updateUI();

	}

	private void saveData() {
		title = editTitle.getText().toString();
		description = editDescription.getText().toString();
		preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
		Editor saveEditor = preferences.edit();
		saveEditor.putInt(YEAR, year).putInt(MONTH, month).putInt(DAY, day).putInt(HOUR, hour).putInt(MINUTE, minute).putString(TITLE, title)
				.putString(DESCRIPTION, description);
		saveEditor.commit();

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
	public void onClick(View v) {
		switch (v.getId()) {
		case (R.id.mainLayout): {
			hideSoftInput();
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
			if (!title.isEmpty()) {
				setAlarmManager();
				showToast(getString(R.string.saved));
			} else
				showToast(getString(R.string.notValid));
			return;
		}
		}
	}

	private void hideSoftInput() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTitle.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(editDescription.getWindowToken(), 0);
	}

	private void setAlarmManager() {
		Intent intent = new Intent(this, AlarmBroadcastReciever.class);
		PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
		Calendar calendar = getAlarmCalendar();
		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
	}

	private void showInputDialog(String type) {
		saveData();
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

	@Override
	public void onTimePicked(int newHour, int newMinute) {
		hour = newHour;
		minute = newMinute;
		updateUI();
	}

	@Override
	public void onDatePicked(int newYear, int newMonth, int newDay) {
		year = newYear;
		month = newMonth;
		day = newDay;
		updateUI();
	}

	private void updateUI() {
		editTitle.setText(title);
		editDescription.setText(description);
		editDate.setText(day + "." + month + "." + year);
		editTime.setText(hour + ":" + minute);
	}

	private Calendar getAlarmCalendar() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

	private void initByToday() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
	}

	private void showToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
		mainLayout.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		saveData();
		super.onPause();
	}

}
