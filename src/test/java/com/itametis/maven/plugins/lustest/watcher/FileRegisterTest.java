/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import org.assertj.core.api.Assertions;
import org.junit.Test;


/**
 * @author ITAMETIS ©
 */
public class FileRegisterTest {

    // FileRegister::registerPathInWatcherRecursively(Path)
    @Test
    public void registerPathInWatcherRecursivelyShouldOnlyAddTheDirectoryIntoTheWatchingSet() throws IOException {
        // Given
        WatchService watcher = this.initWatcherService();
        HashMap<WatchKey, Path> keys = new HashMap<>(2);
        String dirPath = "src/test/resources/watcher/dir_with_one_file/";

        // When
        FileRegister register = new FileRegister(watcher, keys);
        register.registerPathInWatcherRecursively(Paths.get(dirPath));

        // Then
        Assertions.assertThat(keys.size()).isEqualTo(1);

        // Clean
        watcher.close();
    }


    @Test
    public void registerPathInWatcherRecursivelyShouldOnlyAddTheDirectoryIntoTheWatchingSetWithMultipleFilesIntoIt() throws IOException {
        // Given
        WatchService watcher = this.initWatcherService();
        HashMap<WatchKey, Path> keys = new HashMap<>(2);
        String dirPath = "src/test/resources/watcher/dir_with_multiple_files/";

        // When
        FileRegister register = new FileRegister(watcher, keys);
        register.registerPathInWatcherRecursively(Paths.get(dirPath));

        // Then
        Assertions.assertThat(keys.size()).isEqualTo(1);

        // Clean
        watcher.close();
    }


    @Test
    public void registerPathInWatcherRecursivelyShouldAddAllDirectoryButIgnoreFiles() throws IOException {
        // Given
        WatchService watcher = this.initWatcherService();
        HashMap<WatchKey, Path> keys = new HashMap<>(3);
        String dirPath = "src/test/resources/watcher/real_case/";

        // When
        FileRegister register = new FileRegister(watcher, keys);
        register.registerPathInWatcherRecursively(Paths.get(dirPath));

        // Then
        Assertions.assertThat(keys.size()).isEqualTo(2);

        // Clean
        watcher.close();
    }


    private WatchService initWatcherService() {
        WatchService result;

        try {
            result = FileSystems.getDefault().newWatchService();
        }
        catch (IOException ex) {
            result = null;
        }

        return result;
    }
}
