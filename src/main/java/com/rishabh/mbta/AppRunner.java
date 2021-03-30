package com.rishabh.mbta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * We don't need to have a web application, therefore using a simple commandlinerunner to print stuff on the
 * command line
 */
@Component
public class AppRunner implements CommandLineRunner {
    @Autowired
    private MbtaClient mbtaClient;

    @Override
    public void run(String... args) {
        System.out.println("1. List of commuter Rail Routes");
        List<String> commuterRailRoutes = mbtaClient.getCommuterRailRoutes();
        if(commuterRailRoutes != null){
            commuterRailRoutes.forEach(System.out::println);
        }

        System.out.println();
        System.out.println();
        System.out.println("2. Stops on Framingham/Worcester Line");
        List<String> stopsByLine = mbtaClient.listStopsByLine("Framingham/Worcester Line");
        if(stopsByLine != null){
            stopsByLine.forEach(System.out::println);
        }
//sstat: SOUTH STATION qnctr: QUINCY STATION
        System.out.println();
        System.out.println();
        System.out.println("3. Lines going through both stops");
        List<String> response = mbtaClient.findCommuterLinesBetweenStops("place-sstat", "place-qnctr");
        if(response!=null){
            if(response.isEmpty()){
                System.out.println("No lines found connecting the two stops");
            } else{
                response.forEach(System.out::println);
            }
        }
    }
}
