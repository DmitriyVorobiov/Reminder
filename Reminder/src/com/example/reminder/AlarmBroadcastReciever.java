package com.example.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmBroadcastReciever extends BroadcastReceiver {

	NotificationManager notificationManager;

	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent reminderActivityIntent = new Intent(context, MainActivity.class);
		reminderActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

		PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

		builder.setContentIntent(pIntent).setContentTitle(context.getString(R.string.app_name)).setWhen(System.currentTimeMillis())
				.setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher)
				.setTicker(context.getSharedPreferences(MainActivity.PREFERENCES_NAME, 0).getString(MainActivity.TITLE, ""));

		notificationManager.notify(43, builder.build());

		context.startActivity(reminderActivityIntent);
	}

}
