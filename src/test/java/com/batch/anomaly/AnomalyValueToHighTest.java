package com.batch.anomaly;

import com.batch.model.AnomalyConfig;
import com.batch.model.Measurement;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AnomalyValueToHighTest {

    @Test
    public void findOneAnomaly(){
        AnomalyDetector anomalyDetector = new ValueToHighAnomalyDetector();

        Measurement measurementJson = new Measurement();
        measurementJson.setMeasuredValue(200D);
        measurementJson.setParentId("DC1");

        List<AnomalyConfig> anomalyConfigList = new ArrayList<>();

        AnomalyConfig anomalyConfig1 = new AnomalyConfig();
        anomalyConfig1.setLimit(100D);
        anomalyConfig1.setParentPattern("D.*1");
        AnomalyConfig anomalyConfig2 = new AnomalyConfig();
        anomalyConfig2.setParentPattern("DC2");
        anomalyConfig2.setLimit(150D);

        anomalyConfigList.add(anomalyConfig1);
        anomalyConfigList.add(anomalyConfig2);

        List<Measurement> detectedAnomalies = anomalyDetector.detect(measurementJson, anomalyConfigList);

        assertThat(detectedAnomalies).size().isEqualTo(1);
    }
}
