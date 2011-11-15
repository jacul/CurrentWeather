/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateContentTask extends AsyncTask<String, Integer, WeatherData> implements NetworkConstant {

	private String resourcePath;
	private Context context;
	private int widgetId;

	public UpdateContentTask(Context context, int id) {
		this.context = context;
		this.widgetId = id;
	}

	@Override
	protected WeatherData doInBackground(String... params) {
		resourcePath = HOSTURL + WEATHERPARA + Uri.encode(params[0].toString(), "/");
		byte[] data = UpdateService.getResource(resourcePath);
		WeatherDataParser parser = new WeatherDataParser(data);
		WeatherData d = null;
		try {
			parser.parse();
			d = parser.getResult();
			IconFactory.getIcon(d.current.icon);
			for (WeatherData.ForecastCondition fore : d.forecasts) {
				IconFactory.getIcon(fore.icon);
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

	public void onPostExecute(WeatherData data) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main);

		views.setTextViewText(R.id.cityinfo, data.city + " " + data.postal_code + "\n" + data.current.temp_c + " "
				+ data.current.condition);
		views.setBitmap(R.id.currentimage, "setImageBitmap", IconFactory.getIcon(data.current.icon));
		views.setBitmap(R.id.fore1, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(0).icon));
		views.setBitmap(R.id.fore2, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(1).icon));
		views.setBitmap(R.id.fore3, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(2).icon));
		views.setBitmap(R.id.fore4, "setImageBitmap", IconFactory.getIcon(data.forecasts.get(3).icon));
		// Tell the widget manager
		appWidgetManager.updateAppWidget(widgetId, views);
		Log.e("edu.sju.egroup", "done");
	}
}
