package com.example.reminder;

import java.util.Calendar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerDialogFragment extends DialogFragment {

	private OnCompleteListener timePickListener;

	private int hour;
	private int minute;

	public static interface OnCompleteListener {
		public abstract void onTimePicked(int newHour, int newMinute);
	}

	public void setListener(OnCompleteListener listener) {
		timePickListener = listener;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		initByToday();
		TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int nMinute) {
				hour = hourOfDay;
				minute = nMinute;
				timePickListener.onTimePicked(hour, minute);
			}
		};
		return new TimePickerDialog(getActivity(), listener, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	private void initByToday() {
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
	}
}
