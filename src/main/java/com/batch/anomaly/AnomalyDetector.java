package com.batch.anomaly;

import com.batch.model.AnomalyConfig;
import com.batch.model.Measurement;

import java.util.List;

public interface AnomalyDetector {
    public Measurement detect(Measurement element, List<AnomalyConfig> anomalyConfigs);
}
