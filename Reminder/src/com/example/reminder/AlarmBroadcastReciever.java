package com.example.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmBroadcastReciever extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 1212;
	private NotificationManager notificationManager;

	@Override
	public void onReceive(Context context, Intent arg1) {
		Intent targetIntent = new Intent(context, MainActivity.class);
		targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, getNotification(pendingIntent, context));

		context.startActivity(targetIntent);
	}

	private Notification getNotification(PendingIntent pIntent, Context context) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
		builder.setContentIntent(pIntent)
				.setContentTitle(context.getSharedPreferences(MainActivity.PREFERENCES_NAME, 0).getString(MainActivity.TITLE, ""))
				.setWhen(System.currentTimeMillis()).setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher);
		return builder.build();

	}

}
