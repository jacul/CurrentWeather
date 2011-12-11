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
 * This class is to manage all locations for users. Users can enter a new
 * location, view all existing locations, and delete locations.
 * 
 * @author zxd
 * 
 */
public class LocationManagerActivity extends Activity implements
		SettingsConstant, OnClickListener {
	/**
	 * Add location button.
	 */
	private Button								addLocationButton;
	/**
	 * Save button
	 */
	private Button								backButton;
	/**
	 * List of locations.
	 */
	private ListView							locationslist;
	/**
	 * All locations data.
	 */
	private ArrayList<HashMap<String, Object>>	locationsdata;
	/**
	 * Settings
	 */
	private SharedPreferences					settings;

	public void onCreate(Bundle bundle) {
		this.setTitle("Manage Locations");
		super.onCreate(bundle);
		settings = this.getSharedPreferences(this.getPackageName(), 0);

		this.setContentView(R.layout.locationmanage);

		addLocationButton = (Button)this.findViewById(R.id.addlocationbutton);
		addLocationButton.setOnClickListener(this);

		backButton = (Button)this.findViewById(R.id.lomanage_backbutton);
		backButton.setOnClickListener(this);

		locationslist = (ListView)this.findViewById(R.id.locationslist);
		String locations = settings.getString(LOCATIONS, null);
		locationsdata = loadAllLocations(locations);
		//set the behavior of this list.
		locationslist.setAdapter(new SimpleAdapter(this, locationsdata,
				R.layout.locationitem, new String[] { LOCATIONNAME },
				new int[] { R.id.locationname }));
		// click on the item
		locationslist.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> adapter, View view,
					int index, long arg3) {

			}
		});

		// hold down event
		locationslist
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
						int itemid = info.position;
						menu.add(0, itemid, EDIT, "Edit");
						menu.add(0, itemid, DELETE, "Delete");
						menu.add(0, itemid, CANCEL, "Cancel");
					}

				});
	}

	/**
	 * Load all locations data from provided JSON string.
	 * 
	 * @param locations
	 *            A JSON string representing all location data.
	 * @return An array list containing all location data in HashMaps.
	 */
	public static ArrayList<HashMap<String, Object>> loadAllLocations(
			String locations) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		//Get all the location data from the storage.
		if (locations != null) {
			try {
				JSONArray array = new JSONArray(locations);
				int len = array.length();
				for (int i = 0; i < len; i++) {
					JSONArray locationdata = array.getJSONArray(i);
					LocationData ldata = new LocationData(locationdata);
					data.add(ldata.toMap());
				}
			} catch (JSONException e) {
				System.err.println(e.getClass().getName() + e.getMessage());
			}
		}
		return data;
	}

	// Menu
	public boolean onContextItemSelected(MenuItem item) {
		int itemid = item.getItemId();
		switch (item.getOrder()) {
		case DELETE:
			locationsdata.remove(itemid);
			((SimpleAdapter)locationslist.getAdapter()).notifyDataSetChanged();
			saveAllLocations();
			break;
		case EDIT:
			editLocation(itemid);
			break;
		case CANCEL:
		default:
			break;
		}
		return super.onContextItemSelected(item);

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK) {
			LocationData ldata = new LocationData();
			ldata.name = intent.getStringExtra(LOCATIONNAME);
			ldata.useGPS = intent.getBooleanExtra(USEGPS, false);
			for (int i = locationsdata.size() - 1; i >= 0; i--) {
				HashMap<String, Object> hash = locationsdata.get(i);
				if (hash.get(LOCATIONNAME).equals(ldata.name)) {
					locationsdata.remove(i);
				}
			}
			locationsdata.add(ldata.toMap());
			saveAllLocations();
			((SimpleAdapter)locationslist.getAdapter()).notifyDataSetChanged();
		}
	}

	/**
	 * Save all locations. Basically it's transforming the data to a long
	 * string.
	 */
	private void saveAllLocations() {
		JSONArray alllocations = new JSONArray();
		for (HashMap<String, Object> data : locationsdata) {
			LocationData location = (LocationData)data.get(LOCATION);
			alllocations.put(location.toJSONArray());
		}
		Editor editor = settings.edit();
		editor.putString(LOCATIONS, alllocations.toString());
		editor.commit();
	}

	/**
	 * Edit a location. The index of the location in the overall locations is
	 * given.
	 * 
	 * @param itemid
	 */
	private void editLocation(int itemid) {
		HashMap<String, Object> data = locationsdata.get(itemid);
		Intent intent = new Intent(this, AddLocationActivity.class);
		intent.putExtra(LOCATIONNAME, (String)data.get(LOCATIONNAME));
		intent.putExtra(USEGPS, (Boolean)data.get(USEGPS));
		intent.putExtra(LONG, (Double)data.get(LONG));
		intent.putExtra(LATI, (Double)data.get(LATI));
		this.startActivityForResult(intent, 0);
	}

	public void onClick(View v) {
		if (v == addLocationButton) {
			Intent intent = new Intent(this, AddLocationActivity.class);
			this.startActivityForResult(intent, 0);

		} else if (v == backButton) {
			finish();
		}
	}

}
