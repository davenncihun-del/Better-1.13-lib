package com.betterversions.engine;

import com.betterversions.api.MigrationModule;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

public class MigrationEngine {

    private final MigrationContext context;
    private final List<MigrationModule> modules = new ArrayList<>();

    public MigrationEngine(MigrationContext context) {
        this.context = context;
    }
    
    public MigrationEngine() {
        this(new MigrationContext("1.12.2", "1.13.2", "forge"));
    }

    public void addModule(MigrationModule module) {
        this.modules.add(module);
    }

    /**
     * Translates a legacy Java source code string into a target structure by chaining modules.
     * @param sourceCode The original source code.
     * @return The migrated source code.
     */
    public String migrateCode(String sourceCode) {
        // Parse the raw legacy Java source code into an abstract token hierarchy
        CompilationUnit cu = StaticJavaParser.parse(sourceCode);
        context.setCompilationUnit(cu);

        // Apply all chained modules sequentially
        for (MigrationModule module : modules) {
            module.apply(context);
        }

        return cu.toString();
    }
}
