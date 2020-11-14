package com.myprojectspring.demo;

import models.Location;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dren Bilalli on 11/14/2020
 */

@Service
public class CovidData {

    private static final String  VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static final String VIRUS_DATA_DEATH = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private List<Location> allStats = new ArrayList<>();

    public List<Location> getAllStats() {
        return allStats;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<Location> newStats = new ArrayList<>();
        List<Integer> deathNumbers = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

        HttpRequest requestOther = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_DEATH)).build();
        HttpResponse<String> httpResponseOther = client.send(requestOther,HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReaderTwo = new StringReader(httpResponseOther.body());
        Iterable<CSVRecord> recordsOther = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReaderTwo);

        for (CSVRecord record : records) {
            Location locationStat = new Location();
            locationStat.setState(record.get("Province/State"));
            locationStat.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size() - 1));
            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
            locationStat.setLatestTotalCases(latestCases);
            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
            newStats.add(locationStat);

        }
        this.allStats = newStats;


        for(CSVRecord record : recordsOther){
            int latest = Integer.parseInt(record.get(record.size() - 1));
            deathNumbers.add(latest);
        }

        for(int i=0;i<allStats.size();i++){
            allStats.get(i).setDeaths(deathNumbers.get(i));
        }

       // System.out.println(newStats);
    }

    public Location maxLocationByCases(){
        Location location = null;
        int countCases = allStats.get(0).getLatestTotalCases();
        for (Location allStat : allStats) {
            if (location == null || allStat.getLatestTotalCases() > countCases) {
                location = allStat;
                countCases = allStat.getLatestTotalCases();
            }
        }

        return location;

    }
    public Location maxLocationByDeath(){
        Location location = null;
        int countDeath = allStats.get(1).getLatestTotalCases();

        for(Location allStat: allStats){
            if(location == null || allStat.getDeaths() > countDeath){
                location = allStat;
                countDeath = allStat.getDeaths();
            }
        }
        return location;
    }

}