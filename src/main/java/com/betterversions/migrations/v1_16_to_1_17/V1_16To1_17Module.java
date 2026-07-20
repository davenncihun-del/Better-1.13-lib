package com.betterversions.migrations.v1_16_to_1_17;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_16To1_17Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.16.5";
    }

    @Override
    public String getTargetVersion() {
        return "1.17.1";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.16.5 to 1.17.1 migration logic
        System.out.println("Applying 1.16.5 to 1.17.1 Migration Module...");
    }
}
