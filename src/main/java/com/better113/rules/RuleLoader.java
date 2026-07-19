package com.better113.rules;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class RuleLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode loadMappings() {
        return loadJson("rules/mappings.json");
    }

    public static JsonNode loadBlocks() {
        return loadJson("rules/blocks.json");
    }

    public static JsonNode loadEvents() {
        return loadJson("rules/events.json");
    }

    private static JsonNode loadJson(String resourcePath) {
        try (InputStream is = RuleLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("Could not find resource: " + resourcePath);
            }
            return mapper.readTree(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON from " + resourcePath, e);
        }
    }
}
