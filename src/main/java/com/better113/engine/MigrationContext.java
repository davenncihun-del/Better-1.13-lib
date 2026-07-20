package com.better113.engine;

public class MigrationContext {
    private final String sourceVersion;
    private final String targetVersion;
    private final String loader;

    public MigrationContext(String sourceVersion, String targetVersion, String loader) {
        this.sourceVersion = sourceVersion;
        this.targetVersion = targetVersion;
        this.loader = loader;
    }

    public String getSourceVersion() {
        return sourceVersion;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public String getLoader() {
        return loader;
    }

    public String getDirection() {
        if ("1.13.2".equals(sourceVersion) && "1.12.2".equals(targetVersion)) {
            return "backward";
        }
        return "forward";
    }

    public boolean isDownport() {
        return "backward".equals(getDirection());
    }
}
