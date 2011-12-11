/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

public interface SettingsConstant {
	static final String	AUTOMATIC		= "automatic";
	static final String	UPDATEFREQ		= "updatefreq";
	static final String	TEMPFORMAT		= "timeformat";
	static final String	LOCATION		= "location";
	static final String	LOCATIONS		= "locations";

	/**
	 * 1 hour, 2 hours, 3 hours, 6 hours, 12 hours, one day, Manual.
	 */
	static final long[]	INTERVAL		= { 1 * 216000000, 2 * 216000000,
			3 * 216000000, 6 * 216000000, 12 * 216000000, 24 * 216000000, 0 };

	static final String	LOCATIONNAME	= "name";
	static final String	USEGPS			= "gps";
	static final String	LONG			= "long";
	static final String	LATI			= "lati";
	//Used in menus.
	static final int	DELETE			= 0;
	static final int	EDIT			= 1;
	static final int	CANCEL			= 2;
}
