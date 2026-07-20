package com.betterversions.migrations.v1_12_to_1_13.rules;

import com.betterversions.engine.MigrationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class RuleLoader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode loadMappings(MigrationContext context) {
        return loadJson("rules/mappings.json");
    }

    public static JsonNode loadBlocks(MigrationContext context) {
        return loadJson("rules/blocks.json");
    }

    public static JsonNode loadEvents(MigrationContext context) {
        return loadJson("rules/events.json");
    }

    public static JsonNode loadAdvancedRules(MigrationContext context) {
        String filename = context.getSourceVersion() + "_to_" + context.getTargetVersion() + ".json";
        return loadJson("rules/" + filename);
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
