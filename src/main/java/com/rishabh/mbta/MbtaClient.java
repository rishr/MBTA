package com.rishabh.mbta;

import com.rishabh.mbta.model.lines.Line;
import com.rishabh.mbta.model.lines.LineRelationshipRoute;
import com.rishabh.mbta.model.lines.LineResponse;
import com.rishabh.mbta.model.lines.LineResponseSingle;
import com.rishabh.mbta.model.routes.Route;
import com.rishabh.mbta.model.routes.RouteResponse;
import com.rishabh.mbta.model.stops.StopsResponseSingle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Client class for calling MBTA APIs
 */
@Component
@Slf4j
public class MbtaClient {
    @Value("${mbta.url.endpoints.routes}")
    private String routeUrl;

    @Value("${mbta.url.endpoints.lines}")
    private String linesUrl;

    @Value("${mbta.url.endpoints.stops}")
    private String stopsUrl;

    /**
     * Calling the /routes api and filtering by type 2 to only receive a list of commuter rail routes
     *
     * @return Returns a list of strings with the long names of the filtered routes
     */
    //https://api-v3.mbta.com/routes?filter[type]=2
    public List<String> getCommuterRailRoutes(){
        RestTemplate restTemplate = new RestTemplate();
        RouteResponse routeResponse;
        try {
            routeResponse = restTemplate.getForObject(routeUrl + "?filter[type]=2", RouteResponse.class);
        } catch (Exception e){
            log.error("Error while calling routes API", e);
            return null;
        }
        List<String> response = new ArrayList<>();
        for (Route route : routeResponse.getData()) {
            response.add(route.getAttributes().getLongName());
        }

        return response;
    }

    /**
     * Calling the lines API to get a list of all the lines, getting the line id against the input long name of the line
     * Calling the lines API with the line IP along with a request for including the routes associated
     * Getting the stops against the filtered routes from the /stops API
     *
     * @param longName Long name of the line
     * @return Returns a list of string with the names of the stops and their addresses
     */
    //https://api-v3.mbta.com/stops?filter[route]=CR-Worcester,Shuttle-Generic-Worcester
    public List<String> listStopsByLine(String longName){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LineResponse> lineResponseEntity;
        try {
            lineResponseEntity = restTemplate.getForEntity(linesUrl, LineResponse.class);
        } catch (Exception e){
            log.error("Error while calling lines API", e);
            return null;
        }

        LineResponse lineResponse = lineResponseEntity.getBody();
        List<Line> lineList = lineResponse.getData().stream()
                .filter(line->line.getAttributes().getLongName().equalsIgnoreCase(longName))
                .collect(Collectors.toList());
        if(lineList.isEmpty()){
            log.error("Could not find the line corresponding to " + longName);
            return null;
        }

        String lineId = lineList.get(0).getId();


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(linesUrl + "/" + lineId)
                .queryParam("include", "routes");
        ResponseEntity<LineResponseSingle> lineResponseSingleEntity;
        try {
            lineResponseSingleEntity=
                    restTemplate.getForEntity(builder.toUriString(),
                            LineResponseSingle.class);
        } catch (Exception e){
            log.error("Error while calling lines API", e);
            return null;
        }

        LineResponseSingle lineResponseSingle = lineResponseSingleEntity.getBody();


        List<String> routeIdsList = lineResponseSingle.getData().getRelationships().getRoutes()
                .getData().stream().map(LineRelationshipRoute::getId).collect(Collectors.toList());

        String routeIds = routeIdsList.toString().replace(" ", "")
                .replace("[", "").replace("]", "");

        StopsResponseSingle stopsResponseSingle;
        try {
            stopsResponseSingle=
                    restTemplate.getForObject(stopsUrl+"?filter[route]="+routeIds,
                            StopsResponseSingle.class);
        } catch (Exception e){
            log.error("Error while calling stops API", e);
            return null;
        }

        return stopsResponseSingle.getData().stream().map(data->
            data.getAttributes().getName() + "; Address: " + data.getAttributes().getAddress()
        ).collect(Collectors.toList());
    }

    /**
     * Calling the /routes API twice, filtered by the stop name and finding the common routes and extracting
     * the line information
     *
     * @param stopId1 ID for the first stop
     * @param stopId2 ID for the second stop
     * @return Returns a list of strings containing the long names of the stops
     */
    //https://api-v3.mbta.com/routes?filter[stop]=place-north&filter[type]=2
    public List<String> findCommuterLinesBetweenStops(String stopId1, String stopId2){
        RestTemplate restTemplate = new RestTemplate();
        RouteResponse route1Response;
        try {
            route1Response = restTemplate.getForObject(routeUrl + "?filter[type]=2&filter[stop]="+stopId1, RouteResponse.class);
        } catch (Exception e){
            log.error("Error while calling routes API", e);
            return null;
        }

        RouteResponse route2Response;
        try {
            route2Response = restTemplate.getForObject(routeUrl + "?filter[type]=2&filter[stop]="+stopId2, RouteResponse.class);
        } catch (Exception e){
            log.error("Error while calling routes API", e);
            return null;
        }

        List<String> intersectingLineIdList = new LinkedList<>();
        route1Response.getData().forEach(route1Data->{
                List<Route> intersection = route2Response.getData().stream().filter(route2Data->
                    route1Data.getRelationships().getLine().getData().getId()
                            .equalsIgnoreCase(route2Data.getRelationships().getLine().getData().getId()))
                    .collect(Collectors.toList());

            if(!intersection.isEmpty())
                intersectingLineIdList.add(intersection.get(0).getRelationships().getLine().getData().getId());
        });



        if (intersectingLineIdList.isEmpty()){
            return new ArrayList<>();
        }

        String ids = intersectingLineIdList.toString().replace(" ", "")
                .replace("[", "").replace("]", "");

        LineResponse lineResponse;
        try {
            lineResponse=
                    restTemplate.getForObject(linesUrl + "?filter[id]=" + ids,
                            LineResponse.class);
        } catch (Exception e){
            log.error("Error while calling lines API", e);
            return null;
        }

        List<String> response = new ArrayList<>();
        lineResponse.getData().forEach(data->{
            response.add(data.getAttributes().getLongName());
        });

        return response;
    }
}
