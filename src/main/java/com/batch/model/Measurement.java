package com.batch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Measurement {
    private String anomalyType;
    private String parentId,deviceId;
    private Double measuredValue;
    private String timestamp;
}
