package com.rishabh.mbta.model.routes;

import lombok.Data;

@Data
public class Route {
    private RouteAttribute attributes;
    private String id;
    private String type;
    private RouteRelationships relationships;
}
