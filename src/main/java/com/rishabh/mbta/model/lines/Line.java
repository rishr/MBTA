package com.rishabh.mbta.model.lines;

import lombok.Data;

@Data
public class Line {
    private LineAttribute attributes;
    private String id;
    private LineRelationship relationships;
}
