package com.rishabh.mbta.model.routes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RouteAttribute {
    private String color;
    private String description;
    @JsonProperty("fare_class")
    private String fareClass;
    @JsonProperty("long_name")
    private String longName;
    @JsonProperty("short_name")
    private String shortName;
    @JsonProperty("sort_order")
    private String sortOrder;
    @JsonProperty("text_color")
    private String textColor;
    private int type;
}
