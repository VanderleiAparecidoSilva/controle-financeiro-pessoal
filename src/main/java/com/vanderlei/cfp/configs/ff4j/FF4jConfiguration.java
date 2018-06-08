package com.vanderlei.cfp.configs.ff4j;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.ff4j.FF4j;
import org.ff4j.core.FeatureStore;
import org.ff4j.mongo.store.FeatureStoreMongo;
import org.ff4j.mongo.store.PropertyStoreMongo;
import org.ff4j.property.store.PropertyStore;
import org.ff4j.web.ApiConfig;
import org.ff4j.web.FF4jDispatcherServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@Profile("!test")
public class FF4jConfiguration {

    private static final String[] DEFAULT_CONSOLE = new String[]{"/ff4j-console/*", "/ff4j-console"};

    @Bean
    public ApiConfig getApiConfig(final FF4j ff4j) {
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setAuthenticate(false);
        apiConfig.setAutorize(false);
        apiConfig.setFF4j(ff4j);
        return apiConfig;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(final FF4j ff4j) {
        final FF4jDispatcherServlet ff4jDispatcherServlet = new FF4jDispatcherServlet();
        ff4jDispatcherServlet.setFf4j(ff4j);
        return new ServletRegistrationBean(ff4jDispatcherServlet, DEFAULT_CONSOLE);
    }

    @Bean
    public FF4j fF4j(final FeatureStore featureStore, final PropertyStore propertyStore) {
        final FF4j ff4j = new FF4j();
        ff4j.setFeatureStore(featureStore);
        ff4j.setPropertiesStore(propertyStore);
        return ff4j;
    }

    @Bean
    public FeatureStore featureStore(final MongoClient mongoClient, final MongoTemplate mongoTemplate) {
        final MongoDatabase mongoDatabase = mongoClient.getDatabase(mongoTemplate.getDb().getName());
        return new FeatureStoreMongo(mongoDatabase, "ff4j-features");
    }

    @Bean
    public PropertyStore propertyStore(final MongoClient mongoClient, final MongoTemplate mongoTemplate) {
        final MongoDatabase mongoDatabase = mongoClient.getDatabase(mongoTemplate.getDb().getName());
        return new PropertyStoreMongo(mongoDatabase, "ff4j-properties");
    }
}