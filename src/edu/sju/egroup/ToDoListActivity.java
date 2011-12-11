package edu.sju.egroup;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
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

	static final String EVENTKEY = "event";
	/**
	 * Button to add a ToDo.
	 */
	private Button addToDoButton;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setTitle("Manage TODOs");

		this.setContentView(R.layout.eventlist);

		ListView list = (ListView) this.findViewById(R.id.todolist);

		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

		HashMap<String, String> todo = new HashMap<String, String>();
		addToDoButton = (Button) this.findViewById(R.id.eventlist_addbutton);
		addToDoButton.setOnClickListener(this);
 
		list.setAdapter(new SimpleAdapter(this, listItem,// data
				R.layout.todolistitem,// xml layout of listitem
				// key of the data in listitem
				new String[] { EVENTKEY },
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
				menu.add(0, DELETE, 0, "Delete");
				menu.add(0, EDIT, 0, "Edit");
				menu.add(0, CANCEL, 0, "Cancel");
			}

		});
	}

	// Menu
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE:
			break;
		case EDIT:
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
}
