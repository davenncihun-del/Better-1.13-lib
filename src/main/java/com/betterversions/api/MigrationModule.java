package com.betterversions.api;

import com.betterversions.engine.MigrationContext;

/**
 * Represents a discrete step in migrating code or data from one Minecraft version to another.
 * Modules are designed to be chained together to perform multi-step migrations.
 */
public interface MigrationModule {
    
    /**
     * @return The starting version of this migration module (e.g., "1.12.2")
     */
    String getSourceVersion();

    /**
     * @return The ending version of this migration module (e.g., "1.13.2")
     */
    String getTargetVersion();

    /**
     * Registers the visitors, rules, and mappings required for this specific migration step.
     * 
     * @param context The migration context for tracking state across modules.
     */
    void apply(MigrationContext context);
}
