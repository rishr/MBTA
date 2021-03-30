package com.rishabh.mbta.model.stops;

import com.rishabh.mbta.model.lines.Line;
import lombok.Data;

import java.util.List;

@Data
public class StopsResponseSingle {
    private List<Stop> data;
}
