package es.upm.etsisi.restservice;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.upm.etsisi.listener.MQTTSubscriber;
import es.upm.etsisi.websockets.DataController;

@RestController
public class DatosController {

	static Logger logger = LoggerFactory.getLogger(DatosController.class);
	
	@Autowired
	private SimpMessagingTemplate template;

	
	@RequestMapping(value = "/api/data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonNode initial(
			@RequestParam(value = "room", defaultValue = "all") String room, 
			@RequestParam(value = "sensor", defaultValue = "all") String sensor) throws IOException {
		
		String content = "{";
		
		switch (room) {
			case "office": content += "\"office\": {" + getRoomSensorValues("office", sensor) + "}"; break;
			case "dining_room": content += "\"dining_room\": {" + getRoomSensorValues("dining_room", sensor) + "}"; break;
			default: 
				content += "\"office\": {" + getRoomSensorValues("office", sensor) + "}," +
							"\"dining_room\": {" + getRoomSensorValues("dining_room", sensor) + "}";
				break;
		}
		
		if (sensor.equals("all")) {
			content += ",\"emergency\":" + getEmergency();
		}
		
		content += "}";
		
		
		return (new ObjectMapper()).readTree(content);
	}
	
	
	private String getRoomSensorValues(String room, String sensor) {
		String content;
		
		switch (sensor) {
			case "temperature":	content = "\"temperature\":" +	getTemperature(room); break;
			case "luminosity":	content = "\"luminosity\":" + 	getLuminosity(room); break;
			case "humidity":	content = "\"humidity\":" + 	getHumidity(room); break;
			default:
				content = "\"temperature\":" + getTemperature(room) + ",\"luminosity\":" + getLuminosity(room) + ",\"humidity\":" + getHumidity(room);
				break;
		}
		
		return content;
	}
	
	
	@RequestMapping(value = "/api/set/{room}/{sensor}/{value}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setValue(@PathVariable String room, @PathVariable String sensor, @PathVariable Double value) throws IOException {
		
		String sensorId = "\"sensor_";
		switch (room) {
			case "office":
			case "dining_room": sensorId += room + "\""; break;
			default: sensorId += "office\""; break;
		}
		
		String datatypeCommand;
		switch (sensor) {
			case "temperature":
			case "luminosity": datatypeCommand = "\"" + sensor + "\""; break;
			default: datatypeCommand = "\"temperature\""; break;
		}
		
		String id = "\"API\"";
		String datatype = "\"control\"";
		String command = "{\"sensor_id\":" + sensorId + ", \"datatype\":" + datatypeCommand + ", \"value\":" + value.toString() + "}";
		String data = "{\"id\":" + id + ", \"datatype\":" + datatype + ", \"command\": " + command + "}";
		
		JsonNode json = (new ObjectMapper()).readTree(data);
		
		
		this.template.convertAndSend("/topic/facilityManagement", json);
	}
	
	
	
	private String getEmergency() {
		return SimpleDatabase.getEmergency();
	}
	
	
	private String getLuminosity(String room) {
		String data = "";
		switch (room) {
			case "dining_room": data = SimpleDatabase.getDrLuminosity(); break;
			default: data = SimpleDatabase.getOfLuminosity(); break;
		}
		
		return data;
	}
	
	
	private String getTemperature(String room) {
		String data = "";
		switch (room) {
			case "dining_room": data = SimpleDatabase.getDrTemperature(); break;
			default: data = SimpleDatabase.getOfTemperature(); break;
		}
		
		return data;
	}
	
	private String getHumidity(String room) {
		String data = "";
		switch (room) {
			case "dining_room": data = SimpleDatabase.getDrHumidity(); break;
			default: data = SimpleDatabase.getOfHumidity(); break;
		}
		
		return data;
	}
}
