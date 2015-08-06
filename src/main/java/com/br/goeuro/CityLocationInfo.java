package com.br.goeuro;


public class CityLocationInfo {
	
	private Integer _id;

	 private String key;

	 private String name;

	 private String fullName;

	 private String iata_airport_code;

	 private String type;

	 private String country;

	 private GeoPosition geo_position;
	 
	 private Integer location_id;

	 private boolean inEurope;

	 private String countryCode;

	 private boolean coreCountry;

	 private Double distance;

	public Integer get_id() {
		return _id;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return fullName;
	}

	public String getIata_airport_code() {
		return iata_airport_code;
	}

	public String getType() {
		return type;
	}

	public String getCountry() {
		return country;
	}

	public GeoPosition getGeo_position() {
		return geo_position;
	}

	public Integer getLocation_id() {
		return location_id;
	}

	public boolean isInEurope() {
		return inEurope;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public boolean isCoreCountry() {
		return coreCountry;
	}

	public Double getDistance() {
		return distance;
	}

	 
}
