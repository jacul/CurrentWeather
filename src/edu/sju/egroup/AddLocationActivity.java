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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddLocationActivity extends Activity implements SettingsConstant,
		OnClickListener {
	/**
	 * The widget which triggers this activity.
	 */
	private int					widgetId	= AppWidgetManager.INVALID_APPWIDGET_ID;
	/**
	 * TextField to receive user input of location.
	 */
	private EditText			locationText;
	/**
	 * Checkbox to set if use GPS info.
	 */
	private CheckBox			useGPSCheck;
	/**
	 * Save button
	 */
	private Button				saveButton;
	/**
	 * Cancel button
	 */
	private Button				cancelButton;

	@Override
	public void onCreate(Bundle savedBundle) {
		super.onCreate(savedBundle);
		this.setTitle("Add a Location");
		Log.i("edu.sju.egroup", "create activity");

		// Set the view layout resource to use.
		setContentView(R.layout.addlocation);

		// Find the EditText
		locationText = (EditText)findViewById(R.id.locationText);

		// Find the CheckBox
		useGPSCheck = (CheckBox)findViewById(R.id.usegps);

		// Bind the action for the save button.
		saveButton = (Button)findViewById(R.id.addLocation_saveButton);
		saveButton.setOnClickListener(this);

		cancelButton = (Button)findViewById(R.id.addLocation_cancelButton);
		cancelButton.setOnClickListener(this);
		// Find the widget id from the intent.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			//Editing a location
			widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			if(extras.getString(LOCATIONNAME)!=null){
				locationText.setText(extras.getString(LOCATIONNAME));
				useGPSCheck.setChecked(extras.getBoolean(USEGPS));
			}
		}

	}

	public void onClick(View v) {
		if (v == saveButton) {
			/**
			 * To know if the input location is a valid one, we have to start
			 * the update service to check it.
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
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			resultValue.putExtra(LOCATIONNAME, locationText.getText()
					.toString());
			resultValue.putExtra(USEGPS, useGPSCheck.isChecked());
			setResult(RESULT_OK, resultValue);
			finish();
		} else if (v == cancelButton) {
			setResult(RESULT_CANCELED, null);
			finish();
		}
	};
}
