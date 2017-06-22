/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.watcher;

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
import org.apache.maven.plugin.logging.Log;


/**
 *
 * @author ITAMETIS ©
 */
public class FileSystemWatcher {

    private final WatchService watcher;

    private final HashMap<WatchKey, Path> keys;

    private final FileRegister fileRegister;

    private final Log mavenLogger;


    public FileSystemWatcher(Log logger) {
        this.mavenLogger = logger;

        this.keys = new HashMap<>();
        this.watcher = this.initWatcherService();

        this.fileRegister = new FileRegister(watcher, keys);
    }


    public void startWatching() {
        while (true) {
            // wait for key to be signalled
            WatchKey key = this.getKeyOfUpdatedFile();

            Path current = keys.get(key);
            if (this.isRegisteredKey(current)) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path child = this.getChangedChild(current, event);

                    // print out event
                    System.out.format("%s: %s\n", event.kind().name(), child);

                    this.addCreatedFiles(event, child);
                }

                if (this.isFileRemoved(key)) {
                    this.removeDeletedFileFromWatcher(key);
                }
            }
        }
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
            key = null;
            this.mavenLogger.error("Impossible to watch the file system. Plugin has to close", ex);
            System.exit(0);
        }

        return key;
    }


    private WatchService initWatcherService() {
        WatchService result;

        try {
            result = FileSystems.getDefault().newWatchService();
        }
        catch (IOException ex) {
            this.mavenLogger.error("Impossible to get the File System. Lustest has to stop", ex);
            result = null;
        }

        if (result == null) {
            this.mavenLogger.error("Lustest is stopped");
            System.exit(0);
        }

        return result;
    }


    private boolean isFileRemoved(WatchKey key) {
        return !key.reset();
    }


    private boolean isRegisteredKey(Path file) {
        return file != null;
    }


    public void removeDeletedFileFromWatcher(WatchKey key) {
        keys.remove(key);
    }


    public void watch(String filePath) {
        Path path = Paths.get(filePath);

        this.watch(path);
    }


    public void watch(Path path) {
        try {
            this.fileRegister.registerPathInWatcherRecursively(path);
        }
        catch (IOException ex) {
            this.mavenLogger.error("Impossible to add the specified path to the watcher", ex);
        }
    }
}
