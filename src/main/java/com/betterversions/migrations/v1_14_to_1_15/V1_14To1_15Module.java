package com.betterversions.migrations.v1_14_to_1_15;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_14To1_15Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.14.4";
    }

    @Override
    public String getTargetVersion() {
        return "1.15.2";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.14.4 to 1.15.2 migration logic
        System.out.println("Applying 1.14.4 to 1.15.2 Migration Module...");
    }
}
