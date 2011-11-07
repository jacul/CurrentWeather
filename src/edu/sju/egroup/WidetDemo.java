package edu.sju.egroup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidetDemo extends AppWidgetProvider {

	/** Called when the activity is first created. */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.id.button1);
		Intent intent = new Intent(context.getApplicationContext(),
				UpdateService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

		// To react to a click we have to use a pending intent as the
		// onClickListener is
		// excecuted by the homescreen application
		PendingIntent pendingIntent = PendingIntent.getService(
				context.getApplicationContext(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.layout.main, pendingIntent);

		// Finally update all widgets with the information about the click
		// listener
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

		// Update the widgets via the service
		context.startService(intent);
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1,
//				10000);
//		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	private class MyTime extends TimerTask {
		RemoteViews			remoteViews;
		AppWidgetManager	appWidgetManager;
		ComponentName		thisWidget;

		public MyTime(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.main);

			thisWidget = new ComponentName(context, WidetDemo.class);
		}

		public void run() {
			Log.i("weather", "Update weather");
			Date date = new Date();
			Calendar calendar = new GregorianCalendar(2010, 06, 11);
			long days = (((calendar.getTimeInMillis() - date.getTime()) / 1000)) / 86400;
			remoteViews.setTextViewText(R.id.textView1,
					"The flows of wind are wimsical today.");
			appWidgetManager.updateAppWidget(thisWidget, remoteViews);

		}

	}

}