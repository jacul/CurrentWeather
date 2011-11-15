/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddLocationActivity extends Activity implements SettingsConstant {
	int					widgetId	= AppWidgetManager.INVALID_APPWIDGET_ID;
	private EditText	locationText;

	@Override
	public void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		Log.i("edu.sju.egroup", "create activity");
		// Set the result to CANCELED. This will cause the widget host to cancel
		// out of the widget placement if they press the back button.
		// setResult(RESULT_CANCELED);

		// Set the view layout resource to use.
		setContentView(R.layout.addlocation);

		// Find the EditText
		locationText = (EditText)findViewById(R.id.locationText);
		mOnClickListener = new View.OnClickListener() {
			public void onClick(View v) {

				/**
				 * To know if the input location is a valid one, we have to
				 * start the update service to check it.
				 */
				Intent testUpdateIntent = new Intent(AddLocationActivity.this,
						UpdateService.class);
				testUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
						widgetId);
				testUpdateIntent.putExtra(LOCATION, locationText.getText()
						.toString());
				AddLocationActivity.this.startService(testUpdateIntent);

				// Make sure we pass back the original appWidgetId
				Intent resultValue = new Intent();
				resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
						widgetId);
				setResult(RESULT_OK, resultValue);
				finish();
			}
		};
		// Bind the action for the save button.
		findViewById(R.id.save_button).setOnClickListener(mOnClickListener);

		// Find the widget id from the intent.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

	}

	View.OnClickListener	mOnClickListener;
}
