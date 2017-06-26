/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.task;

import java.util.Map;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;

import static org.twdata.maven.mojoexecutor.MojoExecutor.artifactId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executeMojo;
import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;
import static org.twdata.maven.mojoexecutor.MojoExecutor.goal;
import static org.twdata.maven.mojoexecutor.MojoExecutor.groupId;
import static org.twdata.maven.mojoexecutor.MojoExecutor.name;
import static org.twdata.maven.mojoexecutor.MojoExecutor.plugin;
import static org.twdata.maven.mojoexecutor.MojoExecutor.version;


/**
 * This class processes to run the unit tests.
 *
 * @author ITAMETIS ©
 */
public class TestRunner {

    private final PluginDescriptor descriptor;

    private final MavenSession mavenSession;

    private final BuildPluginManager pluginManager;

    private final String mavenSurefireVersion;

    private int nbRuns = 0;


    public TestRunner(MavenSession mavenSession, BuildPluginManager pluginManager, String mavenSurefireVersion) {
        this.mavenSession = mavenSession;
        this.pluginManager = pluginManager;
        this.mavenSurefireVersion = mavenSurefireVersion;
        this.descriptor = this.buildPluginDescriptor();
    }


    public void run() throws MojoExecutionException {
        executeMojo(
            plugin(
                groupId("org.apache.maven.plugins"),
                artifactId("maven-surefire-plugin"),
                version(this.mavenSurefireVersion)
            ),
            goal("test"),
            configuration(
                // 'nbRuns++' is kept in the case where the removing of previous execution does not work :
                element(name("argLine"), "-DnbLustestRuns=" + this.nbRuns++)
            ),
            executionEnvironment(
                this.mavenSession,
                this.pluginManager
            )
        );

        this.removePreviousExecutionFromSurefireHistory();
    }


    private PluginDescriptor buildPluginDescriptor() {
        PluginDescriptor pluginDescriptor = new PluginDescriptor();

        pluginDescriptor.setGroupId("org.apache.maven.plugins");
        pluginDescriptor.setArtifactId("maven-surefire-plugin");
        pluginDescriptor.setVersion(this.mavenSurefireVersion);

        return pluginDescriptor;
    }


    /**
     * Removes the Surefire hash of just executed build. The reason is Surefire generate a hash each time it is executed
     * in order to prevent multiple executions of Surefire. But in our case, we need to execute Surefire multiple times
     * in the same Maven instance. So if we let Surefire adding hashes to the Maven plug-in context {@link Map}, it
     * risks to produce an {@link OutOfMemoryError} (ok, the risk is very very small but it's still a risk). That's why
     * we have to delete the hash.
     */
    private void removePreviousExecutionFromSurefireHistory() {
        Map<String, Object> context = this.mavenSession.getPluginContext(
            this.descriptor,
            this.mavenSession.getCurrentProject()
        );

        String keyToRemove = null;
        for (String key : context.keySet()) {
            if (this.isSurefireBuildHash(key, context)) {
                keyToRemove = key;
                break;
            }
        }

        if (keyToRemove != null) {
            context.remove(keyToRemove);
        }
    }


    private boolean isSurefireBuildHash(String key, Map<String, Object> context) {
        return key.length() == 40 && key.equals(context.get(key));
    }

}
