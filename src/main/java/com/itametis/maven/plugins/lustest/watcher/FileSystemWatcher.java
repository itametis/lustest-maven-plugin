/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.watcher;

import com.itametis.maven.plugins.lustest.event.TimeMaster;
import com.itametis.maven.plugins.lustest.task.SourceCompiler;
import com.itametis.maven.plugins.lustest.task.TestCompiler;
import com.itametis.maven.plugins.lustest.task.TestRunner;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;


/**
 * The class that watch the file system.
 *
 * @author ITAMETIS ©
 */
public class FileSystemWatcher {

    /**
     * The Maven logger.
     */
    private final Log mavenLogger;

    /**
     * The Java Object watching the file system.
     */
    private final WatchService watcher;

    /**
     * Data structure help to retrieve easily {@link Path} object from file system event (call WatchKey).
     */
    private final HashMap<WatchKey, Path> keys;

    /**
     * The library adding file to the watching file set.
     */
    private final FileRegister fileRegister;

    /**
     * Determine whether or not the daemon is running.
     */
    private boolean isRunning;


    /**
     * Initializes this watcher with a Maven plug-in logger.
     *
     * @param logger
     *               The logger of Maven.
     */
    public FileSystemWatcher(Log logger) {
        this.mavenLogger = logger;

        this.keys = new HashMap<>();
        this.watcher = this.initWatcherService();

        this.fileRegister = new FileRegister(watcher, keys);

        this.isRunning = false;
    }


    /**
     * Starts the file system watcher.
     *
     * @param sourceCompiler
     *                       Instance knowing how to compile sources.
     * @param testCompiler
     *                       Instance knowing how to compile tests.
     * @param runner
     *                       Instance knowing how to run unit tests.
     */
    public void startWatching(SourceCompiler sourceCompiler, TestCompiler testCompiler, TestRunner runner) {
        if (!this.isRunning) {

            this.isRunning = true;
            TimeMaster timeMaster = new TimeMaster();

            while (this.isRunning) {
                // wait for key to be signalled
                WatchKey key = this.getKeyOfUpdatedFile();

                Path current = keys.get(key);
                if (this.isRegisteredKey(current)) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path child = this.getChangedChild(current, event);

                        String eventFilePath = child.toString();

                        if (this.isFileEventNeverHandledBefore(event) && timeMaster.hasToRebuild(eventFilePath)) {
                            this.addCreatedFiles(event, child);
                            this.processFile(sourceCompiler, testCompiler, runner, child);
                            timeMaster.updateTimeStamp(eventFilePath);
                        }
                    }

                    if (this.isFileRemoved(key)) {
                        this.removeDeletedFileFromWatcher(key);
                    }
                }
            }
        }
    }


    /**
     * Stops this watching service.
     */
    public void stopWatching() {
        this.isRunning = false;
    }


    /**
     * Adds the specified path to the watching file set.
     *
     * @param path
     *             The path representation of the file or folder to watch.
     */
    public void watch(Path path) {
        try {
            this.fileRegister.registerPathInWatcherRecursively(path);
        }
        catch (IOException ex) {
            this.mavenLogger.error("Impossible to add the specified path to the watcher", ex);
        }
    }


    /**
     * Adds the specified file or folder to the watching file set.
     *
     * @param filePath
     *                 The absolute or relative path to watch.
     *
     * @see FileSystemWatcher#watch(java.nio.file.Path)
     */
    public void watch(String filePath) {
        Path path = Paths.get(filePath);

        this.watch(path);
    }


    private void addCreatedFiles(WatchEvent<?> event, Path child) {
        @SuppressWarnings("rawtypes")
        WatchEvent.Kind kind = event.kind();

        // if directory is created, and watching recursively, then register it and its sub-directories
        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
            if (Files.isDirectory(child)) {
                this.watch(child);
            }
        }
    }


    @SuppressWarnings("unchecked")
    private Path getChangedChild(Path currentPath, WatchEvent<?> event) {
        return currentPath.resolve(((WatchEvent<Path>) event).context());
    }


    @SuppressWarnings("UnusedAssignment")
    private WatchKey getKeyOfUpdatedFile() {
        WatchKey key;
        try {
            key = watcher.take();
        }
        catch (InterruptedException ex) {
            key = this.stopPlugin("Impossible to get event from the file system", ex);
        }

        return key;
    }


    private WatchService initWatcherService() {
        WatchService result;

        try {
            result = FileSystems.getDefault().newWatchService();
        }
        catch (IOException ex) {
            result = this.stopPlugin("Impossible to watch the File System", ex);
        }

        return result;
    }


    private boolean isFileEventNeverHandledBefore(WatchEvent<?> event) {
        return event.count() == 1;
    }


    private boolean isFileRemoved(WatchKey key) {
        return !key.reset();
    }


    private boolean isRegisteredKey(Path file) {
        return file != null;
    }


    private void processFile(SourceCompiler sourceCompiler, TestCompiler testCompiler, TestRunner runner, Path path) {
        if (!Files.isDirectory(path)) {
            try {
                sourceCompiler.compile();
                testCompiler.compile();
                runner.run();
            }
            catch (MojoExecutionException ex) {
                this.mavenLogger.debug("Error while executing compilation mojo : ", ex);
            }
        }
    }


    private void removeDeletedFileFromWatcher(WatchKey key) {
        keys.remove(key);
    }


    private <T> T stopPlugin(String message, Throwable throwable) {
        this.mavenLogger.error(message, throwable);
        this.mavenLogger.error("Lustest is stopped");

        System.exit(1);
        return null;
    }
}
