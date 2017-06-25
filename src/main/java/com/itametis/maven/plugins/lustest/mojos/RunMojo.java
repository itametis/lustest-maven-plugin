/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.mojos;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
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
 * Start the watching service.
 *
 * @author © ITAMETIS
 */
@Mojo(
    aggregator = true,
    name = "run",
    defaultPhase = LifecyclePhase.TEST,
    requiresDependencyResolution = ResolutionScope.TEST
)
public class RunMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (mavenProject == null) {
            System.out.println("PROJECT NULL");
        }
        else {
            System.out.println("OK");
        }

        if (mavenSession == null) {
            System.out.println("SESSION NULL");
        }
        else {
            System.out.println("OK");
        }

        if (pluginManager == null) {
            System.out.println("MANAGER NULL");
        }
        else {
            System.out.println("OK");
        }

        executeMojo(
            plugin(
                groupId("org.apache.maven.plugins"),
                artifactId("maven-compiler-plugin"),
                version("3.6.1")
            ),
            goal("compile"),
            configuration(
                element(name("encoding"), "UTF-8")
            ),
            executionEnvironment(
                this.mavenSession,
                this.pluginManager
            )
        );

        executeMojo(
            plugin(
                groupId("org.apache.maven.plugins"),
                artifactId("maven-compiler-plugin"),
                version("3.6.1")
            ),
            goal("testCompile"),
            configuration(
                element(name("encoding"), "UTF-8")
            ),
            executionEnvironment(
                this.mavenSession,
                this.pluginManager
            )
        );

        executeMojo(
            plugin(
                groupId("org.apache.maven.plugins"),
                artifactId("maven-surefire-plugin"),
                version("2.20")
            ),
            goal("test"),
            configuration(
                element(name("argLine"), "")
            ),
            executionEnvironment(
                this.mavenSession,
                this.pluginManager
            )
        );

//        executeMojo(
//            plugin(
//                groupId("org.apache.maven.plugins"),
//                artifactId("maven-dependency-plugin"),
//                version("3.0.1")
//            ),
//            goal("copy-dependencies"),
//            configuration(
//                element(name("outputDirectory"), "target/dependencies")
//            ),
//            executionEnvironment(
//                mavenSession,
//                pluginManager
//            )
//        );
//        FileSystemWatcher watcher = new FileSystemWatcher(super.getLog());
//        watcher.watch("./src");
//        watcher.startWatching();
    }
}
