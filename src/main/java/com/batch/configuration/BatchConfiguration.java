package com.batch.configuration;

import com.batch.model.Measurement;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd-HH-mm-ss";
    public static final String CSV_FILE_FORMAT = ".csv";
    public static final String ANOMALY_TYPE = "anomalyType";
    public static final String DEVICE_ID = "deviceId";
    public static final String MEASURED_VALUE = "measuredValue";
    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public JsonItemReader<Measurement> reader() {
        JsonItemReader<Measurement> delegate = new JsonItemReaderBuilder<Measurement>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Measurement.class))
                .name("measurementItemReader")
                .resource(new FileSystemResource(""))// mandatory workaround :(
                .build();

        return delegate;
    }

    @Bean
    public MultiResourceItemReader<Measurement> multiResourceReader() {
        return new MultiResourceItemReaderBuilder<Measurement>()
                .name("multiResourceReader")
                .delegate(reader())
                .resources(ParametersHelper.getAllResources())
                .build();
    }

    @Bean
    public AnomalyProcessor processor() {
        return new AnomalyProcessor();
    }

    @Bean
    public FlatFileItemWriter<Measurement> writer() {
        FlatFileItemWriter<Measurement> writer = new FlatFileItemWriter<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
        writer.setResource(new FileSystemResource(ParametersHelper.getOutputPath() + LocalDateTime.now().format(formatter).toString() + CSV_FILE_FORMAT));

        DelimitedLineAggregator<Measurement> aggregator = new DelimitedLineAggregator<>();
        aggregator.setDelimiter(",");

        BeanWrapperFieldExtractor extractor = new BeanWrapperFieldExtractor();
        extractor.setNames(new String[]{ANOMALY_TYPE, DEVICE_ID, MEASURED_VALUE});
        aggregator.setFieldExtractor(extractor);

        writer.setLineAggregator(aggregator);

        return writer;
    }

    @Bean
    public Job job(Step step1) {
        return jobBuilderFactory.get("job")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Measurement, Measurement>chunk(10)
                .reader(multiResourceReader())
                .processor(processor())
                .writer(writer())
                .build();
    }

}
