/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.event;

import java.util.HashMap;


/**
 *
 * @author ITAMETIS ©
 */
public class TimeMaster {

    /**
     * The minimum (in milliseconds) to wait before it is possible to run another build process.
     */
    private static final long MIN_DURATION_BETWEEN_TWO_BUILDS = 500;

    private static final int DEFAULT_NUMBER_OF_WATCHED_FILES = 512;

    private HashMap<String, Long> eventSet;


    public TimeMaster() {
        this.eventSet = new HashMap<>(DEFAULT_NUMBER_OF_WATCHED_FILES, 1.0f);
    }


    public boolean hasToRebuild(String file) {
        Long lastTimeStamp = this.eventSet.get(file);

        return lastTimeStamp == null || this.hasToRebuild(lastTimeStamp);
    }


    public void updateTimeStamp(String file) {
        this.eventSet.put(file, System.currentTimeMillis());
    }


    private boolean hasToRebuild(long lastTimeStamp) {
        long elapsedTime = System.currentTimeMillis() - lastTimeStamp;

        return MIN_DURATION_BETWEEN_TWO_BUILDS < elapsedTime;
    }
}
