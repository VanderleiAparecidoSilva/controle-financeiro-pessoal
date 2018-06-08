package com.vanderlei.cfp.configs.ff4j;

import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class FeatureLoader {

    private final FF4j ff4j;

    @Autowired
    public FeatureLoader(final FF4j ff4j) {
        this.ff4j = ff4j;
    }

    @PostConstruct
    public void load() {
        Arrays.stream(FeatureConfiguration.values())
                .filter(featureConfiguration -> !ff4j.exist(featureConfiguration.getKey()))
                .forEach(featureConfiguration -> ff4j.createFeature(featureConfiguration.feature()));
    }
}