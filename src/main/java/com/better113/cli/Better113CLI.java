package com.better113.cli;

import com.better113.engine.MigrationEngine;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "better1.13lib", mixinStandardHelpOptions = true, version = "better1.13lib 1.0", description = "Migrates Minecraft 1.12.2 source files to 1.13+ flattening structure.")
public class Better113CLI implements Callable<Integer> {

    @Parameters(index = "0", description = "The legacy 1.12.2 source file or directory to translate.")
    private File source;

    @Option(names = { "-o", "--output" }, description = "Output directory for the migrated files.", required = true)
    private File outputDir;

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

        MigrationEngine engine = new MigrationEngine();

        if (source.isDirectory()) {
            Files.walk(source.toPath())
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> migrateFile(engine, path, source.toPath(), outputDir.toPath()));
        } else {
            migrateFile(engine, source.toPath(), source.getParentFile().toPath(), outputDir.toPath());
        }

        System.out.println("Migration complete successfully!");
        return 0;
    }

    private void migrateFile(MigrationEngine engine, Path file, Path sourceRoot, Path outputRoot) {
        try {
            System.out.println("Processing: " + sourceRoot.relativize(file));
            String sourceCode = Files.readString(file);
            String migratedCode = engine.migrateCode(sourceCode);

            Path relativePath = sourceRoot.relativize(file);
            Path outputPath = outputRoot.resolve(relativePath);

            Files.createDirectories(outputPath.getParent());
            Files.writeString(outputPath, migratedCode);
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to compile or migrate: " + file.getFileName());
            System.err.println("Reason: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Better113CLI()).execute(args);
        System.exit(exitCode);
    }
}