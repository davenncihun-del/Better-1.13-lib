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
    private final JsonNode advancedRules;
    private final MigrationContext context;

    public MigrationEngine(MigrationContext context) {
        this.context = context;
        this.mappingsRules = RuleLoader.loadMappings(context);
        this.blocksRules = RuleLoader.loadBlocks(context);
        this.eventsRules = RuleLoader.loadEvents(context);
        this.advancedRules = RuleLoader.loadAdvancedRules(context);
    }
    
    public MigrationEngine() {
        this(new MigrationContext("1.12.2", "1.13.2", "forge"));
    }

    /**
     * Translates a legacy Java source code string into a target structure.
     * @param sourceCode The original source code.
     * @return The migrated source code.
     */
    public String migrateCode(String sourceCode) {
        // Parse the raw legacy Java source code into an abstract token hierarchy
        CompilationUnit cu = StaticJavaParser.parse(sourceCode);

        // Sequential Compilation Passes

        // Pass 1: Handle advanced JSON rules (flattening/unflattening)
        cu.accept(new com.better113.visitors.AdvancedMethodVisitor(), advancedRules);

        // Pass 2: Translate obfuscated method targets
        cu.accept(new MethodTranslationVisitor(), mappingsRules);

        // Pass 3: Resolve multi-to-one block state logic with metadata argument checks
        cu.accept(new BlockStateVisitor(), blocksRules);

        // Pass 4: Generate new event-driven subscriber classes (replace obsolete initialization loops)
        cu.accept(new LifecycleEventVisitor(), eventsRules);

        return cu.toString();
    }
}
