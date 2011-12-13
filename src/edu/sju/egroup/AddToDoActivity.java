package edu.sju.egroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * This activity is for users to add or edit their TO-DO actions which are based
 * on the weather condition. Sound and vibration can be used to alert users.
 * 
 * @author zxd
 * 
 */
public class AddToDoActivity extends Activity implements OnClickListener, SettingsConstant {

	/**
	 * Save button
	 */
	private Button saveButton;
	/**
	 * Cancel button
	 */
	private Button cancelButton;
	/**
	 * Spinner to choos weather condition
	 */
	private Spinner weatherSpinner;
	/**
	 * Content about what to do
	 */
	private EditText toDoField;
	/**
	 * CheckBox for reminding by sound
	 */
	private CheckBox soundCheck;
	/**
	 * CheckBox for reminding by Vibration
	 */
	private CheckBox vibrationCheck;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setContentView(R.layout.addevent);
		this.setTitle("Add a ToDo");
		saveButton = (Button) this.findViewById(R.id.addevent_savebutton);
		saveButton.setOnClickListener(this);

		cancelButton = (Button) this.findViewById(R.id.addevent_cancelbutton);
		cancelButton.setOnClickListener(this);

		weatherSpinner = (Spinner) this.findViewById(R.id.weathertype);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weatherconditions,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weatherSpinner.setAdapter(adapter);

		toDoField = (EditText) this.findViewById(R.id.todotextfield);

		soundCheck = (CheckBox) this.findViewById(R.id.usesound);

		vibrationCheck = (CheckBox) this.findViewById(R.id.usevibrate);
	}

	public void onClick(View v) {
		if (v == saveButton) {
			Intent resultValue = new Intent();
			resultValue.putExtra(TODOTEXT, toDoField.getText().toString());
			resultValue.putExtra(WEATHERCONDITION, this.getResources().getStringArray(R.array.weatherconditions)[weatherSpinner
					.getSelectedItemPosition()]);
			resultValue.putExtra(USESOUND, soundCheck.isChecked());
			resultValue.putExtra(USEVIBRATION, vibrationCheck.isChecked());
			setResult(RESULT_OK, resultValue);
			finish();
		} else if (v == cancelButton) {
			finish();
		}
	}
}