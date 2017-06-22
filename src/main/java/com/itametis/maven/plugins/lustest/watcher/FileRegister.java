/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.watcher;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;


/**
 * Add a file or a folder (and recursively all its content) into the set of watched files.
 *
 * @author ITAMETIS ©
 */
public class FileRegister {

    private final SimpleFileVisitor<Path> treeWalker;


    public FileRegister(WatchService watcher, Map<WatchKey, Path> keys) {
        this.treeWalker = this.buildTreeWalker(watcher, keys);
    }


    /**
     * Adds the specified file or folder, represented as a {@link Path} into the set of watched files. If the given
     * {@link Path} is a a folder, this method will process a recursive scan to ass all the content stored into it.
     *
     * @param root
     *             The path to watch.
     *
     * @throws IOException
     */
    public void registerPathInWatcherRecursively(Path root) throws IOException {
        Files.walkFileTree(root, this.treeWalker);
    }


    private SimpleFileVisitor<Path> buildTreeWalker(WatchService watcher, Map<WatchKey, Path> keys) {
        return new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                WatchKey key = dir.register(
                    watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY
                );
                keys.put(key, dir);

                return FileVisitResult.CONTINUE;
            }
        };
    }
}
