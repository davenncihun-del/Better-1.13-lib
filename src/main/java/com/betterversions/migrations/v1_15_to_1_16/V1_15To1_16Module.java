package com.betterversions.migrations.v1_15_to_1_16;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_15To1_16Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.15.2";
    }

    @Override
    public String getTargetVersion() {
        return "1.16.5";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.15.2 to 1.16.5 migration logic
        System.out.println("Applying 1.15.2 to 1.16.5 Migration Module...");
    }
}
