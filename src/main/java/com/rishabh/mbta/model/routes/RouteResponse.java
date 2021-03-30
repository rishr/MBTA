package com.rishabh.mbta.model.routes;

import lombok.Data;

import java.util.List;

@Data
public class RouteResponse {
    private List<Route> data;
}
