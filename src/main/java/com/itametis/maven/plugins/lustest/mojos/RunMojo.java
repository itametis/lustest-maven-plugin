/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.mojos;

import com.itametis.maven.plugins.lustest.task.SourceCompiler;
import com.itametis.maven.plugins.lustest.task.TestCompiler;
import com.itametis.maven.plugins.lustest.task.TestRunner;
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

    @Parameter
    private String sourceEncoding;

    @Parameter
    private String mavenCompilerVersion;

    @Parameter
    private String mavenSurefireVersion;

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject mavenProject;

    @Parameter(defaultValue = "${session}", readonly = true)
    private MavenSession mavenSession;

    @Component
    private BuildPluginManager pluginManager;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        SourceCompiler sourceCompiler = new SourceCompiler(this.mavenSession, this.pluginManager, this.sourceEncoding, this.mavenCompilerVersion);
        TestCompiler testCompiler = new TestCompiler(this.mavenSession, this.pluginManager, this.sourceEncoding, this.mavenCompilerVersion);
        TestRunner runner = new TestRunner(this.mavenSession, this.pluginManager, this.mavenSurefireVersion);

        sourceCompiler.compile();
        testCompiler.compile();
        runner.run();
//        FileSystemWatcher watcher = new FileSystemWatcher(super.getLog());
//        watcher.watch("./src");
//        watcher.startWatching();
    }
}
