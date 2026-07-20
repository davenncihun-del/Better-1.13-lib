package com.betterversions.migrations.v1_19_to_1_19_3;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_19To1_19_3Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.19";
    }

    @Override
    public String getTargetVersion() {
        return "1.19.3";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.19 to 1.19.3 migration logic
        System.out.println("Applying 1.19 to 1.19.3 Migration Module...");
    }
}
