/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.task;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;


/**
 * Class dedicated to compile changed sources using incremental build.
 *
 * @author ITAMETIS ©
 */
public class TestCompiler extends Compiler {

    public TestCompiler(MavenSession mavenSession, BuildPluginManager pluginManager, String sourceEncoding, String compilerVersion) {
        super(mavenSession, pluginManager, "testCompile", sourceEncoding, compilerVersion);
    }
}
