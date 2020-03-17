package com.batch.services;


import com.batch.anomaly.AnomalyDetector;
import com.batch.model.AnomalyConfig;
import com.batch.model.Measurement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessMeasurementsService {


    @Autowired
    @Qualifier("valueToHighAnomalyDetector")
    private AnomalyDetector anomalyDetector;

    public Measurement processMeasurements(final List<AnomalyConfig> anomalyConfigs, Measurement measurement) {
        return anomalyDetector.detect(measurement, anomalyConfigs);
    }


}
