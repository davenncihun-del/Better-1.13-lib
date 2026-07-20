package com.betterversions.engine;

import com.github.javaparser.ast.CompilationUnit;
import java.util.HashMap;
import java.util.Map;

public class MigrationContext {
    private final String sourceVersion;
    private final String targetVersion;
    private final String loader;
    private CompilationUnit currentCompilationUnit;
    private final Map<String, Object> moduleState = new HashMap<>();

    public MigrationContext(String sourceVersion, String targetVersion, String loader) {
        this.sourceVersion = sourceVersion;
        this.targetVersion = targetVersion;
        this.loader = loader;
    }

    public String getSourceVersion() { return sourceVersion; }
    public String getTargetVersion() { return targetVersion; }
    public String getLoader() { return loader; }
    
    public CompilationUnit getCompilationUnit() {
        return currentCompilationUnit;
    }

    public void setCompilationUnit(CompilationUnit cu) {
        this.currentCompilationUnit = cu;
    }

    public void putState(String key, Object value) {
        moduleState.put(key, value);
    }

    public Object getState(String key) {
        return moduleState.get(key);
    }
}
