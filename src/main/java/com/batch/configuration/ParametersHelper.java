package com.batch.configuration;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ParametersHelper {
    private static String sourceFilesDirectory = getPropertyValue("files.directory");
    private static String anomalyConfigFilePath = getPropertyValue("anomaly.file.path");
    private static String outputPath = getPropertyValue("output.path");

    public static String getOutputPath() { return outputPath; }

    public static String getSourceFilesDirectory() {
        return sourceFilesDirectory;
    }

    public static String getAnomalyConfigFilePath(){
        return anomalyConfigFilePath;
    }

    public static Resource[] getAllResources() {

        Resource[] resources = null;
        try (Stream<Path> walk = Files.walk(Paths.get(sourceFilesDirectory))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            resources = new FileSystemResource[result.size()];
            for (int i = 0; i < resources.length; i++) {
                resources[i] = new FileSystemResource(result.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return resources;
    }

    private static String getPropertyValue(String key) {
        Properties prop = null;
        try (InputStream input = ParametersHelper.class.getClassLoader().getResourceAsStream("application.properties")) {

            prop = new Properties();
            //load a properties file from class path, inside static method
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prop.getProperty(key);
    }

}
