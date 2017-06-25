/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.task;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;

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
public class Compiler {

    private final MavenSession mavenSession;

    private final BuildPluginManager pluginManager;

    private final String sourceEncoding;

    private final String compilerVersion;

    private final String goal;


    public Compiler(MavenSession mavenSession, BuildPluginManager pluginManager, String goal, String sourceEncoding, String compilerVersion) {
        this.mavenSession = mavenSession;
        this.pluginManager = pluginManager;
        this.sourceEncoding = sourceEncoding;
        this.compilerVersion = compilerVersion;
        this.goal = goal;
    }


    public void compile() throws MojoExecutionException {
        executeMojo(
            plugin(
                groupId("org.apache.maven.plugins"),
                artifactId("maven-compiler-plugin"),
                version(this.compilerVersion)
            ),
            goal(this.goal),
            configuration(
                element(name("encoding"), this.sourceEncoding)
            ),
            executionEnvironment(
                this.mavenSession,
                this.pluginManager
            )
        );
    }

}
