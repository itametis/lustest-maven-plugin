/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.task;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;


/**
 * Class dedicated to compile changed test sources using incremental build.
 *
 * @author ITAMETIS ©
 */
public class SourceCompiler extends Compiler {

    public SourceCompiler(MavenSession mavenSession, BuildPluginManager pluginManager, String sourceEncoding, String compilerVersion) {
        super(mavenSession, pluginManager, "compile", sourceEncoding, compilerVersion);
    }
}
