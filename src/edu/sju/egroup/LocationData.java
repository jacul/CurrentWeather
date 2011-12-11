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
public class LocationData implements SettingsConstant {
	public String	name	= "";
	public boolean	useGPS;
	public double	longitude;
	public double	latitude;

	public LocationData() {

	}

	/**
	 * Create a location data from a JSON String.
	 * 
	 * @param jsonString
	 */
	public LocationData(String jsonString) {
		try {
			setData(new JSONArray(jsonString));
		} catch (JSONException e) {}
	}

	/**
	 * Create a location data with a JSONArray object.
	 * 
	 * @param array
	 */
	public LocationData(JSONArray array) {
		setData(array);
	}

	/**
	 * Set the data of this location with a JSONArray object.
	 * 
	 * @param array
	 */
	private void setData(JSONArray array) {
		name = array.optString(0);
		useGPS = array.optBoolean(1);
		longitude = array.optDouble(2);
		latitude = array.optDouble(3);
	}

	/**
	 * Get this Location data as a HashMap.
	 * 
	 * @return
	 */
	public HashMap<String, Object> toMap() {
		HashMap<String, Object> datamap = new HashMap<String, Object>();
		datamap.put(LOCATIONNAME, this.name);
		datamap.put(USEGPS, this.useGPS);
		datamap.put(LONG, this.longitude);
		datamap.put(LATI, this.latitude);
		//also put the location data so that it can be used immediately.
		datamap.put(LOCATION, this);
		return datamap;
	}

	/**
	 * Convert this object into a JSONArray.
	 * 
	 * @return Converted JSONArray.
	 */
	public JSONArray toJSONArray() {
		JSONArray array = new JSONArray();
		array.put(name);
		array.put(useGPS);
		try {
			array.put(longitude);
			array.put(latitude);
		} catch (JSONException e) {
			System.err.println(e.getClass().getName() + e.getMessage());
		}
		return array;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationData other = (LocationData)obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String toString() {
		return name;
	}
}
