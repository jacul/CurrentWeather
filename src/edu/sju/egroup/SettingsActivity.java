package edu.sju.egroup;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SettingsActivity extends Activity {

	private int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	public void onCreate(Bundle savedBundle) {
		Log.i("edu.sju.egroup", "settings activity");
		super.onCreate(savedBundle);
		this.setContentView(R.layout.settings);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		new UpdateContentTask(this, widgetId).execute("19131");
	}
}
