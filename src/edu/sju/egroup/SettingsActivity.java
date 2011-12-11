/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * This activity is for users to set the global behaviors of this application.
 * This includes Update intervals, temperature formats, to-do list, etc.
 * 
 * @author zxd
 * 
 */
public class SettingsActivity extends Activity implements SettingsConstant, OnClickListener {
	/**
	 * Auto update on/off.
	 */
	private Spinner autospinner;
	/**
	 * Update frequency.
	 */
	private Spinner updatefreqspinner;
	/**
	 * Temperature format.
	 */
	private Spinner tempspinner;
	/**
	 * Save button
	 */
	private Button saveButton;
	/**
	 * Cancel button
	 */
	private Button cancelButton;
	/**
	 * ToDo list manage button.
	 */
	private Button manageButton;
	/**
	 * Options.
	 */
	private SharedPreferences settings;

	public void onCreate(Bundle savedBundle) {
		Log.i(this.getPackageName(), "settings activity");
		this.setTitle("Settings");
		super.onCreate(savedBundle);
		this.setContentView(R.layout.settings);

		settings = this.getSharedPreferences(this.getPackageName(), 0);

		autospinner = (Spinner) this.findViewById(R.id.autoupdatespinner);
		updatefreqspinner = (Spinner) this.findViewById(R.id.updatespinner);
		tempspinner = (Spinner) this.findViewById(R.id.tempspinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.onoff_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		autospinner.setAdapter(adapter);

		adapter = ArrayAdapter.createFromResource(this, R.array.update_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		updatefreqspinner.setAdapter(adapter);

		adapter = ArrayAdapter.createFromResource(this, R.array.temp_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tempspinner.setAdapter(adapter);

		autospinner.setSelection(settings.getInt(AUTOMATIC, 0));
		updatefreqspinner.setSelection(settings.getInt(UPDATEFREQ, 0));
		tempspinner.setSelection(settings.getInt(TEMPFORMAT, 0));

		saveButton = (Button) this.findViewById(R.id.set_save_button);
		saveButton.setOnClickListener(this);

		cancelButton = (Button) this.findViewById(R.id.set_cancelButton);
		cancelButton.setOnClickListener(this);

		manageButton = (Button) this.findViewById(R.id.set_manageButton);
		manageButton.setOnClickListener(this);
	}

	/**
	 * Save the settings.
	 */
	private void saveContent() {
		Editor editor = settings.edit();
		editor.putInt(AUTOMATIC, autospinner.getSelectedItemPosition());
		editor.putInt(UPDATEFREQ, updatefreqspinner.getSelectedItemPosition());
		editor.putInt(TEMPFORMAT, tempspinner.getSelectedItemPosition());
		editor.commit();
		this.finish();
	}

	public void onClick(View v) {
		if (v == saveButton) {
			saveContent();

		} else if (v == cancelButton) {
			finish();
		} else if (v == manageButton) {

			Intent intent = new Intent(this, ToDoListActivity.class);
			this.startActivity(intent);

		}
	}
}
