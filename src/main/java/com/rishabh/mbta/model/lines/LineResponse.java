package com.rishabh.mbta.model.lines;

import lombok.Data;

import java.util.List;

@Data
public class LineResponse {
    private List<Line> data;
}
