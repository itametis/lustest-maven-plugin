/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.event;

import java.util.HashMap;


/**
 * For each files in the project, stores the last modified timestamp. This is because on some file systems, unit tests
 * execution is so fast, the {@link com.itametis.maven.plugins.lustest.watcher.FileSystemWatcher} hasn't the time to
 * remove changed file from the event pool and so unit tests are executed twice. This class is a kind of timer letting
 * enough time to the event pool to remove old events.
 *
 * @author ITAMETIS ©
 */
public class TimeMaster {

    /**
     * The minimum (in milliseconds) to wait before it is possible to run another build process.
     */
    static final long MIN_DURATION_BETWEEN_TWO_BUILDS = 500;

    /**
     * By default, this plug-in expects to watch up-to 512 files (but this value increase if necessary).
     */
    private static final int DEFAULT_NUMBER_OF_WATCHED_FILES = 512;

    /**
     * The event monitored.
     * <ul>
     * <li>KEY => the relative path to the changed file.</li>
     * </li>VALUE => the last timestamp when the file changed.<li>
     * </ul>
     */
    private final HashMap<String, Long> events;


    public TimeMaster() {
        this.events = new HashMap<>(DEFAULT_NUMBER_OF_WATCHED_FILES, 1.0f);
    }


    /**
     * Determines (regarding the previous saved timestamp), whether or not the Lustest's process has to be ran (ie.
     * compilation + test compilation + test execution). Note that the whole process is replayed each time a file is
     * changed.
     *
     * @param file
     *             The changed file.
     *
     * @return true if the Lustest's process has to be replay.
     */
    public boolean hasToReplayProcess(String file) {
        boolean res = this.isValidFileFormat(file);

        if (res) {
            Long lastTimeStamp = this.events.get(file);

            res = lastTimeStamp == null || this.hasToRebuild(lastTimeStamp);
        }

        return res;
    }


    public void updateTimeStamp(String file) {
        this.events.put(file, System.currentTimeMillis());
    }


    private boolean hasToRebuild(long lastTimeStamp) {
        long elapsedTime = System.currentTimeMillis() - lastTimeStamp;

        return MIN_DURATION_BETWEEN_TWO_BUILDS < elapsedTime;
    }


    private boolean isValidFileFormat(String file) {
        return file != null && !file.isEmpty();
    }
}
