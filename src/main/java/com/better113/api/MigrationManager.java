package com.better113.api;

import com.better113.engine.MigrationContext;
import com.better113.engine.MigrationEngine;

import java.nio.file.Files;
import java.nio.file.Path;

public class MigrationManager {
    private final MigrationEngine engine;

    public MigrationManager(MigrationContext context) {
        this.engine = new MigrationEngine(context);
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
