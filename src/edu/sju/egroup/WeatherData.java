package edu.sju.egroup;

import java.util.ArrayList;

public class WeatherData {
	public String city;
	public String postal_code;
	public String latitude;
	public String longitude;
	public String forecast_date;
	public String current_date_time;
	public String unit_system;

	public CurrentCondition current;

	// Forecast four days.
	public ArrayList<ForecastCondition> forecasts = new ArrayList<ForecastCondition>();

	public WeatherData() {
		current = new CurrentCondition();
	}

	@Override
	public String toString() {
		return "WeatherData [city=" + city + ", current=" + current + ", current_date_time=" + current_date_time + ", forecast_date="
				+ forecast_date + ", forecasts=" + forecasts + ", latitude=" + latitude + ", longitude=" + longitude + ", postal_code="
				+ postal_code + ", unit_system=" + unit_system + "]";
	}

	public static class CurrentCondition {
		public String condition;
		public String temp_f;
		public String temp_c;
		public String humidity;
		public String icon;
		public String wind;

		@Override
		public String toString() {
			return "CurrentCondition [condition=" + condition + ", humidity=" + humidity + ", icon=" + icon + ", temp_c=" + temp_c
					+ ", temp_f=" + temp_f + ", wind=" + wind + "]";
		}
	}

	public static class ForecastCondition {
		public String day_of_week;
		public String low;
		public String high;
		public String icon;
		public String condition;

		@Override
		public String toString() {
			return "ForecastCondition [condition=" + condition + ", day_of_week=" + day_of_week + ", high=" + high + ", icon=" + icon
					+ ", low=" + low + "]";
		}
	}
}
