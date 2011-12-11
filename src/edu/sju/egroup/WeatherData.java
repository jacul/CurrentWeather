/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import java.util.ArrayList;

public class WeatherData {
	public String						city;
	public String						postal_code;
	public String						latitude;
	public String						longitude;
	public String						forecast_date;
	public String						current_date_time;
	public String						unit_system;
	public long							updatetime;

	private static int					tempFormat	= 0;
	public CurrentCondition				current;

	// Forecast four days.
	public ArrayList<ForecastCondition>	forecasts	= new ArrayList<ForecastCondition>();

	public WeatherData() {
		current = new CurrentCondition();
		updatetime = System.currentTimeMillis();
	}

	/**
	 * Set temperature to standard or metric.
	 * 
	 * @param format
	 *            0 for metric, 1 for standard
	 */
	public void setTempFormat(int format) {
		tempFormat = format;
	}

	@Override
	public String toString() {
		return "WeatherData [city=" + city + ", current=" + current
				+ ", current_date_time=" + current_date_time
				+ ", forecast_date=" + forecast_date + ", forecasts="
				+ forecasts + ", latitude=" + latitude + ", longitude="
				+ longitude + ", postal_code=" + postal_code + ", unit_system="
				+ unit_system + "]";
	}

	public static class CurrentCondition {
		public String	condition;
		public String	temp_f;
		public String	temp_c;
		public String	humidity;
		public String	icon;
		public String	wind;

		/**
		 * Get temperature based on temprature format.
		 * 
		 * @return
		 */
		public String getTemp() {
			if (tempFormat == 0) {//metric
				return temp_c + " C";
			} else {
				return temp_f + " F";
			}
		}

		@Override
		public String toString() {
			return "CurrentCondition [condition=" + condition + ", humidity="
					+ humidity + ", icon=" + icon + ", temp_c=" + temp_c
					+ ", temp_f=" + temp_f + ", wind=" + wind + "]";
		}
	}

	public static class ForecastCondition {
		public String	day_of_week;
		public String	low;
		public String	high;
		public String	icon;
		public String	condition;

		/**
		 * Since the API only provides standard temperature, we may need to
		 * convert it.
		 * 
		 * @return Temperature in right format.
		 */
		public String getLow() {
			if (tempFormat == 0)//metric system, convert the data
				return (Integer.parseInt(low) - 32) * 5 / 9 + "";
			else
				return low;
		}

		/**
		 * @see #getLow()
		 * @return
		 */
		public String getHigh() {
			if (tempFormat == 0)//metric system, convert the data
				return (Integer.parseInt(high) - 32) * 5 / 9 + "";
			else
				return high;
		}

		@Override
		public String toString() {
			return "ForecastCondition [condition=" + condition
					+ ", day_of_week=" + day_of_week + ", high=" + high
					+ ", icon=" + icon + ", low=" + low + "]";
		}
	}
}
