package ru.graduation.voting.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ru.graduation.voting.util.json.JsonUtil;

import javax.annotation.PostConstruct;

@Configuration
public class WebConfig {

    private final ObjectMapper objectMapper;

    @Autowired
    public WebConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void setObjectMapper() {
        JsonUtil.setObjectMapper(objectMapper);
    }
}
