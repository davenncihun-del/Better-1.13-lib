package com.better113.engine;

import com.better113.rules.RuleLoader;
import com.better113.visitors.BlockStateVisitor;
import com.better113.visitors.LifecycleEventVisitor;
import com.better113.visitors.MethodTranslationVisitor;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class MigrationEngine {

    private final JsonNode mappingsRules;
    private final JsonNode blocksRules;
    private final JsonNode eventsRules;

    public MigrationEngine() {
        this.mappingsRules = RuleLoader.loadMappings();
        this.blocksRules = RuleLoader.loadBlocks();
        this.eventsRules = RuleLoader.loadEvents();
    }

    /**
     * Translates a legacy 1.12.2 Java source code string into a 1.13 structure.
     * @param sourceCode The original 1.12.2 source code.
     * @return The migrated 1.13 source code.
     */
    public String migrateCode(String sourceCode) {
        // Parse the raw legacy Java source code into an abstract token hierarchy
        CompilationUnit cu = StaticJavaParser.parse(sourceCode);

        // Sequential Compilation Passes

        // Pass 1: Translate obfuscated method targets
        cu.accept(new MethodTranslationVisitor(), mappingsRules);

        // Pass 2: Resolve multi-to-one block state logic with metadata argument checks
        cu.accept(new BlockStateVisitor(), blocksRules);

        // Pass 3: Generate new event-driven subscriber classes (replace obsolete initialization loops)
        cu.accept(new LifecycleEventVisitor(), eventsRules);

        return cu.toString();
    }
}
