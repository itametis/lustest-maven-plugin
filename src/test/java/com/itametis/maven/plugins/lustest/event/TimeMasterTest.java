/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.event;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;


/**
 * @author ITAMETIS ©
 */
public class TimeMasterTest {

    // TimeMaster::hasToReplayProcess(String)
    @Test
    public void hasToReplaySHOULDreturnTrueForNewFile() {
        // Given
        TimeMaster timeMaster = new TimeMaster();
        String file = "simulate/recently/created/file";

        // When
        boolean hasToReplay = timeMaster.hasToReplayProcess(file);

        // Then
        Assertions.assertThat(hasToReplay).isTrue();
    }


    @Test
    public void hasToReplaySHOULDreturnFalseForRecentlyUpdatedFile() {
        // Given
        TimeMaster timeMaster = new TimeMaster();
        String file = "simulate/recently/updated/file";

        // When
        timeMaster.updateTimeStamp(file);
        boolean hasToReplay = timeMaster.hasToReplayProcess(file);

        // Then
        Assertions.assertThat(hasToReplay).isFalse();
    }


    @Test
    @Ignore("This test create instability in JUnit. It remains skipped until a solution is found.")
    public void hasToReplaySHOULDreturnTrueForFileUpdatedAfter750msOfInterval() {
        // Given
        TimeMaster timeMaster = new TimeMaster();
        String file = "simulate/recently/updated/file";

        // When
        timeMaster.updateTimeStamp(file);
        boolean hasToReplay;
        try {
            Thread.sleep(TimeMaster.MIN_DURATION_BETWEEN_TWO_BUILDS + 1);
            hasToReplay = timeMaster.hasToReplayProcess(file);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
            hasToReplay = false;
        }

        // Then
        Assertions.assertThat(hasToReplay).isTrue();
    }


    @Test
    public void hasToReplaySHOULDreturnFalseForNullPath() {
        // Given
        TimeMaster timeMaster = new TimeMaster();
        String file = null;

        // When
        boolean hasToReplay = timeMaster.hasToReplayProcess(file);

        // Then
        Assertions.assertThat(hasToReplay).isFalse();
    }


    @Test
    public void hasToReplaySHOULDreturnFalseForEmptyPath() {
        // Given
        TimeMaster timeMaster = new TimeMaster();
        String file = "";

        // When
        boolean hasToReplay = timeMaster.hasToReplayProcess(file);

        // Then
        Assertions.assertThat(hasToReplay).isFalse();
    }
}
