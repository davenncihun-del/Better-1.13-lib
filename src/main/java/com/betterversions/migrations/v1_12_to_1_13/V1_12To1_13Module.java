package com.betterversions.migrations.v1_12_to_1_13;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;
import com.betterversions.migrations.v1_12_to_1_13.rules.RuleLoader;
import com.betterversions.migrations.v1_12_to_1_13.visitors.AdvancedMethodVisitor;
import com.betterversions.migrations.v1_12_to_1_13.visitors.BlockStateVisitor;
import com.betterversions.migrations.v1_12_to_1_13.visitors.LifecycleEventVisitor;
import com.betterversions.migrations.v1_12_to_1_13.visitors.MethodTranslationVisitor;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.javaparser.ast.CompilationUnit;

public class V1_12To1_13Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.12.2";
    }

    @Override
    public String getTargetVersion() {
        return "1.13.2";
    }

    @Override
    public void apply(MigrationContext context) {
        CompilationUnit cu = context.getCompilationUnit();
        if (cu == null) return;

        JsonNode mappingsRules = RuleLoader.loadMappings(context);
        JsonNode blocksRules = RuleLoader.loadBlocks(context);
        JsonNode eventsRules = RuleLoader.loadEvents(context);
        JsonNode advancedRules = RuleLoader.loadAdvancedRules(context);

        cu.accept(new AdvancedMethodVisitor(), advancedRules);
        cu.accept(new MethodTranslationVisitor(), mappingsRules);
        cu.accept(new BlockStateVisitor(), blocksRules);
        cu.accept(new LifecycleEventVisitor(), eventsRules);
    }
}
