package com.betterversions.migrations.v1_17_to_1_18;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_17To1_18Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.17.1";
    }

    @Override
    public String getTargetVersion() {
        return "1.18.2";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.17.1 to 1.18.2 migration logic
        System.out.println("Applying 1.17.1 to 1.18.2 Migration Module...");
    }
}
