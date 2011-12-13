/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class UpdateService extends Service implements Runnable, NetworkConstant, SettingsConstant, LocationListener {
	/**
	 * Flag indicating if this service is running.
	 */
	private static boolean isRunning = false;
	/**
	 * Id of the widgets that are waiting to be refreshed.
	 */
	private static ArrayList<Integer> widgetIds = new ArrayList<Integer>();
	/**
	 * The id of last widget we know. Used in auto update based on location.
	 */
	private int lastWidget = 0;
	/**
	 * Timer to restart this service in order to perform a update.
	 */
	private static Timer updatetimer;
	/**
	 * A cache holding all weather data.
	 */
	private HashMap<String, WeatherData> weatherdatacache = new HashMap<String, WeatherData>();
	/**
	 * If there are more than one locations to display, the first location is
	 * displayed. Then we can
	 */
	private int locationindex = 0;
	/**
	 * The location to update.
	 */
	private LocationData location;
	/**
	 * The settings.
	 */
	private SharedPreferences settings;

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i(this.getPackageName(), "service started");
		// Get the settings
		settings = this.getSharedPreferences(this.getPackageName(), 0);

		String locations = settings.getString(LOCATIONS, null);
		ArrayList<HashMap<String, Object>> locationsdata = LocationManagerActivity.loadAllLocations(locations);

		if (locationsdata.size() == 0)
			return;

		if (locationindex >= locationsdata.size()) {
			locationindex = 0;
		}

		ArrayList<LocationData> locationdatas = LocationManagerActivity.loadAllLocationData(settings.getString(LOCATIONS, null));
		location = locationdatas.get(locationindex);

		// Get the given widget id so that we can update it.
		Bundle extras = intent.getExtras();
		if (extras != null) {
			// It's a collection of id's
			int[] widgetId = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
			if (widgetId != null) {
				for (int id : widgetId) {
					widgetIds.add(id);
				}
			}

			// It can be a single id.
			int wid = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
			if (wid != AppWidgetManager.INVALID_APPWIDGET_ID) {
				widgetIds.add(wid);
			}

			if (extras.containsKey(LOCATIONNAME)) {
				// The update request is to change to the next location.
				locationindex++;
				if (locationindex >= locationsdata.size())
					locationindex = 0;
				location = locationdatas.get(locationindex);
			}
		}

		registLocationListener();
		if (!isRunning) {
			isRunning = true;
			new Thread(this).start();
		}
	}

	/**
	 * Setup the location listener.
	 */
	private void registLocationListener() {

		LocationManager locationManager;
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, this);
	}

	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		ArrayList<LocationData> locationdatas = LocationManagerActivity.loadAllLocationData(settings.getString(LOCATIONS, null));
		for (LocationData data : locationdatas) {
			if (data.useGPS) {
				if (Math.pow(latitude - data.latitude, 2) + Math.pow(longitude - data.longitude, 2) <= 0.5) {
					System.out.println(Math.pow(12, 2));
					WeatherData weather = this.retrieveWeatherInfo(data);
					setWidgetContent(weather, lastWidget);
					break;
				}
			}
		}
	}

	public void onProviderDisabled(String provider) {

	}

	public void onProviderEnabled(String provider) {

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * Download the resource from web. And store it in a byte array.
	 * 
	 * @param resourcePath
	 *            path of resource.
	 * @return Weather data we get.
	 */
	protected static byte[] getResource(String resourcePath) {
		if (resourcePath == null)
			return null;
		Log.i("network", resourcePath);
		URL url;
		try {
			url = new URL(resourcePath);
			InputStream is = url.openStream();
			if (is == null)
				return null;
			BufferedInputStream in = new BufferedInputStream(is);
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

	public void run() {

		while (hasMoreWidgetToUpdate()) {
			WeatherData data = retrieveWeatherInfo(location);
			if (data != null) {// Means we get the weather data we want.
				setWidgetContent(data, getNextWidgetID());
			} else {
				Looper.prepare();
				Toast.makeText(getApplicationContext(), "This is not a valid location!", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}

		setAutoUpdate();

		isRunning = false;
		// stopSelf();

	}

	/**
	 * This will set the timer to wake up at user's specified interval. Note
	 * that if the user chooses to update manually, this timer will not be set;
	 * if it is already set, it will be cancelled.
	 */
	private void setAutoUpdate() {
		if (0 == settings.getInt(AUTOMATIC, 1) && INTERVAL.length - 1 != settings.getInt(UPDATEFREQ, 0)) {
			// 0 on, 1 off
			if (updatetimer != null)
				updatetimer.cancel();
			updatetimer = new Timer();
			long delay = INTERVAL[settings.getInt(UPDATEFREQ, 0)];
			int[] id = new int[widgetIds.size()];
			for (int i = 0; i < widgetIds.size(); i++) {
				id[i] = widgetIds.get(i);
			}
			updatetimer.schedule(new UpdateTimer(this.getApplicationContext(), id), delay, delay);
		} else {
			// set to turn off, cancel it.
			if (updatetimer != null)
				updatetimer.cancel();
		}
	}

	/**
	 * Get the next widget id, also it is removed from the cache.
	 * 
	 * @return next widget id.
	 */
	private int getNextWidgetID() {
		lastWidget = widgetIds.remove(0);
		return lastWidget;
	}

	/**
	 * Check if we have more widgets to update.
	 * 
	 * @return true if there is one or more widgets to update. Otherwise false.
	 */
	private boolean hasMoreWidgetToUpdate() {

		return !widgetIds.isEmpty();
	}

	/**
	 * Get the weather data from the web
	 * 
	 * @return Weather information we get from web.
	 */
	protected WeatherData retrieveWeatherInfo(LocationData location) {
		if (weatherdatacache.containsKey(location.name)) {
			WeatherData data = weatherdatacache.get(location.name);
			if (System.currentTimeMillis() - data.updatetime < INTERVAL[settings.getInt(UPDATEFREQ, 0)]) {
				// update time is within update interval, then this weather data
				// can be reused.
				return data;
			}
		}

		String resourcePath;
		if (location.useGPS) {
			DecimalFormat format = new DecimalFormat(".000000");
			String gpsdata = ",,," + format.format(location.latitude) + "," + format.format(location.longitude);
			resourcePath = HOSTURL + WEATHERPARA + gpsdata.replace(".", "");
		} else {
			resourcePath = HOSTURL + WEATHERPARA + Uri.encode(location.name, "/");
		}
		byte[] b = UpdateService.getResource(resourcePath);
		if (b == null)
			return null;
		WeatherDataParser parser = new WeatherDataParser(b);
		WeatherData data = null;
		try {
			parser.parse();
			data = parser.getResult();
			data.setTempFormat(settings.getInt(TEMPFORMAT, 0));
			if (location.useGPS)
				data.city = location.name;// There is no city info in the result
			// by sending GPS data.
			weatherdatacache.put(location.name, data);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Set the content of the widget.
	 * 
	 * @param data
	 *            Weather information.
	 * @param widgetId
	 *            id of the desired widget.
	 */
	protected void setWidgetContent(WeatherData data, int widgetId) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.main);

		views.setTextViewText(R.id.cityinfo, data.city + " " + data.postal_code + "\n" + data.current.condition);

		// set temperature text. Format depends on the settings.
		views.setTextViewText(R.id.temptext, data.current.getTemp());

		views.setTextViewText(R.id.d1, data.forecasts.get(0).day_of_week + "\n" + data.forecasts.get(0).getHigh() + "/"
				+ data.forecasts.get(0).getLow());
		views.setTextViewText(R.id.d2, data.forecasts.get(1).day_of_week + "\n" + data.forecasts.get(1).getHigh() + "/"
				+ data.forecasts.get(1).getLow());
		views.setTextViewText(R.id.d3, data.forecasts.get(2).day_of_week + "\n" + data.forecasts.get(2).getHigh() + "/"
				+ data.forecasts.get(2).getLow());
		views.setTextViewText(R.id.d4, data.forecasts.get(3).day_of_week + "\n" + data.forecasts.get(3).getHigh() + "/"
				+ data.forecasts.get(3).getLow());

		views.setBitmap(R.id.currentimage, "setImageBitmap", IconFactory.getIcon(data.current.icon));
		views.setBitmap(R.id.fore1, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(0).icon));
		views.setBitmap(R.id.fore2, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(1).icon));
		views.setBitmap(R.id.fore3, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(2).icon));
		views.setBitmap(R.id.fore4, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(3).icon));
		// Tell the widget manager
		appWidgetManager.updateAppWidget(widgetId, views);
	}

	/**
	 * This TimerTask is to schedule the service to start. The delay is set by
	 * the user.
	 * 
	 * @author zxd
	 * 
	 */
	private class UpdateTimer extends TimerTask {
		private Context context;
		private int[] widgetIds;

		public UpdateTimer(Context context, int[] widgetIds) {
			this.context = context;
			this.widgetIds = widgetIds;
		}

		public void run() {
			Intent firstUpdateIntent = new Intent(context, UpdateService.class);
			firstUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
			context.startService(firstUpdateIntent);
		}

	}
}