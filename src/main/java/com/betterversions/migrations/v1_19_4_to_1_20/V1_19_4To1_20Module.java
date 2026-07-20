package com.betterversions.migrations.v1_19_4_to_1_20;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_19_4To1_20Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.19.4";
    }

    @Override
    public String getTargetVersion() {
        return "1.20.1";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.19.4 to 1.20.1 migration logic
        System.out.println("Applying 1.19.4 to 1.20.1 Migration Module...");
    }
}
