package com.better113.cli;

import com.better113.api.MigrationManager;
import com.better113.engine.MigrationContext;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "better1.13lib", mixinStandardHelpOptions = true, version = "better1.13lib 1.0", description = "Migrates Minecraft 1.12.2 source files to 1.13+ flattening structure.")
public class Better113CLI implements Callable<Integer> {

    @Parameters(index = "0", description = "The legacy 1.12.2 source file or directory to translate.")
    private File source;

    @Option(names = { "-o", "--output" }, description = "Output directory for the migrated files.", required = true)
    private File outputDir;

    @Option(names = { "-f", "--from" }, description = "Source Minecraft version.", defaultValue = "1.12.2")
    private String fromVersion;

    @Option(names = { "-t", "--to" }, description = "Target Minecraft version.", defaultValue = "1.13.2")
    private String toVersion;

    @Override
    public Integer call() throws Exception {
        if (!source.exists()) {
            System.err.println("Error: Source path '" + source.getAbsolutePath() + "' does not exist.");
            return 1;
        }

        if (!outputDir.exists()) {
            System.out.println("Creating target output directory: " + outputDir.getPath());
            outputDir.mkdirs();
        }

        MigrationContext context = new MigrationContext(fromVersion, toVersion, "forge");
        MigrationManager manager = new MigrationManager(context);

        if (source.isDirectory()) {
            manager.migrateDirectory(source.toPath(), outputDir.toPath());
        } else {
            manager.migrateFile(source.toPath(), outputDir.toPath(), source.getParentFile().toPath());
        }

        System.out.println("Migration complete successfully!");
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Better113CLI()).execute(args);
        System.exit(exitCode);
    }
}