package edu.sju.egroup;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This class displays a list of all activities that are set by the users. Also,
 * users can add a ToDO list.
 * 
 * @author zxd
 * 
 */
public class ToDoListActivity extends Activity implements SettingsConstant, OnClickListener {

	/**
	 * Button to add a ToDo.
	 */
	private Button addToDoButton;
	/**
	 * All todoText
	 */
	private ArrayList<HashMap<String, Object>> listItem;
	/**
	 * List of locations.
	 */
	private ListView list;
	/**
	 * Settings
	 */
	private SharedPreferences settings;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setTitle("Manage TODOs");

		this.setContentView(R.layout.eventlist);
		settings = this.getSharedPreferences(this.getPackageName(), 0);

		list = (ListView) this.findViewById(R.id.todolist);

		listItem = new ArrayList<HashMap<String, Object>>();

		addToDoButton = (Button) this.findViewById(R.id.eventlist_addbutton);
		addToDoButton.setOnClickListener(this);

		String events = settings.getString(EVENTKEY, null);

		// Get all the text data from the storage.
		if (events != null) {
			try {
				JSONArray array = new JSONArray(events);
				int len = array.length();
				for (int i = 0; i < len; i++) {
					JSONArray textData = array.getJSONArray(i);
					ToDoTextData data = new ToDoTextData(textData);
					listItem.add(data.toMap());
				}
			} catch (JSONException e) {
				System.err.println(e.getClass().getName() + e.getMessage());
			}
		}

		list.setAdapter(new SimpleAdapter(this, listItem,// data
				R.layout.todolistitem,// xml layout of listitem
				// key of the data in listitem
				new String[] { TODOTEXT },
				// view in the listitem, corresponding to the key
				new int[] { R.id.textView1 }));

		// click on the item
		list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view, int index, long arg3) {

			}
		});

		// hold down event
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
				int itemid = info.position;
				menu.add(0, itemid, DELETE, "Delete");
				menu.add(0, itemid, EDIT, "Edit");
				menu.add(0, itemid, CANCEL, "Cancel");
			}

		});
	}

	// Menu
	public boolean onContextItemSelected(MenuItem item) {
		int itemid = item.getItemId();
		switch (item.getOrder()) {
		case DELETE:
			listItem.remove(itemid);
			((SimpleAdapter) list.getAdapter()).notifyDataSetChanged();
			break;
		case EDIT:
			/**************************/
			break;
		case CANCEL:
		default:
			break;
		}
		return super.onContextItemSelected(item);

	}

	public void onClick(View v) {
		if (v == addToDoButton) {
			Intent addIntent = new Intent(this, AddToDoActivity.class);
			this.startActivityForResult(addIntent, 0);

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			ToDoTextData tdata = new ToDoTextData();
			tdata.toDoText = intent.getStringExtra(TODOTEXT);
			tdata.weatherCondition = intent.getStringExtra(WEATHERCONDITION);
			tdata.useSound = intent.getBooleanExtra(USESOUND, false);
			tdata.useVibration = intent.getBooleanExtra(USEVIBRATION, false);
			listItem.add(tdata.toMap());
			saveAllText();

			((SimpleAdapter) list.getAdapter()).notifyDataSetChanged();
		}
	}

	private void saveAllText() {
		JSONArray allText = new JSONArray();
		for (HashMap<String, Object> data : listItem) {
			ToDoTextData tText = (ToDoTextData) data.get(EVENTKEY);
			allText.put(tText.toJSONArray());
		}
		Editor editor = settings.edit();
		editor.putString(EVENTKEY, allText.toString());
		editor.commit();
	}
}
