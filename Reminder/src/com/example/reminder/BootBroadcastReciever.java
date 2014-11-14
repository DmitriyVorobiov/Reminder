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

		calendar.set(Calendar.DAY_OF_MONTH, myPreferences.getInt(MainActivity.DAY, calendar.get(Calendar.DAY_OF_MONTH)));
		calendar.set(Calendar.MONTH, myPreferences.getInt(MainActivity.MONTH, calendar.get(Calendar.MONTH)));
		calendar.set(Calendar.YEAR, myPreferences.getInt(MainActivity.YEAR, calendar.get(Calendar.YEAR)));
		calendar.set(Calendar.HOUR_OF_DAY, myPreferences.getInt(MainActivity.HOUR, calendar.get(Calendar.HOUR_OF_DAY)));
		calendar.set(Calendar.MINUTE, myPreferences.getInt(MainActivity.MINUTE, calendar.get(Calendar.MINUTE)));
		
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		Intent targetIntent = new Intent(context, AlarmBroadcastReciever.class);
		PendingIntent pendigIntent = PendingIntent.getBroadcast(context, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendigIntent);
	}

}
