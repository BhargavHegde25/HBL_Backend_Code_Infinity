package com.temenos.spotlight.dbp.idm.migrations.mojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "unify-idm-migrations", defaultPhase = LifecyclePhase.PROCESS_RESOURCES, threadSafe = true)
public class IDMMigrationsUnifierMojo extends AbstractMojo {

    private static final String SYSTEM_LINE_SEPERATOR = System.getProperty("line.separator");

    /** Instance of maven logger injected by plexus IOC */
    private Log log;

    @Parameter(required = true)
    private File scriptsDir;

    @Parameter(readonly = true, defaultValue = "${project.build.outputDirectory}/flyway-migrations")
    private File destDir;

    @Override
    public void setLog(Log log) {
        this.log = log;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (scriptsDir == null || !scriptsDir.exists() || !scriptsDir.isDirectory()) {
            throw new MojoFailureException(
                    "Path to scripts directory doesn't exists or is not a directory");
        }
        log.info("Generating migration scripts using scripts from directory: [" + scriptsDir.getAbsolutePath()
                + "]");

        // Clean up the previous generated migrations
        if (destDir.exists()) {
            log.info("Trying to Cleaning up destination directory: [" + destDir.getAbsolutePath()
                    + "] as it already exists.");
            if (!FileUtils.deleteQuietly(destDir)) {
                throw new MojoFailureException(
                        "Unable to cleanup following directory: [" + destDir.getAbsolutePath() +
                                "]. Re-run the plugin by manually deleting the directory.");
            }
        }

        // generation of migrations starts here
        generateDBMigrations();

        log.info("Successfully completed unifying database migrations");
    }

    private void generateDBMigrations() throws MojoFailureException {
        for (File dbTypeDir : scriptsDir.listFiles()) {
            if (dbTypeDir.isDirectory()) {
                log.info("Start processing of unifying migrations for database: [" + dbTypeDir.getName() + "]");

                File[] versionDirs = dbTypeDir.listFiles();
                for (File versionDir : versionDirs) {
                    // if versionDir is a directory, merge migrations
                    if (versionDir.isDirectory()) {
                        unifyMigrationsOfVersionDir(dbTypeDir, versionDir);
                    }

                    // if versionDir is an sql file, copy the sql file destination as-is. This functionality is to
                    // support older migration files.
                    if (versionDir.isFile() && versionDir.getName().endsWith(".sql")) {
                        copyMigrationFile(dbTypeDir, versionDir);
                    }
                }
                log.info("End of unifying migrations for database: [" + dbTypeDir.getName() + "]");
            }
        }

    }

    private void unifyMigrationsOfVersionDir(File dbTypeDir, File versionDir)
            throws MojoFailureException {
        log.info("Start unifying migrations for version: [" + versionDir.getName() + "]");
        File ddlIDMMigration = new File(versionDir, "ddl_keycloak.sql");
        File dmlIDMMigration = new File(versionDir, "dml_keycloak.sql");

        List<File> migrations = new ArrayList<>(); // list for preserving the order of sql files
        
        if (ddlIDMMigration.exists() && ddlIDMMigration.isFile()) {
            migrations.add(ddlIDMMigration);
        }

        if (dmlIDMMigration.exists() && dmlIDMMigration.isFile()) {
            migrations.add(dmlIDMMigration);
        }
        		

        String unifiedMigrationFileName = "V" + versionDir.getName() + "__migration.sql";
        File unifiedMigrationVersionDBDir = new File(destDir, dbTypeDir.getName());
        if (!unifiedMigrationVersionDBDir.exists()) {
            unifiedMigrationVersionDBDir.mkdirs();
        }

        // unify version migrations
        if (migrations != null && !migrations.isEmpty()) {
            try (OutputStreamWriter fileWriter =
                    new OutputStreamWriter(new FileOutputStream(new File(unifiedMigrationVersionDBDir, unifiedMigrationFileName), true), 
                    		StandardCharsets.UTF_8)) {
                for (File migration : migrations) {
                    try (FileInputStream fileInputStream = new FileInputStream(migration)) {
                        log.info("Unifying scripts of : [" + migration.getName() + "]" + " in migration: ["
                                + unifiedMigrationFileName);
                        fileWriter.append("-- " + migration.getName() + " starts")
                                .append(SYSTEM_LINE_SEPERATOR);
                        IOUtils.copy(fileInputStream, fileWriter, StandardCharsets.UTF_8);
                        fileWriter.append(SYSTEM_LINE_SEPERATOR).append(SYSTEM_LINE_SEPERATOR);
                    }
                }
            } catch (Exception e) {
                log.error("Failure while unifying migrations for version: [" + versionDir.getName() + "]");
                throw new MojoFailureException("Process failed while unifying migrations", e);
            }
        }
        log.info("End of unifying migrations for version: [" + versionDir.getName() + "]");

    }

    private void copyMigrationFile(File dbTypeDir, File migrationFile)
            throws MojoFailureException {
        log.info("Start copying migration file: [" + migrationFile.getName() + "]");
        File unifiedMigrationVersionDBDir = new File(destDir, dbTypeDir.getName());
        if (!unifiedMigrationVersionDBDir.exists()) {
            unifiedMigrationVersionDBDir.mkdirs();
        }
        try (OutputStreamWriter fileWriter =
                new OutputStreamWriter(new FileOutputStream(new File(unifiedMigrationVersionDBDir, migrationFile.getName())), 
                		StandardCharsets.UTF_8)) {
            try (FileInputStream fileInputStream = new FileInputStream(migrationFile)) {
                IOUtils.copy(fileInputStream, fileWriter, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("Failure while copying migration file: [" + migrationFile.getName() + "]");
            throw new MojoFailureException("Process failed while generating migrations", e);
        }
        log.info("End of copying migration file: [" + migrationFile.getName() + "]");
    }

}
