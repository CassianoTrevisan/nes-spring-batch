package com.batch.anomaly;

import com.batch.model.AnomalyConfig;
import com.batch.model.Measurement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
@Qualifier("valueToHighAnomalyDetector")
public class ValueToHighAnomalyDetector implements AnomalyDetector {

    private static final String ANOMALY_NAME = "ValueToHigh";

    /**
     * Compares a element of measurement against anomaly parameters.
     * This implementation of Anomaly Detector compares 'measuredValue'
     * and the parent id pattern in order to determinate whether there
     * is a anomaly.
     * @param element
     * @param anomalyConfigs
     * @return a list measurable elements.
     */
    @Override
    public Measurement detect(Measurement element, List<AnomalyConfig> anomalyConfigs) {

        Measurement measurementReturn = null;

        for (AnomalyConfig anomalyConfig : anomalyConfigs) {
            // check measuredValue != null (in case of a template or value error in json the file
            if (element.getMeasuredValue() != null && element.getMeasuredValue() > anomalyConfig.getLimit()) {
                // check whether the evaluated element parentId matches with the anomaly parentPattern
                boolean matches = Pattern.matches(anomalyConfig.getParentPattern(), element.getParentId());
                if(matches){
                    element.setAnomalyType(ANOMALY_NAME);
                    measurementReturn = element;
                }
            }
        }
        return measurementReturn;
    }
}
