/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.task;

//import java.io.File;
//import java.io.IOException;
//import java.nio.file.FileVisitOption;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Comparator;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

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
 *
 * @author ITAMETIS ©
 */
public class TestRunner {

    private final MavenSession mavenSession;

    private final BuildPluginManager pluginManager;

    private final String mavenSurefireVersion;

    private final MavenProject mavenProject;

    private int nbRuns = 0;


    public TestRunner(MavenSession mavenSession, BuildPluginManager pluginManager, MavenProject mavenProject, String mavenSurefireVersion) {
        this.mavenSession = mavenSession;
        this.pluginManager = pluginManager;
        this.mavenProject = mavenProject;
        this.mavenSurefireVersion = mavenSurefireVersion;
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
                element(name("argLine"), "-DnbLustestRuns=" + this.nbRuns++)
            ),
            executionEnvironment(
                this.mavenSession,
                this.pluginManager
            )
        );
    }
}
