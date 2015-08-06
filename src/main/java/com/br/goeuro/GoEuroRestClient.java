package com.br.goeuro;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class GoEuroRestClient {

	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	private static final String FILE_HEADER = "_id, name, type, latitude, longitude";

	private static Logger logger = Logger.getLogger(GoEuroRestClient.class.getName());
	
	private List<CityLocationInfo> getCitiesLocationInfo(String country) {
		List<CityLocationInfo> citiesLocationInfo = new ArrayList<CityLocationInfo>();
		try {

			logger.info("Going to send request to get cities location info for : " + country);
		    ClientConfig config = new DefaultClientConfig();
		    config.getClasses().add(JacksonJaxbJsonProvider.class);
		    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		    
		    Client client = Client.create(config);

		    WebResource webResource = client
					.resource("http://api.goeuro.com/api/v2/position/suggest/en/"
							+ country);

			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				logger.error("Failed to get cities location info, HTTP error code : " + response.getStatus());
			}
			
			citiesLocationInfo = response
					.getEntity(new GenericType<List<CityLocationInfo>>() {
					});

			logger.info("The request succeeded, got location info about "+citiesLocationInfo.size()+" cities");

		} catch (Exception e) {
			logger.error("Failed to get cities location info", e);
		}

		return citiesLocationInfo;
	}

	private static void generateCsvFile(String sFileName,
			List<CityLocationInfo> citiesLocations) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(sFileName);
			fileWriter.append(FILE_HEADER);
			fileWriter.append(NEW_LINE_SEPARATOR);

			for (CityLocationInfo cityLocationInfo : citiesLocations) {
				fileWriter.append(String.valueOf(cityLocationInfo.get_id()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(cityLocationInfo.getName());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(cityLocationInfo.getType());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(cityLocationInfo
						.getGeo_position().getLatitude()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(cityLocationInfo
						.getGeo_position().getLongitude()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
			logger.info("CSV file was created successfully");
		} catch (Exception e) {
			logger.error("Error during generateCsvFile method",e);
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				logger.error("Error during flushing/closing fileWriter",e);
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		if (args.length==0) {
			logger.error("Missing country parameter");
			System.exit(0);
		}
		String csvFileName = "cities_location_info.csv";
		GoEuroRestClient restClient = new GoEuroRestClient();
		List<CityLocationInfo> citiesLocationInfo = restClient
				.getCitiesLocationInfo(args[0]);
		if (!citiesLocationInfo.isEmpty()) generateCsvFile(csvFileName, citiesLocationInfo);

	}

}
