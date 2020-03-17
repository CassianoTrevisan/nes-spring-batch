package com.batch.configuration;

import com.batch.model.Measurement;
import com.batch.services.ConfigLoaderService;
import com.batch.services.ProcessMeasurementsService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class AnomalyProcessor implements ItemProcessor<Measurement, Measurement> {

    @Autowired
    private ProcessMeasurementsService processMeasurementsService;

    @Autowired
    private ConfigLoaderService configLoaderService;

    @Override
    public Measurement process(Measurement measurement) {
        return processMeasurementsService.processMeasurements(configLoaderService.loadConfigFile(), measurement);
    }
}
