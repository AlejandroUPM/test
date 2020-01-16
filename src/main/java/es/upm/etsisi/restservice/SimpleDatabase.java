package es.upm.etsisi.restservice;

public class SimpleDatabase {

	private static String ofLuminosity = "0.0"; 	
	private static String ofHumidity = "0.0";	
	private static String ofTemperature = "0.0";	
	private static String drLuminosity = "0.0"; 	
	private static String drHumidity = "0.0";	
	private static String drTemperature = "0.0";	
	private static String emergency = "false";
	
	public static String getOfLuminosity() {
		return ofLuminosity;
	}
	public static void setOfLuminosity(String ofLuminosity) {
		SimpleDatabase.ofLuminosity = ofLuminosity;
	}
	public static String getOfHumidity() {
		return ofHumidity;
	}
	public static void setOfHumidity(String ofHumidity) {
		SimpleDatabase.ofHumidity = ofHumidity;
	}
	public static String getOfTemperature() {
		return ofTemperature;
	}
	public static void setOfTemperature(String ofTemperature) {
		SimpleDatabase.ofTemperature = ofTemperature;
	}
	public static String getDrLuminosity() {
		return drLuminosity;
	}
	public static void setDrLuminosity(String drLuminosity) {
		SimpleDatabase.drLuminosity = drLuminosity;
	}
	public static String getDrHumidity() {
		return drHumidity;
	}
	public static void setDrHumidity(String drHumidity) {
		SimpleDatabase.drHumidity = drHumidity;
	}
	public static String getDrTemperature() {
		return drTemperature;
	}
	public static void setDrTemperature(String drTemperature) {
		SimpleDatabase.drTemperature = drTemperature;
	}
	public static String getEmergency() {
		return emergency;
	}
	public static void setEmergency(String emergency) {
		SimpleDatabase.emergency = emergency;
	}	
	
}
