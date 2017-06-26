/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.mojos;

import com.itametis.maven.plugins.lustest.task.SourceCompiler;
import com.itametis.maven.plugins.lustest.task.TestCompiler;
import com.itametis.maven.plugins.lustest.task.TestRunner;
import com.itametis.maven.plugins.lustest.watcher.FileSystemWatcher;
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

    /**
     * Contains the list of folders to watch. If empty watches recursively the folder './src' by default.
     */
    @Parameter(
        defaultValue = "src",
        readonly = true
    )
    private String[] foldersToWatch;

    /**
     * The encoding of your sources. By default 'UTF-8' is used.
     */
    @Parameter
    private String sourceEncoding;

    /**
     * The version of the maven-compiler-plugin to use. This should be the one you defined in your pom (and you really
     * should define it in your pom). If version is specified, this plug-in will use the 3.6.1.
     */
    @Parameter(
        defaultValue = "3.6.1"
    )
    private String mavenCompilerVersion;

    /**
     * The version of the maven-surefire-plugin to use. This should be the one you defined in your pom (and you really
     * should define it in your pom). If version is specified, this plug-in will use the 2.20.
     */
    @Parameter(
        defaultValue = "2.20"
    )
    private String mavenSurefireVersion;

    /**
     * The current Maven project object. Not used yet but it will for legacy purpose.
     */
//    @Parameter(
//        defaultValue = "${project}",
//        readonly = true
//    )
//    private MavenProject mavenProject;
    /**
     * The current Maven session (to get the current Maven instance and to avoid the creation of new one for each run).
     */
    @Parameter(
        defaultValue = "${session}",
        readonly = true
    )
    private MavenSession mavenSession;

    /**
     * Plugin used by MojoExecutor to run others plug-ins from this one.
     */
    @Component
    private BuildPluginManager pluginManager;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        // Init compiler :
        SourceCompiler sourceCompiler = new SourceCompiler(this.mavenSession, this.pluginManager, this.sourceEncoding, this.mavenCompilerVersion);
        TestCompiler testCompiler = new TestCompiler(this.mavenSession, this.pluginManager, this.sourceEncoding, this.mavenCompilerVersion);
        TestRunner runner = new TestRunner(this.mavenSession, this.pluginManager, this.mavenSurefireVersion);

        // Process the first run :
        this.processFirstBuild(sourceCompiler, testCompiler, runner);

        // Start the watching mod :
        FileSystemWatcher watcher = new FileSystemWatcher(super.getLog());
        this.addWatchedFolder(watcher);
        watcher.startWatching(sourceCompiler, testCompiler, runner);
    }


    private void processFirstBuild(SourceCompiler sourceCompiler, TestCompiler testCompiler, TestRunner runner) {
        try {
            sourceCompiler.compile();
            testCompiler.compile();
            runner.run();
        }
        catch (MojoExecutionException ex) {
            super.getLog().debug("Error while executing compilation mojo : ", ex);
        }
    }


    private void addWatchedFolder(FileSystemWatcher watcher) {
        super.getLog().info("Watched folder :");
        for (String folder : this.foldersToWatch) {
            super.getLog().info("    - " + folder);
            watcher.watch(folder);
        }
    }
}
