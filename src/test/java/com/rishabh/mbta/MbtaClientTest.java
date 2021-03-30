package com.rishabh.mbta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Testing the methods as a whole in the interest of time
 */
@SpringBootTest
class MbtaClientTest {

    @Autowired
    private MbtaClient mbtaClient;


// checking if we are not getting any empty result
    @Test
    public void testGetCommuterRailRoutes(){
        List<String> result = mbtaClient.getCommuterRailRoutes();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
    }


//checking for number of stops which is 18 on this framingham/worchester line
    @Test
    public void testListStopsByLine(){
        List<String> result = mbtaClient.listStopsByLine("Framingham/Worcester Line");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 18);
    }

//we are checking 2 test cases here:
//    1)  we are checking if there is a stop between quincy and south station
//    2) There is no direct train between North station and South station
    @Test
    public void testLinesBetweenStops(){
        List<String> expectedResult = List.of("Greenbush Line", "Kingston/Plymouth Line", "Middleborough/Lakeville Line");
        List<String> response = mbtaClient.findCommuterLinesBetweenStops("place-sstat", "place-qnctr");
        Assertions.assertNotNull(response, "Could not get a valid response");
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(response, expectedResult);

        response = mbtaClient.findCommuterLinesBetweenStops("place-north", "place-sstat");
        Assertions.assertNotNull(response, "Could not get a valid response");
        Assertions.assertTrue(response.isEmpty());
    }

}