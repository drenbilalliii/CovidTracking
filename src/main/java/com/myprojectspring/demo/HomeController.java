package com.myprojectspring.demo;

import models.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

/**
 * @author Dren Bilalli on 11/14/2020
 */

@Controller
public class HomeController {

    @Autowired
    CovidData covidData;


    @GetMapping("/")
    public String home(Model model) {
        List<Location> allStats = covidData.getAllStats();

        Location maxLocationByCases = covidData.maxLocationByCases();
        Location maxLocationByDeath = covidData.maxLocationByDeath();

        int totalReportedCases = 0;
        for (Location allStat : allStats) {
            int latestTotalCases = allStat.getLatestTotalCases();
            totalReportedCases += latestTotalCases;
        }
        int totalNewCases = 0;
        for (Location allStat : allStats) {
            int diffFromPrevDay = allStat.getDiffFromPrevDay();
            totalNewCases += diffFromPrevDay;
        }
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);
        model.addAttribute("biggestlocationState",maxLocationByCases);
        model.addAttribute("biggestlocationbyDeath",maxLocationByDeath.getDeathState());

        return "home";
    }


}

