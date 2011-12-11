package edu.sju.egroup;

import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * This activity is for users to add or edit their TO-DO actions which are based
 * on the weather condition. Sound and vibration can be used to alert users.
 * 
 * @author zxd
 * 
 */
public class AddToDoActivity extends Activity implements OnClickListener {

	/**
	 * Save button
	 */
	private Button saveButton;
	/**
	 * Cancel button
	 */
	private Button cancelButton;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setContentView(R.layout.addevent);
		this.setTitle("Add a ToDo");
		saveButton = (Button) this.findViewById(R.id.addevent_savebutton);
		saveButton.setOnClickListener(this);

		cancelButton = (Button) this.findViewById(R.id.addevent_cancelbutton);
		cancelButton.setOnClickListener(this);
		
		
	}

	public void onClick(View v) {
		if (v == saveButton) {
			HashSet<String> map = new HashSet<String>();
			map.add("Holyshit");
			map.add("wocao");
			JSONArray json=new JSONArray(map);
			System.out.println(json.toString());
		} else if (v == cancelButton) {
			finish();
		}
	}
}
