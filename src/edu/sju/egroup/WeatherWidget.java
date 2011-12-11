/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class WeatherWidget extends AppWidgetProvider {

	/** Called when the activity is first created. */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		String packageName = context.getPackageName();
		Log.i(packageName, "onUpdate " + appWidgetIds.length);
		registListener(context, appWidgetManager, appWidgetIds);

		Intent firstUpdateIntent = new Intent(context, UpdateService.class);
		firstUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
		context.startService(firstUpdateIntent);

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	/**
	 * Add listeners to the buttons on the widget.
	 * 
	 * @param context
	 *            Applicaiton
	 * @param appWidgetManager
	 *            Manager of all the widgets
	 * @param appWidgetIds
	 *            our widgets' id's
	 */
	protected void registListener(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int length = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < length; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch AddLocationActivity
			Intent addIntent = new Intent(context, LocationManagerActivity.class);
			Intent settingIntent = new Intent(context, SettingsActivity.class);
			Intent updateIntent = new Intent(context, UpdateService.class);
			Intent changeLocationIntent = new Intent(context, UpdateService.class);

			updateIntent.setData(Uri.parse(updateIntent.toUri(Intent.URI_INTENT_SCHEME)));

			addIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			settingIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
			changeLocationIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

			PendingIntent addPend = PendingIntent.getActivity(context, 0, addIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent settingPend = PendingIntent.getActivity(context, 0, settingIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent updatePend = PendingIntent.getService(context, 0, updateIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent changePend = PendingIntent.getService(context, 0, changeLocationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
			views.setOnClickPendingIntent(R.id.addbutton, addPend);
			views.setOnClickPendingIntent(R.id.settingbutton, settingPend);
			views.setOnClickPendingIntent(R.id.updatebutton, updatePend);
			views.setOnClickPendingIntent(R.id.cityinfo, changePend);

			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

}