package com.example.reminder;

import java.util.Calendar;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.InstanceState;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

@EFragment
public class DatePickerDialogFragment extends DialogFragment {

	private OnCompleteListener datePickListener;

	@InstanceState
	int year, month, day;

	public static interface OnCompleteListener {
		public abstract void onDatePicked(int newYear, int newMonth, int newDay);
	}

	public void setListener(OnCompleteListener listener) {
		datePickListener = listener;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {

		if (savedInstanceState == null)
			initByToday();
		DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker arg0, int newYear, int newMonth,
					int newDay) {
				year = newYear;
				month = newMonth;
				day = newDay;
				datePickListener.onDatePicked(year, month, day);
			}
		};
		return new DatePickerDialog(getActivity(), listener, year, month, day);
	}

	private void initByToday() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
	}

}
