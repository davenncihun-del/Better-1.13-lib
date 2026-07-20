package com.betterversions.migrations.v1_20_4_to_1_21;

import com.betterversions.api.MigrationModule;
import com.betterversions.engine.MigrationContext;

public class V1_20_4To1_21Module implements MigrationModule {

    @Override
    public String getSourceVersion() {
        return "1.20.4";
    }

    @Override
    public String getTargetVersion() {
        return "1.21";
    }

    @Override
    public void apply(MigrationContext context) {
        // Placeholder for 1.20.4 to 1.21 migration logic
        System.out.println("Applying 1.20.4 to 1.21 Migration Module...");
    }
}
