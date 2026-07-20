package com.betterversions.api;

import com.betterversions.engine.MigrationContext;
import com.betterversions.engine.MigrationEngine;
import com.betterversions.migrations.v1_12_to_1_13.V1_12To1_13Module;
import com.betterversions.migrations.v1_13_to_1_14.V1_13To1_14Module;
import com.betterversions.migrations.v1_14_to_1_15.V1_14To1_15Module;
import com.betterversions.migrations.v1_15_to_1_16.V1_15To1_16Module;
import com.betterversions.migrations.v1_16_to_1_17.V1_16To1_17Module;
import com.betterversions.migrations.v1_17_to_1_18.V1_17To1_18Module;
import com.betterversions.migrations.v1_19_to_1_19_3.V1_19To1_19_3Module;
import com.betterversions.migrations.v1_19_4_to_1_20.V1_19_4To1_20Module;
import com.betterversions.migrations.v1_20_4_to_1_21.V1_20_4To1_21Module;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MigrationManager {
    private final MigrationEngine engine;

    // A static registry of all available modules in sequential order
    private static final List<MigrationModule> REGISTRY = new ArrayList<>();
    
    static {
        REGISTRY.add(new V1_12To1_13Module());
        REGISTRY.add(new V1_13To1_14Module());
        REGISTRY.add(new V1_14To1_15Module());
        REGISTRY.add(new V1_15To1_16Module());
        REGISTRY.add(new V1_16To1_17Module());
        REGISTRY.add(new V1_17To1_18Module());
        REGISTRY.add(new V1_19To1_19_3Module());
        REGISTRY.add(new V1_19_4To1_20Module());
        REGISTRY.add(new V1_20_4To1_21Module());
    }

    public MigrationManager(MigrationContext context) {
        this.engine = new MigrationEngine(context);
        
        // Setup modules based on context source and target versions dynamically
        boolean capturing = false;
        for (MigrationModule module : REGISTRY) {
            // Start capturing when we hit the source version
            if (module.getSourceVersion().equals(context.getSourceVersion())) {
                capturing = true;
            }
            
            if (capturing) {
                this.engine.addModule(module);
            }
            
            // Stop capturing once we hit the target version
            if (module.getTargetVersion().equals(context.getTargetVersion())) {
                break;
            }
        }
    }

    public String migrateSource(String sourceCode) {
        return engine.migrateCode(sourceCode);
    }
    
    public void migrateDirectory(Path input, Path output) {
        try {
            Files.walk(input)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(file -> {
                    try {
                        String sourceCode = Files.readString(file);
                        String migrated = migrateSource(sourceCode);
                        
                        Path relative = input.relativize(file);
                        Path target = output.resolve(relative);
                        
                        Files.createDirectories(target.getParent());
                        Files.writeString(target, migrated);
                    } catch (Exception e) {
                        System.err.println("Failed to migrate file: " + file + " - " + e.getMessage());
                    }
                });
        } catch (Exception e) {
            System.err.println("Failed to migrate directory: " + e.getMessage());
        }
    }
    
    public void migrateFile(Path input, Path outputRoot, Path inputRoot) {
        try {
            String sourceCode = Files.readString(input);
            String migrated = migrateSource(sourceCode);
            
            Path relative = inputRoot.relativize(input);
            Path target = outputRoot.resolve(relative);
            
            Files.createDirectories(target.getParent());
            Files.writeString(target, migrated);
        } catch (Exception e) {
            System.err.println("Failed to migrate file: " + input + " - " + e.getMessage());
        }
    }
}
