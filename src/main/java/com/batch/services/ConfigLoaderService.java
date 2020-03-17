package com.batch.services;

import com.batch.configuration.ParametersHelper;
import com.batch.model.AnomalyConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
@Service
public class ConfigLoaderService {

    public List<AnomalyConfig> loadConfigFile() {

        List<AnomalyConfig> anomalyConfigsList = new ArrayList<>();

        try (JsonReader jsonReader = new JsonReader(
                new InputStreamReader(
                        new FileInputStream(ParametersHelper.getAnomalyConfigFilePath()), StandardCharsets.UTF_8))) {

            Gson gson = new GsonBuilder().create();

            jsonReader.beginArray(); //start of json array

            while (jsonReader.hasNext()) { //next json array element
                anomalyConfigsList.add(gson.fromJson(jsonReader, AnomalyConfig.class));
            }

            jsonReader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return anomalyConfigsList;
    }
}
