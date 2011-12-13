package edu.sju.egroup;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class represents a location, which contains its name or zip code, and
 * optional data standing for longitude and latitude.
 * 
 * @author zxd
 * 
 */
public class ToDoTextData implements SettingsConstant {
	public String toDoText = "";
	public boolean useSound;
	public boolean useVibration;
	public String weatherCondition;

	public ToDoTextData() {

	}

	/**
	 * Create a location data from a JSON String.
	 * 
	 * @param jsonString
	 */
	public ToDoTextData(String jsonString) {
		try {
			setData(new JSONArray(jsonString));
		} catch (JSONException e) {
		}
	}

	/**
	 * Create a location data with a JSONArray object.
	 * 
	 * @param array
	 */
	public ToDoTextData(JSONArray array) {
		setData(array);
	}

	/**
	 * Set the data of this location with a JSONArray object.
	 * 
	 * @param array
	 */
	private void setData(JSONArray array) {
		toDoText = array.optString(0);
		weatherCondition = array.optString(1);
		useSound = array.optBoolean(2);
		useVibration = array.optBoolean(3);
	}

	/**
	 * Get this todotext data as a HashMap.
	 * 
	 * @return
	 */
	public HashMap<String, Object> toMap() {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put(TODOTEXT, this.toDoText);
		datamap.put(WEATHERCONDITION, this.weatherCondition);
		datamap.put(USESOUND, this.useSound);
		datamap.put(USEVIBRATION, this.useVibration);
		datamap.put(EVENTKEY, this);
		return datamap;
	}

	/**
	 * Convert this object into a JSONArray.
	 * 
	 * @return Converted JSONArray.
	 */
	public JSONArray toJSONArray() {
		JSONArray array = new JSONArray();
		array.put(toDoText);
		array.put(weatherCondition);
		array.put(useSound);
		array.put(useVibration);
		return array;
	}

	public String toString() {
		return toDoText;
	}
}
