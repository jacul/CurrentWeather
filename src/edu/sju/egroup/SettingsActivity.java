/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsActivity extends Activity implements SettingsConstant {

	private Spinner				onoffspinner;
	private Spinner				updatespinner;
	private Spinner				tempspinner;
	private Button				saveButton;

	private SharedPreferences	settings;

	public void onCreate(Bundle savedBundle) {
		Log.i(this.getPackageName(), "settings activity");
		super.onCreate(savedBundle);
		this.setContentView(R.layout.settings);

		settings = this.getSharedPreferences(this.getPackageName(), 0);

		onoffspinner = (Spinner)this.findViewById(R.id.onoffspinner);
		updatespinner = (Spinner)this.findViewById(R.id.updatespinner);
		tempspinner = (Spinner)this.findViewById(R.id.tempspinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.onoff_array,
						android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		onoffspinner.setAdapter(adapter);

		adapter = ArrayAdapter.createFromResource(this, R.array.update_array,
				android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		updatespinner.setAdapter(adapter);

		adapter = ArrayAdapter.createFromResource(this, R.array.temp_array,
				android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tempspinner.setAdapter(adapter);

		onoffspinner.setSelection(settings.getInt(AUTOMATIC, 0));
		updatespinner.setSelection(settings.getInt(UPDATEFREQ, 0));
		tempspinner.setSelection(settings.getInt(TEMPFORMAT, 0));

		saveButton = (Button)this.findViewById(R.id.set_save_button);
		saveButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				saveContent();
			}

		});

	}

	/**
	 * Save the settings.
	 */
	private void saveContent() {
		Editor editor = settings.edit();
		editor.putInt(AUTOMATIC, onoffspinner.getSelectedItemPosition());
		editor.putInt(UPDATEFREQ, updatespinner.getSelectedItemPosition());
		editor.putInt(TEMPFORMAT, tempspinner.getSelectedItemPosition());
		editor.commit();
		this.finish();
	}
}
