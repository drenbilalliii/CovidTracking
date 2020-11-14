package models;

/**
 * @author Dren Bilalli on 11/14/2020
 */

public class Location {

    private String state;
    private String country;
    private Integer latestTotalCases;
    private Integer diffFromPrevDay;
    private Integer deaths;


    public Location(){

    }

    public int getDiffFromPrevDay() {
        return diffFromPrevDay;
    }

    public void setDiffFromPrevDay(Integer diffFromPrevDay) {
        this.diffFromPrevDay = diffFromPrevDay;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getDeaths(){
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;

    }

    @Override
    public String toString() {
        return  country.toString() + " with : " + latestTotalCases + " infected";
    }
    public String getDeathState(){
        return country + " with  " + deaths;
    }
}
