package com.rishabh.mbta.model.lines;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LineAttribute {
    private String color;
    @JsonProperty("long_name")
    private String longName;
    @JsonProperty("short_name")
    private String shortName;
}
