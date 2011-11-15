/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateService extends Service {
	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(WeatherWidget.packageName, "Called");

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

		int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		if (appWidgetIds.length > 0) {
			for (int widgetId : appWidgetIds) {

			}
			stopSelf();
		}
		// super.onStart(intent, startId);
	}

	public static void updateAppWidget(Context context, int appWidgetId, Bitmap bitmap) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);
		views.setBitmap(R.id.currentimage, "setImageBitmap", bitmap);

		// Tell the widget manager
		appWidgetManager.updateAppWidget(appWidgetId, views);

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected static byte[] getResource(String resourcePath) {
		if (resourcePath == null)
			return null;
		System.out.println(resourcePath);
		URL url;
		try {
			url = new URL(resourcePath);
			BufferedInputStream in = new BufferedInputStream(url.openStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos.toByteArray();
		} catch (MalformedURLException e) {
			Log.e("currentweather", e.getClass().getName() + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("currentweather", e.getClass().getName() + e.getMessage());
			return null;
		}
	}
}