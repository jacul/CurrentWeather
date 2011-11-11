package edu.sju.egroup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WeatherDataParser {
	static final String xml_api_reply = "xml_api_reply";
	static final String weather = "weather";
	static final String forecast_information = "forecast_information";
	static final String city = "city";
	static final String data = "data";
	static final String postal_code = "postal_code";
	static final String latitude_e6 = "latitude_e6";
	static final String longitude_e6 = "longitude_e6";
	static final String forecast_date = "forecast_date";
	static final String current_date_time = "current_date_time";
	static final String unit_system = "unit_system";
	static final String current_conditions = "current_conditions";
	static final String condition = "condition";
	static final String temp_f = "temp_f";
	static final String temp_c = "temp_c";
	static final String humidity = "humidity";
	static final String icon = "icon";
	static final String wind_condition = "wind_condition";
	static final String forecast_conditions = "forecast_conditions";
	static final String day_of_week = "day_of_week";
	static final String low = "low";
	static final String high = "high";
	static final String problem_cause = "problem_cause";

	private InputStream is;
	private WeatherData weatherdata;

	public WeatherDataParser(byte[] data) {
		is = new ByteArrayInputStream(data);
		weatherdata = new WeatherData();
	}

	public void parse() throws ParserConfigurationException, SAXException, IOException, Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(is);
		doc.getDocumentElement().normalize();

		Node root = doc.getFirstChild();
		if (root.getNodeName().equals(xml_api_reply)) {
			Node w = root.getFirstChild();
			if (w.getNodeName().equals(weather)) {
				NodeList nodelist = w.getChildNodes();
				final int l = nodelist.getLength();
				for (int i = 0; i < l; i++) {
					Node node = nodelist.item(i);
					String name = node.getNodeName();
					if (name.equals(forecast_conditions)) {
						this.saveForecastCondition(node);
					} else if (name.equals(current_conditions)) {
						this.saveCurrentCondition(node);
					} else if (name.equals(forecast_information)) {
						this.saveForecastInfo(node);
					} else if (name.equals(problem_cause)) {
						weatherdata = null;
						return;
					}
				}
			}
		}

	}

	public WeatherData getResult() {
		return weatherdata;
	}

	private void saveForecastCondition(Node node) {
		NodeList children = node.getChildNodes();
		WeatherData.ForecastCondition condition = new WeatherData.ForecastCondition();
		condition.day_of_week = children.item(0).getAttributes().getNamedItem(data).getNodeValue();
		condition.low = children.item(1).getAttributes().getNamedItem(data).getNodeValue();
		condition.high = children.item(2).getAttributes().getNamedItem(data).getNodeValue();
		condition.icon = children.item(3).getAttributes().getNamedItem(data).getNodeValue();
		condition.condition = children.item(4).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.forecasts.add(condition);
	}

	private void saveCurrentCondition(Node node) {
		NodeList children = node.getChildNodes();
		weatherdata.current.condition = children.item(0).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.current.temp_f = children.item(1).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.current.temp_c = children.item(2).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.current.humidity = children.item(3).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.current.icon = children.item(4).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.current.wind = children.item(5).getAttributes().getNamedItem(data).getNodeValue();
	}

	private void saveForecastInfo(Node node) {
		NodeList children = node.getChildNodes();
		weatherdata.city = children.item(0).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.postal_code = children.item(1).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.latitude = children.item(2).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.longitude = children.item(3).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.forecast_date = children.item(4).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.current_date_time = children.item(5).getAttributes().getNamedItem(data).getNodeValue();
		weatherdata.unit_system = children.item(6).getAttributes().getNamedItem(data).getNodeValue();
	}

	// private void printNode(Node node) {
	// System.out.println(node.getNodeName());
	// if (node.getNodeType() == Node.TEXT_NODE)
	// System.out.println(node.getTextContent());
	// NamedNodeMap attrs = node.getAttributes();
	// if (attrs.getLength() > 0) {
	// Node a = attrs.item(0);
	// System.out.println(a.getNodeName() + " " + a.getNodeValue());
	// }
	// if (node.hasChildNodes())
	// printChild(node.getChildNodes());
	// }

	// private void printChild(NodeList nodes) {
	// for (int i = 0; i < nodes.getLength(); i++) {
	// Node n = nodes.item(i);
	// printNode(n);
	// }
	// }

}
