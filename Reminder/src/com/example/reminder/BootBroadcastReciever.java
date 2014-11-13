package com.example.reminder;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class BootBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences myPreferences = context.getSharedPreferences(MainActivity.PREFERENCES_NAME, 0);

		Calendar calendar = Calendar.getInstance();
		int day = myPreferences.getInt(MainActivity.DAY, calendar.get(Calendar.DAY_OF_MONTH));
		int month = myPreferences.getInt(MainActivity.MONTH, calendar.get(Calendar.MONTH));
		int year = myPreferences.getInt(MainActivity.YEAR, calendar.get(Calendar.YEAR));
		int hour = myPreferences.getInt(MainActivity.HOUR, calendar.get(Calendar.HOUR_OF_DAY));
		int minutes = myPreferences.getInt(MainActivity.MINUTE, calendar.get(Calendar.MINUTE));

		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minutes);
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		Intent goalIntent = new Intent(context, AlarmBroadcastReciever.class);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, goalIntent, PendingIntent.FLAG_UPDATE_CURRENT);



		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
	}

}
