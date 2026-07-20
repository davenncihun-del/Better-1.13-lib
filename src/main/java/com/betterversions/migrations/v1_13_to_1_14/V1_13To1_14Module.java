package com.betterversions.migrations.v1_13_to_1_14;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_13To1_14Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.13.2";
    }

    @Override
    public String getTargetVersion() {
        return "1.14.4";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.13.2 to 1.14.4 migration logic
        System.out.println("Applying 1.13.2 to 1.14.4 Migration Module...");
    }
}
