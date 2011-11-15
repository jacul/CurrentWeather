/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WeatherWidget extends AppWidgetProvider {
	static final String ACTION_ADD = "edu.sju.egroup.WeatherWidget.ACTION_WIDGET_ADD";
	static final String ACTION_SET = "edu.sju.egroup.WeatherWidget.ACTION_WIDGET_SET";
	static final String ACTION_UPDATE = "edu.sju.egroup.WeatherWidget.ACTION_WIDGET_UPDATE";

	static String packageName;

	public void onEnabled(Context context) {
		System.out.println("holyshit");
	}

	/** Called when the activity is first created. */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.i(packageName, "onUpdate " + appWidgetIds.length);
		packageName = context.getPackageName();
		registListener(context, appWidgetManager, appWidgetIds);

		// new GetWeatherThread(GetWeatherThread.WEATHER).execute("19131");
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 10000);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	protected void registListener(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch AddLocationActivity
			Intent addIntent = new Intent(context, AddLocationActivity.class);
			Intent settingIntent = new Intent(context, SettingsActivity.class);
			Intent updateIntent = new Intent(context, UpdateService.class);
			// updateIntent.setData(Uri.parse(updateIntent.toUri(Intent.URI_INTENT_SCHEME)));

			addIntent.setAction(ACTION_ADD);

			addIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			settingIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

			PendingIntent addPend = PendingIntent.getActivity(context, 0, addIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent settingPend = PendingIntent.getActivity(context, 0, settingIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent updatePend = PendingIntent.getService(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
			Log.i(packageName, "set pending");
			views.setOnClickPendingIntent(R.id.addbutton, addPend);
			views.setOnClickPendingIntent(R.id.settingbutton, settingPend);
			views.setOnClickPendingIntent(R.id.updatebutton, updatePend);

			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	private class MyTime extends TimerTask {
		RemoteViews remoteViews;
		AppWidgetManager appWidgetManager;
		ComponentName thisWidget;

		public MyTime(Context context, AppWidgetManager appWidgetManager) {
			this.appWidgetManager = appWidgetManager;
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);

			thisWidget = new ComponentName(context, WeatherWidget.class);
		}

		public void run() {
			Log.i(packageName, "Update weather");
			Date date = new Date();
			Calendar calendar = new GregorianCalendar(2010, 06, 11);
			long days = (((calendar.getTimeInMillis() - date.getTime()) / 1000)) / 86400;
			remoteViews.setTextViewText(R.id.textView1, "The flows of wind are wimsical today.");

			appWidgetManager.updateAppWidget(thisWidget, remoteViews);

		}

	}

}