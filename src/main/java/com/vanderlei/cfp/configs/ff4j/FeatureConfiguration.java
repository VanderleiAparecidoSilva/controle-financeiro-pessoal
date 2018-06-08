package com.vanderlei.cfp.configs.ff4j;

import org.ff4j.core.Feature;

public enum FeatureConfiguration {

    IMPRIMIR_LOG("IMPRIMIR_LOG", "Imprimir log no controller", "CFP-APP", false);

    private String key;
    private String description;
    private String groupName;
    private boolean defaultValue;

    FeatureConfiguration() {
    }

    FeatureConfiguration(String key, String description, String groupName, boolean defaultValue) {
        this.key = key;
        this.description = description;
        this.groupName = groupName;
        this.defaultValue = defaultValue;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public Feature feature() {
        return new Feature(
                this.getKey(),
                this.isDefaultValue(),
                this.getDescription(),
                this.getGroupName());
    }
}